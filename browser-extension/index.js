console.log("Hey this loaded");

function getEmailContent() {
    const selectors = [".h7", ".a3s.aiL", "gmail_quote", '[role="presentation"]'];
    for (const selector of selectors) {
        const emailBody = document.querySelector(selector);
        if (emailBody) {
            return emailBody.innerText;
        }
    }
    return null;
}

function injectButton() {
    // Remove existing button if present
    document.querySelector(".ai-assistant-button")?.remove();

    const container = document.querySelector(".btC");
    if (!container) return;

    const button = document.createElement("button");
    button.innerText = "AI Assistant";
    button.className = "ai-assistant-button T-I J-J5-Ji aoO v7 T-I-atl L3";
    button.style.marginInline = "8px";

    // Insert as second child
    if (container.children.length >= 1) {
        container.insertBefore(button, container.children[1]);
    } else {
        container.appendChild(button);
    }

    // Add click listener only once
    button.addEventListener('click', async () => {
        console.log("AI Assistant button clicked");

        try {
            button.innerText = "Loading...";
            const apiResponse = await fetch('http://localhost:8080/api/email/generate', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                     },
                body: JSON.stringify({
                    emailContent: getEmailContent()?.trim(),
                    tone: "professional",
                })
            });

            if (!apiResponse.ok) {
                throw new Error("Email Response generation failed");
            }

            const emailResponse = await apiResponse.text();
            console.log(emailResponse);

            const composeBox = document.querySelector('[role="textbox"][g_editable="true"]');
            if (!composeBox) {
                console.error("Compose box not found");
                return;
            }

            composeBox.focus();
            document.execCommand('insertText', false, emailResponse);

        } catch (error) {
            console.error("Error:", error?.message);
        } finally {
            button.innerText = "AI Assistant";
        }
    });
}

// Observe Gmail compose window
const observer = new MutationObserver((mutations) => {
    for (const mutation of mutations) {
        const addedNodes = Array.from(mutation.addedNodes);
        const hasComposeElement = addedNodes.some(node =>
            node.nodeType === Node.ELEMENT_NODE &&
            (node.matches('.btC,.aDh,[role="dialog"]') || node.querySelector('.btC,.aDh,[role="dialog"]'))
        );

        if (hasComposeElement) {
            console.log("Compose window opened");
            setTimeout(injectButton, 500);
        }
    }
});

observer.observe(document.body, { childList: true, subtree: true });

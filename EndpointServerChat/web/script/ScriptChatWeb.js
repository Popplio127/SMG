let username = "Utente Young";
const wsProtocol = location.protocol === "https:" ? "wss" : "ws";
const wsUrl = `${wsProtocol}://${location.host}/ChatServer/chat/${username}`;
const chatDiv = document.getElementById("chat");
const sendBtn = document.getElementById("sendBtn");
const msgInput = document.getElementById("msgInput");
let ws;

function addMessage(text, type, sender) {
    if (!chatDiv)
        return;
    const div = document.createElement("div");
    if (sender === username) {
        div.className = "message right";
    } else if (type === "server") {
        div.className = "message server";
    } else {
        div.className = "message left";
    }
    div.textContent = text;
    chatDiv.appendChild(div);
    chatDiv.scrollTop = chatDiv.scrollHeight;
}

function inviaMessaggio() {
    const text = msgInput.value.trim();
    if (text.length > 0 && username) {
        const message = {
            type: "0002",
            from: username,
            to: "",
            content: text
        };
        ws.send(JSON.stringify(message));
        addMessage(username + ": " + text, "chat", username);
        msgInput.value = "";
    } else if (!username) {
        addMessage("⚠ Nessun dispositivo connesso!", "server");
    }
}

if (chatDiv) {
    ws = new WebSocket(wsUrl);
    ws.onopen = () => {
        msgInput.disabled = false;
        sendBtn.disabled = false;
        addMessage("Connesso al server WebSocket", "server");
    };

    ws.onmessage = (event) => {
        try {
            const msg = JSON.parse(event.data);
            switch (msg.type) {
                case "0002":
                    const from = msg.from;
                    const text = msg.content;
                    addMessage(from + ": " + text, "chat", from);
                    break;
                default:
                    addMessage("Tipo di messaggio non approvato per questo client!", "chat", "server");
            }
        } catch (err) {
            console.error("Errore nel parsing del messaggio:", err);
        }
    };

    ws.onclose = () => addMessage("Connessione chiusa", "server");
    ws.onerror = (err) => {
        console.error("WebSocket error:", err);
        addMessage("Errore di connessione", "server");
    };
    sendBtn.addEventListener("click", inviaMessaggio);
    msgInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter" && !msgInput.disabled) {
            event.preventDefault();
            inviaMessaggio();
        }
    });
}
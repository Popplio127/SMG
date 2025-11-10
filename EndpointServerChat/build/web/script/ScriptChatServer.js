let username = "ServerMonitor";
const wsProtocol = location.protocol === "https:" ? "wss" : "ws";
const wsUrl = `${wsProtocol}://${location.host}/ChatServer/chat/${username}`;
const chatDiv = document.getElementById("chat");
const sendBtn = document.getElementById("sendBtn");
const msgInput = document.getElementById("msgInput");
const showQrBtn = document.getElementById("showQrBtn");
const qrImg = document.getElementById("qr");
let ws;

function addMessage(text, type, sender) {
    if (!chatDiv) return;
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

if (chatDiv) {
    ws = new WebSocket(wsUrl);

    ws.onopen = () => {
        addMessage("Connesso al server WebSocket", "server");
        const initMessage = {
            type: "0001",
            from: username,
            to: "",
            content: "Richiesta sessione"
        };
        ws.send(JSON.stringify(initMessage));
    };
    ws.onmessage = (event) => {
        try {
            const msg = JSON.parse(event.data);
            switch (msg.type) {
                case "0001":
                    generaQr(msg.content);
                    break;
                case "0002":
                    const from = msg.from;
                    const text = msg.content;
                    addMessage(from + ": " + text, "chat", from);
                    break;
                case "0003":
                    username = msg.from; 
                    addMessage("Connessione avvenuta con successo! Benvenuto " + username, "server");
                    break;
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
    sendBtn.addEventListener("click", () => {
        const text = msgInput.value.trim();
        if (text.length > 0) {
            const message = {
                type: "0002",
                from: username,
                to: "",
                content: text
            };
            ws.send(JSON.stringify(message));
            addMessage(username + ": " + text, "chat", username);
            msgInput.value = "";
        }
    });
}

function generaQr(sessionId) {
    if (!qrImg || !sessionId)
        return;
    const url = "https://itismagistri.ddns.net/qr/app/?s=" + sessionId + "&size=500";
    qrImg.src = url;
}

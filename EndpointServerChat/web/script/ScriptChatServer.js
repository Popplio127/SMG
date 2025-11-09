const username = "ServerMonitor";
const wsProtocol = location.protocol === "https:" ? "wss" : "ws";
const wsUrl = `${wsProtocol}://${location.host}/ChatServer/chat/${username}`;
const chatDiv = document.getElementById("chat");
let ws;

function addMessage(text, type) {
    if (!chatDiv)
        return;
    const p = document.createElement("div");
    p.className = "message " + type;
    p.textContent = text;
    chatDiv.appendChild(p);
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
            if (msg.type === "0001") {
                sessionStorage.setItem("sessionId", msg.content);
                console.log("Sessione valida ricevuta dal server:", msg.content);
            } else if (msg.type === "0002") {
                const from = msg.from || "sconosciuto";
                const text = msg.content || "";
                addMessage(`${from}: ${text}`, from === "Server" ? "server" : "user");
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

    document.getElementById("sendBtn").addEventListener("click", () => {
        const text = document.getElementById("msgInput").value.trim();
        if (text.length > 0) {
            const message = {
                type: "0002",
                from: username,
                to: "",
                content: text
            };
            ws.send(JSON.stringify(message));
            document.getElementById("msgInput").value = "";
        }
    });
}

document.getElementById("showQrBtn").addEventListener("click", () => {
    const sessionId = sessionStorage.getItem("sessionId");
    if (!sessionId) {
        alert("Sessione non disponibile, attendi connessione al server!");
        return;
    }
    window.open("qr.html?s=" + sessionId, "_blank");
});

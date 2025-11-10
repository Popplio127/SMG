let username = "ServerMonitorWeb";
let connectedUser = null;
const wsProtocol = location.protocol === "https:" ? "wss" : "ws";
const wsUrl = `${wsProtocol}://${location.host}/ChatServer/chat/${username}`;
const chatDiv = document.getElementById("chat");
const sendBtn = document.getElementById("sendBtn");
const msgInput = document.getElementById("msgInput");
const header = document.getElementById("header");

let ws = new WebSocket(wsUrl);

function addMessage(text, type, sender) {
    const div = document.createElement("div");
    if (type === "server") {
        div.className = "message server";
    } else if (sender === connectedUser) {
        div.className = "message right";
    } else {
        div.className = "message left";
    }
    div.textContent = text;
    chatDiv.appendChild(div);
    chatDiv.scrollTop = chatDiv.scrollHeight;
}

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
                addMessage("Sessione generata. Attendere connessione dispositivo...", "server");
                generaQr(msg.content);
                break;
            case "0002":
                const from = msg.from;
                const text = msg.content;
                addMessage(from + ": " + text, "chat", from);
                break;
            case "0003":
                connectedUser = msg.from;
                username = msg.from;
                header.textContent = "Chat con " + connectedUser;
                addMessage("Connessione avvenuta con successo! Benvenuto " + connectedUser, "server");
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
    if (text.length > 0 && connectedUser) {
        const message = {
            type: "0002",
            from: connectedUser,
            to: "",
            content: text
        };
        ws.send(JSON.stringify(message));
        addMessage(connectedUser + ": " + text, "chat", connectedUser);
        msgInput.value = "";
    } else if (!connectedUser) {
        addMessage("⚠Nessun dispositivo connesso!", "server");
    }
});

function generaQr(sessionId) {
    const url = "https://itismagistri.ddns.net/qr/app/?s=" + sessionId + "&size=300";
    const popup = window.open("", "QR Code", "width=350,height=400");
    popup.document.write("<h3>Scansiona questo codice QR con l'app</h3>");
    popup.document.write("<img src='" + url + "' alt='QR Code'>");
}

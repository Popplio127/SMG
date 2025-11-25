let username = "ServerMonitor";
let connectedUser = null;
const wsProtocol = location.protocol === "https:" ? "wss" : "ws";
const wsUrl = `${wsProtocol}://${location.host}/ChatServer/chat/${username}`;
const chatDiv = document.getElementById("chat");
const sendBtn = document.getElementById("sendBtn");
const msgInput = document.getElementById("msgInput");
const qrImg = document.getElementById("qr");
let ws;

function addMessage(text, type, sender) {
    if (!chatDiv)
        return;

    const div = document.createElement("div");

    if (type === "center") {
        div.className = "message center";
    } else if (sender === connectedUser) {
        div.className = "message right";
    } else {
        div.className = "message left";
    }

    div.textContent = text;
    chatDiv.appendChild(div);
    chatDiv.scrollTop = chatDiv.scrollHeight;
}


function generaQr(sessionId) {
    if (!qrImg || !sessionId)
        return;
    const url = "https://itismagistri.ddns.net/qr/app/?s=" + sessionId + "&size=500";
    qrImg.src = url;
}

function toggleInput(state) {
    msgInput.disabled = !state;
    sendBtn.disabled = !state;
}

function inviaMessaggio() {
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
        addMessage("⚠ Nessun dispositivo connesso!", "center");
    }
}

if (chatDiv) {
    ws = new WebSocket(wsUrl);

    ws.onopen = () => {
        addMessage("Connesso al server WebSocket", "center");
        const initMessage = {
            type: "0001",
            from: username,
            to: "",
            content: "Richiesta sessione"
        };
        ws.send(JSON.stringify(initMessage));
        if (username === "ServerMonitor")
            toggleInput(false);
    };

    ws.onmessage = (event) => {
        try {
            const msg = JSON.parse(event.data);
            switch (msg.type) {
                case "0001":
                    addMessage("Sessione generata. Attendere connessione dispositivo...", "center");
                    generaQr(msg.content);
                    break;

                case "0002":
                    const from = msg.from;
                    const text = msg.content;
                    addMessage(from + ": " + text, "chat", from);
                    break;

                case "0003":
                    connectedUser = msg.from;
                    addMessage("Connessione avvenuta con successo! Utente connesso: " + connectedUser, "center");
                    toggleInput(true);
                    break;

                case "0004":
                    addMessage(msg.content, "center");
                    break;
            }
        } catch (err) {
            console.error("Errore nel parsing del messaggio:", err);
        }
    };

    ws.onclose = () => addMessage("Connessione chiusa", "server");
    ws.onerror = (err) => {
        console.error("WebSocket error:", err);
        addMessage("Errore di connessione", "center ");
    };
    sendBtn.addEventListener("click", inviaMessaggio);
    msgInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter" && !msgInput.disabled) {
            event.preventDefault();
            inviaMessaggio();
        }
    });
}
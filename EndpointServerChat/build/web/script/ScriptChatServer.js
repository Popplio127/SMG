const username = "ServerMonitor";
const wsUrl = "ws//localhost:8080/ChatServer/chat/" + username;
const ws = new WebSocket(wsUrl);
const chatDiv = document.getElementById("chat");

ws.onopen = () => {
    addMessage("Connesso al server WebSocket", "server");
};

ws.onmessage = (event) => {
    const msg = JSON.parse(event.data);
    const from = msg.from || "sconosciuto";
    const text = msg.content || "";
    addMessage(from + ": " + text, from === "Server" ? "server" : "user");
};

ws.onclose = () => addMessage("Connessione chiusa", "server");
ws.onerror = () => addMessage("Errore di connessione", "server");

document.getElementById("sendBtn").addEventListener("click", () => {
    const text = document.getElementById("msgInput").value.trim();
    if (text.length > 0) {
        const message = {from: username, to: "", content: text};
        ws.send(JSON.stringify(message));
        document.getElementById("msgInput").value = "";
    }
});

function addMessage(text, type) {
    const p = document.createElement("div");
    p.className = "message " + type;
    p.textContent = text;
    chatDiv.appendChild(p);
    chatDiv.scrollTop = chatDiv.scrollHeight;
}

const params = new URLSearchParams(window.location.search);
const webSession = params.get("webSession");
const androidSession = params.get("androidSession");

if (!webSession || !androidSession) {
    alert("Sessioni non valide!");
}

const username = "WebReplica_" + webSession;
const wsProtocol = location.protocol === "https:" ? "wss" : "ws";
const wsUrl = `${wsProtocol}://${location.host}/ChatServer/chat/${username}`;
const chatDiv = document.getElementById("chat");

const ws = new WebSocket(wsUrl);

ws.onopen = () => {
    console.log("Replica chat connessa");
    const initMessage = {
        type: "0003",
        from: username,
        to: "",
        content: androidSession
    };
    ws.send(JSON.stringify(initMessage));
};

ws.onmessage = (event) => {
    const msg = JSON.parse(event.data);
    const from = msg.from;
    const text = msg.content;

    const div = document.createElement("div");
    div.className = from.startsWith("WebReplica") ? "sent" : "received";
    div.textContent = `${from}: ${text}`;
    chatDiv.appendChild(div);
    chatDiv.scrollTop = chatDiv.scrollHeight;
};

document.getElementById("sendBtn").onclick = () => {
    const text = document.getElementById("msgInput").value.trim();
    if (text) {
        const message = {
            type: "0002",
            from: username,
            to: androidSession,
            content: text
        };
        ws.send(JSON.stringify(message));
        document.getElementById("msgInput").value = "";
    }
};
document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const sessionId = params.get("s");
    const img = document.getElementById("qr");
    if (!sessionId)
        return;

    const url = "https://api.qrserver.com/v1/create-qr-code/?data=" + encodeURIComponent(sessionId) + "&size=500x500";
    img.src = url;
});

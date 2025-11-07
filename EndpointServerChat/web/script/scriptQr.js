function creaDatoDaInviare() {
    const qr = {
        s: "ciao",
        size: 500
    };
    return qr;
}

const qr = creaDatoDaInviare();

function getQr() {
    const url = "https://itismagistri.ddns.net/qr/app/?s=" + qr.s + "&size=" + qr.size;
    let img = document.getElementById("qr");
    img.src = url;    
}

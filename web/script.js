const RIGA = 15;
const COLONNA = 8;
const board = document.getElementById("campo");
const carteContainer = document.getElementById("carte");
const tiraDadoBtn = document.getElementById("tiraDado");
const piazzaPedinaBtn = document.getElementById("piazzaPedina");
const fineTurnoBtn = document.getElementById("fineTurno");
let campo = [];
let isDadoTirato = false;
let isPiazzaPedinaPressed = false;

const nome = prompt("Inserire il nome");
const socket = new WebSocket('ws://localhost:8080/smgweb/' + nome);

socket.addEventListener('open', () => {
    console.log(nome + " si è connesso!");
    socket.send(nome);
    alert(nome + " ti sei connesso!");
});

socket.addEventListener('message', event => {
    try {
        const msg = JSON.parse(event.data);
        alert("Messaggio: " + msg.testo);
    } catch (e) {
        alert(event.data);
    }
});

// Inizializza griglia
for (let i = 0; i < RIGA; i++) {
    campo[i] = [];
    for (let j = 0; j < COLONNA; j++) {
        const cell = document.createElement("button");
        cell.dataset.row = i;
        cell.dataset.col = j;
        cell.disabled = true;
        cell.addEventListener("click", onCellClick);
        board.appendChild(cell);
        campo[i][j] = cell;
    }
}

// Slot carte (inizialmente vuoti)
//for (let i = 0; i < 5; i++) {
//    const carta = document.createElement("button");
//    carta.textContent = `C${i + 1}`;
//    carta.title = `Carta ${i + 1}`;
//    carta.disabled = true;
//    carta.addEventListener("click", () => usaCarta(i));
//    carteContainer.appendChild(carta);
//}

// Eventi pulsanti
tiraDadoBtn.addEventListener("click", () => {
    fetch("http://localhost:8080/smgweb/api/tiradado", {
        method: "POST",
        headers: {
            "content-type": "text/plain"
        }
    }).then(response => {
        if (!response.ok) {
            alert("Risposta nulla");
            return null;
        }
        return response.json();
    }).then(rispostaFinale => {
        alert("Hai fatto il numero: " + rispostaFinale);
    });
    isDadoTirato = true;
    alert("Hai tirato il dado!");
    fineTurnoBtn.disabled = false;
});

piazzaPedinaBtn.addEventListener("click", () => {
    isPiazzaPedinaPressed = true;
    for (let i = 0; i < COLONNA; i++) {
        campo[RIGA - 1][i].disabled = false;
    }
    tiraDadoBtn.disabled = false;
});

fineTurnoBtn.addEventListener("click", () => {
    isDadoTirato = false;
    const nome = "Pierpaolo";
    fetch("http://localhost:8080/smgweb/api/fineturno", {
        method: "POST",
        headers: {
            "content-type": "text/plain"
        },
        body: nome
    });
    alert("Turno finito.");
    fineTurnoBtn.disabled = true;
});
// Gestione click su celle
function onCellClick(e) {
    const row = +e.target.dataset.row;
    const col = +e.target.dataset.col;
    console.log(`Hai cliccato sulla cella: ${row}, ${col}`);
    if (isPiazzaPedinaPressed) {
        //e.target.textContent = "🟢"; // questo lo fa game, tu leggi solo la board
        const pedina = {x: row, y: col};
        fetch('http://localhost:8080/smgweb/api/piazzapedina', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pedina)
        }).then(response => {
            return response.json();
        }).then(data => {
            aggiornaCampo(data.content.board, data.content.manoCarte);
        });
    }
    bloccaCampo();
    isPiazzaPedinaPressed = false;
}

// Usa una carta
function usaCarta(index) {
    console.log(`Carta ${index} usata`);
    // fetch a /api/usaCarta con index o id
}

// Blocca tutte le celle non pedinate
function bloccaCampo() {
    for (let i = 0; i < RIGA; i++) {
        for (let j = 0; j < COLONNA; j++) {
            campo[i][j].disabled = campo[i][j].textContent !== "";
        }
    }
}

function aggiornaCampo(board, manoCarte) {
    // Aggiorna il campo con la matrice board
    for (let i = 0; i < RIGA; i++) {
        for (let j = 0; j < COLONNA; j++) {
            const cell = campo[i][j];
            const valore = board[i][j];
            if (valore === "x") {
                cell.innerHTML = `<img src="/immagini/pedina.png" alt="pedina" width="24" height="24">`;
            } else {
                cell.innerHTML = '';
            }
        }
    }
    carteContainer.innerHTML = "";

    if (!Array.isArray(manoCarte))
        return;

    // Crea i pulsanti delle carte
    manoCarte.forEach((carta, i) => {
        const slot = document.createElement("button");
        let colore = '';
        let abilitato = true;

        switch (carta.rarita) {
            case "RARO":
                colore = 'green';
                break;
            case "SUPER RARO":
                colore = 'cyan';
                break;
            case "EPICO":
                colore = 'magenta';
                break;
            case "MITICO":
                colore = 'red';
                break;
            case "LEGGENDARIO":
                colore = 'yellow';
                break;
            case "WIDAUTLEVEL":
                abilitato = false;
                break;
            default:
                colore = 'white';
        }

        slot.innerText = carta.quelloCheLaCartaSaFare;
        slot.disabled = !abilitato || (slot.innerText.includes("(Disabilitata se hai già lanciato il dado)") && isDadoTirato);
        slot.style.backgroundColor = colore;
        slot.style.color = 'black';
        slot.addEventListener("click", () => usaCarta(i));
        slot.id = `slot-${i}`;
        carteContainer.appendChild(slot);
    });
}


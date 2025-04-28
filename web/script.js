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

const socket = new WebSocket('ws://localhost:8080/smgweb/');
const nome = prompt("Inserire il nome");

socket.addEventListener('open', () => {
    console.log(nome + " si è connesso!");
    socket.send(nome);
    alert(nome + " ti sei connesso!");
});

socket.addEventListener('message', event => {
    aggiornaCampo(board, carte);
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
        return response.text();
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
            const contenuto = data.content;
            const board = contenuto.board;
            const carte = contenuto.carte;
            console.log(board);
            aggiornaCampo(board, carte);
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
            const valore = board[i].item[j];
            if (valore === "x") {
                cell.textContent = "🟢";
            } else {
                cell.innerHTML = '';
            }
            cell.disabled = valore !== "";
        }
    }
    carteContainer.innerHTML = "";
    if (!Array.isArray(manoCarte)) {
        return;
    }
    manoCarte.forEach((carta, i) => {
        const slot = document.createElement("button");
        slot.innerText = carta.quelloCheLaCartaSaFare;
        let colore = 'white';
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
        }
        const contieneAvvisoDado = slot.innerText.includes("(Disabilitata se hai già lanciato il dado)");
        if (contieneAvvisoDado && isDadoTirato) {
            abilitato = false;
        }
        slot.disabled = !abilitato;
        slot.style.backgroundColor = colore;
        slot.style.color = 'black';
        slot.id = `slot-${i}`;
        slot.addEventListener("click", () => usaCarta(i));
        carteContainer.appendChild(slot);
    });
    // Invia la nuova board via WebSocket
    if (socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify({
            tipo: "aggiornaBoard",
            contenuto: {
                board: board,
                carte: manoCarte
            }
        }));
    }
}


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
    alert(event.data);
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
// fetch a /api/tiraDado o simile
    isDadoTirato = true;
    alert("Hai tirato il dado!");
    fineTurnoBtn.disabled = false;
});

piazzaPedinaBtn.addEventListener("click", () => {
    isPiazzaPedinaPressed = true;
    // Attiva solo le celle della prima riga
    for (let i = 0; i < COLONNA; i++) {
        campo[RIGA - 1][i].disabled = true;
        campo[RIGA - 1][i].disabled = false;
    }
    tiraDadoBtn.disabled = false;
});

fineTurnoBtn.addEventListener("click", () => {
    isDadoTirato = false;
    // fetch a /api/fineTurno o simile
    fetch("http://localhost:8080/smgweb/",{method:"POST",headers:{"content-type":"fine"}});
    alert("Turno finito.");
    fineTurnoBtn.disabled = true;
});

// Gestione click su celle
function onCellClick(e) {
    const row = +e.target.dataset.row;
    const col = +e.target.dataset.col;
    console.log(`Hai cliccato sulla cella: ${row}, ${col}`);
    if (isPiazzaPedinaPressed) {
        e.target.textContent = "🟢"; // Simula una pedina
        bloccaCampo();
        isPiazzaPedinaPressed = false;
    }
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
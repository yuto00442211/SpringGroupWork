const canvas = document.getElementById("game");
const ctx = canvas.getContext("2d");
const scale = 30;
let score = 0;
let intervalId;
const nextCanvas = document.getElementById("next-tetromino");
const nextCtx = nextCanvas.getContext("2d");


const tetrominoes = [
    { color: "cyan", shape: [[1, 1, 1, 1]] },
    { color: "blue", shape: [[1, 1, 1, 0], [1]] },
    { color: "orange", shape: [[1, 1, 1], [1, 0, 0]] },
    { color: "yellow", shape: [[1, 1], [1, 1]] },
    { color: "green", shape: [[0, 1, 1], [1, 1]] },
    { color: "purple", shape: [[0, 1, 0], [1, 1, 1]] },
    { color: "red", shape: [[1, 1, 0], [0, 1, 1]] }
];



let nextTetromino = tetrominoes[Math.floor(Math.random() * tetrominoes.length)];

let current = { x: 5, y: 0, tetromino: null };
let board = Array(20).fill().map(() => Array(10).fill(0));

function reset() {
    current.tetromino = nextTetromino;
    nextTetromino = tetrominoes[Math.floor(Math.random() * tetrominoes.length)];

    current.x = 5;
    current.y = 0;
    if (collide()) {
        board = Array(20).fill().map(() => Array(10).fill(0));
        score = 0;
        gameover();
    }
}


function collide() {
    for (let y = 0; y < current.tetromino.shape.length; y++) {
        for (let x = 0; x < current.tetromino.shape[y].length; x++) {
            if (
                current.tetromino.shape[y][x] && 
                (board[current.y + y] && board[current.y + y][current.x + x]) !== 0 ||
                current.x + x < 0 || // 左境界
                current.x + x >= 10 || // 右境界
                current.y + y >= 20 // 下境界
            ) {
                return true;
            }
        }
    }
    return false;
}



function merge() {
    for (let y = 0; y < current.tetromino.shape.length; y++) {
        for (let x = 0; x < current.tetromino.shape[y].length; x++) {
            if (current.tetromino.shape[y][x]) {
                board[current.y + y][current.x + x] = current.tetromino.color;
            }
        }
    }
}


function rotate(matrix) {
    const rows = matrix.length;
    const cols = matrix[0].length;
    let newMatrix = Array(cols).fill().map(() => Array(rows).fill(0));

    for (let y = 0; y < rows; y++) {
        for (let x = 0; x < cols; x++) {
            newMatrix[x][rows - 1 - y] = matrix[y][x];
        }
    }

    return newMatrix;
}



function drop() {
    current.y++;
    if (collide()) {
        current.y--;
        merge();
        score += removeRows();
        reset();
    }
}

function removeRows() {
    let count = 0;
    outer: for (let y = board.length - 1; y >= 0; y--) {
        for (let x = 0; x < board[y].length; x++) {
            if (board[y][x] === 0) {
                continue outer;
            }
        }

        const row = board.splice(y, 1)[0].fill(0);
        board.unshift(row);
        y++;
        count++;
    }
    return count * 10; // 1行ごとに10ポイント
}


function rotateTetromino() {
    const originalX = current.x;
    let offset = 1;
    
    current.tetromino.shape = rotate(current.tetromino.shape);

    while (collide()) {
        current.x += offset;
        offset = -(offset + (offset > 0 ? 1 : -1));

        if (offset > current.tetromino.shape[0].length) {
            current.tetromino.shape = rotate(rotate(rotate(current.tetromino.shape))); // Rotate back to the original position
            current.x = originalX;
            return;
        }
    }

    // 効果音を再生する
    const sound = document.getElementById('rotationSound');
    sound.volume = 1.0;  // 最大音量に設定
    sound.currentTime = 0;  // 再生位置を初めに戻す
    sound.play();
}







function draw() {
    ctx.fillStyle = "#f0f0f0";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
      drawNextTetromino();


    for (let y = 0; y < 20; y++) {
        for (let x = 0; x < 10; x++) {
            if (board[y][x] !== 0) {
                ctx.fillStyle = board[y][x];
                ctx.fillRect(x * scale, y * scale, scale, scale);
                ctx.strokeRect(x * scale, y * scale, scale, scale);
            }
        }
    }

    for (let y = 0; y < current.tetromino.shape.length; y++) {
        for (let x = 0; x < current.tetromino.shape[y].length; x++) {
            if (current.tetromino.shape[y][x]) {
                ctx.fillStyle = current.tetromino.color;
                ctx.fillRect((current.x + x) * scale, (current.y + y) * scale, scale, scale);
                ctx.strokeRect((current.x + x) * scale, (current.y + y) * scale, scale, scale);
            }
        }
    }

    ctx.fillStyle = "black";
    ctx.font = "20px Arial";
    ctx.fillText("Score: " + score, 10, 30);
}

document.addEventListener("keydown", e => {
    if (e.key === "ArrowLeft") {
        current.x--;
        if (collide()) current.x++;
    } else if (e.key === "ArrowRight") {
        current.x++;
        if (collide()) current.x--;
    } else if (e.key === "ArrowDown") {
        drop();
    } else if (e.key === "ArrowUp") {  // この部分を追加します。
        rotateTetromino();
    } else if (e.key === " ") {
        e.preventDefault();  // デフォルトのブラウザの動作をキャンセル
        rotateTetromino();
    }
});




function gameover() {
    clearInterval(intervalId);
    ctx.fillStyle = "black";
    ctx.font = "40px Arial";
    ctx.fillText("GAME OVER", 30, canvas.height / 2);

    const btn = document.createElement("button");
    btn.innerHTML = "Retry";
    btn.onclick = startGame;
    document.body.appendChild(btn);
    
    stopBGM();
}

function startGame() {
    board = Array(20).fill().map(() => Array(10).fill(0));
    score = 0;
    reset();
    intervalId = setInterval(update, 500); 
    const btn = document.querySelector("button");
    if (btn) {
        btn.remove();
        
        playBGM();
    }
}

function update() {
    drop();
    draw();
}

function drawNextTetromino() {
    // 以前のコードをクリア
    nextCtx.clearRect(0, 0, nextCanvas.width, nextCanvas.height);

    for (let y = 0; y < nextTetromino.shape.length; y++) {
        for (let x = 0; x < nextTetromino.shape[y].length; x++) {
            if (nextTetromino.shape[y][x]) {
                nextCtx.fillStyle = nextTetromino.color;
                nextCtx.fillRect(x * scale, y * scale, scale, scale);
                nextCtx.strokeRect(x * scale, y * scale, scale, scale);
            }
        }
    }
}


const bgm = document.getElementById('bgm');

function playBGM() {
    bgm.play();
}

function stopBGM() {
    bgm.pause();
    bgm.currentTime = 0;  // 再生位置を初めに戻す
}





reset();
intervalId = setInterval(update, 500);
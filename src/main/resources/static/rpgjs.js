let playerX = 300; // Adjusted initial position in pixels
let playerY = 200; // Adjusted initial position in pixels
let playerHP = 100;
let enemyHP = 50;
const MOVE_AMOUNT = 5; // Adjust for desired movement speed in pixels

const playerHPElement = document.getElementById("player-hp");
const enemyHPElement = document.getElementById("enemy-hp");
const messageElement = document.getElementById("message");
const fieldBGM = document.getElementById("fieldBGM");
const battleBGM = document.getElementById("battleBGM");
let battleActive = false;

function attack() {
    const attackSoundElement = document.getElementById("attackSound");
    attackSoundElement.currentTime = 0; // 再生位置を初めからにリセット
    attackSoundElement.play(); // 効果音を再生

    const damage = Math.floor(Math.random() * 20) + 5;
    enemyHP -= damage;
    enemyHPElement.innerText = enemyHP;
    messageElement.innerText = `プレイヤーがスライムに${damage}ダメージ与えた！`;

    let percentageHP = (enemyHP / 50) * 100;
    document.querySelector(".hp-fill").style.width = percentageHP + "%";

    if (enemyHP <= 0) {
        messageElement.innerText = "プレイヤーは勝利した！";
        setTimeout(() => {
            messageElement.innerText = "";
            endBattle();
        }, 2000);
    } else {
        enemyAttack();
    }
}


function enemyAttack() {
    const damage = Math.floor(Math.random() * 10) + 1;
    playerHP -= damage;
    playerHPElement.innerText = playerHP;
    messageElement.innerText += `\nスライムがプレイヤーに${damage}ダメージ与えた！`;

    let percentageHP = (playerHP / 100) * 100;
    document.querySelector(".hp-fill").style.width = percentageHP + "%";

    if (playerHP <= 0) {
        messageElement.innerText = "プレイヤーは倒された…";
        endBattle();
    }
}


function startBattle() {
    const gameScreen = document.getElementById("game-screen");
    gameScreen.classList.add("swirl-effect"); // 渦巻き効果のクラスを追加

    setTimeout(() => {
        gameScreen.classList.remove("swirl-effect"); // 渦巻き効果のクラスを削除
        transitionToBattle(); // 戦闘画面への遷移を行う関数を呼び出す

        setTimeout(() => {
            gameScreen.classList.add("freeze-screen"); // 画面を固定するクラスを追加
            setTimeout(() => {
                gameScreen.classList.remove("freeze-screen");
            }, 100); // 0秒後に実行

        }, 1000); // 渦巻き効果後、1秒後に実行
    }, 1000); // 渦巻き効果は1秒間
}




function transitionToBattle() {
    battleActive = true;
    const gameScreen = document.getElementById("game-screen");
    gameScreen.classList.add("battle-active");  // Use class instead of changing ID

    fieldBGM.pause();
    fieldBGM.currentTime = 0;
    battleBGM.play();
}

function endBattle() {
    battleActive = false;
    const gameScreen = document.getElementById("game-screen");
    gameScreen.classList.remove("battle-active");  // Use class to remove battle style

    enemyHP = 50;
    enemyHPElement.innerText = enemyHP;

    playerHP = 100;
    playerHPElement.innerText = playerHP;
    document.querySelector(".hp-fill").style.width = "100%";

    battleBGM.pause();
    battleBGM.currentTime = 0;
    fieldBGM.play();
}



function checkForBattle() {
    if (!battleActive && Math.random() < 0.1) {
        startBattle();
    }
}


document.addEventListener("keydown", function(event) {
    if (battleActive) {
        return;
    }
    const key = event.key;

    if (key === "ArrowUp") {
        playerY -= MOVE_AMOUNT;
    } else if (key === "ArrowDown") {
        playerY += MOVE_AMOUNT;
    } else if (key === "ArrowLeft") {
        playerX -= MOVE_AMOUNT;
    } else if (key === "ArrowRight") {
        playerX += MOVE_AMOUNT;
    }
    document.getElementById("player").style.left = `${playerX}px`;
    document.getElementById("player").style.top = `${playerY}px`;
    checkForBattle();
});

// Adjusted initial position using JavaScript
document.getElementById("player").style.left = `${playerX}px`;
document.getElementById("player").style.top = `${playerY}px`;

window.onload = function() {
    fieldBGM.play();
}}
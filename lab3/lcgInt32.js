const axios = require('axios');

const m = BigInt(Math.pow(2, 32));
const id = 45536;
const amountToBet = 10;
const attempts = 1;

async function hacLcg() {
    // await createAccount(id);
    const states = await getStates(id, 1, 1);
    console.log(states);
    const solution = solveEquation(states);
    console.log(solution);
    await earnMoney(id, solution, amountToBet, attempts);
}

async function createAccount(id) {
    await axios.get('http://95.217.177.249/casino/createacc', { params: { id } });
}

async function makeBet(id, bet, number) {
    const response = await axios.get('http://95.217.177.249/casino/playLcg', {
        params: {
            id,
            bet,
            number
        }
    });
    return response.data;
}

async function getStates(id, bet, number) {
    const states = {};
    
    for (let i = 0; i < 3; i++) {
        const data = await makeBet(id, bet, number);
        states[`x${i}`] = BigInt(data.realNumber);
    }

    return states;
}

function solveEquation(states) {
    let a = ((states.x2 - states.x1) * modInverse(states.x1 - states.x0, m)) % m;
    a = BigInt.asIntN(32, a);
    let c = (states.x1 - a * states.x0) % m;
    c = BigInt.asIntN(32, c);

    return { a, c };
}

// formula from stackoverflow
function modInverse(number, mod) {
  number = ((number % mod) + mod) % mod;

  const s = [];
  let b = mod;
  while (b) {
    [number, b] = [b, number % b];
    s.push({ number, b });
  }
  if (a !== 1) {
    throw new Error(" inverse does not exists");
  }

  let x = 1;
  let y = 0;
  for (let i = s.length - 2; i >= 0; --i) {
    [x, y] = [y, x - y * Math.floor(s[i].a / s[i].b)];
  }
  return BigInt(((y % m) + m) % m);
}

async function earnMoney(userId, solution, amountToBet, attempts) {
    let data = await makeBet(userId, 1, 1);
    let x = BigInt(data.realNumber);

    for(let i = 0; i < attempts; i++) {
        x = (solution.a * x + solution.c) % m;
        x = BigInt(BigInt.asIntN(32, x));
        data = await makeBet(userId, amountToBet, x);
        console.log(data);
    }
}

hacLcg();

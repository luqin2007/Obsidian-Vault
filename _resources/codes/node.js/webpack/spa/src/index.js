export default function computer(n, m) {
    document.write('<h2>计算累加和：</h2>')
    let sum = 0
    for (let i = n; i < m; i++) {
        sum += i
    }
    document.write(`${n}-${m} 的累加和为 ${sum}<br>`)
}
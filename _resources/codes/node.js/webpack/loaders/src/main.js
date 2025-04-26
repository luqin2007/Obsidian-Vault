// 导入 css、img
import '../css/style.css'
import myimg from '../images/test.jpg'

let p = document.createElement('p')
let txt = document.createTextNode('Hello Webpack!')
p.appendChild(txt)

let img = document.createElement('img')
img.src = myimg

document.body.append(p, img)
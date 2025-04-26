dv.paragraph(`Loading view1`)

function foo(...args) {
  for (let arg of args) {
    dv.paragraph(arg)
  }
}

foo(input)
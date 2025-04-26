<%*
function parse_path(path) {
    path = path.replaceAll('\\', '/')
    path = path.replaceAll('"', '')
    const abs = '_resources/codes'
    if (path.includes(abs)) {
        path = path.substring(path.indexOf(abs) + abs.length + 1)
    }
    while (path.startsWith('/')) {
        path = path.substring(1)
    }
    while (path.endsWith('/')) {
        path = path.substring(0, path.length - 1)
    }
    return path
}
let filepath = parse_path(await tp.system.prompt('代码文件：'))
let dot = filepath.lastIndexOf(".")
let defaultLang = "plant"
if (dot >= 0) {
    defaultLang = filepath.slice(dot + 1)
}
let lang = await tp.system.prompt('语言：', defaultLang, true)
let title = await tp.system.prompt('标题：')
-%>
```embed-<% lang %>
PATH: "vault://_resources/codes/<% filepath %>"
LINES: "<% await tp.system.prompt('行号：') %>"
<%* if (title != '') { -%>
TITLE: "<% title %>"
<%* } -%>
```
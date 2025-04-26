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
let range = await tp.system.prompt('范围：', '')
let start = NaN
let end = NaN
if (range.startsWith('-')) {
    end = parseInt(range.substring(1))
} else if (range.endsWith('-')) {
    start = parseInt(range.substring(0, range.length - 1))
} else if (range.includes('-')) {
    const div = range.indexOf('-')
    start = parseInt(range.substring(0, div))
    end = parseInt(range.substring(div + 1))
} else {
    start = parseInt(range)
    end = start
}
let lang = await tp.system.prompt('语言：', '')
let fold = await tp.system.suggester(['否', '是'], [false, true], false, "是否折叠")
-%>
```reference<%* if (fold) { %> fold<%* } %>
file: "@/_resources/codes/<% filepath %>"
<%* if (lang != '') { -%>
lang: "<% lang %>"
<%* } -%>
<%* if (!isNaN(start)) { -%>
start: <% start %>
<%* } -%>
<%* if (!isNaN(end)) { -%>
end: <% end %>
<%* } -%>
```
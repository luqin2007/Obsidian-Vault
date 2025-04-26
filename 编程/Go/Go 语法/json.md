`json` 包可以将 Json 字符串与结构体互相转换

> [!hint] 只有 `public` 的字段才会写入 Json，即首字母大写的字段

`````col
````col-md
flexGrow=1
===
```embed-go
PATH: "vault://_resources/codes/go/go_json.go"
LINES: "17-23"
TITLE: 结构体转换为 json
```
````
````col-md
flexGrow=1
===
```embed-go
PATH: "vault://_resources/codes/go/go_json.go"
LINES: "25-31"
TITLE: json 转换为结构体
```
````
`````

为属性添加 `json` tag 可修改对应 json 的键名

```reference
file: "@/_resources/codes/go/go_json_tag.go"
start: 8
end: 11
```



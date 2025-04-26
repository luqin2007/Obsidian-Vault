
```meta-bind
INPUT[toggle(showcase, 
             title(开关),
             onValue(done),
             offValue(in progress),
             defaultValue(done)):complete]
```

```meta-bind
INPUT[inlineSelect(showcase,
	defaultValue(trash),
    option(trash),
    option(bad),
    option(ok),
    option(good),
    option(great)
):rating]
```

```meta-bind
INPUT[date(showcase, title(日期输入)):date]
```

```meta-bind
INPUT[datePicker(showcase, title(日期选择))]
```

```meta-bind
INPUT[dateTime(showcase, title(日期时间))]
```

```meta-bind
INPUT[textArea(showcase, title(文本输入))]
```

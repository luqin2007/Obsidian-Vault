Python 模板库，可用于网页生成时将网页逻辑与具体数据分离，类似更复杂的字符串模板
* 相比 Template：更加灵活，提供控制语句、表达式、继承等
* 相比 Mako：仅有控制语句，不允许编写太多业务逻辑
* 相比 Django：性能更好

```shell
pip3 install jinja2
```

# 模板
## 注释

`{# 注释内容 #}`
## 输出

在模板中直接使用 `{{ 变量名 }}` 即可将变量的值替换到此处。

> [!note] 变量名可以是任何一个表达式，包括变量自带的属性、运算、方法等


```jinja2
<p>普通变量：{{ 变量名 }}</p>
<p>属性：{{ 变量名.属性名 }}</p>
<p>方法：{{ 变量名.方法名() }}</p>
<p>字典：{{ 变量名['key'] }}</p>
<p>列表：{{ 变量名[2] }}</p>
```
### 过滤器

Jinja2 自带的方法称为*过滤器*。

* 字符串：

|名称|说明|
| -------------| ------------------------------|
|safe|对字符串不进行转义|
|escape|对给定字符串进行转义|
|capitialize|首字母大写，其余小写|
|lower|全小写|
|upper|全大写|
|title|每个单词首字母大写，其余小写|
|trim|去除首尾空格|
|striptags|删除所有 HTML 标签|
|join|拼接字符串|
|replace|替换|
|`format(*arg, **args)`|格式化字符串|

* 数字：

|名称|说明|
| ---------------------------| ----------------------------------------------------------------------------|
|round|对数字四舍五入|
|int|将给定值转换为整形|
|abs|绝对值|
|filesizeformat(bin=False)|将给定数字格式化成易读的大小字符串（KB，MB等），参数为 True 时为 1024 进制|

* 其他

|名称|说明|
| ----------------------------| --------------------------------|
|default(value)|当变量未定义时使用默认值|
|first, last|列表第一个/最后一个元素|
|groupby(attr, def=None)|根据 attr 属性将序列折叠成字典|
|max, min|列表中元素的最大 最小值|
|random|列表中的一个随机值|
|reject(cond), select(cond)|根据条件过滤/选择列表中的值|
|`rejectattr/selectattr(*attr, **attrs)`|根据变量过滤/选择列表中的值|
|reverse|将序列逆序|
|sort|对序列排序|

使用过滤器时需要用管道运算符 `|` 调用，需要传入其他参数时用 `()`

```jinja2
{# 首字母大写 -> Abc #}
{{ 'abc' | captialize }}
{# 对 18.5 先四舍五入后取整 -> 19 #}
{{ 18.5 | round | int }}
{# 将 world 替换为 daxin -> hello daxin  #}
{{ 'hello world' | replace('world', 'daxin') }}
```
## 控制语句

控制语句使用 `{% %}` 表示控制语句，并带有一个结束语句（如 if 的 `{% endif %}`）
### if

```jinja2
{% if 条件1 %}
模板1
{% elif 条件2 %}
模板2
{# 可以有任意个 elif，最多一个 else #}
{% else %}
其他模板
{% endif %}
```
### for

用于迭代 Python 的列表、元组、字典等，可以有一个 else 在循环完后调用

遍历列表、元组等可迭代对象：

```jinja2
{% for v in values %}
模板
{% else %}
循环完成
{% endfor %}
```

遍历字典：

```jinja2
{% for k, v in dict %}
模板
{% else %}
循环完成
{% endfor %}
```

特殊变量

|名称|作用|
| ----------------| -------------------------------|
|loop.index|当前索引（从 1 开始）|
|loop.index0|当前索引（从 0 开始）|
|loop.first|bool，是否是第一个元素|
|loop.last|bool，是否是最后一个元素|
|loop.length|所有元素数量|
|loop.revindex|到循环结束的次数（从 1 开始）|
|loop.revindex0|到循环结束的次数（从 0 开始）|
### 宏

类似于 Python 的函数

```jinja2
{% macro 宏名(参数列表，支持默认参数值) %}
模板内容
{% endmacro %}
```

使用时就像一般函数调用即可

```jinja2
{{ 宏名(参数) }}
```
## 继承

模板继承允许模板之间建立继承关系，定义一个基本骨架模板，再在其他模板中填充。

父模板中，使用 `{% block 名称 %}  {% endblock %}` 定义块级替换区域，子模板需要替换这些块

```jinja2
{# 父模板 #}
{# 以 HTML 模板为例 #}
<!DOCTYPE html>
<html lang="en">
<head>
	{% block head %}
    <meta charset="UTF-8">
    <title>{% block title %}{% endblock %}</title>
	{% endblock %}
</head>
<body>
{% block body %}
{% endblock %}
</body>
</html>
```

以上模板定义了 `head`，`title` 和 `body` 三个块，`head` 块之间又有内容。

子模板使用时，先使用 `<% extend "父模板路径" %>` 引入父模板，之后使用 `{% block 块名 %}` 替换内容

```jinja2
{# 假设父模板为 "html_template.html" #}
{% extends "html_template.html" %}
{# 替换 title 块 #}
{% block title %}网页标题啊{% endblock %}
{# 替换 head 块 #}
{% block head %}
    {# 引入原 head 块中的内容 #}
    {{ super() }}
    <style>
        .important { color: #FFFFFF }
    </style>
{% endblock %}
{# 替换 body 块 #}
{% block body %}网页内容啊{% endblock %}
```
## 其他

* 加载静态资源文件：
    * 资源一般位于 `项目目录/static` 目录下
    * 模板中，使用 `{{ url_for('static', filename='资源文件相对地址') }}` 获取其 URL

```jinja2
{# 导入 项目目录/static/images/pic.png 图片 #}
<img src="{{ url_for('static', filename='images/pic.png') }}"></img>
```

# 渲染

渲染，即加载模板文件，再根据传入变量生成字符串的过程。

```python
from jinja2 import PackageLoader,Environment
# 初始化环境, loader 可选 PackageLoader 或 FileSystemLoader
env = Environment(loader=PackageLoader('包名','模板所在相对目录'))
# 创建模板生成器
template = env.get_template('模板文件名')
# 渲染
template.render(参数列表)
```

* `PackageLoader`：以给定包名所在位置为起点
* `FileSystemLoader`：通过绝对路径访问本地文件查找

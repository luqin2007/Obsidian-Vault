面向实时事务处理 OLTP 及批量、分析型 OLAP 的开源图计算框架，是一个可用于不同数据库的抽象层，使用 Gremlin 图遍历语言提供统一 API

![[../../../../_resources/images/Pasted image 20241021122258.png]]

# Gremlin

函数式数据流语言，以间接方式描述复杂属性图遍历等操作

> [!example] [Gremlify](https://gremlify.com/) 提供 Gremlin 的 playground

`````col
````col-md
flexGrow=1
===

| 基本元素             | 说明              |
| ---------------- | --------------- |
| Schema           | 所有属性和类型的集合      |
| 顶点 Vertex        | 图的一个节点          |
| 边 Edge           | 连接两个顶点，可以是有向或无向 |
| 属性类型 PropertyKey | 顶点和边都可以使用的属性类型  |
| 顶点类型 VertexLabel | 顶点的类型           |
| 边类型 EdgeLabel    | 边的类型            |

````
````col-md
flexGrow=1
===

| 基本操作            | 说明           |
| --------------- | ------------ |
| map-step        | 对数据流中的对象进行转换 |
| filter-step     | 对数据流中的对象进行过滤 |
| sideEffect-step | 对数据流进行统计     |

````
`````

# 参考

```cardlink
url: https://tinkerpop.apache.org/
title: "Apache TinkerPop: Home"
host: tinkerpop.apache.org
```

```cardlink
url: https://tinkerpop.apache.org/gremlin.html
title: "Apache TinkerPop: Gremlin"
host: tinkerpop.apache.org
```

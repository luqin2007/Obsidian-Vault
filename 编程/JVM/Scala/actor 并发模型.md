Scala 支持 actor 并发模型，以网络访问为例：
* `actor`：线程池与队列池，使用 `!` 运算符向其中添加任务
* `receive`：接收消息，即任务结果

```scala
import scala.io._
import scala.actors._
import Actor._

val caller = self
// 假设所有网址存在 urls 列表中
for (url <- urls) {
    actor {
        caller ! (url, Source.fromURL(url).mkString)
    }
}

for (i <- 1 to urls.size) {
    receive {
        case (url, content) => {
            // do something
        }
    }
}
```

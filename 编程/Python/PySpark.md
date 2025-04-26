大数据处理的统一分析引擎，分布式计算框架

```shell
pip install pyspark
```

安装后，需要设置 `PYSPARK_PYTHON` 环境变量记录 Python 解释器位置。可以修改环境变量，也可以在程序启动 Spark 前添加以下内容：

```python
import os
os.environ['PYSPARK_PYTHON'] = 'python 解释器（python.exe）位置'
```
# 入口

PySpark 入口为 `SparkContext` 类对象

```python
from pyspark import SparkContext, SparkConf

# 创建配置文件
# setMaster 设置运行主机
# setAppName 设置应用名
conf = SparkConf()\
    .setMaster('local[*]')\
    .setAppName('test_spark_app')
# 创建 SparkContext
sc = SparkContext(conf=conf)
```
# 停止

使用 `sc.stop()` 停止
# 数据处理

![[Pasted image 20230720181538.png]]

* 通过 `SparkContext` 中的函数输入数据，输入完成后返回 `RDD` 类对象
* 通过 `RDD` 对象给定方法对数据进行计算
* 将计算后的 `RDD` 对象结果写出文件、数据库或转换成 `list` 等其他输出或进一步处理
## 读入

* RDD：Resilent Distributed Datasets，弹性分布式数据集，Spark 读入的数据都存在 RDD 对象中
* 算子：RDD 对象接收的用于对数据进行计算的方法，支持链式调用
### 内存

使用 `spark_context.parallelize(obj)` 可以将 Python 的 `list`，`tuple`，`set`，`dict`，`str` 等对象转换为 RDD 对象
* 可迭代对象（list，str，tuple，set 等）会将每一个元素存入 RDD（故 str 每个字符作为一条数据）
* 字典仅存 key
### 文件

使用 `spark_context.textFile(文件路径)` 可以从文件读取纯文本数据，每行文本作为一条数据
## 算子

各种链式调用方法

* map
* flatMap，reduceByKey
    * reduceByKey：(V, V) -> V，针对 (K, V) 类型数据，对相同 K 的各个数据进行汇总操作
* filter，distinct
* sortBy：`sortBy(map, ascending, numPartitions)`
    * map：`(T) -> U`，对待排序的数据进行变形，使之可以进行排序操作
    * ascending：bool，是否升序
    * numPartitions：分区数
## 输出
### 到 Python

* `rdd.collect()`：将数据转换为一个 list
* `rdd.reduce((T, T) -> T)`：将所有数据聚合成一个数据
* `rdd.take(num)`：取前 num 个数据成为一个 list
* `rdd.count()`：获取数据元素数目
### 到文件

依赖于 Hadoop，且需要配置 `HADOOP_HOME` 环境变量

* hadoop
* winutils.exe
* hadoop.dll

`rdd.saveAsTextFile(文件夹)`

* 分别将每个分区的数据保存到单独的文件中
* 设置只有一个分区方法有两种：
  1. 全局单分区：`sparkconf.set("spark.default.parallelism", "1")`
  2. PDD 单分区：`parallelize(..., numSlices=1)`

Traversable 接口由 `sequence` 与 `traverse` 组成
* `sequence`：翻转内外容器类型
* `traverse`：对值进行变换后翻转内外容器类型

```
sequence :: (Traversable t, Applicative f) => (a -> f a) -> t(f a) -> f(t a)
traverse :: (Traversable t, Applicative f) =>
                            (a -> f a, a -> t b) -> a -> f(t b) 
```

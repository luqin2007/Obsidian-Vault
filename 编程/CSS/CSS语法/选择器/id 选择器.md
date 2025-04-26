id 选择器通过元素的 `id` 属性选择元素，多用于配合 JS 动态设置样式，同一页面中 id 相同元素的是唯一的，因此可以准确查找元素。

```CSS
<style>
    /* 类选择器，选择 myElement 标签 */
    #myElement {  
        color: green;  
    }  
</style>  
  
<p id='myElement'>这里使用了类选择器，选择出了 myElement 标签</p>  
<p>不带这个类的标签不会改变</p>  
```

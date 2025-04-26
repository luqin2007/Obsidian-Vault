# onSpinWait
#java9 

`Thread.onSpinWait()` 方法，提示处理器该线程暂时无法继续，可优化资源，需要处理器支持

```java
public class SpinWaitTest implements Runnable {
  
    private volatile boolean dataReady = false;
    @Override
    public void run() {
        // Wait while data is ready
        while (!dataReady) {
            // Hint a spin-wait
            Thread.onSpinWait();
        }
        processData();
    }
  
    private void processData() {
        // Data processing logic goes here
    }
  
    public void setDataReady(boolean dataReady) {
        this.dataReady = dataReady;
    }
}
```

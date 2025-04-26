package com.example.mybank;

import org.springframework.web.context.request.async.DeferredResult;

// 这是一个存储了异步上下文的对象
public class ResultContext<T> {

    // 用于判断执行方法
    private final String methodToInvoke;
    // 用于存储执行结果
    private final DeferredResult<T> result = new DeferredResult<>();

    public ResultContext(String methodToInvoke) {
        this.methodToInvoke = methodToInvoke;
    }

    public String getMethodToInvoke() {
        return methodToInvoke;
    }

    public DeferredResult<T> getResult() {
        return result;
    }
}

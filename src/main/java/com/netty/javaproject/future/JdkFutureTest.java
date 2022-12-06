package com.netty.javaproject.future;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class JdkFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<String> future = pool.submit(() -> {
            log.debug("执行计算。。。");
            Thread.sleep(2000);
            return "asd";
        });
        log.debug("等待结果");
        log.debug("结果是{}", future.get());
    }
}

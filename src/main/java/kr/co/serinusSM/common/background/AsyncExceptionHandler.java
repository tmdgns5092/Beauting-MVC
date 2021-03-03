package kr.co.serinusSM.common.background;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/*
* Thread Exception Print
* */
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        System.out.println("Thread Error Exception");
        System.out.println("exception Message :: " + throwable.getMessage());
        System.out.println("method name :: " + method.getName());
        for(Object param : obj) {
            System.out.println("param Val ::: " + param);
        }
    }
}

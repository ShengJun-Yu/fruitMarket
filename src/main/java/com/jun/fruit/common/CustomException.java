package com.jun.fruit.common;

/**
 * @author : Bojack
 * @date : Created in 14:05 2023.02.08
 * 自定义异常
 */
public class CustomException extends RuntimeException {
    public  CustomException(String message) {
        super(message);
    }
}

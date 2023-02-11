package com.jun.fruit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author : Bojack
 * @date : Created in 22:47 2023.02.06
 * 全局异常捕获
 */
//表明捕获的范围（带这些注解的都捕获）
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * SQLIntegrityConstraintViolationException异常处理方法
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.info(ex.getMessage());
//        获取异常信息
        String exMessage = ex.getMessage();
//        判断是否为账号名字重复
        if (exMessage.contains("Duplicate entry")) {
//            空格分隔异常语句 要第二个为用户账户名/菜品名字
            String[] messgae = exMessage.split(" ");
            exMessage = messgae[2];
            return R.error("'" + exMessage + "'已经存在,请更换！");
        }
        return R.error("发生未知错误，失败了！");
    }

    /**
     * 菜品分类删除异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
//        获取异常信息
        String exMessage = ex.getMessage();
        log.info(ex.getMessage());
        return R.error(exMessage);
    }
}

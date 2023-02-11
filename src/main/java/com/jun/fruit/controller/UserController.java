package com.jun.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jun.fruit.common.R;
import com.jun.fruit.entity.User;
import com.jun.fruit.service.UserService;
import com.jun.fruit.utils.SMSUtils;
import com.jun.fruit.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author : Bojack
 * @date : Created in 16:27 2023.02.10
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取登入的手机号
        String phone = user.getPhone();
        //判断手机号是否为空，发送验证码
        if (phone != null) {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为={}" + code);
//            调用阿里云api发送验证码
//            SMSUtils.sendMessage("ss", "eqd", phone, code);
            //将验证码存入session
            session.setAttribute(phone, code);
        }

        return R.success("验证码发送成功");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String code = (String) map.get("code");
        String phone = (String) map.get("phone");

        Object code1 =  session.getAttribute(phone);
        if (code1 != null && code1.equals(code)) {
            //登入成功
            LambdaQueryWrapper<User> userLam = new LambdaQueryWrapper<>();
            userLam.eq(User::getPhone, phone);
            User user = userService.getOne(userLam);
            if (user == null) {
                User usersave = new User();
                usersave.setPhone(phone);
                usersave.setStatus(1);
                userService.save(usersave);
                session.setAttribute("user",usersave.getId());
                return R.success(usersave);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);

        }
        return R.error("登入失败");
    }
}


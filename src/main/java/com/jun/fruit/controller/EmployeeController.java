package com.jun.fruit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jun.fruit.common.BaseContext;
import com.jun.fruit.common.R;
import com.jun.fruit.entity.Employee;
import com.jun.fruit.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : Bojack
 * @date : Created in 11:50 2023.02.06
 * 员工类控制器
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登入
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
//        1密码Md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> law = new LambdaQueryWrapper<>();
        law.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(law);
        //3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("没有发现该用户信息");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if (!password.equals(emp.getPassword())) {
            return R.error("密码错误");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("该员工没有权限");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employeeID", emp.getId());
        return R.success(emp);
    }

    /**
     * 退出功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
//        清除登入id
        request.getSession().removeAttribute("employeeID");
        return R.success("退出成功");
    }

    /**
     * 添加员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //设置初始密码123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //通过mybatisplus公共字段填充
//        获得当前登录用户的id
        Long id = (Long) request.getSession().getAttribute("employeeID");
        log.info("save方法用户id为{}",id);
        //将id储存到线程中的单独域中
        BaseContext.setId(id);
//        设置其他员工属性
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.save(employee);
        return R.success("用户保存成功");
    }

    /**
     * 分页查询员工
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器
        Page page1 = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper();
        //添加过滤条件有name时增加name为查询条件
        lqw.like(name != null, Employee::getName, name);
        //添加排序条件按照创建时间顺序
        lqw.orderByDesc(Employee::getCreateTime);
        //执行查询
        employeeService.page(page1, lqw);
        return R.success(page1);
    }

    /**
     * 根据id修改员工信息
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> updataStatus(HttpServletRequest request, @RequestBody Employee employee) {
        //通过mybatisplus公共字段填充
       Long employeeID = (Long) request.getSession().getAttribute("employeeID");
       //将id储存到线程中的单独域中
        BaseContext.setId(employeeID);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(employeeID);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getByID(@PathVariable Long id) {
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee!= null) {
            return R.success(employee);
        }
        return R.error("查询失败");
    }
}

package com.jun.fruit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.entity.Employee;
import com.jun.fruit.mapper.EmployeeMapper;
import com.jun.fruit.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author : Bojack
 * @date : Created in 11:49 2023.02.06
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

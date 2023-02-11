package com.jun.fruit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jun.fruit.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Bojack
 * @date : Created in 11:45 2023.02.06
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

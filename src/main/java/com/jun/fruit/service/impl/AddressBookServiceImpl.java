package com.jun.fruit.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jun.fruit.entity.AddressBook;
import com.jun.fruit.mapper.AddressBookMapper;
import com.jun.fruit.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}

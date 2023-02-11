package com.jun.fruit.controller;

import com.jun.fruit.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author : Bojack
 * @date : Created in 15:11 2023.02.08
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${fruitmarket.path}")
    private String pathname;

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //获取文件原始名字
        String filename = file.getOriginalFilename();
        log.info("文件名字为" + filename);
        //用.分隔原始文件名字
        String[] filenames = filename.split("\\.");
        //用UUID+ . +文件原始名后缀名字 组成新的名字
        filename = UUID.randomUUID().toString() + "." + filenames[filenames.length - 1];
        //创建一个目录对象
        File dir = new File(pathname);
        //判断当前目录是否存在
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(pathname + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 下载文件
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream inputStream = new FileInputStream(new File(pathname + name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream servletOutputStream = response.getOutputStream();
//            设置响应格式
            response.setContentType("image/jpeg");
//            将读取的文件写入输出流
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                servletOutputStream.write(bytes, 0, len);
                servletOutputStream.flush();
            }
//            关闭流
            inputStream.close();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


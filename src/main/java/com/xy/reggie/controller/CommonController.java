package com.xy.reggie.controller;

import com.xy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    //从application.yml配置文件中获取base路径
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //获取原始文件的文件格式后缀
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID来重新成生文件名，避免文件名重复造成文件覆盖
        String filename = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File("basePath");
        //判断当前目录是否存在
        if(!dir.exists()){
            //若目录不存在则创建一个目录
            dir.mkdirs();
        }

        //file是一个临时文件，需要转存到指定位置，否则此次请求过后会被删除
        try {
            //转存临时图片文件
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);
    }

    /**
     *文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //设置响应数据类型
        response.setContentType("image/jpeg");

        try {
            //输入流，读取文件内容
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            //输出流，写给浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024]; //用来作为读写缓冲
            while((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭输入/输出流
            outputStream.close();
            fileInputStream.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

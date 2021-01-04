package com.github.dark.utils;

import com.github.dark.constants.CommonMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static com.github.dark.utils.Constants.RESOURCE_PREFIX;

@Slf4j
@Service
public class FileUtils {

    public String uploadsFile(MultipartFile file, HttpServletRequest request){
        String path=null;
        try {
            String name = file.getOriginalFilename();
            if (!StringUtils.isBlank(name)){
                String suffix =  name.substring(name.indexOf("."));
                name = UUID.randomUUID().toString().replace("-", "") + suffix;
                InputStream inputStream = file.getInputStream();
                // 获得客户端发送请求的完整url
                String url = request.getRequestURL().toString();
                // 获得去除接口前的url
                String urlVal = url.replace("/upload", "");
                // 目录路径
                Path directory = Paths.get(RESOURCE_PREFIX);
                // 判断目录是否存在，不存在创建
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
                assert name != null;
                // 拷贝文件
                Files.copy(inputStream, directory.resolve(name));
                // url路径
                path = urlVal + "/getImgFile/" + name;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return path;
    }
}

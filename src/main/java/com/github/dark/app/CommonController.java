package com.github.dark.app;

import com.github.dark.Constants.CommonMessage;
import com.github.dark.commom.ResultData;
import com.github.dark.config.BaseContextHandler;
import com.github.dark.entity.PhotoGalleryEntity;
import com.github.dark.utils.Constants;
import com.github.dark.utils.FileUploadUtils;
import com.github.dark.utils.MimeTypeUtils;
import com.github.dark.vo.request.PhotoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static com.github.dark.utils.Constants.RESOURCE_PREFIX;

@RestController
@Api("通用接口")
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${file_url:https://qzzsfat.fjpszx.com/dev-api}")
    private String url;

    @PostMapping("/ign/upload")
    @ApiOperation("文件上传")
    public ResultData<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ResultData<String> resultData = new ResultData<>();
        try {
            String name = file.getOriginalFilename();
            if (StringUtils.isBlank(name)){
                resultData.setCode(CommonMessage.ERROR);
                resultData.setMsg("请上传正确文件");
                return resultData;
            }
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
            String path = urlVal + "/getImgFile/" + name;
            resultData.setData(path);
            return resultData;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.setData(e.getMessage());
        }
        return resultData;
    }

    /**
     * 使用流将图片输出
     * @param response
     * @param name
     * @throws IOException
     */
    @ApiOperation("获取图片文件")
    @GetMapping("/ign/getImgFile/{name}")
    public void getImage(HttpServletResponse response, @PathVariable("name") String name) throws IOException {
        String suffix =  name.substring(name.indexOf(".") + 1);
        if (FileUploadUtils.isAllowedExtension(suffix, MimeTypeUtils.MEDIA_EXTENSION)){
            response.setContentType("video/mp4;charset=utf-8");
        }else {
            response.setContentType("image/jpeg;charset=utf-8");
        }
        response.setHeader("Content-Disposition", "attachment; filename=girls." + suffix);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(Files.readAllBytes(Paths.get(RESOURCE_PREFIX).resolve(name)));
        outputStream.flush();
        outputStream.close();
    }
}

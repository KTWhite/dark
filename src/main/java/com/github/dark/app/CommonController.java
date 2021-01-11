package com.github.dark.app;

import com.github.dark.annotation.RunTimeLog;
import com.github.dark.constants.CommonMessage;
import com.github.dark.commom.ResultData;
import com.github.dark.utils.FileUploadUtils;
import com.github.dark.utils.FileUtils;
import com.github.dark.utils.MimeTypeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.dark.utils.Constants.RESOURCE_PREFIX;

@RestController
@Api(tags = "通用接口")
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${file_url:https://qzzsfat.fjpszx.com/dev-api}")
    private String url;

    @Resource
    private FileUtils fileUtils;

    @RequestMapping(value = "/ign/uploads",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    @ApiOperation("批量文件上传")
    public ResultData<Set<String>> uploads(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
        ResultData<Set<String>> resultData = new ResultData<>();
        Set<String> urlSet=new HashSet<>();
        for (int i=0;i<files.length;i++){
            MultipartFile file = files[i];
            try {
                String path = fileUtils.uploadsFile(file, request);
                urlSet.add(path);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        resultData.setData(urlSet);
        return resultData;
    }

    @RequestMapping(value = "/ign/upload",method = RequestMethod.POST,headers = "content-type=multipart/form-data")
    @ApiOperation("单文件上传")
    public ResultData<String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ResultData<String> resultData = new ResultData<>();
        String path = fileUtils.uploadsFile(file, request);
        if (path!=null){
            resultData.setData(path);
        }else{
            resultData.setData("请上传正确文件");
            resultData.setCode(CommonMessage.ERROR);
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

    @RunTimeLog
    @ApiOperation("测试方法")
    @GetMapping("/ign/testFunction")
    public void testFunction(){
        System.out.println("测试方法时间");
    }

}

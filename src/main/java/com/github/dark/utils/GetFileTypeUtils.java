package com.github.dark.utils;

import com.github.dark.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class GetFileTypeUtils {
    private static String getFileContent(MultipartFile file) throws IOException {
        byte[] b = new byte[20];
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            inputStream.read(b,0,20);
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();;
                }catch (Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }
    /**
     * @description 第二步：将文件头转换成16进制字符串
     * @param
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        System.out.println("文件类型16进制字符串是"+stringBuilder.toString());
        return stringBuilder.toString();
    }
    /**
     * @description 第三步：根据十六进制字符串判断文件类型格式
     * @param file 文件路径
     * @return 文件类型
     */
    public static FileType getType(MultipartFile file) throws IOException {
        String fileHead = getFileContent(file);
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        for (FileType type : fileTypes) {
//            startsWith() 方法用于检测字符串是否以指定的前缀开始
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }
        return null;
    }
}

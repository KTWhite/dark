package com.github.dark.utils;

import com.github.dark.annotation.ExcelColumn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ExcelUtils {
    private final static String TAG= "ExcelUtils";
    private final static String EXCEL2003="xls";
    private final static String EXCEL2007="xlsx";
    public static <T> List<T> readExcel(String path, Class<T> cls, MultipartFile file){
        String filename = file.getOriginalFilename();
        if (!filename.matches("^.+\\.(?i)(xls)$")&&!filename.matches("^.+\\.(?i)(xlsx)$")){
            log.debug(TAG,"上传格式有误！");
        }
        List<T> dataList =new ArrayList<>();
        Workbook workbook=null;
        try {
            InputStream inputStream = file.getInputStream();
            if (filename.endsWith(EXCEL2007)){
                workbook=new XSSFWorkbook(inputStream);
            }
            if (filename.endsWith(EXCEL2003)) {
                workbook= new HSSFWorkbook(inputStream);
            }
            if (workbook!=null){
                //类映射  注解 value-->bean columns
                HashMap<String, List<Field>> classMap = new HashMap<>();
                List<Field> fields = Stream.of(cls.getDeclaredFields()).collect(Collectors.toList());
                log.debug(TAG,"数量："+fields.size());
                fields.forEach(field -> {
                    ExcelColumn annotation = AnnotationUtils.getAnnotation(field,ExcelColumn.class);
                    if (annotation!=null){
                        String value = annotation.value();
                        log.debug(TAG,"值："+value);
                        if (StringUtils.isBlank(value)){
                            log.debug(TAG+": 该行为空",annotation.col()+"");
                            return;//return起到的作用和continue是相同的 语法
                        }
                        if (!classMap.containsKey(value)){
                            classMap.put(value,new ArrayList<>());
                        }
                        field.setAccessible(true);
                        classMap.get(value).add(field);
                    }else{
                        log.debug(TAG,"annotation：为空！");
                    }
                });
                Map<Integer,List<Field>> reflectionMap = new HashMap<>();
                Sheet sheet  = workbook.getSheetAt(0);
                boolean firstRow=true;
                for (int i=sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++){
                    Row row = sheet.getRow(i);
                    //首行提取注解
                    if (firstRow){
                        for (int j = row.getFirstCellNum();j<row.getLastCellNum();j++){
                            Cell cell = row.getCell(j);
                            String cellValue = getCellValue(cell);
                            if (classMap.containsKey(cellValue)) {
                                reflectionMap.put(j,classMap.get(cellValue));
                            }
                        }
                        firstRow=false;
                    }else{
                        if (row==null){
                            continue;
                        }
                        try {
                            T t = cls.newInstance();
                            boolean allBlank = true;
                            for (int j=row.getFirstCellNum();j<row.getLastCellNum();j++){
                                if (reflectionMap.containsKey(j)){
                                    Cell cell = row.getCell(j);
                                    String cellValue = getCellValue(cell);
                                    log.debug(TAG,"当前所在列"+j+"的值："+cellValue);
                                    if (StringUtils.isNotBlank(cellValue)){
                                        allBlank=false;
                                    }
                                    List<Field> fieldList  = reflectionMap.get(j);
                                    fieldList.forEach(x->{
                                        try {
                                            handleField(t,cellValue,x);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            log.error(String.format("reflect field:%s value:%s exception!", x.getName(), cellValue), e);
                                        }
                                    });
                                }
                            }
                            if (!allBlank){
                                dataList.add(t);
                            }else{
                                log.warn(String.format("row:%s is blank ignore!", i));
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (workbook!=null){
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error(String.format("parse excel exception!"), e);
                }
            }
        }
        return dataList;

    }



    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return HSSFDateUtil.getJavaDate(cell.getNumericCellValue()).toString();
            } else {
                return new BigDecimal(cell.getNumericCellValue()).toString();
            }
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return StringUtils.trimToEmpty(cell.getStringCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return StringUtils.trimToEmpty(cell.getCellFormula());
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return "";
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
            return "ERROR";
        } else {
            return cell.toString().trim();
        }

    }

    private static <T> void handleField(T t, String value, Field field) throws Exception {
        Class<?> type = field.getType();
        if (type == null || type == void.class) {
            return;
        }
        if (type == Object.class) {
            field.set(t, value);
            //数字类型
        } else if (type.getSuperclass() == null || type.getSuperclass() == Number.class) {
            if (type == int.class || type == Integer.class) {
                field.set(t, NumberUtils.toInt(value));
            } else if (type == long.class || type == Long.class) {
                field.set(t, NumberUtils.toLong(value));
            } else if (type == byte.class || type == Byte.class) {
                field.set(t, NumberUtils.toByte(value));
            } else if (type == short.class || type == Short.class) {
                field.set(t, NumberUtils.toShort(value));
            } else if (type == double.class || type == Double.class) {
                field.set(t, NumberUtils.toDouble(value));
            } else if (type == float.class || type == Float.class) {
                field.set(t, NumberUtils.toFloat(value));
            } else if (type == char.class || type == Character.class) {
                field.set(t, CharUtils.toChar(value));
            } else if (type == boolean.class) {
                field.set(t, BooleanUtils.toBoolean(value));
            } else if (type == BigDecimal.class) {
                field.set(t, new BigDecimal(value));
            }
        } else if (type == Boolean.class) {
            field.set(t, BooleanUtils.toBoolean(value));
        } else if (type == Date.class) {
            field.set(t, value);
        } else if (type == String.class) {
            field.set(t, value);
        } else {
            Constructor<?> constructor = type.getConstructor(String.class);
            field.set(t, constructor.newInstance(value));
        }
    }



    public static <T> void writeExcel(HttpServletResponse response, List<T> dataList, Class<T> cls){
        Field[] fields  = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(field -> {
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null && annotation.col() > 0) {
                        field.setAccessible(true);
                        return true;
                    }
                    return false;
                }).sorted(Comparator.comparing(field -> {
                    int col = 0;
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    if (annotation != null) {
                        col = annotation.col();
                    }
                    return col;
                })).collect(Collectors.toList());
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        sheet.setDefaultColumnWidth(30);
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            //写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                Cell cell = row.createCell(aj.getAndIncrement());

                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
                cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
                cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
                cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
                cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
                cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
            });
        }
        if (CollectionUtils.isNotEmpty(dataList)) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Cell cell = row1.createCell(aj.getAndIncrement());
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellValue(value.toString());
                    }
                });
            });
        }
        //冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        buildExcelDocument(wb,response);
    }


    /**
     * 浏览器下载excel
     * @param wb
     * @param response
     */

    private static  void  buildExcelDocument(Workbook wb,HttpServletResponse response) {
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + System.currentTimeMillis() + ".xlsx");
            wb.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

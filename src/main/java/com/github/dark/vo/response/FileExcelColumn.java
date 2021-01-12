package com.github.dark.vo.response;

import com.github.dark.annotation.ExcelColumn;
import lombok.Data;

@Data
public class FileExcelColumn {
    @ExcelColumn(value = "园区",col =1)
    private String yuanqu;
    @ExcelColumn(value = "注册时间",col =2)
    private String regDate;
    @ExcelColumn(value = "数量",col =3)
    private String total;
}

package com.shan.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ExportDefinition {
	private String title; // 标题
    private String field; // 字段
    private int rowIndex; // 所在的行
    private int cellIndex; // 所在的列
    private String mainDict; // 主字典-用于加载主字典的数据
    private String subDict; // 子字典-用于加载subField的数据
    private String subField; // 即需要级联的字典
    private String refName; // 主字段所在的位置
    private String point; // 标题的坐标
    private boolean validate;// 是否设置数据的有限性

    public ExportDefinition(String title, String field, String mainDict, String subDict, String subField) {
        this.title = title;
        this.field = field;
        this.mainDict = mainDict;
        this.subDict = subDict;
        this.subField = subField;
    }

}

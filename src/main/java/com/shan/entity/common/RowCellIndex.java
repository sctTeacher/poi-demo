package com.shan.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class RowCellIndex {
	
	 /** 单元格的行索引 */
    private Integer rowIndex;
    /** 单元格的列索引 */
    private Integer cellIndex;
    
    public int incrementRowIndexAndGet() {
        this.rowIndex++;
        return this.getRowIndex();
    }
    public int incrementCellIndexAndGet() {
        this.cellIndex++;
        return this.getCellIndex();
    }
    public int reduceRowIndexAndGet(){
        this.rowIndex--;
        return this.getRowIndex();
    }
    public int reduceCellIndexAndGet(){
        this.cellIndex--;
        return this.getCellIndex();
    }
}

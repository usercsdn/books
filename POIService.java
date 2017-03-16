package com.fzs.spider.excel;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface POIService {
	
	/**
	 * @param outputStream 写入文件
	 * @param dataList 数据列表
	 * @param sheetNames 表头字段列名称
	 * @param titles 表头名称
	 * @param startSheetNum 开始sheet索引
	 * @param rowSize 每次内存缓存数量
	 *
	 */
	abstract void process(SXSSFWorkbook sxsSFWorkbook,Map<String,List<String[]>> dataMap,String [] titles,int startSheetNum);
}

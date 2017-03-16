package com.fzs.spider.excel;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.stereotype.Component;
@Component
public class POIServiceFactory implements POIService {

	private static final Log logger = LogFactory.getLog(POIServiceFactory.class);
	
	
	/* (non-Javadoc)
	 * @see com.zjht.points.admin.template.poi.POIService#process(java.io.OutputStream, java.util.List, java.lang.String[], int, int)
	 */
	@Override
	public void process(SXSSFWorkbook sxsSFWorkbook,Map<String,List<String[]>> dataMap, String [] titles,int startSheetNum) {
		if(dataMap!=null){
			//SXSSFWorkbook sxsSFWorkbook=null;
			logger.debug("POIServiceFactory process export excel start.... dataMap="+dataMap.size()+" titles="+titles.length+" startSheetNum="+startSheetNum);
			try{
				//sxsSFWorkbook = new SXSSFWorkbook(rowSize);//内存中保留 rowSize条数据，以免内存溢出，其余写入 硬盘         
				sxsSFWorkbook.setCompressTempFiles(true);
				for(Map.Entry<String, List<String[]>> entry : dataMap.entrySet()) {
		            String sheetName=entry.getKey(); //获取sheetName名称
		            List<String[]> list=entry.getValue();    //获取对应sheetName数据列表内容
		            logger.debug("startSheetNum="+startSheetNum+" sheetName="+sheetName+" list size="+list.size());
		            Sheet sheet = sxsSFWorkbook.createSheet(String.valueOf(startSheetNum)); //创建sheet
		          sxsSFWorkbook.setSheetName(startSheetNum, sheetName); //设置对应sheet名称
		          
		          /*  String tempName = sheetName;
		        	String regEx="[^0-9]";   
		    		Pattern p = Pattern.compile(regEx);   
		    		Matcher m = p.matcher(tempName);   
		    		Integer index = Integer.valueOf(m.replaceAll("").trim());
		            sxsSFWorkbook.setSheetOrder(sheetName, index);*/
		            if(list!=null){
		            	XSSFCellStyle  cellStyle = (XSSFCellStyle) sxsSFWorkbook.createCellStyle();
		            	//cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());// 设置背景色
		            	//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		                cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框  
		                cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框  
		                cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框  
		                cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框  
		                cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 左对齐  
		                cellStyle.setWrapText(true);//设置自动换行
		                
		            	Font font= sxsSFWorkbook.createFont(); //设置字体
		            	//font.setColor(Font.BOLDWEIGHT_BOLD);
		            	font.setFontHeightInPoints((short) 11);//设置字体大小  
		            	font.setFontName("黑体"); 
		            	cellStyle.setFont(font); 
		            	
		           	    Row titleRow = sheet.createRow(0); 
		            	for(int i=0;i<titles.length;i++){
		            		 Cell cell = titleRow.createCell(i);                     
		                     cell.setCellType(XSSFCell.CELL_TYPE_STRING);//表头文本格式 
		                     cell.setCellStyle(cellStyle);
		                     sheet.setColumnWidth(i, titles[i].length()*2000); //设置表头单元格宽度  
		                     cell.setCellValue(titles[i]);//写入表头内容  
		            	}
		            	cellStyle.setFillPattern(HSSFCellStyle.NO_FILL);//设置前景填充样式
		            	cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);//前景填充色 
		            	for(int i=0;i<list.size();i++){
		            		 String[] listStr =list.get(i);
		            		 Row row = sheet.createRow(i+1); 
		            		 for(int cols=0;cols<listStr.length;cols++){
			            		 Cell cell = row.createCell(cols);                     
			                     cell.setCellType(XSSFCell.CELL_TYPE_STRING);//文本格式
			                     font.setFontName("微软雅黑"); 
					             cellStyle.setFont(font);
			                     cell.setCellStyle(cellStyle);
			                     //sheet.setColumnWidth(cols, listStr[cols].length()*200); //设置单元格宽度  
			                     cell.setCellValue(listStr[cols]);//写入内容  
		            		 }
		            	
		            	}
		            }
	           } 
				 //sxsSFWorkbook.write(outputStream);  
				 //outputStream.close();  
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				//sxsSFWorkbook.dispose();
				dataMap.clear();
			}
			
		}else{
			logger.info("POIServiceFactory process export excel data is null or the file is not find....");
		}

	}

}

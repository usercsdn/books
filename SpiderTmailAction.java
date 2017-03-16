package com.fzs.spider.http.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fzs.spider.bean.FlowProduct;
import com.fzs.spider.bean.ResultBean;
import com.fzs.spider.dao.FlowProductDao;
import com.fzs.spider.excel.POIService;
import com.fzs.spider.http.util.Util;
import com.google.common.collect.Maps;
@Controller
@RequestMapping("admin/tmail")
public class SpiderTmailAction {
	private final static Map<String,String> provCodeMap=Maps.newLinkedHashMap();
	private final static Map<String,String> operatorCodeMap=Maps.newLinkedHashMap();
	private final static Map<String,String> ydStandardPriceMap=Maps.newLinkedHashMap();
	private final static Map<String,String> ltStandardPriceMap=Maps.newLinkedHashMap();
	private final static Map<String,String> dxStandardPriceMap=Maps.newLinkedHashMap();
	private  static Map<String,String> flowMap=new LinkedHashMap<String,String>();
//	final static String url="https://list.tmall.com/search_product.htm?spm=a220m.1000858.0.0.9l2Okb&cat=51134009&fzscondition&q=%C1%F7%C1%BF%B3%E4%D6%B5&sort=s&style=g&from=mallfp..pc_1_suggest&suggest=0_2&industryCatId=51134009&type=pc#J_Filter";
//	
	final static String url="https://list.tmall.com/search_product.htm?spm=a220m.1000858.1000722.1.cEhqHr&cat=51134009&q=%C1%F7%C1%BF%B3%E4%D6%B5&fzscondition&style=g&search_condition=4&from=sn_1_prop-qp&suggest=0_2&industryCatId=51134009#J_crumbs";
	
	@Value("${cookie}")
	String cookieFile;
	
	@Autowired
	POIService poiService;
	
	static{
		Util.cookieMap.put("Host","list.tmall.com");
		Util.cookieMap.put("Connection","keep-alive");
		Util.cookieMap.put("Cache-Control","max-age=0");
		Util.cookieMap.put("Upgrade-Insecure-Requests","1");
		Util.cookieMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		Util.cookieMap.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp;q=0.8");
		Util.cookieMap.put("Referer","https://www.tmall.com/?ali_trackid=2:mm_26632258_3504122_48284354:1482926241_3k7_541968705&upsid=47b1d431389c63226c185779d55e44db&clk1=47b1d431389c63226c185779d55e44db");
		Util.cookieMap.put("Accept-Encoding","gzip, deflate, sdch, br");
		Util.cookieMap.put("Accept-Language","zh-CN,zh;q=0.8");
		Util.cookieMap.put("Cookie","_med=dw:1366&dh:768&pw:1366&ph:768&ist:0; hng=; x=__ll%3D-1%26_ato%3D0; uc3=nk2=3B7unXGsCFs%3D&id2=UoH7Lo9Mvn%2BGAg%3D%3D&vt3=F8dARVWMZs%2Ba%2BlyKxMI%3D&lg2=U%2BGCWk%2F75gdr5Q%3D%3D; uss=AnJ%2BuKMtJDOO6l48WN%2BX4CLT1sUgMZzbVyWcTdcK9SZjcECiKoWlXY4CtOg%3D; lgc=%5Cu5F00%5Cu6302%5Cu4E8622; tracknick=%5Cu5F00%5Cu6302%5Cu4E8622; t=e0dcf4f70fe1b692bc91ce7e1111d62c; _tb_token_=UOQszVrqly1n; cookie2=097a0c59ff71eb57d441a8ab45b037c2; swfstore=129547; cna=qiOsED2q7VMCAQ4XQap1r27N; pnm_cku822=154UW5TcyMNYQwiAiwQRHhBfEF8QXtHcklnMWc%3D%7CUm5Ockt%2BSnFLf0V4QHVMdiA%3D%7CU2xMHDJ7G2AHYg8hAS8XLAIiDFAxVztcIlh2IHY%3D%7CVGhXd1llXGldZlxoUm9XYlthVmtJcEl2THVLc0p1SXRId0J2THVbDQ%3D%3D%7CVWldfS0TMwszBiYaIQEvFm9bdkZ6T2oYYFl%2BTnFHMx1LHQ%3D%3D%7CVmhIGCUFOBgkGiMXNw82DzoaJhgjGDgCOQwsEC4VLg40Cz5oPg%3D%3D%7CV25Tbk5zU2xMcEl1VWtTaUlwJg%3D%3D; res=scroll%3A1349*5448-client%3A1349*661-offset%3A1349*5448-screen%3A1366*768; cq=ccp%3D1; l=AuXl1cK7foAjvp18lYLJBi7ydasfI5m0; isg=AnBwr-Uy3LmT0YDR9vxhbNDNQT6enVQDhtlq_2rBPUueJRDPEskkk8YXC5q_; otherx=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0");
	}
	
	/**
	 * 初始化页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("init")
	public String init(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("flowMap",getFlowTypes(null));
		request.setAttribute("provCodeMap",provCodeMap);
		request.setAttribute("operatorCodeMap",operatorCodeMap);
		return "/admin/flowProduct/tmailFlowProduct";
	}
	
	@RequestMapping("changCookie")
	public String changCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cookies=request.getParameter("cookies");
		request.setAttribute("defCookies", Util.cookieMap.get("Cookie"));
		if(Util.isEmpty(cookies)){
			return "/admin/flowProduct/tmailCookies";
		}
		System.out.println(cookies);
		/*FileOutputStream out=new FileOutputStream(new File(cookieFile));
		out.write(cookies.getBytes());
		Util.cookieMap.clear();
		BufferedReader cookieReader = new BufferedReader(new FileReader(cookieFile));
		String line = "";
		while((line = cookieReader.readLine()) != null){
			if(Util.isNotEmpty(line)){
				String[] km=line.split("\\;");
				if(km!=null&&!"".equals(km)&&km.length>1){
					Util.cookieMap.put(km[0], km[1]);
				}
			}
		}
		cookieReader.close();
		out.close();*/
		Util.cookieMap.put("Cookie", cookies);
		System.out.println(Util.cookieMap);
		request.setAttribute("cookies", cookies);
		request.setAttribute("defCookies", Util.cookieMap.get("Cookie"));
		return "/admin/flowProduct/tmailCookies";
	}
	
	@RequestMapping(value = "/export")
	public void export(HttpServletRequest request, HttpServletResponse respone,FlowProduct fp) {
		try {
			ResultBean rb = new ResultBean();
			StringBuilder condition=new StringBuilder();
			StringBuilder sort=new StringBuilder();
			if(Util.isNotEmpty(fp.getRegion())){
				String[] region=fp.getRegion().split(";");
				fp.setRegion(region[0]);
				fp.setProv(region[1]);
			}
			if(Util.isNotEmpty(fp.getOperator())){
				String[] operator=fp.getOperator().split(";");
				fp.setOperator(operator[0]);
				fp.setOper(operator[1]);
			}
			if(Util.isNotEmpty(fp.getFlowType())){
				String[] flowType=fp.getFlowType().split(";");
				fp.setFlowType(flowType[0]);
				fp.setFlow(flowType[1]);
			}
			condition.append(fp.getRegion()).append(";").append(fp.getOperator()).append(";").append(fp.getFlowType());
			if(Util.isNotEmpty(fp.getSort())){
				sort.append("&sort=").append(fp.getSort());
			}
//			ResultBean parseTmailHtml = parseTmailHtml(fp.getCurrentPage(),condition.toString(),sort.toString(),fp,rb);
//			long total = parseTmailHtml.getTotal();
			List<FlowProduct> flowProductTotals=new ArrayList<FlowProduct>();
			for(int i=0;i<3;i++){
				List<FlowProduct> flowProducts = getFlowProducts(i, condition.toString(), sort.toString(), fp, rb);
				if(Util.isNotEmpty(flowProducts))flowProductTotals.addAll(flowProducts);
			}
			export(flowProductTotals, respone);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("list")
	@ResponseBody
	public ResultBean list(HttpServletRequest request,
			HttpServletResponse response, FlowProduct fp) throws IOException, InterruptedException {
		ResultBean rb = new ResultBean();
		StringBuilder condition=new StringBuilder();
		StringBuilder sort=new StringBuilder();
		if(Util.isNotEmpty(fp.getRegion())){
			String[] region=fp.getRegion().split(";");
			fp.setRegion(region[0]);
			fp.setProv(region[1]);
		}
		if(Util.isNotEmpty(fp.getOperator())){
			String[] operator=fp.getOperator().split(";");
			fp.setOperator(operator[0]);
			fp.setOper(operator[1]);
		}
		if(Util.isNotEmpty(fp.getFlowType())){
			String[] flowType=fp.getFlowType().split(";");
			fp.setFlowType(flowType[0]);
			fp.setFlow(flowType[1]);
		}
		condition.append(fp.getRegion()).append(";").append(fp.getOperator()).append(";").append(fp.getFlowType());
		if(Util.isNotEmpty(fp.getSort())){
			sort.append("&sort=").append(fp.getSort());
		}
		return parseTmailHtml(fp.getCurrentPage(),condition.toString(),sort.toString(),fp,rb);
		
	}
	
	public  ResultBean parseTmailHtml(int i,String condition,String sort, FlowProduct fp,ResultBean rb) throws IOException, InterruptedException{
		 rb.setData(getFlowProducts(i, condition, sort, fp, rb));
		 return rb;
	}
	private List<FlowProduct> getFlowProducts(int i,String condition,String sort, FlowProduct fp,ResultBean rb){
		List<FlowProduct> list=new ArrayList<FlowProduct>();
		String page=(i==1)?"":"&s="+(i-1)*60;
		if(condition.length()<4)condition="";
		String newurl=url.replace("fzscondition", "prop="+condition+page+sort);
		Document doc = Util.getFromTmail(newurl,false);
		Element elementPage =doc.select("input[name='totalPage']").first();
		if(elementPage==null){
			return null;
		}
		String total=elementPage.attr("value");
		System.out.println("总共："+total+"页");
		if(total!=null){
			rb.setTotal(Integer.valueOf(total)*60);
		}
		 Elements elements=doc.select(".product .product-iWrap");
		 for(Element element: elements){
			 FlowProduct product=new FlowProduct();
			 System.out.println(element.html());
			 Element prodPrice=element.select("p.productPrice").first();
			 System.out.println("price="+prodPrice.text().substring(1));
			 product.setPrice(new BigDecimal(prodPrice.text().substring(1)));
			 
			 Element prodTitle=element.select("p.productTitle").first();
			 System.out.println("name="+prodTitle.text());
			 product.setName(prodTitle.text());
			 
			 Element prodLink=element.select("p.productTitle a").first();
			 System.out.println("prodUrl="+prodLink.attr("href"));
			 product.setProdUrl(prodLink.attr("href"));
			 
			 Element shortLink=element.select("a.productShop-name").first();
			 System.out.println("shopName="+shortLink.text());
			 System.out.println("shopUrl="+shortLink.attr("href"));
		
			 product.setShopName(shortLink.text());
			 product.setShopUrl(shortLink.attr("href"));
			 product.setRegion(fp.getProv());
			 product.setFlowType(fp.getFlow());
			 product.setOperator(fp.getOper());
			 String standarPrice=null;
			 if(fp.getOper().equals("中国移动")){
				  standarPrice=ydStandardPriceMap.get(fp.getFlow());
			 }else  if(fp.getOper().equals("中国联通")){
				  standarPrice=ltStandardPriceMap.get(fp.getFlow());
			 }else if(fp.getOper().equals("中国电信")){
				  standarPrice=dxStandardPriceMap.get(fp.getFlow());
			 }
			 if(standarPrice!=null){
				 BigDecimal discount=product.getPrice().divide(new BigDecimal(standarPrice),3, BigDecimal.ROUND_DOWN);
				 product.setDiscount(discount);
			 }
			list.add(product);
		 }
		 return list;
	}
	
	
	public static void main(String[] args) throws Exception {
		String range="";
		String operator="";
		ApplicationContext a = new ClassPathXmlApplicationContext(new String[] { "spring-application.xml" });
		FlowProductDao d = (FlowProductDao) a
				.getBean(StringUtils.uncapitalize(FlowProductDao.class.getSimpleName()));
		POIService poiService=(POIService)a.getBean("POIServiceFactory");
		String[] titles = {"原价","折扣价格", "折扣", "地区", "运营商", "流量类型","产品名称","店铺名称","产品链接","店铺链接"};
		SXSSFWorkbook sxsSFWorkbook = new SXSSFWorkbook(Integer.valueOf(1000));
		sxsSFWorkbook.setCompressTempFiles(true);
		String fileName = "产品信息" + range+"_"+operator + System.currentTimeMillis() + ".xlsx";
		
		FlowProduct e=new FlowProduct();
		e.setRegion("广东");
		e.setFlowType("6GB");
		List<FlowProduct> query = d.query(e);
		
		List<String[]> dataList = new ArrayList<String[]>();
		for(FlowProduct p:query){
			try{
				BigDecimal price = p.getPrice();
				BigDecimal discount = p.getDiscount();
				BigDecimal standPrice = price.divide(discount,2,RoundingMode.HALF_UP);
				String standPriceStr = standPrice.toString();
				String discountStr = discount.toString();
				String priceStr = price.toString();
				String region = p.getRegion();
				String operator2 = p.getOperator();
				String flowType = p.getFlowType();
				String name = p.getName();
				String shopName = p.getShopName();
				String shopUrl = p.getShopUrl();
				String[] data={standPriceStr,priceStr,discountStr,region,operator2,flowType,name,shopName,shopUrl};
				dataList.add(data);
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
		Map<String, List<String[]>> dataMap = new HashMap<String, List<String[]>>();
		dataMap.put("sheet", dataList);
		poiService.process(sxsSFWorkbook, dataMap, titles, 0);
		File file=new File("E:\\test.xlsx");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		sxsSFWorkbook.write(os);
		os.flush();
	}
	
	private void export(List<FlowProduct> query,HttpServletResponse resp){
		try{
			String[] titles = {"原价","折扣价格", "折扣", "地区", "运营商", "流量类型","产品名称","店铺名称","产品链接","店铺链接"};
			SXSSFWorkbook sxsSFWorkbook = new SXSSFWorkbook(Integer.valueOf(1000));
			sxsSFWorkbook.setCompressTempFiles(true);
			String fileName = System.currentTimeMillis() + ".xlsx";
			String newFileName = new String(fileName.getBytes(), "ISO8859-1");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + newFileName + "\";");
			resp.setHeader("Set-Cookie", "fileDownload=true; path=/");
			resp.setContentType("application/octet-stream;");
			//
			resp.setCharacterEncoding("utf-8");
			ServletOutputStream os = resp.getOutputStream();
			
			List<String[]> dataList = new ArrayList<String[]>();
			for(FlowProduct p:query){
				try{
					BigDecimal price = p.getPrice();
					BigDecimal discount = p.getDiscount();
					BigDecimal standPrice = price.divide(discount,2,RoundingMode.HALF_UP);
					String standPriceStr = standPrice.toString();
					String discountStr = discount.toString();
					String priceStr = price.toString();
					String region = p.getRegion();
					String operator2 = p.getOperator();
					String flowType = p.getFlowType();
					String name = p.getName();
					String shopName = p.getShopName();
					String shopUrl = p.getShopUrl();
					String[] data={standPriceStr,priceStr,discountStr,region,operator2,flowType,name,shopName,shopUrl};
					dataList.add(data);
				}catch(Exception e1){
					e1.printStackTrace();
				}
			}
			Map<String, List<String[]>> dataMap = new HashMap<String, List<String[]>>();
			dataMap.put("sheet", dataList);
			poiService.process(sxsSFWorkbook, dataMap, titles, 0);
			sxsSFWorkbook.write(os);
			os.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private  Map<String,String> getFlowTypes(String condition){
		if(condition!=null){
			condition="prop="+condition;
		}else{
			condition="";
		}
		String newurl=url.replace("fzscondition", condition);
		System.out.println("newUrl=="+newurl);
		Document doc = Util.getFromTmail(newurl,false);
		 Elements elements=doc.select(".av-collapse li");
		for (Element element : elements) {
			Elements ells = element.select("[data-i]");
			System.out.println(element.html());
			String prop = ells.attr("data-i");
			if (prop.startsWith("11246843")) {// 流量类型
				flowMap.put(element.text(), prop);
			}
		}
		 System.out.println(flowMap);
		return flowMap;
	}
	
	static {
		provCodeMap.put("全国", "20780:30680");
		provCodeMap.put("广东", "20780:30517");
		provCodeMap.put("山东", "20780:30501");
		provCodeMap.put("江苏", "20780:30511");
		provCodeMap.put("四川", "20780:30508");
		provCodeMap.put("浙江", "20780:30510");
		provCodeMap.put("河北", "20780:30499");
		provCodeMap.put("山西", "20780:30502");
		provCodeMap.put("海南", "20780:30520");
		provCodeMap.put("甘肃", "20780:30505");
		provCodeMap.put("贵州", "20780:30516");
		provCodeMap.put("西藏", "20780:27009");
		provCodeMap.put("青海", "20780:30504");
		provCodeMap.put("宁夏", "20780:30507");
		provCodeMap.put("新疆", "20780:30506");
		provCodeMap.put("江西", "20780:30512");
		provCodeMap.put("重庆", "20780:29404");
		provCodeMap.put("内蒙", "20780:30495");
		provCodeMap.put("黑龙", "20780:30496");
		provCodeMap.put("吉林", "20780:30497");
		provCodeMap.put("云南", "20780:30515");
		provCodeMap.put("河南", "20780:30500");
		provCodeMap.put("湖南", "20780:30514");
		provCodeMap.put("广西", "20780:30518");
		provCodeMap.put("湖北", "20780:30513");
		provCodeMap.put("上海", "20780:29423");
		provCodeMap.put("安徽", "20780:30509");
		provCodeMap.put("辽宁", "20780:30498");
		provCodeMap.put("陕西", "20780:30503");
		provCodeMap.put("天津", "20780:29428");
		provCodeMap.put("北京", "20780:29400");
		provCodeMap.put("福建", "20780:30519");

		operatorCodeMap.put("中国移动", "21228:3236139");
		operatorCodeMap.put("中国联通", "21228:137815084");
		operatorCodeMap.put("中国电信", "21228:138238560");
		
		ydStandardPriceMap.put("10MB", "3");
		ydStandardPriceMap.put("30MB", "5");
		ydStandardPriceMap.put("70MB", "10");
		ydStandardPriceMap.put("150MB", "20");
		ydStandardPriceMap.put("500MB", "30");
		ydStandardPriceMap.put("1GB", "50");
		ydStandardPriceMap.put("2GB", "70");
		ydStandardPriceMap.put("3GB", "100");
		ydStandardPriceMap.put("4GB", "130");
		ydStandardPriceMap.put("6GB", "180");
		ydStandardPriceMap.put("11GB", "280");
		
		ltStandardPriceMap.put("20MB", "3");
		ltStandardPriceMap.put("30MB", "5");
		ltStandardPriceMap.put("50MB", "6");
		ltStandardPriceMap.put("100MB", "10");
		ltStandardPriceMap.put("200MB", "15");
		ltStandardPriceMap.put("300MB", "20");
		ltStandardPriceMap.put("500MB", "30");
		ltStandardPriceMap.put("1GB", "50");
		ltStandardPriceMap.put("2GB", "70");
		
		dxStandardPriceMap.put("5MB", "1");
		dxStandardPriceMap.put("10MB", "2");
		dxStandardPriceMap.put("30MB", "5");
		dxStandardPriceMap.put("50MB", "7");
		dxStandardPriceMap.put("100MB", "10");
		dxStandardPriceMap.put("200MB", "15");
		dxStandardPriceMap.put("500MB", "30");
		dxStandardPriceMap.put("1GB", "50");
		dxStandardPriceMap.put("2GB", "70");
	}
	
	
	
}

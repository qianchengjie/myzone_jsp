package service;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dao.user.UserDao;
import model.user.User;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;


public class DataService {

	@Resource
	private UserDao userDao = new UserDao();
	
	@Resource 
	private UserService userService = new UserService();
	
	/**
	 * 获得用户Excel表
	 * @return
	 * @throws IOException 
	 */
	public void getUserExcel(HttpServletResponse res,OutputStream os) throws IOException{
		setHeader(res, "vnd.ms-excel","excel.xls");
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("用户表");
		Iterable<User> users = userDao.findAll();
		HSSFRow row = sheet.createRow(0);
		String[] header = {"ID","用户名","邮箱","注册时间","头像URL"};
		for(int i = 0; i <= 4; i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(header[i]);
		}
		int index = 1;
		for(User user:users){
			row = sheet.createRow(index);
			String[] userValue = {
					user.getId()+"",
					user.getUsername(),
					user.getEmail(),
					user.getRegDate(),
					user.getImgSrc()
					};
			for(int i = 0; i <= 4; i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(userValue[i]);
			}
			index++;
		}
		wb.write(os);
		wb.close();
	}
	/**
	 * 上传用户Excel
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String uploadUserExcel(HttpServletRequest req) throws Exception {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024*1024*3);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1024*1024*10);
		upload.setFileSizeMax(1024*1024*20);
        upload.setHeaderEncoding("UTF-8");
        
        List<FileItem> formItems = upload.parseRequest(req);
        FileItem file = formItems.get(0);
	    HSSFWorkbook book = new HSSFWorkbook(file.getInputStream());
	    HSSFSheet sheet = book.getSheetAt(0);
	    
	    int usernameRepeat = 0;
	    int emailRepeat = 0;
	    int wrong = 0;
	    int allSum = sheet.getLastRowNum();;
	    int sum = sheet.getLastRowNum();
	    User user = new User();
	    for(int i = 1; i <= sheet.getLastRowNum(); i++) {
	    	user = new User();
	        HSSFRow row = sheet.getRow(i);
	        boolean flag = false;
	        for(int j = 0; j < row.getLastCellNum(); j++)
	        	row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
	        for(int j = 0; j < row.getLastCellNum(); j++){
	        	if(row.getCell(j).getStringCellValue() == null || row.getCell(j).getStringCellValue().equals("")){
	        		wrong++;
	        		sum--;
	    	        flag = true;
	        	}
	        }
	        if(userDao.getUsername(row.getCell(0).getStringCellValue()) != null){
	        	usernameRepeat++;
	        	if(!flag){
	        		sum--;
	        	}
		        flag = true;
	        }
	        if(userDao.getEmail(row.getCell(2).getStringCellValue()) != null){
	        	emailRepeat++;
	        	if(!flag){
	        		sum--;
	        	}
		        flag = true;
	        }
	        if(flag){
	        	continue;
	        }
	        user.setUsername(row.getCell(0).getStringCellValue());
	        user.setPassword(row.getCell(1).getStringCellValue());
	        user.setEmail(row.getCell(2).getStringCellValue());
	        user.setRegDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        user.setImgSrc("http://oq7avxrj8.bkt.clouddn.com/images/headimg/01.jpg");
	        userService.register(user);
	    }
	    book.close();
	    return "一共提交" + allSum + "条记录，成功" 
	    		+ sum + "条，用户名重复"
	    		+ usernameRepeat +"条，邮箱重复"
	    		+ emailRepeat +"条，"
	    		+ wrong + "条未填写完全。";
	}
	/**
	 * 获得用户pdf
	 * @return
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public void getUserPdf(HttpServletRequest req, HttpServletResponse res,OutputStream os, String realPath) throws DocumentException, IOException{
		setHeader(res, "pdf","PDF.pdf");
		res.setHeader("content-Type", "application/pdf;charset=UTF-8");
		
		Document document = new Document();
		document.setMargins(10, 15, 10, 15); 
		
        
		PdfWriter.getInstance(document, res.getOutputStream());
        
		 // 设置中文字体
        BaseFont bfChinese =
                BaseFont.createFont(realPath + "simhei.ttf",BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        //字体
        Font titleChinese = new Font(bfChinese, 20, Font.BOLD);
        Font headerChinese = new Font(bfChinese, 10, Font.NORMAL);
        Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);

		document.open();
		
        Paragraph title = new Paragraph("用户表",titleChinese);
        //设置标题格式对齐方式
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        float[] whdths = {4f,10f,15f,15f,15f}; 

		String[] header = {"ID","用户名","邮箱","注册时间","头像URL"};
		PdfPTable table = new PdfPTable(whdths); 
	    PdfPCell cell = new PdfPCell();
		for(int i = 0; i <= 4; i++){
		     cell.setPhrase(new Paragraph((new Paragraph(header[i],headerChinese))));
			 table.addCell(cell);
			 document.add(table);
		}
		
		Iterable<User> users = userDao.findAll();
	    for (User user : users) {
			table = new PdfPTable(whdths); 
			cell = new PdfPCell();
			String[] userValue = {
						user.getId()+"",
						user.getUsername(),
						user.getEmail(),
						user.getRegDate(),
						user.getImgSrc()
							};
			for(int i = 0; i <= 4; i++){
				cell.setPhrase(new Paragraph((new Paragraph(userValue[i],fontChinese))));
				table.addCell(cell);
			    document.add(table);
			}
	    }
	    
	    document.newPage();
	    
	    

	    String nowPath = req.getServletContext().getRealPath("");
	    String path = nowPath.substring(0, nowPath.lastIndexOf("webapps")) + "logs/my.log";
	    
	    //设置日志
	    title = new Paragraph("日志记录",titleChinese);
        //设置标题格式对齐方式
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20);
        title.setSpacingAfter(20);
        document.add(title);
        
        //读取日志文件
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while ( (line = br.readLine()) != null ) {    
            if(!line.trim().equals("")){  
            	Paragraph p = new Paragraph(line, fontChinese);
            	p.setFirstLineIndent(2);
            	document.add(p);
            }                      
        }   
        br.close();
        
	    document.close();
	}
	/**
	 * 下载word
	 * @param res
	 * @param os
	 * @param contentMap
	 * @throws Exception
	 */
	public void getWord(HttpServletRequest req, HttpServletResponse res, OutputStream os, String realPath) throws Exception {
		setHeader(res, "x-download","Word.doc");
	    File file = new File(realPath+"word.doc");
		FileInputStream tempFileInputStream = new FileInputStream(file);
	    HWPFDocument document = new HWPFDocument(tempFileInputStream);
	    
	    //读取日志文件
	    String nowPath = req.getServletContext().getRealPath("");
	    String path = nowPath.substring(0, nowPath.lastIndexOf("webapps")) + "logs/my.log";
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);
        
        int cms = 0, blackboard = 0, index = 0;
        int leaveSum = 0, deleteSum = 0, replySum = 0, zanSum = 0;
        int fileSum = 0, userSum = 0;
        
        //统计
        String line = "";
        while ( (line = br.readLine()) != null ) {    
            if(!line.trim().equals("")){  
            	if(line.indexOf("访问网页：首页") != -1)
            		index++; 
            	if(line.indexOf("访问网页：留言板") != -1)
            		blackboard++; 
            	if(line.indexOf("访问网页：后台") != -1)
            		cms++; 
            	if(line.indexOf("操作：新增留言") != -1)
            		leaveSum++; 
            	if(line.indexOf("操作：回复，回复楼层id") != -1)
            		replySum++; 
            	if(line.indexOf("操作：赞，赞楼层id") != -1)
            		zanSum++; 
            	if(line.indexOf("删除留言，删除楼层id") != -1)
            		deleteSum++;
            	if(line.indexOf("修改用户") != -1 || line.indexOf("删除用户") != -1 ||line.indexOf("更新角色") != -1)
            		userSum++; 
            	if(line.indexOf("上传文件") != -1 || line.indexOf("下载文件") != -1 
            			|| line.indexOf("删除文件") != -1 || line.indexOf("删除目录") != -1
            			|| line.indexOf("移动文件") != -1 || line.indexOf("移动目录") != -1
            			|| line.indexOf("重命名文件") != -1 || line.indexOf("重命名目录") != -1
            			|| line.indexOf("新建目录") != -1 || line.indexOf("上传用户EXCEL表") != -1)
            		fileSum++;
            }                      
        }   
        br.close();
        
	    Map<String, String> contentMap = new HashMap<>();
	    contentMap.put("cms", cms+"");
	    contentMap.put("blackboard", blackboard+"");
	    contentMap.put("index", index+"");
	    contentMap.put("leaveSum", leaveSum+"");
	    contentMap.put("deleteSum", deleteSum+"");
	    contentMap.put("replySum", replySum+"");
	    contentMap.put("zanSum", zanSum+"");
	    contentMap.put("fileSum", fileSum+"");
	    contentMap.put("userSum", userSum+"");
	    contentMap.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	    
	    // 读取文本内容
	    Range bodyRange = document.getRange();
	    // 替换内容
	    for (Map.Entry<String, String> entry : contentMap.entrySet()) {
	        bodyRange.replaceText("${" + entry.getKey() + "}", entry.getValue());
	    }

	    document.write(os);
	    document.close();
	}
	/**
	 * 设置编码
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public void setHeader(HttpServletResponse res, String fileType, String fileName) throws UnsupportedEncodingException{
		res.setHeader("content-Type", "application/" + fileType + ";charset=UTF-8");
		res.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(), "ISO8859-1"));
	}
	
}

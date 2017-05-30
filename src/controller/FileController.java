package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;

import service.FileService;

public class FileController extends HttpServlet {

	private FileService fileService = new FileService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1);
		
		String json = "";
		switch(action){
		case "getFileList":
			json = JSON.toJSONString(fileService.getFileList(req.getParameter("path")));
			break;
		}
		out.print(json);
		
		out.flush();
		out.close();
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();

		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1);
		
		String json = "";
		switch(action){
		case "doUpload":
			json = JSON.toJSONString(fileService.doUpload(req));
			break;
		case "getFileList":
			json = JSON.toJSONString(fileService.getFileList(req.getParameter("path")));
			break;
		case "getFileInfo":
			json = JSON.toJSONString(fileService.getFileInfo(req.getParameter("key")));
			break;
		case "download":
			json = JSON.toJSONString(fileService.download(req.getParameter("prefix"), req.getParameter("filename")));
			break;
		case "deleteFile":
			json = JSON.toJSONString(fileService.delete(req.getParameter("key")));
			break;
		case "deleteFolder":
			json = JSON.toJSONString(fileService.deleteFolder(req.getParameter("path")));
			break;
		case "removeFile":
			json = JSON.toJSONString(fileService.remove(req.getParameter("fromKey"), req.getParameter("toKey")));
			break;
		case "removeFolder":
			json = JSON.toJSONString(fileService.removeFolder(req.getParameter("fromKey"), req.getParameter("toKey")));
			break;
		case "renameFile":
			json = JSON.toJSONString(fileService.rename(req.getParameter("key"), req.getParameter("newKey")));
			break;
		case "renameFolder":
			json = JSON.toJSONString(fileService.renameFolder(req.getParameter("path"), req.getParameter("newPath")));
			break;
		case "mkDir":
			json = JSON.toJSONString(fileService.mkDir(req.getParameter("path")));
			break;
		}
		out.print(json);
		
		out.flush();
		out.close();
	}

}

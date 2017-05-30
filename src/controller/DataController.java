package controller;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.fastjson.JSON;
import com.itextpdf.text.DocumentException;

import service.DataService;

public class DataController extends HttpServlet {

	private DataService dataService = new DataService();

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		String viewName = "";
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1);
		switch(action){
		case "getUserExcel":
			dataService.getUserExcel(res,res.getOutputStream());
			break;
		case "getWord":
			try {
				dataService.getWord(req, res, res.getOutputStream(),req.getSession().getServletContext().getRealPath("/"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "getUserPdf":
			try {
				dataService.getUserPdf(req, res,res.getOutputStream(), req.getSession().getServletContext().getRealPath("/"));
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
			break;
		case "data":
			viewName = "/WEB-INF/views/data.jsp";
			break;
		}
		if(!"".equals(viewName)){
			RequestDispatcher rd = req.getRequestDispatcher(viewName);
			rd.forward(req, res);
		}
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=utf-8");
		String viewName = "";
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1);
			switch(action){
			case "uploadUserExcel":
				try {
					res.getWriter().write(JSON.toJSONString(dataService.uploadUserExcel(req)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
	}

}

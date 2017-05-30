package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.StringUtil;

import com.alibaba.fastjson.JSON;

import service.CMSService;
import service.UserService;

public class CMSController extends HttpServlet {

	private CMSService cMSService = new CMSService();
	private UserService userService = new UserService();
	private StringUtil su = new StringUtil();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/cms.jsp");
		req.setAttribute("userList", cMSService.getUserList(1));
		req.setAttribute("rights", cMSService.getRightList());
		HttpSession ses = req.getSession();
		String username = (String) ses.getAttribute("username");
		if(username == null || !userService.isHavaThisRight(username, 5) && !userService.isHavaThisRight(username, 6)){
			req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, res);
		}else{
			if(userService.isHavaThisRight(username, 5))
				req.setAttribute("right5", true);
			if(userService.isHavaThisRight(username, 6))
				req.setAttribute("right6", true);
			if(userService.isHavaThisRight(username, 7))
				req.setAttribute("right7", true);
			if(userService.isHavaThisRight(username, 8))
				req.setAttribute("right8", true);
			rd.forward(req, res);
		}
		out.flush();
		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		HttpSession ses = req.getSession();
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1);
		String username = "", rolename = "", json = "";
		int currentPage = 0,pageNum = 0;
		switch( action ){
		case "changeRole":
			username = req.getParameter("username");
			rolename = req.getParameter("rolename");
			json = JSON.toJSONString(cMSService.changeRole(username, rolename, (String)ses.getAttribute("username")));
			break;
		case "deleteUser":
			username = req.getParameter("username");
			currentPage = su.stringToInteger(req.getParameter("currentPage"));
			json = JSON.toJSONString(cMSService.deleteUser(username, currentPage));
			break;
		case "getUserPageSum":
			json = JSON.toJSONString(cMSService.searchUserPageSum(req.getParameter("condition")));
			break;
		case "getUserPage":
			pageNum = su.stringToInteger(req.getParameter("pageNum"));
			json = JSON.toJSONString(cMSService.searchUserList(pageNum, req.getParameter("condition")));
			break;
		case "searchUserPage":
			pageNum = su.stringToInteger(req.getParameter("pageNum"));
			json = JSON.toJSONString(cMSService.searchUserList(pageNum, req.getParameter("condition")));
			break;
		case "getRoleRight":
			json = JSON.toJSONString(cMSService.getRoleRight(req.getParameter("rolename")));
			break;
		case "updateRoleRight":
			json = JSON.toJSONString(cMSService.updateRoleRight(req.getParameter("rolename"), req.getParameterValues("rightsId")));
			break;
		}
		out.print(json);
		out.flush();
		out.close();
	}
}

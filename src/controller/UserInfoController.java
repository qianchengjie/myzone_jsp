package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import model.user.User;
import service.UserService;

public class UserInfoController extends HttpServlet {

	private UserService userService = new UserService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		HttpSession ses = req.getSession();
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/userinfo.jsp");
		if(ses.getAttribute("username") == null){
			res.sendRedirect("/");
		}else{
			req.setAttribute("user", userService.getUser((String)ses.getAttribute("username")));
			rd.forward(req, res);
		}
		
		out.flush();
		out.close();
	}


	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();

		String json = "", msg = "";
		
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1,uri.length());
		switch(action){
		case "getUsername":
			json = JSON.toJSONString((String)session.getAttribute("username"));
			break;
		case "checkUsername":
			json = JSON.toJSONString(userService.checkUsername(req.getParameter("newUsername"), (String)session.getAttribute("username")));
			break;
		case "modifyUsername":
			String newUsername = req.getParameter("newUsername");
			msg = userService.modifyUsername(newUsername, userService.getUser((String)session.getAttribute("username")));
			json = JSON.toJSONString(msg);
			if(msg.equals("更名成功")){
				session.setAttribute("username", newUsername);
			}
			break;
		case "checkPassword":
			json = JSON.toJSONString(userService.checkUsername((String)session.getAttribute("username"),req.getParameter("newPassword")));
			break;
		case "modifyPassword":
			json = JSON.toJSONString(userService.modifyPassword(userService.getUser((String)session.getAttribute("username")),req.getParameter("newPassword")));
			break;
		}
		out.print(json);
		
		out.flush();
		out.close();
	}

}

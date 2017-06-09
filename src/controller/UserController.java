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
import util.Param2Bean;
import util.StringUtil;

public class UserController extends HttpServlet {

	private StringUtil su = new StringUtil();
	private Param2Bean p2b = new Param2Bean();
	private UserService userService = new UserService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("text/html;charset=utf-8");
		
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/fp.jsp");
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1,uri.length());
		String email = "";
		
		switch(action){
			case "forgetPassword":
				email = req.getParameter("email");
				req.setAttribute("msg", userService.forgetPassword(email));
				break;
		}
		rd.forward(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();

		User user = new User();
		String json = "", msg = "";
		
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1,uri.length());
		String email = "";
		switch(action){
		case "register":
			user = p2b.add_param2Bean(req, user);
			msg = userService.register(user);
			if(msg.equals("注册成功")){
				session.setAttribute("username", user.getUsername());
				session.setAttribute("userHeadImg", userService.getImgSrc(user.getUsername()));
			}
			json = JSON.toJSONString(msg);
			break;
		case "login":
			user = p2b.edit_param2Bean(req, new User());
			msg = userService.login(user);
			if(msg.equals("登录成功")){
				session.setAttribute("username", user.getUsername());
				session.setAttribute("userHeadImg", userService.getImgSrc(user.getUsername()));
			}
			json = JSON.toJSONString(msg);
			break;
		case "checkusername":
			String username = req.getParameter("username");
			json = JSON.toJSONString(userService.checkUsername(username));
			break;
		case "checkemail":
			email = req.getParameter("email");
			json = JSON.toJSONString(userService.checkEmail(email));
			break;
		case "logout":
			session.removeAttribute("username");
			json = JSON.toJSONString("退出成功");
			break;
		}
		out.print(json);
		out.flush();
		out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}

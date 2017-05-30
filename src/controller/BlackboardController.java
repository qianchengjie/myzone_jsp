package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;

import model.blackboard.Floor;
import model.blackboard.Reply;
import service.BlackboardService;
import service.UserService;
import util.Param2Bean;
import util.StringUtil;

public class BlackboardController extends HttpServlet {

	private BlackboardService blackboardService = new BlackboardService();;
	private UserService userService = new UserService();
	
	private StringUtil su = new StringUtil();
	private Param2Bean p2b = new Param2Bean();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		res.setContentType("text/html;charset=utf-8");

		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1,uri.length());
		if(action.equals("getUpToken")){
			res.getWriter().print(JSON.toJSONString(blackboardService.getUpToken()));
		}else{
			HttpSession session = req.getSession();
			int pageNum = 1;
			if( req.getParameter("pageNum") != null)
				pageNum = su.stringToInteger(req.getParameter("pageNum"));
			
			long pageSum = blackboardService.getPageSum();
			
			req.setAttribute("floors", blackboardService.findAll(pageNum));
			req.setAttribute("pageSum", pageSum);
			req.setAttribute("currentPage", pageNum);
			if(session.getAttribute("username") == null){
				//游客
				req.setAttribute("right", 0);
			}else{
				String username = (String) session.getAttribute("username");
				//管理权限
				if(userService.isHavaThisRight(username, 2))
					req.setAttribute("right", 2);
				//普通权限
				else if(userService.isHavaThisRight(username, 3)){
					req.setAttribute("right", 3);
				}else{
					req.setAttribute("right", 0);
				}
			}
			req.getRequestDispatcher("/WEB-INF/views/blackboard.jsp").forward(req, res);
		}
		
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		
		res.setContentType("text/html;charset=utf-8");
		PrintWriter out = res.getWriter();
		

		int floorId,currentPage;
		Reply reply = new Reply();
		Floor floor = new Floor();
		String json = "";
		
		String uri = req.getRequestURI();
		String action = uri.substring(uri.lastIndexOf("/")+1,uri.length());
		switch(action){
		case "deleteMessage":
			floorId = su.stringToInteger(req.getParameter("floorId"));
			currentPage = su.stringToInteger(req.getParameter("currentPage"));
			json = JSON.toJSONString(blackboardService.deleteMessage(floorId,currentPage));
			break;
		case "leaveMessage":
			floor = p2b.add_param2Bean(req,floor);
			json = JSON.toJSONString(blackboardService.leaveMessage(floor));
			break;
		case "getpagesum":
			json = JSON.toJSONString(blackboardService.getPageSum());
			break;
		case "viewReply":
			floorId = su.stringToInteger(req.getParameter("floorId"));
			json = JSON.toJSONString(blackboardService.findAllReply(floorId));
			break;
		case "reply":
			reply = p2b.add_param2Bean(req, reply);
			json = JSON.toJSONString(blackboardService.reply(reply));
			break;
		case "zan":
			floorId = su.stringToInteger(req.getParameter("floorId"));
			json = JSON.toJSONString(blackboardService.zan(floorId));
			break;
		}
		
		out.print(json);
		
		out.flush();
		out.close();
	}

}

package filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import service.UserService;


public class LogFilter implements Filter {
	
	private static Logger log = Logger.getLogger(LogFilter.class); 
	private UserService userService = new UserService();
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		HttpSession ses = req.getSession();
		String uri = req.getRequestURI();
		
		//黑名单拦截
		if(getIpAddr(req).indexOf("123.151.43.110")!=-1){
			gotoErrorPage("403", "由于您的IP访问过于频繁，已被加入黑名单。", req ,res);
			return;
		}
		
		String action = uri.substring(uri.lastIndexOf("/")+1);
		
		String viewName = "";
		String username = "";
		String userAction = "";
		if(ses.getAttribute("username") != null)
			username = (String) ses.getAttribute("username");
		
		//权限拦截
		switch( action ){
			case "cms":
				if(cmsRight(username, req, res, -1))
					return;
			break;
			case "deleteMessage":case "leaveMessage":case "reply":
				if(username.equals("")){
					gotoErrorPage("403", "您没有此权限", req, res);
					return;
				}
			break;
			case "changeRole":case "deleteUser":case "updateRoleRight":
				if(cmsRight(username, req, res, 6))
					return;
			break;
			case "doUpload":case "download":case "removeFile":case "mkDir":case "renameFolder":case "removeFolder":
				if(cmsRight(username, req, res, 7))
					return;
			break;
			case "deleteFile":case "deleteFolder":
				if(cmsRight(username, req, res, 8))
					return;
			break;
			case "getUserExcel":case "getWord":case "data":case "getUserPdf":case "uploadUserExcel":
				if(cmsRight(username, req, res, -1))
					return;
				break;
			case "logout":case "modifyUsername":case "modifyPassword":
				if(username.equals("")){
					gotoErrorPage("403", "您没有此权限", req, res);
				}
				break;
		}
		
		//访问页面
		switch(action){
		case "":
			viewName = "首页";
			break;
		case "blackboard":
			viewName = "留言板";
			break;
		case "userinfo":
			viewName = "用户信息";
			break;
		case "cms":
			viewName = "后台";
			break;
		case "data":
			viewName = "上传用户EXCEL";
		}
		if(viewName != "" && username != "")
			log.info("访问IP：" + getIpAddr(req) + "，       用户：" + username  + "，       访问网页：" + viewName );
		else if(viewName != ""){
			log.info("访问IP：" + getIpAddr(req) + "，       用户：游客" + "，       访问网页：" + viewName );
		}
		
		
		//用户操作
		switch(action){
		case "login":
			username = (String) req.getAttribute("username");
			userAction = "登录";
			break;
		case "register":
			username = (String) req.getAttribute("username");
			userAction = "注册";
			break;
		case "logout":
			username = (String) ses.getAttribute("username");
			userAction = "登出";
			break;
		case "modifyUsername":
			username = (String) ses.getAttribute("username");
			userAction = "修改用户名";
			break;
		case "modifyPassword":
			username = (String) ses.getAttribute("username");
			userAction = "修改密码";
			break;
		}
		
		//留言板操作
		switch(action){
		case "deleteMessage":
			userAction = "删除留言，删除楼层id " + req.getParameter("floorId");
			break;
		case "leaveMessage":
			userAction = "新增留言";
			break;
		case "reply":
			userAction = "回复，回复楼层id " + req.getParameter("floorId");
			break;
		case "zan":
			userAction = "赞，赞楼层id " + req.getParameter("floorId");
			break;
		}
		
		//用户管理
		switch( action ){
		case "changeRole":
			userAction = "修改用户" + req.getParameter("username") + "的角色为" + req.getParameter("rolename");
			break;
		case "deleteUser":
			userAction = "删除用户" + req.getParameter("username");
			break;
		case "updateRoleRight":
			userAction = "更新角色" + req.getParameter("rolename") + "的权限";
			break;
		}
		
		//文件管理
		switch(action){
		case "doUpload":
			userAction = "上传文件";
			break;
		case "download":
			userAction = "下载文件 " + req.getParameter("filename");
			break;
		case "deleteFile":
			userAction = "删除文件 " + req.getParameter("key");
			break;
		case "deleteFolder":
			userAction = "删除目录 " + req.getParameter("path");
			break;
		case "removeFile":
			userAction = "移动文件 " + req.getParameter("fromKey") + "，到 " +  req.getParameter("toKey");
			break;
		case "removeFolder":
			userAction = "移动目录 " + req.getParameter("fromKey") + "，到 " +  req.getParameter("toKey");
			break;
		case "renameFile":
			userAction = "重命名文件 " + req.getParameter("key") + "，为 " + req.getParameter("newKey");
			break;
		case "renameFolder":
			userAction = "重命名目录 " + req.getParameter("path") + "，为 " + req.getParameter("newPath");
			break;
		case "mkDir":
			userAction = "新建目录 " + req.getParameter("path");
			break;
		}

		//报表操作
		switch( action ){
		case "getUserExcel":
			userAction = "下载用户EXCEL表";
			break;
		case "getWord":
			userAction = "下载统计资料";
			break;
		case "getUserPdf":
			userAction = "下载详细统计资料";
			break;
		case "uploadUserExcel":
			userAction = "上传用户EXCEL";
			break;
		}
		
		
		
		
		if(username != "" && username != null && userAction != "")
			log.info("访问IP：" + getIpAddr(req) + "，       用户：" + username + "，       操作：" +  userAction);
		else if(userAction != "")
			log.info("访问IP：" + getIpAddr(req) + "，       用户：游客" + "，       操作：" +  userAction);
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	/**
	 * 获取用户IP地址
	 * @param request
	 * @return
	 */
	protected String getIpAddr(HttpServletRequest request) {    
	    String ip = request.getHeader("x-forwarded-for");    
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
	        ip = request.getHeader("Proxy-Client-IP");    
	    }    
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
	        ip = request.getHeader("WL-Proxy-Client-IP");    
	    }    
	    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
	        ip = request.getRemoteAddr();    
	    }    
	    return ip;    
	}  
	
	/**
	 * 前往错误页面
	 * @param code
	 * @param msg
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
	public void gotoErrorPage(String code, String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		Map<String, String> error = new HashMap<>();
		error.put("code", code);
		error.put("msg", msg);
		req.setAttribute("error", error);
		req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, res);
	}
	
	public boolean cmsRight(String username, HttpServletRequest req, HttpServletResponse res, int right) throws ServletException, IOException{
		boolean flag = false;
		if(username.equals("")){
			flag = true;
		}else if(!userService.isHavaThisRight(username, 5)
				&&!userService.isHavaThisRight(username, 6)
				&&!userService.isHavaThisRight(username, 7)
				&&!userService.isHavaThisRight(username, 8)){
			flag = true;
		}else if( right!=-1 && !userService.isHavaThisRight(username, right)){
			flag = true;
		}
		if(flag){
			gotoErrorPage("403", "您没有此权限访问", req, res);
		}
		return flag;
	}

}

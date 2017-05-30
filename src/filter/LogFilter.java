package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


public class LogFilter implements Filter {
	
	private static Logger log = Logger.getLogger(LogFilter.class); 
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		HttpSession ses = req.getSession();
		String uri = req.getRequestURI();
		
		String action = uri.substring(uri.lastIndexOf("/")+1);
		
		String viewName = "";
		String username = "";
		String userAction = "";
		if(ses.getAttribute("username") != null)
			username = (String) ses.getAttribute("username");
		
		//访问页面记录
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
		}
		if(viewName != "" && username != "")
			log.info("访问IP：" + getIpAddr(req) + "，       用户：" + username  + "，       访问网页：" + viewName );
		else if(viewName != "")
			log.info("访问IP：" + getIpAddr(req) + "，       用户：游客" + "，       访问网页：" + viewName );
		
		
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
		case "uploadUserExcel":
			userAction = "上传用户EXCEL表";
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

}

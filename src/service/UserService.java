package service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.sun.mail.util.MailSSLSocketFactory;

import dao.user.*;
import model.user.User;
import model.user.UserRoleRelation;
import util.MyUtil;
import util.StringUtil;

public class UserService {

	private MyUtil mu = new MyUtil();
	private StringUtil su = new StringUtil();
	private UserDao userDao = new UserDao();

	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public String register(User user){
		String msg = "注册成功";
		if("".equals( user.getUsername() ) || user.getUsername() == null)
			msg = "用户名不能为空";
		else if("".equals( user.getPassword() ) || user.getPassword() == null)
			msg = "密码不能为空";
		else if("".equals( user.getEmail() ) || user.getEmail() == null)
			msg = "邮箱不能为空";
		else if(userDao.getUsername( user.getUsername() ) != null)
			msg = "用户名已存在";
		else if(userDao.getEmail( user.getEmail() ) != null)
			msg = "邮箱已被注册";
		else{
			//用户信息默认设置
			user.setImgSrc("http://oq7avxrj8.bkt.clouddn.com/images/headimg/01.jpg");
			user.setRegDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			user.setPassword(mu.getMd5(user.getPassword()));
			user = userDao.save(user);
			UserRoleRelation urr = new UserRoleRelation();
			//用户权限默认设置
			urr.setUId(user.getId());
			urr.setRId(3);
			userDao.save(urr);
		}
		return msg;
	}
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public String login(User user){
		String msg = "登录成功";
		if(userDao.getUsername( user.getUsername() ) == null)
			msg = "用户名不存在";
		else if(!userDao.getPassword( user.getUsername()).equals( mu.getMd5(user.getPassword()) ))
			msg = "密码错误";
		return msg;
	}
	/**
	 * 获得用户ID
	 * @param username
	 * @return
	 */
	public int getUserId(String username){
		return userDao.getUserId(username);
	}
	/**
	 * 用户名验证
	 * @param username
	 * @return
	 */
	public String checkUsername(String username){
		String msg = "用户名可用";
		if(userDao.getUsername(username) != null)
			msg = "用户名已存在";
		return msg;
	}
	/**
	 * 邮箱验证
	 * @param email
	 * @return
	 */
	public String checkEmail(String email){
		String msg = "邮箱可用";
		if(userDao.getEmail(email) != null)
			msg = "邮箱已被注册";
		return msg;
		
	}
	/**
	 * 获得用户对象
	 * @param username
	 * @return
	 */
	public User getUser(String username){
		return userDao.getOneUser(username);
	}
	/**
	 * 获得头像
	 * @param usernmae
	 * @return
	 */
	public String getImgSrc(String usernmae){
		return userDao.getImgSrc(usernmae);
	}
	/**
	 * 获得角色ID
	 * @param uId
	 * @return
	 */
	public int getRoleId(int uId){
		return userDao.getrId(uId);
	}
	/**
	 * 获得角色ID
	 * @param rname
	 * @return
	 */
	public int getRoleId(String rolename){
		return userDao.getrId(rolename);
	}
	/**
	 * 获得角色名
	 * @param rId
	 * @return
	 */
	public String getRolename(int roleId){
		return userDao.getRoleName(roleId);
	}
	/**
	 * 获得权限ID组
	 * @param roleId
	 * @return
	 */
	public Vector<String[]> getRightIdListId(int roleId){
		return userDao.getHaveRightId(roleId);
	}
	/**
	 * 获得拥有权限ID组
	 * @param roleId
	 * @return
	 */
	public Vector<String[]> getHaveRightIdListId(int roleId){
		return userDao.getHaveRightId(roleId);
	}
	/**
	 * 获得权限名
	 * @param rightId
	 * @return
	 */
	public ArrayList<String> getRightNameList(Vector<String[]> vector){
		ArrayList<String> rightNameList = new ArrayList<>();
		for(String[] roleId:vector){
			rightNameList.add(userDao.getRightName(su.stringToInteger(roleId.toString())));
		}
		return rightNameList;
	}
	/**
	 * 判断是有拥有此权限
	 * @param roleId
	 * @param rightId
	 * @return
	 */
	public boolean isHavaThisRight(String username, int rightId){
		for(String[] right : getHaveRightIdListId(getRoleId(userDao.getUserId(username)))){
			if( right[0].equals(rightId+"")){
				return true;
			}
		}
		return false;
	}
	/**
	 * 返回全部用户信息
	 * @return
	 */
	public ArrayList<Map<String, Object>> getAllURR(){
		ArrayList<Map<String, Object>> uRRList = new ArrayList<>();
		
		Iterable<User> users = userDao.findAll();
		for(User user:users){
			Map<String, Object> map = new HashMap<>();
			map.put("username", user.getUsername());
			map.put("rolename", getRolename(getRoleId(user.getId())));
			map.put("rightnamelist", getRightNameList(getHaveRightIdListId(getRoleId(user.getId()))));
			uRRList.add(map);
		}
		return uRRList;
	}
	/**
	 * 更改用户角色
	 * @param username
	 * @param roleName
	 * @return
	 */
	public String changeRole(String username,String rolename){
		String msg = "设置成功";
		UserRoleRelation uRR = userDao.getURR(userDao.getUserId(username));
		uRR.setRId(userDao.getRoleId(rolename));
		userDao.save(uRR);
		return msg;
	}
	/**
	 * 检查用户是否未更改，是否已存在
	 * @param newUsername
	 * @param oldUsername
	 * @return
	 */
	public String checkUsername(String newUsername,String oldUsername){
		String msg = "用户名可以更改";
		if(newUsername.equals(oldUsername))
			msg = "用户名未改变";
		else if(userDao.getUsername(newUsername) != null)
			msg = "该用户名已存在";
		return msg;
	}
	/**
	 * 更改用户名
	 * @param newUsername
	 * @param user
	 * @return
	 */
	public String modifyUsername(String newUsername,User user){
		String msg = "更名成功";
		if(newUsername.equals(user.getUsername())){
			msg = "用户名未更改";
		}else if(userDao.getUsername(newUsername) != null){
			msg = "该用户名已存在";
		}
		else{
			user.setUsername(newUsername);
			userDao.save(user);
		}
		return msg;
	}
	/**
	 * 检测密码是否未变
	 * @param username
	 * @param newPassword
	 * @return
	 */
	public String checkPassword(String username,String newPassword){
		String msg = "密码可以更改";
		if(mu.getMd5(newPassword).equals(userDao.getPassword(username)))
			msg = "密码未改变";
		return msg;
	}
	public String modifyPassword(User user,String newPassword){
		String msg = "改密成功";
		if(mu.getMd5(newPassword).equals(userDao.getPassword(user.getUsername())))
			msg = "密码未改变";
		else{
			user.setPassword(mu.getMd5(newPassword));
			userDao.save(user);
		}
		return msg;
	}
	/**
	 * 删除用户
	 * @param username
	 * @return
	 */
	public Map<String, Object> deleteUser(String username,int currentPage){
		String msg = "删除成功";
		Map<String, Object> map = new HashMap<>();
		long count = userDao.count();
		long pageSum = (count-1)/15+1;
		if(count > 15){
			if(currentPage <  pageSum){
				List<User> list = userDao.findByPage(currentPage+1, 15);
				User user = list.get(0);
				map.put("rolename", getRolename(userDao.getrId(user.getId())));
				map.put("user", user);
			}else{
				msg = "删除成功，此页为尾页";
			}
		}else{
			msg = "删除成功，仅剩一页";
		}
		userDao.delete(userDao.getUserId(username));
		map.put("msg", msg);
		return map;
	}
	/**
	 * 获得用户总数
	 * @return
	 */
	public long getUserSum(){
		return userDao.count();
	}
	/**
	 * 获得搜索用户总数
	 * @return
	 */
	public long searchUserSum(String condition){
		return userDao.searchCount(condition);
	}
	
	/**
	 * 忘记密码
	 * @param email
	 * @return
	 */
	public String forgetPassword(String email){
		if(userDao.getEmail(email) == null)
			return "邮箱不存在";
		else
			return sendModifyPasswordEmail(email);
	}
	
	/**
	 * 邮箱找回密码
	 * @param email
	 * @return
	 */
	public String sendModifyPasswordEmail(String email){
		String msg = "邮件发送成功，请稍等";
		
		Properties props = new Properties();
		
		User user = userDao.getOneUserById(userDao.getUserIdByEmail(email));
		
		// 发送服务器需要身份验证
		props.setProperty("mail.smtp.auth", "true");
		// 设置邮件服务器主机名
		props.setProperty("mail.host", "smtp.qq.com");
		// 发送邮件协议名称
		props.setProperty("mail.transport.protocol", "smtp");
		MailSSLSocketFactory sf;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.ssl.socketFactory", sf);
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Session session = Session.getInstance(props);

		//邮件内容部分
		Message message = new MimeMessage(session);
		try {
			message.setSubject(MimeUtility.encodeText("修改密码",MimeUtility.mimeCharset("UTF-8"), null));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		
		builder.append("<hr /><br /><br />亲爱的<b>" + user.getUsername()+"</b>:<br><br /><br /><br />");
		
		builder.append("<h1 style='text-align:center'>修改密码功能没搞。。直接找本人吧！！！</h1><br /><br /><br /><br /><hr />");
		
		try {
			message.setContent(builder.toString(), "text/html;charset=UTF-8");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		//邮件发送者
		try {
            String nick = "";
			try {
				nick = javax.mail.internet.MimeUtility.encodeText("小小钱", "UTF-8", "B");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
            message.setFrom(new InternetAddress(nick+" <330616153@qq.com>")); 
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try {
			//发送邮件
			Transport transport = session.getTransport();
			transport.connect("smtp.qq.com", "330616153@qq.com", "yaobukeji");
			transport.sendMessage(message, new Address[] { new InternetAddress(email) });
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
}

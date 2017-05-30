package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import dao.user.UserDao;
import model.user.User;
import model.user.UserRight;
import util.StringUtil;

public class CMSService {

	private UserDao userDao = new UserDao();
	
	private UserService userService = new UserService(); 
	
	private StringUtil su = new StringUtil();
	
	/**
	 * 获得用户信息
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, String>> getUserList(int pageNum){
		List<User> users = userDao.findByPage(pageNum, 15);
		List<Map<String, String>> list = new ArrayList<>();
		for(User user:users){
			Map<String, String> map = new HashMap<>();
			map.put("username", user.getUsername());
			map.put("email", user.getEmail());
			map.put("regDate",user.getRegDate());
			map.put("rolename", userService.getRolename(userService.getRoleId(user.getId())));
			list.add(map);
		}
		return list;
	}
	/**
	 * 搜索用户信息
	 * @param pageNum
	 * @return
	 */
	public List<Map<String, String>> searchUserList(int pageNum, String condition){
		List<User> users = userDao.searchByPage(pageNum, 15, sqlLikeFormat(condition));
		List<Map<String, String>> list = new ArrayList<>();
		for(User user:users){
			Map<String, String> map = new HashMap<>();
			map.put("username", user.getUsername());
			map.put("email", user.getEmail());
			map.put("regDate",user.getRegDate());
			map.put("rolename", userService.getRolename(userService.getRoleId(user.getId())));
			list.add(map);
		}
		return list;
	}
	/**
	 * 更改用户角色
	 * @param username
	 * @param roleName
	 * @return
	 */
	public String changeRole(String username, String rolename, String nowUsername){
		int nowRoleId = userService.getRoleId(userService.getUserId(nowUsername));
		int userRoleId = userService.getRoleId(userService.getUserId(username));
		int roleId = userService.getRoleId(rolename);
		if(nowRoleId > roleId){
			return "没有足够权限";
		}else if(nowRoleId > userRoleId){
			return "没有足够权限";
		}
		return userService.changeRole(username, rolename);
	}
	/**
	 * 删除用户
	 * @param username
	 * @return
	 */
	public Map<String, Object> deleteUser(String username,int currentPage){
		return userService.deleteUser(username, currentPage);
	}
	/**
	 * 返回用户总页数
	 * @return
	 */
	public long getUserPageSum(){
		return (userService.getUserSum()-1)/15+1;
	}
	/**
	 * 返回搜索到的用户总页数
	 * @return
	 */
	public long searchUserPageSum(String condition){
		return (userService.searchUserSum(sqlLikeFormat(condition))-1)/15+1;
	}
	/**
	 * LIKE特殊字符处理
	 * @param condition
	 * @return
	 */
	public String sqlLikeFormat(String condition){
		 String[] fbsArr = { "_", "%", "[", "]", "^"  };  
	        for (String key : fbsArr) {  
	            if (condition.contains(key)) {  
	            	condition = condition.replace(key, "\\" + key);  
	            }  
	        }  
		return condition;
	}
	/**
	 * 获得权限树
	 * @return
	 */
	public List<Map<String,List<UserRight>>> getRightList(){
		
		List<Map<String,List<UserRight>>> list = new ArrayList<>();
		
		List<UserRight> pRights =  userDao.findParentRight();
		
		for( int i = 0; i < pRights.size(); i++){
			List<UserRight> cRights = userDao.findChildRight(pRights.get(i).getId());
			List<UserRight> pRight =  new ArrayList<>();
			pRight.add(pRights.get(i));
			Map<String,List<UserRight>> map = new HashMap<>();
			map.put("pRight",pRight);
			map.put("cRights",cRights);
			list.add(map);
		}
		return list;
	}
	/**
	 * 获得角色拥有权限id
	 * @param rolename
	 * @return
	 */
	public List<Integer> getRoleRight(String rolename){
		List<Integer> list = new ArrayList<>();
		Vector<String[]> vec = userDao.getRoleRight(userDao.getRoleId(rolename));
		for(String[] id:vec){
			list.add(su.stringToInteger(id[0]));
		}
		return list;
	}
	/**
	 * 设置角色权限
	 * @param rolename
	 * @param rightname
	 */
	public String updateRoleRight(String rolename, String[] rightsId){
		int roleId = userDao.getRoleId(rolename);
		userDao.clearRoleRight(roleId);
		if(rightsId == null) 
			return "设置成功";
		for( int i = 0; i < rightsId.length ; i++){
			userDao.updateRoleRight(roleId, su.stringToInteger(rightsId[i]));
		}
		return "设置成功";
	}
}

package dao.user;

import java.util.List;
import java.util.Vector;

import model.user.User;
import model.user.UserRight;
import model.user.UserRoleRelation;
import util.DBUtil;
import util.MyUtil;
import util.StringUtil;

public class UserDao {
	
	
	private DBUtil db = new DBUtil();
	private StringUtil su = new StringUtil();
	
	/**
	 * 得到集合中唯一字符串值，若字符串为空则返回 null
	 * @param vec
	 * @return
	 */
	private String getOnlyOne(Vector<String[]> vec){
		if(vec.size() == 0){
			return null;
		}
		return vec.get(0)[0];
	}
	
	public String getImgSrc(String username){
		return getOnlyOne(db.getData("user", new String[]{"img_src"}, "username='" + username + "'"));
	}
	
	public static void main(String[] args) {
		UserDao userDao = new UserDao();
		System.out.println(userDao.getImgSrc("钱程杰"));
	}

	public String getUsername(String username) {
		return getOnlyOne(db.getData("user", new String[]{"username"}, "username='" + username + "'"));
	}

	public String getEmail(String email) {
		return getOnlyOne(db.getData("user", new String[]{"email"}, "email='" + email + "'"));
	}

	public User save(User user) {
		long id = db.insertEntity(user);
		return db.getOneEntity(new User(), "id=" + id);
	}

	public String getPassword(String username) {
		return getOnlyOne(db.getData("user", new String[]{"password"}, "username='" + username +"'"));
	}

	public int getUserId(String username) {
		return su.stringToInteger(getOnlyOne(db.getData("user", new String[]{"id"}, "username='" + username + "'")));
	}
	
	public int getUserIdByEmail(String email) {
		return su.stringToInteger(getOnlyOne(db.getData("user", new String[]{"id"}, "email='" + email + "'")));
	}

	public User getOneUser(String username) {
		return db.getOneEntity(new User(), "username='" + username + "'");
	}
	
	public User getOneUserById(int id) {
		return db.getOneEntity(new User(), "id='" + id + "'");
	}

	public Vector<String[]> getHaveRightId(int roleId) {
		return db.getData("role_right_relation", new String[]{"right_id"}, "role_id=" + roleId + " and rtype=1");
	}

	public String getRightName(int roleId) {
		return getOnlyOne(db.getData("role_right_relation", new String[]{"rname"}, "role_id'=" + roleId + "'"));
	}

	public Iterable<User> findAll() {
		return db.getPageEntity(new User(), 1, 10000, "ASC",  "", "");
	}

	public UserRoleRelation getURR(int userId) {
		return db.getOneEntity(new UserRoleRelation(), "u_id='" + userId + "'");
	}

	public int getRoleId(String rolename) {
		return su.stringToInteger(getOnlyOne(db.getData("user_role", new String[]{"id"}, "rname='" + rolename + "'")));
	}
	
	public int getRightId(String rightname) {
		return su.stringToInteger(getOnlyOne(db.getData("user_right", new String[]{"id"}, "rname='" + rightname + "'")));
	}

	public void save(UserRoleRelation uRR) {
		db.insertEntity(uRR);
	}

	public int getrId(int uId) {
		return su.stringToInteger(getOnlyOne(db.getData("user_role_relation", new String[]{"r_id"}, "u_id=" + uId)));
	}
	
	public int getrId(String rolename) {
		return su.stringToInteger(getOnlyOne(db.getData("user_role", new String[]{"id"}, "rname='" + rolename + "'")));
	}

	public String getRoleName(int roleId) {
		return getOnlyOne(db.getData("user_role", new String[]{"rname"}, "id=" + roleId));
	}

	public void delete(int userId) {
		db.deleteData("user", "id=" + userId);
	}

	public long count() {
		return db.count("user", "");
	}
	
	public long searchCount(String condition) {
		return db.count("user", "username like '%" + condition + "%'");
	}

	public List<User> findByPage(int i, int j) {
		return db.getPageEntity(new User(), i, j, "ASC", "", "");
	}
	
	public List<User> searchByPage(int pageNum, int onPageSum, String condition){
		return db.getPageEntity(new User(), pageNum, onPageSum, "ASC", "username like '%" + condition + "%'", "username like '%" + condition + "%'");
	}

	public List<UserRight> findParentRight() {
		return db.getPageEntity(new UserRight(), 1, 1000, "ASC", "p_id=0", "");
	}
	
	public List<UserRight> findChildRight(int pId) {
		return db.getPageEntity(new UserRight(), 1, 1000, "ASC", "p_id=" + pId, "");
	}
	
	public Vector<String[]> getRoleRight(int roleId){
		return db.getData("role_right_relation", new String[]{"right_id"}, "role_id=" + roleId + " and rtype=1");
	}
	
	public void clearRoleRight(int roleId){
		db.modifyData("role_right_relation", new String[]{"rtype"}, new String[]{"0"}, "role_id=" + roleId);
	}
	
	public void updateRoleRight(int roleId, int rightId){
		db.modifyData("role_right_relation", new String[]{"rtype"}, new String[]{"1"}, "role_id=" + roleId + " and right_id=" + rightId);
	}
}

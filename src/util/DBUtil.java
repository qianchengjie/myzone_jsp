package util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.mysql.jdbc.Statement;

public class DBUtil {
	private Connection conn = null;

	private ResultSet rs = null;

	private PreparedStatement prestmt = null;
	
	public DBUtil(){
		String[] str = readConfigFile();
		try {
			Class.forName(str[0]);
			conn = DriverManager.getConnection(str[1],str[2], str[3]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
//	DBUtil dbc = new DBUtil();
//	String[] str = dbc.readConfigFile();
//	System.out.println(str[0]);
//	String condition = "username='e' and password='e'";
//	boolean b = dbc.CheckedLogin("user", condition);
//	System.out.println(b);
	}

	private  String[] readConfigFile() {
		String[] str = new String[4];
		try{
		Properties props = new Properties();
		InputStream infile = this.getClass().getResourceAsStream("/ConfigFile/DbConfig.properties");
		props.load(infile);

		str[0] = props.getProperty("driver");
		str[1] = props.getProperty("url");
		str[2] = props.getProperty("username");
		str[3] = props.getProperty("password");
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
	/**
	 * delete data by tablename and condition
	 * 
	 * @param tableName
	 * @param condition
	 */
	public boolean deleteData(String tableName, String condition) {
		String sql = "";
		boolean flag = false;
		if(condition==null||condition==""){
		sql = "delete from "+tableName;	
		}else{
		sql = "delete from "+tableName+" where "+condition;
		}
		try {
			prestmt = conn.prepareStatement(sql);
			int rscount = prestmt.executeUpdate();
			if(rscount>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   return flag;
	}
	/**
	 * get data by tablename, fields and condition 
	 * 
	 * @param tableName
	 * @param field
	 * @param condition
	 */
	public Vector<String[]> getData(String tableName, String[] field, String condition) {
		Vector<String[]> vec = new Vector<String[]>();
		String strField = "", sql = "";
		for (int i = 0; i < field.length; i++) {
			strField += field[i] + ",";
		}
		strField = strField.substring(0, strField.lastIndexOf(","));
		if (condition == null || condition == "") {
			sql = "select " + strField + " from " + tableName;
		} else {
			sql = "select " + strField + " from " + tableName + " where "
					+ condition;
		}
		try {
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			while(rs.next()){
				String[] temp = new String[field.length]; 
				for(int i=0;i<field.length;i++){
					temp[i] = rs.getString(field[i]);
					//Debug(temp[i]);
				}
				vec.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return vec;
	
	}
	/**
	 * 获取表中记录总数
	 * @param tableName
	 * @return
	 */
	public long count(String tableName, String condition){
		String sql = "";
		long sum = 0;
		if(condition!=null && !condition.equals(""))
			sql = "select count(*) from " + tableName + " where " + condition ;
		else
			sql = "select count(*) from " + tableName;
		try {
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			while(rs.next())
				sum = rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sum;
	}
	/**
	 * 实体名转化
	 * @param className
	 * @return
	 */
	public String e2s(String className){
		String str = className.substring(className.lastIndexOf(".")+1, className.length());
		StringBuilder result = new StringBuilder();
		if (str != null && str.length() > 0) {
			result.append(str.substring(0, 1).toLowerCase());
			for (int i = 1; i < str.length(); i++) {
				String s = str.substring(i, i + 1);
				if (s.equals(s.toUpperCase())
						&& !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				result.append(s.toLowerCase());
			}
		}
		return result.toString();
	}
	/**
	 * 数据名转化
	 * @param name
	 * @return
	 */
	public static String s2e(String name) {
		StringBuilder result = new StringBuilder();
		if (name == null || name.isEmpty()) {
			return "";
		} else if (!name.contains("_")) {
			return name.substring(0, 1).toLowerCase() + name.substring(1);
		}
		String camels[] = name.split("_");
		for (String camel : camels) {
			if (camel.isEmpty()) {
				continue;
			}
			if (result.length() == 0) {
				result.append(camel.toLowerCase());
			} else {
				result.append(camel.substring(0, 1).toUpperCase());
				result.append(camel.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}
	/**
	 * 查询多条记录，返回单页对应的对象
	 * @param t 实体类：new 一个实体
	 * @param pageNum 页码
	 * @param onePageSum	单页总数
	 * @param method 顺序查询："ASC" ， 倒序：  "DESC"
	 * @param condition 查询条件
	 * @param countCondition 数量查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T>  getPageEntity(T t, int pageNum, int onePageSum, String method, String condition, String countCondition){
		Class<?> clazz = t.getClass();
		T example=null;
		List<T> list=new ArrayList<T>();
		
		//1、获得实体类名作为表名
		String className = clazz.getName();
		String tableName = e2s(className);
		
		//2、计算下标
		int sum = Integer.parseInt(count(tableName, countCondition)+"");
		int startIndex = (pageNum-1)*onePageSum;
		int endIndex = pageNum * onePageSum;
		if(endIndex > sum)
			endIndex = sum;
		String sql = "";
		if(condition!=null && !condition.equals(""))
			sql = "select * from " + tableName + " where " + condition + " order by id " + method + " limit " + startIndex + "," + endIndex;
		else
			sql = "select * from " + tableName + " order by id " + method + " limit " + startIndex + "," + endIndex;
		try {
			//3、获取PreparedStatement
			prestmt = conn.prepareStatement(sql);
			//4、进行查询，得到ResultSet
			rs = prestmt.executeQuery();
			//5、准备一个Map<String,Object>:(前提是结果集中要有记录)
			
			while(rs.next()){
				Map<String,Object> values=new HashMap<String,Object>();
				//6、得到ResultSetMetaData对象
				ResultSetMetaData rsd=rs.getMetaData();
				//7、处理ResultSet,把指针向下移动一个单位
				
				//8、由ResultSetMetaData对象得到结果集中有多少列
				int colSum = rsd.getColumnCount();
				//9、由ResultSetMetaData对象得到每一列的别名，由ResultSet得到具体每一列的值
				for(int i = 0; i < colSum; i++){
					String colName = rsd.getColumnLabel(i+1);
					Object colValue = rs.getObject(i+1);
					//10、填充Map对象
					values.put(colName,colValue);
				}
				//11、用反射创建Class对应的对象
				example = (T) clazz.newInstance();
				//12、遍历Map对象，用反射填充对象的属性值，
				for(Map.Entry<String, Object> ent:values.entrySet()){
					String name = s2e(ent.getKey());
					Object value = ent.getValue();
					//用反射赋值
					ReflectionUtils.setFieldValue(example, name,  value);
			
				}
				list.add(example);
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
		return list;
		
	}
	/**
	 * 返回某页实体数据
	 * @param tableName
	 * @param field
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public Vector<Map<String, String>> findByPage(String tableName, String[] field, int startIndex, int endIndex, String method){
		String sql;
		sql = "select * from " + tableName+ " order by id " + method + " limit " + (startIndex-1) + "," + (endIndex-1);
		Vector<Map<String, String>> vec = new Vector<>();
		try {
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			Map<String, String> map = new HashMap<>();
			while(rs.next()){
				map = new HashMap<>();
				String[] temp = new String[field.length]; 
				for(int i=0;i<field.length;i++){
					temp[i] = rs.getString(field[i]);
					map.put(field[i], temp[i]);
					//Debug(temp[i]);
				}
				vec.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vec;
	}
	/**
	 * 返回一个实体map
	 * @param tableName
	 * @param field
	 * @return
	 */
	public Map<String, String> getEntity(String tableName, String[] field,  String condition){
		String strField = "", sql = "";
		for (int i = 0; i < field.length; i++) {
			strField += field[i] + ",";
		}
		strField = strField.substring(0, strField.lastIndexOf(","));
		sql = "select " + strField + " from " + tableName + " where " + condition;
		Map<String, String> map = new HashMap<>(); 
		try {
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			while(rs.next()){
				for(int i=0;i<field.length;i++){
					map.put(field[i], rs.getString(field[i]));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 查询一条记录，返回对应的对象
	 * @param t 实体类：new 一个实体
	 * @param condition 查询条件
	 * @return
	 */
	public <T> T getOneEntity(T t, String condition){
		Class<?> clazz = t.getClass();
		T example = null;
		
		//1、获得实体类名作为表名
		String className = clazz.getName();
		String tableName = e2s(className);
		
		//2、sql语句
		String sql = "select * from " + tableName + " where " + condition;
		try {
			prestmt = conn.prepareStatement(sql);
			//3、进行查询，得到ResultSet
			rs = prestmt.executeQuery();
			//4、准备一个Map<String,Object>:(前提是结果集中要有记录)
			if(rs.next()){
				Map<String,Object> values=new HashMap<String,Object>();
				
				//5、得到ResultSetMetaData对象
				ResultSetMetaData rsd=rs.getMetaData();
				//6、处理ResultSet,把指针向下移动一个单位
				
				//7、由ResultSetMetaData对象得到结果集中有多少列
				int colNum=rsd.getColumnCount();
				//8、由ResultSetMetaData对象得到每一列的别名，由ResultSet得到具体每一列的值
				for(int i = 0;i < colNum; i++){
					String colName = s2e(rsd.getColumnLabel(i+1));
					Object colValue = rs.getObject(i+1);
					//9、填充Map对象
					values.put(colName,colValue);
				}
			
				
				//10、用反射创建Class对应的对象
				example = (T) clazz.newInstance();
				//11、遍历Map对象，用反射填充对象的属性值，
				for(Map.Entry<String, Object> ent:values.entrySet()){
					String name=ent.getKey();
					Object value=ent.getValue();
					//用反射赋值
					ReflectionUtils.setFieldValue(example, name,  value);
			
				}
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
		return example;
		
	}
	/**
	 * 返回当前实体
	 * @param tableName
	 * @param field
	 * @param lastIndex
	 * @return
	 */
	public Map<String, String> getThisEntity(String tableName, String[] field, int lastIndex){
		String strField = "", sql = "";
		for (int i = 0; i < field.length; i++) {
			strField += field[i] + ",";
		}
		strField = strField.substring(0, strField.lastIndexOf(","));
		sql = "select * from " + tableName + " limit " + (lastIndex-1) + "," + (lastIndex-1);
		Map<String, String> map = new HashMap<>(); 
		try {
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			while(rs.next()){
				for(int i=0;i<field.length;i++){
					map.put(field[i], rs.getString(field[i]));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
/**
 * @param table
 * @param condition
 * @return
 */
	public boolean CheckedLogin(String table,String condition) {
		boolean flag = false;
		try {
			String sql = "select * from "+table+" where "+condition;
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 插入一条数据并返回id
	 * @param tableName
	 * @param field
	 * @param value
	 * @return id
	 */
	public long insertData(String tableName, String[] field, String[] value) {
		long id = -1;
		if (field == null || value == null ||field.length==0||value.length==0|| field.length != value.length)
			return id;
		String strField = "", strValue = "";
		for (int i = 0; i < field.length; i++) {
			strField += field[i] + ",";
			strValue += "'" + value[i] + "',";
		}
		strField = strField.substring(0, strField.lastIndexOf(","));
		strValue = strValue.substring(0, strValue.lastIndexOf(","));
		try {
			String sql = "insert into " + tableName;
			sql += " (" + strField + ") values(";
			sql += strValue + ")";
			prestmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			prestmt.executeUpdate();
			ResultSet rs = prestmt.getGeneratedKeys(); 
			 if (rs.next()) {
	                id = rs.getLong(1); 
	            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	/**
	 * 插入一个实体类
	 * @param t
	 * @return id
	 */
	public <T> long insertEntity(T t){
		//获得实体类名作为表名
		String className = t.getClass().getName();
		String tableName = e2s(className);
		
		
        List<String[]> list = new ArrayList<>();
		Field[] field = t.getClass().getDeclaredFields();
        String[] fileds = new String[field.length];
        String[] values = new String[field.length];
        String id = "";
		for(int i = 0; i < field.length; i ++){
			String name = field[i].getName();
			String value = "";
			try {
				String mName = name.substring(0, 1).toUpperCase()+name.substring(1, name.length());
				Method m = t.getClass().getMethod("get" + mName);
				value = m.invoke(t).toString();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			if(value != null && !"".equals(value)){
				fileds[i] = e2s(name);
				values[i] = value.toString();
				if(name.equals("id"))
					id = value;
			}
		}
		//验证更新还是删除
		String sql = "";
		sql = "select * from " + tableName + " where id=" + id;
		try {
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			while(rs.next()){
		        String[] fileds1 = new String[fileds.length-1];
		        String[] values1 = new String[values.length-1];
		        int index = 0;
		        for(int i = 0; i < fileds.length; i ++){
		        	if(fileds[i].equals("id")){
		        		continue;
		        	}
		        	fileds1[index] = fileds[i];
		        	values1[index] = values[i];
				    index++;
		        }
				 modifyData(tableName, fileds1, values1,
							"id='" + id + "'");
				 return Long.parseLong(id);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		list.add(fileds);
		list.add(values);
		return insertData(tableName, fileds, values);
	}
	/**
	 * 
	 * @param tableName
	 * @param field
	 * @param value
	 * @param condition
	 */
	public boolean modifyData(String tableName, String[] field, String[] value,
			String condition) {
		boolean flag = false;
		if (field == null || value == null ||field.length==0||value.length==0|| field.length != value.length)
			return flag;
		String str = "";
		for(int i=0;i<field.length;i++){
			str += field[i]+"='"+value[i]+"',";
		}
		str = str.substring(0,str.lastIndexOf(","));
		//Debug(str);
		String sql = "";
		if(condition==null||condition==""){
		 sql = "update "+tableName+" set "+str;
		}else{
			sql = "update "+tableName+" set "+str+"  where "+condition;	
		}
		//Debug(sql);
		
		try {
			prestmt = conn.prepareStatement(sql);
			int rscount = prestmt.executeUpdate();
			if(rscount>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
		return flag;
	}
	/**
	 * @param table
	 * @param field
	 * @param condition
	 * @return
	 */
	public Hashtable<String, String> execSQL(String table,String field,String condition){
		Hashtable<String, String> ht = new Hashtable<String, String>();
		try{
			String sql = "select "+field+" from "+table+" where "+condition;
			prestmt = conn.prepareStatement(sql);
			rs = prestmt.executeQuery();
			while(rs.next()){
                    ht.put(field, rs.getString(field));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ht;
		
	}
}

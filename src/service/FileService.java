package service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;


public class FileService {
	/**
	 * 获得BucketManager
	 * @return
	 */
	public BucketManager getBucketManager(){
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		String accessKey = "khCLjbIJ-htjneC2BUtX8zOBSk71wpm1TZnU9s5u";
		String secretKey = "PG0aUetavLZQvD6pp4hvgqDV_5P9_2X5xG5kfGKk";
		return new BucketManager(Auth.create(accessKey, secretKey), cfg);
	}
	/**
	 * 上传文件
	 * @param file
	 * @param filesInfo
	 * @return 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String doUpload(HttpServletRequest req) throws UnsupportedEncodingException{
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024*1024*10);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(1024*1024*30);
		upload.setFileSizeMax(1024*1024*50);
        upload.setHeaderEncoding("UTF-8");
        List<FileItem> formItems = null;
		try {
			formItems = upload.parseRequest(req);
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String msg = "上传成功";
        if(formItems.isEmpty())
			msg = "请选择上传文件";
		else{
			String filename = "",path = "";
			InputStream is = null;
			for(int i = 0 ; i < formItems.size(); i++){
				FileItem item = formItems.get(i);
				if(item.isFormField()){
					switch(item.getFieldName()){
					case "name":
						filename = new String(item.get(),"UTF-8");
						break;
					case "path":
						path = new String(item.get(),"UTF-8");
						break;
					}
				}else{
					try {
						is = item.getInputStream();
					} catch (IOException e) {
						msg = "文件转流出错";
						e.printStackTrace();
					}
				}
			}
			
			//构造一个带指定Zone对象的配置类
			Configuration cfg = new Configuration(Zone.zone2());
			//...其他参数参考类注释
			UploadManager uploadManager = new UploadManager(cfg);
			//...生成上传凭证，然后准备上传
			String accessKey = "khCLjbIJ-htjneC2BUtX8zOBSk71wpm1TZnU9s5u";
			String secretKey = "PG0aUetavLZQvD6pp4hvgqDV_5P9_2X5xG5kfGKk";
			String bucket = "test";
			//默认不指定key的情况下，以文件内容的hash值作为文件名
			Auth auth = Auth.create(accessKey, secretKey);
			String upToken = auth.uploadToken(bucket);
			try {
			    Response response = uploadManager.put(is, path + filename, upToken,null,null);
			    //解析上传成功的结果
			    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

			} catch (QiniuException ex) {
			    Response r = ex.response;
			    System.err.println(r.toString());
			    msg = "上传出错:"+r.toString();
			    try {
			        System.err.println(r.bodyString());
			    } catch (QiniuException ex2) {
			        //ignore
			    }
			}
		}
		return msg;
	}
	
	/**
	 * 新建文件夹
	 * @param path
	 * @return
	 */
	public String mkDir(String path){
		String msg = "新建文件夹成功";
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = "khCLjbIJ-htjneC2BUtX8zOBSk71wpm1TZnU9s5u";
		String secretKey = "PG0aUetavLZQvD6pp4hvgqDV_5P9_2X5xG5kfGKk";
		String bucket = "test";
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put("".getBytes(), path, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

		} catch (QiniuException ex) {
		    Response r = ex.response;
		    System.err.println(r.toString());
		    msg = "新建出错:"+r.toString();
		    try {
		        System.err.println(r.bodyString());
		    } catch (QiniuException ex2) {
		        //ignore
		    }
		}
		return msg;
	}
	/**
	 * 查询目录
	 * @param prefix 文件名前缀
	 * @param delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
	 * @return
	 */
	public Map<String, Object> getFileList(String prefix){
		if(prefix == null || prefix == "")
			prefix = "";
		else
			prefix += "/";
		
		String delimiter = "";
		
		//储存空间
		String bucket = "test";
		//每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		//列举空间文件列表
		BucketManager.FileListIterator fileListIterator = getBucketManager().createFileListIterator(bucket, prefix, limit, delimiter);
		FileInfo[] items = null;
		//文件夹和文件
		ArrayList<String> folderList = new ArrayList<>();
		ArrayList<Map<String,Object>> fileList = new ArrayList<>();
		
		while(fileListIterator.hasNext()){
			items = fileListIterator.next();
			 for (FileInfo item : items) {
				Map<String,Object> map = new HashMap<String, Object>();
				String fileName = item.key;
				String lastStr = fileName.substring(prefix.length(),fileName.length());
				//如果没有'/'则为文件
				if(lastStr.indexOf("/") == -1){
			    	map.put("key", lastStr);
			    	map.put("hash", item.hash);
			    	map.put("fsize", item.fsize);
			    	map.put("mimeType", item.mimeType);
			    	map.put("putTime", item.putTime);
			    	map.put("endUser", item.endUser);
			        
			    	//时间处理
			    	String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(item.putTime / 10000));
			        map.put("putTime", date);
			        fileList.add(map);
				}
				//否则为文件夹
				else{
					String folderName = lastStr.substring(0,lastStr.indexOf("/"));
					//合并重复文件夹
					if(!folderList.contains(folderName)){
						folderList.add(folderName);
					}
						
				}
			 }
		}
		
		Map<String,Object> fileMap = new HashMap<>();
		fileMap.put("folderList",folderList);
		fileMap.put("fileList", fileList);
		return fileMap;
	}
	/**
	 * 查询文件详细信息
	 * @param prefix 
	 * @param fileName
	 * @return
	 */
	public Map<String, String> getFileInfo(String key){
		
		//储存空间
		String bucket = "test";
		BucketManager.FileListIterator fileListIterator = getBucketManager().createFileListIterator(bucket, key, 1, "");
		FileInfo[] items = fileListIterator.next();
		FileInfo item = items[0];
		
		Map<String,String> map = new HashMap<String, String>();
    	map.put("key", key);
    	map.put("hash", item.hash);
    	//保留两位小数
    	map.put("fsize", new BigDecimal(Double.valueOf(item.fsize)/1024).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue()+"KB");
    	map.put("mimeType", item.mimeType);
    	map.put("endUser", item.endUser);
        
    	//时间处理
    	String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(item.putTime / 10000));
        map.put("putTime", date);
        
		return map;
	}
	/**
	 * 获取文件下载地址
	 * @param prefix
	 * @param fileName
	 * @return
	 */
	public String download(String prefix,String fileName){

		
		String domainOfBucket = "http://opw7cwrmm.bkt.clouddn.com";
		String encodedFileName= "";
		try {
			encodedFileName = URLEncoder.encode(prefix+fileName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
		return finalUrl+"?attname=";
	}
	/**
	 * 删除文件
	 * @param key
	 * @return
	 */
	public String delete(String key){
		String msg = "删除成功";
		String bucket = "test";
		
		try {
			getBucketManager().delete(bucket, key);
		} catch (QiniuException ex) {
		    //如果遇到异常，说明删除失败
			msg = "删除失败:"+ex.code()+","+ex.response.toString();
		}
		
		return msg;
	}
	/**
	 * 删除文件夹
	 * @param path
	 * @return
	 */
	public String deleteFolder(String path){
		String msg = "";
		
		//储存空间
		String bucket = "test";
		//每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		//列举空间文件列表
		BucketManager.FileListIterator fileListIterator = getBucketManager().createFileListIterator(bucket, path + "/", limit, "");
		FileInfo[] items = null;
		
		while(fileListIterator.hasNext()){
			items = fileListIterator.next();
			 for (FileInfo item : items) {
				 msg = delete(item.key);
			 }
		}
		
		return msg;
	}
	/**
	 * 重命名文件
	 * @param key
	 * @param newKey
	 * @return
	 */
	public String rename(String key, String newKey){
		String msg = "重命名成功";
		//...其他参数参考类注释
		String bucket = "test";
		String fromKey = key;
		String toKey = newKey;
		try {
			getBucketManager().move(bucket, fromKey, bucket, toKey);
		} catch (QiniuException ex) {
		    //如果遇到异常，说明移动失败
		    System.err.println(ex.code());
		    System.err.println(ex.response.toString());
		    msg = "重命名失败";
		}
		return msg;
	}
	/**
	 * 移动文件
	 * @param key
	 * @param newKey
	 * @return
	 */
	public String remove(String fromKey, String toKey){
		String msg = "移动成功";
		//...其他参数参考类注释
		String fromBucket = "test";
		String toBucket = "test";
		try {
			 toKey = toKey + "/" + fromKey.substring(fromKey.lastIndexOf("/")+1);
			 getBucketManager().move(fromBucket, fromKey, toBucket, toKey);
		} catch (QiniuException ex) {
		    //如果遇到异常，说明移动失败
		    System.err.println(ex.code());
		    System.err.println(ex.response.toString());
		    msg = "移动失败";
		}
		return msg;
	}
	/**
	 * 移动文件夹
	 * @param fromKey
	 * @param toKey
	 * @return
	 */
	public String removeFolder(String fromKey, String toKey){
		String msg = "移动成功";
		
		//储存空间
		String fromBucket = "test";
		String toBucket = "test";
		//每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		//列举空间文件列表
		BucketManager.FileListIterator fileListIterator = getBucketManager().createFileListIterator(fromBucket, fromKey + "/", limit, "");
		FileInfo[] items = null;
		
		while(fileListIterator.hasNext()){
			items = fileListIterator.next();
			 for (FileInfo item : items) {
				try {
					 String fromKey1 = item.key;
					 String toKey1 = toKey + item.key.substring(fromKey.lastIndexOf('/'));
					 getBucketManager().move(fromBucket, fromKey1, toBucket, toKey1);
				} catch (QiniuException ex) {
				    //如果遇到异常，说明移动失败
				    System.err.println(ex.code());
				    if(ex.code() == 614 )
					    msg = "有重复文件名，移动失败";
				    System.err.println(ex.response.toString());
				}
			 }
		}
		
		return msg;
	}
	/**
	 * 重命名文件夹
	 * @param path
	 * @param newPath
	 * @return
	 */
	public String renameFolder(String path, String newPath){
		String msg = "重命名成功";
		
		//储存空间
		String bucket = "test";
		//每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		//列举空间文件列表
		BucketManager.FileListIterator fileListIterator = getBucketManager().createFileListIterator(bucket, path + "/", limit, "");
		FileInfo[] items = null;
		
		while(fileListIterator.hasNext()){
			items = fileListIterator.next();
			 for (FileInfo item : items) {
				 String newKey = newPath + item.key.substring(path.length());
				 try {
						getBucketManager().move(bucket, item.key, bucket, newKey);
					} catch (QiniuException ex) {
					    //如果遇到异常，说明移动失败
					    System.err.println(ex.code());
					    System.err.println(ex.response.toString());
					    if(ex.code() == 614 && item.key.equals(path + "/")){
						    msg = "有重复文件夹名，重命名失败";
						    break;
					    }
					}
			 }
		}
		
		return msg;
	}
}

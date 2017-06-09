package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.qiniu.util.Auth;
import dao.blackboard.FloorDao;
import dao.blackboard.ReplyDao;
import dao.user.UserDao;
import model.blackboard.Floor;
import model.blackboard.Reply;

public class BlackboardService {
	
	private FloorDao floorDao = new FloorDao();
	
	private ReplyDao replyDao = new ReplyDao();
	
	private UserDao userDao = new UserDao();
	
	private final int onePageSum = 6;
	
	/**
	 * 返回页数
	 * @return
	 */
	public long getPageSum(){
		return (floorDao.count()-1)/6+1;
	}
	/**
	 * 找出该pageNum页所有楼层
	 * @param pageNum
	 * @return
	 */
	public List<Floor> findAll(int pageNum){
		return floorDao.findByPage(pageNum, onePageSum);
	}
	/**
	 * 留言
	 * @param floor
	 * @return
	 */
	public Map<String, Object> leaveMessage(Floor floor){
		String msg = "留言成功";
		floor.setImgSrc(userDao.getImgSrc(floor.getUsername()));
		floor.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		floor.setFlNum((int) floorDao.count()+1);
		floor = floorDao.save(floor);
		Map<String,Object>map = new HashMap<>();
		map.put("floor",floor);
		map.put("msg",msg);
		return map;
	}
	/**
	 * 删除留言
	 * @param floorId
	 * @return
	 */
	public Map<String, Object> deleteMessage(int floorId,int currentPage){
		String msg = "删除成功";
		long count = floorDao.count();
		long pageSum = (count-1)/6+1;
		Map<String, Object> map = new HashMap<>();
		if(count > 6){
			if(currentPage <  pageSum){
				List<Floor> list = floorDao.findByPage(currentPage+1, onePageSum);
				Floor floor = list.get(0);
				map.put("floor", floor);
			}
		}else{
			msg = "删除成功，仅剩一页";
		}
		floorDao.delete(floorId);
		map.put("msg", msg);
		return map;
	}
	/**
	 * 找出该id楼层的所有评论
	 * @param id
	 * @return
	 */
	public Iterable<Reply> findAllReply(int id){
		return replyDao.findAllReplyById(id);
	}
	/**
	 * 新增回复
	 * @param reply
	 * @return
	 */
	public String reply(Reply reply){
		reply.setImgSrc(userDao.getImgSrc(reply.getUsername()));
		reply.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		replyDao.save(reply);
		Floor floor = floorDao.findOne(reply.getFloorId());
		floor.setRpCount(floor.getRpCount()+1);
		floorDao.save(floor);
		return "回复成功";
	}
	/**
	 * 新增赞
	 * @param floorId
	 * @return
	 */
	public int zan(int floorId){
		Floor floor = floorDao.findOne(floorId);
		floor.setZanCount(floor.getZanCount()+1);
		floorDao.save(floor);
		return floorDao.getZanCount(floorId);
	}
	/**
	 * 获得upToken
	 * @return
	 */
	public Map<String, String> getUpToken(){
		String accessKey = "khCLjbIJ-htjneC2BUtX8zOBSk71wpm1TZnU9s5u";
		String secretKey = "PG0aUetavLZQvD6pp4hvgqDV_5P9_2X5xG5kfGKk";
		String bucket = "imgs";
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		Map<String,String> map = new HashMap<>();
		map.put("uptoken", upToken);
		return map;
	}
}

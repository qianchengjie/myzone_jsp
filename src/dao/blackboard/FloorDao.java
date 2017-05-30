package dao.blackboard;

import java.util.List;
import model.blackboard.Floor;
import util.DBUtil;

/**
 * @author 33061
 *
 */
public class FloorDao {

	private DBUtil db = new DBUtil();
	
	/**
	 * 获得Floor实体
	 * @param condition
	 * @return
	 */
	public Floor findOne(String condition){
		return db.getOneEntity(new Floor(), "id=180");
	}
	/**
	 * 通过id获得Floor实体
	 * @param id
	 * @return
	 */
	public Floor findOne(int id){
		return db.getOneEntity(new Floor(), "id="+id);
	}
	/**
	 * 返回留言总数
	 * @return
	 */
	public long count(){
		return db.count("floor", "");
	}
	/**
	 * 查询多条记录，返回单页对应的对象
	 * @param pageNum 页码
	 * @param onePageSum 单页总数
	 * @return
	 */
	public List<Floor> findByPage(int pageNum, int onePageSum){
		return db.getPageEntity(new Floor(), pageNum, onePageSum, "DESC", "", "");
	}
	
	/**
	 * 保存Floor实体并返回当前存入之后的实体
	 * @param floor
	 * @return 保存后的实体
	 */
	public Floor save(Floor floor){
		long id = db.insertEntity(floor);
		return db.getOneEntity(new Floor(), "id="+id);
	}
	/**
	 * 删除楼层
	 * @param floorId
	 * @return
	 */
	public boolean delete(int floorId){
		return db.deleteData("floor", "id=" + floorId);
	}
	
	public int getZanCount(int floorId) {
		return Integer.parseInt(db.getData("floor", new String[]{"zan_count"}, "id='"+ floorId + "'").get(0)[0]);
	}  
	
}

package dao.blackboard;


import model.blackboard.Reply;
import util.DBUtil;

public class ReplyDao {

	private DBUtil db = new DBUtil();
	
	public Iterable<Reply> findAllReplyById(int id){
		return (Iterable<Reply>) db.getPageEntity(new Reply(), 1, 1000, "DESC", "floor_id=" + id, "floor_id='" + id + "'");
	}
	public void save(Reply reply) {
		db.insertEntity(reply);
	}
	
}

package model.user;

import java.util.Date;


public class UserRole {
	
	private int id;
	
	private int pid;
	
	private String rname;
	
	private Date gtime;
	
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public Date getGtime() {
		return gtime;
	}

	public void setGtime(Date gtime) {
		this.gtime = gtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}

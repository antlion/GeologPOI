package activity.dominio;

import java.util.Date;



public class Suggestion implements IEntity {
	
	private int id;
	private String user;
	private Poi poi;
	private Date creationDate;
	private String description;
	
	
	
	
	public Suggestion(int id, String user, Poi poi, Date creationDate,String description) {
		super();
		this.id = id;
		this.user = user;
		this.poi = poi;
		this.creationDate = creationDate;
		this.description= description;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Poi getPoi() {
		return poi;
	}
	public void setPoi(Poi poi) {
		this.poi = poi;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int compareTo(IEntity o) {
		// TODO Auto-generated method stub
		return 0;
	}
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}


}

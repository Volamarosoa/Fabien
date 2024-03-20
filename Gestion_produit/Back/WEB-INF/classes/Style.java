package models.models;

import dao.annotations.*;
import java.util.List;


@Table(name = "style")
public class Style {
	@Colonne(name = "style")
	String style;
	@PrimaryKey
	@Colonne(name = "id")
	String id;


	public Style(){}
	public String getStyle(){
		return this.style;
	}
	public void setStyle(String style){
		this.style = style;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}

}

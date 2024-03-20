package models.models;

import dao.annotations.*;
import java.util.List;


@Table(name = "categorie")
public class Categorie {
	@Colonne(name = "categorie")
	String categorie;
	@PrimaryKey
	@Colonne(name = "id")
	String id;


	public Categorie(){}
	public String getCategorie(){
		return this.categorie;
	}
	public void setCategorie(String categorie){
		this.categorie = categorie;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}

}

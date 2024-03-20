package models.models;

import dao.annotations.*;
import java.util.List;


@Table(name = "matiere")
public class Matiere {
	@PrimaryKey
	@Colonne(name = "id")
	String id;
	@Colonne(name = "matiere")
	String matiere;


	public Matiere(){}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getMatiere(){
		return this.matiere;
	}
	public void setMatiere(String matiere){
		this.matiere = matiere;
	}

}

package models.models;

import dao.annotations.*;
import java.util.List;


@Table(name = "personne")
public class Personne {
	@PrimaryKey
	@Colonne(name = "id")
	Integer id;
	@Colonne(name = "nom")
	String nom;


	public Personne(){}
	public Integer getId(){
		return this.id;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public String getNom(){
		return this.nom;
	}
	public void setNom(String nom){
		this.nom = nom;
	}

}

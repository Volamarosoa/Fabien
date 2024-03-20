package models.models;

import dao.annotations.*;
import java.util.List;


@Table(name = "produit")
public class Produit {
	@ForeignKey(references = "idstyle", foreignType = ForeignType.OneToMany)
	Style style;
	@PrimaryKey
	@Colonne(name = "id")
	String id;
	@Colonne(name = "nom")
	String nom;
	@ForeignKey(references = "idcategorie", foreignType = ForeignType.OneToMany)
	Categorie categorie;


	public Produit(){}
	public Style getStyle(){
		return this.style;
	}
	public void setStyle(Style style){
		this.style = style;
	}
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getNom(){
		return this.nom;
	}
	public void setNom(String nom){
		this.nom = nom;
	}
	public Categorie getCategorie(){
		return this.categorie;
	}
	public void setCategorie(Categorie categorie){
		this.categorie = categorie;
	}

}

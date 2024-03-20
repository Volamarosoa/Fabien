package models.models;

import dao.annotations.*;
import java.util.List;


@Table(name = "matiere_style")
public class MatiereStyle {
	@ForeignKey(references = "idstyle", foreignType = ForeignType.OneToMany)
	Style style;
	@PrimaryKey
	@Colonne(name = "id")
	Integer id;
	@ForeignKey(references = "idmatiere", foreignType = ForeignType.OneToMany)
	Matiere matiere;


	public MatiereStyle(){}
	public Style getStyle(){
		return this.style;
	}
	public void setStyle(Style style){
		this.style = style;
	}
	public Integer getId(){
		return this.id;
	}
	public void setId(Integer id){
		this.id = id;
	}
	public Matiere getMatiere(){
		return this.matiere;
	}
	public void setMatiere(Matiere matiere){
		this.matiere = matiere;
	}

}

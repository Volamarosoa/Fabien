package models.controller;

import models.models.Categorie;
import etu2068.annotations.*;
import etu2068.modelView.ModelView;
import connexionBase.ConnexionBase;
import java.util.List;
import dao.generiqueDAO.GeneriqueDAO;


public class CategorieController {

	@Url(name = "/save-categorie")
	@CorsMethod(method = "POST")
	@restAPI()
	public Object save(@Argument Categorie categorie) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.insert(con.getConnection(), categorie);
			con.getConnection().commit();
			con.getConnection().close();
			return "saved successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/update-categorie")
	@CorsMethod(method = "PUT")
	@restAPI()
	public Object update(@Argument Categorie categorie) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.updateById(con.getConnection(), categorie);
			con.getConnection().commit();
			con.getConnection().close();
			return "updated successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/delete-categorie")
	@CorsMethod(method = "DELETE")
	@restAPI()
	public Object delete(@Argument Categorie categorie) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.deleteById(con.getConnection(), categorie);
			con.getConnection().commit();
			con.getConnection().close();
			return "deleted successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/find-categorie")
	@CorsMethod(method = "GET")
	@restAPI()
	public Object findAll() throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			List<Categorie> list = (List<Categorie>)GeneriqueDAO.list(con.getConnection(), new Categorie(), "", "");
			con.getConnection().close();
			return list; 
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}


}

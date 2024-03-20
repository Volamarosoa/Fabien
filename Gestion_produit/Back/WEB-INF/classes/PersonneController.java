package models.controller;

import models.models.Personne;
import etu2068.annotations.*;
import etu2068.modelView.ModelView;
import connexionBase.ConnexionBase;
import java.util.List;
import dao.generiqueDAO.GeneriqueDAO;


public class PersonneController {

	@Url(name = "/save-personne")
	@CorsMethod(method = "POST")
	@restAPI()
	public Object save(@Argument Personne personne) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.insert(con.getConnection(), personne);
			con.getConnection().commit();
			con.getConnection().close();
			return "saved successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/update-personne")
	@CorsMethod(method = "PUT")
	@restAPI()
	public Object update(@Argument Personne personne) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.updateById(con.getConnection(), personne);
			con.getConnection().commit();
			con.getConnection().close();
			return "updated successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/delete-personne")
	@CorsMethod(method = "DELETE")
	@restAPI()
	public Object delete(@Argument Personne personne) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.deleteById(con.getConnection(), personne);
			con.getConnection().commit();
			con.getConnection().close();
			return "deleted successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/find-personne")
	@CorsMethod(method = "GET")
	@restAPI()
	public Object findAll() throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			List<Personne> list = (List<Personne>)GeneriqueDAO.list(con.getConnection(), new Personne(), "", "");
			con.getConnection().close();
			return list; 
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}


}

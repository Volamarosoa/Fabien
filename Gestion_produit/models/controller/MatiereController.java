package models.controller;

import models.models.Matiere;
import etu2068.annotations.*;
import etu2068.modelView.ModelView;
import connexionBase.ConnexionBase;
import java.util.List;
import dao.generiqueDAO.GeneriqueDAO;


public class MatiereController {

	@Url(name = "/save-matiere")
	@CorsMethod(method = "POST")
	@restAPI()
	public Object save(@Argument Matiere matiere) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.insert(con.getConnection(), matiere);
			con.getConnection().commit();
			con.getConnection().close();
			return "saved successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/update-matiere")
	@CorsMethod(method = "PUT")
	@restAPI()
	public Object update(@Argument Matiere matiere) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.updateById(con.getConnection(), matiere);
			con.getConnection().commit();
			con.getConnection().close();
			return "updated successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/delete-matiere")
	@CorsMethod(method = "DELETE")
	@restAPI()
	public Object delete(@Argument Matiere matiere) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.deleteById(con.getConnection(), matiere);
			con.getConnection().commit();
			con.getConnection().close();
			return "deleted successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/find-matiere")
	@CorsMethod(method = "GET")
	@restAPI()
	public Object findAll() throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			List<Matiere> list = (List<Matiere>)GeneriqueDAO.list(con.getConnection(), new Matiere(), "", "");
			con.getConnection().close();
			return list; 
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}


}

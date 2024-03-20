package models.controller;

import models.models.MatiereStyle;
import etu2068.annotations.*;
import etu2068.modelView.ModelView;
import connexionBase.ConnexionBase;
import java.util.List;
import dao.generiqueDAO.GeneriqueDAO;


public class MatiereStyleController {

	@Url(name = "/save-matiereStyle")
	@CorsMethod(method = "POST")
	@restAPI()
	public Object save(@Argument MatiereStyle matiereStyle) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.insert(con.getConnection(), matiereStyle);
			con.getConnection().commit();
			con.getConnection().close();
			return "saved successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/update-matiereStyle")
	@CorsMethod(method = "PUT")
	@restAPI()
	public Object update(@Argument MatiereStyle matiereStyle) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.updateById(con.getConnection(), matiereStyle);
			con.getConnection().commit();
			con.getConnection().close();
			return "updated successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/delete-matiereStyle")
	@CorsMethod(method = "DELETE")
	@restAPI()
	public Object delete(@Argument MatiereStyle matiereStyle) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.deleteById(con.getConnection(), matiereStyle);
			con.getConnection().commit();
			con.getConnection().close();
			return "deleted successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/find-matiereStyle")
	@CorsMethod(method = "GET")
	@restAPI()
	public Object findAll() throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			List<MatiereStyle> list = (List<MatiereStyle>)GeneriqueDAO.list(con.getConnection(), new MatiereStyle(), "", "");
			con.getConnection().close();
			return list; 
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}


}

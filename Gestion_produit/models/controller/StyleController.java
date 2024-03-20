package models.controller;

import models.models.Style;
import etu2068.annotations.*;
import etu2068.modelView.ModelView;
import connexionBase.ConnexionBase;
import java.util.List;
import dao.generiqueDAO.GeneriqueDAO;


public class StyleController {

	@Url(name = "/save-style")
	@CorsMethod(method = "POST")
	@restAPI()
	public Object save(@Argument Style style) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.insert(con.getConnection(), style);
			con.getConnection().commit();
			con.getConnection().close();
			return "saved successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/update-style")
	@CorsMethod(method = "PUT")
	@restAPI()
	public Object update(@Argument Style style) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.updateById(con.getConnection(), style);
			con.getConnection().commit();
			con.getConnection().close();
			return "updated successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/delete-style")
	@CorsMethod(method = "DELETE")
	@restAPI()
	public Object delete(@Argument Style style) throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			GeneriqueDAO.deleteById(con.getConnection(), style);
			con.getConnection().commit();
			con.getConnection().close();
			return "deleted successfully";
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}
	@Url(name = "/find-style")
	@CorsMethod(method = "GET")
	@restAPI()
	public Object findAll() throws Exception {
	 	ConnexionBase con = new ConnexionBase();
		try{
			List<Style> list = (List<Style>)GeneriqueDAO.list(con.getConnection(), new Style(), "", "");
			con.getConnection().close();
			return list; 
		}catch(Exception e){
			con.getConnection().close();
			return "Bad request";
		}
	}


}

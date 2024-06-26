package etu2068.framework.servlet;
import javax.servlet.*;
import javax.servlet.http.*;

import etu2068.annotations.Url;
import etu2068.annotations.Argument;
import etu2068.annotations.Singleton;
import etu2068.annotations.Auth;
import etu2068.annotations.Session;
import etu2068.annotations.restAPI;
import etu2068.annotations.ExportXML;
import etu2068.annotations.Test;
import etu2068.annotations.CorsMethod;
import etu2068.modelView.ModelView;
import netscape.javascript.JSObject;

import java.io.*;
import java.lang.Thread;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Set;
import java.util.Enumeration;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import etu2068.framework.Mapping;
import java.lang.ClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;


import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import javax.servlet.annotation.*;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@MultipartConfig()
public class FrontServlet extends HttpServlet{
    HashMap<String, Mapping> mappingUrls;
    Vector<Class<?>> listeClasse;
    HashMap<String, Object> instances;

    public void init() {
        try{
            this.setMappingUrls(new HashMap<String,Mapping>());
            ClassLoader classLoader = new Thread().getContextClassLoader();
            Enumeration<URL> urls = classLoader.getResources("");
            this.listeClasse = new Vector<Class<?>>();
            this.instances = new HashMap<String, Object>();

        ///maka ny nom an'ilay package misy an'ilay models rehetra, alaina avy eo amin'ny web.xml    
            String nomPackage = getServletContext().getInitParameter("NomduPackage");

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File dir = new File(url.toURI());
                for (File file : dir.listFiles()) {
                    if (file.isDirectory() && file.getName().equals(nomPackage)) {
                        this.listePackage( file, file.getName());
                    }
                }
            }

        } catch(Exception io){
            io.printStackTrace();
        }
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return this.mappingUrls;
    }


    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public Vector<Class<?>> getListeClasse() {
        return this.listeClasse;
    }

    public void setListeClasse(Vector<Class<?>> listeClasse) {
        this.listeClasse = listeClasse;
    }


    public HashMap<String, Object> getInstances() {
        return this.instances;
    }

    public void setInstances(HashMap<String, Object> instances) {
        this.instances = instances;
    }

    public void addInstance(String key, Object value) {
        this.getInstances().put(key, value);
    }

    //test si c'est un fichier
    private boolean isFilePart(Part part) {
        String disposition = part.getHeader("content-disposition");
        return (disposition != null && disposition.contains("filename"));
    }

    //retourne les valeurs de ce qui ne sont pas un fichier
    private String[] readValueFromPart(Part part) throws IOException {
        InputStream partContent = part.getInputStream();
        InputStreamReader reader = new InputStreamReader(partContent, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        return lines.toArray(new String[0]);
    }
    

    //retourne les valeurs en byte de ce qui sont fichier
    private byte[] readBytesFromPart(Part part) throws IOException {
        InputStream partContent = part.getInputStream();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = partContent.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        return output.toByteArray();
    }

    public void resetFieldToNull(Object object) throws Exception {
        String reinitialiser = getServletContext().getInitParameter("reinitialiser");
        Class<?> objectClass = object.getClass();
        Method method = object.getClass().getDeclaredMethod(reinitialiser);
        if(method == null)
            throw new Exception("Il n'y a pas de methode reinitialiser dans votre modele : " + objectClass.getName());
        method.invoke(object); 
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, Map<String, String[]> params) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String contentType = request.getContentType();
        try{
            response.setContentType("text/plain");
            System.out.println("URL = " + request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0]);
            System.out.println("Method = " + request.getMethod().toString());

            Mapping mapping1 = (Mapping)this.getMappingUrls().get(request.getRequestURI().substring(request.getContextPath().length()).split(".do")[0]);
            if(mapping1!=null){
                for (Class<?> class1 : this.getListeClasse()) {
                    if(class1.getSimpleName().equals(mapping1.getClassName())) {
                        Object object = null;

                        //mijery hoe singleton ve le class natsoina sa tsia
                        if(this.getInstances().containsKey(class1.getSimpleName())) {
                            object = this.getInstances().get(class1.getSimpleName());
                            if(object == null) {
                                System.out.println("micreer instance");
                                object = class1.newInstance();
                                this.instancationAttribut(object);
                                this.getInstances().replace(class1.getSimpleName(), object);
                            }
                            else {
                                if(!class1.isAnnotationPresent(Singleton.class)) {
                                    System.out.println("miset field rht");
                                    // this.resetFieldToNull(object);
                                    object = class1.newInstance();
                                    this.instancationAttribut(object);
                                    this.getInstances().replace(class1.getSimpleName(), object);
                                } 
                            }
                        }
                        else{
                            object = class1.newInstance();
                        }

                        System.out.println("Taille parametre: " + params.size());
                        if (contentType != null && contentType.startsWith("multipart/")) {
                            this.makaParametreDonneesAvecFichier(object, request, class1, out); //maka an'ilay parametre setters any jsp fa avec setters
                        } else {
                            object = this.makaParametreDonnees(object, params, class1); //maka an'ilay parametre setters any jsp
                        }

                        Method[] methods = class1.getDeclaredMethods();
                        for (Method method : methods) {
                            if(method.getName().equals(mapping1.getMethod())) {
                                //regarde c'est la methode et la fonction a le meme method demande dans la fonction
                                this.checkCorsMethod(method, request.getMethod().toString());

                                //test si le methode a l'annotation Auth
                                this.checkMethod(method, request);

                                //get tous les sessions demander par la methode
                                this.checkSession(object, method, request);

                                Object[] argument = this.mamenoParametreMethode(method, params);    //mameno parametre an'ilay fonction                                 
                                System.out.println("argument: " + argument);
                                if(method.isAnnotationPresent(restAPI.class)) {
                                    Object json = null;
                                    if(argument != null){
                                        json = method.invoke(object, argument);
                                    }
                                    else{
                                        json = method.invoke(object);
                                    }
                                    String json_tab = new Gson().toJson(json);
                                    System.out.println("io: " + json_tab);
                                    out.println(json_tab);
                                }
                                else if(method.isAnnotationPresent(ExportXML.class)) {
                                    Object objetXML = null;
                                    objetXML = method.invoke(object);
                                    String fichier = (String)objetXML;
                                    this.fichierXML(fichier, request, response);
                                }
                                else {
                                    ModelView view = null;
                                    try {
                                        if(argument != null){
                                            view = (ModelView)method.invoke(object, argument);
                                        }
                                        else{
                                            view = (ModelView)method.invoke(object);
                                        }
                                    } catch (InvocationTargetException e) {
                                        Throwable cause = e.getCause();
                                        if (cause != null) {
                                            cause.printStackTrace();
                                        }
                                    }
                                    
                                    // out.println("View = " + view.getView());

                                    //supprime les sessions demander
                                    this.removeSession(view, request);

                                    //on ajout les sessions
                                    if(view.getSession()!=null) {
                                        this.addSession(view, request);
                                    }
    
                                    //on ajout les attributs a envoyer vers JSP ici
                                    if(view.getData()!=null) {
                                        this.setAttribute(view, request);
                                    }
    
                                    //test si c'est un JSON
                                    if(view.isJson()) {
                                        System.out.println("true");
                                        String json = new Gson().toJson(view.getData());
                                        out.println(json);
                                        System.out.println(json);
                                    }
                                    else {
                                        RequestDispatcher dispatcher = request.getRequestDispatcher("/"+view.getView());
                                        dispatcher.forward(request, response);
                                    }
                                }


                            }
                        }
                    }
                }
            } else {
                System.out.println("Null le mapping");
            }
        }
        catch(Exception io) {
            System.out.println("Erreur piso *********************");
            io.printStackTrace();
            JsonObject errorObject = new JsonObject();
            errorObject.addProperty("error", io.getMessage());
            // Envoyer l'objet JSON en réponse
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errorObject.toString());
            out.println("Erreur aki = " + io.getMessage());
            io.printStackTrace();
            request.setAttribute("erreur", io.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Erreur.jsp");
            dispatcher.forward(request, response);
        }
    }

    public void instancationAttribut(Object object) throws Exception{
        Field[] fields = object.getClass().getFields();
        for(Field field : fields) {
            if(!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String")) {
                System.out.println(field.getName()  + " : " + field.getType().getName());
                Object _object = field.getType().newInstance();
                String nomMethode =  "set" + this.changeFirstAName(field.getName());
                Method setter = object.getClass().getDeclaredMethod(nomMethode, field.getType());
                setter.invoke(object, _object); 
            }
        }
    }

    //fichier XML
    public void fichierXML(String fichier, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Remplacez "chemin/vers/votre/fichier.xml" par le chemin de votre fichier XML
        File file = new File(fichier);

        // Utiliser JDOM pour lire le fichier XML
        SAXBuilder saxBuilder = new SAXBuilder();
        try {
            Document document = saxBuilder.build(file);
            Element racine = document.getRootElement();

            // Afficher le contenu XML dans la réponse HTTP
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");

            // Utiliser JDOM pour convertir le document XML en texte
            String xmlContent = new XMLOutputter().outputString(document);

            // Écrire le contenu XML dans la réponse HTTP
            response.getWriter().println(xmlContent);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Erreur lors de la lecture du fichier XML.");
        }
    }

    //ajout de donnees dans l'Attribute du servlet
    public void setAttribute(ModelView view, HttpServletRequest request) {
        for (Map.Entry<String, Object> entry : view.getData().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            request.setAttribute(key, value);
        }
    }

    //ajout de nouveau session dans la session
    public void addSession(ModelView view, HttpServletRequest request) {
        HttpSession session = request.getSession();
        for (Map.Entry<String, Object> entry : view.getSession().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            session.setAttribute(key, value);
        }
    }

    //supprime les sessions demander
    public void removeSession(ModelView view, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(view.isInvalidateSession()) {
            session.invalidate();
        }
        else {
            for(String key : view.getRemoveSession()){
                session.removeAttribute(key);
            }
        }
    }

    //chec si la method qui appele a le meme methode sur la fonction
    public void checkCorsMethod(Method method, String corsMethod) throws Exception {
        if(method.isAnnotationPresent(CorsMethod.class)) {
            String methodeDemande = method.getAnnotation(CorsMethod.class).method();
            if(!methodeDemande.equals("") && !methodeDemande.equalsIgnoreCase(corsMethod)) {
                throw new Exception("Vous n'aviez pas le droit d'acceder a cet methode.");
            }
        }
    }

    //check si la methode a l'annotation Auth
    public void checkMethod(Method method, HttpServletRequest request) throws Exception {
        if(method.isAnnotationPresent(Auth.class)) {
            String profil = method.getAnnotation(Auth.class).profil();
            String auth_session = getServletContext().getInitParameter("auth_session");
            String profil_session = getServletContext().getInitParameter("profil_session");
            HttpSession session = request.getSession();
            if(session.getAttribute(auth_session)==null || (Boolean)session.getAttribute(auth_session)==false) {
                throw new Exception("Desole vous n'etes pas connecte");
            }

            if(profil.equals("")==false && session.getAttribute(profil_session)!=null && profil.equals((String)session.getAttribute(profil_session))==false) {
                throw new Exception("Desole vous n'avez pas acces a cette methode");
            }
        }
    }

    public void checkSession(Object object, Method method, HttpServletRequest request) throws Exception {
        if(method.isAnnotationPresent(Session.class)) {
            System.out.println("manontany izyy??");
            String attribut = "session";
            String[] sessions = method.getAnnotation(Session.class).sessions();
            Class<?> class1 = object.getClass();
            Field champ = class1.getDeclaredField(attribut);
            if(champ == null)
                throw new Exception("Verifier votre class mais vous n'aviez pas encore l'attribut session.\n Veuillez ajouter une attribut session dans votre class.");
            String nomMethode =  "set" + this.changeFirstAName(attribut);
            Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
            HttpSession session = request.getSession();
            Map<String, Object> sessionAttributes = new HashMap<>();
            
            if(sessions.length == 0) {
                Enumeration<String> attributeNames = session.getAttributeNames();
                while (attributeNames.hasMoreElements()) {
                    String attributeName = attributeNames.nextElement();
                    Object attributeValue = session.getAttribute(attributeName);
                    sessionAttributes.put(attributeName, attributeValue);
                }
            }
            else {
                for(String nom : sessions) {
                    sessionAttributes.put(nom, session.getAttribute(nom));
                }
            }

            setter.invoke(object, sessionAttributes); 
        }
    }

    public void listePackage(File dossier, String packages) {
        try{
            String packageName = packages;
            for (File file : dossier.listFiles()) {
                if (file.isDirectory()) {
                        packages = packages + "." + file.getName();
                        this.listePackage(file, packages);
                }
                else{
                    this.getClass(file, packages);
                }
                packages = packageName;
            }
            
            
        } catch(Exception io){
            io.printStackTrace();
        }
    }
    
    public void getClass(File fichier, String packages) throws Exception {
        String name = fichier.getName();
        name = name.split(".class")[0];
        Class<?> classe = Class.forName( packages + "." + name );
        this.getListeClasse().add(classe);
        System.out.println("class: " + classe.getName());
        //prends tous les noms des methodes ou il y a des annotation URL    
            
        Method[] methods = classe.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(Url.class)){
                Mapping mapping = new Mapping(name, method.getName());
                String key = "/"+name+""+method.getAnnotation(Url.class).name(); 
                this.getMappingUrls().put( key, mapping);
            }
        }
            
        if(classe.getSuperclass() != null){
            Class<?> c = classe.getSuperclass();
            methods = c.getDeclaredMethods();
            for (Method method : methods) {
                if(method.isAnnotationPresent(Url.class)){
                    Mapping mapping = new Mapping(name, method.getName());
                    this.getMappingUrls().put( "/"+name+""+method.getAnnotation(Url.class).name(), mapping);
                }
            }
        }
        
        this.getInstances().put(name, null);    

    }
    

    public String changeFirstAName(String nom){
		return nom.substring(0,1).toUpperCase() + nom.substring(1);
	}

// ty le fonction manao setters anle objet 
    public Object makaParametreDonnees(Object object, Map<String, String[]> params, Class<?> class1) throws Exception{
        if(params.isEmpty()==false) {
            for (String paramName : params.keySet()) {
                String[] values = params.get(paramName);
                Object reponse = null;
                if(values!=null && values.length == 1){
                    reponse = (Object)values[0];
                    try{
                        Field champ = class1.getDeclaredField(paramName);
                        String nomMethode =  "set" + this.changeFirstAName(paramName);
                        Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                        reponse = castValue(champ.getType(), values[0]);    //micaste anle valiny io fonction io
                        setter.invoke(object, reponse); 
                    }
                    catch(Exception io){
                        io.printStackTrace();
                    }
                } 
                
                else if(values!=null && values.length > 1) {
                    reponse = (Object)values;
                    try{
                        Field champ = class1.getDeclaredField(paramName);
                        String nomMethode =  "set" + this.changeFirstAName(paramName);
                        Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                        reponse = liste(champ.getType(), values);
                        setter.invoke(object, reponse); 
                    }
                    catch(Exception io) {
                        io.printStackTrace();
                    }
                }     
            }
            Field[] fields = class1.getFields();
            for(Field field : fields) {
                if(!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String")) {
                    System.out.println("Miditra atyy");
                    String nomMethode =  "get" + this.changeFirstAName(field.getName());
                    Method getter = class1.getDeclaredMethod(nomMethode);
                    Object _objet = getter.invoke(object); 
                    _objet = this.makaParametreDonnees(_objet, params, _objet.getClass());
                    nomMethode =  "set" + this.changeFirstAName(field.getName());
                    Method setter = class1.getDeclaredMethod(nomMethode, field.getType());
                    setter.invoke(object, _objet); 
                } else {
                    System.out.println(field.getName() + " : " + field.getType().isPrimitive());
                }
            }
        }
        return object;
    }

// ty le fonction manao setters anle objet avec un fichier
    private Object makaParametreDonneesAvecFichier(Object objet, HttpServletRequest request, Class<?> class1, PrintWriter out) throws IOException{
        try{
            Collection<Part> parts = request.getParts();     
            for(Part part : parts) {
                if(isFilePart(part)) {
                    String partName = part.getName();
                    // out.println("fichier: " + partName);
                    byte[] fileBytes = readBytesFromPart(part);
                    Field champ = class1.getDeclaredField(partName);
                    String nomMethode =  "set" + this.changeFirstAName(partName);
                    Method setter = class1.getDeclaredMethod(nomMethode, byte[].class);
                    setter.invoke(objet, fileBytes); 
                    // out.println("eto aloha " + nomMethode + " type = " + champ.getType());
                }else{
                    Object reponse = null;
                    String partName = part.getName();
                    // out.println("simple: " + partName);
                    String[] partValue = readValueFromPart(part);
                    if(partValue!=null && partValue.length == 1){
                        try{
                            Field champ = class1.getDeclaredField(partName);
                            String nomMethode =  "set" + this.changeFirstAName(partName);
                            Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                            reponse = castValue(champ.getType(), partValue[0]);    //micaste anle valiny io fonction io
                            setter.invoke(objet, reponse); 
                        }
                        catch(Exception io){
                            io.printStackTrace();
                        }
                    } 
                    
                    else if(partValue!=null && partValue.length > 1) {
                        try{
                            Field champ = class1.getDeclaredField(partName);
                            String nomMethode =  "set" + this.changeFirstAName(partName);
                            Method setter = class1.getDeclaredMethod(nomMethode, champ.getType());
                            reponse = liste(champ.getType(), partValue);
                            setter.invoke(objet, reponse); 
                        }
                        catch(Exception io) {
                            io.printStackTrace();
                        }
                    }
                }
            }
            Field[] fields = class1.getFields();
            for(Field field : fields) {
                if(!field.getType().isPrimitive() && !field.getType().getName().equals("java.lang.String")) {
                    System.out.println("Miditra atyy");
                    String nomMethode =  "get" + this.changeFirstAName(field.getName());
                    Method getter = class1.getDeclaredMethod(nomMethode);
                    Object _objet = getter.invoke(objet); 
                    _objet = this.makaParametreDonneesAvecFichier(_objet, request, _objet.getClass(), out);
                    nomMethode =  "set" + this.changeFirstAName(field.getName());
                    Method setter = class1.getDeclaredMethod(nomMethode, field.getType());
                    setter.invoke(objet, _objet); 
                } else {
                    System.out.println(field.getName() + " : " + field.getType().isPrimitive());
                }
            }
        }
        catch(Exception io) {
            io.printStackTrace();
            out.println(io.getMessage());
        }
        return objet;
    }

    public Object[] mamenoParametreMethode(Method method, Map<String, String[]> params) throws Exception{
        Object[] arguments = null;
        if(params.isEmpty()==false){
            Parameter[] parameters = method.getParameters();
            if(parameters.length != 0) {
                arguments = new Object[parameters.length];
                int i = 0;
                for (Parameter parameter : parameters) {
                    if(!parameter.getType().isPrimitive() && !parameter.getType().getName().equals("java.lang.String")) {
                        Object _object = parameter.getType().newInstance();
                        _object = this.makaParametreDonnees(_object, params, _object.getClass());
                        arguments[i] = _object;
                    } else {
                        for (String paramName : params.keySet()) {
                            if(paramName.equals(parameter.getAnnotation(Argument.class).name())) {
                                System.out.println(paramName + " : " + parameter.getType());
                                String[] values = params.get(paramName);
                                Object reponse = null;
                                if(values!=null && values.length == 1){
                                    arguments[i] = castValue(parameter.getType(), values[0]);
                                } 
                                
                                else if(values!=null && values.length > 1) {
                                    arguments[i] = liste(parameter.getType(), values);
                                }  
                            }   
                        }
                    }
                    i++;
                }     
            }
        } else {
            System.out.println("Tisy parametre akory");
        }
        return arguments;
    }

    public Object castValue(Class<?> type, String value) throws Exception{
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type.toString() == "java.sql.Date") {
            return java.sql.Date.valueOf(value);
        }else if (type == Timestamp.class) {
            value = value.replace('T', ' ');
            value += ":00";
            System.out.println(value);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Mandalo retsy egg");
            return new java.sql.Timestamp(formatter.parse(value).getTime());
        }else if(type == Time.class) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            return new java.sql.Time(formatter.parse(value).getTime());
        }else {
            return null;
        }
    }

    public Object liste(Class<?> type, String[] value){
        if (type == String.class) {
            return value;
        }
        else if (type == Integer.class || type == int.class) {
            int[] liste = new int[value.length];
            for(int i=0; i<value.length; i++) {
                liste[i] = Integer.parseInt(value[i]);
            }
            return liste;
        }
        else if (type == Double.class || type == double.class) {
            double[] liste = new double[value.length];
            for(int i=0; i<value.length; i++) {
                liste[i] = Double.parseDouble(value[i]);
            }
            return liste;
        }
        else {
            return null;
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doOptions(request, response);
        PrintWriter out = response.getWriter();
        try{
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String requestBody = sb.toString();

           // Décodez la chaîne de requête URL encodée
            String decodedRequestBody = URLDecoder.decode(requestBody, "UTF-8");

            // Séparez les paramètres de la chaîne de requête
            String[] params = decodedRequestBody.split("&");

            // Créez une map pour stocker les paramètres et leurs valeurs
            Map<String, String[]> paramMap = new HashMap<>();
            for (String param : params) {
                String[] keyValue = param.split("=");
                String key = keyValue[0];
                String value = null;
                if (keyValue.length == 2) {
                    value = keyValue[1];
                }
                if (paramMap.containsKey(key)) {
                    String[] existingValue = paramMap.get(key);
                    String[] newValue = new String[existingValue.length + 1];
                    System.arraycopy(existingValue, 0, newValue, 0, existingValue.length);
                    newValue[newValue.length - 1] = value;
                    paramMap.put(key, newValue);
                } else {
                    paramMap.put(key, new String[]{value});
                }
            }

            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                System.out.println(key +  " : " + Arrays.toString(values));
            }
            processRequest(request, response, paramMap);
           
        } catch(Exception io) {
            out.print(io.getMessage());
        }    
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doOptions(request, response);
        PrintWriter out = response.getWriter();
        try{
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String requestBody = sb.toString();

           // Décodez la chaîne de requête URL encodée
            String decodedRequestBody = URLDecoder.decode(requestBody, "UTF-8");

            // Séparez les paramètres de la chaîne de requête
            String[] params = decodedRequestBody.split("&");

            // Créez une map pour stocker les paramètres et leurs valeurs
            Map<String, String[]> paramMap = new HashMap<>();
            for (String param : params) {
                String[] keyValue = param.split("=");
                String key = keyValue[0];
                String value = null;
                if (keyValue.length == 2) {
                    value = keyValue[1];
                }
                if (paramMap.containsKey(key)) {
                    String[] existingValue = paramMap.get(key);
                    String[] newValue = new String[existingValue.length + 1];
                    System.arraycopy(existingValue, 0, newValue, 0, existingValue.length);
                    newValue[newValue.length - 1] = value;
                    paramMap.put(key, newValue);
                } else {
                    paramMap.put(key, new String[]{value});
                }
            }

            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                System.out.println(key +  " : " + Arrays.toString(values));
            }
            processRequest(request, response, paramMap);
        } catch(Exception io) {
            out.print(io.getMessage());
        } 
   }
    
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Method do Get = " + request.getMethod().toString());
        doOptions(request, response);
        PrintWriter out = response.getWriter();
        try{
            Map<String, String[]> params = request.getParameterMap();
            processRequest(request, response, params);
        } catch(Exception io) {
            out.print(io.getMessage());
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOptions(request, response);
        PrintWriter out = response.getWriter();
        Map<String, String[]> params = request.getParameterMap();
        processRequest(request, response, params);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Set CORS headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

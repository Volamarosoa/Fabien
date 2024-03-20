cd ../Framework 
javac -d . *java
jar cf fw.jar .
mv fw.jar ../Back/WEB-INF/lib/
cd ../Back/WEB-INF/classes
javac -cp ../lib/* -d . *java 
@REM javac --source 8 --target 8 -cp ../lib/* -d . *java 
cd ../..
jar cf Gestion_produit.war .
mv Gestion_produit.war C:/'Program Files'/'Apache Software Foundation'/'Tomcat 9.0'/webapps
#HOW TO RUN
1. create mysql database with name "to2", user "root" and blank password.
2. run maven goal: to2project(root) -> package
3. executable jar file will be created in app/target/to2project.app-1.0.jar
4. java -jar to2project.app-1.0.jar

Alternative way: <br />
1. create mysql database with name "to2", user "root", and blank password.<br />
2. just run the app in intellij "Run 'ApplicationMain'" on app/src/.../core/ApplicationMain.java

#Project structure
Persistence - DAO<br />
Domain - model classes, hibernate mapping<br />
Service - service layer<br />
App - controller and view layers



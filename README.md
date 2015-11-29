#HOW TO RUN
1. create mysql database with name "to2", user "root" and blank password.
2. run maven goal: to2project(root) -> package
3. executable jar file will be created in app/core/target/to2project.app.core-1.0.jar
4. java -jar to2project.app.core-1.0.jar

Alternative way:
1. create mysql database with name "to2", user "root", and blank password.
2. just run the app in intellij "Run 'ApplicationMain'" on app/core/src/.../ApplicationMain.java

#Organisation
Each subsystem has a dedicated package in app/ (app/expenses, app/budget, app/statistics). Teams implement their subsystems controllers and views (gui) there.
Everything else is shared and its everyone's responsibility to implement and maintain it accordingly.

#Project structure
Persistence - hibernate, DAO
Domain - model layer
Service - service layer
App - controller and view layers



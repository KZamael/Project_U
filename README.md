# Project_U
## Student Project, using Thymeleaf and Javalin.

This App is made for the "Internet-Programming" course.
Made by Javalin as the Web-Framework for Server Administration and Thymeleaf for rendering.

Uses Guava as Coreframework, Mindrot for Password-Encryption and MVN as Dependency-Manger.

All those Dependencies can be seen via Inspecting the **pom.xml**.

## Setting Up the App

At the current Version, the Application is set up by using the MainApplication.java
as the Starting Point, which holds the main() Function.

Use IntelliJ to set it as the Configuration starting Point,
by right-clicking it, selecting **'Run MainApplication.main()'**,
after you built the App via **'Build Project'**.


## Setting Up MongoDB

This App uses the following frameworks:
* [Javalin](https://javalin.io) as Web-Microframework with embedded Jetty Servlet-Container
and simple API for server side programming using HTTP and Websockets
* [Thymeleaf](https://www.thymeleaf.org) as Templating-Engine for the Creation of dynamic
server-driven HTML.
* [Mongo-Java-Driver](https://mongodb.github.io/mongo-java-driver/) for the connectivity of
MongoDB instances in Java-Applications.

Database
---------

You will need an installed MongoDB instance, which works with its own database and a
user with rights to read/write.
Using a fresh installed MongoDB instance, run:

```
mongo
> use projectU
```

Then, inside the mongoDB shell, run the following command.

```
db.createUser({user: "trainer", pwd: "sniebel", roles: [{role: "readWrite", db: "projectU"}]})
```

After this logout of the mongo-terminal, using [strg c].

```
mongo -u trainer -p --authenticationDatabase projectU
> use projectU
> db.createCollection("paygroups")`
> db.createCollection("qusers")
```



```
mongoimport -u trainer -p sniebel --authenticationDatabase projectU --db projectU --collection paymentgroups --file src/main/mongo/paymentgroups.json
mongoimport -u trainer -p sniebel --authenticationDatabase projectU --db projectU --collection users --file src/main/mongo/users.json
```

Repository can be found here: https://github.com/KZamael/Project_U
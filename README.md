# Java2DMMO
A small Java 2D game engine with server and database connections as a basic framework for a (M)MORPG.
- Clientside & Serverside: Written in Java, using the Kryonet libraries for sending and receiving packets
- Database: SQL with Microsoft SQL Server and MSSMS

## Requirements
- NetBeans (Or other suitable Java IDE)
- A SQL server
- JDK 1.8
- [Kryonet](https://github.com/EsotericSoftware/kryonet) (Included in project)
- [MSSQL jdbc driver](https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc/7.0.0.jre8) for interacting Java with the Microsoft SQL database (Also included)

## Features
- Login GUI for clients
  - Will only open if a connection is found to the server
  - Is super oversized due to me working on a 4K screen and having to account for an otherwise tiny GUI
  - Will only open the game window when a match is found for the username and password and that match isn't already connected 
- Very basic game engine using Java Canvas
  - Supports loading separate sprites and spritesheets from image files (PNG)
  - Supports loading maps and tilesets from text files
  - Currently setup with a clientside map editor where the player can modify and save the map using 'CTRL-S', but changes are only clientside
- Basic server
  - Can accept connections from multiple clients then broadcast each connected player to the clients each frame for realtime rendering
  - Can retrieve player and account data from the database in order to use or modify
- Basic SQL code to set up database
  
## Setup
1. Download the projects and open with Netbeans or import .java files into other IDE
2. Build the project and resolve any issues with the client or server projects by linking/importing the shared project library
3. Run the Database queries to create the correct database tables
    1. Run CreateTables.sql
    2. Run PopulateDB.sql
4. Modify the variables for the database in MainDB.java(Server project) for the database values i.e.:
```
private static final String serverAddress = "localhost";
private static final String serverPort = "52216";
private static final String dbName = "mmo_db";

private static final String dbUsername = "mainserver";
private static final String dbPassword = "aZhEjj11zk23455";
```

And change which return statement you are using dependant on if you are using MSSQL with windows authenticated user, or not.
The first part of the return statement includes `jdbc:sqlserver://` which will also need to be changed if you are not using the MSSQL driver for Java.

```
private final String getConnectionURL(){
    // Return the first line instead if you are not using MSSQL with a windows authenticated user
    //return "jdbc:sqlserver://" + serverAddress + ":" + serverPort + ";databaseName=" + dbName + ";user=" + dbUsername + ";password=" + dbPassword;
    return "jdbc:sqlserver://" + serverAddress + ":" + serverPort + ";databaseName=" + dbName + ";integratedSecurity=true";
}
```

5. Modify the Shared projects constants variables to suit, if you modify the MAX_PLAYERS variable make sure to also 
change the MAX_GAMEOBJECTS variable inside the Game.java file to account for the amount of players that could potentially be running around.  
  
Constants.java (Shared project):
```
public static final String SERVER_IP = "127.0.0.1";
public static final int PORT = 27960;
public static final int MAX_PLAYERS = 32;
```
Game.java (Client project):
```
private final int MAX_GAMEOBJECTS = 64;
```

6. Make sure the database is running, then run the Main.java file inside the Server project
7. Finally run the Main.java file inside the client project, and login with one of the accounts created with the populateDB.sql file (Make sure there is a character assosciated with that account)

## Other credit
[Marcus Dubreuil](https://www.youtube.com/watch?v=lDzKX3djE-M) - for the 7 hour video on creating a basic Java Canvas Game engine which I mostly followed as the outline for my clientside Game engine  
[Art credit] (http://opengameart.org/content/16x16-town-remix)

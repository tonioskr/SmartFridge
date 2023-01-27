package com.example.SmartFridge.DbMaintaince;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class Neo4jDriver {

    private static Neo4jDriver NeoDriver = null;
    private static Driver driver;
    private String uri;
    private String user;
    private String password;

    private Neo4jDriver()
    {
        uri = "bolt://localhost:7687";
        user = "neo4j";
        password = "rootroot";
        try{
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        } catch (Exception me) {
            System.out.println("ERRORE!");
            System.err.println(me);
        }
    }

    public static void close(){
        driver.close();
        System.out.println("Closing Neo4j");
    }


    // singleton pattern
    public static Neo4jDriver getInstance() {
        if(NeoDriver == null){
            NeoDriver = new Neo4jDriver();
        }
        return NeoDriver;
    }

    public static Driver  getDriver(){
        return driver;
    }

}

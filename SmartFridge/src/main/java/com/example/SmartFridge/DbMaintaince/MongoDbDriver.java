package com.example.SmartFridge.DbMaintaince;


import com.mongodb.ConnectionString;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
public class MongoDbDriver {



    private static MongoDbDriver driver = null;
    private static MongoClient mongoclient;
    private static MongoDatabase database;

    private MongoDbDriver()
    {


        //---Connect to the MongoDB---
        ConnectionString uri = new ConnectionString("mongodb://localhost:27017/?serverSelectionTimeoutMS=1000&connectTimeoutMS=1000");


        try{
            mongoclient = MongoClients.create(uri);
            database = mongoclient.getDatabase("SmartFridge");
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(command);
            System.out.println("Connected successfully to server.");
            //getCountry();
            setForClustering();
        } catch (Exception me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
            Error panic = new Error(me);
        }
    }

    private void setForClustering() {

        //for the writes
        MongoClient mongoClient = MongoClients.create(
                "mongodb://localhost:27017,localhost:27017,localhost:27017/"+
                        "?w=2&wtimeout = 5000"
        );
        MongoDatabase db = mongoClient.getDatabase("SmartFridge").
                withWriteConcern(WriteConcern.W1);

        //for the reads
        mongoClient = MongoClients.create(
                "mongodb://localhost:27017,localhost:27017,localhost:27017/"+
                        "?readPreference = secondary"
        );
        db = mongoClient.getDatabase("SmartFridge").
                withReadPreference(ReadPreference.nearest());
    }


    // singleton pattern
    public static MongoDbDriver getInstance() {
        if(driver == null){
            driver = new MongoDbDriver();
        }
        return driver;
    }

    public static void close() {
        if(mongoclient!= null){
            mongoclient.close();
            mongoclient = null;
        }

        System.out.println("Mongo Connection closed");
    }

    public static MongoCollection<Document> getUserCollection()
    {
           return database.getCollection("User");
    }

    public static MongoCollection<Document> getRecipeCollection()
    {
        return database.getCollection("Recipe");
    }

    public static MongoCollection<Document> getIngredientCollection()
    {
        return database.getCollection("Ingredient");
    }

}


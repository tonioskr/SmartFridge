package com.example.SmartFridge.DAO;
import com.example.SmartFridge.Application;
import com.example.SmartFridge.DTO.IngredientDTO;
import com.example.SmartFridge.DTO.IngredientInTheFridgeDTO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.model.RegisteredUser;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class IngredientInTheFridgeDAO {

    //get ingredients from the fridge
    public static ArrayList<IngredientInTheFridgeDTO> getProduct(RegisteredUser user){
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            Bson projectionFields = Projections.fields(
                    Projections.include("fridge"),
                    Projections.excludeId());

            ArrayList<IngredientInTheFridgeDTO> products_return = new ArrayList<>();
            Document obj = collection.find(eq("username", user.getUsername())).projection(projectionFields).first();
            ArrayList<Document> array_of_document = (ArrayList<Document>) obj.get("fridge");

            for (int i = 0; i < array_of_document.size(); i++) {
                Document appoggio = array_of_document.get(i);
                products_return.add(
                        new IngredientInTheFridgeDTO(
                                appoggio.getString("name"),
                                appoggio.getInteger("quantity"),
                                getExpiringDateFormatted(appoggio.getString("expiringDate"))
                        )
                );
            }return products_return;

        } catch (Exception error) {
            System.out.println( error );
            return null;
        }
    }

    public static Integer getMaxCalories(){
        MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();
        ArrayList<Document> results = new ArrayList<>();
        collection.find().sort(descending("calories")).into(results);
        System.out.println(results.get(0));
        return results.get(0).getInteger("calories");
    }

    //function to get a LocalDate type from a String. I'll do the cascade of try-catch cause the format of the date may vary
    public static LocalDate getExpiringDateFormatted(String myinput){
        DateTimeFormatter pattern;
        LocalDate datetime;
        try {
            pattern = DateTimeFormatter.ofPattern("dd/M/yyyy");
            datetime = LocalDate.parse(myinput, pattern);
        } catch (DateTimeParseException e) {
            try {
                pattern = DateTimeFormatter.ofPattern("d/M/yyyy");
                datetime = LocalDate.parse(myinput, pattern);
            } catch (DateTimeParseException e1) {
                try {
                    pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    datetime = LocalDate.parse(myinput, pattern);
                } catch (DateTimeParseException e2) {
                    try {
                        pattern = DateTimeFormatter.ofPattern("d/MM/yyyy");
                        datetime = LocalDate.parse(myinput, pattern);
                    } catch (DateTimeParseException e3) {
                        System.err.println(e3);
                        return null;
                    }
                }
            }
        }
        return datetime;
    }

    //function to remore an element
    public static void remove_product_mongo(IngredientInTheFridgeDTO product_to_delete, String id) {

        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            Document doc = collection.find(eq("_id", new ObjectId(Application.authenticatedUser.getId()))).first();

            String date = product_to_delete.getDate().getDayOfMonth() + "/" +
                    product_to_delete.getDate().getMonthValue() + "/" +
                    product_to_delete.getDate().getYear();

            Bson query = eq("_id", new ObjectId(Application.authenticatedUser.getId()));

            BasicDBObject update =
                    new BasicDBObject("fridge",
                            new BasicDBObject("name", product_to_delete.getName())
                                    .append("expiringDate", date)
                    );
            collection.updateOne(query, new BasicDBObject("$pull", update));

            } catch (Exception error) {
            System.out.println( error );
        }
    }

    public static boolean add_product(IngredientInTheFridgeDTO p){
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(Application.authenticatedUser.getId()));

            BasicDBObject product_mongo = new BasicDBObject();
            product_mongo.put("name", p.getName());
            product_mongo.put("quantity", p.getQuantity());
                String date = p.getDate().getDayOfMonth() + "/" +
                    p.getDate().getMonthValue() + "/" +
                    p.getDate().getYear();
            product_mongo.put("expiringDate", date);

            BasicDBObject update = new BasicDBObject();
            update.put("$push", new BasicDBObject("fridge",product_mongo));

            collection.updateOne(query, update);
            return true;
        } catch (Exception error) {
            System.err.println( error );
            return false;
        }
    }

    public static boolean deleteIngredient(IngredientDTO ingred)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();
            collection.deleteOne(eq("food", ingred.getFood()));
            System.out.println("delete ingredient went ok");
            return true;
        } catch (Exception error) {
            System.out.println("delete ingredient went wrong");
            return false;
        }

    }
}



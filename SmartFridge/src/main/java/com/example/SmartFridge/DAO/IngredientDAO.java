package com.example.SmartFridge.DAO;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DTO.IngredientDTO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class IngredientDAO {
    public static ArrayList<IngredientDTO> search_ingredient(String food,String calories,int skip)
    {
        MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();
        ArrayList<IngredientDTO> ingredients_to_return = new ArrayList<>();
        JSONObject obj;
        Bson filter = Filters.and(regex("food", ".*" + Pattern.quote(food) + ".*", "i"), Filters.lte("calories",Integer.parseInt(calories)));
        try (MongoCursor<Document> cursor = collection.find(filter).skip(20*skip).limit(20).iterator()) {
            while (cursor.hasNext()) {
                String text = cursor.next().toJson(); //i get a json
                obj = new JSONObject(text);
                //if(Float.parseFloat(obj.getString("calories")) < Integer.parseInt(calories)) {
                    System.out.println(Float.parseFloat(obj.getString("calories")) + "," + Integer.parseInt(calories.replace(".","")));
                    ingredients_to_return.add(
                            new IngredientDTO(
                                    obj.getString("food"),
                                    obj.getString("measure"),
                                    obj.getString("grams"),
                                    Integer.parseInt(obj.getString("calories").replace(".","")),
                                    obj.getString("protein"),
                                    obj.getString("fat"),
                                    obj.getString("fiber"),
                                    obj.getString("carbs"),
                                    obj.getString("category")
                            ));
                //}
            }
            return ingredients_to_return;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static IngredientDTO getIngredient(String food)
    {
        MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();
        try {
            //System.out.println(search_ingredient(food),calories.get);
            Document obj = collection.find(eq("food", food)).first();
            IngredientDTO result = new IngredientDTO(obj.getString("food"),
                    obj.getString("measure"),
                    obj.getString("grams"),
                    Integer.parseInt(obj.getString("calories").replace(".","")),
                    obj.getString("protein"),
                    obj.getString("fat"),
                    obj.getString("fiber"),
                    obj.getString("carbs"),
                    obj.getString("category"));
            return result;
        } catch (Exception error){
            System.out.println(error);
            return null;
        }
    }


    public static ArrayList<IngredientDTO> getListOfIngredient(int limit, int skipped_times){
        // retrieve ingredient collection
        MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();

        ArrayList<IngredientDTO> ingredients_to_return = new ArrayList<>();

        JSONObject obj;
        try (MongoCursor<Document> cursor = collection.find().skip(limit*skipped_times).limit(limit).projection(Projections.excludeId()).iterator()) {
            while (cursor.hasNext()) {
                String text = cursor.next().toJson(); //i get a json
                obj = new JSONObject(text);
                ingredients_to_return.add(
                        new IngredientDTO(
                                obj.getString("food"),
                                obj.getString("measure"),
                                obj.getString("grams"),
                                Integer.parseInt(obj.getString("calories").replace(".","")),
                                obj.getString("protein"),
                                obj.getString("fat"),
                                obj.getString("fiber"),
                                obj.getString("carbs"),
                                obj.getString("category")
                        )
                );
            }
            return ingredients_to_return;

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addToFridge(IngredientDTO ingredientDTO) {
        //add the ingredient to user fridge
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(Application.authenticatedUser.getId()));
        BasicDBObject ingredient = new BasicDBObject();
        ingredient.put("name", ingredientDTO.getFood());
        ingredient.put("quantity", 2);
        ingredient.put("expiringDate", "21/9/2022");

        BasicDBObject update = new BasicDBObject();
        update.put("$push", new BasicDBObject("fridge",ingredient));

        collection.updateOne(query, update);

        //action to be implemented
        System.out.println("Prodotto aggiunto");
    }

    public static void removeIngredient(IngredientDTO ingredient) {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();
            collection.deleteOne(eq("food", ingredient.getFood()));
        } catch (Exception error) {
            System.out.println( error );
        }
    }

    public static void addIngredient(IngredientDTO ingredient) {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();
            Document doc = new Document()
                    .append("food",ingredient.getFood())
                    .append("measure",ingredient.getMeasure())
                    .append("grams",ingredient.getGrams())
                    .append("calories",ingredient.getCalories())
                    .append("protein",ingredient.getProtein())
                    .append("fat",ingredient.getFat())
                    .append("fiber",ingredient.getFiber())
                    .append("carbs",ingredient.getCarbs())
                    .append("category", ingredient.getCategory());
            collection.insertOne(doc);
        } catch (Exception error) {
            System.out.println( error );
        }
    }

    public static boolean updateIngredient(IngredientDTO row) {
        MongoCollection<Document> collection = MongoDbDriver.getIngredientCollection();

        Document query = new Document();
        query.append("food",row.getFood());
        Document setData = new Document();
        setData.append("measure", row.getMeasure())
                .append("grams", row.getGrams())
                .append("calories", row.getCalories())
                .append("protein", row.getProtein())
                .append("fat", row.getFat())
                .append("fiber", row.getFiber())
                .append("carbs", row.getCarbs())
                .append("category", row.getCategory());
        Document update = new Document();
        update.append("$set", setData);

        try {
            //To update single Document
            collection.updateOne(query, update);
            System.out.println("update ingredient went ok");
            return true;
        } catch (MongoException me) {
            System.err.println("Ingredient: Unable to update due to an error: " + me);
            return false;
        }
    }


}


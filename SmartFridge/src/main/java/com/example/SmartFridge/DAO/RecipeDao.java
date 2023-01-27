package com.example.SmartFridge.DAO;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DTO.*;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.DbMaintaince.Neo4jDriver;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.mongodb.client.model.Sorts.descending;
import static org.neo4j.driver.Values.parameters;

import org.bson.types.ObjectId;
import org.json.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.TransactionWork;

public class RecipeDao {

    public static String addRecipe(RecipeDTO recipe) {
        //save recipe into mongoDB

        String result = add_recipe_MongoDB(recipe);
        if(result.equals("error")){
            return "error";
        }
        recipe.setId((result));
        if(!add_recipe_Neo4J(recipe)){
            return "error";
        }
        return "ok";
    }

    private static boolean add_recipe_Neo4J(RecipeDTO recipe) {
        try (Session session = Neo4jDriver.getDriver().session()) {
            String name = recipe.getName();
            String id_receipe = recipe.getId();
            String id_user = Application.authenticatedUser.getId();

            session.writeTransaction(tx -> {
                    tx.run("MERGE (b:Recipe {name: $name, id: $id, review_count : $review_count, totalTime : $totalTime}) " +
                                    "WITH b " +
                                    "MATCH (a:User) WHERE a.id = $id_user " +
                                    "CREATE (a)-[:SHARE]->(b)",
                            parameters("name", name, "id", id_receipe,
                                    "review_count", recipe.getReviewCount(),
                                    "totalTime", recipe.getTotalTime(),
                                    "id_user", id_user)).consume();
                return 1;
            });
            System.out.println("recipe saved in neo4j");
        }catch (Exception ex){
            System.err.println("error during saving recipe on neo4j");
            //delete recipe in mongodb to mantein consistency
            delete_Recipe_MongoDB(recipe);
            return false;
        }
        return true;
    }

    private static String add_recipe_MongoDB(RecipeDTO recipe){
        Document doc;
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

            doc =
                    new Document("RecipeName", recipe.getName())
                            .append("ReviewCount", 0)
                            .append("Author", recipe.getAuthor())
                            .append("PrepareTime", recipe.getPreparationTime())
                            .append("CookTime", recipe.getCooktime())
                            .append("TotalTime", recipe.getTotalTime())
                            .append("Ingredients", recipe.getIngrients())
                            .append("Directions", recipe.getDirection())
                            .append("IngredientsList", Arrays.asList(recipe.getIngredientsList()));
            collection.insertOne(doc);

        } catch (Exception e) {
            System.out.println("error during saving recipe in MongoDB");
            return "error";
        }
        System.out.println("Recipe saved in MongoDB");
        return doc.get("_id").toString();
    }

    //function to return the index used for creating a new recipe
    public static int get_id_recipe() {
        //i will assign the id to the user
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        // we search for the last id
        Document resultDoc = collection.find().sort(descending("RecipeID")).first();
        int index = resultDoc.getInteger("RecipeID") + 1;
        return index;
    }

    public static List<RecipeDTO> recipe_of_followed_user() {
        List<RecipeDTO> RecipeList = null;
        try (Session session = Neo4jDriver.getDriver().session()) {
            RecipeList = session.readTransaction((TransactionWork<List<RecipeDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id})-[:FOLLOW]->(u1: User)-[:SHARE]->(r: Recipe)"+
                                "RETURN r.id AS id, r.name AS name, " +
                                "r.review_count as ReviewCount, r.totalTime as totalTime ",
                        parameters("id", Application.authenticatedUser.getId()
                                ));
                List<RecipeDTO> Recipe_to_send = new ArrayList<>();
                int i = 0;
                while (result.hasNext() && i < 20) {
                    Record r = result.next();
                    Recipe_to_send.add(new RecipeDTO(
                            r.get("name").asString(),
                            r.get("id").asString(),
                            r.get("ReviewCount").asInt(),
                            r.get("totalTime").asString()
                    ));
                    i++;
                    if(i == 4)
                        System.out.println("ciao");
                }
                return Recipe_to_send;
            });
            return RecipeList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


    // function will find the recipe that has the same ingredient present in the list_of_ingredient;
    public static ArrayList<RecipeDTO> get_suggested_recipe_by_ingredient(String[] list_of_product) {

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        Bson projectionFields = Projections.fields(
                Projections.include("RecipeName"),
                Projections.include("_id"),
                Projections.include("ReviewCount"),
                Projections.include("TotalTime"));

        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();

        BasicDBObject query = new BasicDBObject("IngredientsList", new BasicDBObject("$all", list_of_product));

        MongoCursor<Document> cursor = collection.find(query).projection(projectionFields).limit(20).iterator();

        try {

            while (cursor.hasNext()) {
                Document obj = cursor.next();
                recipes_to_return.add(
                        new RecipeDTO(
                                obj.getString("RecipeName"),
                                obj.get("_id").toString(),
                                obj.getInteger("ReviewCount"),
                                obj.getString("TotalTime")
                        )
                );
            }

            cursor.close();

            query = new BasicDBObject("IngredientsList", new BasicDBObject("$in", list_of_product));
            cursor = collection.find(query).projection(projectionFields).limit(20).iterator();

            while (cursor.hasNext()) {
                Document obj = cursor.next();
                recipes_to_return.add(
                        new RecipeDTO(
                                obj.getString("RecipeName"),
                                obj.get("_id").toString(),
                                obj.getInteger("ReviewCount"),
                                obj.getString("TotalTime")
                        )
                );
            }
            cursor.close();

            return recipes_to_return;
        } catch (Exception e) {
            cursor.close();
            throw new RuntimeException(e);
        }
    }

    public static List<RecipeDTO> getMyRecipe(int limit, int skipped_times) {
        List<RecipeDTO> RecipeList = null;
        int skipped_calculated = limit*skipped_times;
        try (Session session = Neo4jDriver.getDriver().session()) {
            RecipeList = session.readTransaction((TransactionWork<List<RecipeDTO>>) tx -> {
                Result result = tx.run(
                        "MATCH (u:User{id: $id})-[:SHARE]->(r:Recipe) " +
                                "RETURN r.id AS id, r.name AS name, " +
                                "r.review_count as ReviewCount, r.totalTime as totalTime" +
                                " SKIP $skip LIMIT $limit ",

                        parameters("id", Application.authenticatedUser.getId(),
                                "limit", limit,
                                "skip", skipped_calculated));
                List<RecipeDTO> Recipe_to_send = new ArrayList<>();
                while (result.hasNext()) {
                    Record r = result.next();
                    Recipe_to_send.add(new RecipeDTO(
                            r.get("name").asString(),
                            r.get("id").asString(),
                            r.get("ReviewCount").asInt(),
                            r.get("totalTime").asString()
                    ));
                }
                return Recipe_to_send;
            });
            return RecipeList;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


    public static boolean delete_Recipe_MongoDB(RecipeDTO recipe)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
            collection.deleteOne(eq("_id", new ObjectId(recipe.getId())));

            System.out.println("Recipe deleted from mongodb");
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    //skipped_time is used for retriving limit recipe at a time belonging to interval [skipped_times*limit, (skipped_times+1)*limit]
    public static ArrayList<RecipeDTO> getRecipeLoginpage(int limit, int skipped_times) {

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();
        Bson projectionFields = Projections.fields(
                Projections.include("RecipeName"),
                Projections.include("_id"),
                Projections.include("Author"),
                Projections.include("CookTime"),
                Projections.include("Ingredients"),
                Projections.include("Directions"),
                Projections.include("TotalTime"));

        try (MongoCursor<Document> cursor = collection.find().skip(skipped_times*limit).limit(limit).projection(projectionFields).iterator()) {
            while (cursor.hasNext()) {
                Document f = cursor.next();
                /*
                String text = cursor.next().toJson(); //i get a json
                JSONObject obj = new JSONObject(text);
                */
                recipes_to_return.add(
                        new RecipeDTO(
                                f.getString("RecipeName"),
                                f.get("_id").toString(),
                                f.getString("Author"),
                                f.getString("PreparationTime"),
                                f.getString("CookTime"),
                                f.getString("TotalTime"),
                                f.getString("Ingredients"),
                                f.getString("Directions")
                        )
                );
            }
            return recipes_to_return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<RecipeDTO> getRecipe(int limit, int skipped_times,String utente_username) {

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();
        Bson projectionFields = Projections.fields(
                Projections.include("RecipeName"),
                Projections.include("_id"),
                Projections.include("ReviewCount"),
                Projections.include("TotalTime"));

        MongoCursor<Document> cursor = null;

        if(utente_username != null)
            cursor = collection.find(eq("Author", utente_username)).skip(skipped_times*limit).limit(limit).projection(projectionFields).iterator();
        else
            cursor = collection.find().skip(skipped_times*limit).limit(limit).projection(projectionFields).iterator();

        try {
            while (cursor.hasNext()) {
                Document f = cursor.next();
                recipes_to_return.add(
                        new RecipeDTO(
                                f.getString("RecipeName"),
                                f.get("_id").toString(),
                                f.getInteger("ReviewCount"),
                                f.getString("TotalTime")
                        )
                );
            }
            cursor.close();
            return recipes_to_return;
        } catch (Exception e) {
            cursor.close();
            throw new RuntimeException(e);
        }
    }

    public static RecipeDTO getSingleRecipe(RecipeDTO recipe) {

        // retrieve information
        Bson projectionFields = Projections.fields(
                Projections.exclude("RecipeName"),
                Projections.exclude("ReviewCount"),
                Projections.exclude("TotalTime"));

        // retrieve user collection
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        // we search for username
        Document obj = collection.find(eq("_id", new ObjectId(recipe.getId()))).projection(projectionFields).first();

        try{
            recipe.setAuthor(obj.getString("Author"));
            recipe.setPreparationTime(obj.getString("PrepareTime"));
            recipe.setCooktime(obj.getString("CookTime"));
            recipe.setIngrients(obj.getString("Ingredients"));
            recipe.setDirection(obj.getString("Directions"));
            //this 3 following line to get the list of ingredient
            List<String> support = obj.getList("IngredientsList",String.class);
            String[] return_list_ingredient = support.toArray(new String[0]);
            recipe.setIngredientsList(return_list_ingredient);
            try{
                List<Object> l = obj.getList("reviews",Object.class);
                recipe.setReviews(return_array_reviews(l));
            } //this because some document don't have reviews yet
            catch (Exception e){recipe.setReviews(null);}

            return recipe;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<RecipeDTO> getSearchedRecipe(String recipeName,int skip) {
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        ArrayList<RecipeDTO> recipes_to_return = new ArrayList<>();
        JSONObject obj;

        try (MongoCursor<Document> cursor = collection.find(regex("RecipeName", ".*" + Pattern.quote(recipeName) + ".*", "i")).skip(20*skip).limit(20).iterator()) {
            while (cursor.hasNext()) {
                Document d = cursor.next();
                String id = d.get("_id").toString();
                String text = d.toJson();
                obj = new JSONObject(text);
                String review = null;
                try{review = obj.getString("reviews");} //this because some document don't have reviews yet
                catch (Exception e){review = null;}
                try {
                    recipes_to_return.add(
                            new RecipeDTO(
                                    obj.getString("RecipeName"),
                                    id,
                                    Integer.parseInt(obj.getString("ReviewCount")),
                                    obj.getString("Author"),
                                    obj.getString("PrepareTime"),
                                    obj.getString("CookTime"),
                                    obj.getString("TotalTime"),
                                    obj.getString("Ingredients"),
                                    obj.getString("Directions"),
                                    getIngedientList(obj.getString("IngredientsList")),
                                    review != null ? return_array_reviews_json("{ reviews: " + review + "}") : null
                            )
                    );
                }
                catch (Exception e){
                    //a recipe is not set correctly in the db, continue  scanning
                }
            }
            return recipes_to_return;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //remove a review and update review_count
    public static void removeReviews(RecipeDTO Recipe,String username, int rate)
    {
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        Bson query = eq("_id", new ObjectId(Recipe.getId()));

        BasicDBObject delete =
                new BasicDBObject("reviews",
                        new BasicDBObject("profileID", username)
                                .append("Rate",rate)
                );

        BasicDBObject update = new BasicDBObject("$pull", delete);
        update = update.append("$inc", new BasicDBObject().append("ReviewCount", -1));

        collection.updateOne(query, update);
    }

    public static ReviewDTO[] return_array_reviews(List<Object> l) {
        ReviewDTO return_Array[] = new ReviewDTO[l.size()];
        for(int i=0; i<l.size();i++){
            Object support = l.get(i);
            return_Array[i] = new ReviewDTO(
                    ((Document) support).getString("profileID"),
                    ((Document) support).getInteger("Rate"),
                    ((Document) support).getString("Comment")
            );
        }
        return return_Array;
    }

    public static String[] getIngedientList(String d){
        return rubahFormat(d).split(",");
    }

    public static String rubahFormat(String d){
        return d.replaceAll("[\\[\\]\\\"]","");
    }

    private static ReviewDTO[] return_array_reviews_json(String reviews) throws JSONException {
        JSONObject obj = new JSONObject(reviews);
        JSONArray arr = obj.getJSONArray("reviews");

        ReviewDTO[] array_of_reviews = new ReviewDTO[arr.length()];
        for (int i = 0; i < arr.length(); i++) {
            String user = arr.getJSONObject(i).getString("profileID");
            int rating = arr.getJSONObject(i).getInt("Rate");
            String Description = arr.getJSONObject(i).getString("Comment");
            array_of_reviews[i] = new ReviewDTO(user, rating, Description);
        }
        return array_of_reviews;
    }

    public static void updateRecipe(RecipeDTO Recipe, RecipeDTO Recipe_old)
    {
        if(!updateRecipeOnMongoDB(Recipe)){
            System.err.println("An error occur during updating on mongodb");
            return;
        }
        if(!updateRecipeOnNeo4J(Recipe)){
            System.err.println("An error occur during updating on Neo4J");
            updateRecipeOnMongoDB(Recipe_old);
            return;
        }
        System.out.println("the updating went ok");
    }

    private static boolean updateRecipeOnNeo4J(RecipeDTO recipe) {
        try (Session session = Neo4jDriver.getDriver().session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (a:Recipe {id:$id}) " +
                                "SET a.name = $name , a.review_count = $rw , a.totalTime = $tot",
                        parameters("id", recipe.getId(),
                        "name", recipe.getId(),
                                "rw", recipe.getTotalTime(),
                                "tot", recipe.getTotalTime())).consume();
                return 1;
            });
            System.out.println("the recipe correctly update on neo4j");
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    private static boolean updateRecipeOnMongoDB(RecipeDTO Recipe) {
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        Document query = new Document();
        query.append("_id",  new ObjectId(Recipe.getId()));
        Document setData = new Document();
        setData.append("RecipeName", Recipe.getName())
                .append("Author", Recipe.getAuthor())
                .append("Ingredients", Recipe.getIngrients())
                .append("Directions", Recipe.getDirection());
        Document update = new Document();
        update.append("$set", setData);
        try {
            //To update single Document
            collection.updateOne(query, update);
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
    }

    public static void addReview(ReviewDTO review, String id_recipe){
        try {
            MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
            BasicDBObject query = new BasicDBObject();
            query.put( "_id", new ObjectId(id_recipe));


            BasicDBObject review_mongo = new BasicDBObject();
            review_mongo.put("profileID", Application.authenticatedUser.getUsername());
            review_mongo.put("Rate", review.getRate());
            review_mongo.put("Comment", review.getComment());


            BasicDBObject update = new BasicDBObject().append("$push", new BasicDBObject().append("reviews",review_mongo));
            update = update.append("$inc", new BasicDBObject().append("ReviewCount", 1));

            collection.updateOne(query, update);

            //to manteining consistency, update the review count also on the graph
            incremente_review_count_on_the_graph(id_recipe);


        } catch (Exception error) {
            System.out.println( error );
        }
    }

    private static void incremente_review_count_on_the_graph(String id_recipe) {
        try (Session session = Neo4jDriver.getDriver().session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (a:Recipe { id: $id}) " +
                                "SET a.review_count = a.review_count+1",
                        parameters("id", id_recipe)).consume();
                return 1;
            });
        }
    }

    public static void removerecipe(RecipeDTO selected_recipe) {

        RecipeDTO recipe = getSingleRecipe(selected_recipe); //here i get all the detail for a recipe and reinsert everything

        //delete recipe from mongoDB
        if(!delete_Recipe_MongoDB(recipe)){
            return;
        }

        if(!delete_Recipe_Neo4J(recipe)){
            return;
        }

        System.out.println("Recipe removed in a correct way");
        return;
    }

    private static boolean delete_Recipe_Neo4J(RecipeDTO recipe) {
        try (Session session = Neo4jDriver.getDriver().session()) {

            session.writeTransaction(tx -> {
                tx.run("MATCH (a:Recipe {id: $id}) " +
                                "DETACH DELETE a",
                        parameters("id", recipe.getId())).consume();
                return 1;
            });
        }catch (Exception e){
            System.err.println( "error verified during the deletion of a recipe in Neo4j" );
            //here i have to insert the user also from MongoDB

            add_recipe_MongoDB(recipe);
            return false;
        }
        return true;
    }

}


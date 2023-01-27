package com.example.SmartFridge.DAO;
import com.example.SmartFridge.DTO.AggregationTransportDTO;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import static java.util.Collections.sort;


public class aggregationsMongo {
    public static ArrayList<AggregationTransportDTO> top10votedrecipe(){

        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        ArrayList<Document> output = null;
        try {
             output = collection.aggregate(Arrays.asList(
                    new Document("$unwind", "$reviews"),
                    new Document("$group", new Document("_id", new Document("recipeName", "$RecipeName"))
                            .append("avarageRate", new Document("$avg", "$reviews.Rate"))),
                    new Document("$sort", new Document("avarageRate", -1)),
                    new Document("$limit", 10),
                    new Document("$project", new Document("_id", 0)
                            .append("recipeName", "$_id.recipeName")
                            .append("avarageRate", 1))
            )).into(new ArrayList<>());
        }
        // avarageRate | recipeName
        catch (Exception e){
            System.out.println(e);
        }

        ArrayList<AggregationTransportDTO> aggregation_array = new ArrayList<>();
        for(Document d: output){
            aggregation_array.add(new AggregationTransportDTO(
                    d.getString("recipeName"),
                    null,
                    d.getDouble("avarageRate")
            ));
        }
        return aggregation_array;
    }

    public static ArrayList<AggregationTransportDTO> userMostCommented(){
        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();

        ArrayList<Document> output = null;
        try {
            output = collection.aggregate(Arrays.asList(
                    new Document("$unwind", "$reviews"),
                    new Document("$group", new Document("_id", new Document("Username", "$reviews.profileID"))
                            .append("totalComments", new Document("$count", new Document()))),
                    new Document("$sort", new Document("totalComments", -1)),
                    new Document("$limit", 10),
                    new Document("$project", new Document("_id", 0)
                            .append("Username", "$_id.Username")
                            .append("totalComments", 1))
            )).into(new ArrayList<>());
        }
        // totalComments | Username
        catch (Exception e){
            System.out.println(e);
        }

        ArrayList<AggregationTransportDTO> aggregation_array = new ArrayList<>();
        for(Document d: output){
            aggregation_array.add(new AggregationTransportDTO(
                    d.getString("Username"),
                    null,
                    Double.valueOf(d.getInteger("totalComments"))
            ));
        }
        return aggregation_array;

    }

    public static ArrayList<AggregationTransportDTO> top10Ingredients() {

        MongoCollection<Document> collection = MongoDbDriver.getRecipeCollection();
        ArrayList<Document> output = null;
        try {
            output = collection.aggregate(Arrays.asList(
                    new Document("$unwind", "$IngredientsList"),
                    new Document("$group", new Document("_id", new Document("ingredient", "$IngredientsList"))
                            .append("timeUsed", new Document("$count", new Document()))),
                    new Document("$sort", new Document("timeUsed", -1)),
                    new Document("$limit", 10),
                    new Document("$project", new Document("_id", 0)
                            .append("timeUsed", 1)
                            .append("Ingredient", "$_id.ingredient"))
            )).into(new ArrayList<>());
        } catch (Exception e) {
            System.out.println(e);
        }
        // timeUsed | Ingredient
        ArrayList<AggregationTransportDTO> aggregation_array = new ArrayList<>();
        for(Document d: output){
            aggregation_array.add(new AggregationTransportDTO(
                    d.getString("Ingredient"),
                    null,
                    Double.valueOf(d.getInteger("timeUsed"))
            ));
        }
        return aggregation_array;
    }

    public static ArrayList<AggregationTransportDTO> ingredientsByCountry()
    {

        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();
        ArrayList<Document> output=null;
        try {
            output = collection.aggregate(Arrays.asList(
                    new Document("$unwind", "$fridge"),
                    new Document("$group", new Document("_id", new Document("country", "$country")
                            .append("ingredient","$fridge.name"))
                            .append("quantityInsertedInThefridge", new Document("$sum", "$fridge.quantity"))),
                    new Document("$sort", new Document("quantityInsertedInThefridge", 1)),
                    new Document("$group", new Document("_id", new Document("country", "$_id.country"))
                            .append("ingredient", new Document("$last", "$_id.ingredient"))
                            .append("quantityInsertedInThefridge", new Document("$last", "$quantityInsertedInThefridge"))),
                    new Document("$project", new Document("_id", 1)
                            .append("ingredient", 1)
                            .append("country", "$_id.country")
                            .append("quantityInsertedInThefridge", 1)),
                    new Document("$limit", 20)
            )).into(new ArrayList<>());
        }

        // ingredient | country | quantityInsertedInThefridge
        catch (Exception e){
            System.out.println(e);
        }

        ArrayList<AggregationTransportDTO> aggregation_array = new ArrayList<>();
        for(Document d: output){
            aggregation_array.add(new AggregationTransportDTO(
                    d.getString("country"),
                    d.getString("ingredient"),
                    Double.valueOf(d.getInteger("quantityInsertedInThefridge"))
            ));

        }
        return aggregation_array;
    }
}

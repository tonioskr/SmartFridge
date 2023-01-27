package com.example.SmartFridge.DAO;

import com.example.SmartFridge.Application;
import com.example.SmartFridge.DbMaintaince.MongoDbDriver;
import com.example.SmartFridge.model.IngredientInFridge;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class FridgeDAO {

    public static boolean updateFridge(ObservableList<IngredientInFridge> fridge)
    {
        try {
            MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

            for(IngredientInFridge p : fridge) {

                String date = p.getExpireDate().getDayOfMonth() + "/" +
                        p.getExpireDate().getMonthValue() + "/" +
                        p.getExpireDate().getYear();

                Bson query = and(
                        eq("_id", new ObjectId(Application.authenticatedUser.getId())),
                        eq("fridge.name", p.getName()),
                        eq("fridge.expiringDate", date )
                );

                Document documentList = new Document();
                documentList.append(String.format("%s.$.%s","fridge","quantity"), p.getQuantity());
                Document document = new Document("$set",documentList);//$set

                collection.updateOne(query, document);

                remove_product_with_0_as_quantity();

            }
            return true;
        } catch (Exception error) {
            System.out.println( error );
            return false;
        }
    }

    private static void remove_product_with_0_as_quantity() {
        MongoCollection<Document> collection = MongoDbDriver.getUserCollection();

        Bson query = eq("_id", new ObjectId(Application.authenticatedUser.getId()));

        BasicDBObject update =
                new BasicDBObject("fridge",
                        new BasicDBObject("quantity",0)
                );
        collection.updateOne(query, new BasicDBObject("$pull", update));

    }
}

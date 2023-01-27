package com.example.SmartFridge.model;


import java.time.LocalDate;
import java.util.List;

public class Fridge {
    private List<IngredientInFridge> MyFridge;
    // constructor
    public Fridge(List<IngredientInFridge> products) {
        this.MyFridge = products;
    }

    public void setMyFridge(List<IngredientInFridge> myFridge) {
        MyFridge = myFridge;
    }

    public List<IngredientInFridge> getMyFridge() {
        return MyFridge;
    }

    public void addProductToFridge(String product, LocalDate expireDate)
    {
        // search for product in fridge
       for(Integer i= 0; i< this.MyFridge.size();i++){
            if(MyFridge.get(i).getName().equals(product)) // if product is already in fridge
            {
                MyFridge.get(i).setQuantity(MyFridge.get(i).getQuantity()+1); // increment its quantity
            }
       }
       // else, add new row to fridge
        IngredientInFridge newProduct = new IngredientInFridge(product, 1 , expireDate);
        MyFridge.add(newProduct);
    }

    public void removeProduct(IngredientInFridge product){
        MyFridge.remove(product);
    }

    public int getTotalNumberOfProduct() {
        return MyFridge.size();
    }

    public boolean isEmpty() {
        return MyFridge.isEmpty();
    }
}

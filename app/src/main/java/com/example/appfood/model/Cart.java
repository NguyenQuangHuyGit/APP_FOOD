package com.example.appfood.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private static Map<Integer,Integer> items = new HashMap<>();
    public static List<Integer> itemIds = new ArrayList<>();

    public void updateItem(int id, int amount){
        if (!itemIds.contains(id)){
            itemIds.add(id);
        }
        items.put(id,amount);
    }
//    public void updateCart(int id,int amount){
//        items.put(id,amount);
//    }
    public void removeFromCart(int id){
        int currentAmount = items.get(id);
        if(currentAmount==1){
            items.remove(id);
            itemIds.remove(id);
        }
        else {
            updateItem(id,--currentAmount);
        }
    }
    public List<Integer> getItemIds(){
        return  this.itemIds;
    }
}

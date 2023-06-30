package com.example.appfood.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    public static Map<Integer,Integer> items = new HashMap<>();
    public static List<Integer> itemIds = new ArrayList<>();

    public void updateItem(int id, int amount){
        if (itemIds.contains(id)){
            items.put(id,items.get(id)+amount);
            return;
        }
        itemIds.add(id);
        items.put(id,amount);
    }

    public void fixItem(int id, int amount){
        items.put(id,amount);
    }

    public void removeFromCart(int id){
        items.remove(id);
        for(int i =0;i<itemIds.size();i++){
            if(itemIds.get(i) == id){
                itemIds.remove(i);
                break;
            }
        }
    }

    public List<Integer> getItemIds(){
        return  this.itemIds;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }
}

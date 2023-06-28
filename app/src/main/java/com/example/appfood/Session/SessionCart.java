package com.example.appfood.Session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appfood.model.Cart;
import com.google.gson.Gson;


public class SessionCart {
    private  Gson gson = new Gson();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static String key = "cart";

    public SessionCart(Context context) {
        this.sharedPreferences = context.getSharedPreferences("CART",Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.context = context;

    }
    public void setCart(Cart cart){
        String json = gson.toJson(cart);
        editor.putString(key, json);
        editor.apply();
    }
    public void clearCart(){
        editor.clear();
        editor.apply();
    }
    public Cart getCart(){
        String json = sharedPreferences.getString(key,null);
        Cart cart = gson.fromJson(json, Cart.class);
        return cart;
    }

}

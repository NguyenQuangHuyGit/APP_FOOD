package com.example.appfood.model;

public class Bill {
    private int id;
    private String date;
    private int count;
    private String totalBill;

    public Bill(){}

    public Bill(int id, String date, int count, String totalBill) {
        this.id = id;
        this.date = date;
        this.count = count;
        this.totalBill = totalBill;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }
}

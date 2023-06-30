package com.example.appfood.model;

public class Bill {
    private int id;
    private String date;
    private int count;
    private Double totalBill;

    public Bill(int id, String date, int count, Double totalBill) {
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

    public Double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }
}

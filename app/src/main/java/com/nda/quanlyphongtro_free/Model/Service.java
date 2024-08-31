package com.nda.quanlyphongtro_free.Model;

public class Service {
    private String id;
    private String name, price, unit;
    private boolean isDelete;

    public Service(){}

    public Service(String id, String name, String price, String unit, boolean isDelete) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.isDelete = isDelete;
    }

    public String getId() {
        return id;
    }


    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

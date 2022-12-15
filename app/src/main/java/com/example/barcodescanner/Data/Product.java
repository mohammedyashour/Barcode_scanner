package com.example.barcodescanner.Data;

public class Product {
String  Description ,barcode ,exp_date ,pord_date , price , product_image_id , product_image_uri ,product_name , product_type,username;
    public Product() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getExp_date() {
        return exp_date;
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }

    public String getPord_date() {
        return pord_date;
    }

    public void setPord_date(String pord_date) {
        this.pord_date = pord_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_image_id() {
        return product_image_id;
    }

    public void setProduct_image_id(String product_image_id) {
        this.product_image_id = product_image_id;
    }

    public String getProduct_image_uri() {
        return product_image_uri;
    }

    public void setProduct_image_uri(String product_image_uri) {
        this.product_image_uri = product_image_uri;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public Product(String description, String barcode, String exp_date, String pord_date, String price, String product_image_id, String product_image_uri, String product_name, String product_type, String username) {
        Description = description;
        this.barcode = barcode;
        this.exp_date = exp_date;
        this.pord_date = pord_date;
        this.price = price;
        this.product_image_id = product_image_id;
        this.product_image_uri = product_image_uri;
        this.product_name = product_name;
        this.product_type = product_type;
        this.username = username;
    }
}

package com.taesiri.kioskfoodanroid;

/**
 * Created by MohammadReza on 9/1/2014.
 */
public class RestaurantData {
    private String _name;
    private String _address;
    private CategoryData[] _categories;

    public RestaurantData(){

    }

    public RestaurantData(String _name) {
        this._name = _name;
    }

    public RestaurantData(String _name, String _address) {
        this._name = _name;
        this._address = _address;
    }

    public RestaurantData(String _name, String _address, CategoryData[] _categories) {
        this._name = _name;
        this._address = _address;
        this._categories = _categories;
    }

    public void set_categories(CategoryData[] categories){
        _categories = categories;
    }

    public void set_name(String name){
        _name = name;
    }

    public void set_address(String address){
        _address = address;
    }

    public String get_name(){
        return  _name;
    }

    public  String get_address(){
        return  _address;
    }

    public CategoryData[] get_categories(){
        return _categories;
    }
 }

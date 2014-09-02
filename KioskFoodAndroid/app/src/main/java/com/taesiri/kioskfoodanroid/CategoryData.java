package com.taesiri.kioskfoodanroid;

/**
 * Created by MohammadReza on 9/1/2014.
 */
public class CategoryData {
    private String _name;
    private String _imageUrl;
    private FoodData[] _foods;

    public CategoryData(){

    }

    public CategoryData(String _name) {
        this._name = _name;
    }

    public CategoryData(String _name, String _imageUrl) {
        this._name = _name;
        this._imageUrl = _imageUrl;
    }

    public CategoryData(String _name, String _imageUrl, FoodData[] _foods) {
        this._name = _name;
        this._imageUrl = _imageUrl;
        this._foods = _foods;
    }

    public String get_name() {
        return _name;
    }

    public String get_imageUrl() {
        return _imageUrl;
    }

    public FoodData[] get_foods() {
        return _foods;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_imageUrl(String _imageUrl) {
        this._imageUrl = _imageUrl;
    }

    public void set_foods(FoodData[] _foods) {
        this._foods = _foods;
    }
}

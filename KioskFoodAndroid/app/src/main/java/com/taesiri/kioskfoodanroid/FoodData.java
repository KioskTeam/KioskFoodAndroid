package com.taesiri.kioskfoodanroid;

/**
 * Created by MohammadReza on 9/1/2014.
 */
public class FoodData {
    private String _name;
    private String _description;
    private Integer _price;
    private String _thumbnailImageUrl;
    private String[] _imagesUrls;

    public FoodData(String _name, String _description) {
        this._name = _name;
        this._description = _description;
    }

    public FoodData(String _name, String _description, Integer _price, String _thumbnailImageUrl, String[] _imagesUrls) {
        this._name = _name;
        this._description = _description;
        this._price = _price;
        this._thumbnailImageUrl = _thumbnailImageUrl;
        this._imagesUrls = _imagesUrls;
    }

    public String get_name() {
        return _name;
    }

    public String get_description() {
        return _description;
    }

    public Integer get_price() {
        return _price;
    }

    public String get_thumbnailImageUrl() {
        return _thumbnailImageUrl;
    }

    public String[] get_imagesUrls() {
        return _imagesUrls;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public void set_price(Integer _price) {
        this._price = _price;
    }

    public void set_thumbnailImageUrl(String _thumbnailImageUrl) {
        this._thumbnailImageUrl = _thumbnailImageUrl;
    }

    public void set_imagesUrls(String[] _imagesUrls) {
        this._imagesUrls = _imagesUrls;
    }


}

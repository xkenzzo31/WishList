package model;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

/**
 * Created by lucas on 06/12/2017.
 */

public class WishModel {
    private String title;
    private String image;
    private String description;
    private String productUrl;
    private boolean status = false;

    public WishModel(String title, String image, String description, String productUrl) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.productUrl = productUrl;
    }

    public WishModel(HashMap<String, String> hashMap) {
        this.title = hashMap.get("title");
        this.image = hashMap.get("image");
        this.description = hashMap.get("description");
        this.productUrl = hashMap.get("productUrl");
    }

    public WishModel() {

    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
}

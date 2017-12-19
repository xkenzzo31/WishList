package model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    public static List<WishModel> wishsfromDataSnapshot(DataSnapshot dataSnapshot) {
        List<WishModel> wishList = new ArrayList<>();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        int i = 0;
        while (iterator.hasNext()){
            i++;
            DataSnapshot ds = iterator.next();
            if (wishList.get(wishList.size()) == ds.getValue(WishModel.class)){
                wishList.add(ds.getValue(WishModel.class));
            }
        }
        return wishList;
    }
}

package model;

/**
 * Created by lucas on 06/12/2017.
 */

public class WishsModel {
    private String title;
    private String image;
    private String description;
    private String productUrl;
    private boolean status;

    public WishsModel(String title, String image, String description, String productUrl) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.productUrl = productUrl;
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

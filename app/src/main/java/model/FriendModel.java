package model;

/**
 * Created by lucas on 14/12/2017.
 */

public class FriendModel {
    private boolean status = false;
    private String urlFriend;


    public FriendModel(String urlFriend) {
        this.urlFriend = urlFriend;
    }

    public String getUrlFriend() {
        return urlFriend;
    }

    public void setUrlFriend(String urlFriend) {
        this.urlFriend = urlFriend;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

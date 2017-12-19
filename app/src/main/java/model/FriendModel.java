package model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lucas on 14/12/2017.
 */

public class FriendModel {

    private String urlFriend;
    private boolean status;


    public FriendModel(String urlFriend, boolean status) {
        this.urlFriend = urlFriend;
        this.status = status;
    }
    public FriendModel(){

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

    /**
     * parse un dataSnapshot et le retourne en {@link List<FriendModel>}
     * @param dataSnapshot
     * @return {@link List<FriendModel>}
     */

    public static List<FriendModel> friendfromDataSnapshot(DataSnapshot dataSnapshot) {
        List<FriendModel> friendModel = new ArrayList<>();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){
            DataSnapshot ds = iterator.next();
            if (!friendModel.contains(ds.getValue(FriendModel.class))){
                friendModel.add(ds.getValue(FriendModel.class));
            }
        }
        return friendModel;
    }
}

package model;

import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lucas on 15/12/2017.
 */

public class Wisher {
    private String email;
    private ArrayList<WishModel> wishs = new ArrayList<>();
    private List<FriendModel> friendModels = new ArrayList<>();
    public Wisher(String email) {
        this.email = email;

    }

    public Wisher() {
    }

    public ArrayList<WishModel> getWishs() {
        return wishs;
    }

    public List<FriendModel> getFriendModels() {
        return friendModels;
    }

    public void setFriendModels(List<FriendModel> friendModels) {
        this.friendModels = friendModels;
    }

    public void setWishs(ArrayList<WishModel> wishs) {
        this.wishs = wishs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * parse un datasnapshot et le retourne en {@link Wisher}
     *
     * @param dataSnapshot
     * @return {@link Wisher}
     */

    public static Wisher fromDataSnapshot(DataSnapshot dataSnapshot) {
        Wisher wisher = new Wisher();
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            DataSnapshot ds = iterator.next();
            if ("email".equals(ds.getKey())) {
                wisher.setEmail(ds.getValue(String.class));
            }
            else if ("friend_request".equals(ds.getKey())) {
                GenericTypeIndicator<List<FriendModel>> t = new GenericTypeIndicator<List<FriendModel>>() {};
                //TODO ligne qui crash
                List<FriendModel> list = ds.getValue(t);
                wisher.getFriendModels().addAll(list);
            }
            else if ("wishs".equals(ds.getKey())) {
                GenericTypeIndicator<ArrayList<WishModel>> t = new GenericTypeIndicator<ArrayList<WishModel>>() {};
                ArrayList<WishModel> list = ds.getValue(t);
                wisher.getWishs().addAll(list);
            }
        }


        return wisher;
    }
}

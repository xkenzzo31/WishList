package model;

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
    private List<WishModel> wishs = new ArrayList<>();
    public Wisher(String email) {
        this.email = email;

    }

    public Wisher() {
    }

    public List<WishModel> getWishs() {
        return wishs;
    }

    public void setWishs(List<WishModel> wishs) {
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
            } else if ("wishs".equals(ds.getKey())) {
                GenericTypeIndicator<List<WishModel>> t = new GenericTypeIndicator<List<WishModel>>() {};
                List<WishModel> list = ds.getValue(t);
                wisher.getWishs().addAll(list);
            }
        }
        return wisher;
    }
}

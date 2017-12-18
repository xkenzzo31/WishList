package com.example.lucas.wishlist.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lucas.wishlist.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import adapteur.WishAdapteur;
import core.SuccessCallback;
import model.Wisher;
import services.UserService;

public class MyWishsActivity extends AppCompatActivity {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishs);
        userService = new UserService(MyWishsActivity.this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        final ListView listView = (ListView) findViewById(R.id.list_wishs_have);
        userService.updateAdapter(new UserService.WishListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userService.updateWisherAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {
                        WishAdapteur adapteurNoHave = new WishAdapteur(MyWishsActivity.this,userService.getWishDontHave(wisher.getWishs()));
                        listView.setAdapter(adapteurNoHave);

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                userService.updateWisherAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {
                        WishAdapteur adapteurNoHave = new WishAdapteur(MyWishsActivity.this,userService.getWishDontHave(wisher.getWishs()));
                        listView.setAdapter(adapteurNoHave);
                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                userService.updateWisherAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {
                        WishAdapteur adapteurNoHave = new WishAdapteur(MyWishsActivity.this,userService.getWishDontHave(wisher.getWishs()));
                        listView.setAdapter(adapteurNoHave);
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

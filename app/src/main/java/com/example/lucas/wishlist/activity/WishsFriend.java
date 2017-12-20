package com.example.lucas.wishlist.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lucas.wishlist.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import adapteur.FriendAdapteur;
import adapteur.WishAdapteur;
import model.Wisher;
import services.UserService;

public class WishsFriend extends AppCompatActivity {
    private String email;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private UserService userService = new UserService(WishsFriend.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishs_friend);
        Intent i = getIntent();
        email = i.getStringExtra("email");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRef.child("users").orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Wisher friendWisher = Wisher.fromDataSnapshot(dataSnapshot);
                ListView friendlist = (ListView) WishsFriend.this.findViewById(R.id.wishs_firend_list);
                WishAdapteur wishAdapteur = new WishAdapteur(WishsFriend.this, userService.getWishtHave(friendWisher.getWishs()));
                friendlist.setAdapter(wishAdapteur);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                final Wisher friendWisher = Wisher.fromDataSnapshot(dataSnapshot);
                ListView friendlist = (ListView) WishsFriend.this.findViewById(R.id.wishs_firend_list);
                WishAdapteur wishAdapteur = new WishAdapteur(WishsFriend.this, userService.getWishtHave(friendWisher.getWishs()));
                friendlist.setAdapter(wishAdapteur);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

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

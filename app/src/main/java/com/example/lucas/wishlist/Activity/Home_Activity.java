package com.example.lucas.wishlist.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.lucas.wishlist.R;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            auth.signOut();
            Intent intent = new Intent(Home_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

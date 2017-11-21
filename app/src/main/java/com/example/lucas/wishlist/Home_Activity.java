package com.example.lucas.wishlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home_Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null){
            auth.signOut();
            Intent intent = new Intent(Home_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        Button button = (Button) findViewById(R.id.disconnect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(Home_Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

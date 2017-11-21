package com.example.lucas.wishlist;

import android.content.Intent;
import android.os.TokenWatcher;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user){
        if (user != null){
            Intent intent = new Intent(this, Home_Activity.class);
            startActivity(intent);
            finish();
        } else {
            connection();
        }
    }

    private void connection(){
        //Connection !
        Button connect = (Button) findViewById(R.id.button_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.nom_de_compte);
                EditText password = (EditText) findViewById(R.id.mot_de_passe);
                String ndc = email.getText().toString();
                String mdp = password.getText().toString();
                mAuth.signInWithEmailAndPassword(ndc,mdp)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    Toast.makeText(MainActivity.this, "Connect FAIL !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        //Creation
        Button button = (Button) findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = (EditText) findViewById(R.id.nom_de_compte);
                EditText password = (EditText) findViewById(R.id.mot_de_passe);
                String ndc = email.getText().toString();
                String mdp = password.getText().toString();
                mAuth.createUserWithEmailAndPassword(ndc,mdp)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"TA CREE TON COMPTE ENCULEE", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this,"TA PAS CREE TON COMPTE ENCULEE", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}

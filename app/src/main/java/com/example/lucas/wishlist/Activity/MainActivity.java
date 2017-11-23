package com.example.lucas.wishlist.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lucas.wishlist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static Utils.Utils.dpToPixels;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        ImageView img = (ImageView) findViewById(R.id.image_acceuil);
        img.setImageResource(R.drawable.logo_ap);
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
                if (isValidEmail(email.getText()) && password.getText().length() > 0){
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
                } else {
                    Toast.makeText(MainActivity.this , "TU NA PAS REMPLIE LES CHAMP ! TU CROYER QUE SA ALLER BUG ? ",Toast.LENGTH_LONG).show();
                    email.setHint("CHAMP VIDE OU INCORRECT !");
                    password.setHint("CHAMP VIDE OU INCORRECT !");
                }

            }
        });

        //Creation
        Button button = (Button) findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);
                // on récupére la taille de l'écran
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels - (int) dpToPixels(35,MainActivity.this);


                // set the custom dialog components - text, image and button
                final EditText email = (EditText) dialog.findViewById(R.id.register_mail);
                final EditText mdp = (EditText) dialog.findViewById(R.id.register_mdp);
                Button validation = (Button) dialog.findViewById(R.id.register_validation);
                Button leave = (Button) dialog.findViewById(R.id.register_leave);
                // action sur les élément du design
                validation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValidEmail(email.getText()) && mdp.getText().length() > 0){
                            mAuth.createUserWithEmailAndPassword(email.getText().toString(),mdp.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainActivity.this,"ton compte et crée !",Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this,"sa n'a pas marché ! recommence.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity.this,"il y a un problème dans l'email, ou le mot de passe et vide !",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                leave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                // Resize de la popup
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                dialog.show();
            }
        });

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

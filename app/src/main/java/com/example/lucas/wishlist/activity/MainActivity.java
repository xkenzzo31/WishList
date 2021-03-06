package com.example.lucas.wishlist.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lucas.wishlist.R;

import core.Error;
import core.FailCallback;
import core.SuccessCallback;
import services.UserService;
import utils.Utils;

import static utils.Utils.dpToPixels;
import static utils.Utils.isValidEmail;
import static utils.Utils.resizeBitmap;

public class MainActivity extends AppCompatActivity {
    private UserService mUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserService = new UserService(MainActivity.this);
        ImageView img = (ImageView) findViewById(R.id.image_acceuil);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logoap);
        Utils.GetScreenSize getScreenSize = new Utils.GetScreenSize(MainActivity.this);
        int widthImage = getScreenSize.getScreenWidht() - (int) dpToPixels(30,MainActivity.this);
        int heightImage = getScreenSize.getScreenHeight() /3;
        img.setImageBitmap(resizeBitmap(bm,widthImage,heightImage));


    }



    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


        updateUI();

    }

    private void updateUI(){
        if (false){
            mUserService.signOut();
            connection();
            return;
        }
        if (mUserService.isSignedIn()){
            openHome();
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

                if (!isValidEmail(email.getText()) || password.getText().length() == 0) {
                    Toast.makeText(MainActivity.this, "TU NA PAS REMPLIE LES CHAMP ! TU CROYER QUE SA ALLER BUG ? ", Toast.LENGTH_LONG).show();
                    email.setHint("CHAMP VIDE OU INCORRECT !");
                    password.setHint("CHAMP VIDE OU INCORRECT !");
                    return;
                }

                String ndc = email.getText().toString();
                String mdp = password.getText().toString();
                mUserService.authenticate(ndc, mdp,
                        new SuccessCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                openHome();
                            }

                        },
                        new FailCallback() {
                            @Override
                            public void onFail(Error error) {
                                Toast.makeText(MainActivity.this, "Connect FAIL !", Toast.LENGTH_SHORT).show();
                            }});

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
                        if (!isValidEmail(email.getText()) || mdp.getText().length() == 0) {
                            Toast.makeText(MainActivity.this, "TU NA PAS REMPLIE LES CHAMP ! TU CROYER QUE SA ALLER BUG ? ", Toast.LENGTH_LONG).show();
                            email.setHint("CHAMP VIDE OU INCORRECT !");
                            mdp.setHint("CHAMP VIDE OU INCORRECT !");
                            return;
                        }
                        String mail = email.getText().toString();
                        String password = mdp.getText().toString();
                        mUserService.register(mail, password, new SuccessCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MainActivity.this, "Register Succes !", Toast.LENGTH_SHORT).show();
                                updateUI();
                            }

                        }, new FailCallback() {
                            @Override
                            public void onFail(Error error) {
                                Toast.makeText(MainActivity.this, "Register FAIL : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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
                dialog.getWindow().setAttributes(lp);
                dialog.show();
            }
        });
    }

    private void openHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

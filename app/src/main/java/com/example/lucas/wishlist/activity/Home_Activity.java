package com.example.lucas.wishlist.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.lucas.wishlist.R;
import com.google.firebase.auth.FirebaseAuth;

import services.UserService;

public class Home_Activity extends AppCompatActivity {
    private UserService mUserService;
    private ToggleButton shouait, offert, offrire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_home);
        mUserService = UserService.getInstance(Home_Activity.this);
        if (mUserService.isSignedIn()){
            mUserService.singOut();
            Intent intent = new Intent(Home_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        shouait = (ToggleButton) findViewById(R.id.toggle_souhaits);
        offert = (ToggleButton) findViewById(R.id.toggle_offerts);
        offrire = (ToggleButton) findViewById(R.id.toggle_offire);
        shouait.setChecked(true);
        shouait.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonClick();

    }
    private void setButtonClick(){
        shouait.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    shouait.setChecked(true);
                    offert.setChecked(false);
                    offrire.setChecked(false);
                    shouait.setEnabled(false);
                    offert.setEnabled(true);
                    offrire.setEnabled(true);

                }
            }
        });
        offert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    shouait.setChecked(false);
                    offert.setChecked(true);
                    offrire.setChecked(false);
                    shouait.setEnabled(true);
                    offert.setEnabled(false);
                    offrire.setEnabled(true);
                }
            }
        });
        offrire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    shouait.setChecked(false);
                    offert.setChecked(false);
                    offrire.setChecked(true);
                    shouait.setEnabled(true);
                    offert.setEnabled(true);
                    offrire.setEnabled(false);
                }
            }
        });
    }
}

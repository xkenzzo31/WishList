package services;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import core.Error;
import core.FailCallback;
import core.SuccessCallback;
import model.UsersModel;
import model.WishsModel;

/**
 * Created by lucas on 24/11/2017.
 */

public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class.getName());

    private static UserService sInstance;
    private static DatabaseReference mDatabase;

    private Activity activity;

    private FirebaseAuth mAuth;

    public UserService(Activity activity) {
        this.activity = activity;
        mDatabase  = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }



    public @Nullable FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    public boolean isSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void singOut(){
        mAuth.signOut();
    }

    public void authenticate(@Nullable String username, @Nullable String password,
                             @NonNull final SuccessCallback successCallback,
                             final FailCallback failCallback) {
        if (successCallback == null)
        logger.warning("authenticate#successCallback not set");

        // TODO: check failCallback
        mAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            successCallback.onSuccess();
                        } else {
                            failCallback.onFail(new Error("Failed to Authenticate", task.getException()));
                        }
                    }
                });
    }

    public void register(@Nullable String username, @Nullable String password,
                         @NonNull final SuccessCallback authenticationCallback,final @NonNull FailCallback failCallback){
        if (authenticationCallback == null)
            logger.warning("register#successCallback not set");

        mAuth.createUserWithEmailAndPassword(username,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            authenticationCallback.onSuccess();
                        } else {
                            failCallback.onFail(new Error("Failed to Authenticate", task.getException()));
                        }
                    }
                });
    }

    public void pushFireBase(){
        UsersModel usersModel = new UsersModel(mAuth.getUid());
        mDatabase.child("Users").child(usersModel.getIdUser()).setValue(usersModel);
    }


}

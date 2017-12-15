package services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import core.Error;
import core.FailCallback;
import core.SuccessCallback;
import model.Wisher;
import model.UsersModel;
import model.WishModel;

/**
 * Created by lucas on 24/11/2017.
 */

public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class.getName());

    private static UserService sInstance;
    private static DatabaseReference mDatabase;

    private Activity activity;

    private FirebaseAuth mAuth;

    private Wisher wisher;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://wichlist-d0196.appspot.com");

    public UserService(Activity activity) {
        this.activity = activity;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }


    public @Nullable
    FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }

    public boolean isSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void authenticate(@Nullable String username, @Nullable String password,
                             @NonNull final SuccessCallback successCallback,
                             final FailCallback failCallback) {
        if (successCallback == null)
            logger.warning("authenticate#successCallback not set");

        // TODO: check failCallback
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    wisher = Wisher.fromDataSnapshot(dataSnapshot);

                                    mDatabase.child("users").orderByChild("email").equalTo("t@dddt.fr")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Iterator<DataSnapshot> childrenIterator = dataSnapshot.getChildren().iterator();
                                                    while (childrenIterator.hasNext()) {
                                                        Wisher friend = Wisher.fromDataSnapshot(childrenIterator.next());
                                                        successCallback.onSuccess();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    databaseError.getDetails();
                                                }
                                            });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            failCallback.onFail(new Error("Failed to Authenticate", task.getException()));
                        }
                    }
                });
    }

    public void register(@Nullable final String username, @Nullable String password,
                         @NonNull final SuccessCallback authenticationCallback, final @NonNull FailCallback failCallback) {
        if (authenticationCallback == null)
            logger.warning("register#successCallback not set");

        final List<WishModel> wishModels = Collections.emptyList();

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failCallback.onFail(new Error("Failed to Authenticate", e));
                    }
                })
                .continueWithTask(new Continuation<AuthResult, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<AuthResult> task) throws Exception {
                        wisher = new Wisher(mAuth.getCurrentUser().getEmail());
                        return mDatabase.child("users").child(mAuth.getUid()).setValue(wisher);
                    }
                })
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        WishModel wish3 = new WishModel("test3", "url3", "d3", "https://console.firebase.google.com/project/wichlist-d0196/database/wichlist-d0196/data");
                        return mDatabase.child("users").child(mAuth.getUid()).child("wishs")
                                .child(wisher.getWishs().size() + "")
                                .setValue(wish3);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        authenticationCallback.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        failCallback.onFail(new Error("Failed to Add User in User DB", e));
                    }
                });
        ;
    }

    public void pushFireBase() {
        UsersModel usersModel = new UsersModel(mAuth.getUid());
        mDatabase.child("Users").child(usersModel.getIdUser()).setValue(usersModel);
    }

    public Wisher getWisher() {
        return wisher;
    }

    public void addWish(final WishModel newWish, final OnSuccessListener<Void> onSuccessListener) {
        mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wisher = Wisher.fromDataSnapshot(dataSnapshot);

                mDatabase.child("users").child(mAuth.getUid()).child("wishs")
                        .child(wisher.getWishs().size() + "")
                        .setValue(newWish)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                onSuccessListener.onSuccess(aVoid);
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}


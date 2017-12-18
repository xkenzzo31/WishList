package services;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import core.Error;
import core.FailCallback;
import core.SuccessCallback;
import model.Wisher;
import model.WishModel;

/**
 * Created by lucas on 24/11/2017.
 */

public class UserService {
    private static Logger logger = Logger.getLogger(UserService.class.getName());

    private static UserService sInstance;
    private static DatabaseReference mDatabase;

    private Activity activity;
    //TODO a voir si on peux évité le static !
    private static FirebaseAuth mAuth;

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
                             @NonNull final SuccessCallback<Void> successCallback,
                             final FailCallback failCallback) {
        if (successCallback == null)
            logger.warning("authenticate#successCallback not set");

        // TODO: check failCallback
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateWisherAsync(new SuccessCallback<Wisher>() {
                                @Override
                                public void onSuccess(Wisher wisher) {
                                    mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            successCallback.onSuccess(null);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        } else {
                            failCallback.onFail(new Error("Failed to Authenticate", task.getException()));
                        }
                    }
                });
    }

    public void register(@Nullable final String username, @Nullable String password,
                         @NonNull final SuccessCallback<Void> successCallback, final @NonNull FailCallback failCallback) {
        if (successCallback == null)
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
                    public Task<Void> then(@NonNull Task<AuthResult> task) throws Exception {
                        wisher = new Wisher(mAuth.getCurrentUser().getEmail());
                        return mDatabase.child("users").child(mAuth.getUid()).setValue(wisher);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        successCallback.onSuccess(aVoid);

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

    public Wisher getWisher() {
        return wisher;
    }

    public  void updateWisherAsync(final SuccessCallback<Wisher> successCallback) {
        mDatabase.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wisher = Wisher.fromDataSnapshot(dataSnapshot);
                successCallback.onSuccess(wisher);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void addWish(final WishModel newWish, final OnSuccessListener<Void> onSuccessListener) {
        updateWisherAsync(new SuccessCallback<Wisher>() {
            @Override
            public void onSuccess(Wisher wisher) {
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
        });
    }
    public ArrayList<WishModel> getWishDontHave(ArrayList<WishModel> allWish){
         ArrayList<WishModel> result = new ArrayList<>();
         for (WishModel wish: allWish){
             if (wish!=null){
                 if (wish.isStatus()){
                     result.add(wish);
                 }
             }
         }
         return result;
    }
    public void updateAdapter (WishListener wishListener){
        mDatabase.child("users").child(mAuth.getUid()).child("wishs").addChildEventListener(wishListener);
    }
    public interface WishListener extends ChildEventListener{
        @Override
        void onCancelled(DatabaseError databaseError);

        @Override
        void onChildAdded(DataSnapshot dataSnapshot, String s);

        @Override
        void onChildChanged(DataSnapshot dataSnapshot, String s);

        @Override
        void onChildRemoved(DataSnapshot dataSnapshot);

        @Override
        void onChildMoved(DataSnapshot dataSnapshot, String s);
    }

    public void updateWishWishButton(final String url, final boolean wishStatus ){
        updateWisherAsync(new SuccessCallback<Wisher>() {
            @Override
            public void onSuccess(Wisher wisher) {
                for(int i = 0 ; i < wisher.getWishs().size(); i++){
                    if (wisher.getWishs().get(i).getImage().equals(url)){
                        int result = i;
                        WishModel wishModel = wisher.getWishs().get(i);
                        wishModel.setStatus(wishStatus);
                        updateWishWishButton(wishModel, result, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                }
            }
        });

    }

    public ArrayList<WishModel> getWishtHave(ArrayList<WishModel> allWish){
         ArrayList<WishModel> result = new ArrayList<>();
         for (WishModel wish: allWish){
             if (wish!=null){
                 if (!wish.isStatus()){
                     result.add(wish);
                 }
             }
         }
         return result;
    }

    public void updateWishWishButton(WishModel updateWish, int position, final  OnSuccessListener<Void> onSuccessListener){
        mDatabase.child("users").child(mAuth.getUid()).child("wishs").child(position+"").setValue(updateWish).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                onSuccessListener.onSuccess(aVoid);
            }
        });
    }



}


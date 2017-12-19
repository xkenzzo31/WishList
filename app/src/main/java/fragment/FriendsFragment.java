package fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucas.wishlist.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Iterators;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ThrowOnExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import core.SuccessCallback;
import model.FriendModel;
import model.Wisher;
import services.UserService;
import utils.Utils;

import static utils.Utils.dpToPixels;
import static utils.Utils.isValidEmail;

/**
 * Created by lucas on 29/11/2017.
 */

public class FriendsFragment extends Fragment {
    private int width, height;
    private UserService mUserService;
    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_wish1,container,false);
        final AddFloatingActionButton add_friend = rootView.findViewById(R.id.add_friend);
        mUserService = new UserService(getActivity());
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_add_friend);
                //set size  dialog
                Utils.GetScreenSize getScreenSize = new Utils.GetScreenSize(getActivity());
                width = getScreenSize.getScreenWidht() - (int) dpToPixels(35,getActivity());
                height = getScreenSize.getScreenHeight()/3;
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                //set xml attribute
                final EditText add_friend_text = dialog.findViewById(R.id.add_friend_email);
                final Button add_friend_button = dialog.findViewById(R.id.add_friend_firebase);
                add_friend_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValidEmail(add_friend_text.getText().toString())){
                            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                            mRef.child("users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    HashMap<String,HashMap<String, String>> test = new HashMap<>();
                                    test.putAll((Map<? extends String, ? extends HashMap<String, String>>) dataSnapshot.getValue());
                                    final Wisher friendWisher = Wisher.fromDataSnapshot(dataSnapshot.child(test.keySet().iterator().next()));
                                    if (friendWisher.getEmail().equals(add_friend_text.getText().toString())){
                                        mUserService.updateWisherAsync(new SuccessCallback<Wisher>() {
                                            @Override
                                            public void onSuccess(Wisher wisher) {
                                                int i = 0;
                                                for (FriendModel email : friendWisher.getFriendModels()){
                                                    i++;
                                                    if (email.getUrlFriend().equals(wisher.getEmail())){
                                                        mRef.child("users").child(dataSnapshot.getKey()).child("request_friend").child(i+"").child("status").setValue(true);
                                                        FriendModel friendModel = new FriendModel(add_friend_text.getText().toString(),true);
                                                        mUserService.addFriend(friendModel, new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });
                                                    } else {
                                                        FriendModel friendModel = new FriendModel(add_friend_text.getText().toString(),false);
                                                        mUserService.addFriend(friendModel, new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                            }
                                                        });
                                                    }

                                                }

                                            }
                                        });

                                    } else{
                                        Toast.makeText(getActivity(), "L'utilisateur n'existe pas",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
                dialog.show();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserService.updateAdapterFriend(new UserService.WishListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mUserService.updateFriendAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {


                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mUserService.updateFriendAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {


                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
        });
    }
}

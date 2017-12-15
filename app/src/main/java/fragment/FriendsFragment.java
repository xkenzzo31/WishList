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

import com.example.lucas.wishlist.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        AddFloatingActionButton add_friend = rootView.findViewById(R.id.add_friend);
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
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

                            mRef.child("users").child("friend_request").push().setValue(mUserService.getFirebaseUser().getEmail(),add_friend_text.getText().toString());

                        }
                    }
                });
                dialog.show();
            }
        });
        return rootView;
    }
}

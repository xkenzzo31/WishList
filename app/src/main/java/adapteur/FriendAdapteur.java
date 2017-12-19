package adapteur;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucas.wishlist.R;

import java.util.ArrayList;

import model.FriendModel;

/**
 * Created by lucas on 19/12/2017.
 */

public class FriendAdapteur extends ArrayAdapter<FriendModel>{
    public FriendAdapteur(@NonNull Context context, @NonNull FriendModel[] objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.wish_list_layout, parent, false);
        }
        FriendModel friendModel = getItem(position);
        TextView tv = convertView.findViewById(R.id.friend_email);
        tv.setText(friendModel.getUrlFriend());
        return convertView;
    }
}

package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucas.wishlist.R;

/**
 * Created by lucas on 29/11/2017.
 */

public class frag2 extends Fragment {
    public static frag2 newInstance() {
        return new frag2();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_show_wish2,container,false);

        return rootView;
    }
}

package fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lucas.wishlist.R;
import com.example.lucas.wishlist.activity.MainActivity;
import com.getbase.floatingactionbutton.AddFloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

import services.UserService;
import utils.Utils;

import static android.app.Activity.RESULT_OK;
import static utils.Utils.dpToPixels;
import static utils.Utils.getBitmapFromVectorDrawable;
import static utils.Utils.resizeBitmap;

/**
 * Created by lucas on 29/11/2017.
 */

public class frag0 extends Fragment {

    UserService userService;
    private static final int RESULT_LOAD_IMAGE = 1;
    private String selectedImagePath;
    private ImageButton getImage;
    private int width, height;
    public static frag0 newInstance() {
        return new frag0();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_,container,false);
        AddFloatingActionButton addWishButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_wish_button);
        addWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_add_wish);
                //set size  dialog
                Utils.GetScreenSize getScreenSize = new Utils.GetScreenSize(getActivity());
                width = getScreenSize.getScreenWidht() - (int) dpToPixels(35,getActivity());
                height = getScreenSize.getScreenHeight()/3;
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                //set XML attribute
                final EditText title,description,urlWish;
                title =  dialog.findViewById(R.id.wish_title);
                description =  dialog.findViewById(R.id.wish_description);
                urlWish = dialog.findViewById(R.id.add_wish_product_url);
                getImage = dialog.findViewById(R.id.select_image_in_gallery);
                getImage.setImageBitmap(resizeBitmap(getBitmapFromVectorDrawable(getActivity(),R.drawable.ic_take_picture),width,height));
                Button setWishInFirebase = dialog.findViewById(R.id.add_wish);
                getImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });
                dialog.show();
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                if (data != null){
                    try {
                        Uri pickedImage = data.getData();
                        selectedImagePath = pickedImage.toString();
                        InputStream input =  getActivity().getContentResolver().openInputStream(pickedImage);
                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        input.close();
                        getImage.setImageBitmap(resizeBitmap(bitmap,width,height));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Provider) {
            String str = ((Provider) context).getTheTitle();
        }
    }

    public interface Provider {
        public String getTheTitle();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

package adapteur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.lucas.wishlist.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.WishModel;
import utils.Utils;

/**
 * Created by lucas on 15/12/2017.
 */

public class WishAdapteur extends ArrayAdapter<WishModel> {
    private int screenWidth, screenHeight;

    public WishAdapteur(@NonNull Context context, @NonNull List<WishModel> wishList) {
        super(context, 0, wishList);
        Utils.GetScreenSize getScreenSize = new Utils.GetScreenSize(context);
        screenHeight = (int) (getScreenSize.getScreenHeight() /3);
        screenWidth = (int)getScreenSize.getScreenWidht();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.wish_list_layout, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.wish_picture);
        TextView title = (TextView) convertView.findViewById(R.id.wish_title);
        TextView descrip = (TextView) convertView.findViewById(R.id.wish_description);
        Button openProduct = (Button) convertView.findViewById(R.id.go_product_buy_page);
        Button haveProduc = (Button) convertView.findViewById(R.id.product_is_buy);

        WishModel mWish = getItem(position);

        AsyncTaskLoadImage asyncTaskLoadImage = new AsyncTaskLoadImage(imageView);
        asyncTaskLoadImage.execute(mWish.getImage());
        title.setText(mWish.getTitle());
        descrip.setText(mWish.getDescription());
        openProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        haveProduc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return  convertView;
    }
    public class AsyncTaskLoadImage  extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;
        public AsyncTaskLoadImage(ImageView imageView) {
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(Utils.resizeBitmap(bitmap,screenWidth,screenHeight));
        }
    }
}

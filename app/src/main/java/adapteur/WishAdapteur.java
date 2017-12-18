package adapteur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.res.ResourcesCompat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.example.lucas.wishlist.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.WishModel;
import services.UserService;
import utils.Utils;

/**
 * Created by lucas on 15/12/2017.
 */

public class WishAdapteur extends ArrayAdapter<WishModel> {
    private int screenWidth, screenHeight;
    private  Context context;
    private UserService userService;
    public WishAdapteur(@NonNull Context context, @NonNull List<WishModel> wishList) {
        super(context, 0, wishList);
        this.context = context;
         userService = new UserService((Activity) context);
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
        final WishModel mWish = getItem(position);
        if (mWish.isStatus()){
            //set XML
            ImageView imageView = (ImageView) convertView.findViewById(R.id.wish_picture);
            TextView title = (TextView) convertView.findViewById(R.id.wish_title);
            TextView descrip = (TextView) convertView.findViewById(R.id.wish_description);
            Button openProduct = (Button) convertView.findViewById(R.id.go_product_buy_page);
            Button haveProduc = (Button) convertView.findViewById(R.id.product_is_buy);


            //set atribute
            AsyncTaskLoadImage asyncTaskLoadImage = new AsyncTaskLoadImage(imageView);
            asyncTaskLoadImage.execute(mWish.getImage());
            openProduct.setText("Acheté");
            haveProduc.setText("Je les plus !");
            title.setText(mWish.getTitle());
            descrip.setText(mWish.getDescription());
            openProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mWish.getProductUrl();
                    if (!url.startsWith("http://") && !url.startsWith("https://")){
                        url = "http://"+url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);

                }
            });
            haveProduc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userService.updateWishWishButton(mWish.getImage(),false);

                }
            });
            return  convertView;
        }else {
            //set xml
            ImageView imageView = (ImageView) convertView.findViewById(R.id.wish_picture);
            TextView title = (TextView) convertView.findViewById(R.id.wish_title);
            TextView descrip = (TextView) convertView.findViewById(R.id.wish_description);
            Button openProduct = (Button) convertView.findViewById(R.id.go_product_buy_page);
            Button haveProduc = (Button) convertView.findViewById(R.id.product_is_buy);


            // set atribute
            openProduct.setText("Acheté");
            haveProduc.setText("Je les !");
            AsyncTaskLoadImage asyncTaskLoadImage = new AsyncTaskLoadImage(imageView);
            asyncTaskLoadImage.execute(mWish.getImage());
            title.setText(mWish.getTitle());
            descrip.setText(mWish.getDescription());
            openProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = mWish.getProductUrl();
                    if (!url.startsWith("http://") && !url.startsWith("https://")){
                        url = "http://"+url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(browserIntent);
                }
            });
            haveProduc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove(mWish);
                    notifyDataSetChanged();

                }
            });
            return  convertView;

        }
    }

    @Override
    public void remove(@Nullable WishModel object) {
        userService.updateWishWishButton(object.getImage(),true);
        super.remove(object);
    }

    @Override
    public void add(@Nullable WishModel object) {
        super.add(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

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

package adapteur;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.lucas.wishlist.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.WishModel;
import services.UserService;
import utils.Utils;

/**
 * Created by lucas on 15/12/2017.
 */

public class WishAdapteurNoHave extends ArrayAdapter<WishModel> {
    private int screenWidth, screenHeight;
    private  Context context;
    private ArrayList<WishModel> allWish;
    public WishAdapteurNoHave(@NonNull Context context, @NonNull List<WishModel> wishList, @NonNull ArrayList<WishModel> allWish) {
        super(context, 0, wishList);
        this.context = context;
        Utils.GetScreenSize getScreenSize = new Utils.GetScreenSize(context);
        screenHeight = (int) (getScreenSize.getScreenHeight() /3);
        screenWidth = (int)getScreenSize.getScreenWidht();
        this.allWish = allWish;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.wish_list_have_layout, parent, false);
        }
        final WishModel mWish = getItem(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.wish_picture_have);
            TextView title = (TextView) convertView.findViewById(R.id.wish_title_have);
            TextView descrip = (TextView) convertView.findViewById(R.id.wish_description_have);
            Button openProduct = (Button) convertView.findViewById(R.id.deelet_button_wish);
            Button haveProduc = (Button) convertView.findViewById(R.id.product_not_buy);



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
                    if (allWish.contains(mWish)){
                        int positionOriginal = allWish.indexOf(mWish);
                        mWish.setStatus(false);
                        //TODO: a voir si on peux évité la fonction static{@link UserService line}
                        UserService.updateWish(mWish, positionOriginal, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }

                }
            });
            return  convertView;


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

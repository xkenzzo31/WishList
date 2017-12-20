package fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lucas.wishlist.R;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;

import adapteur.WishAdapteur;
import core.SuccessCallback;
import model.WishModel;
import model.Wisher;
import services.UserService;
import utils.Utils;

import static android.app.Activity.RESULT_OK;
import static utils.Utils.dpToPixels;
import static utils.Utils.getBitmapFromVectorDrawable;
import static utils.Utils.resizeBitmap;

/**
 * Created by lucas on 29/11/2017.
 */

public class MyWishListFragment extends Fragment {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    UserService userService;
    private static final int RESULT_LOAD_IMAGE = 1;
    private Uri selectedImagePath;
    private ImageButton getImage;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://wichlist-d0196.appspot.com");
    private StorageReference storageRef = storage.getReference();
    private int width, height;
    public static MyWishListFragment newInstance() {
        return new MyWishListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_home_,container,false);
        userService = new UserService(getActivity());
        AddFloatingActionButton addWishButton = (AddFloatingActionButton) rootView.findViewById(R.id.add_wish_button);
        addWishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_add_wish);
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
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
                        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                });
                setWishInFirebase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        userService.updateWisherAsync(new SuccessCallback<Wisher>() {
                            @Override
                            public void onSuccess(Wisher wisher) {
                                Uri file = selectedImagePath;
                                StorageReference riversRef = storageRef.child("images/"+userService.getFirebaseUser().getUid()+"/"+wisher.getWishs().size()+"/"+file.getLastPathSegment());
                                UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        final WishModel wishModel = new WishModel(title.getText().toString(), downloadUrl.toString(),description.getText().toString(),urlWish.getText().toString());

                                        userService.addWish(wishModel, new OnSuccessListener<Void>() {

                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                userService.getWisher().getWishs().add(wishModel);
                                            }
                                        });
                                    }
                                });
                            }
                        });



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
        userService.updateAdapterWish(new UserService.WishListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //TODO update UI
                userService.updateWishAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {
                        ListView wishListView = getActivity().findViewById(R.id.list_wishs);
                        WishAdapteur wishAdapteur = new WishAdapteur( getActivity(), userService.getWishtHave(wisher.getWishs()));
                        wishListView.setAdapter(wishAdapteur);
                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                userService.updateWishAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {
                        ListView wishListView = getActivity().findViewById(R.id.list_wishs);
                        WishAdapteur wishAdapteur =
                                new WishAdapteur(getActivity(), userService.getWishtHave(wisher.getWishs()));
                        wishListView.setAdapter(wishAdapteur);
                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                userService.updateWishAsync(new SuccessCallback<Wisher>() {
                    @Override
                    public void onSuccess(Wisher wisher) {
                        ListView wishListView = getActivity().findViewById(R.id.list_wishs);
                        WishAdapteur wishAdapteur =
                                new WishAdapteur(getActivity(), userService.getWishtHave(wisher.getWishs()));
                        wishListView.setAdapter(wishAdapteur);
                    }
                });

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                if (data != null){
                    try {
                        Uri pickedImage = data.getData();

                        selectedImagePath = pickedImage;
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

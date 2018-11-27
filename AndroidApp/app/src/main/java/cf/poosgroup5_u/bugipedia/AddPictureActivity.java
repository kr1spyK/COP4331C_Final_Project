package cf.poosgroup5_u.bugipedia;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

import java.io.FileNotFoundException;
import java.io.InputStream;

import cf.poosgroup5_u.bugipedia.api.APICaller;
import cf.poosgroup5_u.bugipedia.api.BugImage;
import cf.poosgroup5_u.bugipedia.api.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPictureActivity extends AppCompatActivity {

    ImageView ivImage;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    private Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get access to the image view.
        ivImage = (ImageView) findViewById(R.id.ivImage);

        // get access to upload button
        uploadButton = findViewById(R.id.button_upload);


   //     FloatingActionButton fab = findViewById(R.id.fab);
   //       fab.setOnClickListener(new View.OnClickListener() {
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                SelectImage();
            }
        });
    }

    //Select from Menu.
    private void SelectImage() {
        //final ActionBar.DisplayOptions[] items = {"Camera", "Gallery", "Cancel"};
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddPictureActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }

            }
        });

        builder.show();
    }


    //This part is to handle the what the user chooses and process data form SelectImages() method above.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {  //result
        //CAMERA CAPTURE
            if (requestCode == REQUEST_CAMERA) { //request

                Bundle bundle = data.getExtras();

                final Bitmap bmp = (Bitmap) bundle.get("data");

                ivImage.setImageBitmap(bmp);

                uploadButton.setEnabled(true);

                uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
   //Call upload image to server method
                        uploadImageApiButton(bmp);
                    }
                });

         //GALLERY
            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                //declare stream to read image from SD card
                InputStream inputStream;
                //get input stream based on URI of an image
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                    // get a bitmap from the stream.
                    final Bitmap image = BitmapFactory.decodeStream(inputStream);
                    // show image to the user
                    ivImage.setImageBitmap(image);

                    uploadButton.setEnabled(true);

                    uploadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
 ////Call upload image to server method
                            uploadImageApiButton(image);
                        }
                    });


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //show message to user that image in unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }//end of elseif
        }//end of outer if.
    }

    private void uploadImageApiButton(Bitmap bmp) {
        //Button is disabled at first.
        uploadButton.setEnabled(false);

        //let user know we're uploading


        //upload api call
        //TODO: "1 was bugID"
        BugImage bugImage = new BugImage(1, bmp); //get the bugID from the ViewDB activity which will pass it to you in an Intent.

        APICaller.call().addImage(bugImage).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
               //Check if request is successful //call to server
                if(response.isSuccessful()){
                    // Check if image is uploaded //call to api
                    if(response.body().wasSuccessful()){
                        //let user know image was uploaded and close activity
                        Toast.makeText(AddPictureActivity.this, R.string.UploadSuccess, Toast.LENGTH_LONG).show();
                        //re-enable image button
                        uploadButton.setEnabled(true);

                        finish();
                    }else{
                        Log.e(AddPictureActivity.this.getLocalClassName(), response.message());
                        Toast.makeText(AddPictureActivity.this, R.string.UploadError, Toast.LENGTH_LONG).show();

                        //re-enable image button
                        uploadButton.setEnabled(true);
                    }
                }else{
                    //Check if it wasn't successfully uploaded
                    Log.e(AddPictureActivity.this.getLocalClassName(), response.message());
                    Toast.makeText(AddPictureActivity.this, R.string.UploadError, Toast.LENGTH_LONG).show();

                    //re-enable image button
                    uploadButton.setEnabled(true);
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                //Log the error and report it to the user that their upload failed

                Log.e(AddPictureActivity.this.getLocalClassName(), t.getMessage() ,t);
                Toast.makeText(AddPictureActivity.this, R.string.UploadError, Toast.LENGTH_LONG).show();
                //re-enable image button
                uploadButton.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
     public boolean onOptionsItemSelected (MenuItem item){

        int id = item.getItemId();

        if(id == R.id.action_camera) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}//end of class.


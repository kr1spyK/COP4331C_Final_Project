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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddPictureActivity extends AppCompatActivity {

    ImageView ivImage;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NEW
        // get access to the image view.
        ivImage = (ImageView) findViewById(R.id.ivImage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
                SelectImage();
            }
        });
    }

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
//THIS STUFF HAPPENS WHEN THE USER CLICKS ON STUFF FROM THE MENU.
                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("images/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }

            }
        });

        //builder.show();
    }


    //This part is to handle the what the user chooses and process the stuff form SelectImages() method above.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {  //RESULT
            //WAY1        //CAMERA CAPTURE
            if (requestCode == REQUEST_CAMERA) { //REQUEST

                Bundle bundle = data.getExtras();
                //import android.graphics.Bitmap;
                final Bitmap bmp = (Bitmap) bundle.get("data");
                //BOTH PREVIOUS LINES CAN BE COMBINED INTO ONE BELOW (COMMENTED)

                //  Bitmap bmp = (Bitmap) data.getExtras().get("data");

                ivImage.setImageBitmap(bmp);

                //WAY2        //GALLERY
            } else if (requestCode == SELECT_FILE) {

                Uri selectedImageUri = data.getData();
                //    ivImage.setImageURI(selectedImageUri);
                //26:32
                //Continue from Brandon Jones video for GALLERY STUFF ONLY.
                //declare stream to read image from SD card
                InputStream inputStream;
                //get input stream based on URI of an image
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    // show image to the user
                    //blah blah -see below-
                    ivImage.setImageBitmap(image);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //show message to user that image in unavailable.
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }//end of elseif
        }//end of outer if.
    }

//    @Override
//    //ALT
//    public boolean onCreateOptionMenu(Menu menu) {
//   //ALT public void onCreateOptionMenu(Menu menu) {
//        //Add items to the action bar if it is present
//     //MY FILE IS CALLED MENU.XML(SO USE "MENU")   getMenuInflater().inflate(R.menu.menu_add_image, menu);
//        getMenuInflater().inflate(R.menu.menu, menu);
//        //getMenuInflater().inflate(R.menu.menu_main, menu);
//    //ALT    return true;
//        return true;
//    }

//    @Override
//     public boolean onOptionsItemSelected (MenuItem item){
//    //public void onOptionsItemSelected(MenuItem item) {
//        //
//        int id = item.getItemId();
//
//        //
//     //   if(id == R.id.action_settings) {       //the action settings is something that should be PART OF MENU.XML :D
//        if(id == R.id.action_camera) {
//            //ALT      return true;
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    //ALT    return super.onOptionsItemSelected(item);
//    }

}//end of class.


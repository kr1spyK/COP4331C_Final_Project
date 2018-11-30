package cf.poosgroup5_u.bugipedia.api;

import android.graphics.Bitmap;
import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.ByteArrayOutputStream;

public class BugImage {

    /**
     * A value from 0-100 on how much to compress the JPEG images we send to the server
     */
    private static final int COMPRESSION_VALUE = 0;


    @SerializedName("id")
    @Expose(deserialize = false)
    private Integer bugID;

    @SerializedName("image")
    @Expose(deserialize = false)
    private String image;

    @SerializedName("url")
    @Expose(serialize = false)
    private String url;

    @SerializedName("imageId")
    @Expose
    private Integer imageID;



    public BugImage(int bugID, Bitmap image){

        this.bugID = bugID;
        this.image = convertImage(image);
    }

    /**
     * Internal Testing constructor. NOT TO BE USED FOR PRODUCTION.
     * @param bugID
     * @param base64Image
     */
    protected BugImage(int bugID, String base64Image){
        this.bugID = bugID;
        this.image = base64Image;
    }

    public Integer getBugID() {
        return bugID;
    }

    public Integer getImageID() {
        return imageID;
    }

    public String getUrl() {
        return url;
    }

    //converts an image from Bitmap to BASE64 Jpeg
    private String convertImage(Bitmap image) {
        if (image == null) return "";

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        if (image.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_VALUE, byteStream)){
            return Base64.encodeToString(byteStream.toByteArray(), Base64.DEFAULT);
        } else { //conversion wasnt possible?

            // ideally this error would be logged...
            return ""; //this should throw an API error.
        }
    }

    @Override
    public String toString() {
        String newline = "\n";
        return  "Bug ID: " + getBugID() + newline +
                "Image ID: " + getImageID() + newline +
                "URL: " + getUrl() + newline +
                "base64Image: " + image;


    }
}

package cf.poosgroup5_u.bugipedia.gallery;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import cf.poosgroup5_u.bugipedia.R;
import cf.poosgroup5_u.bugipedia.api.BugImage;

public class galleryPageAdapter extends PagerAdapter {
    private final Context context;
    private final List<BugImage> images;
    LayoutInflater inflater;
    private View.OnClickListener photoViewOnClickListener;

    public galleryPageAdapter(Context context, List<BugImage> images, View.OnClickListener photoViewOnClickListener) {

        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        this.photoViewOnClickListener = photoViewOnClickListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        View slideLayout = inflater.inflate(R.layout.gallery_slide_layout, container, false);

        final PhotoView photoView = (PhotoView) slideLayout
                .findViewById(R.id.galleryPhotoView);

        photoView.setOnClickListener(photoViewOnClickListener);

        //load the image
        Picasso.get().load(images.get(position).getUrl())
                .error(R.drawable.placeholder_bug_error)
                .placeholder(R.drawable.placeholder_bug)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(context instanceof Activity ? (((Activity) context).getLocalClassName()) : "Gallery Page Adapater", e.getMessage(), e);
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


        container.addView(slideLayout, position);

        return slideLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }



    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }
}

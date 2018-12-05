package cf.poosgroup5_u.bugipedia.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

import cf.poosgroup5_u.bugipedia.R;
import cf.poosgroup5_u.bugipedia.api.BugImage;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;

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
        AppUtils.loadImageIntoView(images.get(position).getUrl(), photoView, context);


        container.addView(slideLayout, Math.min(position, 2));

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

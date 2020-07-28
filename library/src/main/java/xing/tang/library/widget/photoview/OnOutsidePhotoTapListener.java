package xing.tang.library.widget.photoview;

import android.widget.ImageView;

/**
 * https://github.com/chrisbanes/PhotoView
 * Callback when the user tapped outside of the photo
 */
public interface OnOutsidePhotoTapListener {

    /**
     * The outside of the photo has been tapped
     */
    void onOutsidePhotoTap(ImageView imageView);
}

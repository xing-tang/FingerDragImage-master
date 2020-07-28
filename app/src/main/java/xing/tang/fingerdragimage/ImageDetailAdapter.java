package xing.tang.fingerdragimage;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xing.tang.library.widget.FingerDragLayout;
import xing.tang.library.widget.photoview.PhotoView;

/**
 * ImageDetailAdapter
 *
 * @Description:
 * @Author: xing.tang
 * @CreateDate: 2020/7/18 9:04 PM
 */
public class ImageDetailAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private OnPageFinishListener mOnPageFinishListener;
    private OnAlphaChangedListener mOnAlphaChangedListener;

    public ImageDetailAdapter(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        if (images == null) {
            return 0;
        }
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View convertView = View.inflate(container.getContext(), R.layout.item_image_detail_layout, null);
        final FingerDragLayout fingerDragLayout = convertView.findViewById(R.id.image_detail_finger);
        final PhotoView photoView = convertView.findViewById(R.id.image_detail_photo);

        fingerDragLayout.setOnAlphaChangeListener(new FingerDragLayout.OnAlphaChangedListener() {
            @Override
            public void onAlphaChanged(float alpha) {
                if (mOnAlphaChangedListener != null) {
                    mOnAlphaChangedListener.onAlphaChanged(alpha);
                }
            }
        });
        fingerDragLayout.setOnPageFinishListener(new FingerDragLayout.OnPageFinishListener() {
            @Override
            public void onPageFinish() {
                if (mOnPageFinishListener!=null){
                    mOnPageFinishListener.onPageFinish();
                }
            }
        });
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPageFinishListener != null) {
                    mOnPageFinishListener.onPageFinish();
                }
            }
        });
        String url = images.get(position);
        Glide.with(photoView.getContext()).load(url).into(photoView);
        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView((View) object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnAlphaChangeListener(OnAlphaChangedListener onAlphaChangedListener) {
        mOnAlphaChangedListener = onAlphaChangedListener;
    }

    public void setOnPageFinishListener(OnPageFinishListener onPageFinishListener) {
        mOnPageFinishListener = onPageFinishListener;
    }

    public interface OnAlphaChangedListener {
        void onAlphaChanged(float alpha);
    }

    public interface OnPageFinishListener {
        void onPageFinish();
    }
}

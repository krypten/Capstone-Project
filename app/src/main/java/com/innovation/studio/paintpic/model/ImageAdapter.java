package com.innovation.studio.paintpic.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.innovation.studio.paintpic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for loading images.
 *
 * @author Chaitanya Agrawal
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private static final String TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;
    private List<Image> mImageList;
    private LayoutInflater mInflater;

    public ImageAdapter(final Context context, final List<Image> imageList) {
        mContext = context;
        mImageList = imageList;
        if (mImageList == null) {
            mImageList = new ArrayList<>();
        }
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View layoutView = mInflater.inflate(R.layout.gridview_item_image, parent, false);
        final ViewHolder vh = new ViewHolder(layoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String url = mImageList.get(position).getUrl();
        Log.d(TAG, "image url : " + url);
        // Glide.with(mContext).load().into(holder.mImageView);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ViewHolder(final View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.gridview_item_image);
        }
    }
}

package com.innovation.studio.paintpic.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innovation.studio.paintpic.R;
import com.innovation.studio.paintpic.model.Image;
import com.innovation.studio.paintpic.model.ImageAdapter;

import java.util.ArrayList;

/**
 * Image Detail Fragment for displaying all the images in a grid.
 *
 * @author Chaitanya Agrawal
 */
public class ImageGridFragment extends Fragment {
    private static final String SCROLL_OFFSET = "SCROLL_OFFSET";

    private int mOffset;
    private ImageAdapter mImageAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mOffset = savedInstanceState.getInt(SCROLL_OFFSET, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.gridview_images);

        final ArrayList<Image> imageList = new ArrayList<>();
        // TODO(krypten) : Read images and add image url in the list
        mImageAdapter = new ImageAdapter(getActivity(), imageList);
        mRecyclerView.setAdapter(mImageAdapter);
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.smoothScrollBy(0, mOffset);
            }
        });

        final int columnCount = getResources().getInteger(R.integer.grid_column_count);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(SCROLL_OFFSET, mRecyclerView.computeVerticalScrollOffset());
    }
}

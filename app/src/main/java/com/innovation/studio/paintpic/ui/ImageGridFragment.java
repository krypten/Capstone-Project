package com.innovation.studio.paintpic.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innovation.studio.paintpic.R;
import com.innovation.studio.paintpic.model.Image;
import com.innovation.studio.paintpic.model.ImageAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Image Detail Fragment for displaying all the images in a grid.
 *
 * @author Chaitanya Agrawal
 */
public class ImageGridFragment extends Fragment {
    private static final String SCROLL_OFFSET = "SCROLL_OFFSET";
    private static final int REQUEST_READWRITE_STORAGE = 101;

    private int mOffset;
    private ArrayList<Image> mImageList;
    private ImageAdapter mImageAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mOffset = savedInstanceState.getInt(SCROLL_OFFSET, 0);
        }
        /*
        int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READWRITE_STORAGE);
        } else {
            mImageList = readImageDrawables(getActivity());
        }
        */
        mImageList = readImageDrawables(getActivity());
        if (mRecyclerView != null) {
            mImageAdapter = new ImageAdapter(getActivity(), mImageList);
            mRecyclerView.setAdapter(mImageAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READWRITE_STORAGE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                mImageList = readImageDrawables(getActivity());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.gridview_images);

        mImageAdapter = new ImageAdapter(getActivity(), mImageList);
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


    // File representing the folder that you select using a FileChooser
    static final File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg", "jpeg" // and other formats you need
    };

    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static ArrayList<Image> readImageList() {
        final ArrayList<Image> imageList = new ArrayList<>();
        if (dir != null && dir.isDirectory() && isExternalStorageReadable()) { // make sure it's a directory
            final File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (final File f : dir.listFiles(IMAGE_FILTER)) {
                    // imageList.add(new Image(f.getAbsolutePath()));
                }
            }
        }
        return imageList;
    }

    public static ArrayList<Image> readImageDrawables(final Context context) {
        final ArrayList<Image> imageList = new ArrayList<>();
        final String[] fileNames;
        try {
            fileNames = context.getAssets().list("");
            if (fileNames != null) {
                for (final String f : fileNames) {
                    imageList.add(new Image(Drawable.createFromStream(context.getAssets().open(f), null)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageList;
    }
}

package manic.com.bakingrecipes.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import manic.com.bakingrecipes.model.Step;

public class AsyncLoader extends AsyncTaskLoader<List<Bitmap>> {

    private Context mContext;
    private List<Step> stepList;
    private ImageView imgView;

    public AsyncLoader(Context mContext, List<Step> stepList, ImageView imgView){
        super(mContext);
        this.mContext = mContext;
        this.stepList = stepList;
        this.imgView = imgView;
    }


    @Nullable
    @Override
    public List<Bitmap> loadInBackground() {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < stepList.size(); i++) {
            try {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= 14)
                    mediaMetadataRetriever.setDataSource(stepList.get(i).getVideoURL(), new HashMap<String, String>());
                else
                    mediaMetadataRetriever.setDataSource(stepList.get(i).getVideoURL());
                //   mediaMetadataRetriever.setDataSource(videoPath);
                bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
                bitmaps.add(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            } finally {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            }
        }
        return bitmaps;
    }
}

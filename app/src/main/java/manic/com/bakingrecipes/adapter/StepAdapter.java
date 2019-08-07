package manic.com.bakingrecipes.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Step;
import manic.com.bakingrecipes.util.GV;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> implements LoaderManager.LoaderCallbacks<List<Bitmap>> {

    private Context mContext;
    private List<Step> stepList;

    private StepAdapterOnClickHandler mClickHandler;

    @NonNull
    @Override
    public Loader<List<Bitmap>> onCreateLoader(int i, @Nullable Bundle bundle) {
          return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Bitmap>> loader, List<Bitmap> bitmaps) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Bitmap>> loader) {

    }


    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }

    public StepAdapter(Context mContext, StepAdapterOnClickHandler mClickHandler){
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.step_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder viewHolder, int position) {
        viewHolder.stepDescriptionTv.setText(stepList.get(position).getShortDescription());
        String shortDescription = stepList.get(position).getShortDescription();

        if(GV.row_index == position){
            viewHolder.stepDescriptionTv.setBackgroundColor(mContext.getColor(R.color.primaryLightColor));
        }else
            viewHolder.stepDescriptionTv.setBackgroundColor(mContext.getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        if(stepList == null)
            return 0;
        else
            return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.step_description)
        TextView stepDescriptionTv;

        @BindView(R.id.step_iv)
        ImageView stepIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(stepList.get(adapterPosition));
            if(GV.twoPane)
                GV.row_index = adapterPosition;
            GV.currenStep = stepList.get(adapterPosition);
            notifyDataSetChanged();
        }
    }

    public void setStepList(List<Step> stepList){
        this.stepList = stepList;
        notifyDataSetChanged();
    }

    private Uri getImageUri(Bitmap img, String title){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), img, "img", title);
        return Uri.parse(path);
    }

}

package manic.com.bakingrecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Step;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context mContext;
    private List<Step> stepList;

    private StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler {
        void onClick(Step ingredient);
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

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        if(stepList == null)
            return 0;
        else
            return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(stepList.get(adapterPosition));
        }
    }

    public void setStepList(List<Step> stepList){
        this.stepList = stepList;
        notifyDataSetChanged();
    }
}

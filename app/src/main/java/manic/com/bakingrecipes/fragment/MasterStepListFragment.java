package manic.com.bakingrecipes.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.adapter.StepAdapter;
import manic.com.bakingrecipes.model.Step;
import manic.com.bakingrecipes.util.GV;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterStepListFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.ingredients_cv)
    CardView mIngredientsCv;
    @BindView(R.id.fragment_step_list_rv)
    RecyclerView mRecyclerView;

    private List<Step> stepList;
    private OnCardClickListener mCallback;
    private StepAdapter mAdapter;

    @Override
    public void onClick(Step step) {
        mCallback.onClick(step);
    }

    public interface OnCardClickListener {
        void onClick(Step step);
    }

    public MasterStepListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnCardClickListener) context;
        } catch(ClassCastException cce){
            throw new ClassCastException(context.toString() +
                    " must implement OnCardClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.master_step_list, container, false);
        ButterKnife.bind(this, rootView);
        int rowIndex = -1;
        if(savedInstanceState != null){
            rowIndex = savedInstanceState.getInt(getString(R.string.row_index));
        }

        mAdapter = new StepAdapter(getContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        stepList = getArguments().getParcelableArrayList(getString(R.string.step_key));

        assert mRecyclerView != null;

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setStepList(stepList);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}

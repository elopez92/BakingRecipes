package manic.com.bakingrecipes.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.adapter.StepAdapter;
import manic.com.bakingrecipes.model.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterStepListFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.fragment_step_list_rv) RecyclerView mRecyclerView;

    private List<Step> stepList;
    private OnCardClickListener mCallback;
    private StepAdapter mAdapter;

    @Override
    public void onClick(Step ingredient) {

    }

    public interface OnCardClickListener {
        void onClick(int position);
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
        View rootView = inflater.inflate(R.layout.fragment_master_step_list, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new StepAdapter(getContext(), this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        stepList = getArguments().getParcelableArrayList(getString(R.string.step_list));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter.setStepList(stepList);

        return rootView;
    }

}

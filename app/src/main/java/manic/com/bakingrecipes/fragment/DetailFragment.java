package manic.com.bakingrecipes.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";

    private Context mContext;
    private Step step;
    private String key;

    private SimpleExoPlayer mExoPlayer;

    private boolean playWhenReady = true;

    private PlayerView mPlayerView;
    @BindView(R.id.instructions_tv) TextView instructionTv;
    @BindView(R.id.error_message) TextView exoErrorMessage;

    private int mResumeWindow;
    private long mResumePosition = C.INDEX_UNSET;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        mContext = getContext();

        key = getString(R.string.step_key);

        Bundle arguements = getArguments();
        if(arguements.containsKey(key)) {
            step = arguements.getParcelable(key);
            instructionTv.setText(step.getDescription());
        }

        if(savedInstanceState != null){
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mPlayerView ==  null){
            mPlayerView = getView().findViewById(R.id.player_view);
            initializePlayer();
        }else
            resumePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            mResumeWindow = mPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayerView.getPlayer().getContentPosition());
        }
        releasePlayer();
    }

    private void resumePlayer(){
        boolean haveResumePosition = mResumePosition != C.INDEX_UNSET;

        if(haveResumePosition){
            mPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    mContext,
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }

        boolean haveResumePosition = mResumePosition != C.INDEX_UNSET;
        if(haveResumePosition){
            mPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        if (step.getVideoURL().isEmpty()) {
            exoErrorMessage.setText(getString(R.string.exo_no_video_available));
            mPlayerView.setCustomErrorMessage(getString(R.string.exo_no_video_available));
            mPlayerView.setPlayer(null);
            mExoPlayer = null;
        } else {
            exoErrorMessage.setText("");
            MediaSource mediaSource = buildMediaSource(Uri.parse(step.getVideoURL()));
            mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));

        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                .createMediaSource(uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void releasePlayer(){
        if(mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}

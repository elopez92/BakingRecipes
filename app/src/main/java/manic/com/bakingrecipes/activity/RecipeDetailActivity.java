package manic.com.bakingrecipes.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Recipe;
import manic.com.bakingrecipes.model.Step;

public class RecipeDetailActivity extends AppCompatActivity implements Player.EventListener {

    private static final String TAG = "LOG";

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private Step step;
    private Recipe recipe;
    private String stepKey;
    private String recipeKey;
    private int stepId = -1;

    private View mDecorView;

    private int mResumeWindow;
    private long mResumePosition;

    private boolean playWhenReady = true;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    @Nullable
    @BindView(R.id.instructions_tv) TextView instructionsTv;
    @Nullable
    @BindView(R.id.back_button) Button backButton;
    @Nullable
    @BindView(R.id.next_button) Button nextButton;
    @BindView(R.id.error_message) TextView exoErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mDecorView = getWindow().getDecorView();

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            hideSystemUI();
        else
            showSystemUI();

        stepKey = getString(R.string.step_key);
        recipeKey = getString(R.string.recipe_key);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra(stepKey)){
            step = intent.getParcelableExtra(stepKey);
        }

        if(intent.hasExtra(recipeKey)){
            recipe = intent.getParcelableExtra(recipeKey);
            setTitle(recipe.getName());
        }

        if(savedInstanceState != null){
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
        }

        setupButtons();
        updateUI();

        if(mPlayerView == null){
            mPlayerView = findViewById(R.id.player_view);
            initializePlayer();
        }else
            resumePlayer();
    }

    private void updateUI() {
        stepId = step.getId();
        if(backButton != null && nextButton != null && instructionsTv != null) {
            if (stepId == 0)
                hideBackButton();
            else
                showBackButton();

            if (stepId == recipe.getSteps().size() - 1)
                hideNextButton();
            else
                showNextButton();
            instructionsTv.setText(step.getDescription());
        }
    }

    private void setupButtons() {
        if (backButton != null && nextButton != null) {
            backButton.setOnClickListener((View v) -> {
                step = recipe.getSteps().get(stepId - 1);
                updateUI();
            });

            nextButton.setOnClickListener((View v) -> {
                step = recipe.getSteps().get(stepId + 1);
                updateUI();
            });
        }
    }

    private void hideBackButton(){
        backButton.setVisibility(View.GONE);
    }

    private void showBackButton(){
        backButton.setVisibility(View.VISIBLE);
    }

    private void hideNextButton(){
        nextButton.setVisibility(View.GONE);
    }

    private void showNextButton(){
        nextButton.setVisibility(View.VISIBLE);
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
                    this,
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }

        boolean haveResumePosition = mResumePosition != C.INDEX_UNSET;
        if(haveResumePosition){
            mPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        mExoPlayer.setPlayWhenReady(playWhenReady);

        if (step.getVideoURL().isEmpty()) {
            exoErrorMessage.setText(getString(R.string.exo_no_video_available));
            mPlayerView.setPlayer(null);
            mExoPlayer = null;
        }else {
            exoErrorMessage.setText("");
            MediaSource mediaSource = buildMediaSource(Uri.parse(step.getVideoURL()));
            mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        DefaultHttpDataSourceFactory dataSource = new DefaultHttpDataSourceFactory(userAgent);
        return new ExtractorMediaSource.Factory(dataSource)
                .createMediaSource(uri);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayerView != null && mPlayerView.getPlayer() != null) {
            mResumeWindow = mPlayerView.getPlayer().getCurrentWindowIndex();
            mResumePosition = Math.max(0, mPlayerView.getPlayer().getContentPosition());
        }
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void releasePlayer(){
        if(mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void showSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        exoErrorMessage.setText(getString(R.string.exo_no_video_available));
    }
}

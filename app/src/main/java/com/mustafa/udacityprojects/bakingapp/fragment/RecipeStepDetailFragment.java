package com.mustafa.udacityprojects.bakingapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.activity.RecipeStepListActivity;
import com.mustafa.udacityprojects.bakingapp.activity.RecipeStepDetailActivity;
import com.mustafa.udacityprojects.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Recipe Step detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    public static final String EXTRA_SELECTED_STEP = "EXTRA_SELECTED_STEP";

    @BindView(R.id.recipe_step_instruction)
    TextView mRecipeInstructionTextView;

    @BindView(R.id.playerview)
    PlayerView mPlayerView;

    @BindView(R.id.step_image)
    ImageView mStepImage;

    private Step mCurrentStep;
    private SimpleExoPlayer mExoPlayer;

    private StepNavigationListener mListener;
    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullScreen;

    /**
     * Create a new instance.
     *
     * @param step The step that will be shown.
     * @return instance of RecipeStepDetailFragment.
     */
    public static RecipeStepDetailFragment newInstance(Step step) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SELECTED_STEP, step);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXTRA_SELECTED_STEP)) {
            mCurrentStep = getArguments().getParcelable(EXTRA_SELECTED_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipestep_detail, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mCurrentStep != null) {
            mRecipeInstructionTextView.setText(mCurrentStep.getDescription());
        }

        if (mCurrentStep.getVideoUrl() != null && !mCurrentStep.getVideoUrl().isEmpty()) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(mCurrentStep.getVideoUrl());
        } else if ((mCurrentStep.getThumbnailUrl() != null &&
                !mCurrentStep.getThumbnailUrl().isEmpty())) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(mCurrentStep.getThumbnailUrl());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (StepNavigationListener) context;
    }

    private void initFullScreenDialog() {
        mFullScreenDialog = new Dialog(getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                if (mExoPlayerFullScreen) {
                    exitFullScreenDialog();
                }
                super.onBackPressed();
            }
        };
    }

    private void launchFullScreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullScreen = true;
        mFullScreenDialog.show();
    }

    private void exitFullScreenDialog() {

        if (getView() != null) {
            ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
            ((LinearLayout) getView().findViewById(R.id.fragment_recipe_step_detail_container)).addView(mPlayerView, 0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = 600;
            mPlayerView.setLayoutParams(params);
            mExoPlayerFullScreen = false;
            mFullScreenDialog.dismiss();
        }
    }

    private void initializePlayer(String mediaUrl) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                initFullScreenDialog();
                launchFullScreenDialog();
            }

            String userAgent = Util.getUserAgent(getContext(),
                    getContext().getApplicationInfo().name);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    userAgent, null);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaUrl));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);


        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @OnClick(R.id.navigate_next_step)
    public void nextStep() {
        mListener.onNextStep();
    }

    @OnClick(R.id.navigate_preivous_step)
    public void previousStep() {
        mListener.onPreviousStep();
    }

    /**
     * Interface for navigation events.
     */
    public interface StepNavigationListener {
        /**
         * Invoked when the next step button is clicked.
         */
        void onNextStep();

        /**
         * Invoked when the previous step button is clicked.
         */
        void onPreviousStep();
    }

}

package com.mustafa.udacityprojects.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
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
import com.squareup.picasso.Picasso;

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
    private SimpleExoPlayer mPlayer;

    private StepNavigationListener mListener;

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
            startExoPlayer(mCurrentStep.getVideoUrl());
        } else if ((mCurrentStep.getThumbnailUrl() != null &&
                !mCurrentStep.getThumbnailUrl().isEmpty())) {
            mPlayerView.setVisibility(View.VISIBLE);
            startExoPlayer(mCurrentStep.getThumbnailUrl());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mListener = (StepNavigationListener) context;
    }

    private void startExoPlayer(String videoUrl) {

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(
                bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        mPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        mPlayerView.setPlayer(mPlayer);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getActivity(),
                        getContext().getApplicationInfo().name), bandwidthMeter);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl));

        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPlayer != null) {
            mPlayer.release();
        }
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

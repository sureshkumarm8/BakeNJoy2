package com.suresh.bakenjoy2.Fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.suresh.bakenjoy2.DetailsActivity;
import com.suresh.bakenjoy2.R;
import com.suresh.bakenjoy2.Utils.Constants;
import com.suresh.bakenjoy2.datamodel.MainRecipes;
import com.suresh.bakenjoy2.datamodel.Step;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideoInstructionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class VideoInstructionsFragment extends Fragment {


    public SimpleExoPlayerView mPlayerView;
    public SimpleExoPlayer mExoPlayer;
    public TextView mDetailView;
    public Button mButton;
    private Bundle mBundle;
    private Step mStep;
    private MainRecipes mRecipe;
    private int mCurrentIndex;
    private long mResumePosition;
    private ImageView mImageView;
    private ScrollView mScrollView;

    private OnFragmentInteractionListener mListener;
    private Button mButtonP;
    private Uri videoUri;
    private boolean playWhenReady;

    public VideoInstructionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mResumePosition = savedInstanceState.getLong(Constants.CURRENT_POSITION);
            mBundle = savedInstanceState.getBundle(Constants.STEP_BUNDLE);
            playWhenReady = savedInstanceState.getBoolean(Constants.KEY_PLAY_WHEN_READY);
            if(mBundle != null) {
                mStep = mBundle.getParcelable(Constants.STEP);
                mRecipe = mBundle.getParcelable(Constants.RECIPE);
            }
        } else {
            mBundle = getArguments();
            if(mBundle != null) {
                mStep = mBundle.getParcelable(Constants.STEP);
                mRecipe = mBundle.getParcelable(Constants.RECIPE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_instructions, container, false);

        mPlayerView = rootView.findViewById(R.id.exoplayer_view);
        mDetailView = rootView.findViewById(R.id.step_detail_text_view);
        mDetailView.setText(mStep.getDescription());
        mScrollView = rootView.findViewById(R.id.detail_scroll_view);

        mImageView = rootView.findViewById(R.id.default_image_view);
        setUpNextButton(rootView);
        setUpPrevButton(rootView);

        if(!mStep.getVideoURL().isEmpty()){
            mPlayerView.setVisibility(View.VISIBLE);
            videoUri = Uri.parse(mStep.getVideoURL());
//            initPlayer(Uri.parse(mStep.getVideoURL()));
        }else {

            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(Constants.DEFAULT_IMAGE_URL)
                    .placeholder(R.drawable.baking_image)
                    .into(mImageView);
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setUpNextButton(View rootView) {

        mButton = rootView.findViewById(R.id.next_button);

        //check screen dp. if tab size next button is redundant so hide it else show it.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDP = displayMetrics.widthPixels / (getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        if(widthDP > 600){
            mButton.setVisibility(View.GONE);
        }else {
            mButton.setVisibility(View.VISIBLE);
            mCurrentIndex = mStep.getId();
            if(mRecipe != null){
                if(mCurrentIndex == mRecipe.getSteps().size() - 1){
                    mButton.setVisibility(View.GONE);
                }

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mStep != null){
                            if(mCurrentIndex < mRecipe.getSteps().size() - 1){

                                Step nextStep = mRecipe.getSteps().get(mCurrentIndex + 1);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable(Constants.STEP, nextStep);
                                bundle.putParcelable(Constants.RECIPE, mRecipe);

                                VideoInstructionsFragment videosInstructionFragment = new VideoInstructionsFragment();
                                videosInstructionFragment.setArguments(bundle);

                                android.support.v4.app.FragmentManager fragmentManager = ((DetailsActivity)getContext()).getSupportFragmentManager();
                                if(fragmentManager != null) {
                                    releasePlayer();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.details_frame_layout, videosInstructionFragment)
                                            .addToBackStack(Constants.STEP_FRAGMENT)
                                            .commit();
                                }
                            }
                        }
                    }
                });
            }
        }

    }

    private void setUpPrevButton(View rootView) {

        mButtonP = rootView.findViewById(R.id.previous_button);

        //check screen dp. if tab size next button is redundant so hide it else show it.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDP = displayMetrics.widthPixels / (getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        if(widthDP > 600){
            mButtonP.setVisibility(View.GONE);
        }else {
            mButtonP.setVisibility(View.VISIBLE);
            mCurrentIndex = mStep.getId();
            if(mRecipe != null){
                if(mCurrentIndex == 0){
                    mButtonP.setVisibility(View.GONE);
                }

                mButtonP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mStep != null){
                            if(mCurrentIndex > 0){

                                Step prevStep = mRecipe.getSteps().get(mCurrentIndex - 1);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable(Constants.STEP, prevStep);
                                bundle.putParcelable(Constants.RECIPE, mRecipe);

                                VideoInstructionsFragment videosInstructionFragment = new VideoInstructionsFragment();
                                videosInstructionFragment.setArguments(bundle);

                                android.support.v4.app.FragmentManager fragmentManager = ((DetailsActivity)getContext()).getSupportFragmentManager();
                                if(fragmentManager != null) {
                                    releasePlayer();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.details_frame_layout, videosInstructionFragment)
                                            .addToBackStack(Constants.STEP_FRAGMENT)
                                            .commit();
                                }
                            }
                        }
                    }
                });
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initPlayer(videoUri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            //resuming properly
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(mResumePosition);
        } else {
            //Correctly initialize and play properly fromm seekTo function
//            initializeMedia();
            initPlayer(videoUri);
            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(mResumePosition);
        }
    }
    private void initPlayer(Uri uri){
        if(mExoPlayer == null){
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(params);
            mScrollView.setVisibility(View.GONE);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);
            ((DetailsActivity) getActivity()).getSupportActionBar().hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            params.width= ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
            params.height= ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
            mPlayerView.setLayoutParams(params);
            mScrollView.setVisibility(View.VISIBLE);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            ((DetailsActivity) getActivity()).getSupportActionBar().show();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mBundle != null){
            outState.putBundle(Constants.STEP_BUNDLE , mBundle);
            outState.putLong(Constants.CURRENT_POSITION, mResumePosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return true;
    }
}

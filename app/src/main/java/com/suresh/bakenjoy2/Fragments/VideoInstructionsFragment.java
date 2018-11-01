package com.suresh.bakenjoy2.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
 * Use the {@link VideoInstructionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoInstructionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    public VideoInstructionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoInstructionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoInstructionsFragment newInstance(String param1, String param2) {
        VideoInstructionsFragment fragment = new VideoInstructionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
            initPlayer(Uri.parse(mStep.getVideoURL()));
        }else {
           /*
            The below method does not want to work and I have no clue why, I cant find if it was deprecated or not but
            the method setDefaultArtwork does not exist, so I am using an ImageView for when there is no video.
             */
//            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.mipmap.baking_image));

            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(Constants.DEFAULT_IMAGE_URL)
                    .placeholder(R.drawable.baking_image)
                    .into(mImageView);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}

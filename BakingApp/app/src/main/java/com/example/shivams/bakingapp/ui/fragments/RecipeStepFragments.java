package com.example.shivams.bakingapp.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivams.bakingapp.R;
import com.example.shivams.bakingapp.model.internetpojo.Steps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepFragments extends Fragment implements ExoPlayer.EventListener  {
    private SimpleExoPlayerView exoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepsDescription;
    private String mStepsDescriptionText;
    private String mVideoUri;
    private Button btnNextSteps;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder playBackStateBuilder;
    private List<Steps> mStepsList;
    private int listPosition;
    private boolean disableButton=false;
    private ImageView mImageThumbnail;
    ButtonClick mButtonClick;
    BackToActivity backToActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        backToActivity=(BackToActivity)activity;
    }

    public interface BackToActivity{
        void miliSec(long mili);
    }
    public void setMediaPlayerPosition(long mediaPlayerPosition) {
        this.mediaPlayerPosition = mediaPlayerPosition;
    }

    private long mediaPlayerPosition;
    private boolean playWhenReady;
    private int startWindow;

    public void setmThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    private String mThumbnailUrl;

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public interface ButtonClick{
        void toggleButton(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mButtonClick = (ButtonClick)context;
        }catch (ClassCastException c){
            c.printStackTrace();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            mStepsList = savedInstanceState.getParcelableArrayList("steps");
          //  mediaPlayerPosition = savedInstanceState.getLong("mediaPosition");
            startWindow= savedInstanceState.getInt("start");
            playWhenReady=savedInstanceState.getBoolean("play");
            listPosition = savedInstanceState.getInt("position");
            mVideoUri=savedInstanceState.getString("video");
            mStepsDescriptionText=savedInstanceState.getString("des");
            Log.v("CREATE",mediaPlayerPosition+" "+" "+startWindow);
         //   listPosition = savedInstanceState.getInt("position");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =null;
        rootView=inflater.inflate(R.layout.recipe_steps_fragments, null);
            exoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.frame_media_player);
            Log.v("CREATEVIEW",mediaPlayerPosition+"");
            if(savedInstanceState!=null){
             //   mediaPlayerPosition = savedInstanceState.getLong("mediaPosition");

            }
            mStepsDescription = (TextView) rootView.findViewById(R.id.frame_steps);
            mStepsDescription.setText(mStepsDescriptionText);
            btnNextSteps = (Button) rootView.findViewById(R.id.btn_next_step);
            mImageThumbnail = (ImageView)rootView.findViewById(R.id.imgv_thumbnail_url);
            setImageToView();
            if(disableButton){
                btnNextSteps.setVisibility(View.GONE);
            }
            else {
                btnNextSteps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //     Toast.makeText(getContext(), mStepsList.size()+"", Toast.LENGTH_SHORT).show();
                        if (listPosition < (mStepsList.size() - 1)) {
                            listPosition++;
                            //    Toast.makeText(getContext(), listPosition+"", Toast.LENGTH_SHORT).show();

                            mButtonClick.toggleButton(listPosition);
                        } else if (listPosition == mStepsList.size() - 1) {
                            listPosition = 0;
                            mButtonClick.toggleButton(listPosition);
                        }
                    }
                });
            }
            initializeMediaSession();
            intializePlayer(Uri.parse(mVideoUri));
            return rootView;

    }

    public void setmStepsDescriptionText(String mStepsDescriptionText) {
        this.mStepsDescriptionText = mStepsDescriptionText;
    }

    public void setmVideoUri(String mVideoUri) {
        this.mVideoUri = mVideoUri;
    }

    private void initializeMediaSession(){
        mMediaSession = new MediaSessionCompat(getContext(),"bakingapp");
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        playBackStateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        mMediaSession.setPlaybackState(playBackStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
    }

    public void intializePlayer(Uri mediaUri){
        if(mExoPlayer == null){

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector,loadControl);
            exoPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(),"BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
            mExoPlayer.seekTo(mediaPlayerPosition);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
        else {
            String userAgent = Util.getUserAgent(getContext(),"BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
            Log.v("MEDIAPOS",mediaPlayerPosition+"");
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mediaPlayerPosition);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            playBackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,mExoPlayer.getCurrentPosition(),1f);
        }
        else if(playbackState == ExoPlayer.STATE_READY){
            playBackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,mExoPlayer.getCurrentPosition(),1f);
        }

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    public void setmStepsList(List<Steps> mStepsList) {
        this.mStepsList = mStepsList;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }


    public class MySessionCallback extends  MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }
    }

    public void releasePlayer(){
        if(mExoPlayer !=null) {
            mediaPlayerPosition= mExoPlayer.getCurrentPosition();
           playWhenReady= mExoPlayer.getPlayWhenReady();
           startWindow = mExoPlayer.getCurrentWindowIndex();
           backToActivity.miliSec(mediaPlayerPosition);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            Log.v("RELEASE",mediaPlayerPosition+" "+" "+startWindow);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("steps",(ArrayList)mStepsList);
        outState.putInt("position",listPosition);
      //  outState.putLong("mediaPosition",mediaPlayerPosition);
        outState.putInt("start",startWindow);
        outState.putBoolean("play",playWhenReady);
        outState.putString("video",mVideoUri);
        outState.putString("des",mStepsDescriptionText);
        Log.v("SAVED",mediaPlayerPosition+" "+" "+startWindow);

    }


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

        private void setImageToView(){
            if(mThumbnailUrl==null||mThumbnailUrl .equals("")){
                mImageThumbnail.setImageResource(R.drawable.noimage);
            }
            else{
                Picasso.with(getContext()).load(mThumbnailUrl).into(mImageThumbnail);
            }
        }


    @Override
    public void onResume() {
        super.onResume();
        intializePlayer(Uri.parse(mVideoUri));
    }
}

package com.videocall.datingapp.sinchex.videocalling;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;
import com.sinch.android.rtc.video.VideoController;
import com.videocall.datingapp.R;
import com.videocall.datingapp.sinchex.SinchBaseActivity;
import com.videocall.datingapp.sinchex.SinchServiceNew;

import java.util.List;

public class IncomingCallScreenActivity extends SinchBaseActivity {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
   // private AudioPlayer mAudioPlayer;
    private boolean mAcceptVideo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.incoming);

        Button answer = (Button) findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = (Button) findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);

     //   mAudioPlayer = new AudioPlayer(this);
      //  mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchServiceNew.CALL_ID);
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            remoteUser.setText(call.getRemoteUserId());

        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
       // mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallScreenActivity.class);
            intent.putExtra(SinchServiceNew.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
     //   mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
         //   mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // No need to implement for managed push
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
            // and pass it to the CallScreenActivity via Intent and mAcceptVideo
            VideoController vc = getSinchServiceInterface().getVideoController();
            View myPreview = vc.getLocalView();
            View remoteView = vc.getRemoteView();

            mAcceptVideo = true;
        }

        @Override
        public void onVideoTrackResumed(Call call) {

        }

        @Override
        public void onVideoTrackPaused(Call call) {

        }
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };
}

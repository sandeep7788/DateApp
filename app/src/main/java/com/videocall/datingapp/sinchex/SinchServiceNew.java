package com.videocall.datingapp.sinchex;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;
import com.videocall.datingapp.Main.Application;
import com.videocall.datingapp.sinchex.videocalling.IncomingCallScreenActivity;

public class SinchServiceNew extends Service {

    /*private static final String APP_KEY = "7b21eec0-3831-4079-ba84-956da5e7b780";
    private static final String APP_SECRET = "drNcA+IRnkGFn+VqEJfHPg==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";*/

/*
    private static final String APP_KEY = "0c65c2c7-0265-47e0-8c3f-04b9587f65ae";
    private static final String APP_SECRET = "raYJk7Ywr0q3jj1u+eZHBQ==";
    private static final String ENVIRONMENT = "clientapi.sinch.com";
*/


    public static final String CALL_ID = "CALL_ID";
    static final String TAG = SinchServiceNew.class.getSimpleName();

    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private SinchClient mSinchClient;
    private String mUserId;

    private StartFailedListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();
        start();
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

   /* private void start(String userName) {
        if (mSinchClient == null) {
            mUserId = userName;
            mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                    .applicationKey(APP_KEY)
                    .applicationSecret(APP_SECRET)
                    .environmentHost(ENVIRONMENT).build();

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();
            mSinchClient.setSupportManagedPush(true);

            mSinchClient.addSinchClientListener(new SinchServiceNew.MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchServiceNew.SinchCallClientListener());
            mSinchClient.start();
        }else {
            Log.e("sinch service client",""+mSinchClient.getClass().getSimpleName());
        }
    }
*/

    private void start() {
        if(FirebaseAuth.getInstance().getCurrentUser()==null) return;
        mUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (mSinchClient == null) {
           // mUserId = PatientApplication.getCurrentUserId();
            mSinchClient = Application.getSinchClient(mUserId);
            if (!mSinchClient.isStarted())
                mSinchClient.start();
            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        }else {
            Log.e("sinch service client",""+mSinchClient.getClass().getSimpleName());
        }
    }


    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }



    public class SinchServiceInterface extends Binder {

        public Call callUserVideo(String userId) {
            return mSinchClient.getCallClient().callUserVideo(userId);
        }

        public Call callUser(String userId) {
            if (mSinchClient == null) {
                return null;
            }
            return mSinchClient.getCallClient().callUser(userId);
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchServiceNew.this.isStarted();
        }

        public void startClient() {
            start();
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            Log.e("call id : ",""+callId);
            return mSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getAudioController();
        }
    }

    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Log.d(TAG, "Incoming call");
            Intent intent = new Intent(SinchServiceNew.this, IncomingCallScreenActivity.class);
            intent.putExtra(CALL_ID, call.getCallId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            SinchServiceNew.this.startActivity(intent);

        }
    }

}
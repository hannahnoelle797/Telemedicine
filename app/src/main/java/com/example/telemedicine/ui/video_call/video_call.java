package com.example.telemedicine.ui.video_call;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.telemedicine.MainActivity;
import com.example.telemedicine.R;
import com.example.telemedicine.models.Doctor;
import com.example.telemedicine.ui.video_call.Agora_Tokens.media.RtcTokenBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * @author - David Howard
 * Boilerplate from - https://github.com/AgoraIO/Basic-Video-Call.git
 */
public class video_call extends AppCompatActivity {

    private static final int PERMISSION_REQ_ID = 22;

    // Ask for Android device permissions at runtime.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean mMuted;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;

    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;

    private FirebaseUser mUser;
    // Used to get firebase current auth token
    // private Task<GetTokenResult> mToken;

    private String agora_token;
    private String doctorId;
    private String doctorString;
    private String channelName;
    // Agora token expire time
    static int expirationTimeInSeconds = 3600;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
        }

        @Override
        public void onRemoteVideoStateChanged(final int uid, int state, int reason, int elapsed) {

            if (state == Constants.REMOTE_VIDEO_STATE_STARTING) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRemoteVideo(uid);
                    }
                });
            }
        }

        /**
         * Called once the user is offline
         * @param uid - The Agora uid (Auto Generated)
         * @param reason - The int reason why the user is offline
         */
        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("User offline");
                    onRemoteUserLeft();
                }
            });
        }

        /**
         * Once the token expires this method is called and we need to create a new one
         */
        @Override
        public void onRequestToken() {
            Toast.makeText(getApplicationContext(), "Your token has expired", Toast.LENGTH_LONG).show();
            Log.i("Video_Call_Tele", "The token as expired..");
            // mRtcEngine.renewToken(token);
            // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af1428905e5778a9ca209f64592b5bf80
            // Renew token - TODO
        }
    };

    /**
     * Called to setup the remote video for the view
     * @param uid - The agora uid (Auto generated)
     */
    private void setupRemoteVideo(int uid) {
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++){
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }

        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    /**
     * Called when the remote user leaves the room
     */
    private void onRemoteUserLeft() {
        removeRemoteVideo();
    }

    /**
     * Called when the remote video should be deleted
     */
    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        mRemoteView = null;
    }

    /**
     * Called once the activity is created (start)
     * @param savedInstanceState - The saved instance provided by the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Bundle extras = getIntent().getExtras();
        isDoc(new IsDocCallback() {
            @Override
            public void onCallback(boolean value) {
                if (value) {
                    channelName = (Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).substring(Math.max(0, mAuth.getCurrentUser().getUid().length() - 10));
                    // Toast.makeText(getBaseContext(), "This is a doctor: " + channelName, Toast.LENGTH_LONG).show();
                    if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                            checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                        initEngineAndJoinChannel();
                    }
                } else {
                    if (extras == null) {
                        System.out.println("Empty extras..");
                        initializeEngine();
                        startActivity(new Intent(video_call.this, doctor_select.class));
                        finish();
                    } else {
                        doctorString = extras.getString("docString");
                        setDocId(doctorString, new GetDocId() {
                            @Override
                            public void onCallback(String value) {
                                doctorId = value;
                                channelName = (doctorId.substring(Math.max(0, doctorId.length() - 10)));
                                // Toast.makeText(getBaseContext(), "This is a patient: " + channelName, Toast.LENGTH_LONG).show();
                                if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                                    initEngineAndJoinChannel();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * Called to init the UI for Firebase
     */
    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);

        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);
    }

    /**
     *
     * @param permission - The current permission to be checked
     * @param requestCode - The agora request code used to check for permissions
     * @return - Returns boolean that the permissions were accepted or not
     */
    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    /**
     *
     * @param requestCode - Given agora request code
     * @param permissions - All active permissions needed for voice calling
     * @param grantResults - The permissions that were granted by the user (run-time event)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel();
        }
    }

    /**
     * Called to display a long toast
     * @param msg - The display message for the toast
     */
    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called to throw everything together (Ingine, localConfig/Video) and join the channel
     */
    private void initEngineAndJoinChannel() {
        // Join and start call
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    /**
     * Create our agora rtc engine instance
     */
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e("VIDEO.CALL", Log.getStackTraceString(e));
            throw new RuntimeException("NEED to check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    /**
     * Sets up the local video config
     */
    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    /**
     * Sets up the local video attributes
     */
    private void setupLocalVideo() {

        // Enable the video module.
        mRtcEngine.enableVideo();

        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);

        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    /**
     * Called when a user joins the voice channel
     * We need to have a valid token for firebase and agora
     */
    private void joinChannel() {
        // Get the User Id for the current user
        final String userId = mUser.getUid();
        // User Id set to 0 for auto handling by Agora
        int uid = 0;
        // Token object
        RtcTokenBuilder token = new RtcTokenBuilder();
        // Time stamp used for length of token
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);

        String uId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        addCallToDb(uId); // Add current user to db for active calls

        try {
            // Create a token using Agora Sdk
            agora_token = token.buildTokenWithUid(getString(R.string.agora_app_id), getString(R.string.agora_app_certificate),
                    channelName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Join the channel with the given token and channel name
        mRtcEngine.joinChannel(agora_token, channelName, "", uid);
    }

    /**
     * Calls the method once the activity is destroyed.
     * We should destroy our Agora engine instance
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // IF still in a call
        if (!mCallEnd) {
            leaveChannel();
        }
        // Calling static method that destroys the RtcEngine instance
        RtcEngine.destroy();
    }

    /**
     * Leaves the current channel conn.
     */
    private void leaveChannel() {
        // Leave the current channel
        mRtcEngine.leaveChannel();
    }

    /**
     * When the local audio is muted, we should adjust the buttons and audio stream
     * @param view - The current view of the video call
     */
    public void onLocalAudioMuteClicked(View view) {
        // Change the value of muted
        mMuted = !mMuted;
        // Update the agora engine with the mute
        mRtcEngine.muteLocalAudioStream(mMuted);
        // Get the correct mute button
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        // Apply the correct img
        mMuteBtn.setImageResource(res);
    }

    /**
     * Called to switch the camera from front/back
     * @param view - The current view of the video call
     */
    public void onSwitchCameraClicked(View view) {
        // Switch the camera
        mRtcEngine.switchCamera();
    }

    /**
     * When the connect button is selected this method sets/hides the button images
     * @param view - The current view of the video call
     */
    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
           mCallBtn.setImageResource(R.drawable.btn_startcall);
        }
        showButtons(!mCallEnd);
    }

    /**
     * Called to start the call but only if a firebase auth token exists
     */
    private void startCall() {
        // If firebase isn't auth don't call
        if (mUser == null) return;
        setupLocalVideo();
        joinChannel();
    }

    /**
     * Called to 'hang up' the connection and remove all assets required
     */
    private void endCall() {
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
        removeCallFromDb(mUser.getUid());
        // go back to home screen after selected end call
        startActivity(new Intent(this, MainActivity.class));
        // Prevent back button back into the call
        finish();
    }

    /**
     * Called to remove the local video from the call conn.
     */
    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    /**
     * Called to either show/hide buttons
     * @param show - The boolean status of the call connection
     */
    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
    }

    /**
     * Called to get the correct doctor id from the db
     * @param docName - The selected doctor (String)
     * */

    protected void setDocId(final String docName, final GetDocId mCallBack) {
        // Populate the layout with db doctors
        DatabaseReference mDocDatabase = FirebaseDatabase.getInstance().getReference("Doctor");
        mDocDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Doctor docs = child.getValue(Doctor.class);
                    assert docs != null;
                    if (docName.equals(docs.getDocString())) {
                        doctorId = docs.getDocID();
                    }
                }
                mCallBack.onCallback(doctorId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    protected void addCallToDb(String userId) {
        mDatabase.child("Active Calls").child(userId).setValue("In call");
    }

    protected void removeCallFromDb(String userId) {
        mDatabase.child("Active Calls").child(userId).removeValue();
    }

    protected void isDoc(final IsDocCallback mCallback) {
        final boolean[] isDocValue = new boolean[1];
        mDatabase.child("Doctor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Doctor doc = child.getValue(Doctor.class);
                    assert doc != null;
                    if (mUser.getUid().equals(doc.getDocID())) {
                        isDocValue[0] = true;
                    }
                }
                mCallback.onCallback(isDocValue[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
}
package com.example.janusclient;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dnion.CallDebugInfo;
import com.dnion.P2PSDK;
import com.dnion.P2PSDKSignaling;
import com.dnion.RenderProxy;
import com.dnion.VideoConfigProxy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lg on 23/10/2017.
 */

public class RoomActivity extends Activity {
    public static final String TAG="RoomActivity";
    RoomView _room;
    ControlPanelView panelView;

    public RoomActivity(){

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //if (newConfig.orientation == Configuration.)
        _room.relayoutSubviews();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewGroup rootView = new RelativeLayout(this);
        ViewGroup.LayoutParams rootLayout = new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        _room = new RoomView(this);
        ViewGroup.LayoutParams roomLayout = new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

//        setContentView(_room, rootLayout);
        setContentView(rootView, rootLayout);
        rootView.addView(_room, roomLayout);


        Button switchBtn = new Button(this);
        switchBtn.setText(P2PSDK.getInstance().getVideoConfigProxy().useBackCamera() ? "B" : "F");
        RelativeLayout.LayoutParams switchLayout=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        switchLayout.addRule(RelativeLayout.ALIGN_RIGHT, -20);
        switchLayout.addRule(RelativeLayout.ALIGN_TOP, 40);
        rootView.addView(switchBtn, switchLayout);
        switchBtn.setOnClickListener((View )->{
            P2PSDK.getInstance().stopChat(1);
        });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        _room.setToogleAudioMuteListener(new RoomViewInterface.ToogleButtonListener() {
            @Override
            public void onClicked(long identity) {

            }
        });

        _room.setToogleVideoMuteListener(new RoomViewInterface.ToogleButtonListener() {
            @Override
            public void onClicked(long identity) {

            }
        });

        _room.setTooglePublishListener(new RoomViewInterface.ToogleButtonListener() {
            @Override
            public void onClicked(long identity) {
                // JanusGateway.getInstance().startSubscribe(identity, true,true);
            }
        });

        _room.setToogleAspectListener((long identity)-> {
            ControlPanelView thisPanel = _room.getLocalDisplayView(identity, this);
            RenderProxy.AspectMode mode = thisPanel.renderProxy().aspectMode();
            RenderProxy.AspectMode newMode = mode == RenderProxy.AspectMode.aspectFit ? RenderProxy.AspectMode.aspectFill :
                    mode == RenderProxy.AspectMode.aspectFill ? RenderProxy.AspectMode.aspectFull
                            : RenderProxy.AspectMode.aspectFit;
            thisPanel.renderProxy().setAspectMode(newMode);
        });
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        long userId = P2PSDK.getInstance().getMyself().sessionId;
        ControlPanelView panel = _room.getLocalDisplayView(userId, this);
        P2PSDK.getInstance().startPreview();
        P2PSDK.getInstance().setLocalDisplay(panel.renderProxy().getDisplay());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onEvent(JanusMessages.OnChatStop event) {
        Log.i(TAG, "OnChatStop ");
        P2PSDK.getInstance().stopPreview();
        _room.defetchAllDisplayView();
        this.finish();
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onEvent(JanusMessages.onGotUserVideo event) {
        Log.i(TAG, "onGotUserVideo ");
        ControlPanelView panel = _room.getRemoteDisplay(0, this);
        P2PSDK.getInstance().setRemoteDisplay(panel.renderProxy().getDisplay(), 0);
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onEvent(JanusMessages.onLostUserVideo event) {
        Log.i(TAG, "onLostUserVideo ");
        _room.defetchDisplayView(0);
    }
}


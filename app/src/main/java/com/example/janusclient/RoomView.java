package com.example.janusclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.util.Log;

import org.webrtc.Logging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lg on 23/10/2017.
 */

public class RoomView extends PercentRelativeLayout implements RoomViewInterface{
    HashMap<Long, ControlPanelView> _viewDict;
    Context _context;
    ToogleButtonListener _publishLister;
    ToogleButtonListener _muteAudioListerer;
    ToogleButtonListener _muteVideoListerer;
    ToogleButtonListener _aspectListerer;
    final int _COLUMNCOUNT = 2;
    final int _ROWCOUNT = 2;
    private static final String TAG="RoomView";
    public RoomView(Context context) {
        super(context);
        init();
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.BLUE);
        _viewDict = new HashMap<Long, ControlPanelView>() ;
    }

    private ControlPanelView getDisplayView(long identity) {
        return _viewDict.get(identity);
    }

    public void relayoutSubviews()  {
        Activity host = (Activity) getContext();
        MyApplication application = (MyApplication)host.getApplication();
        int orientation = application.getScreenOrientation();
        boolean wideOrientation = application.isScreenLandscape();

        ArrayList<ControlPanelView> views = new ArrayList();
        views.addAll(_viewDict.values());

        int columeCount = 0;
        int rowCount = 0;
        if (views.size() == 1) {
            columeCount = rowCount = 1;
        } else if (views.size() == 2) {
            columeCount = wideOrientation ?  2 : 1 ;
            rowCount = wideOrientation ? 1 : 2;
        } else if (views.size() <= 4) {
            columeCount = 2;
            rowCount = 2;
        }
        else  if(views.size() <= 9) {
            rowCount = columeCount = 3;
        }

        Log.i(TAG, "------------ view w:" + getWidth() + ",view h:" + getHeight() + ", is in wide orientation:" + wideOrientation + ",orientation:" + orientation);
        for (int i=0; i<views.size(); ++i) {
            PercentRelativeLayout.LayoutParams innerParams = (PercentRelativeLayout.LayoutParams)views.get(i).getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info = innerParams.getPercentLayoutInfo();
            Log.i(TAG, "old Margins : " + "x" + info.leftMarginPercent + " y " + info.topMarginPercent + " h:" + info.heightPercent
                    + " w:" + info.widthPercent);
            info.heightPercent = 1.0f / rowCount;
            info.widthPercent = 1.0f / columeCount;
            info.leftMarginPercent = (float) (i % columeCount) / columeCount;
            info.topMarginPercent = (float) (i / columeCount) / rowCount;
            Log.i(TAG, "--------------- Margins: " + "x:" + info.leftMarginPercent + " y: " + info.topMarginPercent + " h:" + info.heightPercent
                    + " w:" + info.widthPercent);
            views.get(i).requestLayout();
        }
    }

    private ControlPanelView fectchDisplayView(long identity, Context context, boolean isFront) {
        ControlPanelView view = getDisplayView(identity);
        if(view != null) return view;
        ControlPanelView panel = new ControlPanelView(context);
        panel.setListener(new ControlPanelView.PanelViewListener() {
            @Override
            public void OnPublishClicked(ControlPanelView panel) {
                // int idx = _views.indexOf(panel);
                // Long id = _identitys.get(idx);
                // --------------------- _publishLister.onClicked(id);
            }

            @Override
            public void OnVideoMuteClicked(ControlPanelView panel) {
                // int idx = _views.indexOf(panel);
                // Long id = _identitys.get(idx);
                // --------------------- _muteVideoListerer.onClicked(id);
            }

            @Override
            public void OnAudioMuteClicked(ControlPanelView panel) {
                // int idx = _views.indexOf(panel);
                // Long id = _identitys.get(idx);
                // --------------------- _muteAudioListerer.onClicked(id);
            }

            @Override
            public void OnTapped(ControlPanelView panel) {
                for (long identity : _viewDict.keySet()) {
                    ControlPanelView eachPanel = _viewDict.get(identity);
                    if (eachPanel == panel) {
                        _aspectListerer.onClicked(identity);
                    }
                }
            }
        });
        _viewDict.put(identity, panel);

        addView(panel);
        relayoutSubviews();
        return panel;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e(TAG, "CHANGE ");
        //relayoutSubviews();
    }

    public ControlPanelView getLocalDisplayView(long identity, Context context) {
        return fectchDisplayView(identity, context, true);
    }

    @Override
    public ControlPanelView getRemoteDisplay(long identity, Context context) {
        return fectchDisplayView(identity, context, false);
    }

    @Override
    public void defetchDisplayView(long identity) {
        Log.e(TAG, "defetchDisplayView ");
        ControlPanelView view = _viewDict.get(identity);
        removeView(view);
        _viewDict.remove(identity);
        relayoutSubviews();
    }

    @Override
    public void defetchAllDisplayView() {
        for (long identity : _viewDict.keySet()) {
            ControlPanelView eachPanel = _viewDict.get(identity);
            removeView(eachPanel);
        }
        _viewDict.clear();
    }

    @Override
    public void setToogleVideoMuteListener(ToogleButtonListener listener) {
        _muteVideoListerer = listener;
    }

    @Override
    public void setToogleAudioMuteListener(ToogleButtonListener listener) {
        _muteAudioListerer = listener;
    }

    @Override
    public void setTooglePublishListener(ToogleButtonListener listener) {
        _publishLister = listener;
    }

    @Override
    public void setToogleAspectListener(ToogleButtonListener listener) {
        _aspectListerer = listener;
    }

}

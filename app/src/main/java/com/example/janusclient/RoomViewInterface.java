package com.example.janusclient;

import android.content.Context;

/**
 * Created by lg on 23/10/2017.
 */

public interface RoomViewInterface {
    public static interface ToogleButtonListener {
        void onClicked(long identity);
    }
    public ControlPanelView getLocalDisplayView(long identity, Context context);
    public ControlPanelView getRemoteDisplay(long identity, Context context);
    public void defetchDisplayView(long identity);
    public void defetchAllDisplayView();

    public void setToogleVideoMuteListener( ToogleButtonListener listener);
    public void setToogleAudioMuteListener( ToogleButtonListener listener);
    public void setTooglePublishListener( ToogleButtonListener listener);
    public void setToogleAspectListener( ToogleButtonListener listener);
}

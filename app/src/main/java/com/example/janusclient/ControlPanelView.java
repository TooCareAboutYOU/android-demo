package com.example.janusclient;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.dnion.P2PSDK;
import com.dnion.RenderProxy;

/**
 * Created by lg on 23/10/2017.
 */


public class ControlPanelView extends RelativeLayout {
    Button _publishBtn;
    Button _AudioBtn;
    Button _VideoBtn;
    RenderProxy _renderProxy;
    PanelViewListener _listener;

    Button publishBtn()
    {
        return _publishBtn;
    }

    Button audioBtn()
    {
        return _AudioBtn;
    }

    Button videoBtn()
    {
        return _VideoBtn;
    }

    public RenderProxy renderProxy() {
        return _renderProxy;
    }
    public ControlPanelView(Context context) {
        super(context);
        init(context);
    }

    public ControlPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ControlPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ctrlPanel = inflater.inflate(R.layout.control_pannel, this, true);
        _renderProxy = P2PSDK.getInstance().createRenderProxy(context);
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        SurfaceView display = _renderProxy.getDisplay();
        addView(display, 0, param);

        _publishBtn = (Button) ctrlPanel.findViewById(R.id.publish_btn);
        _publishBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _listener.OnPublishClicked(ControlPanelView.this);
            }
        });

        _AudioBtn = (Button) ctrlPanel.findViewById(R.id.audio_mute_btn);
        _AudioBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _listener.OnAudioMuteClicked(ControlPanelView.this);
            }
        });
        _VideoBtn = (Button) ctrlPanel.findViewById(R.id.video_mute_btn);
        _VideoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _listener.OnVideoMuteClicked(ControlPanelView.this);
            }
        });

        ctrlPanel.setOnClickListener((View view)->{
            _listener.OnTapped(ControlPanelView.this);
        });

        setBackgroundColor(Color.RED);
    }

    public static interface PanelViewListener {
        public void OnPublishClicked(ControlPanelView panel);
        public void OnVideoMuteClicked(ControlPanelView panel);
        public void OnAudioMuteClicked(ControlPanelView panel);
        public void OnTapped(ControlPanelView panel);
    }

    public void setListener(PanelViewListener listener) {
        _listener = listener;
    }
}

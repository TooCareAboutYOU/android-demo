package com.example.janusclient;

import com.dnion.P2PSDKSignaling;

/**
 * Created by lg on 23/10/2017.
 */

public class JanusMessages {
    public static class OnConnectionSuccess{
        public OnConnectionSuccess() {
        }
    }

    public static class OnConnectionFailed{
        String _error;
        public OnConnectionFailed(String error) {
            _error = error;
        }
    }

    public static class OnDisconnected{
        String _reason;
        public OnDisconnected(String reason) {
            _reason = reason;
        }
    }

    public static  class OnChatStart {
        public OnChatStart() {
        }
    }

    public static  class OnChatStop {
        public OnChatStop() {
        }
    }

    public static class onGotUserVideo {
        public onGotUserVideo() { }
    }

    public static class onLostUserVideo {
        public onLostUserVideo() { }
    }
}

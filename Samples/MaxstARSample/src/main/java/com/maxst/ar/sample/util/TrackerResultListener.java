package com.maxst.ar.sample.util;

public interface TrackerResultListener {
    void sendData(String metaData);
    void sendFusionState(int state);
}

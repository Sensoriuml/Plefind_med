package com.ublox.BLE_med.interfaces;


import android.support.annotation.RequiresApi;

import com.ublox.BLE_med.utils.PhyMode;

public interface ITestInteractionListener {
    void onSwitchCredits(boolean enabled);
    void onSwitchTest(boolean enabled);
    void onModeSet(boolean continuousEnabled);
    void onPacketSizeChanged(int packetSize);
    void onMtuSizeChanged(int size);
    void onBitErrorChanged(boolean enabled);
    void onReset();
    void updateConnectionPrio(int connectionParamter);
    @RequiresApi(26)
    void onPhyModeChange(PhyMode mode);
}

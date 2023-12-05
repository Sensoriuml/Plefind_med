package com.ublox.BLE_med.activities;

public class LockinData {
    public int LockinNow;
    public int LockinAv;
    public int LockinTmp;
    public static int TabIndex;
    public static int Src;
    public static int Led;
    public static int Det;
    public Boolean Enabled;
    public int Amplify;
    public int Modulation;
    public int Offset;


    public LockinData(int aLed, int aDet) {
        Src = (aLed & 0x03) | ((aDet & 0x03) << 4);
        InitSource(Src);
    }

    public void SetupParams(int aAmplify, int aModulation, int aOffset) {
        Amplify = aAmplify;
        Modulation = aModulation;
        Offset = aOffset;
    }

    public void InitSource(int aSrc) {
        Src = aSrc;
        Led = aSrc & 0x03;
        Det = (aSrc >> 4) & 0x03;
        TabIndex = Led + (Det << 2);
        Enabled = false;
    }

    public void UpdateLockin(int aLockinNow) {
        LockinNow = aLockinNow;
    }

}

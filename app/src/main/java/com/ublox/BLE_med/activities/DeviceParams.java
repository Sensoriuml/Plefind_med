package com.ublox.BLE_med.activities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceParams {
    final int LOCKIN_COUNT = 4;

    public LockinData[] Lockins= new LockinData [4];
    public int FrameIndex = 0;
    public int LedsEnabled = 0;
    public int DetsEnabled = 0;
    public int ChangeMask = 0;

    public String Name = "";
    public int HwVersion;
    public int SwVersion;

    public DeviceParams() {
        int i = 0;
        for (LockinData ld : Lockins) {

            ld = new LockinData(i % 2, i / 2);
            Lockins[i] = ld;
            i++;
            // 0 LED1 DET1
            // 1 LED2 DET1
            // 2 LED1 DET2
            // 3 LED2 DET2

        }
    }

    //--------------------
    public void setVersion(char aOpis[], int aStartIndex) {
        char znak;
        StringBuilder str = new StringBuilder();
        while (true) {
            znak = aOpis[aStartIndex++];
            if (znak == 0) break;
            str.append(znak);
        }
        Name = str.toString();
        HwVersion = 0;
        SwVersion = 0;
        Pattern p = Pattern.compile("PLEFIND\\.(H[0-9]{2})\\.S([0-9]{3})"); // H20.H02.S003
        Matcher m = p.matcher(str);
        if (m.find() == false) return;
        HwVersion = Integer.parseInt(m.group());
        if (m.find() == false) return;
        SwVersion = Integer.parseInt((m.group()));
    }

    //--------------------
    public void setFrameIndex(int aIndex) {
        if (FrameIndex == aIndex) return;
        FrameIndex = aIndex;
        ChangeMask |= 0x01; // index
    }

    //--------------------
    public void setLedsCount(int aLeds) {
        aLeds &= 0x03;
        if (LedsEnabled == aLeds) return;
        LedsEnabled = aLeds;
        ChangeMask |= 0x02; // main config

        updateEnabledChannels();
    }

    //--------------------
    public void setDetsCount(int aCount) {
        aCount &= 0x03;
        if (DetsEnabled == aCount) return;
        DetsEnabled = aCount;
        ChangeMask |= 0x02; // main config
        updateEnabledChannels();
    }

    //--------------------
    void updateEnabledChannels() {
        Lockins[0].Enabled = ((LedsEnabled & 0x01) != 0) && ((DetsEnabled & 0x01) != 0);
        Lockins[1].Enabled = ((LedsEnabled & 0x02) != 0) && ((DetsEnabled & 0x01) != 0);
        Lockins[2].Enabled = ((LedsEnabled & 0x01) != 0) && ((DetsEnabled & 0x02) != 0);
        Lockins[3].Enabled = ((LedsEnabled & 0x02) != 0) && ((DetsEnabled & 0x02) != 0);
    }

    //--------------------
    public void setLockinParams(int aOffset, int aModulation, int aAmplify, int aChannel) {
        LockinData ld;

        if (aChannel > 3) return;
        ld = Lockins[aChannel];
        //if(ld.Enabled) return;
        if (aOffset >= 0 && aOffset != ld.Offset) {
            ld.Offset = aOffset;
            ChangeMask |= (0x10 << aChannel);
        }
        if (aModulation >= 0 && aModulation != ld.Modulation) {
            ld.Modulation = aModulation;
            ChangeMask |= (0x10 << aChannel);
        }
        if ((aAmplify >= 0) && aAmplify != ld.Amplify) {
            ld.Amplify = aAmplify;
            ChangeMask |= (0x10 << aChannel);
        }
    }

    //--------------------
    public void setLockinValue(int aLockin, int aChannel) {
        if (aChannel > 3) return;
        LockinData ld = Lockins[aChannel];
        if (ld.Enabled==false) return;
        if (aLockin >= 0 && aLockin != ld.LockinNow) {
            ld.LockinNow = aLockin;
            ChangeMask |= (0x100 << aChannel);
        }
    }
    //--------------------

}

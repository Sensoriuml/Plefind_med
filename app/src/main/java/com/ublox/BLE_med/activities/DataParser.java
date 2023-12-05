package com.ublox.BLE_med.activities;


public class DataParser {


    final static int FRAME_OVERHEAD = 6;

    public byte LastOutIndex = 0;
    public int  TotalData;

    public DeviceParams deviceParams;

    public ParamParser paramParser = new ParamParser();

    final int MAX_BUFFER_SIZE = 128 + 4;
    private char[] ParseBuffer = new char[MAX_BUFFER_SIZE];
    private int BufferIndex = 0;

    public DataParser(DeviceParams aDeviceParams) {
        deviceParams = aDeviceParams;
        clearAnalizer();
    }

    public int addToBuffer(byte[] aData) {
        int changed = 0;
        TotalData+=aData.length;
        for (byte znak : aData) {
            if ((znak == 0x0D) || (znak == 0x0A)) {
                if (BufferIndex > 3) {
                    ParseBuffer[BufferIndex] = 0x0D;
                    ParseBuffer[BufferIndex + 1] = 0;
                    //if (ParseBuffer[0] == 'x')
                    //    changed |= AnalizeFrameLine(ParseBuffer, BufferIndex - 1);
                    //else
                        changed |= AnalizeTextLine(ParseBuffer);
                }
                BufferIndex = 0;
                continue;
            }
            if ((znak < ' ') || (znak > 0x7F) || (BufferIndex >= (MAX_BUFFER_SIZE-2))) {
                BufferIndex = 0;
                continue;
            }
            ParseBuffer[BufferIndex] = (char) znak;
            BufferIndex++;
        }
        return changed;
        //if((changed&0x02)!=0)
//            sendToActiveFragmentL1L2(L1,L2,FrameIndex);
///        if((changed&0x04)!=0)
//            sendToActiveFragmentParams(L1OffsetValue,L1ModValue,L2OffsetValue,L2ModValue);
    }

    /*
        private int L1=0,L2=0;
        private int L1OffsetValue=0, L1ModValue=0,L2OffsetValue=0,L2ModValue=0;
        private int FrameIndex=0;
    */
    //------------------------------------------------
    public int AnalizeFrameLine(char[] aBuffer, int aLength) {
        int len;
        ; //analiza ramek
        if ((aBuffer[1] < 0x40) || (aBuffer[1] >= (0x40 + 64))) return 0;
        if (aBuffer[aLength] != '.') return 0;
        aBuffer[aLength] = 0;
        len = aBuffer[3];
        len -= 0x20;
        if (len < 0 || len > 94) return 0;

        switch (aBuffer[2]) {
            case '?':
                return 1;
            case 'E':
                break;
            case 'R':
                break;
            case 'P':
                //return ScanDataToWrite(char[] aBuffer, int aOffset, int aLength);
                break;
            case 'G':
                //return ScanDataToRead(char[] aBuffer, int aOffset, int aLength);
                break;
            case 'S':
                break;
            default:
                return 0;
        }
        return 0;
    }
    //------------------------------------------------

    private int[] valsS = new int[16];
    private int   indexS;
    private int   minS;

    private void clearAnalizer()
    {
        for (int i=0;i<valsS.length;i++) valsS[i] = -1;
        indexS = 0;
        minS = 1;
    }

    public int AnalizeTextLine(char[] aBuffer) {
        //for (int val : vals) val = -1;
        //int i = 0;
        //int min = 1;

        for (char znak : aBuffer) {
            if (znak == 0x0D) {
                valsS[indexS] *= minS;
                if (indexS == (6 - 1)) {
                    deviceParams.setFrameIndex(valsS[0]);
                    deviceParams.setLedsCount(valsS[1]%10);
                    deviceParams.setDetsCount(valsS[1] / 10);

                    deviceParams.setLockinValue(valsS[2], 0);
                    deviceParams.setLockinValue(valsS[3], 1);
                    deviceParams.setLockinValue(valsS[4], 2);
                    deviceParams.setLockinValue(valsS[5], 3);

                    clearAnalizer();

                    return 6;
                }
                if (indexS == (12 - 1)) {
                    deviceParams.setFrameIndex(valsS[0]);
                    deviceParams.setLedsCount(valsS[1]%10);
                    deviceParams.setDetsCount(valsS[1] /10);

                    deviceParams.setLockinValue(valsS[2], 0);
                    deviceParams.setLockinValue(valsS[3], 1);
                    deviceParams.setLockinValue(valsS[4], 2);
                    deviceParams.setLockinValue(valsS[5], 3);

                    deviceParams.setLockinParams(valsS[6]*10, valsS[7]*10, valsS[10], 0);
                    deviceParams.setLockinParams(valsS[8]*10, valsS[9]*10, valsS[10], 1);
                    deviceParams.setLockinParams(valsS[6]*10, valsS[7]*10, valsS[11], 2);
                    deviceParams.setLockinParams(valsS[8]*10, valsS[9]*10, valsS[11], 3);

                    clearAnalizer();

                    return 12;
                }
                clearAnalizer();
                return 0;
            }
            if (znak == ',') {
                valsS[indexS] *= minS;
                indexS++;
                minS = 1;
                if (indexS > 15) {
                    clearAnalizer();
                    return 0;
                }
                continue;
            }
            if (znak == '-') {
                minS = -1;
                continue;
            }
            if (znak < '0' || znak > '9') {
                clearAnalizer();
                return 0;
            }
            if (valsS[indexS] == -1) valsS[indexS] = 0;
            valsS[indexS] *= 10;
            valsS[indexS] += znak - '0';
        }
        return 0;
    }
    public byte [] makeFrame(char aCommand, String aData)
    {
        byte [] buf = new byte[aData.length()+FRAME_OVERHEAD];

        LastOutIndex = (byte)((LastOutIndex+1) & 0x3F);
        buf[0] = 'X';
        buf[1] =  (byte)(LastOutIndex+0x20);
        buf[2] = (byte)aCommand;
        buf[3]= ' ';



        int i = 0;
        while(i<aData.length()) {
            buf[4 + i] = (byte) aData.charAt(i);
            i++;
        }
        buf[4+i]='.';
        buf[4+1+i]=0x0D;
        return buf;
    }

}
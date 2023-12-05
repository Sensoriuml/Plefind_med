package com.ublox.BLE_med.activities;

import static com.ublox.BLE_med.activities.ParamParser.Parameter.paramValues;

public class ParamParser {

    public long      parsedValueDigital;
    public String    parsedStringValue;
    public Parameter scannedParam;




    public int     offsetNow;
    public int     lengthNow;
    public char [] bufferNow;


    public static final long BOOL_MIN		= +0L;
    public static final long BOOL_MAX		= +1L;
    public static final long INTXX_U_MIN	= +0L;
    public static final long INT08_S_MIN	= -128L;
    public static final long INT08_S_MAX	= +127L;
    public static final long INT08_U_MAX	= +255L;
    public static final long INT16_S_MIN	= -32768L;
    public static final long INT16_S_MAX	= +32767L;
    public static final long INT16_U_MAX	= +65535L;
    public static final long INT32_S_MIN	= -2147483648L;
    public static final long INT32_S_MAX	= +2147483647L;
    public static final long INT32_U_MAX	= +4294967295L;
    public static final long STRING_MIN		= 0L;
    public static final long STRING_MAX		= 255L;

    public enum Type
    {
        UNDEFINED						(0, 0, 0, 0),
        BOOL							(1, 1, BOOL_MIN,    BOOL_MAX),
        STRING							(2, 0, STRING_MIN,  STRING_MAX),
        UINT8							(3, 1, INTXX_U_MIN, INT08_U_MAX),
        INT8							(4, 1, INT08_S_MIN, INT08_S_MAX),
        UINT16							(5, 2, INTXX_U_MIN, INT16_U_MAX),
        INT16							(6, 2, INT16_S_MIN, INT16_S_MAX),
        UINT32							(7, 4, INTXX_U_MIN, INT32_U_MAX),
        INT32							(8, 4, INT32_S_MIN, INT32_S_MAX);

        private Type(int id, int len, long min, long max)	{ this.id = id; this.len = len; this.min = min; this.max = max; }

        public final int id, len;
        public final long min, max;

        public int getId()							{ return id; }
        public String getName()							{ return name(); }
        public static Type findId(int id)			{ for (Type type : values()) if (type.id == id) return type; return UNDEFINED; }
    };

    public enum Parameter {
        UNKNOWN   (0, "",       Type.UNDEFINED, 0, 0),
        SINUS1_OFF(1, "S10F",   Type.UINT16, 0, 999),
        SINUS1_MOD(2, "S1MO",   Type.UINT16, 0, 999),
        SINUS2_OFF(3, "S20F",   Type.UINT16, 0, 999),
        SINUS2_MOD(4, "S2MO",   Type.UINT16, 0, 999),

        AMP1_RES  (5, "AMP1",   Type.UINT8, 0, 127),
        AMP2_RES  (6, "AMP2",   Type.UINT8, 0, 127),

        LEDDET_MODE(8, "MODE",  Type.UINT8, 0, 0x33),
        DISPLAY(9, "STAT",      Type.UINT8, 0, 14);

        public static long[] paramValues = new long[Parameter.values().length];

        public final int id;
        public final String name;
        public final int min;
        public final int max;
        public final Type type;

        public int getId() {
            return id;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public String getName() {
            return name;
        }

        private Parameter(int id, String name, Type typ, int min, int max) {
            this.id = id;
            this.name = name;
            this.min = min;
            this.max = max;
            this.type= typ;
        }

        public static Parameter findParam(String aParam) {
            for (Parameter par : values()) if (par.getName() == aParam) return par;
            return UNKNOWN;
        }
    }

     public int ScanDataToWrite(char[] aBuffer, int aOffset, int aLength)
     {
         offsetNow = aOffset;
         lengthNow = aLength;
         bufferNow = aBuffer;

         return 1;

     }

     public int ScanGetNextParamId()
     {
         String str;
         scannedParam = Parameter.UNKNOWN;
         if ((offsetNow + 6) >= lengthNow) return -1;
         if (bufferNow[offsetNow + 4] != '=') return -1;
         str = new String(bufferNow, offsetNow, 4);
         scannedParam = Parameter.findParam(str);
         return scannedParam.id;
     }

     public int ScanGetNextDigitValue()
     {
         //if(scannedParam.type.id>Type.STRING.id)
         return 0;
     }
     public int ScanGetNextStringValue() {
        return 0;
     }
/*
     public int

            for (long val: paramValues)
                val=NOTCHANGED;

            while (offsetNow < aLength)
            {
                   if ((offsetNow + 6) >= aLength) return -count;
                    if (aBuffer[offsetNow + 4] != '=') return -count;
                    str = new String(aBuffer, aOffset, 4);

                    Parameter par = Parameter.findParam(str);
                    if(par==Parameter.UNKNOWN) return -1;

                    paramValues[par.id]=
            }
     }
*/
     public int ScanDataToRead(char[] aBuffer, int aOffset, int aLength)
     {
        return 0;
     }



}
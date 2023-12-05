package com.ublox.BLE_med.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ibotta.android.support.pickerdialogs.SupportedDatePickerDialog;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ublox.BLE_med.FileChooser;
import com.ublox.BLE_med.R;
import com.ublox.BLE_med.activities.DataParser;
import com.ublox.BLE_med.activities.DeviceParams;
import com.ublox.BLE_med.activities.DevicesActivity;
import com.ublox.BLE_med.activities.MainActivity;
import com.ublox.BLE_med.adapters.ChatAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.view.View.FOCUS_RIGHT;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.util.Arrays.asList;


public class ChatFragment extends Fragment {

    private int width, height;

    private DeviceParams devParams;
    public DataParser dataParser;

    private LinearLayout linMAIN, linENGINEER, linSTOP;

    private ImageButton D1DownButton;
    private ImageButton D1UpButton;

    private ImageButton D2DownButton;
    private ImageButton D2UpButton;

    private ImageButton D1Down10Button;
    private ImageButton D1Up10Button;

    private ImageButton D2Down10Button;
    private ImageButton D2Up10Button;

    private Button saveL1Button, saveL2Button, saveM1Button, saveM2Button;
    private Button saveA1Button, saveA2Button;

    private CheckBox L1enabled;
    private CheckBox L2enabled;
    private CheckBox D1enabled;
    private CheckBox D2enabled;
    private EditText filenameText;


    private CheckBox editEnabled;

    private TextView L1D1value, L2D1value, L1D2value, L2D2value, L1D1avvalue, L2D1avvalue, L1D2avvalue, L2D2avvalue;
    private TextView D1val, D2val;

    private EditText L1offEdit;
    private EditText L2offEdit;

    private EditText L1modEdit;
    private EditText L2modEdit;

    private EditText D1valEdit;
    private EditText D2valEdit;

    private Button SaveAll, SaveFlash;

    private TextView IndexText;
    public int SavedData = 0;

    private TextView storedData;
    private EditText notesTxt, heightTxt, weightTxt;

    GraphView graph11;
    private boolean saving = false;
    private String strAll = "";
    private Vector v0 = new Vector();
    private Vector v1 = new Vector();
    private Vector v2 = new Vector();
    private Vector v3 = new Vector();
    private double mean, var;

    private LineGraphSeries<DataPoint> mSeries11;
    private LineGraphSeries<DataPoint> mSeries12;
    private LineGraphSeries<DataPoint> mSeries21;
    private LineGraphSeries<DataPoint> mSeries22;

    private LineGraphSeries<DataPoint> measure;
    private LineGraphSeries<DataPoint> healthy_std;
    private LineGraphSeries<DataPoint> healthy_mean;

    private Runnable mTimer2;
    public int ScrollIndex;
    private final Handler mHandler = new Handler();

    Context context;

    OutputStreamWriter outputCsvStream = null;
    FileOutputStream fileOut = null;

    private TextView LockinInfo;

    Handler thandler = new Handler();
    Runnable trunnable = new Runnable() {
        @Override
        public void run() {
            if (dataTimeout == 1) {
//                if(linENGINEER.getVisibility() != View.GONE){
//                    LockinInfo.setTextColor(Color.RED);
//                    LockinInfo.setText(R.string.reset);
//                }
                linMAIN.setVisibility(View.GONE);
                linSTOP.setVisibility(VISIBLE);
                txtSTOP.setText(R.string.reset);
            }
            if (dataTimeout != 0) dataTimeout--;
            thandler.postDelayed(this, 100);

        }
    };

    private int dataTimeout = 0;

    private IChatInteractionListener mListener;
    private Spinner jspinner;
    private RadioGroup radioGroupSex, radioGroupLung, radioGroupPleeff, radioGroupThoraco;
    private LineChart linechart, linechart2;
    private BarChart barchart;
    private boolean device = true;
    private String hardware;
    private String software;
    private Button readFile;
    private double[][] xy;
    private double[][][] xy_read;
    private int plot = 0;
    private LinearLayout afterReadLayout;
    private ImageButton backbutton, deletebutton, nextbutton;
    private String[] fileanames;
    private double[][] h_Nmeanstd;
    private String filehealthyN = "healthyN";;
    private double[][] h_LBmeanstd;
    private String filehealthyLB = "healthyLB";;
    private double[][][] xy_read2;
    private double[][] xy2;
    private ImageView gradientAnimation0, gradientAnimation, gradientline;
    private AnimationDrawable animationDrawable0, animationDrawable;
    private LinearLayout barchartLayout;
    private Button showLAB, hideLAB;
    private int showLABint = 0;
    private Button BtOLEDoff, BtOLEDon, bTSaveParams;
    private TextView linechart_filename;
    private int mood = 10;
    private TextView txtSTOP;
    private int[] PESEL;
    private String patientID;
    private RadioButton rb1, rb2;
    private Date dateRun;
    private EditText pesel1, pesel2, pesel3, pesel4, pesel5, pesel6, pesel7, pesel8, pesel9, pesel10, pesel11;
    private boolean achtung;


    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        devParams = new DeviceParams();
        dataParser = new DataParser(devParams);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity().getApplicationContext();
        context.setTheme(android.R.style.Theme_Holo_Light);
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;

        chatAdapter = new ChatAdapter(getActivity());

        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat2, container, false);
    }

    private ChatAdapter chatAdapter;
    private ListView lvChat;
    private BluetoothGattCharacteristic characteristic;

    private void EnableDisableEditText(boolean isEnabled, EditText editText) {
        if(linENGINEER.getVisibility() != View.GONE){
            editText.setBackgroundColor(isEnabled ? Color.LTGRAY : Color.WHITE);
            editText.setEnabled(isEnabled);
        }

        /*
        editText.setFocusable(isEnabled);
        editText.setFocusableInTouchMode(isEnabled) ;
        editText.setClickable(isEnabled);
        editText.setLongClickable(isEnabled);
        editText.setCursorVisible(isEnabled) ;
        */
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        linENGINEER = view.findViewById(R.id.lin_aux);
        linMAIN = view.findViewById(R.id.lin_main);
        linSTOP = view.findViewById(R.id.lin_stop);
        createStopView(view);
        createOldView(view);

        linMAIN.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams linLayoutParam = new FrameLayout.LayoutParams(2*width/3, LinearLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams linLayoutParam2 = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //linLayoutParam.gravity = Gravity.CENTER;
        //linr.setLayoutParams(linLayoutParam);

        LinearLayout.LayoutParams linLayoutParamEdit = new LinearLayout.LayoutParams(width/3, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutParamEdit.gravity = Gravity.CENTER;
        RelativeLayout.LayoutParams linLayoutParamRadioButton = new RelativeLayout.LayoutParams(width/3, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout lineng = new LinearLayout(context);
        lineng.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linLayoutButtonEng = new LinearLayout.LayoutParams(width/6, height/25);
        LinearLayout.LayoutParams linLayoutButtonBirthdate = new LinearLayout.LayoutParams(2*width/3, LinearLayout.LayoutParams.WRAP_CONTENT);
        linLayoutButtonBirthdate.gravity = Gravity.BOTTOM;
        linMAIN.addView(lineng, linLayoutParam2);

        /*TextView txt0 = new TextView(context);
        lineng.addView(txt0, linLayoutButtonEng);
        TextView txt1 = new TextView(context);
        txt1.setText("Birthdate");
        txt1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        txt1.setPadding(0,0,0,0);
        lineng.addView(txt1, linLayoutButtonBirthdate);*/

        TextView txt0 = new TextView(context);
        lineng.addView(txt0, linLayoutButtonEng);
        TextView txt1 = new TextView(context);
        txt1.setText("PESEL");
        txt1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        txt1.setPadding(0,0,0,0);
        lineng.addView(txt1, linLayoutButtonBirthdate);

        showLAB = new Button(context);
        showLAB.setBackgroundColor(Color.TRANSPARENT);
        showLAB.setEnabled(false);
        showLAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLABint++;
                if (showLABint == 3){
                    linENGINEER.setVisibility(VISIBLE);
                    //if (LockinInfo == null) createOldView(view);
                    showLAB.setVisibility(View.GONE);
                }
            }
        });
        lineng.addView(showLAB, linLayoutButtonEng);

        /*Button birthdateb = new Button(context);
        birthdateb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                SupportedDatePickerDialog.OnDateSetListener mDateSetListener = new SupportedDatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ++month;
                        birthdateb.setText(dayOfMonth + "/" + month + "/" + year);
                        birthdate = Integer.toString(dayOfMonth) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
                    }
                };
                SupportedDatePickerDialog datepicker = new SupportedDatePickerDialog(getActivity(), R.style.SpinnerDatePickerDialogTheme, mDateSetListener, year, month, day);
                datepicker.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                datepicker.show();
            }
        });
        birthdateb.setBackgroundResource(R.drawable.rounded_button);
        birthdateb.setText("dd/mm/yyyy");
        linMAIN.addView(birthdateb, linLayoutParam);*/

        LinearLayout linhorpesel = new LinearLayout(context);
        linhorpesel.setOrientation(LinearLayout.HORIZONTAL);
        linhorpesel.setGravity(Gravity.CENTER);
        linMAIN.addView(linhorpesel);

        pesel1 = new EditText(context);
        pesel2 = new EditText(context);
        pesel3 = new EditText(context);
        pesel4 = new EditText(context);
        pesel5 = new EditText(context);
        pesel6 = new EditText(context);
        pesel7 = new EditText(context);
        pesel8 = new EditText(context);
        pesel9 = new EditText(context);
        pesel10 = new EditText(context);
        pesel11 = new EditText(context);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                TextView text = (TextView)getActivity().getCurrentFocus();

                if (text != null && text.length() == 1)
                {
                    View next = text.focusSearch(View.FOCUS_RIGHT); // or FOCUS_FORWARD
                    if (next != null)
                        next.requestFocus();
                }

                if (text != null && text.length() == 0)
                {
                    View previous = text.focusSearch(View.FOCUS_LEFT); // or FOCUS_BACKWARD
                    if (previous != null)
                        previous.requestFocus();
                }

                pesel1.setTextColor(Color.BLACK);
                pesel2.setTextColor(Color.BLACK);
                pesel3.setTextColor(Color.BLACK);
                pesel4.setTextColor(Color.BLACK);
                pesel5.setTextColor(Color.BLACK);
                pesel6.setTextColor(Color.BLACK);
                pesel7.setTextColor(Color.BLACK);
                pesel8.setTextColor(Color.BLACK);
                pesel9.setTextColor(Color.BLACK);
                pesel10.setTextColor(Color.BLACK);
                pesel11.setTextColor(Color.BLACK);

                if (!(pesel1.getText().toString().equals("")
                        || pesel2.getText().toString().equals("")
                        || pesel3.getText().toString().equals("")
                        || pesel4.getText().toString().equals("")
                        || pesel5.getText().toString().equals("")
                        || pesel6.getText().toString().equals("")
                        || pesel7.getText().toString().equals("")
                        || pesel8.getText().toString().equals("")
                        || pesel9.getText().toString().equals("")
                        || pesel10.getText().toString().equals("")
                        || pesel11.getText().toString().equals(""))){
                    PESEL = new int[11];
                    PESEL[0] = Integer.valueOf(pesel1.getText().toString());
                    PESEL[1] = Integer.valueOf(pesel2.getText().toString());
                    PESEL[2] = Integer.valueOf(pesel3.getText().toString());
                    PESEL[3] = Integer.valueOf(pesel4.getText().toString());
                    PESEL[4] = Integer.valueOf(pesel5.getText().toString());
                    PESEL[5] = Integer.valueOf(pesel6.getText().toString());
                    PESEL[6] = Integer.valueOf(pesel7.getText().toString());
                    PESEL[7] = Integer.valueOf(pesel8.getText().toString());
                    PESEL[8] = Integer.valueOf(pesel9.getText().toString());
                    PESEL[9] = Integer.valueOf(pesel10.getText().toString());
                    PESEL[10] = Integer.valueOf(pesel11.getText().toString());
                    if(!peselValidator()){
                        pesel1.setTextColor(Color.parseColor("#CE0200"));
                        pesel2.setTextColor(Color.parseColor("#CE0200"));
                        pesel3.setTextColor(Color.parseColor("#CE0200"));
                        pesel4.setTextColor(Color.parseColor("#CE0200"));
                        pesel5.setTextColor(Color.parseColor("#CE0200"));
                        pesel6.setTextColor(Color.parseColor("#CE0200"));
                        pesel7.setTextColor(Color.parseColor("#CE0200"));
                        pesel8.setTextColor(Color.parseColor("#CE0200"));
                        pesel9.setTextColor(Color.parseColor("#CE0200"));
                        pesel10.setTextColor(Color.parseColor("#CE0200"));
                        pesel11.setTextColor(Color.parseColor("#CE0200"));
                    } else {
                        pesel1.setTextColor(Color.parseColor("#10800C"));
                        pesel2.setTextColor(Color.parseColor("#10800C"));
                        pesel3.setTextColor(Color.parseColor("#10800C"));
                        pesel4.setTextColor(Color.parseColor("#10800C"));
                        pesel5.setTextColor(Color.parseColor("#10800C"));
                        pesel6.setTextColor(Color.parseColor("#10800C"));
                        pesel7.setTextColor(Color.parseColor("#10800C"));
                        pesel8.setTextColor(Color.parseColor("#10800C"));
                        pesel9.setTextColor(Color.parseColor("#10800C"));
                        pesel10.setTextColor(Color.parseColor("#10800C"));
                        pesel11.setTextColor(Color.parseColor("#10800C"));

                        if(PESEL[9] % 2 == 0){
                            rb1.setChecked(true);
                        } else {
                            rb2.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        pesel1.setPadding(0,0,0,0);
        pesel1.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel1.addTextChangedListener(textWatcher);
        pesel1.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel1.setGravity(Gravity.CENTER);
        pesel1.setTag("0");
        linhorpesel.addView(pesel1);

        pesel2.setPadding(0,0,0,0);
        pesel2.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel2.addTextChangedListener(textWatcher);
        pesel2.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel2.setGravity(Gravity.CENTER);
        pesel2.setTag("1");
        linhorpesel.addView(pesel2);

        pesel3.setPadding(0,0,0,0);
        pesel3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel3.addTextChangedListener(textWatcher);
        pesel3.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel3.setGravity(Gravity.CENTER);
        pesel3.setTag("2");
        linhorpesel.addView(pesel3);

        pesel4.setPadding(0,0,0,0);
        pesel4.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel4.addTextChangedListener(textWatcher);
        pesel4.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel4.setGravity(Gravity.CENTER);
        pesel4.setTag("3");
        linhorpesel.addView(pesel4);

        pesel5.setPadding(0,0,0,0);
        pesel5.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel5.addTextChangedListener(textWatcher);
        pesel5.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel5.setGravity(Gravity.CENTER);
        pesel5.setTag("4");
        linhorpesel.addView(pesel5);

        pesel6.setPadding(0,0,0,0);
        pesel6.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel6.addTextChangedListener(textWatcher);
        pesel6.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel6.setGravity(Gravity.CENTER);
        pesel6.setTag("5");
        linhorpesel.addView(pesel6);

        pesel7.setPadding(0,0,0,0);
        pesel7.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel7.addTextChangedListener(textWatcher);
        pesel7.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel7.setGravity(Gravity.CENTER);
        pesel7.setTag("6");
        linhorpesel.addView(pesel7);

        pesel8.setPadding(0,0,0,0);
        pesel8.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel8.addTextChangedListener(textWatcher);
        pesel8.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel8.setGravity(Gravity.CENTER);
        pesel8.setTag("7");
        linhorpesel.addView(pesel8);

        pesel9.setPadding(0,0,0,0);
        pesel9.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel9.addTextChangedListener(textWatcher);
        pesel9.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel9.setGravity(Gravity.CENTER);
        pesel9.setTag("8");
        linhorpesel.addView(pesel9);

        pesel10.setPadding(0,0,0,0);
        pesel10.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel10.addTextChangedListener(textWatcher);
        pesel10.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel10.setGravity(Gravity.CENTER);
        pesel10.setTag("9");
        linhorpesel.addView(pesel10);

        pesel11.setPadding(0,0,0,0);
        pesel11.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
        pesel11.addTextChangedListener(textWatcher);
        pesel11.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        pesel11.setGravity(Gravity.CENTER);
        pesel11.setTag("10");
        linhorpesel.addView(pesel11);

        TextView txt2 = new TextView(context);
        txt2.setText("Weight");
        txt2.setGravity(Gravity.CENTER);
        txt2.setPadding(0,15,0,0);
        linMAIN.addView(txt2, linLayoutParam);

        weightTxt = new EditText(context);
        weightTxt.setHint("kg");
        weightTxt.setGravity(Gravity.CENTER);
        weightTxt.setPadding(0,0,0,4);
        weightTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        linMAIN.addView(weightTxt, linLayoutParamEdit);

        TextView txt3 = new TextView(context);
        txt3.setText("Height");
        txt3.setGravity(Gravity.CENTER);
        txt3.setPadding(0,15,0,0);
        linMAIN.addView(txt3);

        heightTxt = new EditText(context);
        heightTxt.setHint("cm");
        heightTxt.setGravity(Gravity.CENTER);
        heightTxt.setPadding(0,0,0,4);
        heightTxt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        linMAIN.addView(heightTxt, linLayoutParamEdit);

        TextView txt4 = new TextView(context);
        txt4.setText("Sex");
        txt4.setGravity(Gravity.CENTER);
        txt4.setPadding(0,15,0,0);
        linMAIN.addView(txt4);

        radioGroupSex = new RadioGroup(context);
        radioGroupSex.setOrientation(RadioGroup.HORIZONTAL);
        radioGroupSex.setGravity(Gravity.CENTER);
        rb1  = new RadioButton(context);
        rb1.setText("Female");
        rb1.setGravity(Gravity.CENTER);
        rb1.setPadding(0,0,0,0);
        radioGroupSex.addView(rb1, linLayoutParamRadioButton);
        rb2  = new RadioButton(context);
        rb2.setText("Male");
        rb2.setGravity(Gravity.CENTER);
        rb2.setPadding(0,0,0,0);
        radioGroupSex.addView(rb2, linLayoutParamRadioButton);
        linMAIN.addView(radioGroupSex, linLayoutParam);

        TextView txt5 = new TextView(context);
        txt5.setText("Lung");
        txt5.setGravity(Gravity.CENTER);
        txt5.setPadding(0,15,0,0);
        linMAIN.addView(txt5, linLayoutParam);

        radioGroupLung = new RadioGroup(context);
        radioGroupLung.setOrientation(RadioGroup.HORIZONTAL);
        radioGroupLung.setGravity(Gravity.CENTER);
        RadioButton rb12  = new RadioButton(context);
        rb12.setText("Left");
        rb12.setGravity(Gravity.CENTER);
        rb12.setPadding(0,0,0,0);
        radioGroupLung.addView(rb12, linLayoutParamRadioButton);
        RadioButton rb22  = new RadioButton(context);
        rb22.setText("Right");
        rb22.setGravity(Gravity.CENTER);
        rb22.setPadding(0,0,0,0);
        radioGroupLung.addView(rb22, linLayoutParamRadioButton);
        linMAIN.addView(radioGroupLung, linLayoutParam);

        radioGroupLung.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (xy != null || xy_read != null){
                    plotMeasurement();
                    plotMeasurement2();
                }
            }
        });

        TextView txt6 = new TextView(context);
        txt6.setText("Pleural effusion");
        txt6.setGravity(Gravity.CENTER);
        txt6.setPadding(0,15,0,0);
        linMAIN.addView(txt6, linLayoutParam);

        radioGroupPleeff = new RadioGroup(context);
        radioGroupPleeff.setOrientation(RadioGroup.HORIZONTAL);
        radioGroupPleeff.setGravity(Gravity.CENTER);
        RadioButton rb13  = new RadioButton(context);
        rb13.setTag("radioButtonPEYes");
        rb13.setText("Yes");
        rb13.setGravity(Gravity.CENTER);
        rb13.setPadding(0,0,0,0);
        radioGroupPleeff.addView(rb13, linLayoutParamRadioButton);
        RadioButton rb23  = new RadioButton(context);
        rb23.setTag("radioButtonPENo");
        rb23.setText("No");
        rb23.setGravity(Gravity.CENTER);
        rb23.setPadding(0,0,0,0);
        radioGroupPleeff.addView(rb23, linLayoutParamRadioButton);
        linMAIN.addView(radioGroupPleeff, linLayoutParam);

        TextView txt7 = new TextView(context);
        txt7.setText("Thoracocentesis");
        txt7.setGravity(Gravity.CENTER);
        txt7.setPadding(0,15,0,0);
        linMAIN.addView(txt7, linLayoutParam);

        radioGroupThoraco = new RadioGroup(context);
        radioGroupThoraco.setOrientation(RadioGroup.HORIZONTAL);
        radioGroupThoraco.setGravity(Gravity.CENTER);
        RadioButton rb14  = new RadioButton(context);
        rb14.setTag("radioButtonThoracoYes");
        rb14.setText("Yes");
        rb14.setGravity(Gravity.CENTER);
        rb14.setPadding(0,0,0,0);
        radioGroupThoraco.addView(rb14, linLayoutParamRadioButton);
        RadioButton rb24  = new RadioButton(context);
        rb24.setTag("radioButtonThoracoNo");
        rb24.setText("No");
        rb24.setGravity(Gravity.CENTER);
        rb24.setPadding(0,0,0,0);
        radioGroupThoraco.addView(rb24, linLayoutParamRadioButton);
        linMAIN.addView(radioGroupThoraco, linLayoutParam);

        TextView txt8 = new TextView(context);
        txt8.setText("Diagnosis code");
        txt8.setGravity(Gravity.CENTER);
        txt8.setPadding(0,15,0,0);
        linMAIN.addView(txt8, linLayoutParam);

        jspinner = new Spinner(context);
        String[] spinnerItems = new String[]{
                "J90",
                "J91",
                "J92",
                "J93",
                "J94",
                "J81",
                "Others (please note)",
                "Healthy"
        };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.spinner_textview_align, spinnerItems);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_textview_align);
        jspinner.setAdapter(spinnerArrayAdapter);
        linMAIN.addView(jspinner, linLayoutParam);

        TextView txt9 = new TextView(context);
        txt9.setText("Mood");
        txt9.setGravity(Gravity.CENTER);
        txt9.setPadding(0,15,0,10);
        linMAIN.addView(txt9, linLayoutParam);

        LinearLayout linmood = new LinearLayout(context);
        linmood.setOrientation(LinearLayout.HORIZONTAL);
        //linmood.setPadding(0,40,0,40);
        linmood.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams llhmood = new LinearLayout.LayoutParams(2*width/3, height/13);
        llhmood.gravity = Gravity.CENTER_HORIZONTAL;
        linMAIN.addView(linmood, llhmood);

        LinearLayout.LayoutParams llhmoody = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.25f);
        ImageButton mood0 = new ImageButton(context);
        ImageButton mood1 = new ImageButton(context);
        ImageButton mood2 = new ImageButton(context);
        ImageButton mood3 = new ImageButton(context);

        mood0.setImageResource(R.drawable.smiley0);
        mood0.setBackgroundColor(Color.TRANSPARENT);
        mood0.setAdjustViewBounds(true);
        mood0.setPadding(0,0,0,0);
        mood0.setAlpha(.2f);
        mood0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = 0;
                mood0.setAlpha(1.f);
                mood1.setAlpha(.2f);
                mood2.setAlpha(.2f);
                mood3.setAlpha(.2f);
            }
        });
        linmood.addView(mood0, llhmoody);

        mood1.setImageResource(R.drawable.smiley1);
        mood1.setBackgroundColor(Color.TRANSPARENT);
        mood1.setAdjustViewBounds(true);
        mood1.setPadding(0,0,0,0);
        mood1.setAlpha(.2f);
        mood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = 1;
                mood1.setAlpha(1.f);
                mood0.setAlpha(.2f);
                mood2.setAlpha(.2f);
                mood3.setAlpha(.2f);
            }
        });
        linmood.addView(mood1, llhmoody);

        mood2.setImageResource(R.drawable.smiley2);
        mood2.setBackgroundColor(Color.TRANSPARENT);
        mood2.setAdjustViewBounds(true);
        mood2.setPadding(0,0,0,0);
        mood2.setAlpha(.2f);
        mood2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = 2;
                mood2.setAlpha(1.f);
                mood0.setAlpha(.2f);
                mood1.setAlpha(.2f);
                mood3.setAlpha(.2f);
            }
        });
        linmood.addView(mood2, llhmoody);

        mood3.setImageResource(R.drawable.smiley3);
        mood3.setBackgroundColor(Color.TRANSPARENT);
        mood3.setAdjustViewBounds(true);
        mood3.setPadding(0,0,0,0);
        mood3.setAlpha(.2f);
        mood3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mood = 3;
                mood3.setAlpha(1.f);
                mood0.setAlpha(.2f);
                mood1.setAlpha(.2f);
                mood2.setAlpha(.2f);
            }
        });
        linmood.addView(mood3, llhmoody);

        notesTxt = new EditText(context);
        notesTxt.setHint("Notes");
        notesTxt.setGravity(Gravity.CENTER);
        notesTxt.setPadding(0,10,0,0);
        notesTxt.setMinHeight(height/10);
        linMAIN.addView(notesTxt, linLayoutParam);

        filenameText = new EditText(context);
        filenameText.setHint("Name");
        filenameText.setGravity(Gravity.CENTER);
        filenameText.setPadding(0,15,0,4);
        filenameText.setMinHeight(2*height/10/3);
        linMAIN.addView(filenameText, linLayoutParam);

        LinearLayout linh = new LinearLayout(context);
        linh.setOrientation(LinearLayout.HORIZONTAL);
        linh.setPadding(0,40,0,40);
        linh.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams linhParam = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.49f);
        LinearLayout.LayoutParams linhParam2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.02f);
        LinearLayout.LayoutParams llh = new LinearLayout.LayoutParams(2*width/3, LinearLayout.LayoutParams.WRAP_CONTENT);
        llh.gravity = Gravity.CENTER_HORIZONTAL;
        linMAIN.addView(linh, llh);

        Button btRun = new Button(context);
        btRun.setBackgroundResource(R.drawable.rounded_button_green);
        btRun.setText("RUN");
        btRun.setAlpha(1.f);
        btRun.setEnabled(true);
        btRun.setGravity(Gravity.CENTER);
        linh.addView(btRun, linhParam);

        Button space = new Button(context);
        space.setVisibility(View.INVISIBLE);
        linh.addView(space, linhParam2);

        Button btStop = new Button(context);
        btStop.setBackgroundResource(R.drawable.rounded_button_red);
        btStop.setText("STOP");
        btStop.setAlpha(.2f);
        btStop.setEnabled(false);
        btStop.setGravity(Gravity.CENTER);
        linh.addView(btStop, linhParam);

        btRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateRun = new Date();

                measurement = "";
                measurementcont = "";
                strAll = "";

                saving = true;
                SavingFile(saving, filenameText.getEditableText());

                if (verifyPermissionStorage()) {
                    SendRun();
                    btRun.setAlpha(.2f);
                    btRun.setEnabled(false);
                    btStop.setAlpha(1.f);
                    btStop.setEnabled(true);

                    linechart.setVisibility(View.GONE);
                    linechart_filename.setVisibility(View.GONE);
                    linechart2.setVisibility(View.GONE);
                    animationDrawable0.stop();
                    gradientAnimation0.setVisibility(View.GONE);
                    gradientAnimation.setVisibility(VISIBLE);
                    animationDrawable.start();
                    gradientline.setVisibility(VISIBLE);
                    barchartLayout.setVisibility(VISIBLE);
                }
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendStop();
                btStop.setAlpha(.2f);
                btStop.setEnabled(false);
                btRun.setAlpha(1.f);
                btRun.setEnabled(true);

                saving = false;

                splitString();

                if (v0.size()>1 && v2.size()>1){
                    xy = countData_Plot(v0, v2);
                    xy2 = countData_Plot2(v0, v2);
                }
                plot = 0;
                plotMeasurement();
                plotMeasurement2();
                SavingFile(saving, filenameText.getEditableText());
            }
        });

        linechart = new LineChart(context);
        linechart.setVisibility(View.GONE);
        linechart.setMinimumHeight(height/3);
        linMAIN.addView(linechart, linLayoutParam2);

        linechart_filename = new TextView(context);
        linechart_filename.setVisibility(View.GONE);
        linechart_filename.setPadding(0,0,0,40);
        linechart_filename.setGravity(Gravity.CENTER);
        linechart_filename.setSingleLine(false);
        linMAIN.addView(linechart_filename, linLayoutParam2);

        linechart2 = new LineChart(context);
        linechart2.setVisibility(View.GONE);
        linechart2.setPadding(0,10,0,0);
        linechart2.setMinimumHeight(height/3);
        linMAIN.addView(linechart2, linLayoutParam2);

        barchartLayout = new LinearLayout(context);
        barchartLayout.setOrientation(LinearLayout.HORIZONTAL);
        //linhbarchart.setPadding(0,20,0,0);
        LinearLayout.LayoutParams linhbarchartparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linhbarchartparam.gravity = Gravity.CENTER_HORIZONTAL;
        linMAIN.addView(barchartLayout, linhbarchartparam);

        LinearLayout.LayoutParams imag1 = new LinearLayout.LayoutParams(width/10, width/10*406/306);
        imag1.leftMargin = 3*width/20;
        imag1.gravity = Gravity.BOTTOM;
        ImageView img1 = new ImageView(context);
        img1.setImageResource(R.drawable.red_arrow);
        barchartLayout.addView(img1, imag1);

        LinearLayout.LayoutParams linhbarchartparam2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linhbarchartparam2.gravity = Gravity.CENTER;
        linhbarchartparam2.rightMargin = width/6;
        linhbarchartparam2.leftMargin = width/30;
        barchart = new BarChart(context);
        barchart.setMinimumHeight(height/3);
        barchartLayout.addView(barchart, linhbarchartparam2);

        gradientline = new ImageView(context);
        gradientline.setMaxHeight(1);
        gradientline.setMinimumHeight(1);
        gradientline.setBackgroundColor(Color.BLACK);//"#21659E"
        linMAIN.addView(gradientline);

        gradientAnimation0 = new ImageView(context);
        gradientAnimation0.setMinimumHeight(height/3);
        gradientAnimation0.setBackgroundResource(R.drawable.gradient_list0);
        animationDrawable0 = (AnimationDrawable) gradientAnimation0.getBackground();
        animationDrawable0.setEnterFadeDuration(2000);
        animationDrawable0.setExitFadeDuration(2000);
        animationDrawable0.start();
        linMAIN.addView(gradientAnimation0, linhbarchartparam);

        gradientAnimation = new ImageView(context);
        gradientAnimation.setMinimumHeight(height/3);
        gradientAnimation.setBackgroundResource(R.drawable.gradient_list);
        animationDrawable = (AnimationDrawable) gradientAnimation.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        gradientAnimation.setVisibility(View.GONE);
        linMAIN.addView(gradientAnimation, linhbarchartparam);

        afterReadLayout = new LinearLayout(context);
        afterReadLayout.setOrientation(LinearLayout.HORIZONTAL);
        afterReadLayout.setPadding(0,10,0,10);
        afterReadLayout.setGravity(Gravity.CENTER);
        afterReadLayout.setVisibility(View.GONE);
        LinearLayout.LayoutParams linhreadparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //linhreadparam.gravity = Gravity.CENTER;
        linMAIN.addView(afterReadLayout, linhreadparam);

        backbutton = new ImageButton(context);
        backbutton.setBackgroundColor(Color.TRANSPARENT);
        backbutton.setImageResource(R.drawable.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plot = plot - 1;
                System.out.println(plot);
                plotMeasurement();
                plotMeasurement2();
            }
        });
        afterReadLayout.addView(backbutton);

        deletebutton = new ImageButton(context);
        deletebutton.setBackgroundColor(Color.TRANSPARENT);
        deletebutton.setImageResource(R.drawable.delete);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (plot == 0){
                    plot = 1;
                    xy = null;
                    xy2 = null;
                    plotMeasurement();
                    plotMeasurement2();
                } else {
                    int j = 0;
                    double[][][] xy_read_temp = new double[xy_read.length-1][2][1000];
                    double[][][] xy_read2_temp = new double[xy_read2.length-1][2][1000];
                    String[] fileanames_temp = new String[xy_read.length-1];
                    for(int i = 0; i < xy_read.length; i++){
                        if(i != (plot - 1)){
                            xy_read_temp[j] = xy_read[i];
                            xy_read2_temp[j] = xy_read2[i];
                            fileanames_temp[j] = fileanames[i];
                            j++;
                        }
                    }
                    if (plot == xy_read.length) plot = plot - 1;
                    xy_read = xy_read_temp.clone();
                    xy_read2 = xy_read2_temp.clone();
                    fileanames = fileanames_temp.clone();
                    plotMeasurement();
                    plotMeasurement2();
                }
            }
        });
        afterReadLayout.addView(deletebutton);

        nextbutton = new ImageButton(context);
        nextbutton.setBackgroundColor(Color.TRANSPARENT);
        nextbutton.setImageResource(R.drawable.next);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plot = plot + 1;
                System.out.println(plot);
                plotMeasurement();
                plotMeasurement2();
            }
        });
        afterReadLayout.addView(nextbutton);

        LinearLayout.LayoutParams linhreadparam2 = new LinearLayout.LayoutParams(2*width/3, ViewGroup.LayoutParams.WRAP_CONTENT);
        linhreadparam2.setMargins(0,0,0,30);
        readFile = new Button(context);
        readFile.setText("Draw from a file");
        readFile.setBackgroundResource(R.drawable.rounded_button);
        Drawable img = getResources().getDrawable( R.drawable.readcsv );
        img.setBounds( 0, 0, height/27, height/27 );
        readFile.setCompoundDrawables(img,null,null,null);
        readFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyPermissionStorageRead()) {
                    processFile();
                    //performFileSearch();
                }
            }
        });
        linMAIN.addView(readFile, linhreadparam2);

        thandler.postDelayed((Runnable) trunnable, 100);

        mTimer2 = new Runnable() {
            @Override
            public void run() {
                ScrollIndex++;
//                if (devParams.Lockins[0].Enabled) {
//                    mSeries11.appendData(new DataPoint(ScrollIndex, (double) devParams.Lockins[0].LockinNow / 1000.0), true, 800);
//                    ShowGraphValues(graph11v, 0);
//                }
//                if (devParams.Lockins[2].Enabled) {
//                    mSeries12.appendData(new DataPoint(ScrollIndex, (double) devParams.Lockins[2].LockinNow / 1000.0), true, 800);
//                    ShowGraphValues(graph12v, 2);
//                }
//                if (devParams.Lockins[1].Enabled) {
//                    mSeries21.appendData(new DataPoint(ScrollIndex, (double) devParams.Lockins[1].LockinNow / 1000.0), true, 800);
//                    ShowGraphValues(graph21v, 1);
//                }
//                if (devParams.Lockins[3].Enabled) {
//                    mSeries22.appendData(new DataPoint(ScrollIndex, (double) devParams.Lockins[3].LockinNow / 1000.0), true, 800);
//                    ShowGraphValues(graph22v, 3);
//                }
                mHandler.postDelayed(this, 120);
                initGraphBar();
            }
        };
        mHandler.postDelayed(mTimer2, 1000);

        //InitGraph();
        initGraphBar();

    }

    private void createStopView(View view){
        linSTOP.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        param.gravity = Gravity.CENTER;


        txtSTOP = new TextView(context);
        txtSTOP.setGravity(Gravity.CENTER);
        txtSTOP.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        txtSTOP.setTypeface(txtSTOP.getTypeface(), Typeface.BOLD);
        txtSTOP.setSingleLine(false);
        txtSTOP.setTextColor(Color.RED);
        txtSTOP.setText(getResources().getString(R.string.wait));
        linSTOP.addView(txtSTOP, param);
    }

    private void createOldView(View view){

        //linENGINEER = view.findViewById(R.id.lin_aux);
        linENGINEER.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams linauxparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linaux1 = new LinearLayout(context);
        linaux1.setOrientation(LinearLayout.HORIZONTAL);
        linaux1.setPadding(0,20,0,0);
        linENGINEER.addView(linaux1, linauxparam);

        LinearLayout.LayoutParams lin1param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.33f);
        TextView txt1 = new TextView(context);
        txt1.setText("SETUP");
        txt1.setGravity(Gravity.CENTER);
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        txt1.setTypeface(txt1.getTypeface(), Typeface.BOLD);
        linaux1.addView(txt1, lin1param);

        editEnabled = new CheckBox(context);
        editEnabled.setText("edit values");
        editEnabled.setClickable(true);
        editEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                EnableDisableEditText(b, L1offEdit);
                EnableDisableEditText(b, L2offEdit);
                EnableDisableEditText(b, L1modEdit);
                EnableDisableEditText(b, L2modEdit);
                EnableDisableEditText(b, D1valEdit);
                EnableDisableEditText(b, D2valEdit);

                saveL1Button.setVisibility(b ? VISIBLE : INVISIBLE);
                saveL2Button.setVisibility(b ? VISIBLE : INVISIBLE);
                saveM1Button.setVisibility(b ? VISIBLE : INVISIBLE);
                saveM2Button.setVisibility(b ? VISIBLE : INVISIBLE);

                saveA2Button.setVisibility(b ? VISIBLE : INVISIBLE);
                saveA1Button.setVisibility(b ? VISIBLE : INVISIBLE);

                SaveAll.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
            }
        });
        linaux1.addView(editEnabled, lin1param);

        hideLAB = new Button(context);
        hideLAB.setBackgroundColor(Color.TRANSPARENT);
        hideLAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linENGINEER.setVisibility(View.GONE);
                showLABint = 0;
                showLAB.setVisibility(VISIBLE);
            }
        });
        linaux1.addView(hideLAB, lin1param);

        LinearLayout linaux2 = new LinearLayout(context);
        linaux2.setOrientation(LinearLayout.HORIZONTAL);
        linaux2.setPadding(0,0,0,0);
        linENGINEER.addView(linaux2, linauxparam);

        LinearLayout.LayoutParams lin2param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1/7f);
        L1enabled = new CheckBox(context);
        L1enabled.setText("L1");
        L1enabled.setClickable(true);
        L1enabled.setTypeface(L1enabled.getTypeface(), Typeface.BOLD);
        L1enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDetsLeds();
            }
        });
        linaux2.addView(L1enabled, lin2param);

        TextView txt2 = new TextView(context);
        txt2.setText("off=");
        txt2.setGravity(Gravity.CENTER);
        linaux2.addView(txt2, lin2param);

        L1offEdit = new EditText(context);
        L1offEdit.setEnabled(false);
        L1offEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        L1offEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        L1offEdit.setTypeface(L1offEdit.getTypeface(), Typeface.BOLD);
        L1offEdit.setText("0");
        linaux2.addView(L1offEdit, lin2param);

        saveL1Button = new Button(context);
        saveL1Button.setText("S");
        saveL1Button.setVisibility(INVISIBLE);
        saveL1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveParamText(String.valueOf(L1offEdit.getEditableText()), "S1OF");
            }
        });
        linaux2.addView(saveL1Button, lin2param);

        TextView txt3 = new TextView(context);
        txt3.setText("mod=");
        txt3.setGravity(Gravity.CENTER);
        linaux2.addView(txt3, lin2param);

        L1modEdit = new EditText(context);
        L1modEdit.setEnabled(false);
        L1modEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        L1modEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        L1modEdit.setTypeface(L1modEdit.getTypeface(), Typeface.BOLD);
        L1modEdit.setText("0");
        linaux2.addView(L1modEdit, lin2param);

        saveM1Button = new Button(context);
        saveM1Button.setText("S");
        saveM1Button.setVisibility(INVISIBLE);
        saveM1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveParamText(String.valueOf(L1modEdit.getEditableText()), "S1MO");
            }
        });
        linaux2.addView(saveM1Button, lin2param);

        LinearLayout linaux3 = new LinearLayout(context);
        linaux3.setOrientation(LinearLayout.HORIZONTAL);
        linaux3.setPadding(0,0,0,0);
        linENGINEER.addView(linaux3, linauxparam);

        LinearLayout.LayoutParams lin3param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1/7f);
        L2enabled = new CheckBox(context);
        L2enabled.setText("L2");
        L2enabled.setClickable(true);
        L2enabled.setTypeface(L2enabled.getTypeface(), Typeface.BOLD);
        L2enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDetsLeds();
            }
        });
        linaux3.addView(L2enabled, lin3param);

        TextView txt22 = new TextView(context);
        txt22.setText("off=");
        txt22.setGravity(Gravity.CENTER);
        linaux3.addView(txt22, lin3param);

        L2offEdit = new EditText(context);
        L2offEdit.setEnabled(false);
        L2offEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        L2offEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        L2offEdit.setTypeface(L2offEdit.getTypeface(), Typeface.BOLD);
        L2offEdit.setText("0");
        linaux3.addView(L2offEdit, lin3param);

        saveL2Button = new Button(context);
        saveL2Button.setText("S");
        saveL2Button.setVisibility(INVISIBLE);
        saveL2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveParamText(String.valueOf(L2offEdit.getEditableText()), "S2OF");
            }
        });
        linaux3.addView(saveL2Button, lin3param);

        TextView txt32 = new TextView(context);
        txt32.setText("mod=");
        txt32.setGravity(Gravity.CENTER);
        linaux3.addView(txt32, lin3param);

        L2modEdit = new EditText(context);
        L2modEdit.setEnabled(false);
        L2modEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        L2modEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        L2modEdit.setTypeface(L2modEdit.getTypeface(), Typeface.BOLD);
        L2modEdit.setText("0");
        linaux3.addView(L2modEdit, lin3param);

        saveM2Button = new Button(context);
        saveM2Button.setText("S");
        saveM2Button.setVisibility(INVISIBLE);
        saveM2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveParamText(String.valueOf(L2modEdit.getEditableText()), "S2MO");
            }
        });
        linaux3.addView(saveM2Button, lin3param);

        LinearLayout linaux4 = new LinearLayout(context);
        linaux4.setOrientation(LinearLayout.HORIZONTAL);
        linaux4.setPadding(0,0,0,0);
        linENGINEER.addView(linaux4, linauxparam);

        LinearLayout.LayoutParams lin4param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3/7f);
        D1enabled = new CheckBox(context);
        D1enabled.setText("D1");
        D1enabled.setClickable(true);
        D1enabled.setTypeface(D1enabled.getTypeface(), Typeface.BOLD);
        D1enabled.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        D1enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDetsLeds();
            }
        });
        linaux4.addView(D1enabled, lin2param);

        TextView txt41 = new TextView(context);
        txt41.setText("amp=");
        txt41.setGravity(Gravity.CENTER);
        linaux4.addView(txt41, lin2param);

        D1valEdit = new EditText(context);
        D1valEdit.setEnabled(false);
        D1valEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        D1valEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        D1valEdit.setTypeface(D1valEdit.getTypeface(), Typeface.BOLD);
        D1valEdit.setText("0");
        linaux4.addView(D1valEdit, lin2param);

        saveA1Button = new Button(context);
        saveA1Button.setText("S");
        saveA1Button.setVisibility(INVISIBLE);
        saveA1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveParamText(String.valueOf(D1valEdit.getEditableText()), "AMP1");
            }
        });
        linaux4.addView(saveA1Button, lin2param);

        SaveAll = new Button(context);
        SaveAll.setText("Send All");
        SaveAll.setTypeface(SaveAll.getTypeface(), Typeface.BOLD);
        SaveAll.setVisibility(INVISIBLE);
        SaveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendAllData();
            }
        });
        linaux4.addView(SaveAll, lin4param);

        LinearLayout linaux5 = new LinearLayout(context);
        linaux5.setOrientation(LinearLayout.HORIZONTAL);
        linaux5.setPadding(0,0,0,0);
        linENGINEER.addView(linaux5, linauxparam);

        LinearLayout.LayoutParams lin5param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3/7f);
        D2enabled = new CheckBox(context);
        D2enabled.setText("D2");
        D2enabled.setClickable(true);
        D2enabled.setTypeface(D2enabled.getTypeface(), Typeface.BOLD);
        D2enabled.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        D2enabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDetsLeds();
            }
        });
        linaux5.addView(D2enabled, lin2param);

        TextView txt51 = new TextView(context);
        txt51.setText("amp=");
        txt51.setGravity(Gravity.CENTER);
        linaux5.addView(txt51, lin2param);

        D2valEdit = new EditText(context);
        D2valEdit.setEnabled(false);
        D2valEdit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        D2valEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
        D2valEdit.setTypeface(D2valEdit.getTypeface(), Typeface.BOLD);
        D2valEdit.setText("0");
        linaux5.addView(D2valEdit, lin2param);

        saveA2Button = new Button(context);
        saveA2Button.setText("S");
        saveA2Button.setVisibility(INVISIBLE);
        saveA2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveParamText(String.valueOf(D2valEdit.getEditableText()), "AMP2");
            }
        });
        linaux5.addView(saveA2Button, lin2param);

        SaveFlash = new Button(context);
        SaveFlash.setText("Store Config");
        SaveFlash.setTypeface(SaveFlash.getTypeface(), Typeface.BOLD);
        SaveFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendSaveFlash();
            }
        });
        linaux5.addView(SaveFlash, lin5param);

        LinearLayout linaux6 = new LinearLayout(context);
        linaux6.setOrientation(LinearLayout.HORIZONTAL);
        linaux6.setPadding(0,20,0,0);
        linENGINEER.addView(linaux6, linauxparam);

        LinearLayout.LayoutParams lin6param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        LockinInfo = new TextView(context);
        //LockinInfo.setText("WAIT ...");
        LockinInfo.setGravity(Gravity.CENTER);
        LockinInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        LockinInfo.setTypeface(LockinInfo.getTypeface(), Typeface.BOLD);
        linaux6.addView(LockinInfo, lin6param);
        LockinInfo.setTextColor(Color.GREEN);
        LockinInfo.setBackgroundColor(Color.LTGRAY);
        LockinInfo.setText("CONNECTED");

        IndexText = new TextView(context);
        IndexText.setText("0");
        IndexText.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        IndexText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        linaux6.addView(IndexText, lin6param);

        LinearLayout linaux7 = new LinearLayout(context);
        linaux7.setOrientation(LinearLayout.HORIZONTAL);
        linaux7.setPadding(0,16,0,0);
        linENGINEER.addView(linaux7, linauxparam);

        LinearLayout.LayoutParams lin7param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1/6f);
        LinearLayout.LayoutParams lin7Mparam = new LinearLayout.LayoutParams(0, height/20, 1/12f);
        LinearLayout.LayoutParams lin7_2param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1/2f);
        TextView txt_temp = new TextView(context);
        linaux7.addView(txt_temp, lin7param);

        TextView txt71 = new TextView(context);
        txt71.setText("LED1");
        txt71.setGravity(Gravity.CENTER);
        linaux7.addView(txt71, lin7param);

        TextView txt72 = new TextView(context);
        txt72.setText("LED2");
        txt72.setGravity(Gravity.CENTER);
        linaux7.addView(txt72, lin7param);

        TextView txt_temp2 = new TextView(context);
        linaux7.addView(txt_temp2, lin7_2param);

        LinearLayout linaux8 = new LinearLayout(context);
        linaux8.setOrientation(LinearLayout.HORIZONTAL);
        linaux8.setPadding(0,16,0,0);
        linENGINEER.addView(linaux8, linauxparam);

        TextView txt81 = new TextView(context);
        txt81.setText("DET1");
        txt81.setGravity(Gravity.CENTER);
        linaux8.addView(txt81, lin7param);

        L1D1value = new TextView(context);
        L1D1value.setText("0.000");
        L1D1value.setGravity(Gravity.CENTER);
        L1D1value.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L1D1value.setTypeface(L1D1value.getTypeface(), Typeface.BOLD);
        linaux8.addView(L1D1value, lin7param);

        L2D1value = new TextView(context);
        L2D1value.setText("0.000");
        L2D1value.setGravity(Gravity.CENTER);
        L2D1value.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L2D1value.setTypeface(L2D1value.getTypeface(), Typeface.BOLD);
        linaux8.addView(L2D1value, lin7param);

        D1Down10Button = new ImageButton(context);
        D1Down10Button.setBackgroundResource(R.drawable.arrowdown10);
        D1Down10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP1-=10");
            }
        });
        linaux8.addView(D1Down10Button, lin7Mparam);

        D1DownButton = new ImageButton(context);
        D1DownButton.setBackgroundResource(R.drawable.arrowdown);
        D1DownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP1-=1");
            }
        });
        linaux8.addView(D1DownButton, lin7Mparam);

        D1val = new TextView(context);
        D1val.setText("000");
        D1val.setGravity(Gravity.CENTER);
        D1val.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        linaux8.addView(D1val, lin7param);

        D1UpButton = new ImageButton(context);
        D1UpButton.setBackgroundResource(R.drawable.arrowup);
        D1UpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP1+=1");
            }
        });
        linaux8.addView(D1UpButton, lin7Mparam);

        D1Up10Button = new ImageButton(context);
        D1Up10Button.setBackgroundResource(R.drawable.arrowup10);
        D1Up10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP1+=10");
            }
        });
        linaux8.addView(D1Up10Button, lin7Mparam);

        LinearLayout linaux9 = new LinearLayout(context);
        linaux9.setOrientation(LinearLayout.HORIZONTAL);
        linaux9.setPadding(0,2,0,0);
        linENGINEER.addView(linaux9, linauxparam);

        TextView txt_temp3 = new TextView(context);
        linaux9.addView(txt_temp3, lin7param);

        L1D1avvalue = new TextView(context);
        L1D1avvalue.setText("0.000");
        L1D1avvalue.setGravity(Gravity.CENTER);
        L1D1avvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L1D1avvalue.setTypeface(L1D1avvalue.getTypeface(), Typeface.BOLD);
        linaux9.addView(L1D1avvalue, lin7param);

        L2D1avvalue = new TextView(context);
        L2D1avvalue.setText("0.000");
        L2D1avvalue.setGravity(Gravity.CENTER);
        L2D1avvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L2D1avvalue.setTypeface(L2D1avvalue.getTypeface(), Typeface.BOLD);
        linaux9.addView(L2D1avvalue, lin7param);

        TextView txt_temp4 = new TextView(context);
        linaux9.addView(txt_temp4, lin7_2param);

        LinearLayout linaux10 = new LinearLayout(context);
        linaux10.setOrientation(LinearLayout.HORIZONTAL);
        linaux10.setPadding(0,20,0,0);
        linENGINEER.addView(linaux10, linauxparam);

        TextView txt101 = new TextView(context);
        txt101.setText("DET2");
        txt101.setGravity(Gravity.CENTER);
        linaux10.addView(txt101, lin7param);

        L1D2value = new TextView(context);
        L1D2value.setText("0.000");
        L1D2value.setGravity(Gravity.CENTER);
        L1D2value.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L1D2value.setTypeface(L1D2value.getTypeface(), Typeface.BOLD);
        linaux10.addView(L1D2value, lin7param);

        L2D2value = new TextView(context);
        L2D2value.setText("0.000");
        L2D2value.setGravity(Gravity.CENTER);
        L2D2value.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L2D2value.setTypeface(L2D2value.getTypeface(), Typeface.BOLD);
        linaux10.addView(L2D2value, lin7param);

        D2Down10Button = new ImageButton(context);
        D2Down10Button.setBackgroundResource(R.drawable.arrowdown10);
        D2Down10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP2-=10");
            }
        });
        linaux10.addView(D2Down10Button, lin7Mparam);

        D2DownButton = new ImageButton(context);
        D2DownButton.setBackgroundResource(R.drawable.arrowdown);
        D2DownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP2-=1");
            }
        });
        linaux10.addView(D2DownButton, lin7Mparam);

        D2val = new TextView(context);
        D2val.setText("000");
        D2val.setGravity(Gravity.CENTER);
        D2val.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
        linaux10.addView(D2val, lin7param);

        D2UpButton = new ImageButton(context);
        D2UpButton.setBackgroundResource(R.drawable.arrowup);
        D2UpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP2+=1");
            }
        });
        linaux10.addView(D2UpButton, lin7Mparam);

        D2Up10Button = new ImageButton(context);
        D2Up10Button.setBackgroundResource(R.drawable.arrowup10);
        D2Up10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendFrame('P', "AMP2+=10");
            }
        });
        linaux10.addView(D2Up10Button, lin7Mparam);

        LinearLayout linaux11 = new LinearLayout(context);
        linaux11.setOrientation(LinearLayout.HORIZONTAL);
        linaux11.setPadding(0,2,0,0);
        linENGINEER.addView(linaux11, linauxparam);

        TextView txt_temp5 = new TextView(context);
        linaux11.addView(txt_temp5, lin7param);

        L1D2avvalue = new TextView(context);
        L1D2avvalue.setText("0.000");
        L1D2avvalue.setGravity(Gravity.CENTER);
        L1D2avvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L1D2avvalue.setTypeface(L1D2avvalue.getTypeface(), Typeface.BOLD);
        linaux11.addView(L1D2avvalue, lin7param);

        L2D2avvalue = new TextView(context);
        L2D2avvalue.setText("0.000");
        L2D2avvalue.setGravity(Gravity.CENTER);
        L2D2avvalue.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        L2D2avvalue.setTypeface(L2D2avvalue.getTypeface(), Typeface.BOLD);
        linaux11.addView(L2D2avvalue, lin7param);

        TextView txt_temp6 = new TextView(context);
        linaux11.addView(txt_temp6, lin7_2param);

        LinearLayout linaux12 = new LinearLayout(context);
        linaux12.setOrientation(LinearLayout.HORIZONTAL);
        linaux12.setPadding(0,0,0,0);
        linENGINEER.addView(linaux12, linauxparam);

        LinearLayout.LayoutParams lin12param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        BtOLEDoff = new Button(context);
        BtOLEDoff.setText("OledOff");
        BtOLEDoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDisplayOff();
            }
        });
        linaux12.addView(BtOLEDoff,lin12param);

        BtOLEDon = new Button(context);
        BtOLEDon.setText("OledOn");
        BtOLEDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDisplayOn();
            }
        });
        linaux12.addView(BtOLEDon,lin12param);

        bTSaveParams = new Button(context);
        bTSaveParams.setText("Save");
        bTSaveParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendSaveParams();
            }
        });
        linaux12.addView(bTSaveParams,lin12param);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IChatInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void addMessage(byte[] data) {

        dataReceived(data);
        ChatAdapter.ChatMessage message = new ChatAdapter.ChatMessage(new String(data), new Date().toString(), false);
        chatAdapter.addMessage(message);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setCharacteristicFifo(BluetoothGattCharacteristic characteristic) {
        try {
            if (this.characteristic == null) {
                this.characteristic = characteristic;
                /*chatAdapter = new ChatAdapter(getActivity());
                lvChat.setAdapter(chatAdapter);
                */
                mListener.onNotify(characteristic, true);
            } else if (!this.characteristic.equals(characteristic)) {
                this.characteristic = characteristic;
                /*
                chatAdapter = new ChatAdapter(getActivity());
                lvChat.setAdapter(chatAdapter);
                */
                mListener.onNotify(characteristic, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface IChatInteractionListener {
        public void onSendMessage(BluetoothGattCharacteristic characteristic, byte[] message);

        public void onNotify(BluetoothGattCharacteristic characteristic, boolean enabled);
    }


    public void updateFragmentData(DeviceParams devParams) {

        /*
        Fragment fragment = mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem());

        if (fragment == null) {
            return;
        }
*/
        View v = getView();

        if ((devParams.ChangeMask & 0x01) != 0) {
            devParams.ChangeMask &= ~0x01;
            //if(linENGINEER.getVisibility() != View.GONE){
                IndexText.setText(String.format("Index = %3d  (In=%d)", devParams.FrameIndex, dataParser.TotalData));
            //}
        }

        if ((devParams.ChangeMask & 0x02) != 0) {
            devParams.ChangeMask &= ~0x02;

            //InitGraph();

            //if(linENGINEER.getVisibility() != View.GONE){
                L1enabled.setChecked((devParams.LedsEnabled & 0x01) != 0);
                L2enabled.setChecked((devParams.LedsEnabled & 0x02) != 0);
                D1enabled.setChecked((devParams.DetsEnabled & 0x01) != 0);
                D2enabled.setChecked((devParams.DetsEnabled & 0x02) != 0);
            //}

            if (((devParams.LedsEnabled & 0x01) != 0) && ((devParams.DetsEnabled & 0x01) != 0)) {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L1D1value.setVisibility(VISIBLE);
                    L1D1avvalue.setVisibility(INVISIBLE);
                //}
                devParams.ChangeMask |= 0xFFFF0;
            } else {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L1D1value.setVisibility(INVISIBLE);
                    L1D1avvalue.setVisibility(INVISIBLE);
                //}
            }

            if (((devParams.LedsEnabled & 0x02) != 0) && ((devParams.DetsEnabled & 0x01) != 0)) {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L2D1value.setVisibility(VISIBLE);
                    L2D1avvalue.setVisibility(INVISIBLE);
                //}
                devParams.ChangeMask |= 0xFFFF0;
            } else {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L2D1value.setVisibility(INVISIBLE);
                    L2D1avvalue.setVisibility(INVISIBLE);
                //}

            }

            if (((devParams.LedsEnabled & 0x01) != 0) && ((devParams.DetsEnabled & 0x02) != 0)) {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L1D2value.setVisibility(VISIBLE);
                    L1D2avvalue.setVisibility(INVISIBLE);
                //}
                devParams.ChangeMask |= 0xFFFF0;
            } else {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L1D2value.setVisibility(INVISIBLE);
                    L1D2avvalue.setVisibility(INVISIBLE);
                //}
            }

            if (((devParams.LedsEnabled & 0x02) != 0) && ((devParams.DetsEnabled & 0x02) != 0)) {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L2D2value.setVisibility(VISIBLE);
                    L2D2avvalue.setVisibility(INVISIBLE);
                //}
                devParams.ChangeMask |= 0xFFFF0;
            } else {
                //if(linENGINEER.getVisibility() != View.GONE){
                    L2D2value.setVisibility(INVISIBLE);
                    L2D2avvalue.setVisibility(INVISIBLE);
                //}
            }

            //((TextView) v.findViewById(R.id.IndexText)).setText(String.format("Offset = %3d%%", devParams.FrameIndex));

        }

        if ((devParams.ChangeMask & 0xF0) != 0) {
            devParams.ChangeMask &= ~0xF0;

            //if(linENGINEER.getVisibility() != View.GONE){
                L1offEdit.setText(String.format("%d", devParams.Lockins[0].Offset));
                L1modEdit.setText(String.format("%d", devParams.Lockins[0].Modulation));

                L2offEdit.setText(String.format("%d", devParams.Lockins[1].Offset));
                L2modEdit.setText(String.format("%d", devParams.Lockins[1].Modulation));

                String str = String.format("%03d", devParams.Lockins[0].Amplify);
                D1valEdit.setText(str);
                D1val.setText(str);
                str = String.format("%03d", devParams.Lockins[2].Amplify);
                D2valEdit.setText(str);
                D2val.setText(str);
            //}

        }

        // if((devParams.ChangeMask&0xF00)!=0) {
        devParams.ChangeMask &= ~0xF00;

        //if(linENGINEER.getVisibility() != View.GONE){
            L1D1value.setText(String.format("%01d.%03d", devParams.Lockins[0].LockinNow / 1000, devParams.Lockins[0].LockinNow % 1000));
            L1D2value.setText(String.format("%01d.%03d", devParams.Lockins[2].LockinNow / 1000, devParams.Lockins[2].LockinNow % 1000));

            L2D1value.setText(String.format("%01d.%03d", devParams.Lockins[1].LockinNow / 1000, devParams.Lockins[1].LockinNow % 1000));
            L2D2value.setText(String.format("%01d.%03d", devParams.Lockins[3].LockinNow / 1000, devParams.Lockins[3].LockinNow % 1000));

            L1D1avvalue.setText(String.format("%01d.%03d", devParams.Lockins[0].LockinAv / 1000, devParams.Lockins[0].LockinAv % 1000));
            L1D2avvalue.setText(String.format("%01d.%03d", devParams.Lockins[2].LockinAv / 1000, devParams.Lockins[2].LockinAv % 1000));

            L2D1avvalue.setText(String.format("%01d.%03d", devParams.Lockins[1].LockinAv / 1000, devParams.Lockins[1].LockinAv % 1000));
            L2D2avvalue.setText(String.format("%01d.%03d", devParams.Lockins[3].LockinAv / 1000, devParams.Lockins[3].LockinAv % 1000));
        //}

        // }
        devParams.ChangeMask &= ~0xFFF;

    }

    public void dataReceived(byte[] data) {
        //System.out.println((double)devParams.Lockins[0].LockinNow / 1000.0);
        int changed = dataParser.addToBuffer(data);
        SaveString(new String(data));

        if (device) {
            String str = new String(data);
            if (str.contains("PLEFIND")) {
                String[] arr = str.split("\\.");
                if (arr.length > 2) {
                    hardware = arr[1];
                    software = arr[2];
                    device = false;
                    if (DevicesActivity.networkConnection) {
                        if (hardware.equals("H01")) { //4.5 cm
                            getGraphFromCloud("45");
                        } else if (hardware.equals("H02")) {//5 cm
                            getGraphFromCloud("50");
                        }
                        allToCloudBeginning();
                    } else {
                        h_Nmeanstd = getDoubleArray(filehealthyN);
                        h_LBmeanstd = getDoubleArray(filehealthyLB);
                    }
                }
            }
        }

        if (changed != 0) {
            dataTimeout = 20;

//            if(linENGINEER.getVisibility() != View.GONE){
//                LockinInfo.setTextColor(Color.GREEN);
//                LockinInfo.setText(getResources().getString(R.string.continue_txt));
//            }

            linSTOP.setVisibility(View.GONE);
            linMAIN.setVisibility(VISIBLE);

            updateFragmentData(devParams);

            if (device) {
                SendDevice();
            }
        }
    }

    public void SendFrame(char aCommand, String aText) {
        if (characteristic != null && ((MainActivity) getActivity()).isConnected()) {
            byte[] buf = dataParser.makeFrame(aCommand, aText);
            mListener.onSendMessage(characteristic, buf);
        } else {
            Toast.makeText(getActivity(), "You need to be connected to a device in order to do this", Toast.LENGTH_LONG).show();
        }
        /*
        if (characteristic != null && ((MainActivity)getActivity()).isConnected()) {
            String msg = etMessage.getText().toString();
            //APET Lägg till kontroll av carriage return (0x0D) switch
            ChatAdapter.ChatMessage message = new ChatAdapter.ChatMessage(msg, new Date().toString(), true);
            chatAdapter.addMessage(message);
            mListener.onSendMessage(characteristic, message.message.getBytes());
        } else {
            Toast.makeText(getActivity(), "You need to be connected to a device in order to do this", Toast.LENGTH_LONG).show();
        }
        etMessage.setText("");
    }
    */
    }

    public void SendDetsLeds() {
        String par = String.format("MODE=%01d%01d", (D1enabled.isChecked() ? 1 : 0) + (D2enabled.isChecked() ? 2 : 0), (L1enabled.isChecked() ? 1 : 0) + (L2enabled.isChecked() ? 2 : 0));
        SendFrame('P', par);
    }

    public void SendDisplayOn() {
        SendFrame('O', "0");
    }

    public void SendDisplayOff() {
        SendFrame('O', "3");
    }

    public void SendSaveParams() {
        SendFrame('S', "");
    }

    public void SendRun() {
        SendFrame('X', "");
    }

    public void SendStop() {
        SendFrame('Z', "");
    }

    public void SendDevice() {
        SendFrame('?', "");
    }

    public void SendAllData() {

        String str = String.format("S1OF=%s,S1MO=%s,S2OF=%s,S2MO=%s,AMP1=%s,AMP2=%s", L1offEdit.getEditableText(), L1modEdit.getEditableText(), L2offEdit.getEditableText(), L2modEdit.getEditableText(), D1valEdit.getEditableText(), D2valEdit.getEditableText());
        SendFrame('P', str);

        /*String str = String.format("S1OF=%s,S1MO=%s",L1offEdit.getEditableText(),L1modEdit.getEditableText());
        SendFrame('P', str);
        str = String.format("S2OF=%s,S2MO=%s",L2offEdit.getEditableText(),L2modEdit.getEditableText());
        SendFrame('P', str);
        str = String.format("AMP1=%s,AMP2=%s",D1valEdit.getEditableText(),D2valEdit.getEditableText());
        SendFrame('P', str);*/
    }

    public void SendSaveFlash() {
        //String str = String.format("S1OF=%s,S1MO=%s,S2OF=%s,S2MO=%s,AMP1=%s,AMP2=%s",L1offEdit.getEditableText(),L1modEdit.getEditableText(),L2offEdit.getEditableText(),L2modEdit.getEditableText(),D1valEdit.getEditableText(),D2valEdit.getEditableText());
        //SendFrame('P', str);
        //String str = String.format("S1OF=%s,S1MO=%s",L1offEdit.getEditableText(),L1modEdit.getEditableText());
        //SendFrame('P', str);
        //str = String.format("S2OF=%s,S2MO=%s",L2offEdit.getEditableText(),L2modEdit.getEditableText());
        //SendFrame('P', str);
        //str = String.format("AMP1=%s,AMP2=%s",D1valEdit.getEditableText(),D2valEdit.getEditableText());
        //SendFrame('P', str);
        SendFrame('S', "");
    }

    /*
        String str = String.format("S1OF=%s,S1MO=%s",L1offEdit.getEditableText(),L1modEdit.getEditableText());
        SendFrame('P', str);
        str = String.format("S2OF=%s,S2MO=%s",L2offEdit.getEditableText(),L2modEdit.getEditableText());
        SendFrame('P', str);
        str = String.format("AMP1=%s,AMP2=%s",D1valEdit.getEditableText(),D2valEdit.getEditableText());
*/


    private boolean SavingFile(boolean aSaving, Editable aFileName) {
        try {
            if (aSaving && aFileName != null) {
                if (outputCsvStream != null) return true;
                SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File myfile = new File(path, aFileName + currentDateandTime + ".csv");
                fileOut = new FileOutputStream(myfile, false);
                outputCsvStream = new OutputStreamWriter(fileOut);
                SavedData = 0;
                return true;
            } else {
                if (outputCsvStream != null) {
                    outputCsvStream.flush();
                    outputCsvStream.close();
                    outputCsvStream = null;
                    if (fileOut != null)
                        fileOut.close();
                    fileOut = null;

                    if (DevicesActivity.networkConnection)
                        allToCloud();
                    else {
                        List<Map<String, Object>> unsaved = getUnsaved();
                        if (unsaved == null) {
                            unsaved = new ArrayList<>();
                            unsaved.add(getCandidate());
                        } else {
                            unsaved.add(getCandidate());
                        }
                        offlineSaving(unsaved);
                    }
                }
                return false;
            }
        } catch (IOException e) {
            outputCsvStream = null;
            e.printStackTrace();
            //if(e.getCause()==)
            //verifyPermissionStorage();
            //Toast.makeText(getActivity(), "Cannot create file. "+e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void SaveString(String aString) {
        //System.out.println(aString);
        if (outputCsvStream == null) return;
        try {
            outputCsvStream.write(aString);
            if(linENGINEER.getVisibility() != View.GONE){
                SavedData++;
                //storedData.setText(" ( " + Integer.toString(SavedData) + " ) ");
            }
            stringToCloud(aString);
            strAll += aString;
        } catch (IOException e) {
            e.printStackTrace();
            SavingFile(false, null);
        }
    }

    private void ShowGraphValues(TextView aText, int aNumber) {
        char znak;
        aNumber &= 0x03;
        int lval = devParams.Lockins[aNumber].LockinTmp;
        int nval = devParams.Lockins[aNumber].LockinNow;
        devParams.Lockins[aNumber].LockinTmp = nval;
        if (nval == lval)
            znak = '=';
        else if (nval < lval)
            znak = '+';
        else znak = '-';
        String str = String.format(" L%dD%d%c%04d ", 1 + (aNumber & 0x01), 1 + (aNumber >> 1), znak, devParams.Lockins[aNumber].LockinNow);
        aText.setText(str);
    }

    private void initGraphBar(){
        ArrayList<BarEntry> barentry = new ArrayList<>();
        barentry.add(new BarEntry(2f,  (devParams.Lockins[2].LockinNow) / 1000.f + 0.01f)); //bliski
        barentry.add(new BarEntry(5f, (devParams.Lockins[0].LockinNow) / 1000.f + 0.01f)); //daleki

        BarDataSet bardataset;
        if (barchart.getData() != null && barchart.getData().getDataSetCount() > 0) {
            bardataset = (BarDataSet) barchart.getData().getDataSetByIndex(0);
            bardataset.setValues(barentry);

            barchart.getData().notifyDataChanged();
            barchart.notifyDataSetChanged();
        } else {

            bardataset = new BarDataSet(barentry, "");
            bardataset.setColor(Color.BLACK);//#21659E
            barchart.getDescription().setEnabled(false);

            BarData bardata = new BarData(bardataset);
            bardata.setValueFormatter(new DefaultValueFormatter(1));
            //bardata.setValueFormatter(new MyValueFormatter());
            barchart.setData(bardata);

            Legend l = barchart.getLegend();
            l.setEnabled(false);
            barchart.getAxisLeft().setDrawGridLines(false);
            barchart.getAxisRight().setDrawGridLines(false);
            barchart.getXAxis().setDrawGridLines(false);
            barchart.getAxisLeft().setDrawLabels(false);
            barchart.getAxisRight().setDrawLabels(false);
            barchart.getXAxis().setDrawLabels(false);
            barchart.getAxisLeft().setEnabled(false);
            barchart.getAxisRight().setEnabled(false);
            barchart.getXAxis().setEnabled(false);
            barchart.setScaleEnabled(false);
            YAxis y = barchart.getAxisLeft();
            y.setAxisMaxValue(4);
            y.setAxisMinValue(0);
            y.setLabelCount(200);
        }
        barchart.invalidate();
    }

    private double[] countAchtung(double[] x, double[] mean, double[]std){

        double[] x_n = new double[x.length];
        double max = 0;
        for(int i = 0; i < x.length; i++){
            x_n[i] = Math.abs(x[i] - mean[i]);
            if(i>80 && i<800 && (x_n[i] - 1.5*std[i] > max)) max = x_n[i] - 1.5*std[i];
        }

        if(max != 0) achtung = true;
        else achtung = false;

        return x_n;
    }

    private void plotMeasurement() {
        animationDrawable0.stop();
        gradientAnimation0.setVisibility(View.GONE);
        animationDrawable.stop();
        gradientAnimation.setVisibility(View.GONE);
        gradientline.setVisibility(View.GONE);
        barchartLayout.setVisibility(View.GONE);
        linechart.setVisibility(VISIBLE);
        linechart_filename.setVisibility(VISIBLE);
        linechart2.setVisibility(VISIBLE);

        setArrows();

        float k = 1.5f;

        int col; //0 - right, 1 - left, 2 - both
        RadioButton radioButtonLung = (RadioButton) radioGroupLung.findViewById(radioGroupLung.getCheckedRadioButtonId());
        if (radioButtonLung == null) {
            col = 2;
        } else {
            if (String.valueOf(radioButtonLung.getText()).charAt(0) == 'R') {
                col = 0;
            } else {
                col = 1;
            }
        }

        if (h_Nmeanstd == null) h_Nmeanstd = new double[1000][6];
        //if (xy == null) xy = new double[1000][2];


        ArrayList<Entry> upperYVals = new ArrayList<>();
        //ArrayList<Entry> lowerYVals = new ArrayList<>();
        //ArrayList<Entry> meanYVals = new ArrayList<>();
        double[] mean = new double[1000];
        double[] sd = new double[1000];
        for (int i = 0; i < 1000; i++) {
            upperYVals.add(new Entry(i, (float) (k * h_Nmeanstd[i][2*col+1])));//(h_Nmeanstd[i][2*col] + k * h_Nmeanstd[i][2*col+1])));
            //lowerYVals.add(new Entry(i, (float) (h_Nmeanstd[i][2*col] - k * h_Nmeanstd[i][2*col+1])));
            //meanYVals.add(new Entry(i, (float) (h_Nmeanstd[i][2*col])));
            mean[i] = h_Nmeanstd[i][2*col];
            sd[i] = h_Nmeanstd[i][2*col+1];
        }

        // 0 - measurement, >0 - previous
        ArrayList<Entry> measurementYVals = new ArrayList<>();
        if (plot == 0 && xy != null) {
            xy[1] = countAchtung(xy[1], mean, sd);
            for (int i = 0; i < xy[1].length; i++) {
                measurementYVals.add(new Entry(i, (float) (xy[1][i])));
            }
        } else if (plot > 0 && xy_read[plot - 1] != null) {
            xy_read[plot - 1][1] = countAchtung(xy_read[plot - 1][1], mean, sd);
            for (int i = 0; i < xy_read[plot - 1][0].length; i++) {
                measurementYVals.add(new Entry(i, (float) (xy_read[plot - 1][1][i])));
            }
        }

        if (xy != null || xy_read != null){
            //LineDataSet upperDataSet, lowerDataSet, middleDataSet, measurementDataSet;
            LineDataSet upperDataSet, measurementDataSet;

            if (linechart.getData() != null && linechart.getData().getDataSetCount() > 0) {
                measurementDataSet = (LineDataSet) linechart.getData().getDataSetByIndex(1);
                measurementDataSet.setValues(measurementYVals);
                upperDataSet = (LineDataSet) linechart.getData().getDataSetByIndex(0);
                upperDataSet.setValues(upperYVals);
//                lowerDataSet = (LineDataSet) linechart.getData().getDataSetByIndex(1);
//                lowerDataSet.setValues(lowerYVals);
//                middleDataSet = (LineDataSet) linechart.getData().getDataSetByIndex(2);
//                middleDataSet.setValues(meanYVals);
                if (plot == 0){
                    measurementDataSet.setColor(Color.RED);
                    linechart.getDescription().setText("measurement");
                    linechart_filename.setText("measurement");
                } else {
                    measurementDataSet.setColor(Color.BLACK);
                    linechart.getDescription().setText(fileanames[plot-1]);
                    linechart_filename.setText(fileanames[plot-1]);
                }
                linechart.getData().notifyDataChanged();
                linechart.notifyDataSetChanged();
            } else {
                // create a dataset and give it a type
                upperDataSet = new LineDataSet(upperYVals, "");
                upperDataSet.setLineWidth(0);
                upperDataSet.setCircleSize(0);
                upperDataSet.setValueTextSize(0);
                upperDataSet.setDrawCircleHole(false);
                upperDataSet.setDrawFilled(true);
                upperDataSet.setFillAlpha(255);
                upperDataSet.setDrawValues(false);
                upperDataSet.setFillColor(Color.rgb(230, 230, 230));
                upperDataSet.setCircleColor(Color.TRANSPARENT);
                upperDataSet.setColor(Color.TRANSPARENT);
                upperDataSet.setHighLightColor(Color.TRANSPARENT);
                upperDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                upperDataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return 0;
                        //return linechart.getAxisLeft().getAxisMinimum();
                    }
                });

                // create a dataset and give it a type
//                lowerDataSet = new LineDataSet(lowerYVals, "");
//                lowerDataSet.setLineWidth(0);
//                lowerDataSet.setCircleSize(0);
//                lowerDataSet.setValueTextSize(0);
//                lowerDataSet.setDrawCircleHole(false);
//                lowerDataSet.setDrawFilled(true);
//                lowerDataSet.setFillAlpha(255);
//                lowerDataSet.setDrawValues(false);
//                lowerDataSet.setFillColor(Color.rgb(230, 230, 230));
//                lowerDataSet.setCircleColor(Color.TRANSPARENT);
//                lowerDataSet.setColor(Color.TRANSPARENT);
//                lowerDataSet.setHighLightColor(Color.TRANSPARENT);
//                lowerDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//                lowerDataSet.setFillFormatter(new IFillFormatter() {
//                    @Override
//                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                        return 0;
//                        //return linechart.getAxisLeft().getAxisMaximum();
//                    }
//                });

//                middleDataSet = new LineDataSet(meanYVals, "");
//                middleDataSet.setLineWidth(1);
//                middleDataSet.setCircleSize(0);
//                middleDataSet.setValueTextSize(0);
//                middleDataSet.setDrawCircleHole(false);
//                middleDataSet.setDrawFilled(false);
//                middleDataSet.setDrawValues(false);
//                middleDataSet.setCircleColor(Color.TRANSPARENT);
//                middleDataSet.setColor(Color.BLUE);
//                middleDataSet.setHighLightColor(Color.TRANSPARENT);

                measurementDataSet = new LineDataSet(measurementYVals, "");
                measurementDataSet.setLineWidth(3f);
                measurementDataSet.setCircleSize(0);
                measurementDataSet.setValueTextSize(0);
                measurementDataSet.setDrawCircleHole(false);
                measurementDataSet.setDrawFilled(false);
                measurementDataSet.setDrawValues(false);
                measurementDataSet.setCircleColor(Color.TRANSPARENT);
                if (plot == 0){
                    measurementDataSet.setColor(Color.RED);
                    linechart.getDescription().setText("measurement");
                    linechart_filename.setText("measurement");
                } else {
                    measurementDataSet.setColor(Color.BLACK);
                    linechart.getDescription().setText(fileanames[plot-1]);
                    linechart_filename.setText(fileanames[plot-1]);
                }
                measurementDataSet.setHighLightColor(Color.TRANSPARENT);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(upperDataSet);
//                dataSets.add(lowerDataSet);
//                dataSets.add(middleDataSet);
                dataSets.add(measurementDataSet);
                LineData lineData = new LineData(dataSets);

                linechart.setData(lineData);

                Legend l = linechart.getLegend();
                l.setEnabled(false);
                linechart.getDescription().setEnabled(false);
            }
        }
        linechart.invalidate();
    }

    private void plotMeasurement2() {
        //setArrows();

        float k = 1.5f;

        int col; //0 - right, 1 - left, 2 - both
        RadioButton radioButtonLung = (RadioButton) radioGroupLung.findViewById(radioGroupLung.getCheckedRadioButtonId());
        if (radioButtonLung == null) {
            col = 2;
        } else {
            if (String.valueOf(radioButtonLung.getText()).charAt(0) == 'R') {
                col = 0;
            } else {
                col = 1;
            }
        }

        if (h_LBmeanstd == null) h_LBmeanstd = new double[1000][6];
        //if (xy2 == null) xy2 = new double[1000][2];

        ArrayList<Entry> upperYVals = new ArrayList<>();
        ArrayList<Entry> lowerYVals = new ArrayList<>();
        ArrayList<Entry> meanYVals = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            upperYVals.add(new Entry(i, (float) (h_LBmeanstd[i][2*col] + k * h_LBmeanstd[i][2*col+1])));
            lowerYVals.add(new Entry(i, (float) (h_LBmeanstd[i][2*col] - k * h_LBmeanstd[i][2*col+1])));
            meanYVals.add(new Entry(i, (float) (h_LBmeanstd[i][2*col])));
        }

        // int plot: 0 - measurement, >0 - previous
        ArrayList<Entry> measurementYVals = new ArrayList<>();
        if (plot == 0 && xy2 != null) {
            for (int i = 0; i < xy2[1].length; i++) {
                measurementYVals.add(new Entry(i, (float) (xy2[1][i])));
            }
        } else if (plot > 0 && xy_read2[plot - 1] != null) {
            for (int i = 0; i < xy_read2[plot - 1][0].length; i++) {
                measurementYVals.add(new Entry(i, (float) (xy_read2[plot - 1][1][i])));
            }
        }

        if (xy2 != null || xy_read2 != null){
            LineDataSet upperDataSet, lowerDataSet, middleDataSet, measurementDataSet;

            if (linechart2.getData() != null && linechart2.getData().getDataSetCount() > 0) {
                measurementDataSet = (LineDataSet) linechart2.getData().getDataSetByIndex(3);
                measurementDataSet.setValues(measurementYVals);
                upperDataSet = (LineDataSet) linechart2.getData().getDataSetByIndex(0);
                upperDataSet.setValues(upperYVals);
                lowerDataSet = (LineDataSet) linechart2.getData().getDataSetByIndex(1);
                lowerDataSet.setValues(lowerYVals);
                middleDataSet = (LineDataSet) linechart2.getData().getDataSetByIndex(2);
                middleDataSet.setValues(meanYVals);
                if (plot == 0) {
                    measurementDataSet.setColor(Color.RED);
                    linechart2.getDescription().setText("measurement");
                    linechart_filename.setText("measurement");
                } else {
                    measurementDataSet.setColor(Color.BLACK);
                    linechart2.getDescription().setText(fileanames[plot-1]);
                    linechart_filename.setText(fileanames[plot-1]);
                }
                linechart2.getData().notifyDataChanged();
                linechart2.notifyDataSetChanged();

            } else {
                // create a dataset and give it a type
                upperDataSet = new LineDataSet(upperYVals, "");
                upperDataSet.setLineWidth(0);
                upperDataSet.setCircleSize(0);
                upperDataSet.setValueTextSize(0);
                upperDataSet.setDrawCircleHole(false);
                upperDataSet.setDrawFilled(true);
                upperDataSet.setFillAlpha(255);
                upperDataSet.setDrawValues(false);
                upperDataSet.setFillColor(Color.rgb(230, 230, 230));
                upperDataSet.setCircleColor(Color.TRANSPARENT);
                upperDataSet.setColor(Color.TRANSPARENT);
                upperDataSet.setHighLightColor(Color.TRANSPARENT);
                upperDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                upperDataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return 0;
                        //return linechart.getAxisLeft().getAxisMinimum();
                    }
                });

                // create a dataset and give it a type
                lowerDataSet = new LineDataSet(lowerYVals, "");
                lowerDataSet.setLineWidth(0);
                lowerDataSet.setCircleSize(0);
                lowerDataSet.setValueTextSize(0);
                lowerDataSet.setDrawCircleHole(false);
                lowerDataSet.setDrawFilled(true);
                lowerDataSet.setFillAlpha(255);
                lowerDataSet.setDrawValues(false);
                lowerDataSet.setFillColor(Color.rgb(230, 230, 230));
                lowerDataSet.setCircleColor(Color.TRANSPARENT);
                lowerDataSet.setColor(Color.TRANSPARENT);
                lowerDataSet.setHighLightColor(Color.TRANSPARENT);
                lowerDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                lowerDataSet.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return 0;
                        //return linechart.getAxisLeft().getAxisMaximum();
                    }
                });

                middleDataSet = new LineDataSet(meanYVals, "");
                middleDataSet.setLineWidth(1);
                middleDataSet.setCircleSize(0);
                middleDataSet.setValueTextSize(0);
                middleDataSet.setDrawCircleHole(false);
                middleDataSet.setDrawFilled(false);
                middleDataSet.setDrawValues(false);
                middleDataSet.setCircleColor(Color.TRANSPARENT);
                middleDataSet.setColor(Color.BLUE);
                middleDataSet.setHighLightColor(Color.TRANSPARENT);

                measurementDataSet = new LineDataSet(measurementYVals, "");
                measurementDataSet.setLineWidth(3f);
                measurementDataSet.setCircleSize(0);
                measurementDataSet.setValueTextSize(0);
                measurementDataSet.setDrawCircleHole(false);
                measurementDataSet.setDrawFilled(false);
                measurementDataSet.setDrawValues(false);
                measurementDataSet.setCircleColor(Color.TRANSPARENT);
                if (plot == 0) {
                    measurementDataSet.setColor(Color.RED);
                    linechart2.getDescription().setText("measurement");
                    linechart_filename.setText("measurement");
                } else {
                    measurementDataSet.setColor(Color.BLACK);
                    linechart2.getDescription().setText(fileanames[plot-1]);
                    linechart_filename.setText(fileanames[plot-1]);
                }
                measurementDataSet.setHighLightColor(Color.TRANSPARENT);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(upperDataSet);
                dataSets.add(lowerDataSet);
                dataSets.add(middleDataSet);
                dataSets.add(measurementDataSet);
                LineData lineData = new LineData(dataSets);

                linechart2.setData(lineData);

                Legend l = linechart2.getLegend();
                l.setEnabled(false);
                linechart2.getDescription().setEnabled(false);

            }
        }

        linechart2.invalidate();
    }

    private void SaveParamText(String aValue, String aParam) {
        String str = String.format("%s=%s", aParam, aValue);
        SendFrame('P', str);
    }

    private String measurement = "", measurementcont = "", notes = "", birthdate = "", name = "";
    private boolean stop = false;
    private static String filename = "unsavedResults";
    private static List<Map<String, Object>> newUnsaved = new ArrayList<>();

    private void stringToCloud(String s) {
        if (measurement.length() + s.length() < 21000 && measurementcont.isEmpty() && !stop) {
            measurement += s;
        } else {
            if (measurementcont.length() + s.length() < 21000) {
                measurementcont += s;
            } else {
                stop = true;
            }
        }
        /*List<String> parts = new ArrayList<>();
        int length = s.length();
        for (int i = 0; i < length; i += 21000) {
            parts.add(s.substring(i, Math.min(length, i + 21000)));
        }
        String[] m = parts.toArray(new String[0]);*/
    }

    public void initBackendless() {
        Backendless.setUrl(getResources().getString(R.string.SERVER_URL));
        Backendless.initApp(getActivity().getApplicationContext(), getResources().getString(R.string.APPLICATION_ID), getResources().getString(R.string.API_KEY));
    }

    private String peselToString(){
        String PESELstr;
        char[] PESELchar = new char[11];
        if(pesel1.getText().toString().equals("")) PESELchar[0] = ' ';
        else PESELchar[0] = pesel1.getText().charAt(0);
        if(pesel2.getText().toString().equals("")) PESELchar[1] = ' ';
        else PESELchar[1] = pesel2.getText().charAt(0);
        if(pesel3.getText().toString().equals("")) PESELchar[2] = ' ';
        else PESELchar[2] = pesel3.getText().charAt(0);
        if(pesel4.getText().toString().equals("")) PESELchar[3] = ' ';
        else PESELchar[3] = pesel4.getText().charAt(0);
        if(pesel5.getText().toString().equals("")) PESELchar[4] = ' ';
        else PESELchar[4] = pesel5.getText().charAt(0);
        if(pesel6.getText().toString().equals("")) PESELchar[5] = ' ';
        else PESELchar[5] = pesel6.getText().charAt(0);
        if(pesel7.getText().toString().equals("")) PESELchar[6] = ' ';
        else PESELchar[6] = pesel7.getText().charAt(0);
        if(pesel8.getText().toString().equals("")) PESELchar[7] = ' ';
        else PESELchar[7] = pesel8.getText().charAt(0);
        if(pesel9.getText().toString().equals("")) PESELchar[8] = ' ';
        else PESELchar[8] = pesel9.getText().charAt(0);
        if(pesel10.getText().toString().equals("")) PESELchar[9] = ' ';
        else PESELchar[9] = pesel10.getText().charAt(0);
        if(pesel11.getText().toString().equals("")) PESELchar[10] = ' ';
        else PESELchar[10] = pesel11.getText().charAt(0);
        return new String(PESELchar);
    }

    private Map<String, Object> getCandidate() {
        Map<String, Object> oneCandidate = new HashMap();
        Date newbirthdate = null;
        try {
            newbirthdate = new SimpleDateFormat("dd/MM/yyyy").parse(getBirthdateFromPesel());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        oneCandidate.put("age", newbirthdate);
        oneCandidate.put("achtung", achtung);
        oneCandidate.put("id", peselToString());
        oneCandidate.put("DateMeasurement", dateRun);
        oneCandidate.put("hardware", hardware);
        oneCandidate.put("diagnosis", String.valueOf(jspinner.getSelectedItem()));
        RadioButton radioButtonPleeff = (RadioButton) radioGroupPleeff.findViewById(radioGroupPleeff.getCheckedRadioButtonId());
        if (radioButtonPleeff != null){
            if (radioButtonPleeff.getTag() == "radioButtonPEYes")
                oneCandidate.put("Healthy", false);
            else
                oneCandidate.put("Healthy", true);
        }
        oneCandidate.put("height", heightTxt.getEditableText().toString());
        RadioButton radioButtonLung = (RadioButton) radioGroupLung.findViewById(radioGroupLung.getCheckedRadioButtonId());
        if (radioButtonLung == null) {
            oneCandidate.put("Lung", "");
        } else {
            oneCandidate.put("Lung", String.valueOf(radioButtonLung.getText()).charAt(0));
        }
        oneCandidate.put("measurement", measurement);
        oneCandidate.put("measurementcont", measurementcont);
        if(mood < 4) oneCandidate.put("mood", mood);
        notes = notesTxt.getEditableText().toString();
        oneCandidate.put("notes", notes);
        RadioButton radioButtonSex = (RadioButton) radioGroupSex.findViewById(radioGroupSex.getCheckedRadioButtonId());
        if (radioButtonSex == null) {
            oneCandidate.put("Sex", "");
        } else {
            oneCandidate.put("Sex", String.valueOf(radioButtonSex.getText()).charAt(0));
        }
        oneCandidate.put("software", software);
        RadioButton radioButtonThoraco = (RadioButton) radioGroupThoraco.findViewById(radioGroupThoraco.getCheckedRadioButtonId());
        if (radioButtonThoraco != null){
            if (radioButtonThoraco.getTag() == "radioButtonThoracoYes")
                oneCandidate.put("Thoracocentesis", true);
            else
                oneCandidate.put("Thoracocentesis", false);
        }
        oneCandidate.put("weight", weightTxt.getEditableText().toString());
        oneCandidate.put("name", filenameText.getEditableText().toString());
        return oneCandidate;
    }

    private boolean peselValidator() {
        int sum = 1 * PESEL[0] +
                3 * PESEL[1] +
                7 * PESEL[2] +
                9 * PESEL[3] +
                1 * PESEL[4] +
                3 * PESEL[5] +
                7 * PESEL[6] +
                9 * PESEL[7] +
                1 * PESEL[8] +
                3 * PESEL[9];
        sum %= 10;
        sum = 10 - sum;
        sum %= 10;

        if (sum == PESEL[10]) {
            return true;
        }
        else {
            return false;
        }
    }

    private String getBirthdateFromPesel(){
        String birthdatePesel = "";
        if (!(pesel1.getText().toString().equals("") || pesel2.getText().toString().equals("") ||
                pesel3.getText().toString().equals("") || pesel4.getText().toString().equals("") ||
                pesel5.getText().toString().equals("") || pesel6.getText().toString().equals(""))){
            int year;
            int month;
            int day;
            year = 10 * Integer.valueOf(pesel1.getText().toString());
            year += Integer.valueOf(pesel2.getText().toString());
            month = 10 * Integer.valueOf(pesel3.getText().toString());
            month += Integer.valueOf(pesel4.getText().toString());
            if (month > 80 && month < 93) {
                year += 1800;
                month -= 80;
            }
            else if (month > 0 && month < 13) {
                year += 1900;
            }
            else if (month > 20 && month < 33) {
                year += 2000;
                month -= 20;
            }
            else if (month > 40 && month < 53) {
                year += 2100;
                month -= 40;
            }
            else if (month > 60 && month < 73) {
                year += 2200;
                month -= 60;
            }
            day = 10 * Integer.valueOf(pesel5.getText().toString());
            day += Integer.valueOf(pesel6.getText().toString());
            birthdatePesel = Integer.toString(day) + "/" + Integer.toString(month) + "/" + Integer.toString(year);
        }

        return birthdatePesel;
    }

    private void offlineSaving(List<Map<String, Object>> yourObject) {
        FileOutputStream stream = null;
        try {
            stream = context.openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream dout = new ObjectOutputStream(stream);
            dout.reset();
            dout.writeObject(yourObject);
            dout.flush();
            stream.getFD().sync();
            stream.close();
            System.out.println("ice saving");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> getUnsaved() {
        List<Map<String, Object>> readBack = null;
        FileInputStream stream = null;
        try {
            stream = context.openFileInput(filename);
            ObjectInputStream din = new ObjectInputStream(stream);
            readBack = (List<Map<String, Object>>) din.readObject();
            offlineSaving(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return readBack;
    }

    private void saveToCloud(final Map<String, Object> object, final boolean last) {

        System.out.println("saveToCloud");
        Backendless.Data.of("LABdata").save(object, new AsyncCallback<Map>() {
            public void handleResponse(Map response) {
                System.out.println("wysłane");
            }

            public void handleFault(BackendlessFault fault) {
                System.out.println("nie wysłane");
                System.out.println(fault.getMessage());
                newUnsaved.add(object);
                if (last) {
                    offlineSaving(newUnsaved);
                    newUnsaved.clear();
                }
            }
        });

    }

    private void allToCloud() {
        initBackendless();
        List<Map<String, Object>> unsavedToCloud = getUnsaved();
        Map<String, Object> tempcan = getCandidate();
        if (unsavedToCloud == null) {
            unsavedToCloud = new ArrayList<>();
            unsavedToCloud.add(tempcan);
        } else {
            unsavedToCloud.add(tempcan);
            //System.out.println(unsavedToCloud.get(0).get(0));
        }
        for (int i = 0; i < unsavedToCloud.size(); i++) {
            System.out.println("element");
            boolean last = false;
            if (i == unsavedToCloud.size() - 1) {
                last = true;
            }
            saveToCloud(unsavedToCloud.get(i), last);
        }
    }

    private void allToCloudBeginning() {
        initBackendless();
        List<Map<String, Object>> unsavedToCloud = getUnsaved();

        if (unsavedToCloud != null) {
            for (int i = 0; i < unsavedToCloud.size(); i++) {
                System.out.println("element");
                boolean last = false;
                if (i == unsavedToCloud.size() - 1) {
                    last = true;
                }
                saveToCloud(unsavedToCloud.get(i), last);
            }
        }
    }

    @TargetApi(23)
    private boolean verifyPermissionStorage() {
        if (!(ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED)) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    @TargetApi(23)
    private boolean verifyPermissionStorageRead() {
        if (!(ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED)) {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 2);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 1 || requestCode != 2) return;

        if (!(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) && requestCode == 1) {
            Toast.makeText(getActivity(), R.string.storage_permission_toast, Toast.LENGTH_LONG).show();
        }
        if (!(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) && requestCode == 2) {
            Toast.makeText(getActivity(), R.string.storage_permission_read_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void splitString() {
        v0.clear();
        v2.clear();
        ArrayList aList = new ArrayList(asList(strAll.split("\n")));
        for (int i = 0; i < aList.size(); i++) {
            ArrayList commaList = new ArrayList(asList(aList.get(i).toString().split(",")));
            System.out.println(commaList.toString());
            if (commaList.size() > 3) {
                if (Integer.valueOf(commaList.get(2).toString()) != 0 && Integer.valueOf(commaList.get(4).toString()) != 0){
                    v0.add(commaList.get(2));
                    v2.add(commaList.get(4));
                }
            }
        }
    }

    private double[][] countData_Plot(Vector v_daleki, Vector v_bliski) {

        /*int[] v_tes_bliski = {0,
                0,
                3111,
                3105,
                3098,
                3092,
                3085,
                3076,
                3050,
                3019,
                2979,
                2958,
                2958,
                2960,
                2951,
                2922,
                2916,
                2890,
                2897,
                2892,
                2850,
                2856,
                2858,
                2813,
                2769,
                2785,
                2756,
                2731,
                2719,
                2724,
                2755,
                2745,
                2647,
                2632,
                2655,
                2632,
                2636,
                2668,
                2676,
                2652,
                2608,
                2600,
                2605,
                2589,
                2533,
                2550,
                2536,
                2571,
                2555,
                2560,
                2544,
                2484,
                2504,
                2523,
                2541,
                2508,
                2487,
                2479,
                2429,
                2463,
                2471,
                2367,
                2513,
                2436,
                2444,
                2475,
                2484,
                2475,
                2505,
                2533,
                2502,
                2491,
                2536,
                2549,
                2487,
                2508,
                2466,
                2487,
                2505,
                2510,
                2534,
                2518,
                2510,
                2536,
                2582,
                2531,
                2568,
                2545,
                2495,
                2449,
                2481,
                2491,
                2508,
                2478,
                2449,
                2468,
                2470,
                2487,
                2445,
                2449,
                2478,
                2499,
                2487,
                2470,
                2441,
                2426,
                2389,
                2326,
                2347,
                2379,
                2383,
                2397,
                2413,
                2484,
                2499,
                2457,
                2454,
                2431,
                2454,
                2423,
                2445,
                2405,
                2365,
                2400,
                2376,
                2331,
                2333,
                2339,
                2334,
                2329,
                2346,
                2371,
                2357,
                2354,
                2370,
                2358,
                2334,
                2326,
                2321,
                2318};
        Vector v_bliski_t = new Vector();
        for (int i = 0; i < v_tes_bliski.length; i++) {
            v_bliski_t.add(v_tes_bliski[i]);
        }
        Vector<Vector> pom2 = normalize(v_bliski_t);
        int[] v_tes_daleki = {0,
                0,
                2708,
                2698,
                2689,
                2678,
                2673,
                2632,
                2574,
                2489,
                2412,
                2312,
                2157,
                2117,
                2064,
                1985,
                1853,
                1815,
                1840,
                1870,
                1862,
                1790,
                1679,
                1643,
                1595,
                1585,
                1522,
                1488,
                1479,
                1501,
                1504,
                1437,
                1343,
                1297,
                1272,
                1245,
                1184,
                1148,
                1152,
                1100,
                1090,
                1105,
                1119,
                1097,
                1076,
                1048,
                1074,
                1050,
                1040,
                1045,
                1019,
                997,
                976,
                968,
                950,
                926,
                903,
                878,
                850,
                837,
                787,
                750,
                739,
                739,
                742,
                728,
                734,
                734,
                734,
                741,
                734,
                747,
                762,
                728,
                734,
                749,
                773,
                779,
                787,
                792,
                792,
                815,
                816,
                847,
                876,
                894,
                931,
                952,
                957,
                958,
                965,
                970,
                982,
                995,
                1029,
                1028,
                1063,
                1106,
                1150,
                1184,
                1290,
                1314,
                1324,
                1332,
                1377,
                1405,
                1382,
                1353,
                1334,
                1340,
                1277,
                1240,
                1200,
                1184,
                1152,
                1087,
                1032,
                984,
                958,
                934,
                950,
                934,
                889,
                871,
                847,
                816,
                799,
                797,
                791,
                778,
                755,
                750,
                749,
                754,
                752,
                741,
                728,
                728,
                726,
                731};
        Vector v_daleki_t = new Vector();
        for (int i = 0; i < v_tes_daleki.length; i++) {
            v_daleki_t.add(v_tes_daleki[i]);
        }
        Vector<Vector> pom1 = normalize(v_daleki_t);*/

        Vector pom1 = normalize(v_daleki);
        Vector pom2 = normalize(v_bliski);
        double uplim = 0.9999;
        double lowlim = 0.00001;

        double[][] xy_temp = distpor(pom1, pom2, lowlim, uplim);
        xy_temp = leastsquares(xy_temp[0], xy_temp[1]);

        xy_temp[1] = sub_1st_el(xy_temp[1]);
        //xy_temp[1] = sub_mean_numel(xy_temp[1], 80);

        xy_temp[0] = linspaceD(1, 1000, 1000);

        return xy_temp;
    }

    private double[][] countData_Plot2(Vector v_daleki, Vector v_bliski){
        double I0daleki = 0.0029468 * Math.pow(10, -6);
        double I0bliski = 0.75009 * Math.pow(10, -6);
        double[] lb = new double[v_daleki.size()];
        for (int i = 0; i < v_daleki.size(); i++){
            double Ibliski = Double.valueOf(v_bliski.get(i).toString()) / 2932993.23671;
            double Idaleki = Double.valueOf(v_daleki.get(i).toString()) / 746580096.61836;
            double Fdaleki = Math.log10(Idaleki/I0daleki);
            double Fbliski = Math.log10(Ibliski/I0bliski);
            lb[i] = Fdaleki/Fbliski;
        }

        double[][] xyLB = new double[1000][2];
        xyLB[0] = linspaceD(0,1, 1000);
        double[] interplambeer = interpLinear(linspaceD(0,1,v_bliski.size()), lb, xyLB[0]);
        xyLB[1] = sub_mean_numel(interplambeer, 80);
        return xyLB;
    }

    private double[][] distpor(Vector<Vector> r1, Vector<Vector> run2, double lowerl, double upperl) {
        double[][] x1y2 = new double[2][1000];

        double sum1 = 0;
        double sum2 = 0;
        int n = r1.get(1).size();
        int n2 = run2.get(1).size();

        double mx1 = Double.valueOf(Collections.max(r1.get(1)).toString());
        double mx2 = Double.valueOf(Collections.max(run2.get(1)).toString());

        Vector raw1x = new Vector();
        Vector raw1y = new Vector();
        Vector raw2x = new Vector();
        Vector raw2y = new Vector();

        int k = 0;
        for (int i = 0; i < n; i++) {
            if (Double.valueOf(r1.get(1).get(i).toString()) >= mx1 / 1000) {
                k = k + 1;
                raw1x.add(Double.valueOf(r1.get(0).get(i).toString()));
                raw1y.add(Double.valueOf(r1.get(1).get(i).toString()));
            }
        }
        int ile1 = k;
        k = 0;
        for (int i = 0; i < n2; i++) {
            if (Double.valueOf(run2.get(1).get(i).toString()) >= mx2 / 1000) {
                k = k + 1;
                raw2x.add(Double.valueOf(run2.get(0).get(i).toString()));
                raw2y.add(Double.valueOf(run2.get(1).get(i).toString()));
            }
        }
        int ile2 = k;

        if (ile1 != 0 && ile2 != 0 && mx1 != 0 && mx2 != 0) {
            for (int i = 0; i < ile1; i++) {
                sum1 = sum1 + Double.valueOf(raw1y.get(i).toString());
            }
            for (int i = 0; i < ile2; i++) {
                sum2 = sum2 + Double.valueOf(raw2y.get(i).toString());
            }

            Vector dyst1x = (Vector) raw1x.clone();
            dyst1x.add(0, 0);
            Vector dyst1y = (Vector) raw1y.clone();
            dyst1y.add(0, 0);
            Vector dyst2x = (Vector) raw2x.clone();
            dyst2x.add(0, 0);
            Vector dyst2y = (Vector) raw2y.clone();
            dyst2y.add(0, 0);

            // normowanie
            for (int i = 1; i < ile1 + 1; i++) {
                dyst1y.set(i, Double.valueOf(dyst1y.get(i - 1).toString()) + Double.valueOf(raw1y.get(i - 1).toString()));
            }
            for (int i = 1; i < ile2 + 1; i++) {
                dyst2y.set(i, Double.valueOf(dyst2y.get(i - 1).toString()) + Double.valueOf(raw2y.get(i - 1).toString()));
            }
            double[] dyst1ya = new double[dyst1y.size()];
            for (int i = 0; i < dyst1y.size(); i++) {
                //dyst1y.set(i, Double.valueOf(dyst1y.get(i).toString())/sum1);
                dyst1ya[i] = Double.valueOf(dyst1y.get(i).toString()) / sum1;
            }
            double[] dyst2ya = new double[dyst2y.size()];
            for (int i = 0; i < dyst2y.size(); i++) {
                //dyst2y.set(i, Double.valueOf(dyst2y.get(i).toString())/sum2);
                dyst2ya[i] = Double.valueOf(dyst2y.get(i).toString()) / sum2;
            }

            double[] y1 = linspaceD(lowerl, upperl, 1000);
            double[] dyst1xa = new double[dyst1x.size()];
            for (int i = 0; i < dyst1x.size(); i++) {
                dyst1xa[i] = Double.valueOf(dyst1x.get(i).toString());
            }
            double[] x1 = interpLinear(dyst1ya, dyst1xa, y1);
            x1y2[0] = x1;
            double[] dyst2xa = new double[dyst2x.size()];
            for (int i = 0; i < dyst2x.size(); i++) {
                dyst2xa[i] = Double.valueOf(dyst2x.get(i).toString());
            }
            double[] y2 = interpLinear(dyst2ya, dyst2xa, y1);
            x1y2[1] = y2;

        } else {
            double[] x1 = new double[1000];
            double[] y2 = new double[1000];
            x1y2[0] = x1;
            x1y2[1] = y2;
        }

        return x1y2;
    }

    private double[][] leastsquares(double[] xx, double[] yy) {
        double n = xx.length;
        double sumxx = 0;
        double sumyy = 0;
        double sumxy = 0;
        double sumxx2 = 0;
        for (int i = 0; i < n; i++) {
            sumxx += xx[i];
            sumyy += yy[i];
            sumxy += xx[i] * yy[i];
            sumxx2 += xx[i] * xx[i];
        }

        double wspol = sumxx / n;
        double a = (sumxy - wspol * sumyy) / (sumxx2 - wspol * sumxx);
        double b = sumyy / n - wspol * a;

        for (int i = 0; i < n; i++) {
            yy[i] = yy[i] - a * xx[i] - b;
        }

        double[][] xxyy = new double[2][1000];
        xxyy[0] = xx;
        xxyy[1] = yy;
        return xxyy;
    }

    private double[] sub_1st_el(double[] y2) {
        double y0 = y2[0];
        for (int i = 0; i < y2.length; i++) {
            y2[i] = y2[i] - y0;
        }

        return y2;
        /*if (hardware.equals("H01")){ //4.5 cm
            h_mean = new double[]{0, 0, 0, 0, -8.22759635253079e-06, -4.96674663453040e-05, -0.000122567944338318, -0.000105581365678557, -5.06261616076247e-05, -3.49519923446818e-07, 7.20084070206496e-05, 0.000176907848128692, 0.000203792937714990, 0.000225536828109948, 0.000246087417589410, 0.000268830329685025, 0.000292103121735514, 0.000316481113899351, 0.000343262643989467, 0.000370436585104351, 0.000398854700623821, 0.000424612514422329, 0.000448250641801798, 0.000465072085610700, 0.000481253056679553, 0.000498729472778979, 0.000516406063575947, 0.000536755245962624, 0.000557784816664299, 0.000578265317611169, 0.000601745258938086, 0.000629056478491207, 0.000656995032649254, 0.000684158388573724, 0.000709781847072909, 0.000736303474729443, 0.000764496022039314, 0.000789979910836374, 0.000812984476256111, 0.000835392067186745, 0.000859125787540081, 0.000882864092742619, 0.000905967456465256, 0.000928816002346340, 0.000957413170439799, 0.000987761826535215, 0.00101844358464028, 0.00104800042864241, 0.00107949088862785, 0.00111256497076178, 0.00114635966775080, 0.00117968113925321, 0.00120887000787823, 0.00124191740061539, 0.00127632808104415, 0.00130942524062375, 0.00134185158771731, 0.00137541476743258, 0.00141182434790737, 0.00144835272001606, 0.00148426931445687, 0.00151781208743885, 0.00155243785472241, 0.00158808297122730, 0.00162389582514651, 0.00165988372999694, 0.00169644229418509, 0.00173478638959168, 0.00177597243179392, 0.00182138514236981, 0.00186438584250038, 0.00190718091628591, 0.00194860963120730, 0.00198882512287588, 0.00202841403195529, 0.00207001359259043, 0.00211361978492134, 0.00215839632656584, 0.00220197813290081, 0.00224608865988774, 0.00229100405996358, 0.00233829061045179, 0.00238735202415643, 0.00243612045096324, 0.00248402945016053, 0.00253085303550886, 0.00257761411171352, 0.00262759121925027, 0.00267904369577440, 0.00273174074231694, 0.00278574622105189, 0.00283929223621702, 0.00289349427814198, 0.00294879312699682, 0.00300455800732945, 0.00305902359750826, 0.00311349632926854, 0.00316961402577326, 0.00322512498482010, 0.00327909289161381, 0.00333574597423727, 0.00339188402411370, 0.00345146895758812, 0.00351391844233409, 0.00357986080125286, 0.00364483518015215, 0.00370840447192513, 0.00377047513651813, 0.00382936455211539, 0.00388798775452994, 0.00394564429254273, 0.00399843854784702, 0.00404996897785582, 0.00410271977558737, 0.00415353881790736, 0.00420954321919471, 0.00426707529679747, 0.00432233859896555, 0.00437695936972318, 0.00443122471169052, 0.00448993489481772, 0.00454910319991406, 0.00460969713906512, 0.00466642682546952, 0.00472434129812416, 0.00478332534200693, 0.00484737282071652, 0.00491402332082891, 0.00497996270043801, 0.00504333097219201, 0.00510664549307150, 0.00516724399552008, 0.00522561540686128, 0.00528453328637027, 0.00534653697306568, 0.00540800127410148, 0.00546965973976194, 0.00552936296962851, 0.00558623660141665, 0.00564102685372115, 0.00569452567313033, 0.00574807020146516, 0.00580304183091621, 0.00585875776303997, 0.00591456335985087, 0.00597083097802208, 0.00602827785232095, 0.00608385163551368, 0.00613701214837643, 0.00619044100003405, 0.00624585070553715, 0.00630115047998990, 0.00635424225574177, 0.00640542595310532, 0.00645400737685269, 0.00650354577786484, 0.00655602522870856, 0.00660836343729044, 0.00666332917876031, 0.00671926271435812, 0.00677838122063723, 0.00684117682766075, 0.00690328488456168, 0.00696364337631003, 0.00702127059044112, 0.00708135424747655, 0.00713908682042758, 0.00719271102567301, 0.00724817898029029, 0.00730494688638082, 0.00736077997023191, 0.00741380152844788, 0.00746413819957250, 0.00751443708461203, 0.00756214237991410, 0.00761020960329509, 0.00765918751039497, 0.00770473504637167, 0.00774779895282586, 0.00779213463801166, 0.00783701275480627, 0.00788084803436838, 0.00792190972820443, 0.00796196719466676, 0.00800192097345286, 0.00804008163260737, 0.00807750743615905, 0.00811238270898583, 0.00814808879267142, 0.00818324115188498, 0.00821800399876990, 0.00825271208966909, 0.00828560819122947, 0.00831713327123448, 0.00835259322545806, 0.00838675636717649, 0.00842112609468882, 0.00845179371802423, 0.00847886730479455, 0.00850427285409298, 0.00852754344243721, 0.00855215196627385, 0.00857706364135720, 0.00860447386459799, 0.00863280302543203, 0.00866135980212850, 0.00868565757529125, 0.00870899798994455, 0.00873239767816430, 0.00875797313309279, 0.00878462823451202, 0.00881091370068563, 0.00884140721536408, 0.00886970077001499, 0.00889491907698693, 0.00892069167261754, 0.00894900050268248, 0.00898040942540055, 0.00901431428886247, 0.00904966077703126, 0.00908468338754963, 0.00911696906175108, 0.00914959145783117, 0.00918266780638436, 0.00921455749381703, 0.00924447920400387, 0.00927553668344654, 0.00930810305393730, 0.00934376580083924, 0.00938143921658294, 0.00942265275852496, 0.00946535586782926, 0.00950713698460434, 0.00954208902123409, 0.00957538650577242, 0.00960476737353018, 0.00963370499436652, 0.00966402290995357, 0.00969380833785105, 0.00972405766800404, 0.00975518671816557, 0.00978684476537730, 0.00981811017983722, 0.00984871366024403, 0.00987935872405124, 0.00990978950240428, 0.00993690646067243, 0.00996264302464282, 0.00998932152863291, 0.0100211164456731, 0.0100536314457109, 0.0100854970162177, 0.0101202335357751, 0.0101560372214276, 0.0101948826977801, 0.0102369786942187, 0.0102806024559683, 0.0103229996720528, 0.0103607965799717, 0.0104004105175929, 0.0104360509167435, 0.0104708850806352, 0.0105097969081539, 0.0105486257374416, 0.0105863408565796, 0.0106227093420409, 0.0106609537822840, 0.0107009977452624, 0.0107379816392284, 0.0107727921123653, 0.0108091417089807, 0.0108446627883859, 0.0108879880058009, 0.0109285233041620, 0.0109678955641904, 0.0110012465224938, 0.0110305820271712, 0.0110589232546455, 0.0110861917464436, 0.0111121766356855, 0.0111484453718362, 0.0111867593288763, 0.0112229890098168, 0.0112586162844141, 0.0112907431907046, 0.0113236090356333, 0.0113515255787267, 0.0113811331263955, 0.0114144677091618, 0.0114487231931229, 0.0114832473295157, 0.0115224205028016, 0.0115599088115459, 0.0115969644086965, 0.0116387493705039, 0.0116800788532195, 0.0117198337875986, 0.0117526772541934, 0.0117909689681987, 0.0118311206338485, 0.0118711246110092, 0.0119092867497071, 0.0119535945339512, 0.0119910928910370, 0.0120290581714717, 0.0120673643039646, 0.0121036276053802, 0.0121301044529843, 0.0121525946656580, 0.0121751734534785, 0.0122000118415548, 0.0122213785963959, 0.0122491670631213, 0.0122745910243298, 0.0122940629388070, 0.0123131413499434, 0.0123291917421001, 0.0123429658356023, 0.0123583153154663, 0.0123735438483845, 0.0123836180258702, 0.0123901182457668, 0.0123977572200541, 0.0124069825593363, 0.0124162050264622, 0.0124233575506582, 0.0124311046096156, 0.0124415003724722, 0.0124488358140006, 0.0124543548430186, 0.0124580619908462, 0.0124603320749696, 0.0124612887775422, 0.0124606011149357, 0.0124632660171573, 0.0124665560146880, 0.0124649983144455, 0.0124632985751618, 0.0124631478574025, 0.0124628610824809, 0.0124627909939204, 0.0124619140876259, 0.0124581113851750, 0.0124500203927258, 0.0124426261115908, 0.0124352491804726, 0.0124284638758300, 0.0124239628285044, 0.0124243791034385, 0.0124248608147257, 0.0124248996172227, 0.0124218708652192, 0.0124181113009625, 0.0124091752905520, 0.0123970943205914, 0.0123910296486239, 0.0123854148434136, 0.0123807393947847, 0.0123779171352414, 0.0123717926964030, 0.0123618406397780, 0.0123542578948566, 0.0123483107922611, 0.0123452910152162, 0.0123380375668275, 0.0123306546211307, 0.0123240865066590, 0.0123166987205782, 0.0123193100493661, 0.0123224026230222, 0.0123241538684505, 0.0123234779477254, 0.0123235088304328, 0.0123194266831171, 0.0123136053148622, 0.0123103314763926, 0.0123090413059346, 0.0123066158517605, 0.0123027100541351, 0.0123019317656320, 0.0122975106724851, 0.0122847696217504, 0.0122723446414526, 0.0122578834982728, 0.0122430201486135, 0.0122444566360299, 0.0122430473254108, 0.0122370131800157, 0.0122289823874685, 0.0122193893326819, 0.0122078951262848, 0.0121962151776884, 0.0121843146110781, 0.0121785328612609, 0.0121741979519577, 0.0121742367103571, 0.0121737538534464, 0.0121710771978845, 0.0121673933640578, 0.0121621047123471, 0.0121540095011755, 0.0121411017236061, 0.0121264824684435, 0.0121109925054620, 0.0120967143192468, 0.0120842826201211, 0.0120705738303702, 0.0120542754667230, 0.0120324857744811, 0.0120037543287524, 0.0119696137723595, 0.0119354810729950, 0.0119005814061835, 0.0118747801214599, 0.0118543105654924, 0.0118341898568528, 0.0118157055324245, 0.0117980818857552, 0.0117824360977974, 0.0117666007029721, 0.0117468952760781, 0.0117266024405161, 0.0117064277758067, 0.0116863332683780, 0.0116692156754370, 0.0116493762307717, 0.0116270532799578, 0.0116046615194892, 0.0115816829962153, 0.0115606518482603, 0.0115430575651485, 0.0115265629391957, 0.0115105079867408, 0.0114923229610511, 0.0114686190839699, 0.0114423099416891, 0.0114128323404716, 0.0113810324259194, 0.0113471805549033, 0.0113128150451612, 0.0112819669274540, 0.0112571646792847, 0.0112291212934128, 0.0112019159431923, 0.0111729979244516, 0.0111414706823658, 0.0111123149784462, 0.0110787840665933, 0.0110460136920107, 0.0110147917549108, 0.0109830467287102, 0.0109541475137371, 0.0109270428864629, 0.0109025877402406, 0.0108826890722798, 0.0108580835604927, 0.0108364486608715, 0.0108197902296157, 0.0108083462713777, 0.0108001667672817, 0.0107958919458383, 0.0107922285466778, 0.0107837318339212, 0.0107731020740577, 0.0107629544349708, 0.0107449117091025, 0.0107232081036317, 0.0106997301200077, 0.0106758142731694, 0.0106608420582494, 0.0106529732370484, 0.0106414864524554, 0.0106320072116784, 0.0106238852803897, 0.0106114498794381, 0.0105911657272905, 0.0105717883396874, 0.0105518162050489, 0.0105322640171769, 0.0105163270897919, 0.0105018008559551, 0.0104816164428349, 0.0104642577605465, 0.0104516543167424, 0.0104391887496612, 0.0104240165439775, 0.0104081699879339, 0.0103896035249502, 0.0103721532257209, 0.0103599911543208, 0.0103506318091358, 0.0103491132511727, 0.0103521152752842, 0.0103511189778062, 0.0103418047804416, 0.0103263758296562, 0.0103144558044446, 0.0103045130796584, 0.0102963484831881, 0.0102890653214595, 0.0102848235650273, 0.0102765911092477, 0.0102677307868208, 0.0102600748983924, 0.0102505065488361, 0.0102395587003835, 0.0102300701698515, 0.0102203345951990, 0.0102055390990005, 0.0101832274991637, 0.0101568216237236, 0.0101292604722891, 0.0101061642670801, 0.0100943545699681, 0.0100845974436188, 0.0100744645422817, 0.0100658323016587, 0.0100601082998762, 0.0100450005521858, 0.0100299029255490, 0.0100121450571155, 0.00998554785927657, 0.00995844441926749, 0.00993771113572217, 0.00991659826641670, 0.00989640476107057, 0.00987597088142580, 0.00985153434223623, 0.00981845860466641, 0.00978563015538630, 0.00975168675453487, 0.00971825810777144, 0.00969592004201544, 0.00967652691848347, 0.00965763477098192, 0.00963912379222453, 0.00961506130223115, 0.00958389200311873, 0.00955203955237057, 0.00952016159948243, 0.00948927966251446, 0.00946803587686629, 0.00944218755882097, 0.00941514524735872, 0.00939033906764922, 0.00936213707121497, 0.00933360526930106, 0.00930699181031804, 0.00928403412558027, 0.00926414135288045, 0.00924426562713988, 0.00922266082859086, 0.00919886321784919, 0.00917278027940956, 0.00914876316944441, 0.00912523940233022, 0.00909879046021991, 0.00907460998863432, 0.00905238817429989, 0.00902800825749328, 0.00900088736257052, 0.00897459790300827, 0.00894407947907734, 0.00891329524835878, 0.00888230029461192, 0.00884999446693647, 0.00881412694025457, 0.00877693189158714, 0.00873729941708053, 0.00870146949607784, 0.00866866949914043, 0.00863639698234568, 0.00860451463183584, 0.00857226750266712, 0.00853870409264674, 0.00851036688725821, 0.00848331860839892, 0.00845079164793777, 0.00841983175650867, 0.00839301136834986, 0.00836316310633654, 0.00833326874474325, 0.00830354823796466, 0.00827171232761090, 0.00824033867501951, 0.00820996309878972, 0.00817711469732467, 0.00814735764336966, 0.00811706266706909, 0.00808585807048447, 0.00805570489578604, 0.00802964359598768, 0.00800645963839155, 0.00798406302074428, 0.00796106783377304, 0.00793800056065170, 0.00791351183856666, 0.00788978662797010, 0.00786733400465991, 0.00783890855639087, 0.00780688013247862, 0.00777062851941963, 0.00773083490235479, 0.00769306548742642, 0.00765404929535761, 0.00761851865899617, 0.00758393384609778, 0.00754690592323585, 0.00750437947017253, 0.00746049612372924, 0.00741596760616370, 0.00737634215717594, 0.00733860698328833, 0.00730369019331453, 0.00726875379054274, 0.00723019310173825, 0.00718843628583037, 0.00715088392742435, 0.00711993452141262, 0.00709565213368724, 0.00707290080330082, 0.00704937749287387, 0.00702709308226303, 0.00699822685723827, 0.00696417167740162, 0.00692808911610162, 0.00689413169441671, 0.00687219646562345, 0.00685880451632655, 0.00684500701586601, 0.00683081359041398, 0.00681188248124797, 0.00678770692511487, 0.00676254248495928, 0.00673799673573932, 0.00672411250895099, 0.00671529272896150, 0.00670621452668169, 0.00669784777039060, 0.00669003109318788, 0.00667905602630050, 0.00666342225313836, 0.00664435211753043, 0.00662705928820328, 0.00661439481969775, 0.00659961921820819, 0.00658296418773884, 0.00656863010582817, 0.00655732951187132, 0.00654954913677286, 0.00654114157512206, 0.00652994992150913, 0.00651837538368483, 0.00650679505914585, 0.00649914418466730, 0.00649385056831945, 0.00648703088799182, 0.00648682932303987, 0.00648833968545077, 0.00648722405094099, 0.00648806064363136, 0.00649421991846133, 0.00650141115628851, 0.00651096820899110, 0.00652089629806083, 0.00653085981239432, 0.00654030673339450, 0.00654988903414311, 0.00656438780203746, 0.00657839875976126, 0.00659174580236778, 0.00659435498447051, 0.00659645392735584, 0.00659888950024591, 0.00660010452245207, 0.00660552006122828, 0.00661386790977440, 0.00662508758454037, 0.00664147648774345, 0.00666240473070515, 0.00668411019585974, 0.00670571112545881, 0.00672546235055713, 0.00674426274258834, 0.00676326330325220, 0.00677736980741802, 0.00678894375706265, 0.00680001297166394, 0.00681203192513450, 0.00682271638803983, 0.00683411674296122, 0.00684358811361625, 0.00685366767317480, 0.00686512849908419, 0.00687958940526048, 0.00688437250394017, 0.00688739368281490, 0.00688898270058775, 0.00689148527196591, 0.00689732737280913, 0.00690566963237925, 0.00691441464420680, 0.00692590772786209, 0.00694192260383061, 0.00695052499201002, 0.00695343848878684, 0.00695421423578845, 0.00695402795528004, 0.00695187995691711, 0.00695493933497011, 0.00695939360947012, 0.00696444535696067, 0.00697238329412130, 0.00698776665360518, 0.00699971447994841, 0.00701352927238749, 0.00702656511777025, 0.00704126930788066, 0.00705587889111990, 0.00706607023077593, 0.00707380819726743, 0.00708115264527104, 0.00708987496246765, 0.00709871951086101, 0.00710764075752881, 0.00711649547473981, 0.00712605117155772, 0.00713518926691054, 0.00714360880194533, 0.00715367415661802, 0.00716566750169739, 0.00718426730058969, 0.00720514556159649, 0.00722442493299963, 0.00724011803044204, 0.00725616428808958, 0.00727011052020476, 0.00727954364317300, 0.00728733451651008, 0.00729050328730811, 0.00729495840480087, 0.00730343044564029, 0.00730792182427630, 0.00730530619777642, 0.00730095281688394, 0.00730019506156178, 0.00729629703650514, 0.00729495996007011, 0.00730333990862812, 0.00731023561414321, 0.00731713945234824, 0.00732750982549889, 0.00733889534364058, 0.00734574829514584, 0.00735203407605884, 0.00736201310575279, 0.00737451943379896, 0.00738816197092778, 0.00740758107883025, 0.00742663118489329, 0.00744360972746352, 0.00746458556361913, 0.00748281110113973, 0.00749386275518124, 0.00749877182643476, 0.00750264533520262, 0.00750487308203283, 0.00751051795328516, 0.00751829577963354, 0.00752597540127983, 0.00753580590288225, 0.00754920085955295, 0.00756163925496998, 0.00757183295644372, 0.00758639683296100, 0.00759897376758653, 0.00760928337883624, 0.00762164449360218, 0.00763702458930030, 0.00764612849803070, 0.00765395878852228, 0.00765665993737560, 0.00766099478265676, 0.00766402661189402, 0.00767215625557909, 0.00768358307760036, 0.00769473164981952, 0.00770507610438413, 0.00771276687759814, 0.00771827876051021, 0.00773026051802405, 0.00774295276642005, 0.00775695035310887, 0.00777728757599212, 0.00779600209702107, 0.00781110388033464, 0.00782486204985322, 0.00783639689378041, 0.00784481064931025, 0.00785649563796419, 0.00786597102628316, 0.00787567799985189, 0.00788943900907634, 0.00790677033394269, 0.00792592633389461, 0.00794674472244737, 0.00796669073412393, 0.00798240051339445, 0.00800043695750518, 0.00801832449394395, 0.00803354367574955, 0.00805359672612387, 0.00807603925782404, 0.00809884839648735, 0.00812172375661336, 0.00813927765745792, 0.00816125381994957, 0.00818988444761902, 0.00822143634652202, 0.00825006397004254, 0.00827705011606779, 0.00830156424641364, 0.00831972171691445, 0.00833472580845765, 0.00835206501726291, 0.00837012824694209, 0.00839153418685801, 0.00841553890516829, 0.00843628149015590, 0.00845936850641039, 0.00847914571173106, 0.00848486349447740, 0.00848766170800972, 0.00849158015529073, 0.00849502327311606, 0.00849719041341218, 0.00850110174700018, 0.00850537535618547, 0.00850804230181692, 0.00850674257351444, 0.00849993986470560, 0.00849275662811702, 0.00848801493464343, 0.00848573266977403, 0.00848943920596697, 0.00849131284163147, 0.00848771449359000, 0.00848449427461528, 0.00848221674929523, 0.00847976712853545, 0.00848129918799853, 0.00848361965001776, 0.00848668824907425, 0.00849516143657327, 0.00850345779390372, 0.00851003828058892, 0.00851712512936854, 0.00852053120252772, 0.00851722111184632, 0.00851358291244118, 0.00850934033246428, 0.00850220271111949, 0.00849818903100135, 0.00849245367215150, 0.00849047299450538, 0.00848911445210607, 0.00848950121685180, 0.00849190327635895, 0.00849626950600899, 0.00850219986214642, 0.00850194154296884, 0.00850027303810468, 0.00849525428241988, 0.00849025687499233, 0.00848598847562450, 0.00847313460159156, 0.00846040731453087, 0.00844909322883679, 0.00843335512322742, 0.00841498551262213, 0.00840915303029631, 0.00840683356662408, 0.00840206682569741, 0.00839518223930044, 0.00838786198084900, 0.00838184060639182, 0.00837702444235781, 0.00837618175801327, 0.00837767276390746, 0.00837899387373069, 0.00837603072552906, 0.00837217016549666, 0.00836279728109079, 0.00834933470824986, 0.00833393518882059, 0.00832234949465859, 0.00831013555694012, 0.00830195981778722, 0.00829816815833602, 0.00829433956328001, 0.00828880295321290, 0.00828435832540451, 0.00828061821664400, 0.00827183834980903, 0.00825793688582821, 0.00824229995875505, 0.00822424959449985, 0.00820742744251690, 0.00819316697047676, 0.00818117131576690, 0.00816700780621461, 0.00814972043602880, 0.00813259200212685, 0.00811806940001247, 0.00810658124500305, 0.00809344429835177, 0.00808161794377205, 0.00806641501057781, 0.00805156901238019, 0.00803658278805312, 0.00802564629269057, 0.00801868264047401, 0.00800680241025171, 0.00799762326305338, 0.00798935811768223, 0.00797979324167016, 0.00796857032017149, 0.00795410812662455, 0.00793846363051013, 0.00791727001506323, 0.00788971492652079, 0.00785481613287741, 0.00781794868481828, 0.00778142257800804, 0.00774889266607706, 0.00771643533142956, 0.00768711222817871, 0.00765885694108825, 0.00763466261010364, 0.00760882344080472, 0.00758414953221210, 0.00755162026826555, 0.00751571806313054, 0.00747991598068602, 0.00744671680092534, 0.00741520932131315, 0.00738259013814474, 0.00734635284213532, 0.00730961321366400, 0.00727035885908232, 0.00722823215312490, 0.00718570151092888, 0.00714345801102239, 0.00710616647128710, 0.00706303950089747, 0.00701909838637985, 0.00698117464928405, 0.00694354628508396, 0.00690622310789698, 0.00687156352520094, 0.00683582866203906, 0.00679832396104142, 0.00676082489900023, 0.00672211186738276, 0.00668060255973096, 0.00663953973262014, 0.00660131481789925, 0.00656616115933254, 0.00652831641406551, 0.00649077538308153, 0.00645468598202786, 0.00642114445837132, 0.00638125641013811, 0.00633883775153423, 0.00629697096242920, 0.00625490021718556, 0.00620813406692436, 0.00616642400006930, 0.00612734973758607, 0.00608815241622114, 0.00604915822097468, 0.00600969795073126, 0.00596768086319477, 0.00592079588758844, 0.00586860779538738, 0.00581816943905382, 0.00577198260102913, 0.00572899558702868, 0.00569263864508386, 0.00566130477283531, 0.00562884678475037, 0.00559228585406600, 0.00555621536830112, 0.00551916687431775, 0.00547478277814284, 0.00543134343204953, 0.00538858160725766, 0.00534581978246577, 0.00530305795767387, 0.00526029613288201};
            h_std = new double[]{0, 0, 0, 0, 3.67949294713763e-05, 0.000193078772804108, 0.000532539971750296, 0.000957334234506493, 0.00127251273938483, 0.00152209840891645, 0.00175349906503608, 0.00198643049265426, 0.00218194054743739, 0.00237345681189186, 0.00256726062550929, 0.00276168149057188, 0.00295863173068331, 0.00315665890813469, 0.00335165845351869, 0.00354502921700203, 0.00373872848426292, 0.00393283147873773, 0.00412765585746861, 0.00432332461142030, 0.00451837885356319, 0.00471326453591965, 0.00490804234745282, 0.00510051198790822, 0.00529180239396517, 0.00548332011449839, 0.00567748845241728, 0.00587382578463269, 0.00606898621157914, 0.00626516601427572, 0.00646286545314964, 0.00665905058277229, 0.00685308644113726, 0.00704279377148434, 0.00723056768951004, 0.00741753658833771, 0.00760528233431701, 0.00779137154501431, 0.00797725380893796, 0.00816381869352167, 0.00835504388787286, 0.00854635733713294, 0.00873867511740249, 0.00893264786007635, 0.00912287163622207, 0.00930968267577603, 0.00949688159944424, 0.00968375694769958, 0.00986671131008724, 0.0100447835032784, 0.0102212378531822, 0.0103969579114328, 0.0105725857090580, 0.0107479903540956, 0.0109210639995956, 0.0110965228117713, 0.0112728579613909, 0.0114493393657778, 0.0116247143697642, 0.0117997704658060, 0.0119752111551044, 0.0121501261020066, 0.0123261603944419, 0.0125009834047439, 0.0126719967363863, 0.0128385464431652, 0.0130072721818671, 0.0131744474539998, 0.0133420849200108, 0.0135082346660239, 0.0136729653695723, 0.0138379657936052, 0.0140050805319322, 0.0141713587413699, 0.0143400818289325, 0.0145115348201215, 0.0146824315441410, 0.0148531378332085, 0.0150177346684020, 0.0151819152949884, 0.0153454749277044, 0.0155074405829334, 0.0156681236833438, 0.0158263689466260, 0.0159860321230245, 0.0161444518929080, 0.0163029062120597, 0.0164639293405449, 0.0166261070616530, 0.0167823014496360, 0.0169317517364889, 0.0170824445628640, 0.0172330412691061, 0.0173823622557963, 0.0175300078760065, 0.0176760983233959, 0.0178201818483913, 0.0179647374380396, 0.0181078917790421, 0.0182499378948033, 0.0183863970379086, 0.0185218989952099, 0.0186569508590998, 0.0187934162203676, 0.0189296357579178, 0.0190650461579891, 0.0192004212024641, 0.0193376208402714, 0.0194727122066680, 0.0196055865866731, 0.0197377230470366, 0.0198723354236878, 0.0200068881484095, 0.0201445538674535, 0.0202823748660101, 0.0204182557964867, 0.0205509775640443, 0.0206861648445555, 0.0208208541247817, 0.0209530691114881, 0.0210852858703191, 0.0212173800984253, 0.0213457009515754, 0.0214702962847982, 0.0215911130388801, 0.0217115279136028, 0.0218339444931691, 0.0219577586277794, 0.0220823986633935, 0.0222049601406418, 0.0223285279726194, 0.0224526940695063, 0.0225767969069329, 0.0227026603430395, 0.0228281874722080, 0.0229546200815215, 0.0230818525359795, 0.0232111590649195, 0.0233414293243058, 0.0234685192579630, 0.0235951301269868, 0.0237219477075582, 0.0238483608905893, 0.0239696192668847, 0.0240915853352427, 0.0242107516134698, 0.0243299785016827, 0.0244455546765751, 0.0245576319184692, 0.0246694704417125, 0.0247824143373918, 0.0248983693291131, 0.0250143602199528, 0.0251285821323539, 0.0252430318615580, 0.0253567986194265, 0.0254709702506317, 0.0255871546202950, 0.0257070608256623, 0.0258297059610415, 0.0259502607748447, 0.0260708082192015, 0.0261917822410173, 0.0263166110311219, 0.0264399967506465, 0.0265671498757693, 0.0266948567596435, 0.0268198215265720, 0.0269419564858195, 0.0270632533074170, 0.0271871251465978, 0.0273107660490382, 0.0274344283714253, 0.0275526694452629, 0.0276647127976183, 0.0277760029443978, 0.0278905066175406, 0.0280068969131948, 0.0281189113288337, 0.0282301346743674, 0.0283418516875173, 0.0284545521108111, 0.0285673658668747, 0.0286811873006571, 0.0287947150511975, 0.0289099766606129, 0.0290254121553146, 0.0291341050163867, 0.0292397300580172, 0.0293454253486568, 0.0294461388836373, 0.0295455029707249, 0.0296453900311489, 0.0297433961639700, 0.0298381675685309, 0.0299333998898410, 0.0300314451100423, 0.0301321065650543, 0.0302352059248695, 0.0303401770803477, 0.0304457132852312, 0.0305548204314349, 0.0306677611663779, 0.0307806912415456, 0.0308940265031471, 0.0310074309371452, 0.0311200428393809, 0.0312312010354008, 0.0313411006889380, 0.0314504289596644, 0.0315617619758496, 0.0316747043149397, 0.0317884634775531, 0.0319033685062039, 0.0320197403417400, 0.0321360168467384, 0.0322522640404132, 0.0323655525945169, 0.0324774823391552, 0.0325824406637514, 0.0326870542495115, 0.0327917962025288, 0.0328956579381296, 0.0329988926985561, 0.0331061237355935, 0.0332164037221497, 0.0333255587774203, 0.0334346927762383, 0.0335428406077127, 0.0336478391716661, 0.0337488354914378, 0.0338431864053402, 0.0339397935877514, 0.0340338041503075, 0.0341246537772267, 0.0342139378517734, 0.0343091722518435, 0.0344053696567869, 0.0345024187273553, 0.0345992052891891, 0.0346973316300671, 0.0347966031173371, 0.0348923561563302, 0.0349872329262222, 0.0350827275792354, 0.0351744131405923, 0.0352637602073541, 0.0353488272975297, 0.0354355885521569, 0.0355227579033070, 0.0356127312754022, 0.0357033581611453, 0.0357928519935091, 0.0358766231988685, 0.0359555807942155, 0.0360351222743301, 0.0361107362756997, 0.0361823424903444, 0.0362545818330418, 0.0363325568027274, 0.0364127817385316, 0.0364958190493668, 0.0365794179689607, 0.0366659097582004, 0.0367494842513361, 0.0368282391914720, 0.0369065144024243, 0.0369862597045821, 0.0370677345205778, 0.0371519219642673, 0.0372339777740990, 0.0373102165183331, 0.0373813316488705, 0.0374531059332244, 0.0375278683287640, 0.0376067849751416, 0.0376874815407193, 0.0377674651916554, 0.0378484243019170, 0.0379326101218362, 0.0380121906675465, 0.0380905414424740, 0.0381659189978696, 0.0382398380857210, 0.0383115056587471, 0.0383803130624745, 0.0384491350466659, 0.0385143038222407, 0.0385784193421721, 0.0386423235281616, 0.0387043001303454, 0.0387654428430244, 0.0388248806200543, 0.0388821226921407, 0.0389394997952218, 0.0390004076542722, 0.0390615250128191, 0.0391187728122170, 0.0391806928851591, 0.0392374491297125, 0.0392932804869067, 0.0393530802654669, 0.0394135012668196, 0.0394667265000909, 0.0395189213811341, 0.0395724839730518, 0.0396271881786198, 0.0396849909157921, 0.0397394638763545, 0.0397907430943357, 0.0398420403781948, 0.0398934944939016, 0.0399373486649349, 0.0399810070163407, 0.0400239673328638, 0.0400671349098503, 0.0401145612350727, 0.0401541269667130, 0.0401889886555126, 0.0402215611634980, 0.0402539270238377, 0.0402856132182784, 0.0403149823726488, 0.0403441784263947, 0.0403765210697434, 0.0404074636329170, 0.0404353341595192, 0.0404647661680085, 0.0404931419370354, 0.0405210657290148, 0.0405494354537245, 0.0405729720540179, 0.0405889476611166, 0.0406046367071127, 0.0406206732767316, 0.0406364387066966, 0.0406526229288320, 0.0406658143710237, 0.0406724933192395, 0.0406780449150099, 0.0406843943751433, 0.0406916121826395, 0.0407017582063796, 0.0407159863174828, 0.0407317791752551, 0.0407498767424327, 0.0407657838686326, 0.0407781527176270, 0.0407913945401251, 0.0408045779131497, 0.0408087830105478, 0.0408140038695561, 0.0408205591464755, 0.0408271051511581, 0.0408332648040241, 0.0408363794106753, 0.0408345396894225, 0.0408337059947563, 0.0408322828043068, 0.0408296816950182, 0.0408188561013749, 0.0408072544988789, 0.0407980661898521, 0.0407899143884630, 0.0407923246496239, 0.0407957976235880, 0.0407941518144936, 0.0407901899620968, 0.0407820341453386, 0.0407646165259819, 0.0407462022620097, 0.0407244342183640, 0.0407035512273304, 0.0406849000734180, 0.0406677560838256, 0.0406543947050416, 0.0406312779843079, 0.0405934974458128, 0.0405562719048089, 0.0405185314525359, 0.0404856667095240, 0.0404830858113401, 0.0404812201333986, 0.0404812412318044, 0.0404815054343056, 0.0404775048177567, 0.0404690658744757, 0.0404502381011533, 0.0404270262118738, 0.0404045858725143, 0.0403825579100888, 0.0403670852786894, 0.0403523999003084, 0.0403298375141296, 0.0403062084785662, 0.0402817315638346, 0.0402503194749338, 0.0402052929683304, 0.0401588714895004, 0.0401080892033995, 0.0400580652808583, 0.0400137428830356, 0.0399694828656691, 0.0399253553279544, 0.0398771487255286, 0.0398261443745372, 0.0397659360978293, 0.0397031381263445, 0.0396395662027286, 0.0395794571869955, 0.0395163155969887, 0.0394450746487967, 0.0393730856712142, 0.0393043576683233, 0.0392445588882077, 0.0391873461765152, 0.0391299795053827, 0.0390795207468476, 0.0390326304991784, 0.0389909609645323, 0.0389530949551230, 0.0389155574225514, 0.0388732906322888, 0.0388290690163589, 0.0387851060943677, 0.0387408093308451, 0.0386970307216104, 0.0386535179519077, 0.0386092071519849, 0.0385664926182700, 0.0385145046201457, 0.0384646131732932, 0.0384175390851540, 0.0383716328271705, 0.0383251262783359, 0.0382801407311145, 0.0382385471812005, 0.0381970323762880, 0.0381397913700555, 0.0380826463768889, 0.0380252403609669, 0.0379694808435456, 0.0379192818885682, 0.0378632075996340, 0.0378052887438589, 0.0377475194057591, 0.0376878445630086, 0.0376303075011931, 0.0375722843653475, 0.0375160461165344, 0.0374622169055467, 0.0373982747597045, 0.0373317129895386, 0.0372632021135398, 0.0372007972462763, 0.0371417810428536, 0.0370848580895281, 0.0370287425607471, 0.0369709993813988, 0.0369111248000437, 0.0368490945646321, 0.0367790251557113, 0.0367094784463729, 0.0366421340619614, 0.0365753455814140, 0.0365165254784293, 0.0364642484055976, 0.0363983785832706, 0.0363318127543273, 0.0362665657480673, 0.0361973646232603, 0.0361240304614612, 0.0360525205303300, 0.0359811032438551, 0.0359075705916779, 0.0358421930344852, 0.0357804871999918, 0.0357210231160370, 0.0356635492492557, 0.0356134606121405, 0.0355572801076855, 0.0354982226157783, 0.0354365505157187, 0.0353732755497993, 0.0353113744380170, 0.0352657867218915, 0.0352328110879529, 0.0352142699853205, 0.0351936244278186, 0.0351744227254703, 0.0351407648323410, 0.0351031901805802, 0.0350628031864289, 0.0350208866847711, 0.0349817492013638, 0.0349492918249630, 0.0349240981827506, 0.0348979631012278, 0.0348719707650533, 0.0348494820779744, 0.0348316649818195, 0.0348173482153785, 0.0348036230893844, 0.0347866649087006, 0.0347592182930258, 0.0347216812032912, 0.0346811274104492, 0.0346414199489905, 0.0346042305165304, 0.0345842499260889, 0.0345689696044483, 0.0345529720696401, 0.0345369718326862, 0.0345232979315311, 0.0344950337498625, 0.0344652629076623, 0.0344368247447233, 0.0344133905018281, 0.0343937672649393, 0.0343838938680702, 0.0343738729592896, 0.0343628026423630, 0.0343505288636189, 0.0343291145825263, 0.0342833111008993, 0.0342380758581124, 0.0341926870648411, 0.0341523592305567, 0.0341390600407326, 0.0341270691929712, 0.0341168693059891, 0.0341070796415021, 0.0340893580620551, 0.0340567253481202, 0.0340266390224452, 0.0339972491044335, 0.0339702615751643, 0.0339550481539194, 0.0339353971780685, 0.0339143949164824, 0.0338912328308460, 0.0338650495880381, 0.0338417532861584, 0.0338229776995149, 0.0338053384091469, 0.0337879177548197, 0.0337708041444559, 0.0337495258675111, 0.0337258728881441, 0.0337050439708045, 0.0336869185050773, 0.0336808366590692, 0.0336680651018764, 0.0336521007008988, 0.0336382006396131, 0.0336240453662497, 0.0336096998120863, 0.0335987327995444, 0.0335863038919219, 0.0335737918357234, 0.0335619944070901, 0.0335482794029815, 0.0335366788717953, 0.0335256887041438, 0.0335150766677554, 0.0335029820255141, 0.0334947363880597, 0.0334837685135122, 0.0334694633756434, 0.0334566750148781, 0.0334473593924236, 0.0334436028407092, 0.0334406056832991, 0.0334305952582593, 0.0334137405330411, 0.0333997117590941, 0.0333774088309138, 0.0333559784958814, 0.0333383245762488, 0.0333213661042021, 0.0333074798778790, 0.0333038020535311, 0.0332976060530302, 0.0332876228915347, 0.0332787673783512, 0.0332688082105638, 0.0332438057831767, 0.0332166415568923, 0.0331878369777854, 0.0331620161961792, 0.0331365240188241, 0.0331145349085333, 0.0330917286609804, 0.0330696002467251, 0.0330459168277726, 0.0330175728102357, 0.0329865468388317, 0.0329575333338308, 0.0329296325335831, 0.0328992435495673, 0.0328695339914104, 0.0328428232955257, 0.0328189588020314, 0.0327870613984419, 0.0327515723510629, 0.0327189938640356, 0.0326884187784743, 0.0326626597680736, 0.0326415001548530, 0.0326205006451071, 0.0326000775695001, 0.0325768409317377, 0.0325485324999514, 0.0325248941550582, 0.0325026831784529, 0.0324801033476734, 0.0324583095289115, 0.0324373114378115, 0.0324154715106935, 0.0323886836794573, 0.0323591160431794, 0.0323296662181402, 0.0323037245070497, 0.0322885921692450, 0.0322788591210404, 0.0322712471553746, 0.0322636910079882, 0.0322521825781201, 0.0322301751384751, 0.0322044976206504, 0.0321774398669283, 0.0321601675558206, 0.0321390448225826, 0.0321167675894520, 0.0320934456602315, 0.0320739220424678, 0.0320619796168030, 0.0320493709587766, 0.0320347110335505, 0.0320200163383902, 0.0320024119597325, 0.0319836873164728, 0.0319649067520776, 0.0319487444724395, 0.0319322967148395, 0.0319146048333379, 0.0318977814231142, 0.0318835673198701, 0.0318738277672460, 0.0318610886435987, 0.0318539728774794, 0.0318470500373867, 0.0318391079395042, 0.0318367022567256, 0.0318354486433541, 0.0318340379343299, 0.0318337099363526, 0.0318356639203733, 0.0318297890217764, 0.0318199026778258, 0.0318075720115013, 0.0317958738618863, 0.0317832583384144, 0.0317728725682065, 0.0317616435320305, 0.0317506218207644, 0.0317387501296353, 0.0317242075989601, 0.0317088016932839, 0.0316933357342588, 0.0316799815831380, 0.0316682271182624, 0.0316600664290843, 0.0316543718101248, 0.0316493299047946, 0.0316469471413446, 0.0316476713349224, 0.0316457004425520, 0.0316419904398370, 0.0316358833547743, 0.0316285153271073, 0.0316182144493283, 0.0316049591909677, 0.0315901028496217, 0.0315754218234753, 0.0315612951700265, 0.0315436127876846, 0.0315211838472991, 0.0315041783184480, 0.0314867601826551, 0.0314683487167676, 0.0314428593144723, 0.0314209599246585, 0.0314008596890759, 0.0313832779684366, 0.0313778301222228, 0.0313749923881009, 0.0313725525547434, 0.0313697162182078, 0.0313660748346404, 0.0313407705627214, 0.0313157122445966, 0.0312933919258356, 0.0312707460016653, 0.0312499039423534, 0.0312314383784370, 0.0312150643100346, 0.0311998117068498, 0.0311818136724167, 0.0311682762644644, 0.0311522942449657, 0.0311364822449639, 0.0311163890430733, 0.0310979045382842, 0.0310752755445587, 0.0310459481341861, 0.0310172089922608, 0.0309860290712838, 0.0309487114393204, 0.0309100536755721, 0.0308691870573045, 0.0308259931649097, 0.0307826645188383, 0.0307385506302482, 0.0306952540051009, 0.0306525578921964, 0.0306092891130638, 0.0305657604995611, 0.0305259319024346, 0.0304851089133665, 0.0304500061129689, 0.0304144594531115, 0.0303755444213669, 0.0303312794264697, 0.0302842954907684, 0.0302219723322989, 0.0301543149391819, 0.0300931999701607, 0.0300350556167648, 0.0299705653391835, 0.0299041278349603, 0.0298383624753118, 0.0297646080638828, 0.0296899706390169, 0.0296333704970656, 0.0295816969642016, 0.0295325870016450, 0.0294865642054158, 0.0294397006424942, 0.0293839898353221, 0.0293285498160582, 0.0292756707734586, 0.0292198970095257, 0.0291617424811497, 0.0291188621150199, 0.0290759210945045, 0.0290324817024059, 0.0289896912815596, 0.0289473715404851, 0.0288892892873217, 0.0288223849776760, 0.0287465406673065, 0.0286693076257327, 0.0285952904312234, 0.0285260180397084, 0.0284607722051676, 0.0283965320592273, 0.0283298237894451, 0.0282525092803987, 0.0281639882932428, 0.0280791254925410, 0.0279974197299523, 0.0279213999277569, 0.0278537679467779, 0.0278005750461905, 0.0277408607554869, 0.0276785458945644, 0.0276135276050977, 0.0275467379105517, 0.0274794458373318, 0.0274123037770363, 0.0273425089460455, 0.0272719354856268, 0.0271998018417669, 0.0271248326899700, 0.0270545112127209, 0.0269932623501352, 0.0269322318893314, 0.0268753062628510, 0.0268203051903590, 0.0267606197151319, 0.0266926507693289, 0.0266237871494570, 0.0265510861936907, 0.0264669869123491, 0.0263936644261145, 0.0263199265909828, 0.0262442305403377, 0.0261770991578565, 0.0261127653705314, 0.0260493089961795, 0.0259833898560173, 0.0259168313244710, 0.0258495513665538, 0.0257891999146069, 0.0257288574128608, 0.0256670281969582, 0.0256067736627172, 0.0255505484851239, 0.0254968314118971, 0.0254450873482725, 0.0253949262528388, 0.0253452631436172, 0.0253050021544046, 0.0252711391567193, 0.0252321307846509, 0.0251939837243132, 0.0251500792223999, 0.0250966407856112, 0.0250403099052390, 0.0249885983338761, 0.0249448956413271, 0.0249048598306247, 0.0248697245252024, 0.0248393613575223, 0.0248108468195844, 0.0247765012819678, 0.0247210903783174, 0.0246622987178138, 0.0246034068932727, 0.0245501971838746, 0.0245005795494228, 0.0244555009410645, 0.0244116283630737, 0.0243672469253179, 0.0243207252983919, 0.0242731686113212, 0.0242283868774675, 0.0241866478246124, 0.0241484052591852, 0.0241203436417780, 0.0240939456651939, 0.0240698456442997, 0.0240412100370680, 0.0240057822924037, 0.0239687792000720, 0.0239356169312744, 0.0239006182396579, 0.0238639339986085, 0.0238317274847935, 0.0238008482678715, 0.0237745366945745, 0.0237491689749877, 0.0237228438135315, 0.0236869277013907, 0.0236491855692664, 0.0236112535688105, 0.0235735986231755, 0.0235399425603808, 0.0235075426843128, 0.0234848740509467, 0.0234647679613665, 0.0234504182013284, 0.0234440493599055, 0.0234412458196369, 0.0234369291755901, 0.0234144316624063, 0.0233899891968229, 0.0233632839064014, 0.0233386451273447, 0.0233139252233021, 0.0232921230620351, 0.0232732608605761, 0.0232547165654647, 0.0232288198614584, 0.0232062020839106, 0.0231871855708986, 0.0231651664444299, 0.0231422367747450, 0.0231181456079235, 0.0230927315552285, 0.0230655524218638, 0.0230379088240589, 0.0230158845288050, 0.0229935985536697, 0.0229719515045489, 0.0229440488427924, 0.0229174846368324, 0.0228959102498491, 0.0228754879203381, 0.0228582449513300, 0.0228508786169404, 0.0228422653173222, 0.0228291504184167, 0.0228160373182615, 0.0228032396357997, 0.0227895583503915, 0.0227785113361823, 0.0227682549677458, 0.0227606425052346, 0.0227545330714001, 0.0227508242543379, 0.0227487015532019, 0.0227465629084067, 0.0227437668049055, 0.0227408083955011, 0.0227344961982985, 0.0227060971138407, 0.0226780605876685, 0.0226527407777873, 0.0226288654206451, 0.0226206676884490, 0.0226193744986310, 0.0226235285588871, 0.0226316587889838, 0.0226385494345840, 0.0226425496745669, 0.0226510950641335, 0.0226625987200876, 0.0226733036837020, 0.0226842006887948, 0.0226963684539418, 0.0227136421343878, 0.0227271111018217, 0.0227427897487950, 0.0227541772729954, 0.0227626219233781, 0.0227664568629663, 0.0227678482911474, 0.0227769810099124, 0.0228004797234422, 0.0228250698959717, 0.0228574453951134, 0.0228895635075958, 0.0229254897887809, 0.0229613700676651, 0.0229952442529968, 0.0230193437242897, 0.0230485348707573, 0.0230786078723467, 0.0231046654067916, 0.0231336583870167, 0.0231697391148064, 0.0232109968374304, 0.0232494828223897, 0.0232888063796694, 0.0233333498442755, 0.0233809569976454, 0.0234296719789872, 0.0234855917819145, 0.0235395082573517, 0.0235888396140975, 0.0236361013971077, 0.0236851737139825, 0.0237366463129917, 0.0237934521751081, 0.0238529213957135, 0.0239158195511305, 0.0239787245816168, 0.0240394094756178, 0.0241009006343838, 0.0241601534159438, 0.0242157184946298, 0.0242762399821082, 0.0243423267539822, 0.0244094361091539, 0.0244815466613573, 0.0245616984262892, 0.0246336827506745, 0.0247031693182289, 0.0247733357042512, 0.0248407330759515, 0.0248936895155218, 0.0249513972920907, 0.0250110393360504, 0.0250714665699441, 0.0251378852451779, 0.0252058050671724, 0.0252719480888492, 0.0253377387034578, 0.0254036459454735, 0.0254628601597774, 0.0255300898190765, 0.0255962819929645, 0.0256623464949977, 0.0257307892396560, 0.0258096604900265, 0.0258931829253463, 0.0259776290942884, 0.0260642626649697, 0.0261546591198574, 0.0262459990200766, 0.0263385619640034, 0.0264317928120933, 0.0265256845218214, 0.0266202301004509};
        } else if (hardware.equals("H02")){ //5 cm
            h_mean = new double[x1.length];
            h_std = new double[x1.length];
        }*/

    }

    private double[] sub_mean_numel(double[] y2, int numel) {
        double sumnumel = 0;
        for (int i = 0; i < numel; i++) {
            sumnumel += y2[i];
        }
        double sub = sumnumel / numel;
        for (int i = 0; i < y2.length; i++) {
            y2[i] = y2[i] - sub;
        }

        return y2;
        //h_mean = new double[]{-0.000925103216285459, -0.000925103216285459, -0.000925103216285459, -0.000925103216285459, -0.000933330812637990, -0.000974770682630763, -0.00104767116062378, -0.00103068458196402, -0.000975729377893084, -0.000925452736208906, -0.000853094809264809, -0.000748195368156767, -0.000721310278570469, -0.000699566388175511, -0.000679015798696049, -0.000656272886600434, -0.000633000094549946, -0.000608622102386108, -0.000581840572295992, -0.000554666631181109, -0.000526248515661639, -0.000500490701863131, -0.000476852574483661, -0.000460031130674759, -0.000443850159605906, -0.000426373743506480, -0.000408697152709512, -0.000388347970322835, -0.000367318399621160, -0.000346837898674290, -0.000323357957347374, -0.000296046737794252, -0.000268108183636206, -0.000240944827711735, -0.000215321369212550, -0.000188799741556016, -0.000160607194246145, -0.000135123305449085, -0.000112118740029348, -8.97111490987141e-05, -6.59774287453779e-05, -4.22391235428408e-05, -1.91357598202028e-05, 3.71278606088089e-06, 3.23099541543398e-05, 6.26586102497562e-05, 9.33403683548165e-05, 0.000122897212356951, 0.000154387672342393, 0.000187461754476318, 0.000221256451465341, 0.000254577922967755, 0.000283766791592774, 0.000316814184329929, 0.000351224864758687, 0.000384322024338292, 0.000416748371431851, 0.000450311551147124, 0.000486721131621910, 0.000523249503730597, 0.000559166098171411, 0.000592708871153392, 0.000627334638436954, 0.000662979754941844, 0.000698792608861051, 0.000734780513711482, 0.000771339077899626, 0.000809683173306224, 0.000850869215508461, 0.000896281926084348, 0.000939282626214921, 0.000982077700000448, 0.00102350641492184, 0.00106372190659042, 0.00110331081566983, 0.00114491037630497, 0.00118851656863588, 0.00123329311028039, 0.00127687491661535, 0.00132098544360229, 0.00136590084367812, 0.00141318739416633, 0.00146224880787097, 0.00151101723467778, 0.00155892623387507, 0.00160574981922340, 0.00165251089542806, 0.00170248800296481, 0.00175394047948894, 0.00180663752603148, 0.00186064300476643, 0.00191418901993156, 0.00196839106185653, 0.00202368991071136, 0.00207945479104399, 0.00213392038122280, 0.00218839311298308, 0.00224451080948780, 0.00230002176853464, 0.00235398967532835, 0.00241064275795181, 0.00246678080782824, 0.00252636574130266, 0.00258881522604863, 0.00265475758496740, 0.00271973196386669, 0.00278330125563967, 0.00284537192023267, 0.00290426133582993, 0.00296288453824448, 0.00302054107625727, 0.00307333533156156, 0.00312486576157036, 0.00317761655930191, 0.00322843560162190, 0.00328444000290925, 0.00334197208051201, 0.00339723538268009, 0.00345185615343772, 0.00350612149540506, 0.00356483167853226, 0.00362399998362860, 0.00368459392277966, 0.00374132360918406, 0.00379923808183870, 0.00385822212572147, 0.00392226960443106, 0.00398892010454345, 0.00405485948415255, 0.00411822775590655, 0.00418154227678604, 0.00424214077923463, 0.00430051219057582, 0.00435943007008481, 0.00442143375678022, 0.00448289805781602, 0.00454455652347648, 0.00460425975334305, 0.00466113338513120, 0.00471592363743569, 0.00476942245684487, 0.00482296698517970, 0.00487793861463075, 0.00493365454675451, 0.00498946014356541, 0.00504572776173662, 0.00510317463603549, 0.00515874841922822, 0.00521190893209097, 0.00526533778374859, 0.00532074748925169, 0.00537604726370444, 0.00542913903945631, 0.00548032273681986, 0.00552890416056723, 0.00557844256157939, 0.00563092201242310, 0.00568326022100498, 0.00573822596247485, 0.00579415949807266, 0.00585327800435177, 0.00591607361137529, 0.00597818166827622, 0.00603854016002457, 0.00609616737415567, 0.00615625103119110, 0.00621398360414212, 0.00626760780938756, 0.00632307576400483, 0.00637984367009536, 0.00643567675394646, 0.00648869831216243, 0.00653903498328704, 0.00658933386832658, 0.00663703916362864, 0.00668510638700963, 0.00673408429410951, 0.00677963183008621, 0.00682269573654040, 0.00686703142172620, 0.00691190953852081, 0.00695574481808292, 0.00699680651191897, 0.00703686397838130, 0.00707681775716740, 0.00711497841632191, 0.00715240421987359, 0.00718727949270037, 0.00722298557638596, 0.00725813793559952, 0.00729290078248444, 0.00732760887338363, 0.00736050497494401, 0.00739203005494902, 0.00742749000917260, 0.00746165315089103, 0.00749602287840336, 0.00752669050173877, 0.00755376408850909, 0.00757916963780752, 0.00760244022615175, 0.00762704874998839, 0.00765196042507174, 0.00767937064831253, 0.00770769980914657, 0.00773625658584304, 0.00776055435900578, 0.00778389477365909, 0.00780729446187884, 0.00783286991680733, 0.00785952501822656, 0.00788581048440017, 0.00791630399907862, 0.00794459755372953, 0.00796981586070147, 0.00799558845633208, 0.00802389728639702, 0.00805530620911509, 0.00808921107257701, 0.00812455756074580, 0.00815958017126417, 0.00819186584546562, 0.00822448824154571, 0.00825756459009890, 0.00828945427753158, 0.00831937598771841, 0.00835043346716108, 0.00838299983765184, 0.00841866258455378, 0.00845633600029748, 0.00849754954223950, 0.00854025265154380, 0.00858203376831888, 0.00861698580494863, 0.00865028328948696, 0.00867966415724472, 0.00870860177808107, 0.00873891969366811, 0.00876870512156559, 0.00879895445171858, 0.00883008350188011, 0.00886174154909184, 0.00889300696355176, 0.00892361044395857, 0.00895425550776578, 0.00898468628611882, 0.00901180324438697, 0.00903753980835736, 0.00906421831234745, 0.00909601322938760, 0.00912852822942543, 0.00916039379993221, 0.00919513031948965, 0.00923093400514211, 0.00926977948149469, 0.00931187547793321, 0.00935549923968279, 0.00939789645576729, 0.00943569336368629, 0.00947530730130739, 0.00951094770045803, 0.00954578186434972, 0.00958469369186839, 0.00962352252115615, 0.00966123764029411, 0.00969760612575545, 0.00973585056599852, 0.00977589452897693, 0.00981287842294294, 0.00984768889607982, 0.00988403849269525, 0.00991955957210045, 0.00996288478951546, 0.0100034200878766, 0.0100427923479049, 0.0100761433062084, 0.0101054788108858, 0.0101338200383600, 0.0101610885301581, 0.0101870734194001, 0.0102233421555508, 0.0102616561125908, 0.0102978857935313, 0.0103335130681286, 0.0103656399744191, 0.0103985058193479, 0.0104264223624413, 0.0104560299101100, 0.0104893644928763, 0.0105236199768374, 0.0105581441132303, 0.0105973172865161, 0.0106348055952605, 0.0106718611924110, 0.0107136461542185, 0.0107549756369340, 0.0107947305713132, 0.0108275740379080, 0.0108658657519132, 0.0109060174175630, 0.0109460213947237, 0.0109841835334217, 0.0110284913176657, 0.0110659896747515, 0.0111039549551863, 0.0111422610876791, 0.0111785243890947, 0.0112050012366989, 0.0112274914493725, 0.0112500702371930, 0.0112749086252694, 0.0112962753801104, 0.0113240638468358, 0.0113494878080444, 0.0113689597225216, 0.0113880381336580, 0.0114040885258147, 0.0114178626193169, 0.0114332120991808, 0.0114484406320991, 0.0114585148095847, 0.0114650150294813, 0.0114726540037686, 0.0114818793430509, 0.0114911018101767, 0.0114982543343727, 0.0115060013933302, 0.0115163971561868, 0.0115237325977152, 0.0115292516267331, 0.0115329587745607, 0.0115352288586841, 0.0115361855612567, 0.0115354978986503, 0.0115381628008718, 0.0115414527984025, 0.0115398950981600, 0.0115381953588763, 0.0115380446411171, 0.0115377578661954, 0.0115376877776349, 0.0115368108713405, 0.0115330081688896, 0.0115249171764403, 0.0115175228953053, 0.0115101459641871, 0.0115033606595446, 0.0114988596122190, 0.0114992758871531, 0.0114997575984402, 0.0114997964009373, 0.0114967676489337, 0.0114930080846771, 0.0114840720742666, 0.0114719911043060, 0.0114659264323384, 0.0114603116271282, 0.0114556361784992, 0.0114528139189559, 0.0114466894801176, 0.0114367374234926, 0.0114291546785711, 0.0114232075759756, 0.0114201877989307, 0.0114129343505420, 0.0114055514048452, 0.0113989832903735, 0.0113915955042928, 0.0113942068330807, 0.0113972994067368, 0.0113990506521650, 0.0113983747314399, 0.0113984056141474, 0.0113943234668316, 0.0113885020985768, 0.0113852282601071, 0.0113839380896492, 0.0113815126354750, 0.0113776068378496, 0.0113768285493465, 0.0113724074561996, 0.0113596664054650, 0.0113472414251671, 0.0113327802819874, 0.0113179169323281, 0.0113193534197445, 0.0113179441091253, 0.0113119099637302, 0.0113038791711831, 0.0112942861163965, 0.0112827919099994, 0.0112711119614029, 0.0112592113947926, 0.0112534296449755, 0.0112490947356722, 0.0112491334940716, 0.0112486506371610, 0.0112459739815990, 0.0112422901477724, 0.0112370014960617, 0.0112289062848901, 0.0112159985073207, 0.0112013792521581, 0.0111858892891766, 0.0111716111029613, 0.0111591794038357, 0.0111454706140847, 0.0111291722504375, 0.0111073825581956, 0.0110786511124669, 0.0110445105560741, 0.0110103778567096, 0.0109754781898980, 0.0109496769051744, 0.0109292073492069, 0.0109090866405674, 0.0108906023161391, 0.0108729786694698, 0.0108573328815119, 0.0108414974866866, 0.0108217920597927, 0.0108014992242307, 0.0107813245595213, 0.0107612300520925, 0.0107441124591515, 0.0107242730144862, 0.0107019500636724, 0.0106795583032037, 0.0106565797799298, 0.0106355486319748, 0.0106179543488630, 0.0106014597229102, 0.0105854047704554, 0.0105672197447657, 0.0105435158676844, 0.0105172067254036, 0.0104877291241861, 0.0104559292096339, 0.0104220773386179, 0.0103877118288757, 0.0103568637111685, 0.0103320614629993, 0.0103040180771273, 0.0102768127269068, 0.0102478947081661, 0.0102163674660803, 0.0101872117621607, 0.0101536808503078, 0.0101209104757253, 0.0100896885386253, 0.0100579435124247, 0.0100290442974517, 0.0100019396701775, 0.00997748452395518, 0.00995758585599434, 0.00993298034420729, 0.00991134544458607, 0.00989468701333028, 0.00988324305509222, 0.00987506355099621, 0.00987078872955284, 0.00986712533039233, 0.00985862861763570, 0.00984799885777219, 0.00983785121868532, 0.00981980849281702, 0.00979810488734627, 0.00977462690372222, 0.00975071105688394, 0.00973573884196389, 0.00972787002076295, 0.00971638323616997, 0.00970690399539296, 0.00969878206410422, 0.00968634666315262, 0.00966606251100508, 0.00964668512340195, 0.00962671298876339, 0.00960716080089146, 0.00959122387350648, 0.00957669763966967, 0.00955651322654947, 0.00953915454426102, 0.00952655110045696, 0.00951408553337575, 0.00949891332769200, 0.00948306677164846, 0.00946450030866477, 0.00944705000943540, 0.00943488793803539, 0.00942552859285038, 0.00942401003488727, 0.00942701205899876, 0.00942601576152078, 0.00941670156415615, 0.00940127261337078, 0.00938935258815918, 0.00937940986337296, 0.00937124526690268, 0.00936396210517399, 0.00935972034874185, 0.00935148789296225, 0.00934262757053536, 0.00933497168210697, 0.00932540333255063, 0.00931445548409804, 0.00930496695356600, 0.00929523137891351, 0.00928043588271501, 0.00925812428287822, 0.00923171840743811, 0.00920415725600365, 0.00918106105079468, 0.00916925135368260, 0.00915949422733331, 0.00914936132599629, 0.00914072908537320, 0.00913500508359071, 0.00911989733590035, 0.00910479970926353, 0.00908704184083005, 0.00906044464299112, 0.00903334120298203, 0.00901260791943671, 0.00899149505013124, 0.00897130154478511, 0.00895086766514034, 0.00892643112595077, 0.00889335538838095, 0.00886052693910084, 0.00882658353824941, 0.00879315489148598, 0.00877081682572998, 0.00875142370219801, 0.00873253155469646, 0.00871402057593907, 0.00868995808594569, 0.00865878878683327, 0.00862693633608511, 0.00859505838319697, 0.00856417644622900, 0.00854293266058083, 0.00851708434253551, 0.00849004203107326, 0.00846523585136376, 0.00843703385492951, 0.00840850205301560, 0.00838188859403258, 0.00835893090929481, 0.00833903813659499, 0.00831916241085442, 0.00829755761230540, 0.00827376000156373, 0.00824767706312410, 0.00822365995315895, 0.00820013618604476, 0.00817368724393445, 0.00814950677234886, 0.00812728495801443, 0.00810290504120782, 0.00807578414628506, 0.00804949468672281, 0.00801897626279188, 0.00798819203207332, 0.00795719707832646, 0.00792489125065101, 0.00788902372396911, 0.00785182867530168, 0.00781219620079507, 0.00777636627979238, 0.00774356628285497, 0.00771129376606022, 0.00767941141555038, 0.00764716428638166, 0.00761360087636128, 0.00758526367097275, 0.00755821539211346, 0.00752568843165231, 0.00749472854022321, 0.00746790815206440, 0.00743805989005108, 0.00740816552845780, 0.00737844502167920, 0.00734660911132544, 0.00731523545873405, 0.00728485988250426, 0.00725201148103921, 0.00722225442708420, 0.00719195945078363, 0.00716075485419901, 0.00713060167950058, 0.00710454037970223, 0.00708135642210609, 0.00705895980445882, 0.00703596461748759, 0.00701289734436624, 0.00698840862228120, 0.00696468341168464, 0.00694223078837445, 0.00691380534010541, 0.00688177691619316, 0.00684552530313417, 0.00680573168606934, 0.00676796227114096, 0.00672894607907215, 0.00669341544271071, 0.00665883062981232, 0.00662180270695040, 0.00657927625388707, 0.00653539290744378, 0.00649086438987825, 0.00645123894089048, 0.00641350376700287, 0.00637858697702908, 0.00634365057425728, 0.00630508988545279, 0.00626333306954492, 0.00622578071113889, 0.00619483130512716, 0.00617054891740178, 0.00614779758701536, 0.00612427427658841, 0.00610198986597757, 0.00607312364095281, 0.00603906846111616, 0.00600298589981616, 0.00596902847813125, 0.00594709324933799, 0.00593370130004109, 0.00591990379958055, 0.00590571037412852, 0.00588677926496252, 0.00586260370882941, 0.00583743926867383, 0.00581289351945386, 0.00579900929266553, 0.00579018951267605, 0.00578111131039623, 0.00577274455410515, 0.00576492787690242, 0.00575395281001504, 0.00573831903685290, 0.00571924890124498, 0.00570195607191782, 0.00568929160341229, 0.00567451600192273, 0.00565786097145339, 0.00564352688954271, 0.00563222629558586, 0.00562444592048740, 0.00561603835883661, 0.00560484670522367, 0.00559327216739937, 0.00558169184286040, 0.00557404096838184, 0.00556874735203400, 0.00556192767170637, 0.00556172610675442, 0.00556323646916531, 0.00556212083465553, 0.00556295742734590, 0.00556911670217587, 0.00557630794000305, 0.00558586499270565, 0.00559579308177537, 0.00560575659610886, 0.00561520351710904, 0.00562478581785766, 0.00563928458575201, 0.00565329554347580, 0.00566664258608232, 0.00566925176818505, 0.00567135071107038, 0.00567378628396045, 0.00567500130616661, 0.00568041684494282, 0.00568876469348894, 0.00569998436825491, 0.00571637327145799, 0.00573730151441969, 0.00575900697957428, 0.00578060790917335, 0.00580035913427167, 0.00581915952630288, 0.00583816008696674, 0.00585226659113256, 0.00586384054077720, 0.00587490975537848, 0.00588692870884905, 0.00589761317175438, 0.00590901352667576, 0.00591848489733079, 0.00592856445688934, 0.00594002528279874, 0.00595448618897502, 0.00595926928765472, 0.00596229046652944, 0.00596387948430229, 0.00596638205568046, 0.00597222415652367, 0.00598056641609379, 0.00598931142792134, 0.00600080451157663, 0.00601681938754516, 0.00602542177572456, 0.00602833527250138, 0.00602911101950300, 0.00602892473899458, 0.00602677674063165, 0.00602983611868465, 0.00603429039318466, 0.00603934214067521, 0.00604728007783584, 0.00606266343731972, 0.00607461126366295, 0.00608842605610203, 0.00610146190148479, 0.00611616609159520, 0.00613077567483445, 0.00614096701449047, 0.00614870498098198, 0.00615604942898558, 0.00616477174618219, 0.00617361629457555, 0.00618253754124335, 0.00619139225845435, 0.00620094795527227, 0.00621008605062508, 0.00621850558565987, 0.00622857094033256, 0.00624056428541193, 0.00625916408430423, 0.00628004234531103, 0.00629932171671417, 0.00631501481415659, 0.00633106107180413, 0.00634500730391931, 0.00635444042688754, 0.00636223130022462, 0.00636540007102265, 0.00636985518851541, 0.00637832722935483, 0.00638281860799084, 0.00638020298149096, 0.00637584960059849, 0.00637509184527632, 0.00637119382021968, 0.00636985674378465, 0.00637823669234266, 0.00638513239785775, 0.00639203623606278, 0.00640240660921343, 0.00641379212735512, 0.00642064507886038, 0.00642693085977338, 0.00643690988946733, 0.00644941621751350, 0.00646305875464232, 0.00648247786254479, 0.00650152796860783, 0.00651850651117806, 0.00653948234733367, 0.00655770788485427, 0.00656875953889578, 0.00657366861014931, 0.00657754211891717, 0.00657976986574737, 0.00658541473699970, 0.00659319256334808, 0.00660087218499437, 0.00661070268659680, 0.00662409764326749, 0.00663653603868452, 0.00664672974015826, 0.00666129361667554, 0.00667387055130107, 0.00668418016255078, 0.00669654127731672, 0.00671192137301484, 0.00672102528174524, 0.00672885557223682, 0.00673155672109015, 0.00673589156637130, 0.00673892339560856, 0.00674705303929363, 0.00675847986131490, 0.00676962843353406, 0.00677997288809868, 0.00678766366131268, 0.00679317554422475, 0.00680515730173859, 0.00681784955013459, 0.00683184713682341, 0.00685218435970666, 0.00687089888073561, 0.00688600066404918, 0.00689975883356776, 0.00691129367749495, 0.00691970743302479, 0.00693139242167873, 0.00694086780999770, 0.00695057478356643, 0.00696433579279088, 0.00698166711765723, 0.00700082311760915, 0.00702164150616191, 0.00704158751783847, 0.00705729729710899, 0.00707533374121972, 0.00709322127765849, 0.00710844045946409, 0.00712849350983841, 0.00715093604153858, 0.00717374518020189, 0.00719662054032790, 0.00721417444117246, 0.00723615060366411, 0.00726478123133356, 0.00729633313023656, 0.00732496075375708, 0.00735194689978233, 0.00737646103012818, 0.00739461850062899, 0.00740962259217219, 0.00742696180097745, 0.00744502503065663, 0.00746643097057255, 0.00749043568888283, 0.00751117827387044, 0.00753426529012493, 0.00755404249544560, 0.00755976027819194, 0.00756255849172426, 0.00756647693900527, 0.00756992005683060, 0.00757208719712672, 0.00757599853071472, 0.00758027213990001, 0.00758293908553146, 0.00758163935722898, 0.00757483664842014, 0.00756765341183156, 0.00756291171835797, 0.00756062945348857, 0.00756433598968151, 0.00756620962534601, 0.00756261127730454, 0.00755939105832982, 0.00755711353300978, 0.00755466391224999, 0.00755619597171307, 0.00755851643373230, 0.00756158503278879, 0.00757005822028781, 0.00757835457761826, 0.00758493506430346, 0.00759202191308308, 0.00759542798624226, 0.00759211789556086, 0.00758847969615572, 0.00758423711617882, 0.00757709949483403, 0.00757308581471589, 0.00756735045586604, 0.00756536977821992, 0.00756401123582061, 0.00756439800056634, 0.00756680006007349, 0.00757116628972353, 0.00757709664586096, 0.00757683832668338, 0.00757516982181922, 0.00757015106613442, 0.00756515365870687, 0.00756088525933904, 0.00754803138530610, 0.00753530409824541, 0.00752399001255133, 0.00750825190694196, 0.00748988229633667, 0.00748404981401085, 0.00748173035033862, 0.00747696360941195, 0.00747007902301498, 0.00746275876456354, 0.00745673739010636, 0.00745192122607235, 0.00745107854172781, 0.00745256954762200, 0.00745389065744523, 0.00745092750924360, 0.00744706694921120, 0.00743769406480533, 0.00742423149196440, 0.00740883197253513, 0.00739724627837313, 0.00738503234065466, 0.00737685660150176, 0.00737306494205056, 0.00736923634699455, 0.00736369973692744, 0.00735925510911905, 0.00735551500035854, 0.00734673513352357, 0.00733283366954275, 0.00731719674246959, 0.00729914637821439, 0.00728232422623144, 0.00726806375419130, 0.00725606809948144, 0.00724190458992915, 0.00722461721974334, 0.00720748878584139, 0.00719296618372701, 0.00718147802871759, 0.00716834108206631, 0.00715651472748659, 0.00714131179429235, 0.00712646579609473, 0.00711147957176766, 0.00710054307640511, 0.00709357942418855, 0.00708169919396625, 0.00707252004676792, 0.00706425490139677, 0.00705469002538470, 0.00704346710388603, 0.00702900491033909, 0.00701336041422467, 0.00699216679877777, 0.00696461171023533, 0.00692971291659195, 0.00689284546853282, 0.00685631936172258, 0.00682378944979160, 0.00679133211514410, 0.00676200901189325, 0.00673375372480279, 0.00670955939381818, 0.00668372022451926, 0.00665904631592665, 0.00662651705198009, 0.00659061484684508, 0.00655481276440056, 0.00652161358463988, 0.00649010610502770, 0.00645748692185928, 0.00642124962584986, 0.00638450999737854, 0.00634525564279686, 0.00630312893683944, 0.00626059829464342, 0.00621835479473693, 0.00618106325500164, 0.00613793628461201, 0.00609399517009439, 0.00605607143299859, 0.00601844306879850, 0.00598111989161152, 0.00594646030891548, 0.00591072544575360, 0.00587322074475596, 0.00583572168271477, 0.00579700865109730, 0.00575549934344550, 0.00571443651633468, 0.00567621160161379, 0.00564105794304708, 0.00560321319778005, 0.00556567216679607, 0.00552958276574241, 0.00549604124208586, 0.00545615319385265, 0.00541373453524877, 0.00537186774614374, 0.00532979700090010, 0.00528303085063891, 0.00524132078378384, 0.00520224652130061, 0.00516304919993569, 0.00512405500468922, 0.00508459473444580, 0.00504257764690932, 0.00499569267130298, 0.00494350457910192, 0.00489306622276836, 0.00484687938474367, 0.00480389237074322, 0.00476753542879841, 0.00473620155654986, 0.00470374356846491, 0.00466718263778055, 0.00463111215201566, 0.00459406365803230, 0.00454967956185739, 0.00450624021576407, 0.00446347839097220, 0.00442071656618031, 0.00437795474138842, 0.00433519291659655};
        //h_std = new double[]{0.00734862095823370, 0.00734862095823370, 0.00734862095823370, 0.00734862095823370, 0.00734402577341109, 0.00727571444702949, 0.00697399714439984, 0.00650179346955545, 0.00613128356798967, 0.00587084257607920, 0.00561044112439484, 0.00537155461797767, 0.00517682172189250, 0.00498489731051028, 0.00479109016075606, 0.00459691287566140, 0.00440033518630476, 0.00420262954911641, 0.00400771192902825, 0.00381455722120271, 0.00362129215588673, 0.00342755266056487, 0.00323302981233245, 0.00303685983003347, 0.00284146551030470, 0.00264632261832261, 0.00245150910870078, 0.00225908916816785, 0.00206817969580664, 0.00187746668428547, 0.00168414272429198, 0.00148827940638592, 0.00129403006123509, 0.00109938069278333, 0.000904173919363308, 0.000711073656121422, 0.000521749763376871, 0.000342668035883191, 0.000188793335415181, 0.000158927330955486, 0.000294527387428611, 0.000467144159817643, 0.000647406197866047, 0.000831259016980324, 0.00101971465183502, 0.00120879741452282, 0.00139913893635286, 0.00159134929895170, 0.00178006047630979, 0.00196525170763932, 0.00215130226736395, 0.00233735365945098, 0.00251969262842731, 0.00269729598215872, 0.00287352153574265, 0.00304920836250754, 0.00322493767127405, 0.00340056595762891, 0.00357385928813786, 0.00374957110160830, 0.00392624354174660, 0.00410313601133935, 0.00427882015497966, 0.00445419069910725, 0.00462999200030323, 0.00480526829939162, 0.00498162049901095, 0.00515673903005802, 0.00532805377778932, 0.00549491272038621, 0.00566415885685945, 0.00583184056783994, 0.00599995644108729, 0.00616661416399608, 0.00633187308734308, 0.00649737015542553, 0.00666492905655894, 0.00683165072636977, 0.00700093721606512, 0.00717291854615676, 0.00734441540811344, 0.00751575715332826, 0.00768094399607264, 0.00784573446451715, 0.00800986101614702, 0.00817238818050441, 0.00833365087141558, 0.00849245830263828, 0.00865265512953101, 0.00881164546249011, 0.00897075561441876, 0.00913247882518862, 0.00929543878697886, 0.00945249293129904, 0.00960298911254625, 0.00975485999462025, 0.00990668136201321, 0.0100571802276950, 0.0102062178179907, 0.0103539053515610, 0.0104995293632549, 0.0106456322728428, 0.0107901572169734, 0.0109334475360280, 0.0110711131055007, 0.0112078535474694, 0.0113442771134903, 0.0114821887395516, 0.0116198601512098, 0.0117567403610730, 0.0118934975153826, 0.0120319470223634, 0.0121682685426735, 0.0123023658328978, 0.0124356803753306, 0.0125715539262065, 0.0127074239190460, 0.0128464525395030, 0.0129857158711231, 0.0131230655658710, 0.0132570809235769, 0.0133933498469587, 0.0135296440697797, 0.0136636274026744, 0.0137977101775279, 0.0139317192407952, 0.0140622458936594, 0.0141889374547818, 0.0143118681012871, 0.0144344548178444, 0.0145591030447846, 0.0146850487122497, 0.0148116808601676, 0.0149361644838538, 0.0150616059894355, 0.0151877576629261, 0.0153137803300756, 0.0154416180740215, 0.0155691368362887, 0.0156975255208925, 0.0158267774965657, 0.0159581126730581, 0.0160906187289154, 0.0162198210568312, 0.0163486065668775, 0.0164777136362186, 0.0166063812257756, 0.0167299614729959, 0.0168544934657062, 0.0169762783448906, 0.0170981431323822, 0.0172166529910082, 0.0173316549954243, 0.0174463501376162, 0.0175621770225203, 0.0176809222435005, 0.0177996700740606, 0.0179168426163616, 0.0180340424138189, 0.0181506023638370, 0.0182675174900660, 0.0183864847237020, 0.0185093134730858, 0.0186346556018094, 0.0187581000143317, 0.0188814662731545, 0.0190053697136407, 0.0191333276111237, 0.0192599495319356, 0.0193902649309908, 0.0195208850647948, 0.0196486735525881, 0.0197734204506365, 0.0198972906165287, 0.0200239713295202, 0.0201504641658947, 0.0202768982469859, 0.0203980087382954, 0.0205133819182814, 0.0206283155611560, 0.0207464775567975, 0.0208667660526033, 0.0209828209779409, 0.0210981265178015, 0.0212140508790514, 0.0213309403956921, 0.0214478854214241, 0.0215659230401182, 0.0216837407177762, 0.0218033294930126, 0.0219231006685831, 0.0220363472575793, 0.0221468264014170, 0.0222576863004705, 0.0223639199600616, 0.0224688760455844, 0.0225743881649034, 0.0226781726081665, 0.0227787420841508, 0.0228798712765471, 0.0229836139428262, 0.0230894757370103, 0.0231972813473068, 0.0233067453488622, 0.0234169651179672, 0.0235312156871066, 0.0236493545586732, 0.0237675508053416, 0.0238860695685935, 0.0240046847995428, 0.0241225093414898, 0.0242388210500084, 0.0243539999438132, 0.0244686039417979, 0.0245852804498520, 0.0247033672763114, 0.0248220467530629, 0.0249419492752042, 0.0250631459784198, 0.0251840509149882, 0.0253048852660238, 0.0254231275075157, 0.0255401538647026, 0.0256503415439553, 0.0257602263497881, 0.0258700706316875, 0.0259790153832754, 0.0260871665705539, 0.0261991268517976, 0.0263141819421102, 0.0264283646723470, 0.0265425634948701, 0.0266558260702890, 0.0267665602268874, 0.0268733993457095, 0.0269738413535817, 0.0270759679735132, 0.0271753064122565, 0.0272714007171557, 0.0273658147374761, 0.0274658255816412, 0.0275668030859122, 0.0276682457469448, 0.0277693752978001, 0.0278719275624825, 0.0279756666498956, 0.0280761332383142, 0.0281757427736348, 0.0282762100205665, 0.0283725460681026, 0.0284663502626107, 0.0285555959327478, 0.0286464026936365, 0.0287376240392759, 0.0288314602973315, 0.0289257679926253, 0.0290190234214856, 0.0291065069438309, 0.0291894534234689, 0.0292730322882486, 0.0293538261178751, 0.0294316036819879, 0.0295101704826229, 0.0295943082074849, 0.0296807894024763, 0.0297701190970207, 0.0298599647562230, 0.0299526838772401, 0.0300425724824107, 0.0301278891391918, 0.0302127524086954, 0.0302992609914232, 0.0303872757246869, 0.0304779160978455, 0.0305666920790493, 0.0306497643843064, 0.0307275934072521, 0.0308059941319552, 0.0308871819730857, 0.0309720366220498, 0.0310571258355276, 0.0311412072553685, 0.0312263782012466, 0.0313147432063851, 0.0313986328369683, 0.0314812178573535, 0.0315621155287695, 0.0316418215762390, 0.0317194319108521, 0.0317941410528668, 0.0318686617656098, 0.0319380758577163, 0.0320060351005142, 0.0320737633945554, 0.0321392124321202, 0.0322036498543927, 0.0322667394264229, 0.0323289475025296, 0.0323911752158877, 0.0324565637371258, 0.0325218885425651, 0.0325829902198873, 0.0326486227524829, 0.0327102502664650, 0.0327712352907444, 0.0328362261280176, 0.0329019541856601, 0.0329612998005309, 0.0330200570492150, 0.0330798184780708, 0.0331407477385781, 0.0332043917937519, 0.0332636081508260, 0.0333195656480129, 0.0333755477388825, 0.0334315979267282, 0.0334796811708356, 0.0335276097266768, 0.0335751053528601, 0.0336230601147048, 0.0336753844818747, 0.0337199580732561, 0.0337594481429486, 0.0337963175670732, 0.0338329127681512, 0.0338690442280809, 0.0339029867266878, 0.0339368064250562, 0.0339746292060625, 0.0340107945642650, 0.0340435695018870, 0.0340778582852831, 0.0341112950861266, 0.0341448288922112, 0.0341789214054709, 0.0342081760025208, 0.0342295478390123, 0.0342505211824377, 0.0342708185600148, 0.0342910502024012, 0.0343117593764707, 0.0343294808199449, 0.0343407947422100, 0.0343518610218160, 0.0343640613091362, 0.0343772651759010, 0.0343932440589090, 0.0344129247717264, 0.0344337586675440, 0.0344564716671491, 0.0344768911451291, 0.0344944196775928, 0.0345128786749563, 0.0345324885121771, 0.0345431301813316, 0.0345542291059883, 0.0345666443663238, 0.0345789498948573, 0.0345902955537993, 0.0345985919287354, 0.0346021027885932, 0.0346066169489539, 0.0346105258818447, 0.0346131719026065, 0.0346088850647967, 0.0346040538890763, 0.0346018148395738, 0.0346006927779293, 0.0346083579196807, 0.0346169985944718, 0.0346203810919678, 0.0346215317886197, 0.0346183297217718, 0.0346060819776354, 0.0345929568208283, 0.0345766310513263, 0.0345609649183780, 0.0345475204262987, 0.0345358705919444, 0.0345277615097605, 0.0345093084381188, 0.0344769871651542, 0.0344450363507647, 0.0344125502258897, 0.0343850949262700, 0.0343878992165110, 0.0343916723724629, 0.0343972376570136, 0.0344029880782142, 0.0344053238799610, 0.0344035734179362, 0.0343916454140572, 0.0343752140744153, 0.0343577594795359, 0.0343405413647626, 0.0343300047243877, 0.0343203358477016, 0.0343031883263375, 0.0342856772816701, 0.0342679967990777, 0.0342430996325393, 0.0342039985795943, 0.0341635603529611, 0.0341189660704940, 0.0340750455506292, 0.0340362387291833, 0.0339972612434822, 0.0339582376424400, 0.0339150393328560, 0.0338702535317662, 0.0338169820298028, 0.0337612462487498, 0.0337047618067130, 0.0336489089188636, 0.0335896952910196, 0.0335212185583690, 0.0334519717311014, 0.0333870691010766, 0.0333317811265299, 0.0332793403367169, 0.0332273938203215, 0.0331822337735730, 0.0331401346948602, 0.0331033403454661, 0.0330707820292510, 0.0330391474302049, 0.0330021929693686, 0.0329628035039215, 0.0329236866569996, 0.0328837270753807, 0.0328435110287780, 0.0328036919026839, 0.0327629158714532, 0.0327231121493455, 0.0326741095332269, 0.0326270953157337, 0.0325827366517382, 0.0325394975260260, 0.0324963153604622, 0.0324555472039337, 0.0324183925641864, 0.0323816830295138, 0.0323278867028495, 0.0322741595767807, 0.0322202778205693, 0.0321675714332341, 0.0321207173685937, 0.0320691748319286, 0.0320154986775174, 0.0319618494147846, 0.0319061128906287, 0.0318510326543473, 0.0317954631594781, 0.0317417129863130, 0.0316904239194870, 0.0316290235646507, 0.0315644681174181, 0.0314971240676916, 0.0314362811208535, 0.0313786773085128, 0.0313231526932834, 0.0312685372727979, 0.0312126509698251, 0.0311558998543001, 0.0310972316780311, 0.0310315874883263, 0.0309665790031052, 0.0309038921827862, 0.0308415882533611, 0.0307863830291518, 0.0307370977438831, 0.0306735854370994, 0.0306094836267938, 0.0305465826236717, 0.0304801626365467, 0.0304101501914821, 0.0303421676483191, 0.0302743869800748, 0.0302043722865558, 0.0301415942442319, 0.0300828010551452, 0.0300264563138192, 0.0299723071898234, 0.0299253956328636, 0.0298715540225613, 0.0298154674024037, 0.0297569959101072, 0.0296967331588105, 0.0296380482548037, 0.0295956620034422, 0.0295660723759590, 0.0295510166262446, 0.0295341998635130, 0.0295190040886892, 0.0294897855864083, 0.0294569196503257, 0.0294212439348241, 0.0293838872601443, 0.0293485348425265, 0.0293199125308899, 0.0292983205157612, 0.0292760079674420, 0.0292538042848914, 0.0292348402162690, 0.0292207249954918, 0.0292091171844406, 0.0291977606483557, 0.0291832804573560, 0.0291586262242038, 0.0291240260125306, 0.0290872178922771, 0.0290515957186797, 0.0290183350089068, 0.0290023162894361, 0.0289913606805074, 0.0289799455392845, 0.0289680966056511, 0.0289574360716944, 0.0289320588135111, 0.0289052097029044, 0.0288795472934801, 0.0288587435259407, 0.0288422171034701, 0.0288351393307862, 0.0288270809789929, 0.0288177938607809, 0.0288072861433640, 0.0287878619003343, 0.0287454682691810, 0.0287039490180939, 0.0286619383826142, 0.0286248759853194, 0.0286143470969144, 0.0286045182135597, 0.0285965902623548, 0.0285890795384982, 0.0285740032430467, 0.0285456158433396, 0.0285200217626725, 0.0284944378384598, 0.0284709395927246, 0.0284585023455914, 0.0284417531903241, 0.0284238314397476, 0.0284031535061351, 0.0283791221157322, 0.0283574279643550, 0.0283403513436702, 0.0283238046313731, 0.0283066307912394, 0.0282896021449100, 0.0282688338378726, 0.0282457930596119, 0.0282262380059459, 0.0282084186518669, 0.0282029551157672, 0.0281908459199129, 0.0281754881550719, 0.0281618081894358, 0.0281472812341060, 0.0281324926094262, 0.0281207141968806, 0.0281070693939488, 0.0280933282042381, 0.0280810609414816, 0.0280667169062184, 0.0280553669291020, 0.0280449537058146, 0.0280350020549559, 0.0280237636927339, 0.0280163280875266, 0.0280054666401973, 0.0279909982962177, 0.0279772852300334, 0.0279674682569611, 0.0279627037445701, 0.0279583281646003, 0.0279480010295330, 0.0279318176012572, 0.0279182484948398, 0.0278965821120673, 0.0278760079640027, 0.0278588229935749, 0.0278427155548676, 0.0278296064898257, 0.0278266839914395, 0.0278216203040876, 0.0278122949064284, 0.0278039423292188, 0.0277944677666188, 0.0277691796757388, 0.0277412303002850, 0.0277122194151107, 0.0276862511900243, 0.0276612910718501, 0.0276397114183378, 0.0276174238000241, 0.0275959616067035, 0.0275726283628481, 0.0275448300396717, 0.0275137270746101, 0.0274840271066505, 0.0274550820110187, 0.0274232952438088, 0.0273921591448158, 0.0273632558743512, 0.0273372268244872, 0.0273044065656210, 0.0272686824570499, 0.0272355833280323, 0.0272050680147943, 0.0271787697829580, 0.0271563591167177, 0.0271341881361638, 0.0271126837102029, 0.0270892431132499, 0.0270621535809126, 0.0270395394515417, 0.0270179639589894, 0.0269952289965187, 0.0269728396259286, 0.0269511089175723, 0.0269294066793346, 0.0269033906350610, 0.0268745113094650, 0.0268458256245714, 0.0268207804382454, 0.0268058078769394, 0.0267960433420583, 0.0267883776089639, 0.0267810422601176, 0.0267710439447508, 0.0267519941585085, 0.0267289142016311, 0.0267047489034968, 0.0266877155040359, 0.0266659774185508, 0.0266428916404337, 0.0266185562167936, 0.0265975365898894, 0.0265836621672086, 0.0265694452832392, 0.0265527352739194, 0.0265358903884762, 0.0265165917091428, 0.0264957944918994, 0.0264743969358228, 0.0264556748564909, 0.0264363263082959, 0.0264163686973326, 0.0263975081040319, 0.0263809759895329, 0.0263691757596971, 0.0263550141785527, 0.0263461566705675, 0.0263374660478643, 0.0263280069242893, 0.0263226739166718, 0.0263185176793847, 0.0263138318287458, 0.0263095222494883, 0.0263070570213592, 0.0262978642433741, 0.0262846443385649, 0.0262691641545599, 0.0262545248132306, 0.0262388979256727, 0.0262257794475392, 0.0262121496818410, 0.0261991352519379, 0.0261849459443136, 0.0261682084633485, 0.0261506162117513, 0.0261330558882662, 0.0261180841955606, 0.0261031566747101, 0.0260906175475385, 0.0260801287211575, 0.0260707488636983, 0.0260639098433283, 0.0260600696772146, 0.0260533227810834, 0.0260450438719228, 0.0260346705790896, 0.0260232849106260, 0.0260091263180065, 0.0259928557977940, 0.0259750787508686, 0.0259572097594432, 0.0259403145363426, 0.0259187075900004, 0.0258925429834910, 0.0258727489518572, 0.0258525249487174, 0.0258312516451874, 0.0258052915672499, 0.0257832271272031, 0.0257627814667095, 0.0257443695489311, 0.0257359640392103, 0.0257295236398802, 0.0257234331899762, 0.0257171075719348, 0.0257092533657442, 0.0256808360762303, 0.0256533790210216, 0.0256289667177174, 0.0256046977158496, 0.0255821060515098, 0.0255619178077697, 0.0255438514498719, 0.0255271601363061, 0.0255078800047291, 0.0254921127559640, 0.0254742950661375, 0.0254563568739658, 0.0254337885291913, 0.0254127581167310, 0.0253880981608909, 0.0253566139659283, 0.0253256410824599, 0.0252919277878440, 0.0252520039369972, 0.0252109772019613, 0.0251673935066602, 0.0251210828360061, 0.0250747572049763, 0.0250274208321236, 0.0249804362463499, 0.0249339277657778, 0.0248867790167834, 0.0248390087610571, 0.0247942611570164, 0.0247487966102919, 0.0247085285772710, 0.0246683640244846, 0.0246263978132094, 0.0245801464736734, 0.0245307961830977, 0.0244649380144220, 0.0243932893389413, 0.0243275073654388, 0.0242643430849914, 0.0241946135675963, 0.0241228591010595, 0.0240522093942995, 0.0239758609457588, 0.0238990380975929, 0.0238396535557151, 0.0237860308865932, 0.0237347819476986, 0.0236861473675106, 0.0236359368982784, 0.0235764237274505, 0.0235168069565220, 0.0234583139000461, 0.0233957116472116, 0.0233306419099591, 0.0232813423106657, 0.0232317613544233, 0.0231817552379471, 0.0231320113351074, 0.0230820103623024, 0.0230161318605541, 0.0229411983286553, 0.0228588795906083, 0.0227756752145240, 0.0226955285455522, 0.0226206994364235, 0.0225505083650709, 0.0224809218119955, 0.0224089658223147, 0.0223264074840087, 0.0222324941587315, 0.0221412078697373, 0.0220533064026784, 0.0219707043998282, 0.0218967287246001, 0.0218367316417477, 0.0217702186201143, 0.0217010675107390, 0.0216282011239859, 0.0215544942557435, 0.0214791785193841, 0.0214039441474190, 0.0213253518427492, 0.0212448785980979, 0.0211623620220509, 0.0210771166029218, 0.0209958885616128, 0.0209234783624238, 0.0208510264093706, 0.0207824100681053, 0.0207160894481204, 0.0206451280869420, 0.0205647477567917, 0.0204832143560996, 0.0203978663621071, 0.0203019035936220, 0.0202171166275193, 0.0201312565751041, 0.0200434376915733, 0.0199623853926202, 0.0198844127936991, 0.0198076090862183, 0.0197292636058097, 0.0196509602312360, 0.0195729964250529, 0.0195022942117949, 0.0194313132596798, 0.0193580445912907, 0.0192850309299854, 0.0192156080712180, 0.0191484353277331, 0.0190832969992974, 0.0190190963703179, 0.0189557425732198, 0.0189024763144154, 0.0188558447725355, 0.0188039249531714, 0.0187528899132567, 0.0186970164866042, 0.0186310228049407, 0.0185622870941436, 0.0184983905055049, 0.0184422961878457, 0.0183897878009470, 0.0183425567664507, 0.0183001446392216, 0.0182598021281297, 0.0182141474090020, 0.0181474592474455, 0.0180773649661052, 0.0180076189595584, 0.0179433734726305, 0.0178817733115434, 0.0178249269189411, 0.0177695769839354, 0.0177141357161774, 0.0176565122597740, 0.0175980643116095, 0.0175428629395372, 0.0174918005298173, 0.0174440988116648, 0.0174060057839740, 0.0173695481531952, 0.0173352969456525, 0.0172968382454988, 0.0172524383758096, 0.0172069077255785, 0.0171655930639785, 0.0171227814834299, 0.0170783295265316, 0.0170383723640518, 0.0170001324072451, 0.0169664553834109, 0.0169337398313780, 0.0169008668376694, 0.0168589724313767, 0.0168153292860536, 0.0167718097362181, 0.0167289993538798, 0.0166899354354063, 0.0166524588593202, 0.0166254761183283, 0.0166010615462971, 0.0165819093491924, 0.0165701084609138, 0.0165622999888277, 0.0165537477376497, 0.0165263862355821, 0.0164975859682536, 0.0164669632737794, 0.0164387844469194, 0.0164109551784038, 0.0163866895041507, 0.0163653538637367, 0.0163443761893169, 0.0163153530725079, 0.0162898208204260, 0.0162669323107056, 0.0162411882281011, 0.0162144049693811, 0.0161869535020275, 0.0161583494531724, 0.0161279941682510, 0.0160974626200312, 0.0160737976715995, 0.0160502242965608, 0.0160276284847937, 0.0159996525657600, 0.0159741613892881, 0.0159549441933948, 0.0159371246470999, 0.0159224848687826, 0.0159176238770934, 0.0159117018694918, 0.0159007495533042, 0.0158895959367244, 0.0158789463068884, 0.0158678659053409, 0.0158601605545618, 0.0158536260931266, 0.0158509475507720, 0.0158508146546853, 0.0158533321991725, 0.0158583809371602, 0.0158635732283067, 0.0158678562418396, 0.0158714801829613, 0.0158719735485976, 0.0158499189485838, 0.0158284432896685, 0.0158096879651166, 0.0157923146727990, 0.0157935049806299, 0.0158024168120938, 0.0158169305320591, 0.0158353086092786, 0.0158529061364666, 0.0158666613776589, 0.0158848927426963, 0.0159063555787474, 0.0159278493327055, 0.0159498090901016, 0.0159733139457087, 0.0160026222617168, 0.0160289339553091, 0.0160572998255249, 0.0160809000466091, 0.0161022897364844, 0.0161207525053893, 0.0161377115901870, 0.0161641256298192, 0.0162074795014385, 0.0162524029118095, 0.0163047906168793, 0.0163567285714399, 0.0164108974536131, 0.0164643721037578, 0.0165162176972264, 0.0165598149471131, 0.0166104092116284, 0.0166619767038038, 0.0167097239734378, 0.0167606971404092, 0.0168196768522396, 0.0168848190437233, 0.0169473996362622, 0.0170109799597744, 0.0170801103994093, 0.0171530800229012, 0.0172274772730847, 0.0173096110932842, 0.0173911736905994, 0.0174680903654547, 0.0175409965262565, 0.0176159812344130, 0.0176935095395045, 0.0177750315664938, 0.0178594773247554, 0.0179485142960159, 0.0180376688048147, 0.0181249055943985, 0.0182131608274378, 0.0182980239340948, 0.0183784507458984, 0.0184632709855560, 0.0185550991574944, 0.0186482487159268, 0.0187466101681514, 0.0188525149330600, 0.0189512986430215, 0.0190479204113067, 0.0191452558980808, 0.0192391271469985, 0.0193200319368868, 0.0194045402855527, 0.0194905283462006, 0.0195772676968458, 0.0196701753849858, 0.0197646481250766, 0.0198572817976969, 0.0199502092975464, 0.0200447502805723, 0.0201307832534064, 0.0202228752198985, 0.0203143349436201, 0.0204056288821214, 0.0204985035567248, 0.0206028702938690, 0.0207145029174954, 0.0208269410054655, 0.0209413865363570, 0.0210583159501634, 0.0211746503667909, 0.0212917331526048, 0.0214094004186856, 0.0215276425809584, 0.0216464502184390};
    }

    private double count() {
        splitString();

        //System.out.println(v0);
        //System.out.println(v1);
        //System.out.println(v2);
        //System.out.println(v3);

//        int[] v_tes = {0,
//                1264,
//                1298,
//                1310,
//                1266,
//                1274,
//                1237,
//                1263,
//                1306,
//                1369,
//                1326,
//                1239,
//                1079,
//                1081,
//                1086,
//                1090,
//                1116,
//                1113,
//                1057,
//                1095,
//                1066,
//                1106,
//                1026,
//                1095,
//                1029,
//                1036,
//                1042,
//                952,
//                1032,
//                1016,
//                992,
//                1063,
//                1019,
//                987,
//                1008,
//                1029,
//                952,
//                1032,
//                1034,
//                992,
//                1050,
//                1060,
//                978,
//                1031,
//                1021,
//                1007,
//                966,
//                952,
//                928,
//                908,
//                908,
//                865,
//                844,
//                895,
//                923,
//                889,
//                902,
//                866,
//                899,
//                849,
//                870,
//                868,
//                854,
//                828,
//                815,
//                800,
//                821,
//                883,
//                828,
//                892,
//                855,
//                915,
//                858,
//                905,
//                1005,
//                963,
//                955,
//                968,
//                981,
//                999,
//                1160,
//                1119,
//                1069,
//                1016,
//                1044,
//                941,
//                971,
//                1192,
//                1177,
//                1174,
//                1147,
//                1140,
//                1139,
//                1295,
//                1301,
//                1227,
//                1218,
//                1173,
//                1197,
//                1124,
//                1156,
//                1142,
//                1084,
//                1198,
//                1074,
//                1108,
//                1089,
//                1115,
//                1142,
//                1111,
//                1137,
//                1071,
//                1164,
//                1198,
//                1289,
//                1205,
//                1135,
//                1173,
//                1089,
//                1155,
//                1174,
//                1142,
//                1156,
//                1106,
//                994,
//                889,
//                852,
//                791,
//                787,
//                879,
//                815,
//                673,
//                702,
//                783,
//                689,
//                696,
//                699,
//                655,
//                686,
//                639,
//                642,
//                688,
//                720,
//                731,
//                694,
//                744,
//                665,
//                697,
//                699,
//                778,
//                804,
//                771,
//                710,
//                712,
//                734,
//                686,
//                715,
//                721,
//                704,
//                734,
//                734,
//                765,
//                758,
//                717,
//                678,
//                755,
//                742,
//                715,
//                694,
//                684,
//                715,
//                760,
//                739,
//                721,
//                808,
//                746,
//                731,
//                720,
//                778,
//                841,
//                802,
//                778,
//                841,
//                886,
//                783,
//                783,
//                789,
//                729,
//                841,
//                892,
//                771,
//                804,
//                783,
//                907,
//                812,
//                802,
//                721,
//                718,
//                762,
//                823,
//                833,
//                760,
//                673,
//                650,
//                667,
//                699,
//                650,
//                697,
//                683,
//                692,
//                660,
//                618,
//                668,
//                649,
//                779,
//                707,
//                650,
//                644,
//                686,
//                676,
//                729,
//                739};
//
//        Vector v_test = new Vector();
//        for (int i = 0; i < v_tes.length; i++) {
//            v_test.add(v_tes[i]);
//        }

        double val = 0;
        int m = 0;
        int step = 10;
        Vector v_norm = normalize(v0);
        System.out.println(v_norm.size());
        if (v_norm.size() > step) {
            for (int k = 0; k < v_norm.size() - step; k = k + step) {
                countVarMean(v_norm.subList(k, k + step + 1));
                val += var / mean * (double) 100;
                m = m + 1;
            }
            val = val / m;
        } else {
            countVarMean(Collections.list(v_norm.elements()));
            val += var / mean * (double) 100;
            m = m + 1;
            val = val / m;
            Toast.makeText(getActivity(), "LESS THAN 10 SAMPLES!", Toast.LENGTH_LONG).show();
        }

        //System.out.println(val);

        return val;
    }

    private void countVarMean(List a) {
        double sum = 0;
        int n = a.size();
        for (int i = 0; i < n; i++)
            sum += Double.valueOf(a.get(i).toString());
        mean = sum / (double) n;

        var = 0;
        for (int i = 0; i < n; i++)
            var += (Double.valueOf(a.get(i).toString()) - mean) *
                    (Double.valueOf(a.get(i).toString()) - mean);
        var = var / (n - 1);
    }

    private Vector<Vector> normalize(Vector a) {
        int dataSize = a.size();
        Vector norm_x = linspace(0, 1, dataSize);
        Vector norm_y = new Vector();
        double integral = 0;
        for (int i = 0; i < dataSize; i++)
            integral += Integer.valueOf(a.get(i).toString());
        for (int i = 0; i < dataSize; i++) {
            double temp = Double.valueOf(a.get(i).toString()) / integral;
            norm_y.add(temp);
        }
        Vector<Vector> norm = new Vector<>();
        norm.add(norm_x);
        norm.add(norm_y);
        return norm;
    }

    private Vector linspace(double start, double stop, int n) {
        Vector result = new Vector();
        double step = (stop - start) / (n - 1);
        for (int i = 0; i <= n - 2; i++) {
            result.add(start + (i * step));
        }
        result.add(stop);
        return result;
    }

    private double[] linspaceD(double start, double stop, int n) {
        double[] result = new double[n];
        double step = (stop - start) / (n - 1);
        for (int i = 0; i < n - 1; i++) {
            result[i] = (start + (i * step));
        }
        result[n - 1] = stop;
        return result;
    }

//    private void setImage(double val) {
//        double zielony = 0.020910006884401;
//        double czerwony = 0.002679935043650;
//        double val_bin = (zielony - val) / (zielony - czerwony);
//
//        if (val_bin < 0.125)
//            image.setImageResource(R.drawable.iu2_01);
//        if (val_bin >= 0.125 && val_bin < 0.250)
//            image.setImageResource(R.drawable.iu2_02);
//        if (val_bin >= 0.250 && val_bin < 0.375)
//            image.setImageResource(R.drawable.iu2_03);
//        if (val_bin >= 0.375 && val_bin < 0.5)
//            image.setImageResource(R.drawable.iu2_04);
//        if (val_bin >= 0.5 && val_bin < 0.625)
//            image.setImageResource(R.drawable.iu2_05);
//        if (val_bin >= 0.625 && val_bin < 0.75)
//            image.setImageResource(R.drawable.iu2_06);
//        if (val_bin >= 0.75 && val_bin < 0.875)
//            image.setImageResource(R.drawable.iu2_07);
//        if (val_bin >= 0.875)
//            image.setImageResource(R.drawable.iu2_08);
//
//    }

    private static final double[] interpLinear(double[] x, double[] y, double[] xi) throws IllegalArgumentException {

        if (x.length != y.length) {
            throw new IllegalArgumentException("X and Y must be the same length");
        }
        if (x.length == 1) {
            throw new IllegalArgumentException("X must contain more than one value");
        }
        double[] dx = new double[x.length - 1];
        double[] dy = new double[x.length - 1];
        double[] slope = new double[x.length - 1];
        double[] intercept = new double[x.length - 1];

        // Calculate the line equation (i.e. slope and intercept) between each point
        for (int i = 0; i < x.length - 1; i++) {
            dx[i] = x[i + 1] - x[i];
            if (dx[i] == 0) {
                throw new IllegalArgumentException("X must be montotonic. A duplicate " + "x-value was found");
            }
            if (dx[i] < 0) {
                throw new IllegalArgumentException("X must be sorted");
            }
            dy[i] = y[i + 1] - y[i];
            slope[i] = dy[i] / dx[i];
            intercept[i] = y[i] - x[i] * slope[i];
        }

        // Perform the interpolation here
        double[] yi = new double[xi.length];
        for (int i = 0; i < xi.length; i++) {
            if ((xi[i] > x[x.length - 1]) || (xi[i] < x[0])) {
                yi[i] = Double.NaN;
            } else {
                int loc = Arrays.binarySearch(x, xi[i]);
                if (loc < -1) {
                    loc = -loc - 2;
                    yi[i] = slope[loc] * xi[i] + intercept[loc];
                } else {
                    yi[i] = y[loc];
                }
            }
        }

        return yi;
    }

    private void getGraphFromCloud(String dev) {
        System.out.println("getGraphFromCloud");
        initBackendless();
        String whereClause = "device = '" + dev + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);
        Backendless.Data.of("GRAPHdata").find(queryBuilder,
                new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> foundData) {
                        if (foundData.size() == 1 && foundData.get(0).get("NsdB") != null
                                && foundData.get(0).get("NsdL") != null
                                && foundData.get(0).get("NsdR") != null
                                && foundData.get(0).get("NmeanB") != null
                                && foundData.get(0).get("NmeanL") != null
                                && foundData.get(0).get("NmeanR") != null
                                && foundData.get(0).get("LBsdB") != null
                                && foundData.get(0).get("LBsdL") != null
                                && foundData.get(0).get("LBsdR") != null
                                && foundData.get(0).get("LBmeanB") != null
                                && foundData.get(0).get("LBmeanL") != null
                                && foundData.get(0).get("LBmeanR") != null) {
                            String NsdB = foundData.get(0).get("NsdB").toString();
                            String NsdL = foundData.get(0).get("NsdL").toString();
                            String NsdR = foundData.get(0).get("NsdR").toString();
                            String NmeanB = foundData.get(0).get("NmeanB").toString();
                            String NmeanL = foundData.get(0).get("NmeanL").toString();
                            String NmeanR = foundData.get(0).get("NmeanR").toString();
                            String LBsdB = foundData.get(0).get("LBsdB").toString();
                            String LBsdL = foundData.get(0).get("LBsdL").toString();
                            String LBsdR = foundData.get(0).get("LBsdR").toString();
                            String LBmeanB = foundData.get(0).get("LBmeanB").toString();
                            String LBmeanL = foundData.get(0).get("LBmeanL").toString();
                            String LBmeanR = foundData.get(0).get("LBmeanR").toString();
                            String[] NsdB_array = NsdB.split(",");
                            String[] NsdL_array = NsdL.split(",");
                            String[] NsdR_array = NsdR.split(",");
                            String[] NmeanB_array = NmeanB.split(",");
                            String[] NmeanL_array = NmeanL.split(",");
                            String[] NmeanR_array = NmeanR.split(",");
                            String[] LBsdB_array = LBsdB.split(",");
                            String[] LBsdL_array = LBsdL.split(",");
                            String[] LBsdR_array = LBsdR.split(",");
                            String[] LBmeanB_array = LBmeanB.split(",");
                            String[] LBmeanL_array = LBmeanL.split(",");
                            String[] LBmeanR_array = LBmeanR.split(",");
                            if (NsdB_array.length == 1000
                                    && NsdL_array.length == 1000
                                    && NsdR_array.length == 1000
                                    && NmeanB_array.length == 1000
                                    && NmeanL_array.length == 1000
                                    && NmeanR_array.length == 1000
                                    && LBsdB_array.length == 1000
                                    && LBsdL_array.length == 1000
                                    && LBsdR_array.length == 1000
                                    && LBmeanB_array.length == 1000
                                    && LBmeanL_array.length == 1000
                                    && LBmeanR_array.length == 1000) {
                                h_Nmeanstd = new double[1000][6];
                                h_LBmeanstd = new double[1000][6];
                                for (int i = 0; i < 1000; i++) {
                                    h_Nmeanstd[i][0] = Double.valueOf(NmeanR_array[i]);
                                    h_Nmeanstd[i][1] = Double.valueOf(NsdR_array[i]);
                                    h_Nmeanstd[i][2] = Double.valueOf(NmeanL_array[i]);
                                    h_Nmeanstd[i][3] = Double.valueOf(NsdL_array[i]);
                                    h_Nmeanstd[i][4] = Double.valueOf(NmeanB_array[i]);
                                    h_Nmeanstd[i][5] = Double.valueOf(NsdB_array[i]);
                                    h_LBmeanstd[i][0] = Double.valueOf(LBmeanR_array[i]);
                                    h_LBmeanstd[i][1] = Double.valueOf(LBsdR_array[i]);
                                    h_LBmeanstd[i][2] = Double.valueOf(LBmeanL_array[i]);
                                    h_LBmeanstd[i][3] = Double.valueOf(LBsdL_array[i]);
                                    h_LBmeanstd[i][4] = Double.valueOf(LBmeanB_array[i]);
                                    h_LBmeanstd[i][5] = Double.valueOf(LBsdB_array[i]);
                                }
                                saveDoubleArray(filehealthyN, null);
                                saveDoubleArray(filehealthyN, h_Nmeanstd);
                                saveDoubleArray(filehealthyLB, null);
                                saveDoubleArray(filehealthyLB, h_LBmeanstd);
                            } else {
                                h_Nmeanstd = getDoubleArray(filehealthyN);
                                h_LBmeanstd = getDoubleArray(filehealthyLB);
                            }

                        } else {
                            h_Nmeanstd = getDoubleArray(filehealthyN);
                            h_LBmeanstd = getDoubleArray(filehealthyLB);
                        }
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        System.out.println(fault.getMessage());
                        h_Nmeanstd = getDoubleArray(filehealthyN);
                        h_LBmeanstd = getDoubleArray(filehealthyLB);
                    }
                });

    }

    //to internal storage
    private void saveDoubleArray(String output, double[][] array) {
        FileOutputStream stream = null;
        try {
            stream = context.openFileOutput(output, MODE_PRIVATE);
            ObjectOutputStream dout = new ObjectOutputStream(stream);
            dout.reset();
            dout.writeObject(array);
            dout.flush();
            stream.getFD().sync();
            stream.close();
            System.out.println("saved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //from internal storage
    private double[][] getDoubleArray(String output) {
        double[][] readBack = null;
        FileInputStream stream = null;
        try {
            stream = context.openFileInput(output);
            ObjectInputStream din = new ObjectInputStream(stream);
            readBack = (double[][]) din.readObject();
            System.out.println("get mean, std");
        } catch (Exception e) {
            e.printStackTrace();
            readBack = new double[1000][6];
        }
        return readBack;
    }

    private void performFileSearch() {

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        properties.error_dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        properties.offset = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        properties.extensions = new String[]{"csv"};
        FilePickerDialog dialog = new FilePickerDialog(getActivity(), properties);
        dialog.setTitle("Select a file");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if (files.length > 0) {
                    xy_read = new double[files.length][2][1000];
                    xy_read2 = new double[files.length][2][1000];
                    fileanames = new String[files.length];
                    for (int i = 0; i < files.length; i++) {
                        File csvfile = new File(files[i]);
                        Vector vec0 = new Vector();
                        Vector vec2 = new Vector();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(csvfile));
                            String line = br.readLine();
                            while (line != null) {
                                String[] comma = line.split(",");
                                if (comma.length > 3) {
                                    if (Integer.valueOf(comma[2]) != 0 && Integer.valueOf(comma[4]) != 0){
                                        vec0.add(comma[2]);
                                        vec2.add(comma[4]);
                                    }
                                }
                                line = br.readLine();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!(vec0.isEmpty() || vec2.isEmpty())){
                            xy_read[i] = countData_Plot(vec0, vec2);
                            xy_read2[i] = countData_Plot2(vec0, vec2);
                        }

                        String[] parts = files[i].split("/");
                        fileanames[i] = parts[parts.length-1];
                    }

                    plot = 1;
                    plotMeasurement();
                    plotMeasurement2();
                }
            }
        });
        dialog.show();
    }

    private void processFile(){
        FileChooser fileChooser = new FileChooser(getActivity());
        fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
            @Override
            public void fileSelected(final String[] files) {
                if (files.length > 0) {
                    xy_read = new double[files.length][2][1000];
                    xy_read2 = new double[files.length][2][1000];
                    fileanames = new String[files.length];
                    for (int i = 0; i < files.length; i++) {
                        File csvfile = new File(files[i]);
                        Vector vec0 = new Vector();
                        Vector vec2 = new Vector();
                        try {
                            BufferedReader br = new BufferedReader(new FileReader(csvfile));
                            String line = br.readLine();
                            while (line != null) {
                                String[] comma = line.split(",");
                                if (comma.length > 3) {
                                    if (Integer.valueOf(comma[2]) != 0 && Integer.valueOf(comma[4]) != 0){
                                        vec0.add(comma[2]);
                                        vec2.add(comma[4]);
                                    }
                                }
                                line = br.readLine();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (!(vec0.isEmpty() || vec2.isEmpty())){
                            xy_read[i] = countData_Plot(vec0, vec2);
                            xy_read2[i] = countData_Plot2(vec0, vec2);
                        } else {
                            xy_read[i] = null;
                            xy_read2[i] = null;
                        }

                        String[] parts = files[i].split("/");
                        String part = parts[parts.length-1];
                        fileanames[i] = part.replace(".csv", "");;
                    }

                    plot = 1;
                    plotMeasurement();
                    plotMeasurement2();
                }
            }
        });
        fileChooser.showDialog();
    }


    private void setArrows(){
        int meas = 0;
        int adds = 0;
        if (xy != null){
            meas = 1;
        }
        if (xy_read != null){
            if (xy_read.length == 0) adds = 0;
            else adds = xy_read.length;
        }

        if(meas + adds > 1){
            afterReadLayout.setVisibility(VISIBLE);
            nextbutton.setEnabled(true);
            nextbutton.setAlpha(1.f);
            backbutton.setEnabled(true);
            backbutton.setAlpha(1.f);
            if(plot == 0 || (xy == null && plot == 1)){ //no back button
                backbutton.setEnabled(false);
                backbutton.setAlpha(.2f);
            }
            if(plot == adds){ //no next button
                nextbutton.setEnabled(false);
                nextbutton.setAlpha(.2f);
            }
        }
        else afterReadLayout.setVisibility(View.GONE);

    }

//    public class MyValueFormatter extends ValueFormatter {
//
//        private DecimalFormat mFormat;
//
//        public MyValueFormatter() {
//            mFormat = new DecimalFormat("###,###,##0.0");
//        }
//
//        @Override
//        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//            System.out.println(value);
//            return mFormat.format(value - 0.01);
//        }
//
//    }
}
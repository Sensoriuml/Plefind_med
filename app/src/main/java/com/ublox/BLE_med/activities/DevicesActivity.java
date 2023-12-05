package com.ublox.BLE_med.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.textclassifier.ConversationActions;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ublox.BLE_med.R;
import com.ublox.BLE_med.fragments.ChatFragment;
import com.ublox.BLE_med.interfaces.BluetoothDeviceRepresentation;
import com.ublox.BLE_med.utils.UBloxDevice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.BluetoothDevice.BOND_BONDING;
import static android.bluetooth.BluetoothDevice.BOND_NONE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DevicesActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = DevicesActivity.class.getSimpleName();
    private static final int LOCATION_REQUEST = 255;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;

    private static final int REQUEST_ENABLE_BT = 1;

    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_REMOTE = "remote";

    private static boolean allBle = false;
    private int width, height;
    private Timer timer = new Timer();
    private ImageView img;
    private AnimationDrawable frameAnimation;
    private TextView statustxt, nametxt;

    public static boolean networkConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getActionBar().setTitle("");
        getActionBar().setLogo(R.drawable.plefind_icon);
        getActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_devices);

        img = (ImageView) findViewById(R.id.animationViewConnnect);
        statustxt = (TextView) findViewById(R.id.status_txt);
        nametxt = (TextView) findViewById(R.id.name_txt);

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();


        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.bAllBle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allBle = ((CheckBox) v).isChecked();
            }
        });
/*
        GraphView graph = (GraphView) findViewById(R.id.graphH);
        mSeries1 = new LineGraphSeries<>(generateData());
        graph.addSeries(mSeries1);
*/
        networkConnection();
        bluetoothBeginning();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_devices, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case R.id.menu_remote_control:
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra(EXTRA_REMOTE, true);
                startActivity(mainIntent);
                break;
            case R.id.menu_about:
                scanLeDevice(false);
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
        }
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        /*// Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        *//*
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(generateData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);
*//*
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);

        scanLeDevice(true);*/

        networkConnection();
        bluetoothBeginning();
    }

    private void setListAdapter(BaseAdapter baseAdapter) {
        ListView lvDevices = findViewById(R.id.lvDevices);
        lvDevices.setAdapter(baseAdapter);
        lvDevices.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
       // mHandler.removeCallbacks(mTimer1);
        super.onPause();

        scanLeDevice(false);
        mLeDeviceListAdapter.clear();

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            verifyPermissionAndScan();
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        //invalidateOptionsMenu();
    }

    @TargetApi(23)
    private void verifyPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED) {
            startScan();
        } else {
            requestPermissions(new String[] {ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != LOCATION_REQUEST) return;

        if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
            startScan();
        } else {
            Toast.makeText(this, R.string.location_permission_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void startScan() {
        mScanning = true;
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mLeDeviceListAdapter.getCount() > position) {
            Log.d(TAG, "onListItemClick");
            BluetoothDeviceRepresentation device = mLeDeviceListAdapter.getDevice(position);

            if (mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mScanning = false;
            }

            Intent intent = new Intent(this, MainActivity.class);
            Log.w(TAG, "Putting " + EXTRA_DEVICE + " " + String.valueOf(device == null));
            intent.putExtra(EXTRA_DEVICE, device);
            startActivity(intent);
        }
    }

    static long startTime;
    static long endTime;
    static long fileSize;
    static OkHttpClient client = new OkHttpClient();

    public static void networkConnection(){

        int AVERAGE_BANDWIDTH = 500;
        final int[] kilobytePerSec = new int[1];

        Request request = new Request.Builder()
                .url("https://sensoriumlab.com/wp-content/uploads/2019/07/eit.png")
                .build();

        startTime = System.currentTimeMillis();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                InputStream input = response.body().byteStream();

                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    while (input.read(buffer) != -1) {
                        bos.write(buffer);
                    }
                    byte[] docBuffer = bos.toByteArray();
                    fileSize = bos.size();

                } finally {
                    input.close();
                }

                endTime = System.currentTimeMillis();

                double timeTakenSecs = Math.floor(endTime - startTime) / 1000;  // time taken in seconds
                kilobytePerSec[0] = (int) Math.round(1024 / timeTakenSecs);

                if(kilobytePerSec[0] <= AVERAGE_BANDWIDTH)
                    networkConnection =  false;
                else
                    networkConnection =  true;
            }
        });

    }

    public synchronized void tryConnectCaller() {
        timer.cancel();
        timer = new Timer();

        TimerTask action = new TimerTask() {
            public void run() {
                tryConnect();
            }
        };

        timer.schedule(action, 3000);
    }

    private void tryConnect(){

        int count = mLeDeviceListAdapter.getCount();

        if (count > 0){

            Log.d(TAG, "onListItemClick");
            BluetoothDeviceRepresentation device;

            if (count == 1){
                device = mLeDeviceListAdapter.getDevice(0);
            } else {
                List<Integer> rssivalues = new ArrayList<>();
                int index;
                for (int i = 0; i < count; i++){
                    rssivalues.add(mLeDeviceListAdapter.mDevicesRssi.get(mLeDeviceListAdapter.getDevice(i)));
                }
                try {
                    index = rssivalues.indexOf(Collections.max(rssivalues));
                } catch (ClassCastException e) {
                    System.out.println("Exception thrown : " + e);
                    index = 0;
                } catch (NoSuchElementException e) {
                    System.out.println("Exception thrown : " + e);
                    index = 0;
                }
                device = mLeDeviceListAdapter.getDevice(index);
            }

            //setText(statustxt, "Connecting");
            //setText(nametxt, device.getName());

            if (mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mScanning = false;
            }

            Intent intent = new Intent(this, MainActivity.class);
            Log.w(TAG, "Putting " + EXTRA_DEVICE + " " + String.valueOf(device == null));
            intent.putExtra(EXTRA_DEVICE, device);
            startActivity(intent);
        } else {
            tryConnectCaller();
        }

    }

    private void bluetoothBeginning(){
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        //mLeDeviceListAdapter.clear();
        scanLeDevice(true);

        setText(statustxt, "Scanning");
        setText(nametxt, "");

        img.getDrawable().setAlpha(0);
        img.setBackgroundResource(R.drawable.connecting_animation);
        frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

        tryConnectCaller();
    }

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {

        private ArrayList<BluetoothDeviceRepresentation> mLeDevices;
        private LayoutInflater mInflator;

        private HashMap<BluetoothDeviceRepresentation, Integer> mDevicesRssi = new HashMap<>();

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflator = DevicesActivity.this.getLayoutInflater();
        }

        final byte[] SERIALUUID = {0x11, 0x07, 0x01, (byte) 0xD7, (byte) 0xE9, 0x01, 0x4F, (byte) 0xF3, (byte) 0x44, (byte) 0xE7,
                (byte) 0x83, (byte) 0x8F, (byte) 0xE2, 0x26, (byte) 0xB9, (byte) 0xE1, 0x56, 0x24};

        boolean btAnalizeRecord(byte[] aRecord) {
            byte match = 0;
            int len = -1;
            for (byte data : aRecord) {
                if (len == -1) {
                    len = data & 0xFF;
                    match = 0;
                }
                if (data == SERIALUUID[match])
                    match++;
                else match = 0;
                if (--len == -1) {
                    if (match == SERIALUUID.length) return true;
                }
            }
            return false;
        }

        public void addDevice(BluetoothDeviceRepresentation device, int rssi, byte[] scanRecord) {

            boolean sps;

            sps = btAnalizeRecord(scanRecord);
            if (allBle == false && sps == false) return;

            if (mDevicesRssi.containsKey(device)) {
                int oldRssi = mDevicesRssi.get(device);
                if (Math.abs(oldRssi - rssi) > 10) {
                    mDevicesRssi.put(device, rssi);
                    notifyDataSetChanged();
                }
            } else {
                mDevicesRssi.put(device, rssi);
                notifyDataSetChanged();
            }
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                notifyDataSetChanged();
                //if(device.getAddress().equalsIgnoreCase(.lastConnected)) AutoConnect();
            }

        }

        public BluetoothDeviceRepresentation getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            BluetoothDeviceRepresentation device = mLeDevices.get(i);
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceRssi = view.findViewById(R.id.device_rssi);
                viewHolder.deviceName = view.findViewById(R.id.device_name);
                viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceBonded = view.findViewById(R.id.device_bonded);
                viewHolder.imgRssi = view.findViewById(R.id.img_rssi);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            final View finalView = view;
            view.setOnClickListener(v -> onItemClick(null, finalView, i, i));

            final String deviceName = device.getName();
            final String deviceAddress = device.getAddress();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
                viewHolder.deviceName.setVisibility(View.VISIBLE);
                viewHolder.deviceAddress.setTypeface(null, Typeface.NORMAL);
            }
            else {
                viewHolder.deviceName.setVisibility(View.INVISIBLE);
                //viewHolder.deviceAddress.setTypeface(null, Typeface.BOLD);
            }
            viewHolder.deviceAddress.setText(deviceAddress);
            updateBondedStateComponents(device, viewHolder);
            updateRssiComponents(device, viewHolder);

            return view;
        }

        private void updateBondedStateComponents(BluetoothDeviceRepresentation device, ViewHolder viewHolder) {
            switch(device.getBondState()) {
                case BOND_NONE:
                    viewHolder.deviceBonded.setVisibility(View.INVISIBLE);
                    break;
                case BOND_BONDING:
                    viewHolder.deviceBonded.setText(R.string.bonding_state);
                    viewHolder.deviceBonded.setVisibility(View.VISIBLE);
                    break;
                case BOND_BONDED:
                    viewHolder.deviceBonded.setText(R.string.bonded_state);
                    viewHolder.deviceBonded.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private void updateRssiComponents(BluetoothDeviceRepresentation device, ViewHolder viewHolder) {
            final int rssi = mDevicesRssi.get(device);
            viewHolder.deviceRssi.setText(String.format("%s dBm", String.valueOf(rssi)));
            if(rssi <= -100) {
                viewHolder.imgRssi.setImageResource(R.drawable.signal_indicator_0);
            } else if (rssi < -85) {
                viewHolder.imgRssi.setImageResource(R.drawable.signal_indicator_1);
            } else if (rssi < -70) {
                viewHolder.imgRssi.setImageResource(R.drawable.signal_indicator_2);
            } else if (rssi < -55) {
                viewHolder.imgRssi.setImageResource(R.drawable.signal_indicator_3);
            } else {
                viewHolder.imgRssi.setImageResource(R.drawable.signal_indicator_4);
            }
        }

    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    runOnUiThread(() -> mLeDeviceListAdapter.addDevice(new UBloxDevice(device), rssi, scanRecord));
                }
            };

    static class ViewHolder {
        ImageView imgRssi;
        TextView deviceRssi;
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceBonded;
    }

    double mLastRandom = 2;
    Random mRand = new Random();
    private double getRandom() {
        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
    }




}

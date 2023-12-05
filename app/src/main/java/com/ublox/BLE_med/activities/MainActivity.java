package com.ublox.BLE_med.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ublox.BLE_med.R;
import com.ublox.BLE_med.adapters.ChatAdapter;
import com.ublox.BLE_med.datapump.DataPumpManager;
import com.ublox.BLE_med.fragments.ChatFragment;
import com.ublox.BLE_med.fragments.OverviewFragment;
import com.ublox.BLE_med.fragments.RemoteControlFragment;
import com.ublox.BLE_med.fragments.ServicesFragment;
import com.ublox.BLE_med.fragments.TestFragment;
import com.ublox.BLE_med.interfaces.BluetoothDeviceRepresentation;
import com.ublox.BLE_med.interfaces.ITestInteractionListener;
import com.ublox.BLE_med.server.ServerManager;
import com.ublox.BLE_med.services.BluetoothLeService;
import com.ublox.BLE_med.utils.BLEQueue;
import com.ublox.BLE_med.utils.ConnectionState;
import com.ublox.BLE_med.utils.GattAttributes;
import com.ublox.BLE_med.utils.PhyMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class MainActivity extends Activity implements ActionBar.TabListener,
        OverviewFragment.IOverviewFragmentInteraction, ServicesFragment.IServiceFragmentInteraction,
        ChatFragment.IChatInteractionListener, AdapterView.OnItemSelectedListener,
        ITestInteractionListener, DataPumpManager.IDataPumpListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_DEVICE = "device";
    public static final String EXTRA_REMOTE = "remote";

    private boolean isRemoteMode;
    private boolean showRxInRemoteMode;
    private boolean showTxInRemoteMode;
    private boolean withCreditsInRemoteMode;
    private int mtuSizeForRemote = -1;
    private boolean needToWaitForMtuUpdate = false;
    private int packetSizeForRemote = -1;
    private boolean isFirstTimeToSetFifo = true;
    private boolean isServiceBinded;

    String lastConnected= new String("");

    private TextView tvStatus;
    private RelativeLayout rlProgress;
    private Timer timer = new Timer();

    //private Handler timerHandler;
    //private Runnable timerRunnable;
    //private int  timerCountDown=0;

    private BluetoothDeviceRepresentation mDevice;

    private ArrayList<BluetoothGattCharacteristic> characteristics = new ArrayList<>();
    private BluetoothLeService mBluetoothLeService;
    private static ConnectionState mConnectionState = ConnectionState.DISCONNECTED;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Fragment remoteControlFragment;

    private BluetoothGattCharacteristic characteristicRedLED;
    private BluetoothGattCharacteristic characteristicGreenLED;

    private BluetoothGattCharacteristic characteristicFifo;
    private BluetoothGattCharacteristic characteristicCredits;

    private DataPumpManager mDataPumpManager;

    // Code to manage Service lifecycle.
    private int DownTimer;
    private ImageView img;
    private TextView nametxt;
    private LinearLayout llAnimation;
    private AnimationDrawable frameAnimation;

    public MainActivity() {

     }
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            mDataPumpManager.setBluetoothLeService(mBluetoothLeService);
            // Automatically connects to the device upon successful start-up initialization.
            if (mDevice != null) {
                mBluetoothLeService.connect(mDevice);
                mConnectionState = ConnectionState.CONNECTING;
            }
            invalidateOptionsMenu();
            updateStatus();
            connectingProcess();

            if(!isRemoteMode) {
                //rlProgress.setVisibility(VISIBLE);
                llAnimation.setVisibility(VISIBLE);
                frameAnimation.start();
                nametxt.setText(mDevice.getName());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };



    public final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnectionState = ConnectionState.CONNECTED;
                StorePreferences(mBluetoothLeService.mBluetoothDeviceAddress);
                invalidateOptionsMenu();
                updateStatus();
                connectingProcess();
                sendToActiveFragment(true);
                //rlProgress.setVisibility(View.GONE);
                frameAnimation.stop();
                llAnimation.setVisibility(View.GONE);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnectionState = ConnectionState.DISCONNECTED;
                invalidateOptionsMenu();
                sendToActiveFragment(false);
                updateStatus();
                //rlProgress.setVisibility(View.GONE);
                frameAnimation.stop();
                llAnimation.setVisibility(View.GONE);
                do {
                    tryConnectCaller();
                } while (mConnectionState == ConnectionState.CONNECTED);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                sendToActiveFragment(mBluetoothLeService.getSupportedGattServices());
                updateStatus();
                for (BluetoothGattService service : mBluetoothLeService.getSupportedGattServices()) {
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        String uuid = characteristic.getUuid().toString();
                        if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_RANGE)) {
                            try {
                                mBluetoothLeService.readCharacteristic(characteristic);
                            } catch (Exception ignore) {}
                        } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_GREEN_LED)) {
                            try {
                                characteristicGreenLED = characteristic;
                                mBluetoothLeService.readCharacteristic(characteristic);
                            } catch (Exception ignore) {}
                        } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_RED_LED)) {
                            try {
                                characteristicRedLED = characteristic;
                                mBluetoothLeService.readCharacteristic(characteristic);
                            } catch (Exception ignore) {}
                        } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_X)
                                || uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_Y)
                                || uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_Z)
                                || uuid.equals(GattAttributes.UUID_CHARACTERISTIC_BATTERY_LEVEL)
                                || uuid.equals(GattAttributes.UUID_CHARACTERISTIC_TEMP_VALUE)
                                ) {
                            try {
                                mBluetoothLeService.readCharacteristic(characteristic);
                                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                            } catch (Exception ignore) {}
                        } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_FIFO)) {
                            characteristicFifo = characteristic;
                            sendToActiveFragment(characteristicFifo,true);
                            updateStatus();
                        } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_CREDITS)) {
                            characteristicCredits = characteristic;
                            sendToActiveFragment(characteristicCredits,false);
                            updateStatus();
                        }
                    }
                }

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {


                String extraUuid = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                int extraType = intent.getIntExtra(BluetoothLeService.EXTRA_TYPE, -1);
                byte[] extraData = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);

                sendToActiveFragment(extraUuid, extraType, extraData);

            } else if (BluetoothLeService.ACTION_RSSI_UPDATE.equals(action)) {
                int rssi = intent.getIntExtra(BluetoothLeService.EXTRA_RSSI, 0);
                sendToActiveFragment(rssi);

            } else if (BluetoothLeService.ACTION_MTU_UPDATE.equals(action)) {
                int mtu = intent.getIntExtra(BluetoothLeService.EXTRA_MTU, 0);
                int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, 0);
                sendToActiveFragment(mtu, status);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && BluetoothLeService.ACTION_PHY_MODE_AVAILABLE.equals(action)) {
                final boolean isUpdate = intent.getBooleanExtra(BluetoothLeService.EXTRA_IS_PHY_UPDATE, true);
                final PhyMode txPhyMode = mBluetoothLeService.getTxPhyMode();
                if (!isUpdate) {
                    //sendToTestFragment(txPhyMode);
                }
                updateStatus();
            } else if(BluetoothLeService.ACTION_DESCRIPTOR_WRITE.equals(action) && isRemoteMode
                    && isFirstTimeToSetFifo) { // TODO check other solution
                isFirstTimeToSetFifo = false;
                mDataPumpManager.setCharacteristicCredits(characteristicCredits);
                if (withCreditsInRemoteMode) {
                    mDataPumpManager.toggleCreditsConnection(true);
                }
                if(!needToWaitForMtuUpdate && !mDataPumpManager.isTestRunning()) {
                    mDataPumpManager.startDataPump();
                }
            }
        }
    };

    private List<BluetoothGattService> mServices;

    private void sendToActiveFragment(List<BluetoothGattService> services) {
        mServices = services;
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (ServicesFragment.class.isInstance(fragment)) {
            ServicesFragment servicesFragment = (ServicesFragment) fragment;
            servicesFragment.displayGattServices(services);
        }
    }

    private void sendToActiveFragment(boolean connected) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (TestFragment.class.isInstance(fragment)) {
            if (!connected) {
                View v = fragment.getView();
                TestFragment testFragment = (TestFragment) fragment;
                testFragment.setTxTestToOff(v);
                testFragment.setSwCreditsToOff(v);
                testFragment.updateMTUSize(23);
                mDataPumpManager.reset();
            }
        }

        if(RemoteControlFragment.class.isInstance(fragment)) {
            ((RemoteControlFragment)fragment).writeTransferData(connected ? "Connected!\n\n\r" : "Disconnected!\n\n\r");
            if (connected) {
                if (isRemoteMode) {
                    if (mtuSizeForRemote != -1) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDataPumpManager.setMtuSize(mtuSizeForRemote);
                            }
                        }, 2000);
                    }
                }
            } else {
                if (isServiceBinded) {
                    unbindService(mServiceConnection);
                    isServiceBinded = false;
                }
            }
        }
    }

//    @RequiresApi(26)
//    private void sendToTestFragment(PhyMode txPhyMode) {
//        final int testPosition = 3;
//        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(testPosition);
//        if (TestFragment.class.isInstance(fragment)) {
//            TestFragment testFragment = (TestFragment) fragment;
//            testFragment.setIsLE2MPhySupported(txPhyMode);
//        }
//    }

    private String phyStateToString() {
        PhyMode txPhyMode = mBluetoothLeService.getTxPhyMode();
        PhyMode rxPhyMode = mBluetoothLeService.getRxPhyMode();

        if (txPhyMode == PhyMode.PHY_UNDEFINED) {
            return "";
        } else if (txPhyMode == rxPhyMode) {
            return ", central and peripheral have " + txPhyMode.toString() + " enabled";
        } else if (rxPhyMode == PhyMode.PHY_UNDEFINED){
            return ", central has " + txPhyMode.toString() + " enabled";
        } else {
            return ", only " + (PhyMode.is2MPhyEnabled(txPhyMode) ? "central" : "peripheral") + " has 2 M phy enabled";
        }

    }


    private void sendToActiveFragment(final BluetoothGattCharacteristic characteristic, boolean isFifoCharacteristic) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (ChatFragment.class.isInstance(fragment) && isFifoCharacteristic) {
            ChatFragment chatFragment = (ChatFragment) fragment;
            chatFragment.setCharacteristicFifo(characteristic);
        }

        if (TestFragment.class.isInstance(fragment)) {
                if(isFifoCharacteristic) {
                    mDataPumpManager.setCharacteristicFifo(characteristic);
                } else {
                    mDataPumpManager.setCharacteristicCredits(characteristic);
                }
        }

        if (RemoteControlFragment.class.isInstance(fragment) && isFifoCharacteristic) {
            mDataPumpManager.setCharacteristicFifo(characteristic);
        }
    }

    private void sendToActiveFragment(int mtu, int status) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (TestFragment.class.isInstance(fragment)) {
            if (status == GATT_SUCCESS) {
                mDataPumpManager.updateMtuSizeChanged(mtu);
            } else {
                Toast.makeText(fragment.getActivity(), ("Request MTU - error: " + status), Toast.LENGTH_LONG).show();
            }
        }

        if(RemoteControlFragment.class.isInstance(fragment)) {
            if (status == GATT_SUCCESS) {
                mDataPumpManager.updateMtuSizeChanged(mtu);
                if(packetSizeForRemote == -1) {
                    mDataPumpManager.setPacketSize(mtu - 3);
                }
                needToWaitForMtuUpdate = false;
                if (!mDataPumpManager.isTestRunning() && !isFirstTimeToSetFifo) {
                    mDataPumpManager.startDataPump();
                }
            } else {
                ((RemoteControlFragment)fragment).writeTransferData("Request MTU - error: " + status + "\n\n\r");
            }
        }
    }

    private void sendToActiveFragment(int rssi) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (OverviewFragment.class.isInstance(fragment)) {
            View v = fragment.getView();
            ((TextView) v.findViewById(R.id.tvRSSI)).setText(String.format("%d", rssi));
        }
    }

    private void sendToActiveFragment(String uuid, int type, byte[] data) {
        int changed=0;
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }
       // changed = dataParser.addToBuffer(data);

        if (OverviewFragment.class.isInstance(fragment)) {
            View v = fragment.getView();

           // if(dataParser.deviceParams.ChangeMask!=0) updateFragmentData(dataParser.deviceParams);

            if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_BATTERY_LEVEL)) {
                ((TextView) v.findViewById(R.id.tvBatteryLevel)).setText(String.format("%d", data[0]));
            }

            if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_TEMP_VALUE)) {
               ;// ((TextView) v.findViewById(R.id.tvTemperature)).setText(String.format("%d", data[0]));
            }

            if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_RANGE)) {
                ;// ((TextView) v.findViewById(R.id.tvAccelerometerRange)).setText(String.format("+-%d", data[0]));
            }

            if (type == BLEQueue.ITEM_TYPE_READ) {

                StringBuilder stringBuilder = new StringBuilder(data.length);

                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));

                if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_GREEN_LED)) {
                    /*
                    Switch sGreen = (Switch) v.findViewById(R.id.sGreenLight);
                    if (data[0]  == 0) {
                        sGreen.setChecked(false);
                    } else {
                        sGreen.setChecked(true);
                    }
                    */
                    ;
                } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_RED_LED)) {
                    /*
                    Switch sRed = (Switch) v.findViewById(R.id.sRedLight);
                    if (data[0]  == 0) {
                        sRed.setChecked(false);
                    } else {
                        sRed.setChecked(true);
                    }
                    */;
                }

            } else if (type == BLEQueue.ITEM_TYPE_NOTIFICATION) {
                if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_X)) {
                    ;//((ProgressBar) v.findViewById(R.id.pbX)).setProgress(data[0] + 128);
                } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_Y)) {
                    ;//((ProgressBar) v.findViewById(R.id.pbY)).setProgress(data[0] + 128);
                } else if (uuid.equals(GattAttributes.UUID_CHARACTERISTIC_ACC_Z)) {
                    ;//((ProgressBar) v.findViewById(R.id.pbZ)).setProgress(data[0] + 128);
                }
            }
        } else if (ServicesFragment.class.isInstance(fragment)) {
            View v = fragment.getView();
            Log.i("SERVICE", "gotData, uuid: " + uuid);
            Log.i("SERVICE", "wanted __UUID: " + ((ServicesFragment) fragment).currentUuid);


            if (((ServicesFragment) fragment).currentUuid.equals(uuid)) {
                StringBuilder stringBuilder = new StringBuilder(data.length);

                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));

                TextView tvValue = (TextView) v.findViewById(R.id.tvValue);
                tvValue.setText(new String(data) + "\n<" + stringBuilder.toString() + ">");
                Log.i("SERVICE", "gotData: " + tvValue.getText().toString());
            }

        }  else if (ChatFragment.class.isInstance(fragment)) {
            if (type == BLEQueue.ITEM_TYPE_NOTIFICATION && uuid.equals(GattAttributes.UUID_CHARACTERISTIC_FIFO)) {

                ChatFragment chatFragment = (ChatFragment) fragment;
                chatFragment.addMessage(data);
            }
        } else if (TestFragment.class.isInstance(fragment) || RemoteControlFragment.class.isInstance(fragment)) {
            mDataPumpManager.notifyGattUpdate(uuid, type, data);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_RSSI_UPDATE);
        intentFilter.addAction(BluetoothLeService.ACTION_MTU_UPDATE);
        intentFilter.addAction(BluetoothLeService.ACTION_PHY_MODE_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DESCRIPTOR_WRITE);
        return intentFilter;
    }

    private void updateStatus() {
        switch (mConnectionState) {
            case DISCONNECTED:
                tvStatus.setText(R.string.status_disconnected);
                break;
            case CONNECTING:
                if(!isRemoteMode) {
                    tvStatus.setText(R.string.status_connecting);
                }
                break;
            case CONNECTED:
                int fragmentId = 0;
                switch (fragmentId){
                    default:
                        tvStatus.setText(R.string.status_connected);
                        if (mServices != null && !mServices.isEmpty()) {
                            tvStatus.setText(R.string.status_discovered);
                        }
                        break;
                    case 2:
                        if (characteristicFifo != null) {
                            tvStatus.setText(R.string.status_chat_available);
                        } else {
                            tvStatus.setText(R.string.status_fifo_unavailable);
                        }
                        break;
                    case 3:
                        if (characteristicFifo != null && characteristicCredits != null) {
                            String status = getString(R.string.status_sps_available) + phyStateToString();
                            tvStatus.setText(status);
                        } else {
                            tvStatus.setText(R.string.status_fifo_credits_unavailable);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mGattUpdateReceiver,
                makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDevice);
            Log.d(TAG, "Connect request result=" + result);
            mConnectionState = ConnectionState.CONNECTING;
            invalidateOptionsMenu();
            updateStatus();
            connectingProcess();
            //rlProgress.setVisibility(VISIBLE);
            llAnimation.setVisibility(VISIBLE);
            frameAnimation.start();
            nametxt.setText(mDevice.getName());
        }
        invalidateOptionsMenu();
        connectingProcess();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
            mConnectionState = ConnectionState.DISCONNECTED;
        } catch (Exception ignore) {}
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
        invalidateOptionsMenu();
        connectingProcess();
        //timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLeService != null) {
            if (isServiceBinded) {
                unbindService(mServiceConnection);
                isServiceBinded = false;
            }
            mBluetoothLeService = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle("");
        getActionBar().setLogo(R.drawable.plefind_icon);
        getActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        rlProgress = (RelativeLayout) findViewById(R.id.rlProgress);
        llAnimation = (LinearLayout) findViewById(R.id.llanimation);
        img = (ImageView) findViewById(R.id.animationViewConnnect2);
        nametxt = (TextView) findViewById(R.id.name_txt2);

        img.getDrawable().setAlpha(0);
        img.setBackgroundResource(R.drawable.connecting_animation);
        frameAnimation = (AnimationDrawable) img.getBackground();

        final Intent intent = getIntent();
        isRemoteMode = intent.hasExtra(EXTRA_REMOTE);
        updateStatus();

        LoadPreferences();


        /*
        Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                ;
            }

        };*/

        if(isRemoteMode) {

            mViewPager.setVisibility(View.GONE);
            //rlProgress.setVisibility(View.GONE);
            frameAnimation.stop();
            llAnimation.setVisibility(View.GONE);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            remoteControlFragment = new RemoteControlFragment();
            transaction.add(R.id.llMain, remoteControlFragment);

            transaction.commit();

        } else {
            mDataPumpManager = new DataPumpManager(this);
            connectToDevice((BluetoothDeviceRepresentation) intent.getParcelableExtra(EXTRA_DEVICE));

            // Get a ref to the actionbar and set the navigation mode
            final ActionBar actionBar = getActionBar();
            setTabsWithPagerAdapter(0);

            final String name = mDevice.getName();
            if (!TextUtils.isEmpty(name)) {
                getActionBar().setTitle(name);
            } else {
                getActionBar().setTitle(mDevice.getAddress());
            }

            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    private void connectToDevice(BluetoothDeviceRepresentation bluetoothDevice) {
        // get the information from the device scan
        mDevice = bluetoothDevice;
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        if (!isServiceBinded) { //TODO maybe better to bind anyway
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
            isServiceBinded = true;
        }
    }

    public void connectToDeviceByRemoteControl(Bundle connectInfoBundle) {
        isFirstTimeToSetFifo = true;
        showRxInRemoteMode = connectInfoBundle.getBoolean(ServerManager.BUNDLE_SHOW_RX, false);
        showTxInRemoteMode = connectInfoBundle.getBoolean(ServerManager.BUNDLE_SHOW_TX, false);
        withCreditsInRemoteMode = connectInfoBundle.getBoolean(ServerManager.BUNDLE_WITH_CREDITS, false);
        mDataPumpManager = new DataPumpManager(this);

        mtuSizeForRemote = connectInfoBundle.getInt(ServerManager.BUNDLE_MTU, -1);
        needToWaitForMtuUpdate = mtuSizeForRemote != -1;
        packetSizeForRemote = connectInfoBundle.getInt(ServerManager.BUNDLE_PACKETSIZE, -1);
        if(packetSizeForRemote != -1) {
            mDataPumpManager.setPacketSize(packetSizeForRemote);
        } else {
            mDataPumpManager.setContinuousMode(true);
        }

        connectToDevice((BluetoothDeviceRepresentation) connectInfoBundle.getParcelable(EXTRA_DEVICE));
    }

    private void setTabsWithPagerAdapter(int position) {

        // Initiate the view pager that holds our views
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        updateStatus();

        ChatFragment mChatFragment = (ChatFragment) mSectionsPagerAdapter.getItem(position);

        if (characteristicFifo != null) {
            mChatFragment.setCharacteristicFifo(characteristicFifo);
        }

        mViewPager.setOffscreenPageLimit(3); // Set to num tabs to keep the fragments in memory
    }

    public boolean isConnected() {
        return mConnectionState == ConnectionState.CONNECTED;
    }

    public synchronized void tryConnectCaller() {
        timer.cancel();
        timer = new Timer();

        TimerTask action = new TimerTask() {
            public void run() {
                connectingProcess();
            }
        };

        timer.schedule(action, 300);
    }

    private void connectingProcess(){
         if (mBluetoothLeService != null && mConnectionState == ConnectionState.DISCONNECTED){
            mBluetoothLeService.connect(mDevice);
            mConnectionState = ConnectionState.CONNECTING;
            //invalidateOptionsMenu();
            //updateStatus();
            //rlProgress.setVisibility(VISIBLE);

             runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     llAnimation.setVisibility(VISIBLE);
                     frameAnimation.start();
                     nametxt.setText(mDevice.getName());
                 }
             });

        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connected, menu);
        if(!isRemoteMode) {
            switch (mConnectionState) {
                case CONNECTED:
                    menu.findItem(R.id.menu_connect).setVisible(false);
                    menu.findItem(R.id.menu_disconnect).setVisible(true);
                    break;
                case CONNECTING:
                    menu.findItem(R.id.menu_connect).setVisible(false);
                    menu.findItem(R.id.menu_disconnect).setVisible(false);
                    break;
                case DISCONNECTED:
                    menu.findItem(R.id.menu_connect).setVisible(true);
                    menu.findItem(R.id.menu_disconnect).setVisible(false);
                    break;
            }
        } else {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }*/
//---------------
    private void AutoConnect(BluetoothLeService aService)
    {
        aService.connect(mDevice);
        mConnectionState = ConnectionState.CONNECTING;
        invalidateOptionsMenu();
        updateStatus();
        connectingProcess();
        //rlProgress.setVisibility(VISIBLE);
        llAnimation.setVisibility(VISIBLE);
        frameAnimation.start();
        nametxt.setText(mDevice.getName());
        lastConnected="";
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDevice);
                mConnectionState = ConnectionState.CONNECTING;
                invalidateOptionsMenu();
                updateStatus();
                connectingProcess();
                //rlProgress.setVisibility(VISIBLE);
                llAnimation.setVisibility(VISIBLE);
                frameAnimation.start();
                nametxt.setText(mDevice.getName());
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                updateStatus();
                //rlProgress.setVisibility(VISIBLE);
                llAnimation.setVisibility(VISIBLE);
                frameAnimation.start();
                nametxt.setText(mDevice.getName());
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);
        if(fragment != null && fragment instanceof ServicesFragment &&
                ((ServicesFragment) fragment).isCharacteristicViewVisible()) {
            ((ServicesFragment) fragment).backButtonPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onGreenLight(boolean enabled) {
        byte[] valueOn = {1};
        byte[] valueOff = {0};
        try {
            if (enabled) {
                mBluetoothLeService.writeCharacteristic(characteristicGreenLED, valueOn);
            } else {
                mBluetoothLeService.writeCharacteristic(characteristicGreenLED, valueOff);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void onRedLight(boolean enabled) {
        byte[] valueOn = {1};
        byte[] valueOff = {0};
        try {
            if (enabled) {
                mBluetoothLeService.writeCharacteristic(characteristicRedLED, valueOn);
            } else {
                mBluetoothLeService.writeCharacteristic(characteristicRedLED, valueOff);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void onRead(BluetoothGattCharacteristic characteristic) {
        try {
            mBluetoothLeService.readCharacteristic(characteristic);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void onWrite(BluetoothGattCharacteristic characteristic, byte[] value) {
        try {
            mBluetoothLeService.writeCharacteristic(characteristic, value);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void onNotify(BluetoothGattCharacteristic characteristic, boolean enabled) {
        try {
            mBluetoothLeService.setCharacteristicNotification(characteristic, enabled);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    @Override
    public void onSendMessage(BluetoothGattCharacteristic characteristic, byte[] message) {
        try {
            mBluetoothLeService.writeCharacteristic(characteristic, message);
        } catch (Exception ignore) {}
    }

    @Override
    public void onPhyModeChange(PhyMode mode) {
        mBluetoothLeService.setPreferredPhy(mode);
    }

    @Override
    public void onSwitchCredits(boolean enabled) {
        mDataPumpManager.toggleCreditsConnection(enabled);
    }

    @Override
    public void onSwitchTest(boolean enabled) {
        if (enabled) {
            mDataPumpManager.startDataPump();
        } else {
            mDataPumpManager.stopDataPump();
        }
    }

    @Override
    public void onModeSet(boolean continuousEnabled) {
        mDataPumpManager.setContinuousMode(continuousEnabled);
    }

    @Override
    public void onMtuSizeChanged(int size) {
        mDataPumpManager.setMtuSize(size);
    }

    @Override
    public void onPacketSizeChanged(int size) {
        mDataPumpManager.setPacketSize(size);
    }

    @Override
    public void onBitErrorChanged(boolean enabled) {
        mDataPumpManager.setBitErrorActive(enabled);
    }

    @Override
    public void onReset() {
        mDataPumpManager.resetDataPump();
    }

    @Override
    public void updateConnectionPrio(int connectionParameter) {
        mDataPumpManager.connectionPrioRequest(connectionParameter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
        } catch (Exception ignore) {}
        try {
            mBluetoothLeService.connect(mDevice);
            mConnectionState = ConnectionState.CONNECTING;
            invalidateOptionsMenu();
            updateStatus();
            connectingProcess();
            //rlProgress.setVisibility(VISIBLE);
            llAnimation.setVisibility(VISIBLE);
            frameAnimation.start();
            nametxt.setText(mDevice.getName());
        } catch (Exception ignore) {}
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void updateMTUSize(int size) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (TestFragment.class.isInstance(fragment)) {
            ((TestFragment)fragment).updateMTUSize(size);
        }

        if (RemoteControlFragment.class.isInstance(fragment)) {

            ((RemoteControlFragment)fragment).writeTransferData("MTU size: " + size + "\n\n\r"); //TODO handle here the mtu package relation??
        }
    }

    @Override
    public void updateTxCounter(String value, String average) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (TestFragment.class.isInstance(fragment)) {
            ((TestFragment)fragment).updateTxCounter(value, average);
        }

        if (RemoteControlFragment.class.isInstance(fragment) && showTxInRemoteMode) {
            ((RemoteControlFragment)fragment).writeTxData(value, average);
        }
    }

    @Override
    public void updateRxCounter(String rxValue, String average) {
        Fragment fragment = isRemoteMode ? remoteControlFragment : mSectionsPagerAdapter.getItem(0);

        if (fragment == null) {
            return;
        }

        if (TestFragment.class.isInstance(fragment)) {
            ((TestFragment)fragment).updateRxCounter(rxValue, average);
        }

        if (RemoteControlFragment.class.isInstance(fragment) && showRxInRemoteMode) {
            ((RemoteControlFragment)fragment).writeRxData(rxValue, average);
        }
    }

    @Override
    public void onLastPacketSent() {
        if (isRemoteMode) {
            Fragment fragment = remoteControlFragment;
            if (RemoteControlFragment.class.isInstance(fragment) && !showRxInRemoteMode) {
                ((RemoteControlFragment)fragment).writeResult();
            }
        }


    }

    public void disconnectFromDeviceByRemoteControl() {
        if (mDataPumpManager != null && mBluetoothLeService != null) {
            mDataPumpManager.stopDataPump();
            mBluetoothLeService.disconnect();
            mBluetoothLeService.close();
            if (isServiceBinded) {
                unbindService(mServiceConnection);
                isServiceBinded = false;
            }
            mBluetoothLeService = null;
            mConnectionState = ConnectionState.DISCONNECTED;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateStatus();
                }
            });
        }
    }

    public BluetoothLeService getLeService() {
        return mBluetoothLeService;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private OverviewFragment mOverviewFragment = null;
        private ServicesFragment mServicesFragment = null;
        private ChatFragment mChatFragment = null;
        private TestFragment mTestFragment = null;

        long lastSendLed = 0;

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {


                if (mChatFragment == null) {
                    mChatFragment = ChatFragment.newInstance();
                }

                // Need to have a delay so that this don't get called to often.
                // Else the Bluetooth manager will crash..
                long timeout = (System.currentTimeMillis() - lastSendLed);
                if (timeout > 1000 || timeout < 0) {
                    lastSendLed = System.currentTimeMillis();
                    if (characteristicGreenLED != null)
                        mBluetoothLeService.readCharacteristic(characteristicGreenLED);
                    if (characteristicRedLED != null)
                        mBluetoothLeService.readCharacteristic(characteristicRedLED);
                }

                return mChatFragment;

            } else if (position == 1) {
                if (mServicesFragment == null) {
                    mServicesFragment = ServicesFragment.newInstance();
                }

                return mServicesFragment;
            } else if (position == 2) {
                if (mOverviewFragment == null)
                    mOverviewFragment = OverviewFragment.newInstance();

                return mOverviewFragment;
            } else if (position == 3) {
                if (mTestFragment == null) {
                    mTestFragment = TestFragment.newInstance();
                }

                return mTestFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    private void StorePreferences(String aDeviceAddress) {
        if (lastConnected !=aDeviceAddress) {
            lastConnected = aDeviceAddress;
            SharedPreferences settings = getSharedPreferences("DevInfo", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("BleAddress", lastConnected);
            editor.commit();
        }
    }
    private void LoadPreferences() {
        SharedPreferences settings = getSharedPreferences("DevInfo",0);
        lastConnected = settings.getString("BleAddress","");
    }

}

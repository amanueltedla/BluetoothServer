package com.example.dventus_hq.bluetooth_training;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dventus_hq.bluetooth_training.Bluetooth.ConnectThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int MESSAGE_READ = 1;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ArrayAdapter<String> listAdapter;
    ListView listView;
    TextView textView;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> deviceArray;
    ArrayList<String> pairedDevices;
    ArrayList<BluetoothDevice> devices;
    IntentFilter filter;
    BroadcastReceiver receiver;
    private static final int success_connect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        if (btAdapter == null) {
            Toast.makeText(this, "no bluetooth detected", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            if (!btAdapter.isEnabled()) {

                turnOnBT();
            }

            getPairedDevices();
            startDiscovery();
        }
    }


    private void init() {

        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 0);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = new ArrayList<String>();
        devices = new ArrayList<BluetoothDevice>();
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    String s = "";

                    if (listAdapter.getCount() > 0) {
//                        for (int i = 0; i < listAdapter.getCount(); i++) {
//                            for (int a = 0; a < pairedDevices.size(); a++) {
//                                if (device.getName().equals(pairedDevices.get(a))) {
//
//                                    s = " {Paired} ";
//                                    break;
//                                }
//                            }
//                        }
                    }


                    listAdapter.add(device.getName() + s + "\n" + device.getAddress());

                } else if (BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)) {

                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceName = dev.getName();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        dev.setPairingConfirmation(true);

                    } else {

                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {


                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {


                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                    if (btAdapter.getState() == btAdapter.STATE_OFF) {

                        turnOnBT();
                    }
                }
            }
        };

        registerReceiver(receiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, filter);

    }
    private void turnOnBT() {

        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, 1);
    }

    private void startDiscovery() {

        btAdapter.cancelDiscovery();
        btAdapter.startDiscovery();

    }
    private void getPairedDevices() {

        deviceArray = btAdapter.getBondedDevices();
        if (deviceArray.size() > 0) {

            for (BluetoothDevice device : deviceArray) {

                pairedDevices.add(device.getName());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "bluetooth must be enable to continue", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (btAdapter.isDiscovering()) {
            btAdapter.cancelDiscovery();
        }

        //if (listAdapter.getItem(i).contains("Paired"))
        {

            BluetoothDevice selectedDevice = devices.get(i);
            //ConnectThread connect = new ConnectThread(selectedDevice, i);
            ConnectThread connnet =  new ConnectThread(selectedDevice,btAdapter,mHandler,getApplicationContext());
            connnet.start();


        }
//        else {
//            Toast.makeText(this, "device is not paired", Toast.LENGTH_SHORT).show();
//        }
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Context context = getApplicationContext();
            switch (msg.what) {

                case 0:
                    if (null != context) {
                        Toast.makeText(getApplicationContext(),msg.getData().getString("toast"),
                                Toast.LENGTH_SHORT).show();

                    }
                    break;
                case 1:
                {
                    byte[] readBuf = (byte[]) msg.obj;
                    for (byte b :readBuf) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(String.format("%02X ", b));
                    }
                    break;
                }

            }
        }
    };

}



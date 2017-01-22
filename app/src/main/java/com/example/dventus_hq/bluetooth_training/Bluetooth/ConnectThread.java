package com.example.dventus_hq.bluetooth_training.Bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by AManuel on 7/23/2016.
 */

public class ConnectThread extends Thread {



    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothSocket mmSocket;
    public static BluetoothServerSocket mmServerSocket;
    private BluetoothDevice mmDevice;
    ConnectedThread receiveMessage;
    BluetoothAdapter bluetoothAdapter;
    private Handler mHandler;
    Context mcontext;
    TextView blue_status,valueFrom;
    Dialog dialog;
    private static final int success_connect = 0;

    public ConnectThread()
    {

    }

    public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter, Handler handler, Context context)
    {

       // BluetoothSocket tmp = null;
        mmDevice = device;
        this.bluetoothAdapter = bluetoothAdapter;
        mHandler = handler;
        mcontext = context;
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothServer", MY_UUID);
        }
        catch (IOException e) { }
        mmServerSocket = tmp;

    }

@Override
    public void run() {
    // Keep listening until exception occurs or a socket is returned
    while (true) {
        try {
            mmSocket = mmServerSocket.accept();
        } catch (IOException e) {
            break;
        }
        // If a connection was accepted
        if (mmSocket != null) {
            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(socket);
            Message msg = mHandler.obtainMessage(0);
            Bundle bundle = new Bundle();
            bundle.putString("toast","connected");
            msg.setData(bundle);
            mHandler.sendMessage(msg);
            try {
                receiveMessage = new ConnectedThread(mmSocket, mcontext, mHandler);
                //receiveMessage.write(newbyte);
                receiveMessage.start();

                // mmServerSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                //receiveMessage.run();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }



}

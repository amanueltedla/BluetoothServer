package com.example.dventus_hq.bluetooth_training.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

//import com.dventus.watermeter.Database_and_File_Managment.HandheldDatabaseHelper;
//import com.dventus.watermeter.Database_and_File_Managment.MeterFile;
//import com.dventus.watermeter.tab_view.headend;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by AManuel on 7/23/2016.
 */
 public class ConnectedThread extends Thread {

    private static final int MESSAGE_READ = 1;
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private MeterFile mFile;
    //private HandheldDatabaseHelper dbHandler;
    private Handler mHandler;
    private String mFilename;
   // MeterFile mfile;
    Context mContext;
    SQLiteDatabase db;
    TextView valueFrom;
   // headend hd;



    public ConnectedThread(BluetoothSocket socket , Context context,Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        mContext = context;
        mHandler = handler;
        mFile = new MeterFile();
       // mfile = new MeterFile(mContext);
       // mHandler = handler;
       // mFilename = filename;
        // hd= new headend();
        //this.mContext = mContext;
        //this.valueFrom = valueFrom;

//        try {
//           // dbHandler = new HandheldDatabaseHelper(mContext);
//
//        } catch(SQLiteException e) {
//          //  Toast toast = Toast.makeText(mContext,"Database unavailable", Toast.LENGTH_SHORT);
//          //  toast.show();
//        }
//        // Get the input and output streams, using temp objects because
//        db = dbHandler.getWritableDatabase();
        // member streams are final
        try {

            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();

        } catch (Exception e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        //this.run();
    }

    @Override
    public void run() {

        //byte[] buffer = new byte[9000];  // buffer store for the stream
        //int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
       int ifavailable;
        boolean filerecieved = true;

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        mFilename = df.format(Calendar.getInstance().getTime());
        while(true){
            byte[] newbyte = {0,22,3,4};
            write(newbyte);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        while (true) {
//            try {
//
//                    //hd.sendClicked = false;
//                ifavailable = mmSocket.getInputStream().available();
//                // Read from the InputStream
//                if(ifavailable > 0) {
//                    byte[] paketByte = new byte[ifavailable];
//                    mmInStream.read(paketByte);
//                    StringBuilder sb = new StringBuilder();
//                    for (byte b :paketByte) {
//                        sb.append(String.format("%02X ", b));
//                    }
//                   String sbb = sb.toString();
//                    mFile.addStreamData(sbb,"check.txt");
//               }
//            } catch (Exception e) {
//
//            }
//        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {

            mmOutStream.write(bytes);

        } catch (Exception e) { }
    }


//    Handler mHandler = new Handler(){
//
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what){
//
//                case MESSAGE_READ:
//
//                    byte[] readBuf = (byte[]) msg.obj;
//                   // String readMessage = new String(readBuf, 0, msg.arg1);
//                    //Toast.makeText(getApplicationContext(), readMessage , Toast.LENGTH_LONG).show();
//                    //String string = new String(readBuf);
//                   char afe = (char) readBuf[0];
//                    // Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
//                   // valueFrom.setText("" + afe);
//                    //Toast.makeText(mContext,readMessage,Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };
}

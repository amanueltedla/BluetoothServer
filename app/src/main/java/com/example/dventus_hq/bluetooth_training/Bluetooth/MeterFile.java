package com.example.dventus_hq.bluetooth_training.Bluetooth;


import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

//import java.io.FileOutputStream;

public class MeterFile {
    private Context mContext;
    public MeterFile(Context c)
    {
        mContext = c;
    }
    public MeterFile()
    {

    }




    public void saveMeterFile(String Directory, String Filename, String FileData)
            throws IOException {

        String state;
        state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/" + Directory);
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            File file = new File(Dir, (Filename + ".txt"));

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(FileData.getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(mContext, "Storage not available", Toast.LENGTH_LONG).show();
        }
    }
    public void addStreamData(String mfile, String filename){
        try{
            File Root = Environment.getExternalStorageDirectory();
            File Dir = new File(Root.getAbsolutePath() + "/" + "tanaMeter");
            if (!Dir.exists()) {
                Dir.mkdir();
            }
            FileWriter fw = new FileWriter(Dir + "/" + filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mfile);
            bw.close();
        }
        catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

  public String getFilePath()
  {
      File Root = Environment.getExternalStorageDirectory();
      File Dir = new File(Root.getAbsolutePath() + "/" + "tanaMeter/email.txt");
      //String path = Environment.getExternalStorageDirectory().toString();
      return Dir.toString();
  }

    public void removeAFile(String Filename){


        File Root = Environment.getExternalStorageDirectory();
        File Dir = new File(Root.getAbsolutePath() + "/" + "tanaMeter");
        File file = new File(Dir, (Filename + ".txt"));
        boolean check = false;
        check = file.delete();
        int i;
    }

    public File[] getFileArray(String Directory)
    {
        String path = Environment.getExternalStorageDirectory().toString()+"/" + Directory;
        File f = new File(path);
        File file[] = f.listFiles();
        return file;

    }

    public String[] getFileNameArray(String Directory)
    {
        String path = Environment.getExternalStorageDirectory().toString()+"/" + Directory;
        File f = new File(path);
        File[] file = f.listFiles();
        String Filenames[] = new String[file.length];
        for (int i=0; i < file.length; i++)
        {
            Filenames[i] = file[i].getName().toString();
        }
        return Filenames;
    }

    public void clearDirectory(String Directory){

        String path = Environment.getExternalStorageDirectory().toString()+"/" + Directory;
        File f = new File(path);
        File[] file = f.listFiles();
            if(file != null) {
                int j;
                for(j = 0; j < file.length; j++) {
                    file[j].delete();
                }
    }
    }
}

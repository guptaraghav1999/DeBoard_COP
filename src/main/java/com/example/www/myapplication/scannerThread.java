package com.example.www.myapplication;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneURL;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import static com.example.www.myapplication.MainActivity.s1;
import static com.example.www.myapplication.MainActivity.s2;
import static com.example.www.myapplication.MainActivity.s3;

public class scannerThread extends Thread {
    String[] arr= new String[3];
    TextToSpeech t1;
    public static  Handler handler;
    int i=0;
    Context mContext;
    public  scannerThread(){

//        t1=new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    t1.setLanguage(new Locale("en", "IN"));
//                }
//            }
//        });
    }

    public ScanCallback  cb =new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//
//            Log.d("onScanResult", "    "+Thread.currentThread());
//            final ScanResult result1 =result;
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    List<ADStructure> structures =
//                            ADPayloadParser.getInstance().parse(result1.getScanRecord().getBytes());
//
//                    for (ADStructure structure : structures) {
//                        EddystoneURL es = null;
//                        if (structure.getClass().equals(EddystoneURL.class)) {
//                            es = (EddystoneURL) structure;
//                            URL url = es.getURL();
//                            String s = url.toString();
//                            Character c = s.charAt(8);
//                            if((int)c==49|| (int)c==50||(int)c==51) {
//                                String str="";
//                                for (int j=9;j<s.length();){
//                                    Log.d("Advert    ", str+"   "+Thread.currentThread());
//                                    if((s.charAt(j)=='.')){
//                                        i++;
//                                        break;
//                                    }
//                                    str=str+s.charAt(j);
//                                    j++;
//
//                                }
//                                arr[Integer.parseInt(c+"")-1]=str;
//                                if(arr[0]!=null && arr[1]!=null && arr[2]!=null){
//                                    bluetoothLeScanner.stopScan(cb);
//
//                                    arr[0]=arr[0].replace('_',' ');
//                                    arr[1]=arr[1].replace('_',' ');
//                                    arr[2]=arr[2].replace('_',' ');
//                                    s1.setText(arr[0]);
//                                    s2.setText(arr[1]);
//                                    s3.setText(arr[2]);
//                                    String toSpeak = arr[0] + "  " + arr[1] + "  " + arr[2];
//                                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
//
//                                    i=0;
//                                    arr[0]=null;
//                                    arr[1]=null;
//                                    arr[2]=null;
//
//
//                                }
//
//                            }
//                        }
//                    }
//
//                }
//            });
//
//        }
//
//        @Override
//        public void onBatchScanResults(List< android.bluetooth.le.ScanResult > results) {
//            super.onBatchScanResults(results);
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            super.onScanFailed(errorCode);
//        }
    };

    @Override
    public void run() {

        Looper.prepare();
        handler=new Handler(Looper.myLooper());
        MainActivity.handler=handler;
        Log.d("THD", "run: "+Thread.currentThread()+"   "+ Looper.myQueue());
        Looper.loop();
    }



    public  void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = mContext.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += mContext.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                ((AppCompatActivity)mContext).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d("TAG", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

}

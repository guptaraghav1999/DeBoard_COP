package com.example.www.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneURL;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;
import com.neovisionaries.bluetooth.ble.advertising.TxPowerLevel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Context context;
    public  BluetoothAdapter ba;
    public   ScanCallback cb;
    public   String bus_name="OnBoard_";
    public  String message="";
    int route_no=1;
    BluetoothLeScanner ble=null;
    BluetoothManager bluetoothManager;
    ArrayList<String> devices=new ArrayList<>();
    ArrayList<ScanFilter>filters;
    ScanSettings settings;
    int i=0;
    String[] arr= new String[3];
    String str1 = null;
    String str2 = null;
    String str3 = null;
    public static Handler  handler;
    static TextView s1;
    static TextView s2;
    static TextView s3;
    CheckBox c1;
    CheckBox c2;
    TextToSpeech t1;
    String[] filterlist = {
            "B8:27:EB:3E:4B:F8",
            "B8:27:EB:26:03:CA"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        final TextView info = findViewById(R.id.info);
        if(!isLocationServiceEnabled()){
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
        }
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        ba=bluetoothManager.getAdapter();
        ble=ba.getBluetoothLeScanner();

        filters = new ArrayList<>();
        settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();

        c1=findViewById(R.id.bus1);
        c2=findViewById(R.id.bus2);





                t1=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(new Locale("en", "IN"));
                }
            }
        });

        Button btn=findViewById(R.id.button);
        Button speak = findViewById(R.id.speak);
        s1 = findViewById(R.id.stop1);
        s2 = findViewById(R.id.stop2);
        s3 = findViewById(R.id.stop3);
        cb =new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                final List<ADStructure> structures =
                        ADPayloadParser.getInstance().parse(result.getScanRecord().getBytes());
                Log.d("Structure", "onScanResult: "+" "+structures);
               if(structures.size()==4) {
                   Log.d("TAG", "onScanResult: "+result.getScanRecord().getServiceUuids()+ "     "+result.getDevice().getName());
                   ADStructure structure =structures.get(2);
                               EddystoneURL es = null;
                               if (structure.getClass().equals(EddystoneURL.class)) {
                                   es = (EddystoneURL) structure;
                                   URL url = es.getURL();
                                   String s = url.toString();
                                   Character c = s.charAt(8);
                                   if ((int) c == 49 || (int) c == 50 || (int) c == 51) {
                                       String str = "";
                                       for (int j = 9; j < s.length(); ) {
                                           Log.d("Advert    ", str );
                                           if ((s.charAt(j) == '.')) {
                                               i++;
                                               break;
                                           }
                                           str = str + s.charAt(j);
                                           j++;

                                       }
                                       arr[Integer.parseInt(c + "") - 1] = str;
                                       if (arr[0] != null && arr[1] != null && arr[2] != null) {

                                                   ble.stopScan(cb);

                                                   arr[0] = arr[0].replace('_', ' ');
                                                   arr[1] = arr[1].replace('_', ' ');
                                                   arr[2] = arr[2].replace('_', ' ');
                                                   info.setText("Next Stops are:");
                                                   s1.setText(arr[0]);
                                                   s2.setText(arr[1]);
                                                   s3.setText(arr[2]);
                                                   str1 = arr[0];
                                                   str2 = arr[1];
                                                   str3 = arr[2];
                                                   String toSpeak = arr[0] + "  " + arr[1] + "  " + arr[2];
                                                   t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

                                                   i = 0;
                                                   arr[0] = null;
                                                   arr[1] = null;
                                                   arr[2] = null;

                                       }

                                   }
                               }

                       }


            }

            @Override
            public void onBatchScanResults(List< android.bluetooth.le.ScanResult > results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };


        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str1!=null && str2!=null && str3!= null){
                    String s = str1 + "  " + str2 + "  " + str3;
                    t1.speak(s, TextToSpeech.QUEUE_FLUSH, null);

                }
                else{
                    Toast.makeText(MainActivity.this,"Press Query button",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ba.isEnabled() && ( (c1.isChecked() && !c2.isChecked()) || (c2.isChecked() && !c1.isChecked()) )) {
                    bus_name = bus_name + route_no;
                    checkBTPermissions();
                    info.setText("");
                    str1=null;
                    str2=null;
                    str3=null;
                    s1.setText("");
                    s2.setText("");
                    s3.setText("");
                    Toast.makeText(MainActivity.this,"Searching",Toast.LENGTH_SHORT).show();
                    if(c1.isChecked() && !c2.isChecked()){
                        ScanFilter filter =new ScanFilter.Builder().setDeviceAddress(filterlist[1]).build();
                        filters.add(filter);
                    }

                    if(c2.isChecked() && !c1.isChecked()){
                        ScanFilter filter =new ScanFilter.Builder().setDeviceAddress(filterlist[0]).build();
                        filters.add(filter);
                    }
                    ble.startScan(filters,settings,cb);
                    filters.clear();
                }
                else if(ba.isEnabled() && c1.isChecked() && c2.isChecked()){
                    Toast.makeText(MainActivity.this, "Select one bus", Toast.LENGTH_SHORT).show();
                }
                else if(ba.isEnabled() && !c1.isChecked() && !c2.isChecked()){
                    Toast.makeText(MainActivity.this, "Select one bus", Toast.LENGTH_SHORT).show();
                }
                else{
                    checkBTPermissions();
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            Log.d("TAG", "onActivityResult: ");
            if(resultCode==BluetoothAdapter.STATE_ON){
                ba=bluetoothManager.getAdapter();
                ble=ba.getBluetoothLeScanner();
                bus_name = bus_name + route_no;
                checkBTPermissions();
                s1.setText("");
                s2.setText("");
                s3.setText("");
                if(c1.isChecked() && !c2.isChecked()){
                    ScanFilter filter =new ScanFilter.Builder().setDeviceAddress(filterlist[0]).build();
                    filters.add(filter);
                }

                if(c2.isChecked() && !c1.isChecked()){
                    ScanFilter filter =new ScanFilter.Builder().setDeviceAddress(filterlist[0]).build();
                    filters.add(filter);
                }
                filters.clear();
                ble.startScan(filters,settings,cb);
            }

        }
    }


    public boolean isLocationServiceEnabled(){

        LocationManager locationManager = null;
        boolean gps_enabled= false,network_enabled = false;

        if(locationManager ==null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        try{
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        return gps_enabled || network_enabled;

    }

//    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//    context.startActivity(myIntent);


    public void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d("TAG", "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
}

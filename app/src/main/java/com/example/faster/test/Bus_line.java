package com.example.faster.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class Bus_line extends ActionBarActivity
        implements GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener {
    double latSearch;
    double lngSearch;
    double latFirst;
    double lngFirst;
    String inputOnline;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Bus_line";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //รับค่าจาก activity อื่น
        Intent nameBusStop = getIntent();
        if (nameBusStop != null) {
            inputOnline = nameBusStop.getStringExtra("inputOnline");
            //Toast.makeText(getApplicationContext(), inputOnline, Toast.LENGTH_SHORT).show();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void Onclick1(View view) {
        Button bt_Onclick1 = (Button) findViewById(R.id.buttOnline);
        Geocoder gc = new Geocoder(this);
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(inputOnline, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address address = list.get(0);
        String locality = address.getLocality();
        latSearch = address.getLatitude();
        lngSearch = address.getLongitude();
        Toast.makeText(this,locality+"ละติจูด"+latSearch+"ลองติจูด"+lngSearch,Toast.LENGTH_LONG).show();
        Intent intentt = new Intent(Bus_line.this, Bus_details.class);
        intentt.putExtra("latSearch", latSearch);
        intentt.putExtra("lngSearch", lngSearch);
        intentt.putExtra("latFirst", latFirst);
        intentt.putExtra("lngFirst", lngFirst);
        startActivity(intentt);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
            String lat = String.valueOf(lastLocation.getLatitude());
            latFirst = Double.parseDouble(lat);
            String lng = String.valueOf(lastLocation.getLongitude());
            lngFirst = Double.parseDouble(lng);
            //Toast.makeText(getApplicationContext(), latFirst + " " + lngFirst, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "ไม่สามารถระบุตำแหน่งได้", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: error code = " + connectionResult.getErrorCode());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }
}

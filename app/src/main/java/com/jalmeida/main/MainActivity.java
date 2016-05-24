package com.jalmeida.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jalmeida.R;
import com.jalmeida.data.Temperature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements WeatherService {

    public static TreeMap<String, Temperature> storeMap = new TreeMap<>();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 0;
    public Button retryBtn;
    private static Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Temperature> cityTemp = new ArrayList<>();
    private WeatherActivity service = null;
    private String m_Text = null;
    private String addressStr = "";
    private LocationManager locationManager;
    private ProgressDialog progressDoalog;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();
            Geocoder geocoder = new Geocoder(MainActivity.this);
            try {
                List<Address> fromLocation = geocoder.getFromLocation(lat, lng, 1);
                Address address = fromLocation.get(0);
                addressStr += address.getAddressLine(1) + ", ";
                addressStr += address.getAddressLine(2);
                service.getWeather(addressStr);
                progressDoalog.hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDoalog = new ProgressDialog(MainActivity.this);
        context = this;
        if (isNetworkConnection()) {
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            service = new WeatherActivity(this);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))){
                Toast.makeText(context, "GPS is disabled on your device", Toast.LENGTH_SHORT).show();
                showDialogBox();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_ACCESS_COARSE_LOCATION);
            }
        } else {
            setContentView(R.layout.warning_layout);
            retryBtn = (Button) findViewById(R.id.retryBtn);
            retryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
        }
    }

    public void initControls(TreeMap<String, Temperature> cardValues, String key, int pos) {

        Temperature version = cardValues.get(key);
        Temperature feed = new Temperature();

        feed.setUnits(version.getUnits());
        feed.setItem(version.getItem());
        feed.getItem().setCondition(version.getItem().getCondition());
        feed.setLocation(version.getLocation());
        feed.setTitle(key);
        cityTemp.add(feed);

        recyclerView.setHasFixedSize(true);
        mAdapter = new CardViewActivity(cityTemp);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyItemInserted(pos);
        mAdapter.notifyDataSetChanged();
        stopLocation();
    }

    @Override
    public void serviceSuccess(Temperature temperature) {
        int position = 0;
        if (mAdapter != null) {
            position = storeMap.size();
        } else {
            position = 0;
        }
        if (!(storeMap.containsKey(temperature.getTitle()))) {
            storeMap.put(temperature.getTitle(), temperature);
            initControls(storeMap, temperature.getTitle(), position);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void stopLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    progressDoalog.setMax(100);
                    progressDoalog.setMessage("Searching location....");
                    progressDoalog.setTitle("Please Wait");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    progressDoalog.show();
                } else {
                    showDialogBox();
                }
                break;
        }
    }

    public void showDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Enter Location: ");
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog, null, false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                m_Text = input.getText().toString();
                service.getWeather(m_Text);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private boolean isNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                cm.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                cm.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showDialogBox();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

}
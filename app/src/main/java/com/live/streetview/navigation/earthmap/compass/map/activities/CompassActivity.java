package com.live.streetview.navigation.earthmap.compass.map.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniBillingHelper;
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppAds;
import com.live.streetview.navigation.earthmap.compass.map.Ads.StreetViewAppSoniMyAppShowAds;
import com.live.streetview.navigation.earthmap.compass.map.R;
import com.live.streetview.navigation.earthmap.compass.map.activities.callack.MyLocationCallBack;

import com.live.streetview.navigation.earthmap.compass.map.databinding.ActivityCompassBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

/*public class CompassActivity extends AppCompatActivity implements SensorEventListener, LocationListener {
    private ImageView imagedile, arrowcompass;
    private TextView textDegree;
    private TextView txtLat, txtLong;
    public double comlat;
    public double comlng;
    MyLocation location;
    TextView txtcompasscurrentloc;
    ProgressBar progressBar, progressBar1;
    ConstraintLayout constraintLayout;
    ActivityCompassBinding bindingCompass;


    private float[] gravity = new float[3];
    private float[] geomgnetic = new float[3];
    private float azimuth = 0f;
    private float azimuthCurrent = 0f;
    private SensorManager sensorManager;
    float pressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingCompass = ActivityCompassBinding.inflate(getLayoutInflater());
        setContentView(bindingCompass.getRoot());

        StreetViewAppSoniMyAppShowAds.logAnalyticsForClicks("StreetViewCompassOnCreate", CompassActivity.this);
        imagedile = findViewById(R.id.imagedile);
        textDegree = findViewById(R.id.textfind);
        txtcompasscurrentloc = findViewById(R.id.currentcompassplace);
        progressBar = findViewById(R.id.progressbarcom);
        constraintLayout = findViewById(R.id.constraintLayout18);
        //initBannerAdLiveCompass();

        //call back back press
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        bindingCompass.imagecompassarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });


        PackageManager PM = getPackageManager();
        boolean acc = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        boolean mag = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);

        if (!acc || !mag) {
            Toast.makeText(this, "No Compass supported", Toast.LENGTH_SHORT).show();
        }
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


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

        location = new MyLocation(this, new MyLocationCallBack() {
            @Override
            public void onLocationListener(Location location) {
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                bindingCompass.txtaltilat.setText(String.valueOf(location.getLatitude()));
                bindingCompass.textaltilng.setText(String.valueOf(location.getLongitude()));
                onLocationChanged(location);
            }

            @Override
            public void MyLocationListener(Location location) {

            }
        });
        location.getLocation();

        // startVoiceRecognitionActivity();
        streetViewBannerAdsSmall();
    }

    private void streetViewBannerAdsSmall() {
        StreetViewAppSoniBillingHelper billingHelper = new StreetViewAppSoniBillingHelper(this);
        LinearLayout adContainer = bindingCompass.bannerAdPlace.adContainer;
        AdView adView = new AdView(this);
        adView.setAdUnitId(StreetViewAppSoniMyAppAds.banner_admob_inApp);
        adView.setAdSize(StreetViewAppSoniMyAppAds.getAdSize(this));
        if (billingHelper.isNotAdPurchased()) {
            StreetViewAppSoniMyAppAds.loadSmartToolsBannerForMainMediation(
                    adContainer, adView, this
            );
        } else {
            bindingCompass.bannerID.setVisibility(View.GONE);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final float alpha = 0.97f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geomgnetic[0] = alpha * geomgnetic[0] + (1 - alpha) * event.values[0];
                geomgnetic[1] = alpha * geomgnetic[1] + (1 - alpha) * event.values[1];
                geomgnetic[2] = alpha * geomgnetic[2] + (1 - alpha) * event.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomgnetic);

            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = (float) Math.toDegrees(orientation[0]);

                azimuth = (azimuth + 360) % 360;

                //
                Animation anim = new RotateAnimation(-azimuthCurrent, -azimuth, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                azimuthCurrent = azimuth;
                anim.setDuration(500);
                anim.setRepeatCount(0);
                anim.setFillAfter(true);
                imagedile.startAnimation(anim);

                int a = (int) Math.round(azimuthCurrent);
                textDegree.setText(String.valueOf(a + "°"));
//                binding.txt1Compassnorth.setText(String.valueOf(a));
//                int str_south=a+180;
//                binding.txt1Compasssouth.setText(String.valueOf(str_south));
            }
        }

        pressure = event.values[0];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_PRESSURE);
        if (sensors.size() > 0) {

            sensor = sensors.get(0);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        }
        *//* float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure);*//*

//        binding.txt1CompassDistance.setText(String.valueOf(altitude) + " feet");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        bindingCompass.progressbar2.setVisibility(View.GONE);
        txtcompasscurrentloc.setVisibility(View.VISIBLE);
        if (location != null) {
            comlat = location.getLatitude();
            comlng = location.getLongitude();
            try {
                Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geo.getFromLocation(comlat, comlng, 4);
                //latituteField.setText("Loading...");
                if (addresses != null && addresses.size() > 0) {
               *//* String locality = addresses.get(0).getAddressLine(0);
                String country = addresses.get(0).getCountryName();
                String state = addresses.get(0).getAdminArea();
                String sub_admin = addresses.get(0).getSubAdminArea();
                String city = addresses.get(0).getFeatureName();
                String pincode = addresses.get(0).getPostalCode();*//*
                    String locality_city = addresses.get(0).getLocality();
                    *//*    String sub_localoty = addresses.get(0).getSubLocality();*//*
                    txtcompasscurrentloc.setText(locality_city);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            StreetViewAppSoniMyAppShowAds.mediationBackPressedSimpleStreetViewLocation(
                    CompassActivity.this,
                    StreetViewAppSoniMyAppAds.admobInterstitialAd,
                    StreetViewAppSoniMyAppAds.maxInterstitialAdStreetViewST
            );

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onBackPressedCallback.remove();
    }

}*/

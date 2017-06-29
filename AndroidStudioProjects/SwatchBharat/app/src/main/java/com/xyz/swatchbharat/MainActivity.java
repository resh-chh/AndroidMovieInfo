package com.xyz.swatchbharat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    GoogleApiClient mLocationClient;
    Location mLastLocation;
    ImageView ivPhoto;
    Button btnTakePhoto, btnSharePhoto;
    TextView tvAddress;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnSharePhoto = (Button) findViewById(R.id.btnSharePhoto);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        btnSharePhoto.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 100);
            }
        });

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        mLocationClient = builder.build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mLocationClient != null)
            mLocationClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            photo = (Bitmap) data.getExtras().get("data");
            ivPhoto.setImageBitmap(photo);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation= LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        if(mLastLocation!=null){
            double Latitude = mLastLocation.getLatitude();
            double Longitude = mLastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> fetchAddress = geocoder.getFromLocation(Latitude, Longitude, 1);

                if(fetchAddress !=null) {
                    Address myAddress = fetchAddress.get(0);
                    tvAddress.setText(myAddress.getLocality() + " " + myAddress.getPostalCode());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            tvAddress.setText("Location not found");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        try {
            File file = new File(getExternalCacheDir(), "my_image.png");
            FileOutputStream fout= new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.close();

            Intent sharingIntent= new Intent(Intent.ACTION_SEND);
            String shareBody= tvAddress.getText().toString();
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sharingIntent.setType("image/png");
            startActivity(sharingIntent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
    }
    }
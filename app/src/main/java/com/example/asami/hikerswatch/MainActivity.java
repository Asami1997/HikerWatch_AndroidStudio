package com.example.asami.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView infoText ;

    String info ;


    Geocoder geoCoder ;

    public  void getAddress(Location location)
       {

           infoText.setText("");

           if(String.valueOf(location.getLatitude()) != null)
           {
               infoText.append("Latitude : " + String.valueOf(location.getLatitude()) + System.lineSeparator());
           }

           if(String.valueOf(location.getLongitude()) != null)
           {
               infoText.append("Longitude : " + String.valueOf(location.getLongitude()) + System.lineSeparator());
           }


           if(String.valueOf(location.getAccuracy()) != null)
           {
               infoText.append("Accuracy: " + String.valueOf(location.getAccuracy()) + System.lineSeparator());
           }

           if(String.valueOf(location.getAltitude()) != null)

           {

               infoText.append("Altitude: " + String.valueOf(location.getAltitude()) + System.lineSeparator());
           }
           try {
               List<Address> address = geoCoder.getFromLocation(location.getLongitude(),location.getLatitude(),1);

               infoText.append(System.lineSeparator());

               info +="Couldn't Find Address. ";
               if(address!=null && address.size()>0)
               {
                   info = "";
                   info+= "Address :" + System.lineSeparator();
                   if(address.get(0).getSubThoroughfare()!=null)
                   {
                       info+=address.get(0).getSubThoroughfare()+ "" +  System.lineSeparator();
                   }

                   if(address.get(0).getThoroughfare()!=null)
                   {
                       info+=address.get(0).getThoroughfare()+ "" +  System.lineSeparator();
                   }

                   if(address.get(0).getLocality()!=null)
                   {
                       info+=address.get(0).getLocality() + "" +  System.lineSeparator();

                   }

                   if(address.get(0).getPostalCode()!=null)
                   {
                       info+=address.get(0).getPostalCode() + "" +  System.lineSeparator();

                   }


                   if (address.get(0).getCountryName()!=null)

                   {
                       info+=address.get(0).getCountryName() + "" + System.lineSeparator();
                   }

               }

               infoText.append(info);

           } catch (IOException e) {
               e.printStackTrace();
           }

       }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoText = (TextView) findViewById(R.id.infoId);
        geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        info= "";
         locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

         locationListener = new LocationListener() {
             @Override
             public void onLocationChanged(Location location) {




                 //now get the address from the address from the latitude and longitude
                 getAddress(location);

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

         // check if permission is granted

        if(Build.VERSION.SDK_INT < 25)
           {
                  infoText.setText("");
                  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
           }else
               {
                   if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                   {
                       ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

                   }else
                   {
                       infoText.setText("");
                       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                       Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                       getAddress(lastLocation);

                   }
               }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
           {
               if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                  {
                      infoText.setText("");
                      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                 }

           }
    }


}

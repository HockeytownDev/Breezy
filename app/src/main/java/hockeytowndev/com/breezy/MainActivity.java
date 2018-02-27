package hockeytowndev.com.breezy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //Constants
    private final String TAG = MainActivity.class.getSimpleName();
    private String OWM_URL = "http://api.openweathermap.org/data/2.5/weather";
    private String APP_ID = "03d46cba9e2d859bd9f36371e2cdfed1";
    private CurrentWeather mCurrentWeather;
    //Time between location updates
    private long MIN_TIME = 5000;
    //Distance between location updates
    private float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 123;

    //Set Location Provider
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;

    //Declare location manager and listener
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    //Variables
    @BindView(R.id.temperatureLabel)
    TextView mTemperature;
    @BindView(R.id.degreeImageView)
    ImageView mDegreeImageView;
    @BindView(R.id.locationLabel)
    TextView mLocation;
    @BindView(R.id.iconImageView)
    ImageView mIcon;
    @BindView(R.id.tempLowLabel)
    TextView mTempLowLabel;
    @BindView(R.id.tempLowValue)
    TextView mTempLow;
    @BindView(R.id.tempHighLabel)
    TextView mTempHighLabel;
    @BindView(R.id.tempHighValue)
    TextView mTempHigh;
    @BindView(R.id.windSpeedLabel)
    TextView mWindLabel;
    @BindView(R.id.windSpeedValue)
    TextView mWindSpeed;
    @BindView(R.id.summaryLabel)
    TextView mSummary;
    @BindView(R.id.windDegreesLabel)
    TextView mWindDegLabel;
    @BindView(R.id.windDegreesValue)
    TextView mWindDegrees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind butterknife from Variables
        ButterKnife.bind(this);


    }

    //Add onResume
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Breezy", "onResume() called");
        Log.i("Breezy", "Getting weather for current location");
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("Breezy", "onLocationChanged() callback received");

                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                Log.i("Breezy", "longitude is: " + longitude);
                Log.i("Breezy", "latitude is: " + latitude);

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

                Log.i("Breezy", "onProviderDisabled() callback received. Provider: " + provider);
                Toast.makeText(MainActivity.this, "Network failed", Toast.LENGTH_LONG).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.i("Breezy", "onRequestPermissionsResult(): Permission granted!");
                getWeatherForCurrentLocation();
            }else{
                Log.i("Breezy", "Permission denied.");
            }
        }
    }

    //Connect to the network
    private void letsDoSomeNetworking(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(OWM_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){

                Log.i("Breezy", "Success! JSON: " + response.toString());
                CurrentWeather weatherData = CurrentWeather.fromJson(response);
                updateDisplay(weatherData);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                Log.e("Breezy", "Fail " + e.toString());
                Log.i("Breezy", "Status code: " + statusCode);
                Toast.makeText(MainActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }

        });
    }


        //Update display
        private void updateDisplay(CurrentWeather weatherData) {
            mLocation.setText(weatherData.getLocation());
            mTemperature.setText(weatherData.getTemperature());
            mWindSpeed.setText(weatherData.getWindSpeed() + " mph");
            mWindDegrees.setText(weatherData.getWindDegrees() + "°");
            mTempLow.setText(weatherData.getTempLow() + "°");
            mTempHigh.setText(weatherData.getTempHigh() + "°");
            mSummary.setText(weatherData.getSummary());

            int resourceID = getResources().getIdentifier(String.valueOf(weatherData.getIcon()), "drawable", getPackageName());
            mIcon.setImageResource(resourceID);

    }

    //Freeing up resources on paused state
    @Override
    protected void onPause(){
        super.onPause();

        if (mLocationManager != null) mLocationManager.removeUpdates(mLocationListener);
    }







}
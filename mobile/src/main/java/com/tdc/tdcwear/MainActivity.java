package com.tdc.tdcwear;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.tdc.common.Constants;
import com.tdc.common.model.Forecast;
import com.tdc.common.model.Temp;
import com.tdc.common.model.Weather;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MessageApi.MessageListener {

    private static final String LOG_TAG = "Mobile Main Activity";

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = getGoogleApiClient(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(LOG_TAG, "ON CONNECTED");
//        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
//        Wearable.NodeApi.addListener(mGoogleApiClient, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "ON CONNECTION SUSPENDED: " + i);

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e(LOG_TAG, "ON MESSAGE RECEIVED: "+messageEvent.toString());
        getForecast();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "ON CONNECTION FAILED: " + connectionResult);
    }

    public interface ForecastService {
        @GET("/{directory}")
        void listObjects(@Path("directory") String directory, @QueryMap Map<String, String> queryMap, Callback<Forecast> forecastCallback);
    }


    public void sendDataToWearable(Forecast forecast){

        if(forecast != null && forecast.getList().size() > 0){

            Temp temp = null;
            Weather weather = null;
            try {
                temp = forecast.getList().get(0).getTemp();
                weather = forecast.getList().get(0).getWeather().get(0);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            if(temp != null && weather != null){
                PutDataMapRequest dataMap = PutDataMapRequest.create(Constants.PATH);
                dataMap.getDataMap().putLong("time", new Date().getTime());
                dataMap.getDataMap().putString(Constants.CITY_NAME, forecast.getCity().getName());
                dataMap.getDataMap().putString(Constants.TEMP_DAY, Double.toString(temp.getDay()));
                dataMap.getDataMap().putString(Constants.MAIN, weather.getMain());
                dataMap.getDataMap().putString(Constants.DESCRIPTION, weather.getDescription());

                PutDataRequest request = dataMap.asPutDataRequest();
                Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                        .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                            @Override
                            public void onResult(DataApi.DataItemResult dataItemResult) {
                                Log.e(LOG_TAG, "Sending forecast was successful: " + dataItemResult.getStatus()
                                        .isSuccess());
                            }
                        });

            }
        }
    }


    public void getForecast(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org/data/2.5/forecast/")
                .build();

        ForecastService service = restAdapter.create(ForecastService.class);
        service.listObjects("daily", getQueryMap(), new Callback<Forecast>() {
            @Override
            public void success(Forecast forecast, Response response) {
                Log.e("RESPONSE: ", forecast.toString());
                sendDataToWearable(forecast);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("FAIL: ", error.getMessage());
            }
        });
    }

    public Map<String, String> getQueryMap(){

        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(QUERY_PARAM, "Florianopolis");
        map.put(FORMAT_PARAM, "json");
        map.put(UNITS_PARAM, "metric");
        map.put(DAYS_PARAM, "7");

        return map;

    }

}

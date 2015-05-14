package com.tdc.tdcwear;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.tdc.common.Constants;
import com.tdc.common.DataMapParcelableUtils;
import com.tdc.common.model.Forecast;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        MessageApi.MessageListener, NodeApi.NodeListener{

    private static int CONNECTION_TIME_OUT_MS = 50000;
    private static final String LOG_TAG = "WEAR MAIN ACTIVITY";

    private GoogleApiClient mGoogleApiClient;
    private String nodeId;

    @InjectView(R.id.city_name)
    TextView mCityName;
    @InjectView(R.id.temperature)
    TextView mTemperature;
    @InjectView(R.id.main_info)
    TextView mMainInfo;
    @InjectView(R.id.description)
    TextView mMDescription;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;

    private boolean alreadySet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_common_layout);
        ButterKnife.inject(this);

        retrieveDeviceNode();

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

    private void retrieveDeviceNode() {
        mGoogleApiClient = getGoogleApiClient(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();

                    callWeatherService();

//                    sendToast();
                }
            }
        }).start();
    }

    private void sendToast() {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, "MY MESSAGE FROM WEAR", null);
//                    mGoogleApiClient.disconnect();
                }
            }).start();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(LOG_TAG, "ON CONNECTED");

        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);


    }

    public void callWeatherService(){

        if(!alreadySet){
            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Constants.WEATHER_SERVICE_PATH);
            putDataMapRequest.getDataMap().putLong("time", new Date().getTime());

            Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest())
                    .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            Log.e(LOG_TAG, "Calling forecast was successful: " + dataItemResult.getStatus()
                                    .isSuccess());
                        }
                    });
        }


    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.e(LOG_TAG, "ON CONNECTION SUSPENDED");

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.e(LOG_TAG, "ON DATA CHANGED");

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                if (path.equals(Constants.PATH)) {

                    Log.e(LOG_TAG, "IS OUR FORECAST PATH!");

                    DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());

                    final String cityName = dataMapItem.getDataMap()
                            .getString(Constants.CITY_NAME);
                    final String temperature = dataMapItem.getDataMap()
                            .getString(Constants.TEMP_DAY);
                    final String main = dataMapItem.getDataMap()
                            .getString(Constants.MAIN);
                    final String description = dataMapItem.getDataMap()
                            .getString(Constants.DESCRIPTION);

                    Forecast forecast = DataMapParcelableUtils.getParcelable(dataMapItem.getDataMap(), Constants.FORECAST_PARCEL, Forecast.CREATOR);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);

                            mCityName.setText(cityName);
                            mTemperature.setText(temperature + "ºC");
                            mMainInfo.setText(main);
                            mMDescription.setText(description);
                        }
                    });

                    alreadySet = true;
                    mGoogleApiClient.disconnect();

                    Log.e(LOG_TAG, "MAIN: "+main);

                }

            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.e(LOG_TAG, "TYPE DELETED");
            } else {
                Log.e(LOG_TAG, "OTHER TYPE");
            }
        }


    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.e(LOG_TAG, "ON MESSAGE RECEIVED");

    }

    @Override
    public void onPeerConnected(Node node) {
        Log.e(LOG_TAG, "ON PEER CONNECTED");

    }

    @Override
    public void onPeerDisconnected(Node node) {
        Log.e(LOG_TAG, "ON PEER DISCONNECTED");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "ON CONNECTION FAILED");

    }
}

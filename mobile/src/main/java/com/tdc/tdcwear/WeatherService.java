package com.tdc.tdcwear;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.tdc.tdcwear.model.Forecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public class WeatherService extends Service {

    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public interface ForcastService {
        @GET("/{directory}")
        void listObjects(@Path("directory") String directory, Callback<Forecast> forecastCallback);
    }

    RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://api.openweathermap.org/data/2.5/forecast/")
            .build();

    public void getForecast(){
        ForcastService service = restAdapter.create(ForcastService.class);
        service.listObjects("daily", new Callback<Forecast>() {
            @Override
            public void success(Forecast forecast, Response response) {
                Log.e("RESPONSE: ", forecast.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("FAIL: ", error.getMessage());
            }
        });
    }



    private class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(LOG_TAG, "ON PRE EXECUTE");

        }

        @Override
        protected Void doInBackground(String... params) {

            String format = "json";
            String units = "metric";
            int days = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                //http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
//                http://api.openweathermap.org/data/2.5/forecast/daily?q=florianopolis&mode=json&units=metric&cnt=7

                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(days))
                        .build();

                Log.d(LOG_TAG, "BUILT URI " + builtUri.toString());
                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, forecastJsonStr);








            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error "+e.getMessage(), e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }

            Log.d(LOG_TAG, "ON POST EXECUTE");

        }

    }

}

package es.uniovi.eii.bestfood;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonTask extends AsyncTask<String, String, String> {


    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                Log.d("Response: ", "> " + line);

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        String jsonString = result;
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonString);
            JSONObject jsonResponse = obj.getJSONObject("product").getJSONObject("nutriments");

            String salt = jsonResponse.getString("salt_100g");
            String carbohydrates = jsonResponse.getString("carbohydrates_100g");
            String energy = jsonResponse.getString("energy-kcal_100g");
            String proteins = jsonResponse.getString("proteins_100g");
            String saturated = jsonResponse.getString("saturated-fat_100g");


            jsonResponse = obj.getJSONObject("product").getJSONObject("nutriscore_data");
            String scoreLetter = jsonResponse.getString("grade");
            String scoreNumber = jsonResponse.getString("score");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

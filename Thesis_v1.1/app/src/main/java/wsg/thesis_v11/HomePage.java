package wsg.thesis_v11;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.telephony.SignalStrength;
import android.view.Menu;
import android.view.MenuItem;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.text.format.Time;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.net.URI;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;

import wsg.thesis_v11.LoginPage;
import wsg.thesis_v11.R;


public class HomePage extends Activity{

    // UI references
    private View mLogoView;
    private TextView mTemp;
    private TextView mTemp2;
    private TextView mTemp3;
    private TextView mTemp4;
    private TextView mTemp5;
    TelephonyManager TelephonManager;
    myPhoneStateListener pslistener;
    Time instant;
    LocationManager locationManager;
    LocationListener locationListener;
    String provider;
    String carrierName;
    double lat;
    double lng;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        sharedPreferences = getSharedPreferences("Ampalaya", Context.MODE_PRIVATE);
        //setup home page
        try {
            pslistener = new myPhoneStateListener();
            TelephonManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            TelephonManager.listen(pslistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        final TextView mTemp = (TextView) findViewById(R.id.tempSS);
        final TextView mTemp2 = (TextView) findViewById(R.id.tempGPS);
        final TextView mTemp3 = (TextView) findViewById(R.id.tempDT);
        final TextView mTemp4 = (TextView) findViewById(R.id.tempPts);
        final TextView mTemp5 = (TextView) findViewById(R.id.tempNetProvider);

        instant = new Time(Time.getCurrentTimezone());
        final String username = sharedPreferences.getString("username","");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                provider = location.getProvider();
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        /*if(LocationManager.NETWORK_PROVIDER != null) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }*/

        Button mLeaderBoardsButton = (Button) findViewById(R.id.leader_boards_button);
        mLeaderBoardsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LeaderboardsPage.class));
            }
        });

        Button mAchievements = (Button) findViewById(R.id.achievements_button);
        mAchievements.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(),AchievementsPage.class));
            }
        });

        Button mQuests = (Button) findViewById(R.id.quests_button);
        mQuests.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(),QuestsPage.class));
            }
        });

        Button mStory = (Button) findViewById(R.id.story_button);
        Button mAbout = (Button) findViewById(R.id.about_button);
        mAbout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        final RequestParams params = new RequestParams();
        String deviceName = Build.MODEL +" "+ Build.DEVICE;
        params.put("device_name",deviceName);
        final TelephonyManager manager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        carrierName = manager.getNetworkOperatorName();
        mTemp5.setText("Network Provider: "+carrierName);

        final JsonHttpResponseHandler getPoints = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                try{
                    String getPt = response.getString("score");
                    mTemp4.setText("Points: "+getPt);
                } catch(Exception e){
                    mTemp4.setText("json points error");
                }
            }
            @Override
            public void onFailure(int statusCode,Header[] headers,Throwable e,JSONObject response){
                mTemp4.setText("points did not work");
            }
        };
        try{
            String url = "http://128.199.134.172/api/get_public_user_profile?user="+username;
            client.get(url,getPoints);
        } catch(Exception e){
            mTemp4.setText("points url error");
        }

        Button mSendSignal = (Button) findViewById(R.id.signal_report_button);
        mSendSignal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                        //mTemp.setText(response.toString());
                    }
                    @Override
                    public void onFailure(int statusCode,Header[] headers,Throwable e,JSONObject response){
                        //mTemp.setText(response.toString());
                    }
                };

                if(carrierName.length()>0){
                    mTemp5.setText("Network Provider: "+carrierName);
                }
                else{
                    mTemp5.setText("Network Provider: No Service");
                }

                int SignalStrength = pslistener.getSS();
                if(SignalStrength == 0) {
                    mTemp.setText("No Signal");
                }
                else
                    mTemp.setText("Signal Strength: " + Integer.toString(SignalStrength) + "dBm");

                params.put("signal_strength", Integer.toString(SignalStrength));
                params.put("carrier_string", "AmpalayaNet");
                params.put("gps_lat",Double.toString(lat));
                params.put("gps_long",Double.toString(lng));
                mTemp2.setText("GPS latitude: "+lat+"\nGPS longitude: "+lng);

                instant.setToNow();
                String timeStmp = instant.format("%Y-%m-%d %H:%M:%S");
                mTemp3.setText(timeStmp+"\n"+instant.timezone);
                params.put("timestamp", timeStmp);
                String token = sharedPreferences.getString("token","");
                client.addHeader("Authorization","Token "+token);
                client.post("http://128.199.134.172/api/send/",params, responseHandler);
                try{
                    String url = "http://128.199.134.172/api/get_public_user_profile?user="+username;
                    client.get(url,getPoints);
                } catch(Exception e){
                    mTemp4.setText("points url error");
                }
                //RealNet netInterface = new RealNet();
                //netInterface.execute(pairs);
            }
        });

        Button mLogout = (Button) findViewById(R.id.logout_button);
        mLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("token");
                editor.remove("username");
                editor.commit();
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }
}

class myPhoneStateListener extends PhoneStateListener {

    int SignalStrength = 0;

    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        SignalStrength = signalStrength.getGsmSignalStrength();
        SignalStrength = (2 * SignalStrength) - 113; // -> dBm
    }

    public int getSS() {
        if (SignalStrength > 31) {
            return 0;
        }
        else
            return SignalStrength;
    }

}

class RealNet extends AsyncTask<ArrayList<NameValuePair>, Integer, Integer> {

    protected Integer doInBackground(ArrayList<NameValuePair>... pairs) {

        final HttpClient client = new DefaultHttpClient();

        try{
            String SetServerString = "";
            HttpGet getter = new HttpGet("http://128.199.134.172/data/submit/");
            ResponseHandler<String> handler = new BasicResponseHandler();
            SetServerString = client .execute(getter,handler);
            System.out.println(SetServerString);
        } catch (Exception e){
            System.out.println("Error in httpGet");
        }

        final HttpPost poster = new HttpPost("http://128.199.134.172/data/submit/");
        try {
            poster.setEntity(new UrlEncodedFormEntity(pairs[0]));
            HttpResponse response = client.execute(poster);
        } catch (Exception e){
            System.out.println("Error in httpPost");
        }

        return new Integer(0);
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {

    }
}
package wsg.thesis_v11;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;


public class LeaderboardsPage extends ActionBarActivity {

    JSONObject leaderboardList;
    String space = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards_page_activity);

        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        final LinearLayout imageContainer = (LinearLayout) findViewById(R.id.imageContainer);
        final TextView mTemp4 = (TextView) findViewById(R.id.errorDisplay);
        TextView headers = new TextView(getApplicationContext());
        for(int j=0;j<49;j++){
            space = space+" ";
        }
        headers.setText("Score"+space+"Username");
        imageContainer.addView(headers);

        AsyncHttpClient client = new AsyncHttpClient();
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                try{
                    for(int i=1;i<=response.length();i++){
                        TextView position = new TextView(getApplicationContext());
                        JSONObject place = response.getJSONObject(Integer.toString(i));
                        String user = place.getString("user");
                        String score = place.getString("score");
                        space = "";
                        int x = 46-score.length();
                        for(int j=0;j<x;j++){
                            space = space+" ";
                        }
                        position.setText(score+" points"+space+user);
                        imageContainer.addView(position);
                    }
                } catch(Exception e){
                    mTemp4.setText("Leaderboard listing error");
                }
            }
            @Override
            public void onFailure(int statusCode,Header[] headers,Throwable e,JSONObject response){
                mTemp4.setText("leaderboard loading error");
            }
        };

        try{
            client.get("http://128.199.134.172/api/get_leaderboard",responseHandler);
        } catch(Exception e){
            mTemp4.setText("leaderboard url error");
        }
    }

    public void goBack(){
        startActivity(new Intent(getApplicationContext(), HomePage.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboards_page, menu);
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
}

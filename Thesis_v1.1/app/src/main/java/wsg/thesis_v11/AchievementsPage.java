package wsg.thesis_v11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;



public class AchievementsPage extends ActionBarActivity {

    JSONObject achievementList;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements_page_activity);

        Button mLeaderBoardsButton = (Button) findViewById(R.id.leader_boards_button_b);
        mLeaderBoardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LeaderboardsPage.class));
                overridePendingTransition(0,0);
            }
        });

        Button back = (Button) findViewById(R.id.homebutton_b);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        sharedPreferences = getSharedPreferences("Ampalaya", Context.MODE_PRIVATE);
        final LinearLayout imageContainer = (LinearLayout) findViewById(R.id.imageContainer);
        final TextView mTemp4 = (TextView) findViewById(R.id.errorDisplay);
        String username = sharedPreferences.getString("username","");

        AsyncHttpClient client = new AsyncHttpClient();
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onStart(){

            }
            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                try{
                    achievementList = response.getJSONObject("achievements");
                    int i=0;
                    for(i=1;i<=achievementList.length();i++){
                        ImageView imageView = new ImageView(getApplicationContext());
                        JSONObject achievement = achievementList.getJSONObject(Integer.toString(i));
                        Boolean unlocked = achievement.getBoolean("unlocked");
                        int id = 0;
                        if(unlocked){
                            String imgName = "quest"+Integer.toString(i);
                            try{
                                id = getResources().getIdentifier(imgName, "drawable", getPackageName());
                            } catch(Exception e){
                                id = getResources().getIdentifier("locked","drawable",getPackageName());
                            }
                            imageView.setImageResource(id);
                        }
                        else{
                            id = getResources().getIdentifier("locked", "drawable", getPackageName());
                            imageView.setImageResource(id);
                        }
                        imageContainer.addView(imageView);
                    }
                } catch(Exception e){
                    mTemp4.setText(e.toString());
                }
            }
            @Override
            public void onFailure(int statusCode,Header[] headers,Throwable e,JSONObject response){
                mTemp4.setText(e.toString());
            }
        };
        RequestParams params = new RequestParams();
        try{
            String url = "http://128.199.134.172/api/get_full_user_achievements?user="+username;
            String token = sharedPreferences.getString("token","");
            client.addHeader("Authorization","Token "+token);
            client.get(url,responseHandler);
        } catch(Exception e){
            mTemp4.setText(e.toString());
        }
    }

    public void goBack(){
        startActivity(new Intent(getApplicationContext(), HomePage.class));
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_achievements_page, menu);
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

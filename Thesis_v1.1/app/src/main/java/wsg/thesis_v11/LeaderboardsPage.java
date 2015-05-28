package wsg.thesis_v11;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

        final TextView mTemp4 = (TextView) findViewById(R.id.errorDisplay);
        final TableLayout imgContainer = (TableLayout) findViewById(R.id.listContainer);

        AsyncHttpClient client = new AsyncHttpClient();
        JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode,Header[] headers,JSONObject response){
                try{
                    for(int i=1;i<=response.length();i++){
                        TextView position1 = new TextView(getApplicationContext());
                        TextView position2 = new TextView(getApplicationContext());
                        TableRow newRow = new TableRow(getApplicationContext());

                        JSONObject place = response.getJSONObject(Integer.toString(i));

                        String user = place.getString("user");
                        String score = place.getString("score");

                        position1.setText(user);
                        position2.setText(score);
                        position1.setTextColor(Color.WHITE);
                        position2.setTextColor(Color.WHITE);
                        position1.setGravity(Gravity.CENTER);
                        position2.setGravity(Gravity.CENTER);
                        if(i%2 == 0){
                            position1.setBackgroundColor(Color.parseColor("#8dbab3"));
                            position2.setBackgroundColor(Color.parseColor("#8dbab3"));
                        }
                        else{
                            position1.setBackgroundColor(Color.parseColor("#58857e"));
                            position2.setBackgroundColor(Color.parseColor("#58857e"));
                        }
                        if(i<4){
                            position1.setPadding(0,7,0,7);
                            position2.setPadding(0,7,0,7);
                        }
                            position1.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                            position2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        if(i==1){
                            position1.setBackgroundColor(Color.parseColor("#ffd700"));
                            position2.setBackgroundColor(Color.parseColor("#ffd700"));
                            position1.setTextColor(Color.BLACK);
                            position2.setTextColor(Color.BLACK);
                            position1.setTypeface(Typeface.DEFAULT_BOLD);
                            position2.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                        if(i==2){
                            position1.setBackgroundColor(Color.parseColor("#c0c0c0"));
                            position2.setBackgroundColor(Color.parseColor("#c0c0c0"));
                            position1.setTextColor(Color.BLACK);
                            position2.setTextColor(Color.BLACK);
                            position1.setTypeface(Typeface.DEFAULT_BOLD);
                            position2.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                        if(i==3){
                            position1.setBackgroundColor(Color.parseColor("#cd7f32"));
                            position2.setBackgroundColor(Color.parseColor("#cd7f32"));
                            position1.setTextColor(Color.BLACK);
                            position2.setTextColor(Color.BLACK);
                            position1.setTypeface(Typeface.DEFAULT_BOLD);
                            position2.setTypeface(Typeface.DEFAULT_BOLD);
                        }
                        newRow.addView(position1);
                        newRow.addView(position2);
                        imgContainer.addView(newRow);
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

package sd.sdhydro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserHomeActivity extends AppCompatActivity {
    private String userName;
    TextView dumpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        SharedPreferences myprefs= this.getSharedPreferences("userName", MODE_PRIVATE);
        userName = myprefs.getString("userName",null);
        System.out.println(userName);

        dumpText = (TextView) findViewById(R.id.dumpTextField);


        //create request here
        String url ="http://192.168.56.1:8081/website/appUserHome.php";
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        evaluateResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                }

                ){

            //add post parameters
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userName",userName);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };





        //Use singleton here
        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();



        // Add a request (in this example, called stringRequest) to your RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    public void evaluateResponse(String response){


        //parse JSON response
        try {
            //initalize JSON array and make arrays for parsing
            JSONArray jArray = new JSONArray(response);
            String[] equipID = new String[jArray.length()];
            String[] nick = new String[jArray.length()];
            String[] time = new String[jArray.length()];
            String[] pH = new String[jArray.length()];
            String[] TDS = new String[jArray.length()];
            String[] Lux = new String[jArray.length()];
            for(int i=0;i<jArray.length();i++){
                JSONObject temp = jArray.getJSONObject(i);

                //add columns to individual arrays
                equipID[i] = temp.get("equipmentID").toString();
                nick[i] = temp.get("nickname").toString();
                time[i]= temp.get("currentTimestamp").toString();
                pH[i] = temp.get("currentPH").toString();
                TDS[i] = temp.get("currentTDS").toString();
                Lux[i] = temp.get("currentLUX").toString();
            }


            System.out.println(Arrays.toString(equipID));
            System.out.println(Arrays.toString(nick));
            System.out.println(Arrays.toString(time));
            System.out.println(Arrays.toString(pH));
            System.out.println(Arrays.toString(TDS));
            System.out.println(Arrays.toString(Lux));




        } catch (JSONException e) {
            e.printStackTrace();
        }



        //dumpText.setText(jObj.getString("userName"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_manageEquipmentIDs) {
            System.out.println("clicked logout");
            Intent intent = new Intent(this, ManageEquipmentIDsActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.action_settings) {
            System.out.println("clicked settings");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.action_about) {
            System.out.println("clicked about");
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

            return true;
        }

        if (id == R.id.action_logout) {
            System.out.println("clicked logout");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}

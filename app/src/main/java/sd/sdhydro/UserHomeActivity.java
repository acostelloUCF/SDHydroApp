package sd.sdhydro;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHomeActivity extends AppCompatActivity{
    private String userName;
    private ArrayList<JSONObject> jArrayList = new ArrayList<JSONObject>();
    ListView listView;
    JSONObjectAdapter jArrayAdapter;
    private ProgressBar loadWheel;
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
        loadWheel = (ProgressBar) findViewById(R.id.progressBar);
        loadWheel.setVisibility(View.VISIBLE);




        //create request here
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for(int i=0;i<jArray.length();i++) {
                                jArrayList.add(jArray.getJSONObject(i));
                            }
                            makeLV();


                        }catch (JSONException e){
                                e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserHomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        loadWheel.setVisibility(View.GONE);
                    }
                }

                ){

            //add post parameters
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userName",userName);
                params.put("opCode", "3");
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




//        //create listview, pass it the listlayout and list of JSON objects
//
//        listView = (ListView)findViewById(R.id.listview);
//        jArrayAdapter = new JSONObjectAdapter(UserHomeActivity.this, R.layout.list_item,jArrayList, this);
//        listView.setAdapter(jArrayAdapter);



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
            System.out.println("clicked equipment ID management");
            Intent intent = new Intent(this, ManageEquipmentIDsActivity.class);
            startActivity(intent);

            return true;
        }

//        if (id == R.id.action_settings) {
//            System.out.println("clicked settings");
//            Intent intent = new Intent(this, SettingsActivity.class);
//            startActivity(intent);
//
//            return true;
//        }

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
    public void makeLV(){
        //create listview, pass it the listlayout and list of JSON objects

        listView = (ListView)findViewById(R.id.listview);
        jArrayAdapter = new JSONObjectAdapter(UserHomeActivity.this, R.layout.list_item,jArrayList, this);
        listView.setAdapter(jArrayAdapter);
        loadWheel.setVisibility(View.GONE);
    }
    public String convertToAmPm(String timestamp) throws ParseException {
        //timestamp format:
        //2017-04-01 22:47:40
        String[] date = timestamp.split(" ");
        String[] day = date[0].split("-");
        String[] time=date[1].split(":");
        String amPm = "A.M.";
        if((Integer.valueOf(time[0]) > 11))
            amPm = "P.M.";
        if((Integer.valueOf(time[0]) == 0))
            time[0] = "12";
        if(Integer.valueOf(time[0]) > 12){
            time[0] = String.valueOf((Integer.valueOf(time[0]) - 12));
        }



        String response = time[0] + ":"+time[1] + ":"+time[2] +" "+amPm +" "+day[1]+"/"+day[2]+"/"+day[0].substring(2);
        System.out.println(response);
        return response;



    }


}

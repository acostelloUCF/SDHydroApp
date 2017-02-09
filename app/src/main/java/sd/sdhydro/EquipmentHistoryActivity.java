package sd.sdhydro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EquipmentHistoryActivity extends AppCompatActivity {


    private String eID;
    private String nick;
    private TextView eIDTitle;
    private ArrayList<JSONObject> jArrayList = new ArrayList<JSONObject>();
    ListView listView;
    JSONHistoryAdapter jArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_history);
        String eID = "";
        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        eIDTitle = (TextView) findViewById(R.id.eIDTitle);

        Bundle b = getIntent().getExtras();
        if(b!=null) {
            eID = b.getString("eID");
            nick = b.getString("nick");
        }
        else
            eID=null;

        if(!nick.equals("") && !nick.equals("null"))
            eIDTitle.setText(nick);
        else
            eIDTitle.setText(eID);
        //create request here
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        final String finalEID = eID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    //check if login succeeded or not
                    public void onResponse(String response){
                       // dumpText.setText(response);

                        try {
                            JSONArray jArray = new JSONArray(response);

                            for(int i=0;i<jArray.length();i++) {
                                jArrayList.add(jArray.getJSONObject(i));
                            }



                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        listView = (ListView)findViewById(R.id.historylistview);
                        jArrayAdapter = new JSONHistoryAdapter(EquipmentHistoryActivity.this, R.layout.history_list_item,jArrayList, this);
                        listView.setAdapter(jArrayAdapter);
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
                params.put("equipmentID", finalEID);
                params.put("opCode", "5");
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




}

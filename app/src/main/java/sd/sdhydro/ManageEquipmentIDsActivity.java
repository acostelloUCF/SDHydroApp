package sd.sdhydro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageEquipmentIDsActivity extends AppCompatActivity implements Serializable {

    private ArrayList<JSONObject> jArrayList = new ArrayList<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_equipment_ids);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //get passed list
        jArrayList = (ArrayList<JSONObject>) getIntent().getSerializableExtra("jArray");
        for(int i=0;i<jArrayList.size();i++){
            JSONObject job = jArrayList.get(i);
            try {
                System.out.println(job.get("equipmentID").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //create request here
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    //check if login succeeded or not
                    public void onResponse(String response){
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
               // params.put("equipmentID", equipmentID);
                params.put("opCode", "4");
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

    }
}

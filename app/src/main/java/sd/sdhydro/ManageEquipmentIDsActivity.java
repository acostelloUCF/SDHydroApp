package sd.sdhydro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageEquipmentIDsActivity extends AppCompatActivity{
    private TextView eID;
    private Button addEIDButton;
    private String equipmentID;
    private String userName;
    private ArrayList<JSONObject> jArrayList = new ArrayList<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_equipment_ids);

        eID = (TextView) findViewById(R.id.addEquipmentIDEditText);
        addEIDButton = (Button) findViewById(R.id.addEquipmentIDButton);
        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        SharedPreferences myprefs= this.getSharedPreferences("userName", MODE_PRIVATE);
        userName = myprefs.getString("userName",null);
        System.out.println(userName);

        //do another userHome query to get jArrayList
        getJArrayList();



        addEIDButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!eID.getText().toString().isEmpty())
                    addEquipmentID();
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
                    alertDialog.setTitle("Oops...");
                    alertDialog.setMessage("You must enter an equipment ID.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                }
            }
        });

//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.planets_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter);


    }

    public void getJArrayList(){
        //create request here
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    //check if login succeeded or not
                    public void onResponse(String response){
                        try {
                            JSONArray jArray = new JSONArray(response);

                            for(int i=0;i<jArray.length();i++) {
                                jArrayList.add(jArray.getJSONObject(i));
                                System.out.println(jArray.getJSONObject(i).get("equipmentID").toString());
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
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


    }

    public void addEquipmentID(){

        equipmentID = eID.getText().toString();

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
                params.put("userName", userName);
                params.put("equipmentID", equipmentID);
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
        String parsedResponse= response.substring(0,7);
        if(parsedResponse.equals("Success")){
            AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
            alertDialog.setTitle("Success!");
            alertDialog.setMessage("Equipment ID successfully added.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //refresh screen
                            finish();
                            startActivity(getIntent());
                        }
                    });
            alertDialog.show();



        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
            alertDialog.setTitle("Failed!");
            alertDialog.setMessage(response);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }


        }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }
}

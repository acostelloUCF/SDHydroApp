package sd.sdhydro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageEquipmentIDsActivity extends AppCompatActivity{
    private TextView eID;
    private TextView nickNameText;
    private Button addEIDButton;
    private Button deleteButton;
    private Button profileButton;
    private String equipmentID;
    private String userName;
    private Spinner spinner;
    private ProgressBar loadWheel;
    private ArrayList<JSONObject> jArrayList = new ArrayList<JSONObject>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_equipment_ids);

        eID = (TextView) findViewById(R.id.addEquipmentIDEditText);
        addEIDButton = (Button) findViewById(R.id.addEquipmentIDButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        profileButton = (Button) findViewById(R.id.btn_profile);
        spinner = (Spinner) findViewById(R.id.spinner);
        //nickNameText = (TextView) findViewById(R.id.nickEditText);
        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        loadWheel = (ProgressBar) findViewById(R.id.progressBar);
        loadWheel.setVisibility(View.VISIBLE);

        SharedPreferences myprefs= this.getSharedPreferences("userName", MODE_PRIVATE);
        userName = myprefs.getString("userName",null);
        System.out.println(userName);

        //do another userHome query to get jArrayList


        getJArrayList();
        System.out.println(jArrayList.toString());
//        System.out.println(jArrayList.size());

        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EquipmentProfileActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("equipmentID",spinner.getSelectedItem().toString());
                startActivity(intent);


            }
        });


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

        deleteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
                alertDialog.setTitle("Warning!");
                alertDialog.setMessage("You are about to remove an equipment ID from your profile. Are you sure you want to do this?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes, delete it.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEquipmentID();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                alertDialog.show();

            }
        });


//        nickButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(nickNameText.getWindowToken(),
//                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
//
//
//                System.out.println(nickNameText.getText().toString());
//                AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
//                if(nickNameText.getText().toString().isEmpty()){
//
//                    alertDialog.setTitle("Oops...");
//                    alertDialog.setMessage("You must enter a nickname.");
//                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                    alertDialog.show();
//                }
//                else{
//                    setNickname();
//                }
//            }
//        });


    }

    private void initSpinner() {


        String[] array = new String[jArrayList.size()];
        int i=0;

        for(JSONObject j : jArrayList){
            String temp="LUL";
            try {
                temp = j.getString("nickname").toString();
                if(temp.equals("null")){
                    temp = j.getString("equipmentID").toString();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            array[i]=temp;
            i++;
        }


        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, array);
        //adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        // Specify the layout to use when the list of choices appears
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        loadWheel.setVisibility(View.GONE);
        //nickNameText.setText(spinner.getSelectedItem().toString());
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                nickNameText.setText(spinner.getSelectedItem().toString());
////            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });




    }


    public void getJArrayList(){
        //create request here
        jArrayList = new ArrayList<JSONObject>();

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

                        initSpinner();
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
                        Toast.makeText(ManageEquipmentIDsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        loadWheel.setVisibility(View.GONE);
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
                            //finish();
                            //startActivity(getIntent());
                            getJArrayList();
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

        //make deletion query here
    private void deleteEquipmentID(){

        final String EIDremove = spinner.getSelectedItem().toString();
        System.out.println(EIDremove);

        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    //check if login succeeded or not
                    public void onResponse(String response){
                        if(response.startsWith("Success")){
                            AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
                            alertDialog.setTitle("Success");
                            alertDialog.setMessage(response);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            //refresh screen
                                          //  finish();
                                            //startActivity(getIntent());
                                            getJArrayList();

                                        }
                                    });
                            alertDialog.show();
                        }
                        else {
                            AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
                            alertDialog.setTitle("Failed");
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
                params.put("removeEID", EIDremove);
                params.put("opCode", "7");
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
    private void setNickname(){

        final String NICK = spinner.getSelectedItem().toString();
        //System.out.println(EID);
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    //check if login succeeded or not
                    public void onResponse(String response){
                        if(response.startsWith("Success")){
                            AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
                            alertDialog.setTitle("Success");
                            alertDialog.setMessage(response);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            //refresh screen
                                           // finish();
                                           // startActivity(getIntent());
                                            getJArrayList();

                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(ManageEquipmentIDsActivity.this).create();
                            alertDialog.setTitle("Failed");
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }

        ){

            //add post parameters
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("equipmentID", equipmentID);
                params.put("opCode", "6");
                params.put("nickname", nickNameText.getText().toString());
                try {
                    params.put("equipmentID",jArrayList.get(spinner.getSelectedItemPosition()).get("equipmentID").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

package sd.sdhydro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.Timer;
import java.util.TimerTask;

public class EquipmentProfileActivity extends AppCompatActivity {
    private String userName;
    private String equipmentID;
    private Spinner startHourSpinner;
    private Spinner startMinuteSpinner;
    private Spinner startAmPmSpinner;
    private Spinner endHourSpinner;
    private Spinner endMinuteSpinner;
    private Spinner endAmPmSpinner;
    private TextView nickname;
    private TextView plants;
    private TextView datePlanted;
    private TextView tdsHigh;
    private TextView tdsLow;
    private TextView phHigh;
    private TextView phLow;
    private TextView tdsSolution;
    private TextView phSolution;
    private TextView floweringSolution;
    private CheckBox tdsCheck;
    private CheckBox phCheck;
    private CheckBox floweringCheck;
    private ProgressBar loadWheel;

    private JSONObject equipInfo;

    private Button saveProfileButton;
    private ToggleButton ledToggleButton;
    private ToggleButton flowerToggleButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_profile);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if(b!=null){
            userName = (String) b.get("userName");
            equipmentID = (String) b.get("equipmentID");
        }

        String[] hours = new String[12];
        for(int j=1;j<13;j++){
            hours[j-1] = Integer.toString(j);
        }
        String[] mins = new String[60];
        for(int j=0;j<60;j++){
            if(j<=9){
                mins[j]="0"+Integer.toString(j);
            }
            else{
                mins[j] = Integer.toString(j);
            }
        }
        String[] amPm = new String[2];
        amPm[0]="A.M.";
        amPm[1]="P.M.";

        loadWheel = (ProgressBar) findViewById(R.id.progressBar);
        loadWheel.setVisibility(View.VISIBLE);

        startHourSpinner= (Spinner) findViewById(R.id.startHourSpinner);
        startMinuteSpinner = (Spinner) findViewById(R.id.startMinuteSpinner);
        startAmPmSpinner = (Spinner) findViewById(R.id.startAmPmSpinner);
        endHourSpinner= (Spinner) findViewById(R.id.endHourSpinner);
        endMinuteSpinner = (Spinner) findViewById(R.id.endMinuteSpinner);
        endAmPmSpinner = (Spinner) findViewById(R.id.endAmPmSpinner);

        initSpinner(startHourSpinner, hours);
        initSpinner(startMinuteSpinner, mins);
        initSpinner(startAmPmSpinner, amPm);
        initSpinner(endHourSpinner, hours);
        initSpinner(endMinuteSpinner, mins);
        initSpinner(endAmPmSpinner, amPm);

        nickname = (TextView) findViewById(R.id.nickText);
        plants = (TextView) findViewById(R.id.plantsText);
        datePlanted = (TextView) findViewById(R.id.datePlantedText);
        tdsHigh = (TextView) findViewById(R.id.tdsHighText);
        tdsLow = (TextView) findViewById(R.id.tdsLowText);
        phHigh = (TextView) findViewById(R.id.phHighText);
        phLow = (TextView) findViewById(R.id.phLowText);
        tdsSolution = (TextView) findViewById(R.id.remTdsText);
        phSolution = (TextView) findViewById(R.id.remPhText);
        floweringSolution = (TextView) findViewById(R.id.remFlowerText);
        tdsCheck = (CheckBox) findViewById(R.id.tdsCheckBox);
        phCheck = (CheckBox) findViewById(R.id.phCheckBox);
        floweringCheck = (CheckBox) findViewById(R.id.flowerCheckBox);
        saveProfileButton = (Button) findViewById(R.id.saveProfileButton);
        ledToggleButton = (ToggleButton) findViewById(R.id.ledToggleButton);
        flowerToggleButton = (ToggleButton) findViewById(R.id.floweringToggleButton);

        getEquipmentInfo();
        System.out.println(toMilitary(startHourSpinner.getSelectedItem().toString()+" "+startMinuteSpinner.getSelectedItem().toString()+" "+startAmPmSpinner.getSelectedItem().toString()));
        //hide keyboard on screen create
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,String> postData = new HashMap<String,String>();
                if(!nickname.getText().toString().equals(""))
                    postData.put("nickname", nickname.getText().toString());
                if(!plants.getText().toString().equals(""))
                    postData.put("plants", plants.getText().toString());
                if(!datePlanted.getText().toString().equals(""))
                    postData.put("datePlanted",datePlanted.getText().toString());
                if(!tdsHigh.getText().toString().equals(""))
                    postData.put("settingTdsHigh",tdsHigh.getText().toString());
                if(!tdsLow.getText().toString().equals(""))
                    postData.put("settingTdsLow",tdsLow.getText().toString());
                if(!phHigh.getText().toString().equals(""))
                    postData.put("settingPhHigh",phHigh.getText().toString());
                if(!phLow.getText().toString().equals(""))
                    postData.put("settingPhLow",phLow.getText().toString());
                if(ledToggleButton.getText().toString().equals("ON"))
                    postData.put("light","turnLightOn");
                else
                    postData.put("light","turnLightOff");
                if(flowerToggleButton.getText().toString().equals("ON"))
                    postData.put("flowering","turnFloweringOn");
                else
                    postData.put("flowering","turnFloweringOff");


                //send time if they are not both 1:00 am, aka the defaults
                String start = toMilitary(startHourSpinner.getSelectedItem().toString()+" "+startMinuteSpinner.getSelectedItem().toString()+" "+startAmPmSpinner.getSelectedItem().toString());
                String end = toMilitary(endHourSpinner.getSelectedItem().toString()+" "+ endMinuteSpinner.getSelectedItem().toString()+" "+ endAmPmSpinner.getSelectedItem().toString());
                if(!(start.equals("01:00:00") && end.equals("01:00:00"))){
                    String[] startArr = start.split(":");
                    String[] endArr = end.split(":");

                    postData.put("lightOnTimehours",startArr[0]);
                    postData.put("lightOnTimeminutes",startArr[1]);
                    postData.put("lightOnTimesec",startArr[2]);
                    postData.put("lightOffTimehours",endArr[0]);
                    postData.put("lightOffTimeminutes",endArr[1]);
                    postData.put("lightOffTimesec",endArr[2]);

                }

                if(phCheck.isChecked())
                    postData.put("resetPH", "True");
                if(tdsCheck.isChecked())
                    postData.put("resetTDS", "True");
                if(floweringCheck.isChecked())
                    postData.put("resetFlowering", "True");



                System.out.println(Arrays.asList(postData));
                //System.out.println(toMilitary(postData.get("lightOnTime")));

                updateProfile(postData);

                goToManageIDs();
            }
        });





    }

    private void updateProfile(final Map<String, String> postData) {
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        if(response.startsWith("Success")){
                            goToManageIDs();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EquipmentProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        loadWheel.setVisibility(View.GONE);
                    }
                }

        ){

            //add post parameters
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = postData;
                params.put("UpdateSetting", "1");
                params.put("userName", userName);
                params.put("EID", equipmentID);
                params.put("opCode", "6");
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

    private void goToManageIDs() {
//        Intent intent = new Intent(this, ManageEquipmentIDsActivity.class);
//        startActivity(intent);
//

    }

    private void getEquipmentInfo() {
        //create request here
        String url = getString(R.string.dbURL);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray j = new JSONArray(response);
                            equipInfo = j.getJSONObject(0);
                            //System.out.println(equipInfo.toString());
                            setInfo();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        loadWheel.setVisibility(View.GONE);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EquipmentProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        loadWheel.setVisibility(View.GONE);
                    }
                }

        ){

            //add post parameters
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("equipmentID",equipmentID);
                params.put("opCode", "8");
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

    private void setInfo() throws JSONException {

        if(!equipInfo.get("nickname").toString().equals("null")){
            nickname.setText(equipInfo.get("nickname").toString());
        }
        if(!equipInfo.get("datePlanted").toString().equals("null")){
            datePlanted.setText(equipInfo.get("datePlanted").toString());
        }
        if(!equipInfo.get("plants").toString().equals("null")){
            plants.setText(equipInfo.get("plants").toString());
        }
        tdsSolution.setText("Remaining TDS Solution: "+equipInfo.get("counterTDS").toString());
        phSolution.setText("Remaining pH Solution: "+equipInfo.get("counterPHup").toString());
        floweringSolution.setText("Remaining Flowering Solution: "+equipInfo.get("counterFlowering").toString());

        if(!equipInfo.get("settingTdsHigh").toString().equals("null")){
            tdsHigh.setText(equipInfo.get("settingTdsHigh").toString());
        }
        if(!equipInfo.get("settingTdsLow").toString().equals("null")){
            tdsLow.setText(equipInfo.get("settingTdsLow").toString());
        }
        if(!equipInfo.get("settingPhHigh").toString().equals("null")){
            phHigh.setText(equipInfo.get("settingPhHigh").toString());
        }
        if(!equipInfo.get("settingPhLow").toString().equals("null")){
            phLow.setText(equipInfo.get("settingPhLow").toString());
        }

        if(equipInfo.get("led").toString().equals("0")){
            ledToggleButton.setChecked(false);
        }
        else
            ledToggleButton.setChecked(true);

        if(equipInfo.get("flowering").toString().equals("0")){
            flowerToggleButton.setChecked(false);
        }
        else
            flowerToggleButton.setChecked(true);


    }





    private void initSpinner(Spinner spinner, String[] array){
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                array);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    private String toMilitary(String amPm){
        String[] arr = amPm.split(" ");
        if (arr.length<3)
            return "error";
        else
        {
            String response;
            if(arr[2].startsWith("A")){
                switch (arr[0]){
                    case "12":{
                        response="00";
                        break;
                    }
                    case "11":{
                        response="11";
                        break;
                    }
                    case "10":{
                        response="10";
                        break;
                    }
                    default:{
                        response="0"+arr[0];
                        break;
                    }

                }
            }
            else{
                switch (arr[0]){
                    case "12":{
                        response="12";
                        break;
                    }
                    default:{
                        int temp =Integer.valueOf(arr[0]) + 12;
                        response=Integer.toString(temp);
                        break;
                    }

                }

            }
            response+=":"+arr[1]+":00";
            return response;
        }



    }
    private String fromMilitary(String mil){
        String[] arr = mil.split(":");
        if(arr.length<2){
            return "error";
        }
        else{

            return arr[0]+arr[1]+arr[2];

        }


    }
}

package sd.sdhydro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NewUserActivity extends AppCompatActivity {

    private String userName;
    private String password;
    private String confirmPassword;
    private String equipmentID;

    private TextView userNameTextField;
    private TextView passwordTextField;
    private TextView confirmPasswordTextField;
    private TextView equipmentIDTextField;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //get reference to UI objects
        userNameTextField = (TextView) findViewById(R.id.userNameTextField);
        passwordTextField = (TextView) findViewById(R.id.passwordTextField);
        confirmPasswordTextField = (TextView) findViewById(R.id.confirmPasswordTextField);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                userName = userNameTextField.getText().toString();
                password = passwordTextField.getText().toString();
                confirmPassword = confirmPasswordTextField.getText().toString();

                if(userName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                    //create alert for empty field
                    AlertDialog alertDialogEmpty = new AlertDialog.Builder(NewUserActivity.this).create();
                    alertDialogEmpty.setTitle("Oops...");
                    alertDialogEmpty.setMessage("One or more of the fields are empty. Please fill them out.");
                    alertDialogEmpty.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialogEmpty.show();
                }

                        //check if passwords are the same
                else if(!password.equals(confirmPassword)){
                    System.out.println("not the same");
                    //create alert for mismatched passwords
                    AlertDialog alertDialog = new AlertDialog.Builder(NewUserActivity.this).create();
                    alertDialog.setTitle("Oops...");
                    alertDialog.setMessage("Your password entries do not match. Please re-enter your password.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    //valid info kinda...
                    //create alert for empty field
                    makeRequest();

                }


                System.out.println(userName+" "+password+" "+confirmPassword);
            }
        });

    }


    public void makeRequest(){

        //create request here
        String url = getString(R.string.dbURL);
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
                params.put("newUser-name",userName);
                params.put("newUser-password",password);
                params.put("opCode", "2");
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

        String parsedResponse = response.substring(0,7);
        if(parsedResponse.equals("Welcome")) {

        AlertDialog alertDialog = new AlertDialog.Builder(NewUserActivity.this).create();
        alertDialog.setTitle("Success!");
        alertDialog.setMessage(response);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        transition();
                    }
                });
        alertDialog.show();

        }
        else{

        AlertDialog alertDialog = new AlertDialog.Builder(NewUserActivity.this).create();
        alertDialog.setTitle("Oops...");
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

    public void transition(){
        //store username, pass and isLoggedIn
        SharedPreferences myprefs= this.getSharedPreferences("userName", MODE_PRIVATE);
        myprefs.edit().putString("userName", userName).commit();

        myprefs= this.getSharedPreferences("password", MODE_PRIVATE);
        myprefs.edit().putString("password", password).commit();

        myprefs= this.getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        myprefs.edit().putBoolean("isLoggedIn", true).commit();

        //go to user home
        Intent intent = new Intent(this, UserHomeActivity.class);
        startActivity(intent);
    }

}

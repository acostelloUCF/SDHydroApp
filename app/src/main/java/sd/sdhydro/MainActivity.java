package sd.sdhydro;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {


    private String userName;
    private String password;
    private TextView userNameTextField;
    private TextView passwordTextField;
    private Button loginButton;
    private Button newUserButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //get reference to textfields and buttons
        userNameTextField = (TextView) findViewById(R.id.userNameTextField);
        passwordTextField = (TextView) findViewById(R.id.passwordTextField);
        loginButton = (Button) findViewById(R.id.loginButton);
        newUserButton = (Button) findViewById(R.id.newUserButton);

        //set click listeners and onClick methods
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onLoginClick();
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onNewUserClick();
            }
        });





    }

    public void onLoginClick(){
        userName = userNameTextField.getText().toString();
        password = passwordTextField.getText().toString();

        System.out.println(userName+" "+password);

        //final MainActivity thisMain = this;


            //create request here
            String url ="http://192.168.56.1:8081/website/appLogin.php";
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
                    params.put("login-id",userName);
                    params.put("login-password", password);
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

    public void onNewUserClick(){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);

    }


    public void evaluateResponse(String response){

        String parsedResponse= response.substring(0,5);
        if(parsedResponse.equals("Valid")){


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
        else{
            parsedResponse= response.substring(0,31);
            //if not valid login credentials
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Oops...");
            alertDialog.setMessage(parsedResponse);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        //clicked on about in menu
        if (id == R.id.action_about) {
            System.out.println("clicked about");

            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}



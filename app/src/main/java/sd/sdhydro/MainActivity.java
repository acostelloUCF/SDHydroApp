package sd.sdhydro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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

        if(userName.equals("123")){
            //if valid login credentials
            

        }else{
            //if not valid login credentials
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Oops...");
            alertDialog.setMessage("Invalid login credentials. Please try again.");
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

    public void onNewUserClick(){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);

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
        //clicked on settings in menu
        if (id == R.id.action_settings) {
            System.out.println("clicked settings");

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        //clicked on about in menu
        if (id == R.id.action_about) {
            System.out.println("clicked about");

            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}



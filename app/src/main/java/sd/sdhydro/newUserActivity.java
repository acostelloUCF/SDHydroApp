package sd.sdhydro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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


        //get reference to UI objects
        userNameTextField = (TextView) findViewById(R.id.userNameTextField);
        passwordTextField = (TextView) findViewById(R.id.passwordTextField);
        confirmPasswordTextField = (TextView) findViewById(R.id.confirmPasswordTextField);
        equipmentIDTextField = (TextView) findViewById(R.id.equipmentIDTextField);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                userName = userNameTextField.getText().toString();
                password = passwordTextField.getText().toString();
                confirmPassword = confirmPasswordTextField.getText().toString();
                equipmentID = equipmentIDTextField.getText().toString();

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

                        //check if passwords are the same
                if(!password.equals(confirmPassword)){
                    System.out.println("not the same");
                    alertDialog.show();
                    return;

                }


                System.out.println(userName+" "+password+" "+confirmPassword+" "+equipmentID);
            }
        });

    }


}

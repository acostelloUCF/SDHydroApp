package sd.sdhydro;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    private TextView label;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        label = (TextView) findViewById(R.id.labelz);

        SharedPreferences myprefs= getSharedPreferences("user", MODE_PRIVATE);
        String userName= myprefs.getString("userName", null);
        label.setText(userName);

    }
}

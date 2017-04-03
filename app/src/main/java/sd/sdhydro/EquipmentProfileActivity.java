package sd.sdhydro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EquipmentProfileActivity extends AppCompatActivity {
    private String userName;
    private String equipmentID;

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





    }
}

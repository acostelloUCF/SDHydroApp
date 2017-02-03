package sd.sdhydro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ManageEquipmentIDsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_equipment_ids);

        //add icon to toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.let);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }
}

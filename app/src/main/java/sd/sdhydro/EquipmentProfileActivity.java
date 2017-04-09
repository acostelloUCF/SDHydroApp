package sd.sdhydro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EquipmentProfileActivity extends AppCompatActivity {
    private String userName;
    private String equipmentID;
    private Spinner startHourSpinner;
    private Spinner startMinuteSpinner;
    private Spinner startAmPmSpinner;
    private Spinner endHourSpinner;
    private Spinner endMinuteSpinner;
    private Spinner endAmPmSpinner;

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

    }


    private void initSpinner(Spinner spinner, String[] array){
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                array);
        spinner.setAdapter(spinnerArrayAdapter);
    }
}

package sd.sdhydro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

/**
 * Created by desk on 2/8/2017.
 */
public class JSONObjectAdapter extends ArrayAdapter<JSONObject> {
    int resource;
    String response;
    Context context;
    String eID;
    Activity a;
    //Initialize adapter
    public JSONObjectAdapter(Context context, int resource, List<JSONObject> items, Activity a) {
        super(context, resource, items);
        this.resource=resource;
        this.a=a;
        this.context=context;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LinearLayout jView;
        //Get the current alert object
        JSONObject jObj = getItem(position);

        //Inflate the view
        if(convertView==null)
        {
            jView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, jView, true);
        }
        else
        {
            jView = (LinearLayout) convertView;
        }
        //Get the text boxes
        TextView eIDText =(TextView)jView.findViewById(R.id.list_item_equipmentID);
        TextView phText =(TextView)jView.findViewById(R.id.list_item_ph);
        TextView tdsText =(TextView)jView.findViewById(R.id.list_item_tds);
        TextView luxText =(TextView)jView.findViewById(R.id.list_item_lux);
        TextView timeText =(TextView)jView.findViewById(R.id.list_item_time);
        TextView nickText =(TextView)jView.findViewById(R.id.list_item_nick);

        Button infoButton = (Button) jView.findViewById(R.id.list_item_btn);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, EquipmentHistoryActivity.class);
                JSONObject j = getItem(position);
                try {
                    intent.putExtra("eID",j.getString("equipmentID").toString());
                    intent.putExtra("nick",j.getString("nickname").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                context.startActivity(intent);
            }
        });

        //Assign the appropriate data from our alert object above
        try {
            eIDText.setText("Equipment ID: "+jObj.get("equipmentID").toString());
            //eID = jObj.get("equipmentID").toString();
            phText.setText("pH: "+jObj.get("currentPH").toString());
            tdsText.setText("TDS: "+jObj.get("currentTDS").toString());
            luxText.setText("Lux: "+jObj.get("currentLUX").toString());
            try {
                timeText.setText(convertToAmPm(jObj.get("currentTimestamp").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String tempNick = jObj.get("nickname").toString();
            if(!jObj.get("nickname").toString().equals("null"))
                nickText.setText(jObj.get("nickname").toString());
            else
                nickText.setText(jObj.get("equipmentID").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jView;
    }
    public String convertToAmPm(String timestamp) throws ParseException {
        //timestamp format:
        //2017-04-01 22:47:40
        String[] date = timestamp.split(" ");
        String[] day = date[0].split("-");
        String[] time=date[1].split(":");
        String amPm = "A.M.";
        if((Integer.valueOf(time[0]) > 11))
            amPm = "P.M.";
        if((Integer.valueOf(time[0]) == 0))
            time[0] = "12";
        if(Integer.valueOf(time[0]) > 12){
            time[0] = String.valueOf((Integer.valueOf(time[0]) - 12));
        }



        String response = time[0] + ":"+time[1] + ":"+time[2] +" "+amPm +" "+day[1]+"/"+day[2]+"/"+day[0].substring(2);
        System.out.println(response);
        return response;



    }
//
//    public void onInfoClick(String eID){
//        Intent intent =new Intent(context.getApplicationContext(), EquipmentHistoryActivity.class);
//        //intent.putExtra("eID",eID);
//        context.startActivity(intent);
//
//    }


}

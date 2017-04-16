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

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by desk on 2/9/2017.
 */
public class JSONHistoryAdapter extends ArrayAdapter<JSONObject>{

    int resource;
    String response;
    Context context;
    String eID;
    Response.Listener a;
    //Initialize adapter
    public JSONHistoryAdapter(Context context, int resource, List<JSONObject> items, Response.Listener<String> a) {
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
        TextView phText =(TextView)jView.findViewById(R.id.list_ph);
        TextView tdsText =(TextView)jView.findViewById(R.id.list_tds);
        TextView luxText =(TextView)jView.findViewById(R.id.list_lux);
        TextView timeText =(TextView)jView.findViewById(R.id.list_timestamp);
        TextView waterLevelText = (TextView) jView.findViewById(R.id.list_waterlevel);

        //Assign the appropriate data from our alert object above
        try {
            phText.setText("pH: "+jObj.get("PH").toString());
            tdsText.setText("TDS: "+jObj.get("TDS").toString());
            luxText.setText("Lux: "+jObj.get("LUX").toString());
            waterLevelText.setText("Water Level: "+jObj.get("WaterLevel").toString()+"%");
            timeText.setText(convertToAmPm(jObj.get("timestamp").toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                timeText.setText(jObj.get("timestamp").toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
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

}



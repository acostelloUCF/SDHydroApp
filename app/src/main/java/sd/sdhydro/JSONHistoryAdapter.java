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



        //Assign the appropriate data from our alert object above
        try {
            phText.setText("pH: "+jObj.get("PH").toString());
            tdsText.setText("TDS: "+jObj.get("TDS").toString());
            luxText.setText("Lux: "+jObj.get("LUX").toString());
            timeText.setText(jObj.get("timestamp").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jView;
    }



}



package sd.sdhydro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by desk on 2/8/2017.
 */
public class JSONObjectAdapter extends ArrayAdapter<JSONObject>{
    int resource;
    String response;
    Context context;
    //Initialize adapter
    public JSONObjectAdapter(Context context, int resource, List<JSONObject> items) {
        super(context, resource, items);
        this.resource=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
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
        //Get the text boxes from the listitem.xml file
        TextView eIDText =(TextView)jView.findViewById(R.id.list_item_equipmentID);
        TextView phText =(TextView)jView.findViewById(R.id.list_item_ph);
        TextView tdsText =(TextView)jView.findViewById(R.id.list_item_tds);
        TextView luxText =(TextView)jView.findViewById(R.id.list_item_lux);
        TextView timeText =(TextView)jView.findViewById(R.id.list_item_time);
        TextView nickText =(TextView)jView.findViewById(R.id.list_item_nick);

        //Assign the appropriate data from our alert object above
        try {
            eIDText.setText("Equipment ID:"+jObj.get("equipmentID").toString());
            phText.setText("pH: "+jObj.get("currentPH").toString());
            tdsText.setText("TDS: "+jObj.get("currentTDS").toString());
            luxText.setText("Lux: "+jObj.get("currentLUX").toString());
            timeText.setText(jObj.get("currentTimestamp").toString());
            String tempNick = jObj.get("nickname").toString();
            if(jObj.get("nickname").toString().substring(0,3).equals("null"))
                nickText.setText(jObj.get("nickname").toString());
            else
                nickText.setText("No nickname set.");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jView;
    }
}

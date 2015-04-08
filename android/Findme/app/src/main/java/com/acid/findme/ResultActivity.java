package com.acid.findme;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.acid.findme.R;



public class ResultActivity extends ListActivity {
    
    private ArrayList<Node> nodes = null;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		Category currentCat = (Category) getIntent().getExtras().getParcelable("category");
		setTitle("Next "+currentCat.getName());
        
        // Get listview
        ListView lv = getListView();

        nodes = getIntent().getParcelableArrayListExtra("nodes");
        //TODO: debug lv.setAdapter Exception ... nodes
        ListAdapter adapterNode = new ListAdapter(getApplicationContext(), R.layout.list_item, nodes);
        lv.setAdapter(adapterNode);
        
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
        	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Starting new intent
            	Intent intent = new Intent(getApplicationContext(), ViewNodeActivity.class);
                intent.putExtra("node", nodes.get(position));
                startActivity(intent); 
            }
        });
	}
	

	public class ListAdapter extends ArrayAdapter<Node>{
    	private ArrayList<Node> nodes;
    	private Context context;
    	private LayoutInflater inflator = null;
    	
    	public ListAdapter(Context context, int textViewResourceId) {
    	    super(context, textViewResourceId);
    	    // TODO Auto-generated constructor stub
    	}

        public ListAdapter(Context context, int resource, ArrayList<Node> nodes) {
    	   super(context, resource, nodes);
    	   inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    	   this.nodes = nodes;
    	   this.context = context;
        }

        
        @Override
        public View getView(int position, View convertView, ViewGroup parent){            
        	TextView tvName = null;
        	TextView tvDistance = null;
        	TextView tvNid = null;
        	
        	if(convertView == null){
            	convertView = inflator.inflate(R.layout.list_item, parent, false);
            	
            }
        	tvName = (TextView) convertView.findViewById(R.id.name);
        	tvDistance = (TextView) convertView.findViewById(R.id.distance);
        	tvNid = (TextView) convertView.findViewById(R.id.nid);
        	
        	Node node = nodes.get(position);
        	String name = node.getName();
        	if(name.equals("null"))
        		name = "Unkown Name";
        	
            tvName.setText(name);
            tvDistance.setText(Distance.format(node.getDistance())); 
            tvNid.setText(String.valueOf(node.getNid()));

            return convertView;
        }
    }
}

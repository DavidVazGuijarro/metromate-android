package com.scapehouse.metromate;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StationListArrayAdapter extends ArrayAdapter<String> 
{
	private final Context context;
  	private final ArrayList<String> values;

  	public StationListArrayAdapter(Context context, ArrayList<String> values)
  	{
    	super(context, R.layout.station_list_item, values);
    	this.context = context;
    	this.values = values;
  	}
  	
  	@Override
  	public View getView(int position, View convertView, ViewGroup parent)
  	{
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	float screen_density = context.getResources().getDisplayMetrics().density;
    	
    	View rowView = inflater.inflate(R.layout.station_list_item, parent, false);
    	
    	ImageView node_line_upper = (ImageView)rowView.findViewById(R.id.station_list_item_line_upper);
    	ImageView node_line_lower = (ImageView)rowView.findViewById(R.id.station_list_item_line_lower);
    	ImageView node = (ImageView)rowView.findViewById(R.id.station_list_item_node);
    	ImageView airplane_pictogram = (ImageView)rowView.findViewById(R.id.airplane_pictogram);
    	TextView node_number_label = (TextView)rowView.findViewById(R.id.station_list_item_node_label);
    	TextView station_name_label = (TextView)rowView.findViewById(R.id.station_list_item_label);
    	
    	Typeface shmm_default_font = Typeface.createFromAsset(context.getAssets(), "fonts/arial_rounded_bold.ttf");
    	 
    	node_number_label.setText(MainActivity.station_list_red_numbers[position]);
    	station_name_label.setText(values.get(position));
    	
    	node_number_label.setTypeface(shmm_default_font);
    	station_name_label.setTypeface(shmm_default_font);
	    
    	if ( position == 0 )
    	{
    		node_line_upper.setVisibility(View.INVISIBLE);
    	}
    	
    	if ( position == values.size() - 1 )
    	{
    		node_line_lower.setVisibility(View.INVISIBLE);
    	}
    	
	    if ( SetDirectionActivity.is_red_line )
	    {
	    	node.setImageResource(R.drawable.node_red);
	    	node_line_upper.setImageResource(R.drawable.line_red_vertical);
	    	node_line_lower.setImageResource(R.drawable.line_red_vertical);
	    	
	    	if ( position == 2 || position == 3 )
	    	{
	    		airplane_pictogram.setVisibility(View.VISIBLE);
	    	}
	    	
	    	if ( position == 7 || position == 8 ) // Interchange
	    	{
	    		if ( position == 7 )
	            {
	    			node_number_label.setText("20 18");
	            }
	            else
	            {
	            	node_number_label.setText("26 19");
	            }
	    		
	    		RelativeLayout.LayoutParams params_node_label = (RelativeLayout.LayoutParams)node_number_label.getLayoutParams();
	    		params_node_label.setMargins((int)(-5 * screen_density), (int)(33 * screen_density), 0, 0); // Substitute parameters for left, top, right, bottom.
	    		node_number_label.setLayoutParams(params_node_label);
	    		
	    		RelativeLayout.LayoutParams params_node = (RelativeLayout.LayoutParams)node.getLayoutParams();
	    		params_node.setMargins((int)(-32 * screen_density), (int)(29 * screen_density), 0, 0);
	    		node.setLayoutParams(params_node);
	    		
	    		node_number_label.getLayoutParams().width = (int)(50 * screen_density);
	    		node.getLayoutParams().width = (int)(83 * screen_density);
	    		node.setImageResource(R.drawable.node_red_change);
	    	}
	    }
	    else
	    {
	    	node.setImageResource(R.drawable.node_green);
	    	node_line_upper.setImageResource(R.drawable.line_green_vertical);
	    	node_line_lower.setImageResource(R.drawable.line_green_vertical);
	    	
	    	if ( position == 9 || position == 15 ) // Interchange
	    	{
	    		if ( position == 9 )
	            {
	    			node_number_label.setText("18 20");
	            }
	            else
	            {
	            	node_number_label.setText("19 26");
	            }
	    		
	    		RelativeLayout.LayoutParams params_node_label = (RelativeLayout.LayoutParams)node_number_label.getLayoutParams();
	    		params_node_label.setMargins((int)(-6 * screen_density), (int)(33 * screen_density), 0, 0); // Substitute parameters for left, top, right, bottom.
	    		node_number_label.setLayoutParams(params_node_label);
	    		
	    		RelativeLayout.LayoutParams params_node = (RelativeLayout.LayoutParams)node.getLayoutParams();
	    		params_node.setMargins((int)(-33 * screen_density), (int)(29 * screen_density), 0, 0);
	    		node.setLayoutParams(params_node);
	    		
	    		node_number_label.getLayoutParams().width = (int)(50 * screen_density);
	    		node.getLayoutParams().width = (int)(83 * screen_density);
	    		node.setImageResource(R.drawable.node_green_change);
	    	}
	    }

    	return rowView;
  	}
}
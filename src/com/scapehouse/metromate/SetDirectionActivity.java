package com.scapehouse.metromate;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class SetDirectionActivity extends Activity
{
	static boolean is_active = false;
	public static String selected_station;
	public static String selected_direction;
	public static boolean is_red_line = true;
	static ListView station_list_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_direction);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.DIRECTION);
		int button_ID = Integer.parseInt(message);
		
		Typeface shmm_default_font = Typeface.createFromAsset(getAssets(), "fonts/arial_rounded_bold.ttf");
		
		Button active_direction_button = (Button)findViewById(R.id.active_direction_button);
		active_direction_button.setTextColor(Color.parseColor("white"));
		active_direction_button.setTypeface(shmm_default_font);
		
		// We need to scale down the images by half.
        Drawable drawable_arrow_right = getResources().getDrawable(R.drawable.arrow_pictogram_right);
        Drawable drawable_arrow_left = getResources().getDrawable(R.drawable.arrow_pictogram_left);
        
        Drawable drawable_terminus_rashidiya = getResources().getDrawable(R.drawable.terminus_rashidiya);
        Drawable drawable_terminus_jebel_ali = getResources().getDrawable(R.drawable.terminus_jebelali);
        Drawable drawable_terminus_etisalat = getResources().getDrawable(R.drawable.terminus_etisalat);
        Drawable drawable_terminus_creek = getResources().getDrawable(R.drawable.terminus_creek);
        
        drawable_arrow_right.setBounds(0, 0, (int)(drawable_arrow_right.getIntrinsicWidth() * 0.5), 
                                 	(int)(drawable_arrow_right.getIntrinsicHeight() * 0.5));
        drawable_arrow_left.setBounds(0, 0, (int)(drawable_arrow_left.getIntrinsicWidth() * 0.5), 
									(int)(drawable_arrow_left.getIntrinsicHeight() * 0.5));
        
        drawable_terminus_rashidiya.setBounds(0, 0, (int)(drawable_terminus_rashidiya.getIntrinsicWidth() * 0.5), 
             						(int)(drawable_terminus_rashidiya.getIntrinsicHeight() * 0.5));
        drawable_terminus_jebel_ali.setBounds(0, 0, (int)(drawable_terminus_jebel_ali.getIntrinsicWidth() * 0.5), 
        							(int)(drawable_terminus_jebel_ali.getIntrinsicHeight() * 0.5));
        drawable_terminus_etisalat.setBounds(0, 0, (int)(drawable_terminus_etisalat.getIntrinsicWidth() * 0.5), 
									(int)(drawable_terminus_etisalat.getIntrinsicHeight() * 0.5));
        drawable_terminus_creek.setBounds(0, 0, (int)(drawable_terminus_creek.getIntrinsicWidth() * 0.5), 
									(int)(drawable_terminus_creek.getIntrinsicHeight() * 0.5));

        ScaleDrawable sd_arrow_right = new ScaleDrawable(drawable_arrow_right, 0, 5000, 5000);
        ScaleDrawable sd_arrow_left = new ScaleDrawable(drawable_arrow_left, 0, 5000, 5000);
        
        ScaleDrawable sd_terminus_rashidiya = new ScaleDrawable(drawable_terminus_rashidiya, 0, 5000, 5000);
        ScaleDrawable sd_terminus_jebel_ali = new ScaleDrawable(drawable_terminus_jebel_ali, 0, 5000, 5000);
        ScaleDrawable sd_terminus_etisalat = new ScaleDrawable(drawable_terminus_etisalat, 0, 5000, 5000);
        ScaleDrawable sd_terminus_creek = new ScaleDrawable(drawable_terminus_creek, 0, 5000, 5000);
        
		switch ( button_ID )
		{
			case R.id.rashidiya_button:
				active_direction_button.setText(getResources().getString(R.string.direction_rashidiya));
				active_direction_button.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_rashidiya.getDrawable(), null);
				
				selected_direction = "Rashidiya";
				
				break;
				
			case R.id.jebel_ali_button:
				active_direction_button.setText(getResources().getString(R.string.direction_jebel_ali));
				active_direction_button.setCompoundDrawables(sd_terminus_jebel_ali.getDrawable(), null, sd_arrow_right.getDrawable(), null);
				
				selected_direction = "JebelAli";
				
				break;
				
			case R.id.etisalat_button:
				active_direction_button.setText(getResources().getString(R.string.direction_etisalat));
				active_direction_button.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_etisalat.getDrawable(), null);
				
				selected_direction = "Etisalat";
				
				break;
				
			case R.id.creek_button:
				active_direction_button.setText(getResources().getString(R.string.direction_creek));
				active_direction_button.setCompoundDrawables(sd_terminus_creek.getDrawable(), null, sd_arrow_right.getDrawable(), null);
				
				selected_direction = "Creek";
				
				break;
		}
		
		ValueAnimator direction_light_separator_animation_1 = ObjectAnimator.ofFloat(findViewById(R.id.direction_light_separator_1), "alpha", 0.8f, 0.5f);
        direction_light_separator_animation_1.setDuration(500);
        direction_light_separator_animation_1.setRepeatCount(ValueAnimator.INFINITE);
        direction_light_separator_animation_1.setRepeatMode(ValueAnimator.REVERSE);
        direction_light_separator_animation_1.start();
        
        ValueAnimator direction_light_separator_animation_2 = ObjectAnimator.ofFloat(findViewById(R.id.direction_light_separator_2), "alpha", 0.8f, 0.5f);
        direction_light_separator_animation_2.setDuration(500);
        direction_light_separator_animation_2.setRepeatCount(ValueAnimator.INFINITE);
        direction_light_separator_animation_2.setRepeatMode(ValueAnimator.REVERSE);
        direction_light_separator_animation_2.start();
        
        station_list_view = (ListView)findViewById(R.id.station_list);
        station_list_view.setChoiceMode(1);
        
        final ArrayList<String> list = new ArrayList<String>();
        
        if ( is_red_line )
        {
        	for (int i = 0; i < MainActivity.station_list_red_numbers.length; ++i)
            {
            	String full_station_name = MainActivity.station_list_red_names_en[i] + "\n" + MainActivity.station_list_red_names_ar[i];
            	list.add(full_station_name);
            }
        }
        else
        {
        	for (int i = 0; i < MainActivity.station_list_green_numbers.length; ++i)
            {
            	String full_station_name = MainActivity.station_list_green_names_en[i] + "\n" + MainActivity.station_list_green_names_ar[i];
            	list.add(full_station_name);
            }
        }
        
        final StationListArrayAdapter adapter = new StationListArrayAdapter(this, list);
        station_list_view.setAdapter(adapter);
        
        station_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
        	@Override
	        public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
        	{
        		final MediaPlayer player = MediaPlayer.create(SetDirectionActivity.this, R.raw.menu_focus);
                player.setOnCompletionListener(new OnCompletionListener()
                {	
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        // TODO Auto-generated method stub
                    	player.release();
                    }
                });   
                player.start();
                
        		MainActivity.selected_row = position;
        		MainActivity.station_autoselected = false; // Reset this so it doesn't jump to the closest station every time (maybe you're now closer to a different station).
        		
        		if ( is_red_line )
                {
        			selected_station = (String)MainActivity.station_list_red_names_en[position];
                }
                else
                {
                	selected_station = (String)MainActivity.station_list_green_names_en[position];
                }
        		
        		showNextTimingForStation();
	        }
        });
        
        if ( MainActivity.selected_row != -1 && MainActivity.station_autoselected )
        {
        	station_list_view.performItemClick(
        			station_list_view.getAdapter().getView(MainActivity.selected_row, null, null),
        			MainActivity.selected_row,
        	        station_list_view.getAdapter().getItemId(MainActivity.selected_row));
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_direction, menu);
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ( keyCode == KeyEvent.KEYCODE_MENU )
        {
        	Intent intent = new Intent(this, BackActivity.class);
        	startActivity(intent);
        	
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
	
	public void goBack(View view)
	{
		finish();
	}
	
	public static void selectRow(int row)
	{
		station_list_view.performItemClick(
    			station_list_view.getAdapter().getView(row, null, null),
    			row,
    	        station_list_view.getAdapter().getItemId(row));
	}
	
	public void showNextTimingForStation()
    {
    	Intent intent = new Intent(this, NextTrainActivity.class);
    	
    	startActivity(intent);
    }
	
	@Override
    public void onStart()
	{
       super.onStart();
       is_active = true;
    } 

    @Override
    public void onStop()
    {
       super.onStop();
       is_active = false;
    }
}

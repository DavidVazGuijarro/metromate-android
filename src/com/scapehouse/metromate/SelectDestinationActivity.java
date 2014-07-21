package com.scapehouse.metromate;

import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class SelectDestinationActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_destination);
		
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
        
		if ( SetDirectionActivity.selected_direction.equals("Rashidiya") )
		{
			active_direction_button.setText(getResources().getString(R.string.direction_rashidiya));
			active_direction_button.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_rashidiya.getDrawable(), null);
		}
		else if ( SetDirectionActivity.selected_direction.equals("JebelAli") )
		{
			active_direction_button.setText(getResources().getString(R.string.direction_jebel_ali));
			active_direction_button.setCompoundDrawables(sd_terminus_jebel_ali.getDrawable(), null, sd_arrow_right.getDrawable(), null);
		}
		else if ( SetDirectionActivity.selected_direction.equals("Etisalat") )
		{
			active_direction_button.setText(getResources().getString(R.string.direction_etisalat));
			active_direction_button.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_etisalat.getDrawable(), null);
		}
		else
		{
			active_direction_button.setText(getResources().getString(R.string.direction_creek));
			active_direction_button.setCompoundDrawables(sd_terminus_creek.getDrawable(), null, sd_arrow_right.getDrawable(), null);
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
        
        ListView station_list_view = (ListView)findViewById(R.id.station_list);
        
        final ArrayList<String> list = new ArrayList<String>();
        
        if ( SetDirectionActivity.is_red_line )
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
        		NextTrainActivity.ETA_destination = new DateTime();
        		Calendar calendar = Calendar.getInstance();
        		
        		final MediaPlayer player = MediaPlayer.create(SelectDestinationActivity.this, R.raw.menu_focus);
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
                
                if ( SetDirectionActivity.selected_direction.equals("Rashidiya") )
                {
                    if ( position > MainActivity.selected_row )
                    {
                        new AlertDialog.Builder(SelectDestinationActivity.this)
                        .setTitle("Oops!")
                        .setMessage("You seem to be on the wrong train then! Get off at the next station & take the next train to Jebel Ali.\n\nأنت على متن القطار الخطأ! اصعد على القطار القادم إلى جبل علي.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() 
                        {
                            public void onClick(DialogInterface dialog, int which)
                            { 
                            	dialog.cancel();
                            }
                         })
                         .show();
                        
                        return;
                    }
                    else
                    {
                        for ( int i = MainActivity.selected_row; i > position ; i-- )
                        {
                            int distance_time_in_seconds = Integer.parseInt(MainActivity.station_time_difference_red[i]) * 60;
                            
                            calendar.setTime(NextTrainActivity.ETA_destination.toDate());
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
                            NextTrainActivity.ETA_destination = new DateTime(calendar.getTime());
                        }
                    }
                }
                else if ( SetDirectionActivity.selected_direction.equals("JebelAli") )
                {
                    if ( position < MainActivity.selected_row )
                    {
                        new AlertDialog.Builder(SelectDestinationActivity.this)
                        .setTitle("Oops!")
                        .setMessage("You seem to be on the wrong train then! Get off at the next station & take the next train to Rashidiya.\n\nأنت على متن القطار الخطأ! اصعد على القطار القادم إلى الراشدية.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                        {
                        	public void onClick(DialogInterface dialog, int which)
                            { 
                            	dialog.cancel();
                            }
                         })
                         .show();
                        
                        return;
                    }
                    else
                    {
                    	for ( int i = MainActivity.selected_row; i < position ; i-- )
                        {
                            int distance_time_in_seconds = Integer.parseInt(MainActivity.station_time_difference_red[i]) * 60;
                            
                            calendar.setTime(NextTrainActivity.ETA_destination.toDate());
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
                            NextTrainActivity.ETA_destination = new DateTime(calendar.getTime());
                        }
                    }
                }
                else if ( SetDirectionActivity.selected_direction.equals("Etisalat") )
                {
                    if ( position > MainActivity.selected_row )
                    {
                        new AlertDialog.Builder(SelectDestinationActivity.this)
                        .setTitle("Oops!")
                        .setMessage("You seem to be on the wrong train then! Get off at the next station & take the next train to Creek.\n\nأنت على متن القطار الخطأ! اصعد على القطار القادم إلى الخور.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() 
                        {
                        	public void onClick(DialogInterface dialog, int which)
                            { 
                            	dialog.cancel();
                            }
                         })
                         .show();
                        
                        return;
                    }
                    else
                    {
                    	for ( int i = MainActivity.selected_row; i > position ; i-- )
                        {
                            int distance_time_in_seconds = Integer.parseInt(MainActivity.station_time_difference_green[i]) * 60;
                            
                            calendar.setTime(NextTrainActivity.ETA_destination.toDate());
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
                            NextTrainActivity.ETA_destination = new DateTime(calendar.getTime());
                        }
                    }
                }
                else if ( SetDirectionActivity.selected_direction.equals("Creek") )
                {
                    if ( position < MainActivity.selected_row )
                    {
                        new AlertDialog.Builder(SelectDestinationActivity.this)
                        .setTitle("Oops!")
                        .setMessage("You seem to be on the wrong train then! Get off at the next station & take the next train to Etisalat.\n\nأنت على متن القطار الخطأ! اصعد على القطار القادم إلى اتصالات.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                        {
                        	public void onClick(DialogInterface dialog, int which)
                            { 
                            	dialog.cancel();
                            }
                         })
                         .show();
                        
                        return;
                    }
                    else
                    {
                    	for ( int i = MainActivity.selected_row; i < position ; i-- )
                        {
                            int distance_time_in_seconds = Integer.parseInt(MainActivity.station_time_difference_green[i]) * 60;
                            
                            calendar.setTime(NextTrainActivity.ETA_destination.toDate());
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
                            NextTrainActivity.ETA_destination = new DateTime(calendar.getTime());
                        }
                    }
                }
                
                int ETAHour = NextTrainActivity.ETA_destination.getHourOfDay();
                int ETAMinute = NextTrainActivity.ETA_destination.getMinuteOfHour();
                String time_period = "am";
                NextTrainActivity.time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
                
                if ( ETAHour > 12 ) // Convert back to 12-hour format for display purposes.
                {
                    ETAHour -= 12;
                    time_period = "pm";
                    NextTrainActivity.time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
                }
                
                if ( ETAHour >= 12 ) // This needs its own fix for the case of 12 pm.
                {
                	time_period = "pm";
                	NextTrainActivity.time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
                }
                
                if ( ETAHour == 0 )
                {
                    ETAHour = 12;
                    time_period = "am";
                    NextTrainActivity.time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
                }
                
                NextTrainActivity.selected_destination_row = position;
        		
        		if ( SetDirectionActivity.is_red_line )
                {
        			NextTrainActivity.selected_destination = (String)MainActivity.station_list_red_names_en[position];
                }
                else
                {
                	NextTrainActivity.selected_destination = (String)MainActivity.station_list_green_names_en[position];
                }
        		
                goBack();
	        }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_destination, menu);
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
	
	public void goBack()
	{
		finish();
	}
}

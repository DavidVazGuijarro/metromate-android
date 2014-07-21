package com.scapehouse.metromate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class NextTrainActivity extends Activity
{
	public final static int TIME_FROM_ANNOUNCEMENT = 14;       // Seconds between the train's arrival announcement and it coming to a stop.
	public final static int TIME_ON_PLATFORM = 25;             // 25 second wait.
	public final static int TIME_ON_PLATFORM_INTER_RED = 35;   // 35 second wait.
	public final static int TIME_ON_PLATFORM_INTER_GREEN = 45; // 45 second wait.
	static TextView eta_label;
	static Button on_train_button;
	static Timer timer_help_label;
	static Timer timer_next_train_label;
	static Timer timer_next_train_fetch;
	static Timer timer_countdown;
	static Timer timer_destination_ETA;
	static DateTime ETA;
	static DateTime ETA_destination;
	static String time_of_arrival;
	static String selected_destination;
	static int selected_destination_row = -1;
	static long seconds_to_departure;
	static boolean countdown_started = false;
	static boolean train_service_running = true;
	static boolean is_last_train = false;
	static boolean displayed_nol_card_info_is_english = true;
	static boolean displayed_notification_is_english = false;
	static boolean displayed_station_info_is_english = true;
	static boolean displayed_time_is_english = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next_train);
		
		Typeface shmm_default_font = Typeface.createFromAsset(getAssets(), "fonts/arial_rounded_bold.ttf");
		
		TextView station_number_label = (TextView)findViewById(R.id.station_number_label);
		on_train_button = (Button)findViewById(R.id.on_train_button);
		Button active_direction_button = (Button)findViewById(R.id.active_direction_button);
		
		on_train_button.setTextColor(Color.parseColor("white"));
		active_direction_button.setTextColor(Color.parseColor("white"));
		
		on_train_button.setTypeface(shmm_default_font);
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
        
        if ( SetDirectionActivity.is_red_line )
        {
        	if ( SetDirectionActivity.selected_direction.equals("Rashidiya") )
        	{
        		active_direction_button.setText(getResources().getString(R.string.direction_rashidiya));
				active_direction_button.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_rashidiya.getDrawable(), null);
        	}
        	else
        	{
        		active_direction_button.setText(getResources().getString(R.string.direction_jebel_ali));
				active_direction_button.setCompoundDrawables(sd_terminus_jebel_ali.getDrawable(), null, sd_arrow_right.getDrawable(), null);
        	}
        }
        else
        {
        	if ( SetDirectionActivity.selected_direction.equals("Etisalat") )
        	{
        		active_direction_button.setText(getResources().getString(R.string.direction_etisalat));
				active_direction_button.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_etisalat.getDrawable(), null);
        	}
        	else
        	{
        		active_direction_button.setText(getResources().getString(R.string.direction_creek));
				active_direction_button.setCompoundDrawables(sd_terminus_creek.getDrawable(), null, sd_arrow_right.getDrawable(), null);
        	}
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
        
        ValueAnimator location_arrow_animation = ObjectAnimator.ofFloat(findViewById(R.id.location_arrow), "alpha", 1.0f, 0.5f);
        location_arrow_animation.setDuration(500);
        location_arrow_animation.setRepeatCount(ValueAnimator.INFINITE);
        location_arrow_animation.setRepeatMode(ValueAnimator.REVERSE);
        location_arrow_animation.start();
        
        if ( timer_help_label == null )
        {
        	timer_help_label = new Timer();
    		
        	timer_help_label.scheduleAtFixedRate(new TimerTask()
        	{
        		  @Override
        		  public void run()
        		  {
        			  alternateHelpLabelLanguage();
        		  }
        		}, 2 * 1000, 2 * 1000 + 2
        	);
        }
        
        TextView station_name_label = (TextView)findViewById(R.id.station_list_description_label);
        TextView next_train_label = (TextView)findViewById(R.id.next_train_label);
        eta_label = (TextView)findViewById(R.id.eta_label);
        
        station_name_label.setText(SetDirectionActivity.selected_station);
        station_name_label.setTypeface(shmm_default_font);
        next_train_label.setTypeface(shmm_default_font);
		
        station_number_label.setTypeface(shmm_default_font);
        
        if ( SetDirectionActivity.is_red_line )
        {
        	station_number_label.setText(MainActivity.station_list_red_numbers[MainActivity.selected_row]);
        }
        else
        {
        	station_number_label.setText(MainActivity.station_list_green_numbers[MainActivity.selected_row]);
        }
        
        fetchTrainTiming();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.next_train, menu);
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
	
	public void alternateTimingLabelLanguage()
	{
		final TextView next_train_label = (TextView)findViewById(R.id.next_train_label);
		final TextView countdown_label = (TextView)findViewById(R.id.countdown_label);
		
		Calendar calendar = Calendar.getInstance();
		Date date_today = new Date();
	    
		calendar.setTime(date_today);
		
		int time_on_platform = TIME_ON_PLATFORM;
	    long current_seconds = (long)((calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis())) / 1000);
	    
	    calendar.setTime(ETA.toDate());
	    
	    long ETASeconds = (long)((calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis())) / 1000);
	    
	    // Waits at interchange stations are different.
	    if ( SetDirectionActivity.is_red_line )
	    {
	        if ( MainActivity.selected_row == 7 || MainActivity.selected_row == 8 )
	        {
	        	time_on_platform = TIME_ON_PLATFORM_INTER_RED;
	        }
	    }
	    else
	    {
	        if ( MainActivity.selected_row == 9 || MainActivity.selected_row == 15 )
	        {
	        	time_on_platform = TIME_ON_PLATFORM_INTER_GREEN;
	        }
	    }
	    
	    //========================================
	    long timeLeftInSeconds = Math.abs(ETASeconds - current_seconds);
	    long timeLeftInMinutes = (timeLeftInSeconds + 59) / 60;
	    
	    String display_string = timeLeftInMinutes + "";
	    
	    if ( displayed_time_is_english )
	    {
	    	displayed_time_is_english = false;
	        
	        if ( current_seconds >= ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	        	display_string = "القطار على الرصيف";
	            seconds_to_departure = TIME_FROM_ANNOUNCEMENT + time_on_platform - (current_seconds - ETASeconds);
	            
	            if ( seconds_to_departure >= 0 ) // Don't want negative numbers showing up.
	            {
	            	if ( seconds_to_departure == 0 )
	            	{
	            		timer_countdown.cancel();
	            		timer_countdown.purge();
	            		
	            		timer_countdown = null;
	            	}
	            	
	            	this.runOnUiThread(new Runnable() // Label text modifying code needs to run on the UI thread.
	            	{
	            	    public void run()
	            	    {
	            	    	countdown_label.setText(seconds_to_departure + "");
	            	    	countdown_label.setAlpha(1);
	            	    	
	            	    	eta_label.setAlpha(0);
	            	    	
	            	    	on_train_button.setText("أنا على متن القطار");
	            	    	on_train_button.setVisibility(View.VISIBLE);
	            	    }
	            	});
	            }
	            
	            if ( !countdown_started )
	            {
	                countdown_started = true;
	                
	                if ( timer_countdown == null )
	    	        {
	                	timer_countdown = new Timer();
	                	
	                	timer_countdown.scheduleAtFixedRate(new TimerTask()
		            	{
		            		  @Override
		            		  public void run()
		            		  {
		            			  countdownToDeparture(); // Begin countdown.
		            		  }
		            		}, 0, 1 * 1000
		            	);
	    	        }
	            }
	            
	            if ( timer_next_train_fetch == null )
	            {
	            	long timer_delay = (seconds_to_departure + 18) * 1000;
	            	
	            	if ( timer_delay < 0 )
	            	{
	            		timer_delay = 0;
	            	}
	            	
	            	timer_next_train_fetch = new Timer();
	            	
	            	timer_next_train_fetch.schedule(new TimerTask()
	            	{
	            		  @Override
	            		  public void run()
	            		  {
	            			  fetchTrainTiming();
	            		  }
	            		}, timer_delay // How long the train waits on the platform.
	            	);
	            }
	        }
	        else if ( current_seconds >= (ETASeconds - 20) && current_seconds < ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	        	display_string = "القطار يقترب من المحطة";
	        }
	        else
	        {
	            if ( is_last_train )
	            {
	            	this.runOnUiThread(new Runnable()
	            	{
	            	    public void run()
	            	    {
	            	    	countdown_label.setText("آخر قطار!");
	            	    }
	            	});
	            }
	            
	            if ( timeLeftInMinutes == 1 )
	            {
	            	display_string = "القطار القادم\n" + MainActivity.westernToArabicNumerals(display_string) + " دقيقة";
	            }
	            else
	            {
	            	display_string = "القطار القادم\n" + MainActivity.westernToArabicNumerals(display_string) + " دقائق";
	            }
	        }
	    }
	    else
	    {
	    	displayed_time_is_english = true;
	        
	        if ( current_seconds >= ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	        	display_string = "Train is on the platform";
	            seconds_to_departure = TIME_FROM_ANNOUNCEMENT + TIME_ON_PLATFORM - (current_seconds - ETASeconds);
	            
	            if ( seconds_to_departure >= 0 )
	            {
	            	if ( seconds_to_departure == 0 )
	            	{
	            		timer_countdown.cancel();
	            		timer_countdown.purge();
	            		
	            		timer_countdown = null;
	            	}
	            	
	            	this.runOnUiThread(new Runnable()
	            	{
	            	    public void run()
	            	    {
	            	    	countdown_label.setText(seconds_to_departure + "");
	            	    	countdown_label.setAlpha(1);
	            	    	
	            	    	eta_label.setAlpha(0);
	            	    	
	            	    	on_train_button.setText("I'm on the train");
	            	    	on_train_button.setVisibility(View.VISIBLE);
	            	    }
	            	});
	            }
	            
	            if ( !countdown_started )
	            {
	            	countdown_started = true;
	                
	            	if ( timer_countdown == null )
	    	        {
	            		timer_countdown = new Timer();
	            		
	            		timer_countdown.scheduleAtFixedRate(new TimerTask()
		            	{
		            		  @Override
		            		  public void run()
		            		  {
		            			  countdownToDeparture(); // Begin countdown.
		            		  }
		            		}, 0, 1 * 1000
		            	);
	    	        }
	            }
	            
	            if ( timer_next_train_fetch == null )
	            {
	            	long timer_delay = (seconds_to_departure + 18) * 1000;
	            	
	            	if ( timer_delay < 0 )
	            	{
	            		timer_delay = 0;
	            	}
	            	
	            	timer_next_train_fetch = new Timer();
	            	
	            	timer_next_train_fetch.schedule(new TimerTask()
	            	{
	            		  @Override
	            		  public void run()
	            		  {
	            			  fetchTrainTiming();
	            		  }
	            		},timer_delay // How long the train waits on the platform.
	            	);
	            }
	        }
	        else if ( current_seconds >= (ETASeconds - 20) && current_seconds < ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	        	display_string = "Train entering station";
	        }
	        else
	        {
	            if ( is_last_train )
	            {
	            	this.runOnUiThread(new Runnable()
	            	{
	            	    public void run()
	            	    {
	            	    	countdown_label.setText("Last train!");
	            	    }
	            	});
	            }
	            
	            if ( timeLeftInMinutes == 1 )
	            {
	            	display_string = "Next Train\n1 min";
	            }
	            else
	            {
	            	display_string = "Next Train\n" + timeLeftInMinutes + " mins";
	            }
	        }
	    }
	    
	    final String final_display_string = display_string;
	    
	    this.runOnUiThread(new Runnable()
    	{
    	    public void run()
    	    {
    	    	next_train_label.setText(final_display_string);
    	    }
    	});
	}

	public void fetchTrainTiming()
	{
		try
        {	
			if ( timer_next_train_label != null )
			{
				timer_next_train_label.cancel();
				timer_next_train_label.purge();
				timer_next_train_label = null;
			}
			
			if ( timer_next_train_fetch != null )
			{
				timer_next_train_fetch.cancel();
				timer_next_train_fetch.purge();
				timer_next_train_fetch = null;
			}
			
			if ( timer_countdown != null )
			{
				timer_countdown.cancel();
				timer_countdown.purge();
				timer_countdown = null;
			}
			
			if ( timer_destination_ETA != null )
			{
				timer_destination_ETA.cancel();
				timer_destination_ETA.purge();
				timer_destination_ETA = null;
			}
			
			final TextView countdown_label = (TextView)findViewById(R.id.countdown_label);
			final TextView next_train_label = (TextView)findViewById(R.id.next_train_label);
			
			List<String> station_timings = new ArrayList<String>();
	        
			LocalDate date_today = new LocalDate();
	        
	        Calendar calendar = Calendar.getInstance(); 
	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
	        int current_minute = calendar.get(Calendar.MINUTE);
	        
	        if ( current_hour < 5 ) // 24-hour format fix.
		    {
		        current_hour += 12;
		    }
	        
	        DateTimeFormatter date_formatter = DateTimeFormat.forPattern("EEEE");
	        
	        String week_day = date_formatter.print(date_today);
	        int last_index = 0; // Index of the last train timing.
	        
	        if ( SetDirectionActivity.selected_direction.equals("Rashidiya") )
	        {
	            if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 4 )  // Logic fix for when the clock passes 12 am.
	            {
	            	station_timings = MainActivity.station_timings_rashidiya_saturday.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 1; // Index of the last train timing.
	            }
	            else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 8 )
	            {
	            	station_timings = MainActivity.station_timings_rashidiya_friday.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 1;
	            }
	            else
	            {
	            	station_timings = MainActivity.station_timings_rashidiya_week_days.get(MainActivity.selected_row);
	                
	                if ( week_day.equals("Thursday") )
	                {
	                	last_index = station_timings.size() - 1;
	                }
	                else
	                {
	                	last_index = station_timings.size() - 7;
	                }
	            }
	        }
	        else if ( SetDirectionActivity.selected_direction.equals("JebelAli") )
	        {
	            if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 3 )
	            {
	            	station_timings = MainActivity.station_timings_jebel_ali_saturday.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 1;
	            }
	            else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 9 )
	            {
	                station_timings = MainActivity.station_timings_jebel_ali_friday.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 1;
	            }
	            else
	            {
	                station_timings = MainActivity.station_timings_jebel_ali_week_days.get(MainActivity.selected_row);
	                
	                if ( week_day.equals("Thursday") )
	                {
	                	last_index = station_timings.size() - 1;
	                }
	                else
	                {
	                	last_index = station_timings.size() - 7;
	                }
	            }
	        }
	        else if ( SetDirectionActivity.selected_direction.equals("Etisalat") )
	        {
	            if ( week_day.equals("Saturday") && current_hour >= 1 )
	            {
	            	station_timings = MainActivity.station_timings_etisalat_week_days.get(MainActivity.selected_row);
	            	last_index = station_timings.size() - 8;
	            }
	            else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 2 )
	            {
	            	station_timings = MainActivity.station_timings_etisalat_friday.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 1;
	            }
	            else
	            {
	            	station_timings = MainActivity.station_timings_etisalat_week_days.get(MainActivity.selected_row);
	                
	                if ( week_day.equals("Thursday") )
	                {
	                	last_index = station_timings.size() - 1;
	                }
	                else
	                {
	                	last_index = station_timings.size() - 8;
	                }
	            }
	        }
	        else
	        {
	            if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 4 )
	            {
	            	station_timings = MainActivity.station_timings_creek_week_days.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 8;
	            }
	            else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 6 )
	            {
	            	station_timings = MainActivity.station_timings_creek_friday.get(MainActivity.selected_row);
	                last_index = station_timings.size() - 1;
	            }
	            else
	            {
	            	station_timings = MainActivity.station_timings_creek_week_days.get(MainActivity.selected_row);
	                
	                if ( week_day.equals("Thursday") )
	                {
	                	last_index = station_timings.size() - 1;
	                }
	                else
	                {
	                	last_index = station_timings.size() - 8;
	                }
	            }
	        }
	        
	        for ( int i = 0; i < station_timings.size(); i++ )
	        {
	        	date_formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	            String date_string = date_formatter.print(date_today);
	            String time = station_timings.get(i);
	            train_service_running = true;
	            is_last_train = false;
	            
	            if ( i == last_index )
	            {
	                is_last_train = true;
	            }
	            else if ( i > last_index )
	            {
	                train_service_running = false;
	                break;
	            }
	            
	            String final_string = date_string + " " + time + " GMT+0400";
	            
	            date_formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss 'GMT'Z");
	            
	            ETA = date_formatter.withOffsetParsed().parseDateTime(final_string);
	            
	            int ETAHour = ETA.getHourOfDay();
	            int ETAMinute = ETA.getMinuteOfHour();
	            
	            if ( ETAHour == 0 ) // Fucks up the conditions otherwise...
	            {
	                ETAHour = 24;
	            }
	            
	            if ( current_hour == 0 )
	            {
	            	current_hour = 24;
	            }
	            
	            if ( (ETAHour > current_hour) || (ETAHour == current_hour && ETAMinute >= current_minute) )
	            {
	                seconds_to_departure = TIME_ON_PLATFORM;
	                train_service_running = true;
	                
	                break;
	            }
	        }
	        
	        if ( !train_service_running )
	        {
	        	this.runOnUiThread(new Runnable()
		    	{
		    	    public void run()
		    	    {
		    	    	eta_label.setAlpha(0);
		    	    	countdown_label.setAlpha(0);
		    	    	on_train_button.setVisibility(View.INVISIBLE);
		    	    	
		    	    	next_train_label.setText("No information\nلا توجد معلومات");
		    	    }
		    	});
	            
	            return;
	        }
	        
	        int ETAHour = ETA.getHourOfDay();
            int ETAMinute = ETA.getMinuteOfHour();
	        String time_period = "am";
	        
	        time_of_arrival = String.format(Locale.getDefault(), "%02d:%02d %s", ETAHour, ETAMinute, time_period);
	        
	        if ( ETAHour > 12 ) // Convert back to 12-hour format for display purposes.
	        {
	            ETAHour -= 12;
	            time_period = "pm";
	            time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
	        }
	        
	        if ( ETAHour >= 12 ) // This needs its own fix for the case of 12 pm.
	        {
	        	time_period = "pm";
	            time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
	        }
	        
	        if ( ETAHour == 0 )
	        {
	            ETAHour = 12;
	            time_period = "am";
	            time_of_arrival = String.format("%02d:%02d %s", ETAHour, ETAMinute, time_period);
	        }
	        
	        this.runOnUiThread(new Runnable()
	    	{
	    	    public void run()
	    	    {
	    	    	Animation fadeOut = new AlphaAnimation(1, 0);
	    	    	fadeOut.setInterpolator(new AccelerateInterpolator());
	    	    	fadeOut.setStartOffset(1000);
	    	    	fadeOut.setDuration(1000);
	    	    	
	    	    	AnimationSet animation_1 = new AnimationSet(false);
	    	    	animation_1.addAnimation(fadeOut);
	    	    	countdown_label.setAnimation(animation_1);
	    	    	
	    	    	eta_label.setText("ETA " + time_of_arrival);
	    	    	eta_label.setAlpha(1);
	    	    	
	    	    	countdown_label.setAlpha(0);
	    	    	on_train_button.setVisibility(View.INVISIBLE);
	    	    }
	    	});
	    	
	        if ( timer_next_train_label == null )
	        {
	        	timer_next_train_label = new Timer();
	        	
	        	timer_next_train_label.scheduleAtFixedRate(new TimerTask()
	        	{
	        		  @Override
	        		  public void run()
	        		  {
	        			  alternateTimingLabelLanguage();
	        		  }
	        		}, 0, 5 * 1000
	        	);
	        }
        }
        catch ( Exception e )
        {
       	  	// Handle the exception.
        	System.out.println("Exception in timing fetch: " + e);
        }
	}
	
	public void countdownToDeparture()
	{
		if ( seconds_to_departure > 0 )
	    {
			seconds_to_departure--;
	    }
	    
	    if ( seconds_to_departure >= 0 )
	    {
	    	final TextView countdown_label = (TextView)findViewById(R.id.countdown_label);
	    	
	    	this.runOnUiThread(new Runnable()
	    	{
	    	    public void run()
	    	    {
	    	    	countdown_label.setText(seconds_to_departure + "");
	    	    }
	    	});
	    }
	}
	
	public void alternateHelpLabelLanguage()
	{
		final Animation anim_out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out); 
	    final Animation anim_in  = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
	    
		this.runOnUiThread(new Runnable()
    	{
    	    public void run()
    	    {
    	    	final TextView station_name_label = (TextView)findViewById(R.id.station_list_description_label);

    		    anim_out.setAnimationListener(new AnimationListener()
    		    {
    		        @Override
    		        public void onAnimationStart(Animation animation) {}

    		        @Override
    		        public void onAnimationRepeat(Animation animation) {}

    		        @Override
    		        public void onAnimationEnd(Animation animation)
    		        {
    		        	String display_string = "";
    		    		int target_row = MainActivity.selected_row;
    		    		
    		    		if ( selected_destination_row != -1 )
    		    		{
    		    			target_row = selected_destination_row;
    		    		}
    		        	
    		    		if ( SetDirectionActivity.is_red_line )
    		            {
    		                if ( displayed_station_info_is_english )
    		                {
    		                	displayed_station_info_is_english = false;
    		                	display_string = MainActivity.station_list_red_names_ar[target_row];
    		                }
    		                else
    		                {
    		                	displayed_station_info_is_english = true;
    		                	display_string = MainActivity.station_list_red_names_en[target_row];
    		                }
    		            }
    		            else
    		            {
    		                if ( displayed_station_info_is_english )
    		                {
    		                	displayed_station_info_is_english = false;
    		                	display_string = MainActivity.station_list_green_names_ar[target_row];
    		                }
    		                else
    		                {
    		                	displayed_station_info_is_english = true;
    		                	display_string = MainActivity.station_list_green_names_en[target_row];
    		                }
    		            }
    		    		
    		    		station_name_label.setText(display_string);

    		            anim_in.setAnimationListener(new AnimationListener()
    		            {
    		                @Override
    		                public void onAnimationStart(Animation animation) {}

    		                @Override
    		                public void onAnimationRepeat(Animation animation) {}

    		                @Override
    		                public void onAnimationEnd(Animation animation) {}
    		            });

    		            station_name_label.startAnimation(anim_in);
    		        }
    		    });

    		    station_name_label.startAnimation(anim_out);
    	    }
    	});
	}
	
	public void dispalayDestinationETA()
	{
		final TextView next_train_label = (TextView)findViewById(R.id.next_train_label);
		final TextView countdown_label = (TextView)findViewById(R.id.countdown_label);
		
		Calendar calendar = Calendar.getInstance();
		Date date_today = new Date();
	    
		calendar.setTime(date_today);
		
		int time_on_platform = TIME_ON_PLATFORM;
	    long current_seconds = (long)((calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis())) / 1000);
	    
	    calendar.setTime(ETA_destination.toDate());
	    
	    long ETASeconds = (long)((calendar.getTimeInMillis() + calendar.getTimeZone().getOffset(calendar.getTimeInMillis())) / 1000);
	    
	    // Waits at interchange stations are different.
	    if ( SetDirectionActivity.is_red_line )
	    {
	        if ( MainActivity.selected_row == 7 || MainActivity.selected_row == 8 )
	        {
	        	time_on_platform = TIME_ON_PLATFORM_INTER_RED;
	        }
	    }
	    else
	    {
	        if ( MainActivity.selected_row == 9 || MainActivity.selected_row == 15 )
	        {
	        	time_on_platform = TIME_ON_PLATFORM_INTER_GREEN;
	        }
	    }
	    
	    //========================================
	    long timeLeftInSeconds = Math.abs(ETASeconds - current_seconds);
	    long timeLeftInMinutes = (timeLeftInSeconds + 59) / 60;
	    
	    String display_string = timeLeftInMinutes + "";
	    
	    if ( displayed_time_is_english )
	    {
	    	displayed_time_is_english = false;
	        
	        if ( current_seconds >= ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	        	display_string = "انزل من القطار";
	        	seconds_to_departure = TIME_FROM_ANNOUNCEMENT + time_on_platform - (current_seconds - ETASeconds);
	            
	            if ( seconds_to_departure >= 0 ) // Don't want negative numbers showing up.
	            {
	            	if ( seconds_to_departure == 0 )
	            	{
	            		timer_countdown.cancel();
	            		timer_countdown.purge();
	            		
	            		timer_countdown = null;
	            	}
	            	
	            	this.runOnUiThread(new Runnable() // Label text modifying code needs to run on the UI thread.
	            	{
	            	    public void run()
	            	    {
	            	    	countdown_label.setText(seconds_to_departure + "");
	            	    	countdown_label.setAlpha(1);
	            	    	
	            	    	eta_label.setAlpha(0);
	            	    }
	            	});
	            }
	            
	        	if ( !countdown_started )
	            {
	                countdown_started = true;
	                
	                if ( timer_countdown == null )
	    	        {
	                	timer_countdown = new Timer();
	                	
	                	timer_countdown.scheduleAtFixedRate(new TimerTask()
		            	{
		            		  @Override
		            		  public void run()
		            		  {
		            			  countdownToDeparture(); // Begin countdown.
		            		  }
		            		}, 0, 1 * 1000
		            	);
	    	        }
	            }
	        }
	        else if ( current_seconds >= (ETASeconds - 20) && current_seconds < ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	            display_string = "المحطة القادمة هي محطتنا";
	        }
	        else
	        {
	            if ( timeLeftInMinutes == 1 )
	            {
	            	display_string = "سنصل بعد\n" + MainActivity.westernToArabicNumerals(display_string) + " دقيقة";
	            }
	            else
	            {
	            	display_string = "سنصل بعد\n" + MainActivity.westernToArabicNumerals(display_string) + " دقائق";
	            }
	        }
	    }
	    else
	    {
	    	displayed_time_is_english = true;
	        
	        if ( current_seconds >= ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	            display_string = "Get off the train";
	            
	            this.runOnUiThread(new Runnable() // Label text modifying code needs to run on the UI thread.
            	{
            	    public void run()
            	    {
            	    	countdown_label.setText(seconds_to_departure + "");
            	    	countdown_label.setAlpha(1);
            	    	
            	    	eta_label.setAlpha(0);
            	    }
            	});
	            
	            if ( !countdown_started )
	            {
	                countdown_started = true;
	                
	                if ( timer_countdown == null )
	    	        {
	                	timer_countdown = new Timer();
	                	
	                	timer_countdown.scheduleAtFixedRate(new TimerTask()
		            	{
		            		  @Override
		            		  public void run()
		            		  {
		            			  countdownToDeparture(); // Begin countdown.
		            		  }
		            		}, 0, 1 * 1000
		            	);
	    	        }
	            }
	        }
	        else if ( current_seconds >= (ETASeconds - 20) && current_seconds < ETASeconds + TIME_FROM_ANNOUNCEMENT )
	        {
	            display_string = "This station is our stop";
	        }
	        else
	        {
	            if ( timeLeftInMinutes == 1 )
	            {
	            	display_string = "We'll be arriving in\n1 min";
	            }
	            else
	            {
	            	display_string = "We'll be arriving in\n" + timeLeftInMinutes + " mins";
	            }
	        }
	    }
	    
	    final String final_display_string = display_string;
	    
	    this.runOnUiThread(new Runnable()
    	{
    	    public void run()
    	    {
    	    	next_train_label.setText(final_display_string);
    	    }
    	});
	}
	
	public void embarkTrain(View view)
	{
		final MediaPlayer player = MediaPlayer.create(this, R.raw.menu_focus);
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
        
		Intent intent = new Intent(this, SelectDestinationActivity.class);
    	
    	startActivity(intent);
	}
	
	public void goBack(View view)
	{
		finish();
	}
	
	public static void cancelAllTimers()
	{
		if ( timer_help_label != null )
		{
			timer_help_label.cancel();  // Terminates this timer, discarding any currently scheduled tasks.
			timer_help_label.purge();
			timer_help_label = null;
		}
		
		if ( timer_next_train_label != null )
		{
			timer_next_train_label.cancel();
			timer_next_train_label.purge();
			timer_next_train_label = null;
		}
		
		if ( timer_next_train_fetch != null )
		{
			timer_next_train_fetch.cancel();
			timer_next_train_fetch.purge();
			timer_next_train_fetch = null;
		}
		
		if ( timer_countdown != null )
		{
			timer_countdown.cancel();
			timer_countdown.purge();
			timer_countdown = null;
		}
		
		if ( timer_destination_ETA != null )
		{
			timer_destination_ETA.cancel();
			timer_destination_ETA.purge();
			timer_destination_ETA = null;
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		selected_destination_row = -1; // Reset this.
		
		cancelAllTimers();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if ( selected_destination_row != -1 )
		{
			final TextView countdown_label = (TextView)findViewById(R.id.countdown_label);
			
			this.runOnUiThread(new Runnable()
        	{
        	    public void run()
        	    {
        	    	Animation fadeOut = new AlphaAnimation(1, 0);
	    	    	fadeOut.setInterpolator(new AccelerateInterpolator());
	    	    	fadeOut.setStartOffset(1000);
	    	    	fadeOut.setDuration(1000);
	    	    	
	    	    	AnimationSet animation_1 = new AnimationSet(false);
	    	    	animation_1.addAnimation(fadeOut);
	    	    	countdown_label.setAnimation(animation_1);
	    	    	
	    	    	eta_label.setText("ETA at destination: " + time_of_arrival);
	    	    	eta_label.setAlpha(1);
	    	    	
	    	    	countdown_label.setAlpha(0);
	    	    	on_train_button.setVisibility(View.INVISIBLE);
        	    }
        	});
			
			if ( timer_destination_ETA == null )
	        {
				timer_destination_ETA = new Timer();
	        	
				timer_destination_ETA.scheduleAtFixedRate(new TimerTask()
	        	{
	        		  @Override
	        		  public void run()
	        		  {
	        			  dispalayDestinationETA();
	        		  }
	        		}, 0, 5 * 1000
	        	);
	        }
			
			if ( timer_help_label == null )
	        {
	        	timer_help_label = new Timer();
	    		
	        	timer_help_label.scheduleAtFixedRate(new TimerTask()
	        	{
	        		  @Override
	        		  public void run()
	        		  {
	        			  alternateHelpLabelLanguage();
	        		  }
	        		}, 2 * 1000, 2 * 1000 + 2
	        	);
	        }
		}
		else
		{
			fetchTrainTiming();
		}
	}
}

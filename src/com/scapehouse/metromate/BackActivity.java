package com.scapehouse.metromate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class BackActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back);
		
		Typeface shmm_default_font = Typeface.createFromAsset(getAssets(), "fonts/arial_rounded_bold.ttf");
		Typeface shmm_georgia = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		
		Button nol_info_button = (Button)findViewById(R.id.nol_info_button);
        Button call_rta_button = (Button)findViewById(R.id.call_rta_button);
        
		TextView version_label = (TextView)findViewById(R.id.version_label);
		TextView about_label = (TextView)findViewById(R.id.about_label);
		TextView copyright_label = (TextView)findViewById(R.id.copyright_label);
		
		Drawable drawable_phone = getResources().getDrawable(R.drawable.phone);
        Drawable drawable_rta_logo = getResources().getDrawable(R.drawable.rta_logo);
		
        drawable_phone.setBounds(20, 3, (int)(drawable_phone.getIntrinsicWidth()), 
             								(int)(drawable_phone.getIntrinsicHeight()));
        drawable_rta_logo.setBounds(-20, 5, (int)(drawable_rta_logo.getIntrinsicWidth() * 0.5), 
											(int)(drawable_rta_logo.getIntrinsicHeight() * 0.5));
        
        ScaleDrawable sd_phone = new ScaleDrawable(drawable_phone, 0, 5000, 5000);
        ScaleDrawable sd_rta_logo = new ScaleDrawable(drawable_rta_logo, 0, 5000, 5000);
        
        call_rta_button.setCompoundDrawables(sd_phone.getDrawable(), null, sd_rta_logo.getDrawable(), null);
        
		nol_info_button.setTextColor(Color.parseColor("white"));
		call_rta_button.setTextColor(Color.parseColor("white"));
        
        nol_info_button.setTypeface(shmm_default_font);
        call_rta_button.setTypeface(shmm_default_font);
        
		version_label.setTypeface(shmm_default_font);
		about_label.setTypeface(shmm_georgia);
		copyright_label.setTypeface(shmm_georgia);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.back, menu);
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ( keyCode == KeyEvent.KEYCODE_MENU )
        {
        	finish();
        	
            return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
	
	public void presentNolInfo(View view)
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
        
		Intent intent = new Intent(this, NolInfoActivity.class);
    	
    	startActivity(intent);
	}
	
	public void callRTA(View view)
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
        
		try
		{
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:8009090"));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
            startActivity(callIntent);
        }
		catch (ActivityNotFoundException activityException)
        {
            System.out.println("Call failed!" + activityException);
        }
	}
}

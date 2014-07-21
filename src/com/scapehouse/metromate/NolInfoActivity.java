package com.scapehouse.metromate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class NolInfoActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nol_info);
		
		Typeface shmm_default_font = Typeface.createFromAsset(getAssets(), "fonts/arial_rounded_bold.ttf");
		
		TextView title_label = (TextView) findViewById(R.id.title_label);
		EditText nol_number_field = (EditText) findViewById(R.id.nol_number_field);
		Button check_balance_button = (Button) findViewById(R.id.check_balance_button);
		
		title_label.setTypeface(shmm_default_font);
		
		check_balance_button.setTypeface(shmm_default_font);
		
		if ( !MainActivity.nol_card_number.equals("no_card") )
		{
			nol_number_field.setText(MainActivity.nol_card_number, TextView.BufferType.EDITABLE);
		}
		
		nol_number_field.setTypeface(shmm_default_font);
		nol_number_field.setOnEditorActionListener(new OnEditorActionListener()
		{
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		    {
		        if ( actionId == EditorInfo.IME_ACTION_DONE )
		        {
		        	checkCardBalance(null);
		        }
		        
		        return false;
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nol_info, menu);
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
	
	public void checkCardBalance(View view)
	{
		EditText nol_number_field = (EditText) findViewById(R.id.nol_number_field);
		
		String card_number = nol_number_field.getText().toString();
    	card_number = card_number.replaceAll("[^0-9]", "");
    	card_number = card_number.trim();
    	
    	// We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("nol_card_number", card_number);
        
        // Commit the edits!
        editor.commit();
        
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nol_number_field.getWindowToken(), 0);
        
        MainActivity.nol_card_number = card_number;
        
        MainActivity.checkCardBalance();
	}
}

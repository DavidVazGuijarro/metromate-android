package com.scapehouse.metromate;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity
{
	public static final String PREFS_NAME = "SH_MM_PREFERENCES";
	public static final String DIRECTION = "com.scapehouse.metromate.DIRECTION";
	public static double location_longitude = -9999;
	public static double location_latitude = -9999;
	public static int selected_row = -1;
	public static boolean station_autoselected = false;
	public static boolean is_checking_balance = false;
	public static String nol_card_number = "no_card";
	public static float nol_current_balance;
	protected static GPSTracker gps;
	public static Map <String, JSONObject> nearest_station_data =  new HashMap<String, JSONObject>();
	
	static String[] station_red_1_coordinates = {"25.230309", "55.391162"};
	static String[] station_red_2_coordinates = {"25.241076", "55.365729"};
	static String[] station_red_3_coordinates = {"25.244958", "55.359592"};
	static String[] station_red_4_coordinates = {"25.248417", "55.352468"};
	static String[] station_red_5_coordinates = {"25.249475", "55.340002"};
	static String[] station_red_6_coordinates = {"25.253966", "55.330136"};
	static String[] station_red_7_coordinates = {"25.262657", "55.325131"};
	static String[] station_red_8_coordinates = {"25.266291", "55.31389"};
	static String[] station_red_9_coordinates = {"25.254856", "55.304312"};
	static String[] station_red_10_coordinates = {"25.244507", "55.298218"};
	static String[] station_red_11_coordinates = {"25.233589", "55.292172"};
	static String[] station_red_12_coordinates = {"25.22482", "55.284979"};
	static String[] station_red_13_coordinates = {"25.217283", "55.27984"};
	static String[] station_red_14_coordinates = {"25.211061", "55.275559"};
	static String[] station_red_15_coordinates = {"25.201388", "55.269518"};
	static String[] station_red_16_coordinates = {"25.191272", "55.260404"};
	static String[] station_red_17_coordinates = {"25.155623", "55.228443"};
	static String[] station_red_18_coordinates = {"25.126621", "55.207774"};
	static String[] station_red_19_coordinates = {"25.121114", "55.200344"};
	static String[] station_red_20_coordinates = {"25.114727", "55.190855"};
	static String[] station_red_21_coordinates = {"25.101985", "55.173683"};
	static String[] station_red_22_coordinates = {"25.088796", "55.158008"};
	static String[] station_red_23_coordinates = {"25.079851", "55.147414"};
	static String[] station_red_24_coordinates = {"25.070717", "55.138546"};
	static String[] station_red_25_coordinates = {"25.05783", "55.127136"};
	static String[] station_red_26_coordinates = {"25.046638", "55.117416"};
	static String[] station_red_27_coordinates = {"25.026346", "55.101244"};
	static String[] station_red_28_coordinates = {"25.001267", "55.095711"};
	static String[] station_red_29_coordinates = {"24.977485", "55.091007"};
	
	public final static String[][] station_coordinates_red = {station_red_1_coordinates,
											station_red_2_coordinates,
											station_red_3_coordinates,
											station_red_4_coordinates,
											station_red_5_coordinates,
											station_red_6_coordinates,
											station_red_7_coordinates,
											station_red_8_coordinates,
											station_red_9_coordinates,
											station_red_10_coordinates,
											station_red_11_coordinates,
											station_red_12_coordinates,
											station_red_13_coordinates,
											station_red_14_coordinates,
											station_red_15_coordinates,
											station_red_16_coordinates,
											station_red_17_coordinates,
											station_red_18_coordinates,
											station_red_19_coordinates,
											station_red_20_coordinates,
											station_red_21_coordinates,
											station_red_22_coordinates,
											station_red_23_coordinates,
											station_red_24_coordinates,
											station_red_25_coordinates,
											station_red_26_coordinates,
											station_red_27_coordinates,
											station_red_28_coordinates,
											station_red_29_coordinates};
	
	public final static String[] station_list_express_rashidiya = {"true", // Rashidiya
        "true", 	// Emirates
        "true", 	// Airport Terminal 3
        "true", 	// Airport Terminal 1
        "false",  	// GGICO
        "true", 	// Deira City Centre
        "true", 	// Al Rigga
        "true", 	// Union
        "true", 	// BurJuman
        "true", 	// Al Karama
        "false",  	// Al Jafiliya
        "true", 	// World Trade Centre
        "false", 	// Emirates Towers
        "false", 	// Financial Centre
        "false",  	// Burj Khalifa/Dubai Mall
        "false",  	// Business Bay
        "true", 	// Noor Bank
        "false",  	// First Gulf Bank
        "false",  	// Mall of the Emirates
        "false",  	// Sharaf DG
        "false",  	// Dubai Internet City
        "false",  	// Nakheel
        "false",  	// Dubai Marina
        "false",  	// Jumeirah Lakes Towers
        "false",  	// Nakheel Harbour & Tower
        "true", 	// Ibn Battuta
        "false",  	// Energy
        "false",  	// Danube
        "true"}; 	// Jebel Ali
	
	public final static String[] station_list_express_jebel_ali = {"true", // Rashidiya
        "true", 	// Emirates
        "false", 	// Airport Terminal 3
        "false", 	// Airport Terminal 1
        "false",  	// GGICO
        "false", 	// Deira City Centre
        "true", 	// Al Rigga
        "true", 	// Union
        "true", 	// BurJuman
        "true", 	// Al Karama
        "true",  	// Al Jafiliya
        "false", 	// World Trade Centre
        "true",  	// Emirates Towers
        "false",  	// Financial Centre
        "false",  	// Burj Khalifa/Dubai Mall
        "false",  	// Business Bay
        "true", 	// Noor Bank
        "true",  	// First Gulf Bank
        "false",  	// Mall of the Emirates
        "false",  	// Sharaf DG
        "true",  	// Dubai Internet City
        "false",  	// Nakheel
        "false",  	// Dubai Marina
        "false",  	// Jumeirah Lakes Towers
        "false",  	// Nakheel Harbour & Tower
        "true", 	// Ibn Battuta
        "false",  	// Energy
        "false",  	// Danube
        "true"}; 	// Jebel Ali
	
	public final static String[] station_list_red_numbers = {"11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "29",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42"};
	
	public final static String[] station_list_red_names_en = {"Rashidiya",
		"Emirates",
		"Airport Terminal 3",
		"Airport Terminal 1",
		"GGICO",
		"Deira City Centre",
		"Al Rigga",
		"Union",
		"BurJuman",
		"Al Karama",
		"Al Jafiliya",
		"World Trade Centre",
		"Emirates Towers",
		"Financial Centre",
		"Burj Khalifa/Dubai Mall",
		"Business Bay",
		"Noor Bank",
		"First Gulf Bank",
		"Mall of the Emirates",
		"Sharaf DG",
		"Dubai Internet City",
		"Nakheel",
		"Dubai Marina",
		"Jumeirah Lakes Towers",
		"Nakheel Harbour & Tower",
		"Ibn Battuta",
		"Energy",
		"Danube",
		"Jebel Ali"};
	
	public final static String[] station_list_red_names_ar = {"الراشدية",
		"طيران الإمارات",
		"المبنى رقم ٣",
		"المبنى رقم ١",
		"جي جي كو",
		"ديرة سيتي سنتر",
		"الرقة",
		"الاتحاد",
		"برجمان",
		"الكرامة",
		"الجافلية",
		"المركز التجاري العالمي",
		"أبراج الإمارات",
		"المركز المالي",
		"برج خليفة/دبي مول",
		"الخليج التجاري",
		"بنك نور",
		"بنك الخليج الأول",
		"مول الإمارات",
		"شرف دي جي",
		"مدينة دبي للإنترنت",
		"نخيل",
		"مرسى دبي",
		"أبراج بحيرات جميرا",
		"نخيل هاربر اند تاور",
		"ابن بطوطة",
		"الطاقة",
		"دانوب",
		"جبل علي"};
	
	// Time difference between stations.
	public final static String[] station_time_difference_red = {"0", // Rashidiya
		"4", // Emirates
		"2", // Airport Terminal 3
		"2", // Airport Terminal 1
		"2", // GGICO
		"2", // Deira City Centre
		"2", // Al Rigga
		"3", // Union
		"2", // BurJuman
		"2", // Al Karama
		"2", // Al Jafiliya
		"2", // World Trade Centre
		"2", // Emirates Towers
		"1", // Financial Centre
		"3", // Burj Khalifa/Dubai Mall
		"2", // Business Bay
		"5", // Noor Bank
		"3", // First Gulf Bank
		"2", // Mall of the Emirates
		"2", // Sharaf DG
		"3", // Dubai Internet City
		"3", // Nakheel
		"2", // Dubai Marina
		"2", // Jumeirah Lakes Towers
		"2", // Nakheel Harbour & Tower
		"3", // Ibn Battuta
		"3", // Energy
		"2", // Danube
		"2"}; // Jebel Ali
	
	static List<String> station_timings_rashidiya_week_days_rashidiya = Arrays.asList("06:27:00", "06:37:00");
	static List<String> station_timings_rashidiya_week_days_emirates = Arrays.asList("06:23:00", "06:33:00");
	static List<String> station_timings_rashidiya_week_days_airport_terminal_3 = Arrays.asList("06:22:00", "06:32:00");
	static List<String> station_timings_rashidiya_week_days_airport_terminal_1 = Arrays.asList("06:20:00", "06:30:00");
	static List<String> station_timings_rashidiya_week_days_ggico = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_deira_city_centre = Arrays.asList("06:16:00", "06:26:00");
	static List<String> station_timings_rashidiya_week_days_al_rigga = Arrays.asList("06:14:00", "06:24:00");
	static List<String> station_timings_rashidiya_week_days_union = Arrays.asList("06:11:00", "06:21:00");
	static List<String> station_timings_rashidiya_week_days_burjuman = Arrays.asList("06:09:00",
        "06:19:00",
        //-- End of Express trains
        //-- Morning peak hours
        "06:28:00",
        "06:35:00",
        "06:42:00");
	static List<String> station_timings_rashidiya_week_days_al_karama = Arrays.asList("06:06:00", "06:16:00");
	static List<String> station_timings_rashidiya_week_days_al_jafiliya = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_world_trade_centre = Arrays.asList("06:03:00", "06:13:00");
	static List<String> station_timings_rashidiya_week_days_emirates_towers = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_financial_centre = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_burj_khalifa = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_business_bay = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_noor_bank = Arrays.asList("05:53:00",
        "06:03:00",
        //-- End of Express trains
        "06:21:00",
        //-- Morning peak hours
        "06:27:00",
        "06:33:00",
        "06:39:00",
        "06:45:00",
        "06:51:00",
        "06:57:00",
        "07:03:00",
        "07:09:00",
        "07:15:00",
        "07:18:00",
        "07:21:00",
        "07:24:00",
        "07:27:00",
        "07:30:00",
        "07:33:00",
        "07:36:00",
        "07:39:00",
        "07:42:00",
        "07:45:00",
        "07:48:00",
        "07:51:00",
        "07:57:00",
        "08:03:00",
        "08:09:00",
        "08:15:00",
        "08:21:00",
        "08:27:00",
        "08:33:00",
        "08:39:00",
        "08:45:00",
        "08:48:00",
        "08:51:00",
        "08:57:00",
        "09:03:00",
        "09:06:00",
        "09:09:00",
        "09:15:00",
        "09:21:00",
        "09:27:00",
        "09:30:00",
        "09:33:00",
        //--Off-peak
        "09:42:00",
        "09:45:00",
        "09:50:00",
        "09:54:00",
        "10:06:00",
        "10:09:00",
        "10:14:00",
        "10:18:00",
        "10:22:00",
        "10:30:00",
        "10:33:00",
        "10:38:00",
        "10:46:00",
        "10:54:00",
        "11:02:20",
        "11:10:00",
        "11:18:00",
        "11:26:00",
        "11:34:00",
        "11:42:00",
        "11:50:00",
        "11:58:00",
        "12:06:00",
        "12:14:00",
        "12:22:00",
        "12:30:00",
        "12:38:00",
        "12:46:00",
        "12:54:00",
        "13:02:00",
        "13:10:00",
        "13:18:00",
        "13:26:00",
        "13:34:00",
        "13:42:00",
        "13:50:00",
        "13:58:00",
        "14:06:00",
        "14:14:00",
        "14:22:00",
        "14:30:00",
        "14:38:00",
        "14:46:00",
        "14:54:00",
        "15:02:00",
        "15:10:00",
        "15:18:00",
        "15:26:00",
        "15:34:00",
        "15:42:00",
        "15:50:00",
        "15:58:00",
        "16:06:00",
        "16:14:00",
        "16:22:00",
        "16:30:00",
        "16:38:00",
        "16:46:00",
        "16:54:00",
        "17:02:00",
        "17:05:00",
        "17:08:00",
        "17:11:00",
        "17:14:00",
        "17:17:00",
        "17:20:00",
        "17:23:00",
        "17:26:00",
        "17:29:00",
        "17:32:00",
        "17:35:00",
        "17:38:00",
        "17:41:00",
        "17:44:00",
        "17:47:30",
        "17:50:00",
        "17:53:00",
        "17:56:00",
        "17:59:00",
        "18:02:00",
        //-- Evening peak hours
        "18:05:30",
        "18:08:30",
        "18:11:30",
        "18:14:30",
        "18:17:30",
        "18:20:30",
        "18:23:30",
        "18:26:30",
        "18:29:30",
        "18:32:30",
        "18:35:30",
        "18:38:30",
        "18:42:50",
        "18:44:30",
        "18:47:30",
        "18:50:30",
        "18:53:50",
        "18:56:30",
        "18:59:30",
        "19:02:30",
        "19:05:30",
        "19:08:30",
        "19:11:30",
        "19:14:30",
        "19:17:30",
        "19:20:30",
        "19:23:00",
        "19:26:30",
        "19:29:00",
        "19:34:30",
        "19:38:00",
        "19:42:30",
        "19:47:00",
        "19:51:30",
        "19:53:00",
        "19:58:30",
        "20:02:00",
        "20:06:30",
        "20:11:00",
        "20:14:30",
        "20:17:00",
        "20:22:00",
        "20:30:00",
        //--Off-peak
        "20:38:00",
        "20:46:00",
        "20:54:00",
        "21:02:00",
        "21:10:00",
        "21:18:00",
        "21:26:00",
        "21:34:00",
        "21:42:00",
        "21:50:00",
        "21:58:00",
        "22:06:00",
        "22:14:00",
        "22:22:00",
        "22:30:00",
        "22:40:00",
        "22:50:00",
        "23:00:00",
        "23:10:00",
        "23:20:00",
        "23:30:00",
        "23:40:00",
        "23:50:00",
        "00:00:00",
        "00:10:00",
        "00:20:00",
        "00:30:00");
	static List<String> station_timings_rashidiya_week_days_first_gulf_bank = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_mall_of_the_emirates = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_sharaf_dg = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_dubai_internet_city = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_nakheel = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_dubai_marina = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_jumeirah_lakes_towers = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_nakheel_harbour = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_ibn_battuta = Arrays.asList("05:38:00", "05:48:00");
	static List<String> station_timings_rashidiya_week_days_energy = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_danube = new ArrayList<String>();
	static List<String> station_timings_rashidiya_week_days_jebel_ali = Arrays.asList("05:30:00", "05:40:00");
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_rashidiya_week_days = Arrays.asList(station_timings_rashidiya_week_days_rashidiya,
		station_timings_rashidiya_week_days_emirates,
		station_timings_rashidiya_week_days_airport_terminal_3,
		station_timings_rashidiya_week_days_airport_terminal_1,
		station_timings_rashidiya_week_days_ggico,
		station_timings_rashidiya_week_days_deira_city_centre,
		station_timings_rashidiya_week_days_al_rigga,
		station_timings_rashidiya_week_days_union,
		station_timings_rashidiya_week_days_burjuman,
		station_timings_rashidiya_week_days_al_karama,
		station_timings_rashidiya_week_days_al_jafiliya,
		station_timings_rashidiya_week_days_world_trade_centre,
		station_timings_rashidiya_week_days_emirates_towers,
		station_timings_rashidiya_week_days_financial_centre,
		station_timings_rashidiya_week_days_burj_khalifa,
		station_timings_rashidiya_week_days_business_bay,
		station_timings_rashidiya_week_days_noor_bank,
		station_timings_rashidiya_week_days_first_gulf_bank,
		station_timings_rashidiya_week_days_mall_of_the_emirates,
		station_timings_rashidiya_week_days_sharaf_dg,
		station_timings_rashidiya_week_days_dubai_internet_city,
		station_timings_rashidiya_week_days_nakheel,
		station_timings_rashidiya_week_days_dubai_marina,
		station_timings_rashidiya_week_days_jumeirah_lakes_towers,
		station_timings_rashidiya_week_days_nakheel_harbour,
		station_timings_rashidiya_week_days_ibn_battuta,
		station_timings_rashidiya_week_days_energy,
		station_timings_rashidiya_week_days_danube,
		station_timings_rashidiya_week_days_jebel_ali);
	
	static List<String> station_timings_rashidiya_friday_rashidiya = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_emirates = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_airport_terminal_3 = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_airport_terminal_1 = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_ggico = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_deira_city_centre = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_al_rigga = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_union = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_burjuman = Arrays.asList("13:26:00", "13:34:00", "13:42:00");
	static List<String> station_timings_rashidiya_friday_al_karama = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_al_jafiliya = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_world_trade_centre = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_emirates_towers = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_financial_centre = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_burj_khalifa = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_business_bay = new ArrayList<String>();
	static List<String> station_timings_rashidiya_friday_noor_bank = Arrays.asList("13:30:00",
		"13:38:00",
		"13:46:00",
		"13:54:00",
		"14:02:00",
		"14:10:00",
		"14:18:00",
		"14:26:00",
		"14:34:00",
		"14:42:00",
		"14:50:00",
		"14:58:00",
		"15:06:00",
		"15:14:00",
		"15:22:00",
		"15:30:00",
		"15:38:00",
		"15:46:00",
		"15:54:00",
		"16:02:00",
		"16:10:00",
		"16:18:00",
		"16:26:00",
		"16:34:00",
		"16:42:00",
		"16:50:00",
		"16:58:00",
		"17:06:00",
		"17:14:00",
		"17:22:00",
		"17:30:00",
		"17:36:00",
		"17:42:00",
		"17:48:00",
		"17:54:00",
		"18:00:00",
		"18:06:00",
		"18:12:00",
		"18:18:00",
		"18:24:00",
		"18:30:00",
		"18:36:00",
		"18:42:00",
		"18:48:00",
		"18:54:00",
		"19:00:00",
		"19:06:00",
		"19:12:00",
		"19:18:00",
		"19:24:00",
		"19:30:00",
		"19:36:00",
		"19:42:00",
		"19:48:00",
		"19:54:00",
		"20:00:00",
		"20:06:00",
		"20:12:00",
		"20:18:00",
		"20:24:00",
		"20:30:00",
		"20:36:00",
		"20:42:00",
		"20:48:00",
		"20:54:00",
		"21:00:00",
		"21:06:00",
		"21:14:00",
		"21:22:00",
		"21:30:00",
		"21:38:00",
		"21:46:00",
		"21:54:00",
		"22:02:00",
		"22:10:00",
		"22:18:00",
		"22:26:00",
		"22:34:00",
		"22:42:00",
		"22:50:00",
		"22:58:00",
		"23:06:00",
		"23:14:00",
		"23:22:00",
		"23:30:00",
		"23:38:00",
		"23:46:00",
		"23:54:00",
		"00:02:00",
		"00:10:00",
		"00:18:00",
		"00:26:00");
		static List<String> station_timings_rashidiya_friday_first_gulf_bank = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_mall_of_the_emirates = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_sharaf_dg = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_dubai_internet_city = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_nakheel = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_dubai_marina = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_jumeirah_lakes_towers = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_nakheel_harbour = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_ibn_battuta = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_energy = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_danube = new ArrayList<String>();
		static List<String> station_timings_rashidiya_friday_jebel_ali = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_rashidiya_friday = Arrays.asList(station_timings_rashidiya_friday_rashidiya,
		station_timings_rashidiya_friday_emirates,
		station_timings_rashidiya_friday_airport_terminal_3,
		station_timings_rashidiya_friday_airport_terminal_1,
		station_timings_rashidiya_friday_ggico,
		station_timings_rashidiya_friday_deira_city_centre,
		station_timings_rashidiya_friday_al_rigga,
		station_timings_rashidiya_friday_union,
		station_timings_rashidiya_friday_burjuman,
		station_timings_rashidiya_friday_al_karama,
		station_timings_rashidiya_friday_al_jafiliya,
		station_timings_rashidiya_friday_world_trade_centre,
		station_timings_rashidiya_friday_emirates_towers,
		station_timings_rashidiya_friday_financial_centre,
		station_timings_rashidiya_friday_burj_khalifa,
		station_timings_rashidiya_friday_business_bay,
		station_timings_rashidiya_friday_noor_bank,
		station_timings_rashidiya_friday_first_gulf_bank,
		station_timings_rashidiya_friday_mall_of_the_emirates,
		station_timings_rashidiya_friday_sharaf_dg,
		station_timings_rashidiya_friday_dubai_internet_city,
		station_timings_rashidiya_friday_nakheel,
		station_timings_rashidiya_friday_dubai_marina,
		station_timings_rashidiya_friday_jumeirah_lakes_towers,
		station_timings_rashidiya_friday_nakheel_harbour,
		station_timings_rashidiya_friday_ibn_battuta,
		station_timings_rashidiya_friday_energy,
		station_timings_rashidiya_friday_danube,
		station_timings_rashidiya_friday_jebel_ali);
	
	static List<String> station_timings_rashidiya_saturday_rashidiya = Arrays.asList("06:27:00", "06:37:00");
	static List<String> station_timings_rashidiya_saturday_emirates = Arrays.asList("06:23:00", "06:33:00");
	static List<String> station_timings_rashidiya_saturday_airport_terminal_3 = Arrays.asList("06:22:00", "06:32:00");
	static List<String> station_timings_rashidiya_saturday_airport_terminal_1 = Arrays.asList("06:20:00", "06:30:00");
	static List<String> station_timings_rashidiya_saturday_ggico = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_deira_city_centre = Arrays.asList("06:16:00", "06:26:00");
	static List<String> station_timings_rashidiya_saturday_al_rigga = Arrays.asList("06:14:00", "06:24:00");
	static List<String> station_timings_rashidiya_saturday_union = Arrays.asList("06:11:00", "06:21:00");
	static List<String> station_timings_rashidiya_saturday_burjuman = Arrays.asList("06:09:00",
        "06:19:00",
        //-- End of Express trains
        //-- Morning peak hours
        "06:24:00",
        "06:32:00",
        "06:40:00");
	static List<String> station_timings_rashidiya_saturday_al_karama = Arrays.asList("06:06:00", "06:16:00");
	static List<String> station_timings_rashidiya_saturday_al_jafiliya = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_world_trade_centre = Arrays.asList("06:03:00", "06:13:00");
	static List<String> station_timings_rashidiya_saturday_emirates_towers = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_financial_centre = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_burj_khalifa = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_business_bay = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_noor_bank = Arrays.asList("05:53:00",
		"06:03:00",
		//-- End of Express trains
		"06:20:00",
		"06:28:00",
		"06:36:00",
		"06:44:00",
		"06:52:00",
		"07:00:00",
		"07:08:05",
		"07:16:19",
		"07:24:00",
		"07:32:00",
		"07:40:00",
		"07:48:30",
		"07:56:00",
		"08:04:00",
		"08:12:00",
		"08:20:00",
		"08:28:00",
		"08:36:00",
		"08:44:00",
		"08:52:00",
		"09:00:00",
		"09:08:05",
		"09:16:19",
		"09:24:00",
		"09:32:00",
		"09:40:00",
		"09:48:00",
		"09:56:00",
		"10:04:00",
		"10:12:00",
		"10:20:00",
		"10:28:00",
		"10:36:00",
		"10:44:00",
		"10:52:00",
		"11:00:00",
		"11:08:05",
		"11:16:19",
		"11:24:00",
		"11:32:00",
		"11:40:00",
		"11:48:00",
		"11:56:00",
		"12:04:00",
		"12:12:00",
		"12:20:00",
		"12:28:00",
		"12:36:00",
		"12:44:00",
		"12:52:00",
		"13:00:00",
		"13:08:05",
		"13:16:19",
		"13:24:00",
		"13:32:00",
		"13:40:00",
		"13:48:00",
		"13:56:00",
		"14:04:00",
		"14:12:00",
		"14:20:00",
		"14:28:00",
		"14:36:00",
		"14:44:00",
		"14:52:00",
		"14:00:00",
		"14:08:05",
		"14:16:19",
		"14:24:00",
		"14:32:00",
		"14:40:00",
		"14:48:00",
		"14:56:00",
		"15:04:00",
		"15:12:00",
		"15:20:00",
		"15:28:00",
		"15:36:00",
		"15:44:00",
		"15:52:00",
		"16:00:00",
		"16:08:05",
		"16:16:19",
		"16:24:00",
		"16:32:00",
		"16:40:00",
		"16:48:00",
		"16:56:00",
		"17:04:00",
		"17:12:00",
		"17:20:00",
		"17:28:00",
		"17:36:00",
		"17:44:00",
		"17:52:00",
		//-- Evening peak hours
		"17:58:30",
		"18:04:30",
		"18:10:30",
		"18:16:30",
		"18:22:30",
		"18:28:30",
		"18:34:30",
		"18:40:30",
		"18:46:30",
		"18:52:30",
		"18:58:15",
		"19:04:20",
		"19:06:30",
		"19:12:30",
		"19:18:30",
		"19:24:30",
		"19:30:30",
		"19:36:30",
		"19:42:30",
		"19:47:30",
		"19:54:30",
		"19:57:30",
		"20:00:30",
		//--Off-peak
		"20:08:05",
		"20:16:19",
		"20:22:00",
		"20:28:00",
		"20:36:00",
		"20:44:00",
		"20:52:00",
		"21:00:00",
		"21:08:00",
		"21:16:00",
		"21:24:00",
		"21:32:00",
		"21:40:00",
		"21:48:00",
		"21:56:00",
		"22:04:05",
		"22:12:19",
		"22:20:00",
		"22:28:00",
		"22:40:00",
		"22:36:00",
		"22:44:00",
		"22:52:00",
		"23:00:00",
		"23:08:00",
		"23:16:00",
		"23:24:00");
	static List<String> station_timings_rashidiya_saturday_first_gulf_bank = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_mall_of_the_emirates = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_sharaf_dg = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_dubai_internet_city = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_nakheel = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_dubai_marina = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_jumeirah_lakes_towers = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_nakheel_harbour = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_ibn_battuta = Arrays.asList("05:38:00", "05:48:00");
	static List<String> station_timings_rashidiya_saturday_energy = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_danube = new ArrayList<String>();
	static List<String> station_timings_rashidiya_saturday_jebel_ali = Arrays.asList("05:30:00", "05:40:00");
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_rashidiya_saturday = Arrays.asList(station_timings_rashidiya_saturday_rashidiya,
		station_timings_rashidiya_saturday_emirates,
		station_timings_rashidiya_saturday_airport_terminal_3,
		station_timings_rashidiya_saturday_airport_terminal_1,
		station_timings_rashidiya_saturday_ggico,
		station_timings_rashidiya_saturday_deira_city_centre,
		station_timings_rashidiya_saturday_al_rigga,
		station_timings_rashidiya_saturday_union,
		station_timings_rashidiya_saturday_burjuman,
		station_timings_rashidiya_saturday_al_karama,
		station_timings_rashidiya_saturday_al_jafiliya,
		station_timings_rashidiya_saturday_world_trade_centre,
		station_timings_rashidiya_saturday_emirates_towers,
		station_timings_rashidiya_saturday_financial_centre,
		station_timings_rashidiya_saturday_burj_khalifa,
		station_timings_rashidiya_saturday_business_bay,
		station_timings_rashidiya_saturday_noor_bank,
		station_timings_rashidiya_saturday_first_gulf_bank,
		station_timings_rashidiya_saturday_mall_of_the_emirates,
		station_timings_rashidiya_saturday_sharaf_dg,
		station_timings_rashidiya_saturday_dubai_internet_city,
		station_timings_rashidiya_saturday_nakheel,
		station_timings_rashidiya_saturday_dubai_marina,
		station_timings_rashidiya_saturday_jumeirah_lakes_towers,
		station_timings_rashidiya_saturday_nakheel_harbour,
		station_timings_rashidiya_saturday_ibn_battuta,
		station_timings_rashidiya_saturday_energy,
		station_timings_rashidiya_saturday_danube,
		station_timings_rashidiya_saturday_jebel_ali);
	
	static List<String> station_timings_jebel_ali_week_days_rashidiya = Arrays.asList("05:30:00", "06:40:00");
	static List<String> station_timings_jebel_ali_week_days_emirates = Arrays.asList("05:34:00", "05:44:00");
	static List<String> station_timings_jebel_ali_week_days_airport_terminal_3 = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_airport_terminal_1 = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_ggico = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_deira_city_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_al_rigga = Arrays.asList("05:39:00", "05:49:00");
	static List<String> station_timings_jebel_ali_week_days_union = Arrays.asList("05:42:00", "05:52:00");
	static List<String> station_timings_jebel_ali_week_days_burjuman = Arrays.asList("05:45:00", "05:55:00");
	static List<String> station_timings_jebel_ali_week_days_al_karama = Arrays.asList("05:48:00", "05:58:00");
	static List<String> station_timings_jebel_ali_week_days_al_jafiliya = Arrays.asList("05:51:00", "06:01:00");
	static List<String> station_timings_jebel_ali_week_days_world_trade_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_emirates_towers = Arrays.asList("05:53:00", "06:03:00");
	static List<String> station_timings_jebel_ali_week_days_financial_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_burj_khalifa = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_business_bay = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_noor_bank = Arrays.asList("06:02:20",
        "06:12:20",
        //-- End of Express trains
        "06:27:20",
        //-- Morning peak hours
        "06:33:20",
        "06:39:20",
        "06:45:20",
        "06:51:20",
        "06:57:20",
        "07:03:20",
        "07:09:20",
        "07:12:20",
        "07:15:20",
        "07:18:20",
        "07:21:20",
        "07:24:20",
        "07:27:20",
        "07:30:20",
        "07:33:20",
        "07:36:20",
        "07:39:20",
        "07:42:20",
        "07:45:20",
        "07:48:20",
        "07:51:20",
        "07:54:20",
        "07:57:20",
        "08:00:20",
        "08:03:20",
        "08:06:20",
        "08:09:20",
        "08:12:20",
        "08:15:50",
        "08:18:20",
        "08:21:20",
        "08:24:20",
        "08:27:20",
        "08:30:20",
        "08:33:20",
        "08:36:20",
        "08:39:20",
        "08:43:20",
        "08:46:20",
        "08:49:20",
        "08:52:20",
        "08:55:20",
        "08:58:20",
        "09:01:20",
        "09:04:20",
        "09:07:20",
        "09:10:20",
        "09:13:20",
        "09:16:20",
        "09:19:20",
        "09:22:20",
        "09:25:20",
        "09:28:20",
        "09:31:20",
        "09:34:20",
        "09:40:20",
        "09:49:20",
        "09:58:20",
        "10:05:12",
        //--Off-peak
        "10:13:20",
        "10:17:12",
        "10:24:55",
        "10:32:55",
        "10:40:55",
        "10:48:55",
        "10:56:55",
        "11:04:55",
        "11:12:55",
        "11:20:55",
        "11:28:55",
        "11:36:55",
        "11:44:55",
        "11:52:55",
        "12:00:55",
        "12:08:55",
        "12:16:55",
        "12:24:55",
        "12:32:55",
        "12:40:55",
        "12:48:55",
        "12:56:55",
        "13:04:55",
        "13:12:55",
        "13:20:55",
        "13:28:55",
        "13:36:55",
        "13:44:55",
        "13:52:55",
        "14:00:55",
        "14:08:55",
        "14:16:55",
        "14:24:55",
        "14:32:55",
        "14:40:55",
        "14:48:55",
        "14:56:55",
        "15:04:55",
        "15:12:55",
        "15:20:55",
        "15:28:55",
        "15:36:55",
        "15:44:55",
        "15:52:55",
        "16:00:55",
        //-- Evening peak hours
        "16:04:00",
        "16:07:00",
        "16:10:00",
        "16:13:00",
        "16:16:00",
        "16:19:00",
        "16:22:00",
        "16:25:00",
        "16:28:00",
        "16:31:00",
        "16:34:00",
        "16:37:00",
        "16:40:00",
        "16:43:00",
        "16:46:00",
        "16:49:00",
        "16:52:00",
        "16:55:00",
        "16:57:00",
        "17:05:00",
        "17:09:00",
        "17:14:00",
        "17:19:00",
        "17:22:00",
        "17:25:00",
        "17:28:00",
        "17:31:00",
        "17:34:00",
        "17:37:00",
        "17:41:00",
        "17:46:00",
        "17:50:00",
        "17:55:00",
        "18:00:00",
        "18:05:00",
        "18:10:00",
        "18:15:00",
        "18:20:00",
        "18:25:00",
        "18:30:00",
        "18:35:00",
        "18:40:00",
        "18:45:00",
        "18:50:00",
        "18:55:00",
        "19:00:00",
        "19:06:55",
        "19:10:55",
        "19:14:55",
        "19:19:00",
        "19:23:00",
        "19:25:00",
        "19:33:00",
        "19:36:00",
        "19:40:00",
        "19:42:55",
        "19:48:55",
        "19:56:55",
        "19:59:55",
        "20:04:55",
        "20:09:55",
        "20:12:55",
        "20:18:55",
        "20:21:00",
        "20:29:00",
        "20:33:00",
        "20:37:00",
        "20:42:00",
        "20:45:00",
        "20:53:00",
        //--Off-peak
        "21:01:20",
        "21:09:20",
        "21:17:20",
        "21:25:20",
        "21:33:20",
        "21:41:20",
        "21:49:20",
        "22:57:20",
        "22:05:20",
        "22:13:20",
        "22:21:20",
        "22:29:20",
        "22:39:20",
        "22:49:20",
        "22:59:20",
        "23:09:20",
        "23:19:20",
        "23:29:20",
        "23:39:20",
        "23:49:20",
        "23:59:20",
        "00:09:20",
        "00:19:20",
        "00:29:20",
        "00:39:20");
	static List<String> station_timings_jebel_ali_week_days_first_gulf_bank = Arrays.asList("06:06:00", "06:16:00");
	static List<String> station_timings_jebel_ali_week_days_mall_of_the_emirates = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_sharaf_dg = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_dubai_internet_city = Arrays.asList("06:10:00", "06:20:00");
	static List<String> station_timings_jebel_ali_week_days_nakheel = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_dubai_marina = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_jumeirah_lakes_towers = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_nakheel_harbour = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_ibn_battuta = Arrays.asList("06:21:00", "06:31:00");
	static List<String> station_timings_jebel_ali_week_days_energy = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_danube = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_week_days_jebel_ali = Arrays.asList("06:29:00", "06:39:00");
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_jebel_ali_week_days = Arrays.asList(station_timings_jebel_ali_week_days_rashidiya,
		station_timings_jebel_ali_week_days_emirates,
		station_timings_jebel_ali_week_days_airport_terminal_3,
		station_timings_jebel_ali_week_days_airport_terminal_1,
		station_timings_jebel_ali_week_days_ggico,
		station_timings_jebel_ali_week_days_deira_city_centre,
		station_timings_jebel_ali_week_days_al_rigga,
		station_timings_jebel_ali_week_days_union,
		station_timings_jebel_ali_week_days_burjuman,
		station_timings_jebel_ali_week_days_al_karama,
		station_timings_jebel_ali_week_days_al_jafiliya,
		station_timings_jebel_ali_week_days_world_trade_centre,
		station_timings_jebel_ali_week_days_emirates_towers,
		station_timings_jebel_ali_week_days_financial_centre,
		station_timings_jebel_ali_week_days_burj_khalifa,
		station_timings_jebel_ali_week_days_business_bay,
		station_timings_jebel_ali_week_days_noor_bank,
		station_timings_jebel_ali_week_days_first_gulf_bank,
		station_timings_jebel_ali_week_days_mall_of_the_emirates,
		station_timings_jebel_ali_week_days_sharaf_dg,
		station_timings_jebel_ali_week_days_dubai_internet_city,
		station_timings_jebel_ali_week_days_nakheel,
		station_timings_jebel_ali_week_days_dubai_marina,
		station_timings_jebel_ali_week_days_jumeirah_lakes_towers,
		station_timings_jebel_ali_week_days_nakheel_harbour,
		station_timings_jebel_ali_week_days_ibn_battuta,
		station_timings_jebel_ali_week_days_energy,
		station_timings_jebel_ali_week_days_danube,
		station_timings_jebel_ali_week_days_jebel_ali);
	
	static List<String> station_timings_jebel_ali_friday_rashidiya = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_emirates = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_airport_terminal_3 = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_airport_terminal_1 = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_ggico = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_deira_city_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_al_rigga = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_union = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_burjuman = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_al_karama = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_al_jafiliya = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_world_trade_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_emirates_towers = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_financial_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_burj_khalifa = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_business_bay = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_noor_bank = Arrays.asList("13:37:20",
		"13:44:55",
		"13:53:20",
		"14:01:20",
		"14:09:20",
		"14:17:20",
		"14:25:20",
		"14:33:20",
		"14:41:20",
		"14:49:20",
		"14:57:20",
		"15:05:20",
		"15:13:20",
		"15:21:20",
		"15:29:20",
		"15:37:20",
		"15:44:20",
		"15:53:20",
		"16:01:20",
		"16:09:20",
		"16:17:20",
		"16:25:20",
		"16:33:20",
		"16:41:20",
		"16:49:20",
		"16:57:20",
		"17:05:20",
		"17:13:20",
		"17:21:20",
		"17:29:20",
		"17:37:20",
		"17:44:20",
		"17:53:20",
		"18:01:20",
		"18:07:20",
		"18:13:20",
		"18:19:20",
		"18:25:20",
		"18:31:20",
		"18:37:20",
		"18:43:20",
		"18:49:20",
		"18:55:20",
		"19:01:20",
		"19:07:20",
		"19:13:20",
		"19:19:20",
		"19:25:20",
		"19:31:20",
		"19:37:20",
		"19:43:20",
		"19:49:20",
		"19:55:20",
		"20:01:20",
		"20:07:20",
		"20:13:20",
		"20:19:20",
		"20:25:20",
		"20:31:20",
		"20:37:20",
		"20:43:20",
		"20:49:20",
		"20:55:20",
		"21:01:20",
		"21:07:20",
		"21:13:20",
		"21:19:20",
		"21:25:20",
		"21:31:20",
		"21:37:20",
		"21:43:20",
		"21:49:20",
		"21:55:20",
		"22:01:20",
		"22:07:20",
		"22:13:20",
		"22:19:20",
		"22:25:20",
		"22:31:20",
		"22:37:20",
		"22:43:20",
		"22:49:20",
		"22:57:20",
		"23:05:20",
		"23:13:20",
		"23:21:20",
		"23:29:20",
		"23:37:20",
		"23:45:20",
		"23:53:20",
		"00:01:20",
		"00:09:20",
		"00:17:20",
		"00:25:20",
		"00:33:20");
	static List<String> station_timings_jebel_ali_friday_first_gulf_bank = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_mall_of_the_emirates = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_sharaf_dg = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_dubai_internet_city = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_nakheel = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_dubai_marina = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_jumeirah_lakes_towers = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_nakheel_harbour = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_ibn_battuta = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_energy = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_danube = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_friday_jebel_ali = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_jebel_ali_friday = Arrays.asList(station_timings_jebel_ali_friday_rashidiya,
		station_timings_jebel_ali_friday_emirates,
		station_timings_jebel_ali_friday_airport_terminal_3,
		station_timings_jebel_ali_friday_airport_terminal_1,
		station_timings_jebel_ali_friday_ggico,
		station_timings_jebel_ali_friday_deira_city_centre,
		station_timings_jebel_ali_friday_al_rigga,
		station_timings_jebel_ali_friday_union,
		station_timings_jebel_ali_friday_burjuman,
		station_timings_jebel_ali_friday_al_karama,
		station_timings_jebel_ali_friday_al_jafiliya,
		station_timings_jebel_ali_friday_world_trade_centre,
		station_timings_jebel_ali_friday_emirates_towers,
		station_timings_jebel_ali_friday_financial_centre,
		station_timings_jebel_ali_friday_burj_khalifa,
		station_timings_jebel_ali_friday_business_bay,
		station_timings_jebel_ali_friday_noor_bank,
		station_timings_jebel_ali_friday_first_gulf_bank,
		station_timings_jebel_ali_friday_mall_of_the_emirates,
		station_timings_jebel_ali_friday_sharaf_dg,
		station_timings_jebel_ali_friday_dubai_internet_city,
		station_timings_jebel_ali_friday_nakheel,
		station_timings_jebel_ali_friday_dubai_marina,
		station_timings_jebel_ali_friday_jumeirah_lakes_towers,
		station_timings_jebel_ali_friday_nakheel_harbour,
		station_timings_jebel_ali_friday_ibn_battuta,
		station_timings_jebel_ali_friday_energy,
		station_timings_jebel_ali_friday_danube,
		station_timings_jebel_ali_friday_jebel_ali);
	
	static List<String> station_timings_jebel_ali_saturday_rashidiya = Arrays.asList("05:30:00", "06:40:00");
	static List<String> station_timings_jebel_ali_saturday_emirates = Arrays.asList("05:34:00", "05:44:00");
	static List<String> station_timings_jebel_ali_saturday_airport_terminal_3 = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_airport_terminal_1 = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_ggico = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_deira_city_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_al_rigga = Arrays.asList("05:39:00", "05:49:00");
	static List<String> station_timings_jebel_ali_saturday_union = Arrays.asList("05:42:00", "05:52:00");
	static List<String> station_timings_jebel_ali_saturday_burjuman = Arrays.asList("05:45:00", "05:55:00");
	static List<String> station_timings_jebel_ali_saturday_al_karama = Arrays.asList("05:48:00", "05:58:00");
	static List<String> station_timings_jebel_ali_saturday_al_jafiliya = Arrays.asList("05:51:00", "06:01:00");
	static List<String> station_timings_jebel_ali_saturday_world_trade_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_emirates_towers = Arrays.asList("05:53:00", "06:03:00");
	static List<String> station_timings_jebel_ali_saturday_financial_centre = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_burj_khalifa = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_business_bay = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_noor_bank = Arrays.asList("06:01:00",
        "06:11:00",
        //-- End of Express trains
        "06:27:55",
        "06:34:55",
        "06:43:55",
        "06:51:00",
        "06:58:55",
        "07:06:55",
        "07:14:55",
        "07:22:55",
        "07:30:55",
        "07:39:00",
        "07:45:00",
        "07:50:55",
        "07:57:00",
        "08:03:00",
        "08:09:00",
        "08:15:00",
        "08:20:55",
        "08:26:55",
        "08:34:55",
        "08:43:55",
        "08:51:00",
        "08:58:55",
        "09:06:55",
        "09:14:55",
        "09:22:55",
        "09:30:55",
        "09:39:00",
        "09:47:00",
        "09:54:55",
        "10:03:00",
        "10:11:00",
        "10:19:00",
        "10:27:55",
        "10:34:55",
        "10:43:55",
        "10:51:00",
        "10:58:55",
        "11:06:55",
        "11:14:55",
        "11:22:55",
        "11:30:55",
        "11:39:00",
        "11:47:00",
        "11:54:55",
        "12:03:00",
        "12:11:00",
        "12:19:00",
        "12:27:55",
        "12:34:55",
        "12:43:55",
        "12:51:00",
        "12:58:55",
        "13:06:55",
        "13:14:55",
        "13:22:55",
        "13:30:55",
        "13:39:00",
        "13:47:00",
        "13:54:55",
        "14:03:00",
        "14:11:00",
        "14:19:00",
        "14:27:55",
        "14:34:55",
        "14:43:55",
        "14:51:00",
        "14:58:55",
        "15:06:55",
        "15:14:55",
        "15:22:55",
        "15:30:55",
        "15:39:00",
        "15:47:00",
        "15:54:55",
        "16:03:00",
        "16:11:00",
        "16:19:00",
        "16:27:55",
        "16:34:55",
        "16:43:55",
        "16:51:00",
        "16:58:55",
        "16:06:55",
        "16:14:55",
        "16:22:55",
        "16:30:55",
        "16:39:00",
        "16:47:00",
        "16:54:55",
        "17:03:00",
        "17:11:00",
        "17:19:00",
        "17:27:55",
        "17:34:55",
        "17:43:55",
        "17:51:00",
        "17:58:55",
        "18:06:55",
        //-- Evening peak hours
        "18:10:30",
        "18:16:30",
        "18:22:30",
        "18:28:30",
        "18:34:30",
        "18:40:30",
        "18:46:30",
        "18:52:30",
        "18:58:30",
        "19:04:30",
        "19:10:30",
        "19:16:30",
        "19:22:30",
        "19:28:30",
        "19:34:30",
        "19:40:30",
        "19:46:30",
        "19:52:30",
        "19:58:30",
        "20:04:30",
        "20:10:30",
        "20:16:30",
        "20:22:30",
        "20:28:30",
        "20:34:30",
        "20:40:30",
        "20:46:30",
        //--Off-peak
        "20:51:00",
        "20:59:00",
        "21:07:00",
        "21:15:00",
        "21:23:00",
        "21:31:00",
        "21:39:00",
        "21:47:00",
        "21:55:00",
        "22:03:00",
        "22:11:00",
        "22:19:00",
        "22:27:00",
        "22:35:00",
        "22:43:00",
        "22:51:00",
        "22:59:00",
        "23:07:00",
        "23:15:00",
        "23:23:00",
        "23:31:00");
	static List<String> station_timings_jebel_ali_saturday_first_gulf_bank = Arrays.asList("06:06:00", "06:16:00");
	static List<String> station_timings_jebel_ali_saturday_mall_of_the_emirates = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_sharaf_dg = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_dubai_internet_city = Arrays.asList("06:10:00", "06:20:00");
	static List<String> station_timings_jebel_ali_saturday_nakheel = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_dubai_marina = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_jumeirah_lakes_towers = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_nakheel_harbour = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_ibn_battuta = Arrays.asList("06:21:00", "06:31:00");
	static List<String> station_timings_jebel_ali_saturday_energy = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_danube = new ArrayList<String>();
	static List<String> station_timings_jebel_ali_saturday_jebel_ali = Arrays.asList("06:29:00", "06:39:00");
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_jebel_ali_saturday = Arrays.asList(station_timings_jebel_ali_saturday_rashidiya,
		station_timings_jebel_ali_saturday_emirates,
		station_timings_jebel_ali_saturday_airport_terminal_3,
		station_timings_jebel_ali_saturday_airport_terminal_1,
		station_timings_jebel_ali_saturday_ggico,
		station_timings_jebel_ali_saturday_deira_city_centre,
		station_timings_jebel_ali_saturday_al_rigga,
		station_timings_jebel_ali_saturday_union,
		station_timings_jebel_ali_saturday_burjuman,
		station_timings_jebel_ali_saturday_al_karama,
		station_timings_jebel_ali_saturday_al_jafiliya,
		station_timings_jebel_ali_saturday_world_trade_centre,
		station_timings_jebel_ali_saturday_emirates_towers,
		station_timings_jebel_ali_saturday_financial_centre,
		station_timings_jebel_ali_saturday_burj_khalifa,
		station_timings_jebel_ali_saturday_business_bay,
		station_timings_jebel_ali_saturday_noor_bank,
		station_timings_jebel_ali_saturday_first_gulf_bank,
		station_timings_jebel_ali_saturday_mall_of_the_emirates,
		station_timings_jebel_ali_saturday_sharaf_dg,
		station_timings_jebel_ali_saturday_dubai_internet_city,
		station_timings_jebel_ali_saturday_nakheel,
		station_timings_jebel_ali_saturday_dubai_marina,
		station_timings_jebel_ali_saturday_jumeirah_lakes_towers,
		station_timings_jebel_ali_saturday_nakheel_harbour,
		station_timings_jebel_ali_saturday_ibn_battuta,
		station_timings_jebel_ali_saturday_energy,
		station_timings_jebel_ali_saturday_danube,
		station_timings_jebel_ali_saturday_jebel_ali);
	
	static String[] station_green_1_coordinates = {"25.254816", "55.401033"};
	static String[] station_green_2_coordinates = {"25.26257", "55.387439"};
	static String[] station_green_3_coordinates = {"25.269822", "55.374994"};
	static String[] station_green_4_coordinates = {"25.273194", "55.369323"};
	static String[] station_green_5_coordinates = {"25.277851", "55.361454"};
	static String[] station_green_6_coordinates = {"25.277695", "55.352699"};
	static String[] station_green_7_coordinates = {"25.27527", "55.346267"};
	static String[] station_green_8_coordinates = {"25.270817", "55.332963"};
	static String[] station_green_9_coordinates = {"25.270332", "55.320572"};
	static String[] station_green_10_coordinates = {"25.266291", "55.31389"};
	static String[] station_green_11_coordinates = {"25.269218", "55.307732"};
	static String[] station_green_12_coordinates = {"25.276179", "55.301686"};
	static String[] station_green_13_coordinates = {"25.268738", "55.293988"};
	static String[] station_green_14_coordinates = {"25.265253", "55.289056"};
	static String[] station_green_15_coordinates = {"25.25816", "55.297475"};
	static String[] station_green_16_coordinates = {"25.254856", "55.304312"};
	static String[] station_green_17_coordinates = {"25.243643", "55.315942"};
	static String[] station_green_18_coordinates = {"25.230906", "55.322862"};
	
	public final static String[][] station_coordinates_green = {station_green_1_coordinates,
											station_green_2_coordinates,
											station_green_3_coordinates,
											station_green_4_coordinates,
											station_green_5_coordinates,
											station_green_6_coordinates,
											station_green_7_coordinates,
											station_green_8_coordinates,
											station_green_9_coordinates,
											station_green_10_coordinates,
											station_green_11_coordinates,
											station_green_12_coordinates,
											station_green_13_coordinates,
											station_green_14_coordinates,
											station_green_15_coordinates,
											station_green_16_coordinates,
											station_green_17_coordinates,
											station_green_18_coordinates};
	
	public final static String[] station_list_green_numbers = {"11",
		"12",
		"13",
		"14",
		"15",
		"16",
		"17",
		"18",
		"19",
		"20",
		"21",
		"22",
		"23",
		"24",
		"25",
		"26",
		"27",
		"28"};
	
	public final static String[] station_list_green_names_en = {"Etisalat",
		"Al Qusais",
		"Dubai Airport Free Zone",
		"Al Nahda",
		"Stadium",
		"Al Qiyadah",
		"Abu Hail",
		"Abu Baker Al Siddique",
		"Salah Al Din",
		"Union",
		"Baniyas Square",
		"Palm Deira",
		"Al Ras",
		"Al Ghubaiba",
		"Al Fahidi",
		"BurJuman",
		"Oud Metha",
		"Dubai Healthcare City"};
	
	public final static String[] station_list_green_names_ar = {"اتصالات",
		"القصيص",
		"المنطقة الحرة بمطار دبي",
		"النهدة",
		"الاستاد",
		"القيادة",
		"أبو هيل",
		"أبو بكر الصديق",
		"صلاح الدين",
		"الاتحاد",
		"بني ياس",
		"نخلة ديرة",
		"الراس",
		"الغبيبة",
		"الفهيدي",
		"برجمان",
		"عود ميثاء",
		"مدينة دبي الطبية"};
	
	public final static String[] station_time_difference_green = {"0", // Etisalat
		"3", // Al Qusais
		"2", // Dubai Airport Free Zone
		"1", // Al Nahda
		"2", // Stadium
		"2", // Al Qiyadah
		"2", // Abu Hail
		"2", // Abu Baker Al Siddique
		"2", // Salah Al Din
		"2", // Union
		"2", // Baniyas Square
		"2", // Palm Deira
		"2", // Al Ras
		"2", // Al Ghubaiba
		"2", // Al Fahidi
		"2", // Burjuman
		"2", // Oud Metha
		"4"}; // Dubai Healthcare City

	static List<String> station_timings_creek_week_days_etisalat = Arrays.asList("05:48:00",
		"05:56:00",
		"06:04:00",
		"06:12:00",
		"06:20:00",
		"06:28:00",
		"06:36:00",
		"06:44:00",
		"06:52:00",
		"07:00:00",
		"07:08:00",
		"07:16:00",
		"07:24:00",
		"07:32:00",
		"07:40:00",
		"07:48:00",
		"07:56:00",
		"08:04:00",
		"08:12:00",
		"08:20:00",
		"08:28:00",
		"08:36:00",
		"08:44:00",
		"08:52:00",
		"09:00:00",
		"09:08:00",
		"09:16:00",
		"09:24:00",
		"09:32:00",
		"09:40:00",
		"09:48:00",
		"09:56:00",
		"10:04:00",
		"10:12:00",
		"10:20:00",
		"10:28:00",
		"10:36:00",
		"10:44:00",
		"10:52:00",
		"11:00:00",
		"11:08:00",
		"11:16:00",
		"11:24:00",
		"11:32:00",
		"11:40:00",
		"11:48:00",
		"11:56:00",
		"12:04:00",
		"12:12:00",
		"12:20:00",
		"12:28:00",
		"12:36:00",
		"12:44:00",
		"12:52:00",
		"13:00:00",
		"13:08:00",
		"13:16:00",
		"13:24:00",
		"13:32:00",
		"13:40:00",
		"13:48:00",
		"13:56:00",
		"14:04:00",
		"14:12:00",
		"14:20:00",
		"14:28:00",
		"14:36:00",
		"14:44:00",
		"14:52:00",
		"15:00:00",
		"15:08:00",
		"15:16:00",
		"15:24:00",
		"15:32:00",
		"15:40:00",
		"15:48:00",
		"15:56:00",
		"16:04:00",
		"16:12:00",
		"16:20:00",
		"16:28:00",
		"16:36:00",
		"16:44:00",
		"16:52:00",
		"17:00:00",
		"17:08:00",
		"17:16:00",
		"17:24:00",
		"17:32:00",
		"17:40:00",
		"17:48:00",
		"17:56:00",
		"18:04:00",
		"18:12:00",
		"18:20:00",
		"18:28:00",
		"18:36:00",
		"18:44:00",
		"18:52:00",
		"19:00:00",
		"19:08:00",
		"19:16:00",
		"19:24:00",
		"19:32:00",
		"19:40:00",
		"19:48:00",
		"19:56:00",
		"20:04:00",
		"20:12:00",
		"20:20:00",
		"20:28:00",
		"20:36:00",
		"20:44:00",
		"20:52:00",
		"21:00:00",
		"21:08:00",
		"21:16:00",
		"21:24:00",
		"21:32:00",
		"21:40:00",
		"21:48:00",
		"21:56:00",
		"22:04:00",
		"22:12:00",
		"22:20:00",
		"22:28:00",
		"22:36:00",
		"22:44:00",
		"22:52:00",
		"23:00:00",
		"23:08:00",
		"23:16:00",
		"23:24:00",
		"23:32:00",
		"23:40:00",
		"23:48:00",
		"23:56:00",
		"00:04:00",
		"00:12:00",
		"00:20:00",
		"00:29:00");
	static List<String> station_timings_creek_week_days_al_qusais = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_dubai_airport_free_zone = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_al_nahda = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_stadium = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_al_qiyadah = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_abu_hail = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_abu_baker = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_salah_al_din = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_union = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_baniyas_square = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_palm_deira = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_al_ras = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_al_ghubaiba = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_al_fahidi = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_burjuman = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_oud_metha = new ArrayList<String>();
	static List<String> station_timings_creek_week_days_dubai_healthcare_city = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_creek_week_days = Arrays.asList(station_timings_creek_week_days_etisalat,
		station_timings_creek_week_days_al_qusais,
		station_timings_creek_week_days_dubai_airport_free_zone,
		station_timings_creek_week_days_al_nahda,
		station_timings_creek_week_days_stadium,
		station_timings_creek_week_days_al_qiyadah,
		station_timings_creek_week_days_abu_hail,
		station_timings_creek_week_days_abu_baker,
		station_timings_creek_week_days_salah_al_din,
		station_timings_creek_week_days_union,
		station_timings_creek_week_days_baniyas_square,
		station_timings_creek_week_days_palm_deira,
		station_timings_creek_week_days_al_ras,
		station_timings_creek_week_days_al_ghubaiba,
		station_timings_creek_week_days_al_fahidi,
		station_timings_creek_week_days_burjuman,
		station_timings_creek_week_days_oud_metha,
		station_timings_creek_week_days_dubai_healthcare_city);
	
	static List<String> station_timings_creek_friday_etisalat = Arrays.asList("12:59:00",
		"13:07:00",
		"13:15:00",
		"13:23:00",
		"13:31:00",
		"13:39:00",
		"13:47:00",
		"13:55:00",
		"14:03:00",
		"14:11:00",
		"14:19:00",
		"14:27:00",
		"14:35:00",
		"14:43:00",
		"14:51:00",
		"14:59:00",
		"15:07:00",
		"15:15:00",
		"15:23:00",
		"15:31:00",
		"15:39:00",
		"15:47:00",
		"15:55:00",
		"16:03:00",
		"16:11:00",
		"16:19:00",
		"16:27:00",
		"16:35:00",
		"16:43:00",
		"16:51:00",
		"16:59:00",
		"17:07:00",
		"17:15:00",
		"17:23:00",
		"17:31:00",
		"17:39:00",
		"17:47:00",
		"17:55:00",
		"18:03:00",
		"18:11:00",
		"18:19:00",
		"18:27:00",
		"18:35:00",
		"18:43:00",
		"18:51:00",
		"18:59:00",
		"19:07:00",
		"19:15:00",
		"19:23:00",
		"19:31:00",
		"19:39:00",
		"19:47:00",
		"19:55:00",
		"20:03:00",
		"20:11:00",
		"20:19:00",
		"20:27:00",
		"20:35:00",
		"20:43:00",
		"20:51:00",
		"20:59:00",
		"21:07:00",
		"21:15:00",
		"21:23:00",
		"21:31:00",
		"21:39:00",
		"21:47:00",
		"21:55:00",
		"22:03:00",
		"22:11:00",
		"22:19:00",
		"22:27:00",
		"22:35:00",
		"22:43:00",
		"22:51:00",
		"22:59:00",
		"23:07:00",
		"23:15:00",
		"23:23:00",
		"23:31:00",
		"23:39:00",
		"23:47:00",
		"23:55:00",
		"00:03:00",
		"00:11:00",
		"00:19:00",
		"00:27:00");
	static List<String> station_timings_creek_friday_al_qusais = new ArrayList<String>();
	static List<String> station_timings_creek_friday_dubai_airport_free_zone = new ArrayList<String>();
	static List<String> station_timings_creek_friday_al_nahda = new ArrayList<String>();
	static List<String> station_timings_creek_friday_stadium = new ArrayList<String>();
	static List<String> station_timings_creek_friday_al_qiyadah = new ArrayList<String>();
	static List<String> station_timings_creek_friday_abu_hail = new ArrayList<String>();
	static List<String> station_timings_creek_friday_abu_baker = new ArrayList<String>();
	static List<String> station_timings_creek_friday_salah_al_din = new ArrayList<String>();
	static List<String> station_timings_creek_friday_union = new ArrayList<String>();
	static List<String> station_timings_creek_friday_baniyas_square = new ArrayList<String>();
	static List<String> station_timings_creek_friday_palm_deira = new ArrayList<String>();
	static List<String> station_timings_creek_friday_al_ras = new ArrayList<String>();
	static List<String> station_timings_creek_friday_al_ghubaiba = new ArrayList<String>();
	static List<String> station_timings_creek_friday_al_fahidi = new ArrayList<String>();
	static List<String> station_timings_creek_friday_burjuman = new ArrayList<String>();
	static List<String> station_timings_creek_friday_oud_metha = new ArrayList<String>();
	static List<String> station_timings_creek_friday_dubai_healthcare_city = new ArrayList<String>();
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_creek_friday = Arrays.asList(station_timings_creek_friday_etisalat,
		station_timings_creek_friday_al_qusais,
		station_timings_creek_friday_dubai_airport_free_zone,
		station_timings_creek_friday_al_nahda,
		station_timings_creek_friday_stadium,
		station_timings_creek_friday_al_qiyadah,
		station_timings_creek_friday_abu_hail,
		station_timings_creek_friday_abu_baker,
		station_timings_creek_friday_salah_al_din,
		station_timings_creek_friday_union,
		station_timings_creek_friday_baniyas_square,
		station_timings_creek_friday_palm_deira,
		station_timings_creek_friday_al_ras,
		station_timings_creek_friday_al_ghubaiba,
		station_timings_creek_friday_al_fahidi,
		station_timings_creek_friday_burjuman,
		station_timings_creek_friday_oud_metha,
		station_timings_creek_friday_dubai_healthcare_city);
	
	static List<String> station_timings_etisalat_week_days_etisalat = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_al_qusais = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_dubai_airport_free_zone = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_al_nahda = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_stadium = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_al_qiyadah = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_abu_hail = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_abu_baker = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_salah_al_din = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_union = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_baniyas_square = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_palm_deira = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_al_ras = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_al_ghubaiba = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_al_fahidi = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_burjuman = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_oud_metha = new ArrayList<String>();
	static List<String> station_timings_etisalat_week_days_dubai_healthcare_city = Arrays.asList("05:55:00",
		"06:03:00",
		"06:11:00",
		"06:19:00",
		"06:27:00",
		"06:35:00",
		"06:43:00",
		"06:51:00",
		"06:59:00",
		"07:07:00",
		"07:15:00",
		"07:23:00",
		"07:31:00",
		"07:39:00",
		"07:47:00",
		"07:55:00",
		"08:03:00",
		"08:11:00",
		"08:19:00",
		"08:27:00",
		"08:35:00",
		"08:43:00",
		"08:51:00",
		"08:59:00",
		"09:07:00",
		"09:15:00",
		"09:23:00",
		"09:31:00",
		"09:39:00",
		"09:47:00",
		"09:55:00",
		"10:03:00",
		"10:11:00",
		"10:19:00",
		"10:27:00",
		"10:35:00",
		"10:43:00",
		"10:51:00",
		"10:59:00",
		"11:07:00",
		"11:15:00",
		"11:23:00",
		"11:31:00",
		"11:39:00",
		"11:47:00",
		"11:55:00",
		"12:03:00",
		"12:11:00",
		"12:19:00",
		"12:27:00",
		"12:34:00",
		"12:42:00",
		"12:50:00",
		"12:58:00",
		"13:06:00",
		"13:14:00",
		"13:22:00",
		"13:30:00",
		"13:38:00",
		"13:46:00",
		"13:54:00",
		"14:02:00",
		"14:10:00",
		"14:18:00",
		"14:26:00",
		"14:34:00",
		"14:42:00",
		"14:50:00",
		"14:58:00",
		"15:06:00",
		"15:14:00",
		"15:22:00",
		"15:30:00",
		"15:38:00",
		"15:46:00",
		"15:54:00",
		"16:02:00",
		"16:10:00",
		"16:18:00",
		"16:26:00",
		"16:34:00",
		"16:42:00",
		"16:50:00",
		"16:58:00",
		"17:06:00",
		"17:14:00",
		"17:22:00",
		"17:30:00",
		"17:38:00",
		"17:46:00",
		"17:54:00",
		"18:02:00",
		"18:10:00",
		"18:18:00",
		"18:26:00",
		"18:34:00",
		"18:42:00",
		"18:50:00",
		"18:58:00",
		"19:06:00",
		"19:14:00",
		"19:22:00",
		"19:30:00",
		"19:38:00",
		"19:46:00",
		"19:54:00",
		"20:02:00",
		"20:10:00",
		"20:18:00",
		"20:26:00",
		"20:34:00",
		"20:42:00",
		"20:50:00",
		"20:58:00",
		"21:06:00",
		"21:14:00",
		"21:22:00",
		"21:30:00",
		"21:38:00",
		"21:46:00",
		"21:54:00",
		"22:02:00",
		"22:10:00",
		"22:18:00",
		"22:26:00",
		"22:34:00",
		"22:42:00",
		"22:50:00",
		"22:58:00",
		"23:06:00",
		"23:14:00",
		"23:22:00",
		"23:30:00",
		"23:38:00",
		"23:46:00",
		"23:54:00",
		"00:02:00",
		"00:10:00",
		"00:18:00",
		"00:26:00");
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_etisalat_week_days = Arrays.asList(station_timings_etisalat_week_days_etisalat,
		station_timings_etisalat_week_days_al_qusais,
		station_timings_etisalat_week_days_dubai_airport_free_zone,
		station_timings_etisalat_week_days_al_nahda,
		station_timings_etisalat_week_days_stadium,
		station_timings_etisalat_week_days_al_qiyadah,
		station_timings_etisalat_week_days_abu_hail,
		station_timings_etisalat_week_days_abu_baker,
		station_timings_etisalat_week_days_salah_al_din,
		station_timings_etisalat_week_days_union,
		station_timings_etisalat_week_days_baniyas_square,
		station_timings_etisalat_week_days_palm_deira,
		station_timings_etisalat_week_days_al_ras,
		station_timings_etisalat_week_days_al_ghubaiba,
		station_timings_etisalat_week_days_al_fahidi,
		station_timings_etisalat_week_days_burjuman,
		station_timings_etisalat_week_days_oud_metha,
		station_timings_etisalat_week_days_dubai_healthcare_city);
	
	static List<String> station_timings_etisalat_friday_etisalat = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_al_qusais = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_dubai_airport_free_zone = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_al_nahda = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_stadium = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_al_qiyadah = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_abu_hail = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_abu_baker = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_salah_al_din = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_union = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_baniyas_square = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_palm_deira = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_al_ras = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_al_ghubaiba = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_al_fahidi = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_burjuman = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_oud_metha = new ArrayList<String>();
	static List<String> station_timings_etisalat_friday_dubai_healthcare_city = Arrays.asList("13:05:00",
		"13:13:00",
		"13:21:00",
		"13:29:00",
		"13:37:00",
		"13:45:00",
		"13:53:00",
		"14:01:00",
		"14:09:00",
		"14:17:00",
		"14:25:00",
		"14:33:00",
		"14:41:00",
		"14:49:00",
		"14:57:00",
		"15:05:00",
		"15:13:00",
		"15:21:00",
		"15:29:00",
		"15:37:00",
		"15:45:00",
		"15:53:00",
		"16:01:00",
		"16:09:00",
		"16:17:00",
		"16:25:00",
		"16:33:00",
		"16:41:00",
		"16:49:00",
		"16:57:00",
		"17:05:00",
		"17:13:00",
		"17:21:00",
		"17:29:00",
		"17:37:00",
		"17:45:00",
		"17:53:00",
		"18:01:00",
		"18:09:00",
		"18:17:00",
		"18:25:00",
		"18:33:00",
		"18:41:00",
		"18:49:00",
		"18:57:00",
		"19:05:00",
		"19:13:00",
		"19:21:00",
		"19:29:00",
		"19:37:00",
		"19:45:00",
		"19:53:00",
		"20:01:00",
		"20:09:00",
		"20:17:00",
		"20:25:00",
		"20:33:00",
		"20:41:00",
		"20:49:00",
		"20:57:00",
		"21:05:00",
		"21:13:00",
		"21:21:00",
		"21:29:00",
		"21:37:00",
		"21:45:00",
		"21:53:00",
		"22:01:00",
		"22:09:00",
		"22:17:00",
		"22:25:00",
		"22:33:00",
		"22:41:00",
		"22:49:00",
		"22:57:00",
		"23:05:00",
		"23:13:00",
		"23:21:00",
		"23:29:00",
		"23:37:00",
		"23:45:00",
		"23:53:00",
		"00:01:00",
		"00:09:00",
		"00:17:00",
		"00:25:00");
	
	@SuppressWarnings("unchecked")
	public final static List<List<String>> station_timings_etisalat_friday = Arrays.asList(station_timings_etisalat_friday_etisalat,
		station_timings_etisalat_friday_al_qusais,
		station_timings_etisalat_friday_dubai_airport_free_zone,
		station_timings_etisalat_friday_al_nahda,
		station_timings_etisalat_friday_stadium,
		station_timings_etisalat_friday_al_qiyadah,
		station_timings_etisalat_friday_abu_hail,
		station_timings_etisalat_friday_abu_baker,
		station_timings_etisalat_friday_salah_al_din,
		station_timings_etisalat_friday_union,
		station_timings_etisalat_friday_baniyas_square,
		station_timings_etisalat_friday_palm_deira,
		station_timings_etisalat_friday_al_ras,
		station_timings_etisalat_friday_al_ghubaiba,
		station_timings_etisalat_friday_al_fahidi,
		station_timings_etisalat_friday_burjuman,
		station_timings_etisalat_friday_oud_metha,
		station_timings_etisalat_friday_dubai_healthcare_city);
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	// Remove the title bar.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Typeface shmm_default_font = Typeface.createFromAsset(getAssets(), "fonts/arial_rounded_bold.ttf");
        
        Button rashidiyaDirectionButton = (Button)findViewById(R.id.rashidiya_button);
        Button jebelAliDirectionButton = (Button)findViewById(R.id.jebel_ali_button);
        Button etisalatDirectionButton = (Button)findViewById(R.id.etisalat_button);
        Button creekDirectionButton = (Button)findViewById(R.id.creek_button);
        
        rashidiyaDirectionButton.setTextColor(Color.parseColor("white"));
        jebelAliDirectionButton.setTextColor(Color.parseColor("white"));
        etisalatDirectionButton.setTextColor(Color.parseColor("white"));
        creekDirectionButton.setTextColor(Color.parseColor("white"));
        
        rashidiyaDirectionButton.setTypeface(shmm_default_font);
        jebelAliDirectionButton.setTypeface(shmm_default_font);
        etisalatDirectionButton.setTypeface(shmm_default_font);
        creekDirectionButton.setTypeface(shmm_default_font);
        
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
        
        rashidiyaDirectionButton.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_rashidiya.getDrawable(), null);
        jebelAliDirectionButton.setCompoundDrawables(sd_terminus_jebel_ali.getDrawable(), null, sd_arrow_right.getDrawable(), null);
        etisalatDirectionButton.setCompoundDrawables(sd_arrow_left.getDrawable(), null, sd_terminus_etisalat.getDrawable(), null);
        creekDirectionButton.setCompoundDrawables(sd_terminus_creek.getDrawable(), null, sd_arrow_right.getDrawable(), null);
        
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
        
        ValueAnimator direction_light_separator_animation_3 = ObjectAnimator.ofFloat(findViewById(R.id.direction_light_separator_3), "alpha", 0.8f, 0.5f);
        direction_light_separator_animation_3.setDuration(500);
        direction_light_separator_animation_3.setRepeatCount(ValueAnimator.INFINITE);
        direction_light_separator_animation_3.setRepeatMode(ValueAnimator.REVERSE);
        direction_light_separator_animation_3.start();
        
        calculateBeforeNoorForRashidiya();
        calculateAfterNoorForRashidiya();
        calculateBeforeNoorForJebelAli();
        calculateAfterNoorForJebelAli();
        calculateForEtisalat();
        calculateForCreek();
        
        gps = new GPSTracker(this);

        // Check if GPS is enabled. 
        if ( gps.canGetLocation() )
        {
            location_latitude = gps.getLatitude();
            location_longitude = gps.getLongitude();
            
            System.out.println("1) longitude: " + location_longitude + ", latitude: " + location_latitude);
            
            fetchNearbyStation();
        }
        else
        {
            // Can't get location!
            // GPS or network is disabled.
            // Ask user to enable GPS/network in Settings.
            gps.showSettingsAlert();
        }
        
        // Get the Nol card number
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        nol_card_number = settings.getString("nol_card_number", "no_card");
        nol_current_balance = settings.getFloat("nol_current_balance", -100);
        
        final MediaPlayer player = MediaPlayer.create(this, R.raw.menu_countdown);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        
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
    
    public void setDirection(View view)
    {
    	Intent intent = new Intent(this, SetDirectionActivity.class);
    	int view_ID = view.getId();
    	
    	switch ( view_ID )
		{
			case R.id.rashidiya_button:
				SetDirectionActivity.is_red_line = true;
				
				break;
				
			case R.id.jebel_ali_button:
				SetDirectionActivity.is_red_line = true;
				
				break;
				
			case R.id.etisalat_button:
				SetDirectionActivity.is_red_line = false;
				
				break;
				
			case R.id.creek_button:
				SetDirectionActivity.is_red_line = false;
				
				break;
		}

    	intent.putExtra(DIRECTION, view_ID + "");
    	
    	startActivity(intent);
    }
    
    public void calculateBeforeNoorForRashidiya()
    {
    	new Thread(new Runnable()
    	{
    	    public void run()
    	    {
    	    	try
                {
    	    		SimpleDateFormat date_formatter = new SimpleDateFormat("EEEE");
        	    	Date date_today = new Date();
        	    	String week_day = date_formatter.format(date_today);
        	    	
        	    	List<List<String>> active_array = new ArrayList<List<String>>();
        	    	boolean express_trains_today = true;
        	        final int noor_index = 16;
        	        
        	        Calendar calendar = Calendar.getInstance();
        	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        	        int current_minute = calendar.get(Calendar.MINUTE);
        	        
        	        if ( current_hour < 5 ) // 24-hour format fix.
        		    {
        		        current_hour += 12;
        		    }
        	        
        	        if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 4 ) // Logic fix for when the clock passes 12 am.
        	        {
        	        	active_array = station_timings_rashidiya_saturday;
        	        }
        	        else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 8 )
        	        {
        	        	active_array = station_timings_rashidiya_friday;
        	            express_trains_today = false;
        	        }
        	        else
        	        {
        	        	active_array = station_timings_rashidiya_week_days;
        	        }
        	        
        	        List<String> noor_timings = active_array.get(noor_index);
        	        
        	        date_formatter.applyPattern("HH:mm:ss");
        	        
        	        for ( int row = 2; row < noor_timings.size(); row++ )  // Skip express trains.
        	        {
        	            for ( int column = noor_index + 1; column < active_array.size(); column++ )
        	            {
        	                int actual_row_previous = row;
        	                
        	                if ( !Boolean.valueOf(station_list_express_rashidiya[column - 1]) || !express_trains_today )
        	                {
        	                    actual_row_previous -= 2;
        	                }
        	                
        	                List<String> current_station = new ArrayList<String>();
        	                current_station.addAll(active_array.get(column));
        	                
        	                List<String> next_station = active_array.get(column - 1);
        	                
        	                Date old_timing = date_formatter.parse(next_station.get(actual_row_previous));
        	                int distance_time_in_seconds = Integer.parseInt(station_time_difference_red[column]) * 60;
        	                
        	                calendar.setTime(old_timing);
        	                calendar.add(Calendar.SECOND, -distance_time_in_seconds);
        	                
        	                current_station.add(date_formatter.format(calendar.getTime()));
        	                
        	                active_array.set(column, current_station);
        	            }
        	        }
                } 
                catch ( Exception e )
                {
               	  	// Handle the exception
                	System.out.println("Exception in Rashidiya before Noor: " + e);
                }
    	    }
    	}).start();
    }
    
    public void calculateAfterNoorForRashidiya()
    {
    	new Thread(new Runnable() 
    	{
    	    public void run() 
    	    {
    	    	try
                {
    	    		SimpleDateFormat date_formatter = new SimpleDateFormat("EEEE");
        	    	Date date_today = new Date();
        	    	String week_day = date_formatter.format(date_today);
        	    	
        	    	List<List<String>> active_array = new ArrayList<List<String>>();
        	    	boolean express_trains_today = true;
        	        final int noor_index = 16;
        	        
        	        Calendar calendar = Calendar.getInstance(); 
        	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        	        int current_minute = calendar.get(Calendar.MINUTE);
        	        
        	        if ( current_hour < 5 ) // 24-hour format fix.
        		    {
        		        current_hour += 12;
        		    }
        	        
        	        if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 4 ) // Logic fix for when the clock passes 12 am.
        	        {
        	        	active_array = station_timings_rashidiya_saturday;
        	        }
        	        else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 8 )
        	        {
        	        	active_array = station_timings_rashidiya_friday;
        	            express_trains_today = false;
        	        }
        	        else
        	        {
        	        	active_array = station_timings_rashidiya_week_days;
        	        }
        	        
        	        List<String> noor_timings = active_array.get(noor_index);
        	        
        	        date_formatter.applyPattern("HH:mm:ss");
        	        
        	        for ( int column = noor_index - 1; column >= 0; column-- )
        	        {
        	            int timingCount = noor_timings.size();
        	            int row = 2;
        	            
        	            if ( !Boolean.valueOf(station_list_express_rashidiya[column]) || !express_trains_today ) // Skip express trains.
        	            {
        	                row = 0;
        	            }
        	            
        	            if ( column <= 7 )
        	            {
        	                timingCount = noor_timings.size() + 3;
        	            }
        	            
        	            for ( ; row < timingCount; row++ )
        	            {
        	                int actual_row_previous = row;
        	                
        	                List<String> current_station = new ArrayList<String>();
        	                current_station.addAll(active_array.get(column));
        	                
        	                List<String> previous_station = active_array.get(column + 1);
        	                
        	                Date old_timing = date_formatter.parse(previous_station.get(actual_row_previous));
        	                int distance_time_in_seconds = Integer.parseInt(station_time_difference_red[column + 1]) * 60;
        	                
        	                calendar.setTime(old_timing);
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
        	                current_station.add(date_formatter.format(calendar.getTime()));
        	                
        	                active_array.set(column, current_station);
        	            }
        	        }
                }
                catch ( Exception e )
                {
                	// Handle the exception
                	System.out.println("Exception in Rashidiya after Noor: " + e);
                }
    	    }
    	}).start();
    }
    
    public void calculateBeforeNoorForJebelAli()
    {
    	new Thread(new Runnable() 
    	{
    	    public void run() 
    	    {
    	    	try
                {
    	    		SimpleDateFormat date_formatter = new SimpleDateFormat("EEEE");
        	    	Date date_today = new Date();
        	    	String week_day = date_formatter.format(date_today);
        	    	
        	    	List<List<String>> active_array = new ArrayList<List<String>>();
        	    	boolean express_trains_today = true;
        	        final int noor_index = 16;
        	        
        	        Calendar calendar = Calendar.getInstance(); 
        	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        	        int current_minute = calendar.get(Calendar.MINUTE);
        	        
        	        if ( current_hour < 5 ) // 24-hour format fix.
        		    {
        		        current_hour += 12;
        		    }
        	        
        	        if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 3 ) // Logic fix for when the clock passes 12 am.
        	        {
        	        	active_array = station_timings_jebel_ali_saturday;
        	        }
        	        else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 9 )
        	        {
        	        	active_array = station_timings_jebel_ali_friday;
        	            express_trains_today = false;
        	        }
        	        else
        	        {
        	        	active_array = station_timings_jebel_ali_week_days;
        	        }
        	        
        	        List<String> noor_timings = active_array.get(noor_index);
        	        
        	        date_formatter.applyPattern("HH:mm:ss");
        	        
        	        for ( int row = 2; row < noor_timings.size(); row++ )  // Skip express trains.
        	        {
        	            for ( int column = noor_index - 1; column >= 0; column-- )
        	            {
        	                int actual_row_current = row;
        	                int actual_row_previous = row;
        	                
        	                if ( !Boolean.valueOf(station_list_express_rashidiya[column]) || !express_trains_today ) // Go back to account for uneeded express train slots.
        	                {
        	                	actual_row_current -= 2;
        	                }
        	                
        	                if ( !Boolean.valueOf(station_list_express_jebel_ali[column + 1]) || !express_trains_today )
        	                {
        	                	actual_row_previous -= 2;
        	                }
        	                
        	                List<String> current_station = new ArrayList<String>();
        	                current_station.addAll(active_array.get(column));
        	                
        	                List<String> next_station = active_array.get(column + 1);
        	                
        	                Date old_timing = date_formatter.parse(next_station.get(actual_row_previous));
        	                int distance_time_in_seconds = Integer.parseInt(station_time_difference_red[column + 1]) * 60;
        	                
        	                calendar.setTime(old_timing);
        	                calendar.add(Calendar.SECOND, -distance_time_in_seconds);
        	                
        	                current_station.add(date_formatter.format(calendar.getTime()));
        	                
        	                active_array.set(column, current_station);
        	            }
        	        }
                }
                catch ( Exception e )
                {
                	// Handle the exception
                	System.out.println("Exception in Jebel Ali before Noor: " + e);
                }
    	    }
    	}).start();
    }
    
    public void calculateAfterNoorForJebelAli()
    {
    	new Thread(new Runnable() 
    	{
    	    public void run() 
    	    {
    	    	try
                {
    	    		SimpleDateFormat date_formatter = new SimpleDateFormat("EEEE");
        	    	Date date_today = new Date();
        	    	String week_day = date_formatter.format(date_today);
        	    	
        	    	List<List<String>> active_array = new ArrayList<List<String>>();
        	    	boolean express_trains_today = true;
        	        final int noor_index = 16;
        	        
        	        Calendar calendar = Calendar.getInstance(); 
        	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        	        int current_minute = calendar.get(Calendar.MINUTE);
        	        
        	        if ( current_hour < 5 ) // 24-hour format fix.
        		    {
        		        current_hour += 12;
        		    }
        	        
        	        if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 3 ) // Logic fix for when the clock passes 12 am.
        	        {
        	        	active_array = station_timings_jebel_ali_saturday;
        	        }
        	        else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 9 )
        	        {
        	        	active_array = station_timings_jebel_ali_friday;
        	            express_trains_today = false;
        	        }
        	        else
        	        {
        	        	active_array = station_timings_jebel_ali_week_days;
        	        }
        	        
        	        List<String> noor_timings = active_array.get(noor_index);
        	        
        	        date_formatter.applyPattern("HH:mm:ss");
        	        
        	        for ( int row = 2; row < noor_timings.size(); row++ )  // Skip express trains.
        	        {
        	            for ( int column = noor_index + 1; column < active_array.size(); column++ )
        	            {
        	                int actual_row_current = row;
        	                int actual_row_previous = row;
        	                
        	                if ( !Boolean.valueOf(station_list_express_jebel_ali[column]) || !express_trains_today )
        	                {
        	                	actual_row_current -= 2;
        	                }
        	                
        	                if ( !Boolean.valueOf(station_list_express_jebel_ali[column - 1]) || !express_trains_today )
        	                {
        	                	actual_row_previous -= 2;
        	                }
        	                
        	                List<String> current_station = new ArrayList<String>();
        	                current_station.addAll(active_array.get(column));
        	                
        	                List<String> previous_station = active_array.get(column - 1);
        	                
        	                Date old_timing = date_formatter.parse(previous_station.get(actual_row_previous));
        	                int distance_time_in_seconds = Integer.parseInt(station_time_difference_red[column]) * 60;
        	                
        	                calendar.setTime(old_timing);
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
        	                current_station.add(date_formatter.format(calendar.getTime()));
        	                
        	                active_array.set(column, current_station);
        	            }
        	        }
                }
                catch ( Exception e )
                {
                	// Handle the exception
                	System.out.println("Exception in Jebel Ali after Noor: " + e);
                }
    	    }
    	}).start();
    }
    
    public void calculateForEtisalat()
    {
    	new Thread(new Runnable() 
    	{
    	    public void run() 
    	    {
    	    	try
    	    	{
    	    		SimpleDateFormat date_formatter = new SimpleDateFormat("EEEE");
        	    	Date date_today = new Date();
        	    	String week_day = date_formatter.format(date_today);
        	    	
        	    	List<List<String>> active_array = new ArrayList<List<String>>();
        	        final int healthcare_city_index = 17;
        	        
        	        Calendar calendar = Calendar.getInstance(); 
        	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        	        int current_minute = calendar.get(Calendar.MINUTE);
        	        
        	        if ( current_hour < 5 ) // 24-hour format fix.
        		    {
        		        current_hour += 12;
        		    }
        	        
        	        if ( week_day.equals("Saturday") && current_hour >= 1 ) // Logic fix for when the clock passes 12 am.
        	        {
        	        	active_array = station_timings_etisalat_week_days;
        	        }
        	        else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 2 )
        	        {
        	        	active_array = station_timings_etisalat_friday;
        	        }
        	        else
        	        {
        	        	active_array = station_timings_etisalat_week_days;
        	        }
        	        
        	        List<String> healthcare_city_timings = active_array.get(healthcare_city_index);
        	        
        	        date_formatter.applyPattern("HH:mm:ss");
        	        
        	        for ( int row = 0; row < healthcare_city_timings.size(); row++ )
        	        {
        	            for ( int column = healthcare_city_index - 1; column >= 0; column-- )
        	            {
        	            	List<String> current_station = new ArrayList<String>();
        	                current_station.addAll(active_array.get(column));
        	                
        	                List<String> next_station = active_array.get(column + 1);
        	                
        	                Date old_timing = date_formatter.parse(next_station.get(row));
        	                int distance_time_in_seconds = Integer.parseInt(station_time_difference_green[column + 1]) * 60;
        	                
        	                calendar.setTime(old_timing);
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
        	                current_station.add(date_formatter.format(calendar.getTime()));
        	                
        	                active_array.set(column, current_station);
        	            }
        	        }
    	    	}
    	    	catch ( Exception e )
                {
                	// Handle the exception
                	System.out.println("Exception in Etisalat: " + e);
                }
    	    }
    	}).start();
    }
    
    public void calculateForCreek()
    {
    	new Thread(new Runnable() 
    	{
    	    public void run() 
    	    {
    	    	try
    	    	{
    	    		SimpleDateFormat date_formatter = new SimpleDateFormat("EEEE");
        	    	Date date_today = new Date();
        	    	String week_day = date_formatter.format(date_today);
        	    	
        	    	List<List<String>> active_array = new ArrayList<List<String>>();
        	        final int etisalat_index = 0;
        	        
        	        Calendar calendar = Calendar.getInstance(); 
        	        int current_hour = calendar.get(Calendar.HOUR_OF_DAY);
        	        int current_minute = calendar.get(Calendar.MINUTE);
        	        
        	        if ( current_hour < 5 ) // 24-hour format fix.
        		    {
        		        current_hour += 12;
        		    }
        	        
        	        if ( week_day.equals("Saturday") && current_hour >= 1 && current_minute > 4 ) // Logic fix for when the clock passes 12 am.
        	        {
        	        	active_array = station_timings_creek_week_days;
        	        }
        	        else if ( week_day.equals("Friday") && current_hour >= 1 && current_minute > 6 )
        	        {
        	        	active_array = station_timings_creek_friday;
        	        }
        	        else
        	        {
        	        	active_array = station_timings_creek_week_days;
        	        }
        	        
        	        List<String> etisalat_timings = active_array.get(etisalat_index);
        	        
        	        date_formatter.applyPattern("HH:mm:ss");
        	        
        	        for ( int row = 0; row < etisalat_timings.size(); row++ )
        	        {
        	            for ( int column = etisalat_index + 1; column < active_array.size(); column++ )
        	            {
        	            	List<String> current_station = new ArrayList<String>();
        	                current_station.addAll(active_array.get(column));
        	                
        	                List<String> next_station = active_array.get(column - 1);
        	                
        	                Date old_timing = date_formatter.parse(next_station.get(row));
        	                int distance_time_in_seconds = Integer.parseInt(station_time_difference_green[column]) * 60;
        	                
        	                calendar.setTime(old_timing);
        	                calendar.add(Calendar.SECOND, distance_time_in_seconds);
        	                
        	                current_station.add(date_formatter.format(calendar.getTime()));
        	                
        	                active_array.set(column, current_station);
        	            }
        	        }
    	    	}
    	    	catch ( Exception e )
                {
                	// Handle the exception
                	System.out.println("Exception in Creek: " + e);
                }
    	    }
    	}).start();
    }
    
    public static void fetchNearbyStation()
    {
    	Calendar calendar = Calendar.getInstance();
    	int current_year = calendar.get(Calendar.YEAR);
        int current_month = calendar.get(Calendar.MONTH);
        int current_day = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        
    	String url_string = "https://api.foursquare.com/v2/venues/search?"; // 4sq requires this first param as v=yyyymmdd.
        
    	url_string = url_string + String.format("v=%d%02d%02d", current_year, current_month, current_day);
    	url_string = url_string + "&client_id=" + YOUR_4SQ_CLIENT_ID;
    	url_string = url_string + "&client_secret=" + YOUR_4SQ_CLIENT_SECRET;
    	url_string = url_string + "&intent=browse";
    	url_string = url_string + "&radius=800";
    	url_string = url_string + "&ll=" + location_latitude + "," + location_longitude; // The ordering is important!!!
        
    	System.out.println("2) longitude: " + location_longitude + ", latitude: " + location_latitude);
    	
    	ConnectivityManager connection_manager = (ConnectivityManager)GPSTracker.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection_manager.getActiveNetworkInfo();
        
        if ( networkInfo != null && networkInfo.isConnected() )
        {
        	AsyncHttpClient client = new AsyncHttpClient();
            client.get(url_string, new AsyncHttpResponseHandler()
            {
                @Override
                public void onSuccess(String response)
                {
                	try
                	{
                		boolean should_break = false;
                    	//System.out.println("response data: " + result);
                    	
                    	JSONObject jObject = new JSONObject(response);
                    	
                    	JSONObject response_data = jObject.getJSONObject("response");
                    	JSONArray venues = response_data.getJSONArray("venues");
                    	
                    	for ( int i = 0; i < venues.length(); i++ )
                    	{
                    		JSONObject venue = venues.getJSONObject(i);
                    		
                    		JSONArray categories = venue.getJSONArray("categories");
                    		
                    		for ( int j = 0; j < categories.length(); j++ )
                        	{
                        		JSONObject category = categories.getJSONObject(j);
                    	        
                        		String cat_name = category.getString("shortName");
                        		
                        		if ( cat_name.equals("Train Station") || cat_name.equals("Light Rail") || cat_name.equals("Subway") || cat_name.equals("Building") ) // "Building" is for the NHT station.
                                {
                        			nearest_station_data.put("stationData", venue);
                                    
                                    String station_name = venue.getString("name");
                                    
                                    // Fix & remove unecessary keywords.
                                    station_name = station_name.replaceAll("(?i)metro", "");
                                    station_name = station_name.replaceAll("(?i)station", "");
                                    station_name = station_name.replaceAll("(?i)Center", "Centre");
                                    station_name = station_name.replaceAll("(?i)|", "");
                                    station_name = station_name.replaceAll("(?i)and", "&");
                                    station_name = station_name.replaceAll("(?i)Khalid Bin Al Waleed", "BurJuman");
                                    
                                    station_name = station_name.replaceAll("[^a-zA-Z0-9\\s\\\\]", "");
                                    station_name = station_name.trim();
                                    
                                    // Special case fixes.
                                    if ( station_name.matches("(?i).*Jumeirah Lake Towers.*") ) 
                                    {
                                    	station_name = "Jumeirah Lakes Towers";
                                    }
                                    
                                    if ( station_name.matches("(?i).*Airport Terminal 3.*") )
                                    {
                                    	station_name = "Airport Terminal 3";
                                    }
                                    
                                    if ( station_name.matches("(?i).*Rashidya.*") )
                                    {
                                    	station_name = "Rashidiya";
                                    }
                                    
                                    System.out.println(station_name);
                                    
                                    for (int k = 0; k < station_list_red_names_en.length; k++)
                                    {
                                    	String target_station = station_list_red_names_en[k].toLowerCase();
                                    	
                                        if ( target_station.contains(station_name.toLowerCase()) )
                                        {
                                            should_break = true; // To break the outer loops.
                                            station_autoselected = true;
                                            selected_row = k;
                                            
                                            if ( SetDirectionActivity.is_active )
                                            {
                                            	SetDirectionActivity.selectRow(selected_row);
                                            }
                                            
                                            break;
                                        }
                                    }
                                    
                                    if ( !should_break )
                                    {
                                        for (int k = 0; k < station_list_green_names_en.length; k++)
                                        {
                                        	String target_station = station_list_red_names_en[k].toLowerCase();
                                        	
                                            if ( target_station.contains(station_name.toLowerCase()) )
                                            {
                                                should_break = true; // To break the outer loops.
                                                station_autoselected = true;
                                                selected_row = k;
                                                
                                                if ( SetDirectionActivity.is_active )
                                                {
                                                	SetDirectionActivity.selectRow(selected_row);
                                                }
                                                
                                                break;
                                            }
                                        }
                                    }
                                }
                        		
                        		if ( should_break )
                                {
                                    break;
                                }
                        	}
                    		
                    		if ( should_break )
                            {
                                break;
                            }
                    	}
                    	
                        // Nol Card Balance
                        if ( !nol_card_number.equals("no_card"))
                        {
                        	checkCardBalance();
                        }
                	}
                	catch ( Exception e )
                	{
                		System.out.println("Exception while parsing 4sq response: " + e);
                	}
                }
            });
        }
        else
        {
            new AlertDialog.Builder(GPSTracker.mContext)
            .setTitle("Error!")
            .setMessage("No network connection available!")
            .setPositiveButton("Okay", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which)
                { 
                	dialog.cancel();
                }
             })
             .show();
        }
    }
    
    public static void checkCardBalance()
    {
    	is_checking_balance = true;
    	
    	String url_string = "http://rta.tacme.com/RTAP2/NolService.svc";
        
    	ConnectivityManager connection_manager = (ConnectivityManager)GPSTracker.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection_manager.getActiveNetworkInfo();
        
        if ( networkInfo != null && networkInfo.isConnected() )
        {
        	String content_type = "text/xml; charset=utf-8;";
            String xml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns=\"http://tempuri.org/\"><soap:Header/><soap:Body><GetNolBalance><NolCardNumber>" + nol_card_number + "</NolCardNumber><language>ar</language></GetNolBalance></soap:Body></soap:Envelope>";
            HttpEntity entity;
            
            try
            {
                entity = new StringEntity(xml, "UTF-8");
            } 
            catch (IllegalArgumentException e)
            {
            	System.out.println("StringEntity: IllegalArgumentException: " + e);
                return;
            } 
            catch (UnsupportedEncodingException e) {
                System.out.println("StringEntity: UnsupportedEncodingException: " + e);
                return;
            }
        	
        	AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        	client.addHeader("SOAPAction", "http://tempuri.org/INolService/GetNolBalance");
        	client.post(GPSTracker.mContext, url_string, entity, content_type, new AsyncHttpResponseHandler() 
        	{
        	    @Override
        	    public void onSuccess(String response) 
        	    {
        	    	try
        	    	{
        	    		String parsed_xml = response.substring(0, response.indexOf("<GetNolBalanceResult>") + 21); // + 21 to include the XML tag itself.
            	        
        	    		parsed_xml = response.replace(parsed_xml, "");
            	        
        	    		parsed_xml = parsed_xml.substring(0, parsed_xml.indexOf("</GetNolBalanceResult>"));
            	        
            	        if ( parsed_xml.length() > 0 )
            	        {
            	        	float balance = Float.parseFloat(parsed_xml);
            	        	
            	        	if ( balance <= 9.0 )
                            {
            	        		String balance_message = String.format("AED %.02f left. Better top it up!", balance);
                                
            	        		Bitmap icon_bitmap = BitmapFactory.decodeResource(GPSTracker.mContext.getResources(), R.drawable.notification_icon_small);  
            	                
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(GPSTracker.mContext)
                                        .setSmallIcon(R.drawable.notification_icon_small)
                                        .setLargeIcon(icon_bitmap)
                                        .setContentTitle("Nol Card balance low")
                                        .setContentText(balance_message);
                                
                                Intent resultIntent = new Intent(GPSTracker.mContext, MainActivity.class);
                                
                                // The stack builder object will contain an artificial back stack for the
                                // started Activity.
                                // This ensures that navigating backward from the Activity leads out of
                                // your application to the Home screen.
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(GPSTracker.mContext);
                                // Adds the back stack for the Intent (but not the Intent itself).
                                stackBuilder.addParentStack(MainActivity.class);
                                // Adds the Intent that starts the Activity to the top of the stack.
                                stackBuilder.addNextIntent(resultIntent);
                                
                                PendingIntent resultPendingIntent =
                                        stackBuilder.getPendingIntent(
                                            0,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                        );
                                mBuilder.setContentIntent(resultPendingIntent);
                                NotificationManager mNotificationManager = (NotificationManager)GPSTracker.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                                // mId allows you to update the notification later on.
                                mNotificationManager.notify(1, mBuilder.build());
                                
                                new AlertDialog.Builder(GPSTracker.mContext)
                                .setTitle("Nol Card Balance")
                                .setMessage(balance_message)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() 
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    { 
                                    	dialog.cancel();
                                    }
                                 })
                                 .show();
                            }
                            else if  ( nol_current_balance == -100 )
                            {
                                String balance_string = String.format("%.02f", balance);
                                String balance_message = "Your current balance is AED " + balance_string + "\n\nرصيدك الحالي هو " + westernToArabicNumerals(balance_string) + " درهم";
                                
                                new AlertDialog.Builder(GPSTracker.mContext)
                                .setTitle("Nol Card Balance")
                                .setMessage(balance_message)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() 
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    { 
                                    	dialog.cancel();
                                    }
                                 })
                                 .show();
                            }
            	        	
            	        	saveNolBalance(balance);
            	        }
        	    	}
        	    	catch ( Exception e )
        	    	{
        	    		System.out.println("Exception in Nol card response: " + e);
        	    	}
        	    }
        	});
        }
        else
        {
            new AlertDialog.Builder(GPSTracker.mContext)
            .setTitle("Error!")
            .setMessage("No network connection available!")
            .setPositiveButton("Okay", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which)
                { 
                	dialog.cancel();
                }
             })
             .show();
        }
    }
    
    public static void saveNolBalance(float balance)
    {
    	// We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = GPSTracker.mContext.getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("nol_current_balance", balance);
        
        // Commit the edits!
        editor.commit();
    }
    
    public static String westernToArabicNumerals(String number)
	{
		number = number.replace("0", "٠");
		number = number.replace("1", "١");
		number = number.replace("2", "٢");
		number = number.replace("3", "٣");
		number = number.replace("4", "٤");
		number = number.replace("5", "٥");
		number = number.replace("6", "٦");
		number = number.replace("7", "٧");
		number = number.replace("8", "٨");
		number = number.replace("9", "٩");
		
		return number;
	}
}

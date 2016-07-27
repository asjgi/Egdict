package engdict.player.newdesign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class RankingActivity extends Activity  {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser j4Ranking = new JSONParser();

	String my_phone;
	String my_mileage;
	int mileage = 0;
	
	String phone1;
	String mileage1;
	String phone2;
	String mileage2;
	String phone3;
	String mileage3;
	String phone4;
	String mileage4;
	String phone5;
	String mileage5;
	
//	TextView tvMyPhone;
	TextView tvMyMileage;

	TextView tvPhone1;
	TextView tvMileage1;
	TextView tvPhone2;
	TextView tvMileage2;
	TextView tvPhone3;
	TextView tvMileage3;
	TextView tvPhone4;
	TextView tvMileage4;
	TextView tvPhone5;
	TextView tvMileage5;
	
	ArrayList<HashMap<String, String>> flightsList;

	// url to create new mileage
	private static String url_create_mileage = "http://125.131.73.143/engdict/create_flight.php";

	// url to get all flights list
	private static String url_all_flights = "http://125.131.73.143/engdict/get_all_flights.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_FLIGHTS = "flights";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_MILEAGE = "mileage";

	// flights JSONArray
	JSONArray flights = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);

		// Hashmap for ListView
		flightsList = new ArrayList<HashMap<String, String>>();
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		my_phone = tm.getLine1Number();
		mileage = 0;
		DBHandler dbhandler = DBHandler.open(this);
		Cursor cursor = dbhandler.selectMileage();
		if (cursor.getCount() > 0) {
			do {
				mileage += Integer.parseInt(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		dbhandler.deleteMileage();
		dbhandler.close();
		tvPhone1 = (TextView) findViewById(R.id.textView5);
		tvMileage1 = (TextView) findViewById(R.id.textView6);
		tvPhone2 = (TextView) findViewById(R.id.textView7);
		tvMileage2 = (TextView) findViewById(R.id.textView8);
		tvPhone3 = (TextView) findViewById(R.id.textView9);
		tvMileage3 = (TextView) findViewById(R.id.textView10);
		tvPhone4 = (TextView) findViewById(R.id.textView11);
		tvMileage4 = (TextView) findViewById(R.id.textView12);
		tvPhone5 = (TextView) findViewById(R.id.textView13);
		tvMileage5 = (TextView) findViewById(R.id.textView14);
		
//		tvMyPhone = (TextView) findViewById(R.id.my_phone);
		tvMyMileage = (TextView) findViewById(R.id.my_mileage);

		if (mileage > 0) {
			my_mileage = "" + mileage;
		} else {
			my_mileage = "0";
		}
		
//		tvMyPhone.setText(my_phone);
		tvMyMileage.setText(my_mileage);
		// Loading flights in Background Thread
		new LoadAllFlights().execute();
	}

	/**
	 * Background Async Task to Load all flight by making HTTP Request
	 * */
	class LoadAllFlights extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RankingActivity.this);
			pDialog.setMessage("Loading Flights. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All flights from url
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> my_params = new ArrayList<NameValuePair>();
			my_params.add(new BasicNameValuePair("phone", my_phone));
			my_params.add(new BasicNameValuePair("mileage", my_mileage));

			// getting JSON Object
			// Note that create mileage url accepts POST method
			j4Ranking.makeHttpRequest(url_create_mileage, "POST", my_params);

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = j4Ranking.makeHttpRequest(url_all_flights, "GET",
					params);

			// Check your log cat for JSON response
			Log.d("All Flights: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// flights found
					// Getting Array of flights
					flights = json.getJSONArray(TAG_FLIGHTS);

					// looping through All flights
					
					if (flights.length() > 0) {
						JSONObject c = flights.getJSONObject(0);	
						phone1 = c.getString(TAG_PHONE);
						mileage1 = c.getString(TAG_MILEAGE);						
					}
					if (flights.length() > 1) {
						JSONObject c = flights.getJSONObject(1);	
						phone2 = c.getString(TAG_PHONE);
						mileage2 = c.getString(TAG_MILEAGE);						
					}
					if (flights.length() > 2) {
						JSONObject c = flights.getJSONObject(2);	
						phone3 = c.getString(TAG_PHONE);
						mileage3 = c.getString(TAG_MILEAGE);						
					}
					if (flights.length() > 3) {
						JSONObject c = flights.getJSONObject(3);	
						phone4 = c.getString(TAG_PHONE);
						mileage4 = c.getString(TAG_MILEAGE);						
					}
					if (flights.length() > 4) {
						JSONObject c = flights.getJSONObject(4);	
						phone5 = c.getString(TAG_PHONE);
						mileage5 = c.getString(TAG_MILEAGE);						
					}
				} else {
					// no flights found
					// Launch Summary Activity
					Intent i = new Intent(getApplicationContext(),
							SummaryActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all flights
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					tvPhone1.setText(phone1);
					tvMileage1.setText(mileage1);					
					tvPhone2.setText(phone2);
					tvMileage2.setText(mileage2);					
					tvPhone3.setText(phone3);
					tvMileage3.setText(mileage3);					
					tvPhone4.setText(phone4);
					tvMileage4.setText(mileage4);					
					tvPhone5.setText(phone5);
					tvMileage5.setText(mileage5);					
				}
			});

		}

	}
}

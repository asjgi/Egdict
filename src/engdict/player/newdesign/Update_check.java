package engdict.player.newdesign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class Update_check extends Activity {

	private Handler mHandler;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo);
		mHandler = new Handler();

		/* Get Last Update Time from Preferences */
		SharedPreferences prefs = getPreferences(0);
		long lastUpdateTime = prefs.getLong("lastUpdateTime", 0);

		/* Should Activity Check for Updates Now? */
		if ((lastUpdateTime + (24 * 60 * 60 * 1000 * 3)) < System.currentTimeMillis()) {

			/* Save current timestamp for next Check */
			lastUpdateTime = System.currentTimeMillis();
			SharedPreferences.Editor editor = getPreferences(0).edit();
			editor.putLong("lastUpdateTime", lastUpdateTime);
			editor.commit();

			/* Start Update */
			checkUpdate.start();

		} else {
			Intent intent = new Intent(Update_check.this, main.class);
			startActivity(intent);
			finish();
		}
	}

	/* This Thread checks for Updates in the Background */
	private Thread checkUpdate = new Thread() {
		public void run() {
			/* Post a Handler for the UI to pick up and open the Dialog */
			mHandler.post(showUpdate);
		}
	};

	/* This Runnable creates a Dialog and asks the user to open the Market */
	private Runnable showUpdate = new Runnable() {
		public void run() {
			new AlertDialog.Builder(Update_check.this)
					.setTitle("Update Check")
					.setMessage(
							"An update for is available!\n"
									+ "Open Android Market and see the details?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* User clicked OK so do some stuff */
									Intent intent = new Intent(
											Intent.ACTION_VIEW,
											Uri.parse("https://play.google.com/store/apps/details?id=engdict.player.notFree"));
									startActivity(intent);
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* User clicked Cancel */
									Intent intent = new Intent(
											Update_check.this, main.class);
									startActivity(intent);
									finish();
								}
							}).show();

		}

	};
}
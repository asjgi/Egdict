package engdict.player.newdesign;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryActivity extends Activity {
	Button btn_screen_shot;
	Button btnSelect;
	Button btnclose;

	String path;

	TextView totalQuestions;
	TextView correctAnswers;

	int total_questions = 0;
	int correct_answers = 0;
	int mileage = 0;

	double score = 0;

	// sound_pool
	SoundPool sp;
	int Sound_F, Sound_S;
	// screen shot add

	Bitmap bmScreen;
	RelativeLayout mLayout;
	Dialog screenDialog;
	static final int ID_SCREENDIALOG = 1;
	private static final int IMAGE_CAPTURE = 0;

	ImageView bmImage;
	// TextView TextOut;

	TextView movie_title;
	EditText EditTextIn;
	LinearLayout container;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.summary);

		container = (LinearLayout) findViewById(R.id.screen);
		Intent intent = getIntent();
		if (intent.hasExtra("FilePath")) {
			path = intent.getStringExtra("FilePath");
		}

		movie_title = (TextView) findViewById(R.id.summary_movie_title);
		movie_title.setText(path);

		// Sound Management
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		Sound_F = sp.load(this, R.raw.camera_focusing, 1);
		Sound_S = sp.load(this, R.raw.camera_shutter_click, 1);

		btnclose = (Button) findViewById(R.id.summary_close);
		btnSelect = (Button) findViewById(R.id.select_btn);

		totalQuestions = (TextView) findViewById(R.id.summary_score);
		totalQuestions.setGravity(Gravity.CENTER);
		// correctAnswers = (TextView) findViewById(R.id.Text2);

		// add screen

		btn_screen_shot = (Button) findViewById(R.id.screen_shot);

		DBHandler dbhandler = DBHandler.open(this);
		path = dbhandler.getMediaNameOfLastBlank();
		Cursor cursor = dbhandler.selectBlank(path);

		btnclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (cursor.getCount() > 0) {
			total_questions = Integer.parseInt(cursor.getString(2));
			correct_answers = Integer.parseInt(cursor.getString(3));
			dbhandler.deleteBlank(path);
		}

		mileage = (total_questions * 50) + (correct_answers * 100);
		dbhandler.insertMileage(mileage);
		dbhandler.close();

		score = (double) (correct_answers * 100);

		score = (double) (total_questions + score);

		if (score < 99) {

			score = 0;
		}

		String total_number_str = "" + (int) score;
		totalQuestions.setText(total_number_str);

		// String correct_number_str = "" + correct_answers;
		// correctAnswers.setText(correct_number_str);

		btn_screen_shot.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Ranking Activity

				try {
					saveImage();
				} catch (Exception e) {
					e.printStackTrace();
				}
				sp.play(Sound_F, 1, 1, 0, 0, 1);
				sp.play(Sound_S, 1, 1, 0, 0, 1);
			}
		});

		btnSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Ranking Activity
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("https://m.facebook.com/306473089529441"));
				startActivity(intent);
				finish();
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	protected void saveImage() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyymmddhhmmss");
		Date d = new Date();
		String date = format.format(d);
		String fname = "engdict_" + date;
		String path = null;
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		} else {
			path = Environment.MEDIA_UNMOUNTED;
		}

		File dir = new File(path + "/DCIM/Camera/");
		String nPath;
		if (!dir.isDirectory()) {
			dir.mkdir();
		}
		nPath = dir + "/" + fname + ".jpg";
		container.setDrawingCacheEnabled(true);
		container.buildDrawingCache();
		Bitmap captureview = container.getDrawingCache();
		FileOutputStream fos;

		try {
			// Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
//			Toast.makeText(this, nPath, Toast.LENGTH_SHORT).show();
			fos = new FileOutputStream(nPath);
			captureview.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
			container.setDrawingCacheEnabled(false);
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + nPath)));
			Toast.makeText(this, "Be captured and saved Pic in the gallery", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
		}
	}

	public void TakeAbumAction() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, IMAGE_CAPTURE);
	}
}
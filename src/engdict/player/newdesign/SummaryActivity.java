package engdict.player.newdesign;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SummaryActivity extends Activity {
	Button btn_screen_shot;
	Button btnSelect;

	String path;

	TextView totalQuestions;
	TextView correctAnswers;

	int total_questions = 0;
	int correct_answers = 0;
	int mileage = 0;
	
	double score =0;

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

	View screen;
	EditText EditTextIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.summary);
		// Sound Management
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		Sound_F = sp.load(this, R.raw.camera_focusing, 1);
		Sound_S = sp.load(this, R.raw.camera_shutter_click, 1);

		btnSelect = (Button) findViewById(R.id.select_btn);

		totalQuestions = (TextView) findViewById(R.id.Text1);
//		correctAnswers = (TextView) findViewById(R.id.Text2);

		// add screen

		btn_screen_shot = (Button) findViewById(R.id.screen_shot);
		screen = (View) findViewById(R.id.screen);

		DBHandler dbhandler = DBHandler.open(this);
		path = dbhandler.getMediaNameOfLastBlank();
		Cursor cursor = dbhandler.selectBlank(path);

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
		
		
		if(score < 99){
			
			score =0;
		}

		String total_number_str = "" + (int)score;
		totalQuestions.setText(total_number_str);

//		String correct_number_str = "" + correct_answers;
//		correctAnswers.setText(correct_number_str);

		btn_screen_shot.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Ranking Activity
				screen.setDrawingCacheEnabled(true);
				bmScreen = screen.getDrawingCache();
				saveImage(bmScreen);

				sp.play(Sound_F, 1, 1, 0, 0, 1);
				sp.play(Sound_S, 1, 1, 0, 0, 1);

			}

		});

		btnSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Ranking Activity
				finish();

			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	protected void saveImage(Bitmap bmScreen2) {
		// TODO Auto-generated method stub

		SimpleDateFormat format = new SimpleDateFormat("yyyymmddhhmmss");
		Date d = new Date();
		String date = format.format(d);
		String fname = "Engdict_Score_" + date;
		File saved_image_file = new File(
				Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + fname
				+ ".png");

		try {

			OutputStream out = new FileOutputStream(saved_image_file);
			bmScreen2.compress(Bitmap.CompressFormat.PNG, 100, out);

			out.flush();

			out.close();

			Toast toast = Toast.makeText(this, "Score Image saved in gallery",
					Toast.LENGTH_LONG);

			toast.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL, 0, 0);

			toast.show();

			finish();

			// TakeAbumAction();

		} catch (Exception e) {

			e.printStackTrace();

			finish();
		}

	}

	public void TakeAbumAction() {

		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, IMAGE_CAPTURE);
	}
}
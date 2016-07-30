package engdict.player.newdesign;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingsActivity extends Activity implements View.OnClickListener {
	Button btnSave;
	RadioGroup grpType;
	RadioGroup grpVoca;
	RatingBar skbSentence;
	SeekBar skbFrequency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);

		grpType = (RadioGroup) findViewById(R.id.radioGroup1);
		grpVoca = (RadioGroup) findViewById(R.id.radioGroup2);
		skbSentence = (RatingBar) findViewById(R.id.seekBar1);
		skbFrequency = (SeekBar) findViewById(R.id.seekBar2);

		btnSave = (Button) findViewById(R.id.save_btn);
		btnSave.setOnClickListener(this);

		DBHandler dbhandler = DBHandler.open(this);

		Cursor cursor = dbhandler.settings();

		String type = "word";
		int maxblank = 10;
		String voca = "300";
		int frequency = 10;

		if (cursor.getCount() > 0) {
			type = cursor.getString(1);
			maxblank = Integer.parseInt(cursor.getString(2));
			voca = cursor.getString(3);
			frequency = Integer.parseInt(cursor.getString(4));
		}

		dbhandler.close();

		if (type.equals("Sentence")) {
			grpType.check(R.id.radioButton1);
		} else {
			grpType.check(R.id.radioButton2);
		}

		if (voca.equals("300~")) {
			grpVoca.check(R.id.radioButton4);
		} else {
			grpVoca.check(R.id.radioButton3);
		}

		skbSentence.setProgress(maxblank);
		skbFrequency.setProgress(frequency);
	}

	public void onClick(View view) {
		DBHandler dbhandler = DBHandler.open(this);
		if (view.getId() == R.id.save_btn) {
			/*
			 * findViewById(R.id.radioButton1); //Sentence
			 * findViewById(R.id.seekBar1); //MaxBlank
			 * findViewById(R.id.radioButton2); //Word(Voca)
			 * findViewById(R.id.radioButton3); //300
			 * findViewById(R.id.radioButton4); //300~
			 * findViewById(R.id.seekBar2); //Frequency
			 */

			String type = "word";
			String maxblank = "10";
			String voca = "300";
			String frequency = "10";

			if (grpType.getCheckedRadioButtonId() == R.id.radioButton1) {
				type = "Sentence";
			}

			if (grpVoca.getCheckedRadioButtonId() == R.id.radioButton4) {
				voca = "300~";
			}

//			Toast.makeText(this,
//					"rating the value" + skbSentence.getProgress(),
//					Toast.LENGTH_SHORT).show();
			maxblank = "" + skbSentence.getProgress();
			frequency = "" + skbFrequency.getProgress();

			long cnt = dbhandler
					.insertSettings(type, maxblank, voca, frequency);
			if (cnt == -1) {
				Toast.makeText(this, "Fail to save", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Save successfully", Toast.LENGTH_LONG)
						.show();
			}
		}

		dbhandler.close();

		Intent intent = new Intent(SettingsActivity.this, main.class);
		startActivity(intent);
		finish();
	}
}
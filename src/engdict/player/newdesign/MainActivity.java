package engdict.player.newdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Button btnSelect;
	Button btnSettings;
	Button btnSummary;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);

		btnSelect = (Button) findViewById(R.id.select_btn);
		btnSettings = (Button) findViewById(R.id.settings_btn);
		btnSummary = (Button) findViewById(R.id.summary_btn);

		btnSelect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Select Activity
				Intent i = new Intent(getApplicationContext(),
						main.class);
				startActivity(i);

			}
		});

		btnSettings.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Settings Activity
				Intent i = new Intent(getApplicationContext(),
						SettingsActivity.class);
				startActivity(i);

			}
		});

		btnSummary.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Summary Activity
				Intent i = new Intent(getApplicationContext(),
						SummaryActivity.class);
				startActivity(i);

			}
		});
	}
}

package engdict.player.notFree;

import engdict.player.notFree.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends Activity {

	Button btnDictation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select);

		btnDictation = (Button) findViewById(R.id.dictation_btn);

		btnDictation.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Dictation Activity
				Intent i = new Intent(getApplicationContext(),
						DictationActivity.class);
				startActivity(i);

			}
		});
	}

}
package engdict.player.newdesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

class smiData {
	long time;
	String text;

	smiData(long time, String text) {
		this.time = time;
		this.text = text;
	}

	public long gettime() {
		return time;
	}

	public String gettext() {
		return text;
	}
}

class blankedSmiData extends smiData {
	int blankPos;
	int blankLength;
	int smiCount;
	boolean correctAnswer;

	static String easyWordTable[] = { "mother", "school", "lesson", "break", "world", "father", "thing", "house",
			"dialog", "morning", "today", "birthday", "brother", "right", "child", "earth", "farmer", "teacher",
			"friend", "family", "country", "money", "class", "water", "please", "future", "happy", "night", "place",
			"student", "tomorrow", "woman", "again", "another", "beautiful", "clock", "doctor", "every", "great",
			"language", "little", "sorry", "thank", "sport", "weather", "dinner", "music", "picture", "question",
			"afternoon", "different", "really", "computer", "animal", "letter", "lunch", "watch", "together",
			"yesterday", "small", "sister", "science", "example", "street", "winter", "diary", "spring", "important",
			"interesting", "speak", "excuse", "always", "prince", "uncle", "summer", "grandfather", "river", "table",
			"baseball", "tennis", "answer", "brown", "evening", "problem", "course", "mountain", "homework", "young",
			"write", "parent", "clothes", "soccer", "north", "never", "often", "learn", "light", "window", "party",
			"south", "building", "number", "change", "newspaper", "flower", "history", "hello", "saturday", "sunday",
			"almost", "bicycle", "large", "present", "sound", "sometimes", "movie", "minute", "person", "paper",
			"second", "listen", "welcome", "early", "pretty", "later", "company", "culture", "experience", "education",
			"effect", "liberty", "affair"

	};
	static String easyCommonWordTable[] = { "that", "their", "when", "how", "what", "where", "why", "who", "which",
			"our", "have", "make", "take", "order", "could", "would", "should", "done", "your", "her", "him", "they",
			"been", "this", "okay", "yeah", "them", "be", "there", "and", "for", "not", "with", "but", "from", "say",
			"she", "will", "out", "about", "can", "like", "time", "just", "know", "people", "into", "year", "good",
			"some", "see", "other", "than", "then", "look", "only", "come", "over", "think", "also", "back", "after",
			"use", "two", "work", "first", "way", "well", "even", "new", "want", "because", "any", "give", "day",
			"most" };

	blankedSmiData(long time, String text) {
		super(time, text);
		this.blankPos = 0;
		this.blankLength = 0;
		this.smiCount = 0xFFFFFFF;
		this.correctAnswer = false;
	}

	public void setBlank(int count, boolean wordNotSentence, boolean easy, int maxBlank, int frequency) {
		int freq, randNum; /* adjusted frequency */
		int cnt = 0, startWordCnt = 0;

		if (this.smiCount == count)
			return;
		this.smiCount = count;

		/* frequency handling first */
		freq = frequency / 10;
		if (freq <= 0)
			return;

		randNum = (int) (Math.random() * 10);

		if (randNum > freq)
			return;

		if (wordNotSentence == true) {
			int wordCnt = 0;
			String modifiedText;

			/* Filtering */
			if (this.text.contains("&nbsp") == true) {
				return;
			}

			/* Pre-processing the text */
			modifiedText = this.text.replace(".", " ");
			modifiedText = modifiedText.replace("-", " ");
			modifiedText = modifiedText.replace("_", " ");
			modifiedText = modifiedText.replace(",", " ");
			modifiedText = modifiedText.replace("?", " ");
			modifiedText = modifiedText.replace("+", " ");
			modifiedText = modifiedText.replace("=", " ");
			modifiedText = modifiedText.replace("{", " ");
			modifiedText = modifiedText.replace("}", " ");
			modifiedText = modifiedText.replace("[", " ");
			modifiedText = modifiedText.replace("]", " ");
			modifiedText = modifiedText.replace(":", " ");
			modifiedText = modifiedText.replace(";", " ");
			modifiedText = modifiedText.replace("`", " ");
			modifiedText = modifiedText.replace("~", " ");
			modifiedText = modifiedText.replace("|", " ");
			modifiedText = modifiedText.replace("!", " ");
			modifiedText = modifiedText.replace("@", " ");
			modifiedText = modifiedText.replace("#", " ");
			modifiedText = modifiedText.replace("&", " ");
			modifiedText = modifiedText.replace("*", " ");
			modifiedText = modifiedText.replace("%", " ");
			modifiedText = modifiedText.replace("$", " ");
			modifiedText = modifiedText.replace("'", " ");
			modifiedText = modifiedText.replace("<br>", " ");
			modifiedText = modifiedText.trim();

			// Log.w("setBlank","modifiedText1="+modifiedText);
			String[] arr = modifiedText.split(" ");

			// Log.w("setBlank","length="+arr.length);

			// some kind of random effect
			if (arr.length > 10) {
				randNum = (int) (Math.random() * 10);
				if ((randNum % 2) == 1)
					startWordCnt = arr.length / 2;
				else
					startWordCnt = 0;
				// Log.w("setBlank","startWordCnt="+startWordCnt);
			}

			for (wordCnt = startWordCnt; wordCnt < arr.length; wordCnt++) {
				if (arr[wordCnt].length() < 4) {
					continue;
				}
				if (arr[wordCnt].contains("'") || arr[wordCnt].contains("`")) {
					continue;
				}
				// skip HTML macros
				if (arr[wordCnt].equalsIgnoreCase("<font") == true) {
					// Log.w("setBlank","Process HTML font macro");
					while (wordCnt < (arr.length - 1)) {
						wordCnt++;
						if (arr[wordCnt].contains(">") == true) {
							wordCnt++;
							break;
						}
					}
					if (wordCnt >= arr.length)
						break;
				}

				if (easy == true) {
					// Log.w("setBlank","word="+arr[wordCnt]);
					if (arr[wordCnt].length() > 4) {
						for (cnt = 0; cnt < easyWordTable.length; cnt++) {
							// Log.w("setBlank","easyWordTable
							// word="+easyWordTable[cnt]);

							if (easyWordTable[cnt].equalsIgnoreCase(arr[wordCnt]) == true) {
								break;
							}
						}
						if (cnt < easyWordTable.length) {
							// Log.w("setBlank","Easy
							// word="+easyWordTable[cnt]);
							break;
						}
					} else if (arr[wordCnt].length() == 4)// let's treat 4
															// characters-word
															// as easy word ;>
					{
						randNum = (int) (Math.random() * 10);
						if ((randNum % 4) == 1) // 25% probability
							break;
						;
						for (cnt = 0; cnt < easyCommonWordTable.length; cnt++) // process
																				// too
																				// many
																				// common
																				// words
						{
							if (easyCommonWordTable[cnt].equalsIgnoreCase(arr[wordCnt]) == true) {
								break;
							}
						}
						if (cnt == easyCommonWordTable.length)
							break;
					}

				} else {
					// Log.w("setBlank","word="+arr[wordCnt]);
					if (arr[wordCnt].length() < 5) // let's treat more than 5
													// characters-word can be
													// difficult word at least
													// ;>
						continue;
					for (cnt = 0; cnt < easyWordTable.length; cnt++) {
						if (easyWordTable[cnt].equalsIgnoreCase(arr[wordCnt]) == true) {
							break;
						}
					}

					if (cnt == easyWordTable.length) {
						// Log.w("setBlank","Difficult word="+arr[wordCnt]);
						break;
					}
				}
			}
			// wordCnt = 0;
			// Log.w("setBlank","wordCnt="+wordCnt);
			if (wordCnt >= arr.length) // No word to meet the given condition
				return;

			if (this.text.contains(arr[wordCnt]) == false) // let's skip parsing
															// problem
				return;

			this.blankPos = this.text.indexOf(arr[wordCnt]);
			// Log.w("setBlank","blankPos="+this.blankPos);
			this.blankLength = arr[wordCnt].length();
			// Log.w("setBlank","blankLength="+this.blankLength);

			// Log.w("setBlank","Word="+arr[wordCnt]);

		} else // Sentence
		{
			int sentenceCnt = 0;
			String modifiedText;

			/* Filtering */
			if (this.text.contains("&nbsp") == true) {
				return;
			}

			modifiedText = this.text.replace("-", "");
			modifiedText = modifiedText.replace(",", " ");
			modifiedText = modifiedText.replace("<br>", "#");
			modifiedText = modifiedText.replace("?", "#");
			modifiedText = modifiedText.replace("!", "#");
			modifiedText = modifiedText.replace(".", "#");
			modifiedText = modifiedText.trim();
			// Log.w("setBlank","modifiedText="+modifiedText);
			String[] arr = modifiedText.split("#");
			// Log.w("setBlank","arr length="+arr.length);
			for (sentenceCnt = 0; sentenceCnt < arr.length; sentenceCnt++) {
				if (arr[sentenceCnt].length() <= maxBlank && arr[sentenceCnt].length() > 3)
					break;
			}

			if (sentenceCnt >= arr.length) // No sentence to meet the given
											// condition
				return;

			arr[sentenceCnt] = arr[sentenceCnt].replace("#", "");

			this.blankPos = this.text.indexOf(arr[sentenceCnt]);
			if (this.blankPos < 0) {
				this.blankPos = 0;
				return;
			}
			// Log.w("setBlank","blankPos="+this.blankPos);
			this.blankLength = arr[sentenceCnt].length();
			// Log.w("setBlank","blankLength="+this.blankLength);

			// Log.w("setBlank","Sentence="+arr[sentenceCnt]);
		}

	}
}

public class video extends Activity {
	private VideoView videoView;
	private TextView subtitle;
	private TextView score;
	private ArrayList<blankedSmiData> parsedSmi;
	private boolean useSmi;
	private boolean useWeb;
	private int countSmi;
	private String path;
	private Uri iPath;
	private File smiFile;
	private URL iSmiPath;
	private EditText smiblank;
	boolean skipPressed;
	boolean showAllPressed;
	boolean repeatPressed;
	int prevCountSmi = 0xffffff;
	int blankHappend;
	int blankHit;
	int vidStartPos;
	Toast mToast = null;
	Toast m2Toast = null;
	InputMethodManager mImm;
	boolean wordNotSentence = true;
	int maxblank = 10;
	boolean easyWord = true;
	int frequency = 50;
	airplane progress;

	SoundPool sp;
	int Sound_R, Sound_F, Sound_Skip;

	@SuppressLint("ShowToast")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoview);

		Intent intent = getIntent();
		if (intent.hasExtra("FilePath")) {
			path = intent.getStringExtra("FilePath");
			useWeb = false;
		} else {
			if (intent.getData().getScheme().equals("http")) {
				iPath = intent.getData();
				useWeb = true;
			} else {
				path = intent.getData().getPath();
				useWeb = false;
			}
		}
		progress = (airplane) findViewById(R.id.airplane);
		DBHandler dbhandler = DBHandler.open(this);
		dbhandler.setCurrentMedia(path);
		Cursor cursor = dbhandler.settings();

		if (cursor.getCount() > 0) {
			if (cursor.getString(1).equals("word"))
				wordNotSentence = true;
			else
				wordNotSentence = false;
			maxblank = (Integer.parseInt(cursor.getString(2))) * 5;
			if (cursor.getString(3).equals("300"))
				easyWord = true;
			else
				easyWord = false;

			frequency = Integer.parseInt(cursor.getString(4));
		}
		dbhandler.close();

		Log.w("Video onCreate", "wordNotSentence=" + wordNotSentence);
		Log.w("Video onCreate", "maxblank=" + maxblank);
		Log.w("Video onCreate", "easyWord=" + easyWord);
		Log.w("Video onCreate", "frequency=" + frequency);

		videoView = (VideoView) findViewById(R.id.videoView1);
		subtitle = (TextView) findViewById(R.id.subtitle);
		smiblank = (EditText) findViewById(R.id.smiBlank);
		smiblank.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		score = (TextView) findViewById(R.id.score);

		mToast = Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT);
		m2Toast = Toast.makeText(this, "Lost Score!", Toast.LENGTH_SHORT);
		mImm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

		Sound_R = sp.load(this, R.raw.button_r, 1);
		Sound_F = sp.load(this, R.raw.button_f, 1);
		Sound_Skip = sp.load(this, R.raw.button_skip, 1);

		if (useWeb == false) {
			String smiPath = path.substring(0, path.lastIndexOf(".")) + ".smi";
			smiFile = new File(smiPath);

			if (smiFile.isFile() && smiFile.canRead()) {
				useSmi = true;
			} else {
				useSmi = false;
			}
		} else {
			String temp = iPath.toString().substring(0, iPath.toString().lastIndexOf(".")) + ".smi";
			try {
				iSmiPath = new URL(temp);
				useSmi = true;
			} catch (MalformedURLException e) {
				useSmi = false;
			}
		}

		parsedSmi = new ArrayList<blankedSmiData>();
		try {
			BufferedReader in;
			if (useWeb == false) {
				in = new BufferedReader(
						new InputStreamReader(new FileInputStream(new File(smiFile.toString())), "MS949"));
			} else {
				in = new BufferedReader(new InputStreamReader(iSmiPath.openStream(), "MS949"));
			}

			String s;
			long time = -1;
			String text = null;
			boolean smistart = false;

			while ((s = in.readLine()) != null) {
				if (s.contains("<SYNC")) {
					smistart = true;
					if (time != -1) {
						parsedSmi.add(new blankedSmiData(time, text));
					}
					time = Integer.parseInt(s.substring(s.indexOf("=") + 1, s.indexOf(">")));
					text = s.substring(s.indexOf(">") + 1, s.length());
					text = text.substring(text.indexOf(">") + 1, text.length());
				} else {
					if (smistart == true) {
						text += s;
					}
				}
			}

			if (smistart == false) {
				useSmi = false;
			}
			in.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}

		subtitle.setText("");
		score.setText("");

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(videoView);

		videoView.setMediaController(mediaController);
		if (useWeb == false) {
			videoView.setVideoPath(path);
		} else {
			videoView.setVideoURI(iPath);
		}
		videoView.requestFocus();

		blankHappend = 0;
		blankHit = 0;
		vidStartPos = 0;
		DBHandler dbhandler2 = DBHandler.open(this);
		Cursor cursor2 = dbhandler2.selectBlank(path);

		if (cursor2.getCount() > 0) {
			blankHappend = Integer.parseInt(cursor2.getString(2));
			blankHit = Integer.parseInt(cursor2.getString(3));
			vidStartPos = Integer.parseInt(cursor2.getString(4));
		}
		dbhandler2.close();

		videoView.seekTo((int) (vidStartPos));
		videoView.start();

		if (useSmi == true) {
			new Thread(new Runnable() {
				public void run() {
					boolean blankOccured = false;
					boolean videoPaused = false;
					long startTime = 0, stopTime = 0;
					int tempSmiCnt;

					try {
						while (true) {
							Thread.sleep(300);
							if (processBlank() == 1) {
								if (blankOccured == false) {
									prevCountSmi = countSmi;
									blankHappend++; // increase total number of
													// blank
									startTime = videoView.getCurrentPosition();
								}
								blankOccured = true;
							}

							myHandler.sendMessage(myHandler.obtainMessage());

							if (blankOccured == true && prevCountSmi < countSmi) {
								// Log.w("THREAD","Paused video");

								// show blanked text for user
								tempSmiCnt = countSmi;
								countSmi = prevCountSmi;
								myHandler.sendMessage(myHandler.obtainMessage());

								if (videoPaused == false) {
									boolean wordHit = false;
									String blankedWords;

									skipPressed = false;
									showAllPressed = false;
									repeatPressed = false;

									stopTime = videoView.getCurrentPosition();
									videoView.pause();
									videoPaused = true;

									// compare input with blanked text
									blankedWords = parsedSmi.get(prevCountSmi).gettext().substring(
											parsedSmi.get(prevCountSmi).blankPos, parsedSmi.get(prevCountSmi).blankPos
													+ parsedSmi.get(prevCountSmi).blankLength);
									Log.w("THREAD", "blankedWords=" + blankedWords);

									while (true) {
										if (blankedWords.equalsIgnoreCase(smiblank.getText().toString()) == true) {
											wordHit = true;
											blankHit++;
											Log.w("THREAD", "Word OK");

											mImm.hideSoftInputFromWindow(smiblank.getWindowToken(), 0);

											mToast.show();
											sp.play(Sound_R, 1, 1, 0, 0, 1);
											break;
										}
										if (skipPressed) {

											break;
										} else if (showAllPressed) {
											// Log.w("THREAD","Show All
											// Pressed");
											break;
										} else if (repeatPressed) {
											long duration;
											long adjustedStartTime;

											// Log.w("THREAD","Smi START
											// time="+parsedSmi.get(prevCountSmi).gettime());
											// Log.w("THREAD","STOP="+parsedSmi.get(prevCountSmi+1).gettime());

											// Log.w("THREAD","Measured START
											// time="+startTime);
											// Log.w("THREAD","STOP
											// time="+stopTime);

											if (parsedSmi.get(prevCountSmi).gettime() < startTime)
												adjustedStartTime = parsedSmi.get(prevCountSmi).gettime();
											else
												adjustedStartTime = startTime;
											if (adjustedStartTime > 500)
												adjustedStartTime = adjustedStartTime - 500;
											duration = stopTime - adjustedStartTime + 500;
											// Log.w("THREAD","Adjusted START
											// time="+adjustedStartTime);
											// Log.w("THREAD","duration="+duration);

											videoView.seekTo((int) (adjustedStartTime));
											Thread.sleep(200);
											videoView.start();
											Thread.sleep(duration);
											videoView.pause();
											repeatPressed = false;
										}

										Thread.sleep(200);
									}
									// Log.w("THREAD","PPT2");
									if (showAllPressed) {
										// show full text
										showAllHandler.sendMessage(showAllHandler.obtainMessage());
									}
									// Log.w("THREAD","AFTER showAllPressed");
									// wait util skip pressed
									while (skipPressed == false && wordHit == false) {

										if (repeatPressed) {
											long duration;
											long adjustedStartTime;

											if (parsedSmi.get(prevCountSmi).gettime() < startTime)
												adjustedStartTime = parsedSmi.get(prevCountSmi).gettime();
											else
												adjustedStartTime = startTime;

											if (adjustedStartTime > 500)
												adjustedStartTime = adjustedStartTime - 500;
											duration = stopTime - adjustedStartTime + 500;

											videoView.seekTo((int) (adjustedStartTime));
											Thread.sleep(200);
											videoView.start();
											Thread.sleep(duration);
											videoView.pause();
											repeatPressed = false;
										}
										Thread.sleep(100);
									}

									countSmi = tempSmiCnt; // restore the
															// current SMI count

									// store result
									if (wordHit == true) {
										parsedSmi.get(prevCountSmi).correctAnswer = true;
										wordHit = false;
									}

									// Log.w("THREAD","SCORE==> Total
									// blank="+blankHappend+" Hit="+blankHit);

									vidStartPos = videoView.getCurrentPosition();
									if (vidStartPos == 0 && blankHappend > 1) // the
																				// case
																				// which
																				// app
																				// gets
																				// the
																				// wrong
																				// video
																				// position
																				// due
																				// to
																				// some
																				// reason(e.g.
																				// quit
																				// by
																				// Home
																				// button)
									{
										vidStartPos = (int) stopTime;
										videoView.seekTo((int) (vidStartPos));
									}

									insertBlank(blankHappend, blankHit, vidStartPos);

									// Log.w("THREAD","Resume
									// video="+vidStartPos);
									videoView.start();
									videoPaused = false;

								}
								blankOccured = false;
							}
						}
					} catch (Throwable t) {
						// Exit Thread
					}
				}

			}).start();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1: // skip
			skipPressed = true;
			m2Toast.show();
			sp.play(Sound_Skip, 1, 1, 0, 0, 1);
			Log.w("Button", "SKIP");
			mImm.hideSoftInputFromWindow(smiblank.getWindowToken(), 0);
			break;
		case R.id.button2: // show all
			showAllPressed = true;
			sp.play(Sound_F, 1, 1, 0, 0, 1);
			Log.w("Button", "Show All");
			mImm.hideSoftInputFromWindow(smiblank.getWindowToken(), 0);
			break;
		case R.id.button3: // repeat
			repeatPressed = true;
			Log.w("Button", "Repeat");
			mImm.hideSoftInputFromWindow(smiblank.getWindowToken(), 0);
			break;
		case R.id.button4: // summary
			Log.w("Button", "summary");
			videoView.stopPlayback();
			videoView.clearFocus();
			finish();
			Intent i = new Intent(getApplicationContext(), SummaryActivity.class);
			i.putExtra("FilePath", path.toString());
			startActivity(i);
			break;
		// case R.id.video_back_button:
		// Log.w("Button", "back");
		// finish();
		// break;
		}
	}

	@SuppressLint("HandlerLeak")
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {

			int blankLength = 0;

			// countSmi = getSyncIndex(videoView.getCurrentPosition());

			score.setText("Total:" + blankHappend + " / Hit=" + blankHit);

			blankLength = parsedSmi.get(countSmi).blankLength;

			// Log.w("Handler","countSmi="+countSmi);
			// Log.w("Handler","pos="+parsedSmi.get(countSmi).blankPos);
			// Log.w("Handler","length="+blankLength);
			// Log.w("Handler","fullText="+parsedSmi.get(countSmi).gettext());

			if (blankLength <= 0) {
				subtitle.setText(Html.fromHtml(parsedSmi.get(countSmi).gettext()));
			} else {
				String blankedText = null;
				String blankedWords = null;
				String blank = "";
				int i;

				blankedWords = parsedSmi.get(countSmi).gettext().substring(parsedSmi.get(countSmi).blankPos,
						parsedSmi.get(countSmi).blankPos + blankLength);

				// Log.w("Handler","blankedWords="+blankedWords);
				for (i = 0; i < blankLength; i++)
					blank = blank + "_";

				blankedText = parsedSmi.get(countSmi).gettext().replace(blankedWords, blank);
				subtitle.setText(Html.fromHtml(blankedText));
				smiblank.setText("");
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler showAllHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Log.w("showAllHandler","Text="+parsedSmi.get(prevCountSmi).gettext());
			subtitle.setText(Html.fromHtml(parsedSmi.get(prevCountSmi).gettext()));
		}
	};

	public int processBlank() {
		countSmi = getSyncIndex(videoView.getCurrentPosition());
		progress.move(parsedSmi.size(), countSmi);
		/* Blank processing */
		parsedSmi.get(countSmi).setBlank(countSmi, wordNotSentence, easyWord, maxblank, frequency);
		if (parsedSmi.get(countSmi).blankLength > 0)
			return 1;
		return 0;
	}

	public int getSyncIndex(long playTime) {
		int l = 0, m, h = parsedSmi.size();

		while (l <= h) {
			m = (l + h) / 2;
			if (parsedSmi.get(m).gettime() <= playTime && playTime < parsedSmi.get(m + 1).gettime()) {
				return m;
			}
			if (playTime > parsedSmi.get(m + 1).gettime()) {
				l = m + 1;
			} else {
				h = m - 1;
			}
		}
		return 0;
	}

	public int insertBlank(int blankHappend, int blankHit, int vidStartPos) {
		DBHandler dbhandler = DBHandler.open(this);
		long cnt = dbhandler.insertBlank(path, blankHappend, blankHit, vidStartPos);
		if (cnt == -1) {
			Log.w("DB", "fail to save");
		} else {
			Log.w("DB", "save successfully");
		}
		dbhandler.close();
		return 0;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			videoView.stopPlayback();
			videoView.clearFocus();
			finish();
		}
		return false;
	}
}

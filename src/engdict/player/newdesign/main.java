package engdict.player.newdesign;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

class ListData {
	int type;
	String name;
	long size;

	ListData(int type, String name, long size) {
		this.type = type;
		this.name = name;
		this.size = size;
	}

	public int gettype() {
		return type;
	}

	public String getname() {
		return name;
	}

	public long getsize() {
		return size;
	}
}

@SuppressLint({ "UseValueOf", "InlinedApi", "NewApi", "InflateParams" })
public class main extends Activity {
	private TextView dirText;
	private ArrayList<ListData> arrayList;
	private ListView fileList;
	private LinearLayout main_layout;
	private GroupAdapter adapter;
	private String nPath;
	private ArrayList<String> PathList_prev;
	private ArrayList<String> PathList_next;
	private TextView empty_textview;
	private TextView empty_subtext;

	// private View nextBtn;
	// private View prevBtn;
	// private View upBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		// nPath = new String("/storage/extSdCard/Data/");
		// TODO: destoryed with file directory path
		nPath = new String("/storage/sdcard0/engdict/");
		PathList_prev = new ArrayList<String>();
		PathList_next = new ArrayList<String>();

		makeFileList();
		// TODO: destoryed with file directory path
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.main);
		makeFileList();
	}

	public void makeFileList() {
		fileList = (ListView) findViewById(R.id.listView1);
		dirText = (TextView) findViewById(R.id.dirText);
		main_layout = (LinearLayout) findViewById(R.id.main_layout);
		empty_textview = (TextView) findViewById(R.id.emptyText);
		empty_subtext = (TextView) findViewById(R.id.empty_subText);
		// TODO: destoryed with file directory path

		Button option_menu = (Button) findViewById(R.id.title_button_more);
		option_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openOptionsMenu();
			}
		});

		String path = null;
		String ext = Environment.getExternalStorageState();
		if (ext.equals(Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
		} else {
			path = Environment.MEDIA_UNMOUNTED;
		}

		File dir = new File(path + "/Download/");
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		nPath = path + "/Download/";

		final Builder errorDlg = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("에러")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		PathList_prev.add(nPath);
		updateFileList(nPath);
		// TODO: destoryed with file directory path

		fileList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListData item = arrayList.get(position);
				if (item.gettype() == 0) {
					File dir = new File(nPath + item.getname() + "/");
					if (dir.isDirectory() && dir.canRead()) {
						PathList_prev.add(nPath);
						PathList_next.clear();
						nPath = dir.getAbsolutePath().toString();
						if (!nPath.endsWith("/")) {
							nPath += "/";
						}
						updateFileList(nPath);
					} else {
						errorDlg.setMessage(
								"Please, Connect to USB with Desktop and put the MP4 file and SMI file in the Download folder.\n"
										+ "(USB를 연결해서 Download 폴더에  MP4 영상파일과 함께 Smi영어 자막 파일을 넣어주세요.)"
										+ "(영상이 없으시면  샘플 다운로드 페이지에서 받으세요.)")
								.show();
					}
				} else if (item.gettype() == 1) {
					File file = new File(nPath + item.getname());
					if (file.isFile() && file.canRead()) {
						Intent intent = new Intent(main.this, video.class);
						intent.putExtra("FilePath", file.toString());
						startActivity(intent);
					} else {
						errorDlg.setMessage(
								"Please, Connect to USB with Desktop and put the MP4 file and SMI file in the Download folder.\n"
										+ "(Download 폴더에  Mp4 파일과 SMI의 파일을  확인하세요.)"
										+ "(영상이 없으시면  샘플 다운로드 페이지에서 받으세요.)")
								.show();
					}
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		final Builder virsionDlg = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle(R.string.how_to_use)
				.setMessage("\n" + "- Step 1 : 샘플영상을 다운받거나 자기가 원하는 영상의 SMI파일과 MP4파일을 Download폴더에 넣는다.\n"
						+ "[first, Download to sample Movie or put in the download folder with MP4 and SMI file ]\n"
						+ "\n" + "- Step 2 : 난의도를 설정한다.\n" + "[second, Set the level and frequency ]\n" + "\n"
						+ "- Step 3 : 영상을 재생하여 플레이를 시작한다.\n" + "[Let`s play the engdict palyer !!]\n" + "\n"
						+ "- Notice -" + "\n"
						+ "[영상 파일과 자막파일은 같은 이름으로 해야 인식이 가능합니다. 또한 영상파일 형식은 MP4형식을 자막파일은 SMI형식을 권장합니다.]\n"
						+ "[Please, check to the same as MP4 and SMI file name. we recomand to file of MP4 and SMI format movies]"
						+ "\n")

				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setNegativeButton("Sample", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse("http://m.blog.naver.com/asjgi/220011155455"));
						startActivity(intent);
					}
				});

		final Builder infoDlg = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle("Information")
				.setMessage("[EngDictlayer]\n" + "- Made by : David.Ahn.\n" + "- Designed by : HeyumUX\n"
						+ "- Contact me : asjgi@kaist.ac.kr\n" + "- Blog : http://blog.naver.com/asjgi\n"
						+ "- Prologue: 저로 인해  좀더 살기 좋은  세상이 되었으면 좋겠습니다.")
				.setPositiveButton("어플 평가 하기", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(
								Uri.parse("https://play.google.com/store/apps/details?id=engdict.player.notFree"));
						startActivity(intent);
					}
				});

		int id = item.getItemId();
		switch (id) {
		case R.id.Setting_menu:
			Intent i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			break;

		case R.id.howto_use_menu:
			virsionDlg.show();
			break;

		case R.id.info_menu:
			infoDlg.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateFileList(String sPath) {
		String ext = Environment.getExternalStorageState();
		String path = null;
		ListData temp;
		int type;
		long size = 0;
		int count = 0;
		String name;

		final Builder virsionDlg = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle("Empty space");

		if (sPath == null) {
			if (ext.equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
			} else {
				path = Environment.MEDIA_UNMOUNTED;
			}
		} else {
			path = sPath;
		}

		// TODO: destoryed with file directory path
		dirText.setText("Vedio File Path: " + path);
		nPath = path;

		File files = new File(path);
		if (files.listFiles().length == 0) {
			path = "/storage/sdcard0/Download/";
			dirText.setText(path);
			nPath = path;
			files = new File(path);

			// virsionDlg.setMessage("Please, Connect USB and into Download
			// folder into the Eng MP4 file and SMI file.\n"
			// + "(USB를 연결해서 Download 폴더에 영상파일과 함께 Smi 영어 자막 파일을 넣어주세요.)" +
			// "(영상이 없으시면 커뮤니티를 참조해 주세요.)");
			// virsionDlg.show();
			updateFileList(null);
			Toast.makeText(this, "no files", Toast.LENGTH_SHORT).show();
			ImageView empty_imgview = new ImageView(this);
			empty_imgview.setImageResource(R.drawable.empty_img_view);
			main_layout.addView(empty_imgview);
			empty_textview.setText("You don`t have Video.");
			empty_imgview.getLayoutDirection();
			empty_textview.setTextSize(20);
			empty_textview.setGravity(Gravity.CENTER);
			empty_textview.setPadding(0, 750, 0, 50);
			empty_subtext.setText("원하시는 영상을 넣거나 샘플영상을 다운받아서 EngDict를 시작해 보세요.");
			empty_subtext.setTextSize(15);
			empty_subtext.setGravity(Gravity.CENTER);
			empty_imgview.setPadding(0, 30, 0, 30);
			count++;
		}

		arrayList = new ArrayList<ListData>();

		if (files.listFiles().length > 0) {
			for (File file : files.listFiles()) {
				name = file.getName();
				if (file.getName().endsWith(".mpg") || file.getName().endsWith(".avi")
						|| file.getName().endsWith(".wmv") || file.getName().endsWith(".asf")
						|| file.getName().endsWith(".mp4") || file.getName().endsWith(".mkv")
						|| file.getName().endsWith(".m4v") || file.getName().endsWith(".3gp")) {
					type = 1;
					size = file.length();
					temp = new ListData(type, name, size);
					arrayList.add(temp);
					count++;
				}
			}
			if (count == 0) {
				ImageView empty_imgview = new ImageView(this);
				empty_imgview.setImageResource(R.drawable.empty_img_view);
				main_layout.addView(empty_imgview);
				empty_textview.setText("You don`t have Video.");

				empty_imgview.getLayoutDirection();
				empty_textview.setTextSize(20);
				empty_textview.setGravity(Gravity.CENTER);
				empty_textview.setPadding(0, 750, 0, 50);
				empty_subtext.setText("원하시는 영상을 넣거나 샘플영상을\n" + "다운받아서 EngDict를 시작해 보세요.");
				empty_subtext.setTextSize(15);
				empty_subtext.setGravity(Gravity.CENTER);
				empty_imgview.setPadding(0, 30, 0, 30);
				count++;
			}
			// Toast.makeText(this, "these are files",
			// Toast.LENGTH_SHORT).show();
		}

		adapter = new GroupAdapter(this, R.layout.listview, arrayList);
		fileList.setAdapter(adapter);

	}

	private class GroupAdapter extends ArrayAdapter<Object> {
		private ArrayList<ListData> item;
		private ListData temp;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public GroupAdapter(Context ctx, int resourceID, ArrayList item) {
			super(ctx, resourceID, item);

			this.item = item;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listview, null);
			}
			temp = item.get(position);

			Bitmap image = ThumbnailUtils.createVideoThumbnail(nPath + temp.getname(),
					android.provider.MediaStore.Video.Thumbnails.MICRO_KIND);

			if (temp == null) {

			}
			if (temp != null) {
				ImageView icon = (ImageView) v.findViewById(R.id.imageView1);
				TextView name = (TextView) v.findViewById(R.id.textView1);
				TextView size = (TextView) v.findViewById(R.id.textView2);
				switch (temp.gettype()) {
				case 0:
					icon.setImageResource(R.drawable.folder_horizontal);
					break;
				case 1:
					icon.setImageBitmap(image);
					break;
				case 2:
					icon.setImageResource(R.drawable.application_blue);
					break;
				default:
					icon.setImageResource(R.drawable.document);
					break;
				}
				name.setText(temp.getname());
				if (temp.gettype() == 0) {
					size.setText("(폴더)");
				} else {
					size.setText("(" + byteTranslater(temp.getsize()) + ")");
				}
			}
			return v;
		}
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
	 * inflater = getMenuInflater(); inflater.inflate(R.menu.main, menu); return
	 * true; }
	 */

	@SuppressLint("UseValueOf")
	public static String byteTranslater(long size) {
		NumberFormat nf = NumberFormat.getIntegerInstance();
		java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
		int intSize = 0;
		int kbyteSize = 1024;
		double doubleSize = 0;
		String returnSize = null;

		if (size >= (1000 * 1024 * 1024)) {
			intSize = new Long(size / (1000 * 1024 * 1024)).intValue();
			return nf.format(intSize) + "GB";
		} else if (size > (kbyteSize * 1024)) {
			intSize = (int) (((double) size) / ((double) (kbyteSize * 1024)) * 100);
			doubleSize = (double) (((double) intSize) / 100);
			returnSize = df.format(doubleSize);
			if (returnSize.lastIndexOf(".") != -1) {
				if ("00".equals(returnSize.substring(returnSize.length() - 2, returnSize.length()))) {
					returnSize = returnSize.substring(0, returnSize.lastIndexOf("."));
				}
			}
			return returnSize + "MB";
		} else if (size > kbyteSize) {
			intSize = new Long(size / kbyteSize).intValue();
			return nf.format(intSize) + "KB";
		} else {
			return "1KB";
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			String alertTitle = getResources().getString(R.string.app_close);
			String buttonMessage = getResources().getString(R.string.alert_msg_exit);
			String buttonYes = getResources().getString(R.string.button_yes);
			String buttonNo = getResources().getString(R.string.button_no);

			new AlertDialog.Builder(main.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle(alertTitle)
					.setMessage(buttonMessage).setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {

						@SuppressLint("NewApi")
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							moveTaskToBack(false);
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse("https://m.facebook.com/306473089529441"));
							startActivity(intent);

						}
					}).setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							moveTaskToBack(true);
							finish();
							android.os.Process.killProcess(android.os.Process.myPid());
						}

					}).show();
		}

		return true;
	}
}

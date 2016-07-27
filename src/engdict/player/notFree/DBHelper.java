package engdict.player.notFree;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public DBHelper(Context context) {
		super(context, "dictation.db", null, 11);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String media_table = "CREATE TABLE media ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "				
				+ "media_name TEXT NOT NULL);";
		db.execSQL(media_table);
		
		String mileage_table = "CREATE TABLE mileage ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "				
				+ "mileage INTEGER NOT NULL);";
		db.execSQL(mileage_table);
		
		String blank_table = "CREATE TABLE blank ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "				
				+ "media_name TEXT, "
				+ "blankHappend INTEGER, "
				+ "blankHit INTEGER, "
				+ "vidStartPos INTEGER);";
		db.execSQL(blank_table);
		
		String blanks_table = "CREATE TABLE blanks ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "				
				+ "media_name TEXT NOT NULL, "
				+ "position INTEGER, "
				+ "total_number INTEGER, "
				+ "correct_number INTEGER);";
		db.execSQL(blanks_table);

		String settings_table = "CREATE TABLE settings ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "				
				+ "type TEXT NOT NULL, "
				+ "maxblank INTEGER NOT NULL, "                
				+ "voca TEXT NOT NULL, "                
				+ "frequency INTEGER NOT NULL);";
		db.execSQL(settings_table);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS media");		
		db.execSQL("DROP TABLE IF EXISTS mileage");		
		db.execSQL("DROP TABLE IF EXISTS blank");		
		db.execSQL("DROP TABLE IF EXISTS blanks");
		db.execSQL("DROP TABLE IF EXISTS settings");		
		onCreate(db);
	}
}

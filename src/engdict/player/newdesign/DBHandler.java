package engdict.player.newdesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class DBHandler {
	private DBHelper helper;
	private SQLiteDatabase db;

	private DBHandler(Context ctx) {
		this.helper = new DBHelper(ctx);
		this.db = helper.getWritableDatabase();
	}

	public static DBHandler open(Context ctx) throws SQLException {
		DBHandler handler = new DBHandler(ctx);

		return handler;
	}

	public void close() {
		helper.close();
	}

	public long insert(String media_name) {
		ContentValues values = new ContentValues();
		values.put("media_name", media_name);
		values.put("total_number", "3");
		values.put("correct_number", "2");
		// db.execSQL("INSERT INTO blanks(media_name, position, total_number, correct_number) VALUE('modern_family', 10, 13, 7);");
		Log.e("DB", "insert :" + media_name);
		return db.insert("blanks", null, values);
	}

	public long insertBlank(String media_name, int blankHappend, int blankHit,
			int vidStartPos) {
		ContentValues values = new ContentValues();
		values.put("media_name", media_name);
		values.put("blankHappend", blankHappend);
		values.put("blankHit", blankHit);
		values.put("vidStartPos", vidStartPos);
		Log.e("DB", "insertBlank - blankHappend :" + blankHappend);
		return db.insert("blank", null, values);
	}

	public long insertSettings(String type, String maxblank, String voca,
			String frequency) {
		ContentValues values = new ContentValues();
		values.put("type", type);
		values.put("maxblank", maxblank);
		values.put("voca", voca);
		values.put("frequency", frequency);
		Log.e("DB", "insertSettings");
		return db.insert("settings", null, values);
	}

	public long insertMileage(int mileage) {
		ContentValues values = new ContentValues();
		values.put("mileage", mileage);
		Log.e("DB", "insertMileage - mileage :" + mileage);
		return db.insert("mileage", null, values);
	}

	public long deleteMileage() throws SQLException {
		return db.delete("mileage", null, null);
	}

	public Cursor selectMileage() throws SQLException {
		Cursor cursor = db.query(true, "mileage", new String[] { "_id",
				"mileage" }, null, null, null, null, "_id DESC", null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public Cursor select(int id) throws SQLException {
		Cursor cursor = db.query(true, "blanks", new String[] { "_id",
				"media_name" }, "_id" + "=" + id, null, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public Cursor selectBlank(String path) throws SQLException {
		Cursor cursor = db.query(true, "blank", new String[] { "_id",
				"media_name", "blankHappend", "blankHit", "vidStartPos" },
				"media_name" + "='" + path + "'", null, null, null, "_id DESC",
				null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public long setCurrentMedia(String path) {
		ContentValues values = new ContentValues();
		values.put("media_name", path);
		Log.e("DB", "setCurrentMedia " + path);
		return db.insert("media", null, values);
	}

	public String getMediaNameOfLastBlank() throws SQLException {
		String path = "none";
		Cursor cursor = db.query(true, "media", new String[] { "_id",
				"media_name" },
				null, null, null, null, "_id DESC", null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		if (cursor.getCount() > 0) {
			path = cursor.getString(1);
		}
		Log.e("DB", "getMediaNameOfLastBlank :" + path);
		return path;
	}

	public long deleteBlank(String path) throws SQLException {
		return db.delete("blank", "media_name" + "='" + path + "'", null);
	}

	public Cursor summary() throws SQLException {
		Cursor cursor = db.query(true, "blanks", new String[] { "_id",
				"media_name", "total_number", "correct_number" }, null, null,
				null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}

	public Cursor settings() throws SQLException {
		Cursor cursor = db.query(true, "settings", new String[] { "_id",
				"type", "maxblank", "voca", "frequency" }, null, null, null,
				null, "_id DESC", null);
		if (cursor != null) {
			cursor.moveToFirst();
		}

		return cursor;
	}
}

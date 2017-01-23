package com.lg.database;

import java.io.IOException;

import com.lg.sentimentalanalysis.MethodCreator;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author Johnnatan Messias
 */
public class DatabaseAdapter {
	private static String TAG = DatabaseAdapter.class.getSimpleName();
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;
	private static DatabaseAdapter databaseAdapter;

	public static DatabaseAdapter getInstance() {
		if (databaseAdapter == null)
			databaseAdapter = new DatabaseAdapter();
		return databaseAdapter;
	}

	private DatabaseAdapter() {
		this.context = MethodCreator.context;
		this.dbHelper = new DatabaseHelper(context);
		createDatabase();
	}

	public DatabaseAdapter createDatabase() throws SQLException {
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			Log.e(TAG, e.toString() + "Unable To Create Database");
			throw new Error("Unable To Create Databse");
		}
		return this;
	}

	public DatabaseAdapter open() throws SQLException {
		try {
			dbHelper.openDataBase();
			dbHelper.close();
			db = dbHelper.getReadableDatabase();
		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;
		}
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public Cursor getData(String tablename, String term) {
		Cursor cursor;
		try {
			String[] args = { term };
			String sql = "SELECT * FROM " + tablename + " WHERE term = ?";
			cursor = db.rawQuery(sql, args);
		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;
		}
		return cursor;
	}

	public Cursor getData(String sql) {
		Cursor cursor;
		try {
			cursor = db.rawQuery(sql, null);
		} catch (SQLException e) {
			Log.e(TAG, e.toString());
			throw e;
		}
		return cursor;
	}
}

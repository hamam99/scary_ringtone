package com.avaloncode.scaryringtone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	MediaPlayer mp;
	int resId;
	String filename;

	// title in list view
	String[] itemname = { "Ghost 1", "Ghost 2", "Ghost 3", "Ghost 4", "Ghost 5" };

	// description in listview
	String[] desc = { "Wolf Sound", "Door Open", "Girl Scream 1", "Ghost Sound 1", "Unknown Sound 1" };

	// id for the image
	int[] imgid = { R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4, R.drawable.pic5 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);
		MenuAdapter adapter = new MenuAdapter(this, itemname, imgid, desc);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long arg3) {

				// get the title in current postion
				String selectedItem = (String) getListAdapter().getItem(position);

				switch (selectedItem) {
				case "Ghost 1":
					filename = "ghost1.mp3";
					resId = R.raw.ghost1;

					break;

				case "Ghost 2":
					filename = "ghost2.mp3";
					resId = R.raw.ghost2;

					break;

				case "Ghost 3":
					filename = "ghost3.mp3";
					resId = R.raw.ghost3;

					break;

				case "Ghost 4":
					filename = "ghost4.mp3";
					resId = R.raw.ghost4;

					break;

				case "Ghost 5":
					filename = "ghost5.mp3";
					resId = R.raw.ghost5;
					break;

				}
				setFileName(filename);
				saveas(RingtoneManager.TYPE_RINGTONE);
				Toast.makeText(getApplicationContext(), selectedItem + " become default ringtone!", Toast.LENGTH_SHORT)
						.show();
				return true;
			}
		});

	}

	// setter

	public void setFileName(String filename) {
		this.filename = filename;
	}

	public String getFileName() {
		return filename;
	}

	@Override
	public void onListItemClick(ListView lv, View view, int position, long imgid) {

		String selectedItem = (String) getListAdapter().getItem(position);
		Toast.makeText(getApplicationContext(), selectedItem, 10).show();

		switch (selectedItem) {
		case "Ghost 1":
			resId = R.raw.ghost1;
			break;

		case "Ghost 2":
			resId = R.raw.ghost2;
			break;

		case "Ghost 3":
			resId = R.raw.ghost3;
			break;

		case "Ghost 4":
			resId = R.raw.ghost4;
			break;

		case "Ghost 5":
			resId = R.raw.ghost5;
			break;
		}

		// release
		if (mp != null)
			mp.release();

		// create new sound
		mp = MediaPlayer.create(this, resId);
		mp.start();

	}

	public boolean saveas(int type) {
		filename = getFileName();

		byte[] buffer = null;
		InputStream fIn = getBaseContext().getResources().openRawResource(resId);
		int size = 0;

		try {
			size = fIn.available();
			buffer = new byte[size];
			fIn.read(buffer);
			fIn.close();
		} catch (IOException e) {
			return false;
		}

		String path = Environment.getExternalStorageDirectory().getPath() + "/media/audio/ringtones/";

		boolean exists = (new File(path).exists());
		if (!exists) {
			new File(path).mkdirs();
		}

		FileOutputStream save;
		try {
			save = new FileOutputStream(path + filename);
			save.write(buffer);
			save.flush();
			save.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + filename)));

		File k = new File(path, filename);
		ContentValues cv = new ContentValues();
		cv.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
		cv.put(MediaStore.MediaColumns.TITLE, filename);
		cv.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");

		// this medthod allow to change notication and alarm tone also .
		// just pass corresponding type as parameter
		cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		cv.put(MediaStore.Audio.Media.IS_ALARM, true);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
		Uri newUri = MainActivity.this.getContentResolver().insert(uri, cv);
		RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this, type, newUri);

		// insert into db
		this.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()), cv);
		return true;

	}

	@Override
	public void onStart() {
		if (mp != null)
			mp.release();

		super.onStart();
	}

	@Override
	protected void onDestroy() {
		if (mp != null) {
			mp.release();
		}

		finish();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}

}
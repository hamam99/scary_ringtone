package com.avaloncode.scaryringtone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] itemname;
	private final int[] imgid;
	private final String[] desc;

	public MenuAdapter(Activity context, String[] itemname, int[] imgid2, String[] desc) {
		super(context, R.layout.menu_list, itemname);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.itemname = itemname;
		this.imgid = imgid2;
		this.desc = desc;

	}

	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.menu_list, null, true);

		TextView txtTitle = (TextView) rowView.findViewById(R.id.menuItem);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.menuIcon);
		TextView extratxt = (TextView) rowView.findViewById(R.id.menuTextView1);

		txtTitle.setText(itemname[position]);
		imageView.setImageResource(imgid[position]);
		extratxt.setText("Description : " + desc[position]);

		return rowView;
	}

}

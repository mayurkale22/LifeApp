package com.career;

import com.szugyi.circlemenu.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Inserts Record values into ListView items.
 *
 *
 */
public class MenuAdapter extends ArrayAdapter<String> {
	private Activity context;
	private String[] names;
	
	/**
	 * Object holding View elements belonging to a ListView item
	 *
	 */
	static class ViewHolder {
		public TextView menu_name;
	}
	
	public MenuAdapter(Activity context, String[] names) {
		super(context, R.layout.menu_list_item, names);
		this.context = context;
		this.names = names;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		// Only inflate row once
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.menu_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			// Store TextView instances in ViewHolder
			viewHolder.menu_name = (TextView) rowView.findViewById(R.id.menu_name);

			// Save ViewHolder in a Tag
			rowView.setTag(viewHolder);
		}
		
		// Retrieve ViewHolder from Tag
		ViewHolder holder = (ViewHolder) rowView.getTag();
		// Load value into TextView
		String menu_item = names[position];
		holder.menu_name.setText(menu_item);

		return rowView;
	}	
}

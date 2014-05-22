package com.career;

import java.util.List;

import com.career.db.Record;
import com.career.db.RecordHolder;
import com.szugyi.circlemenu.R;

import android.app.Activity;
import android.util.Log;
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
public class CalendarAdapter extends ArrayAdapter<Record> {
	private Activity context;
	private List<Record> records;
	
	/**
	 * Object holding View elements belonging to a ListView item
	 *
	 */
	static class ViewHolder {
		public TextView type_date;
		public TextView job_co;
		public TextView recr_co;
		public TextView comments;
		public TextView goal_loc;
		public TextView goal_title;
		public TextView goal_desc;
		public TextView cal_id;
	}
	
	public CalendarAdapter(Activity context, List<Record> records) {
		super(context, R.layout.calendar_list_item, records);
		this.context = context;
		this.records = records;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d("Navin-CalAdap", "getView started");
		View rowView = convertView;
		
		// Only inflate row once
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.calendar_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			
			// Store TextView instances in ViewHolder
			viewHolder.type_date = (TextView) rowView.findViewById(R.id.cal_type_date_time);
			Record record = records.get(position);
			viewHolder.cal_id = (TextView) rowView.findViewById(R.id.cal_id);				
			
			System.out.println("abvoe"+record.getRecordType().equals(RecordHolder.RECORD_TYPE_GOAL));
			if(record.getRecordType().equals(RecordHolder.RECORD_TYPE_GOAL))
			{				
				viewHolder.goal_loc = (TextView) rowView.findViewById(R.id.cal_job_company);
				viewHolder.goal_title = (TextView) rowView.findViewById(R.id.cal_recr_company);				
				viewHolder.goal_desc = (TextView) rowView.findViewById(R.id.cal_comments);
				
			}
			else
			{				
				viewHolder.job_co = (TextView) rowView.findViewById(R.id.cal_job_company);
				viewHolder.recr_co = (TextView) rowView.findViewById(R.id.cal_recr_company);
				viewHolder.comments = (TextView) rowView.findViewById(R.id.cal_comments);				
			}
			
			// Save ViewHolder in a Tag
			rowView.setTag(viewHolder);
		}
		
		// Retrieve ViewHolder from Tag
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		System.out.println(holder.toString());
		System.out.println(rowView.getTag());
		
		// Load values into TextViews from the Record
		Record record = records.get(position);
		
		System.out.println(record.getId());
		
		RecordHolder rHolder = new RecordHolder(record);
		String typeDate = record.getRecordType()+": "+rHolder.getDateTimeScreen();
		
		holder.type_date.setText(typeDate);
					
		System.out.print(RecordHolder.RECORD_TYPE_GOAL);
		System.out.println(record.getRecordType());
		System.out.println("a"+record.getRecordType().equals(RecordHolder.RECORD_TYPE_GOAL));
		
		String calid = "Cal Id: "+record.getId();										
		holder.cal_id.setText(calid);	
			
		
		if(record.getRecordType().equals(RecordHolder.RECORD_TYPE_GOAL)){	
			
			
			//holder.goal_loc = new TextView(context);
			String goalTitle = "Goal Title: "+record.getGoalTitle();										
			holder.goal_loc.setText(goalTitle);						
			
			//holder.goal_title = new TextView(context);
			String goalLoc = "Location: "+record.getGoalLocation();								
			holder.goal_title.setText(goalLoc);			
			
			//holder.goal_desc = new TextView(context);
			String goalDesc = "Description: "+record.getGoalDesc();			
			holder.goal_desc.setText(goalDesc);									
		}
		else{	
			String jobCo = "Job: "+record.getJobTitle()+" at "+record.getJobCompany();			
			holder.job_co.setText(jobCo);
			
			String recrCo = "Recruiter: "+record.getRecruiterName()+" of "+record.getRecruiterCompany();			
			holder.recr_co.setText(recrCo);
				
			String comm = "Comments: "+record.getComments();
			holder.comments.setText(comm);
		}
		
		Log.d("Navin-CalAdapter", "getView completed");
		return rowView;
	}	
}

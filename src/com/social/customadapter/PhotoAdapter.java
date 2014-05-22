
package com.social.customadapter;

import java.util.List;

import com.szugyi.circlemenu.R;
import org.brickred.socialauth.Photo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class PhotoAdapter extends ArrayAdapter<Photo> {

	// SocialAuth Components
	List<Photo> photos;

	// Android Components
	LayoutInflater mInflater;
	Context context;

	// Other Components
	PhotoHolder photoHolder;
	ImageLoader imageLoader;

	public PhotoAdapter(Context context, int textViewResourceId, List<Photo> photos) {
		super(context, textViewResourceId);
		this.photos = photos;
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {

		final Photo bean = photos.get(position);

		if (row == null) {
			row = mInflater.inflate(R.layout.photoitem, null);

			photoHolder = new PhotoHolder();

			photoHolder.photoThumbnail = (ImageView) row.findViewById(R.id.photoThumbnail);
			row.setTag(photoHolder);
		} else {
			photoHolder = (PhotoHolder) row.getTag();
		}

		if (bean.getTitle() != null)
			Log.d("LifeView", "Photo Title = " + bean.getTitle());

		imageLoader.DisplayImage(bean.getSmallImage(), photoHolder.photoThumbnail);

		return row;
	}

	static class PhotoHolder {
		ImageView photoThumbnail;
	}
}


package com.social.customadapter;

import java.util.List;

import com.szugyi.circlemenu.R;
import org.brickred.socialauth.Album;
import org.brickred.socialauth.Photo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AlbumsAdapter extends ArrayAdapter<Album> {

	// SocialAuth Components
	List<Album> albums;
	List<Photo> photos;

	// Android Components
	Context context;
	LayoutInflater mInflater;

	// Other Components
	AlbumHolder albumHolder;
	ImageLoader imageLoader;

	public AlbumsAdapter(Context context, int textViewResourceId, List<Album> albums) {
		super(context, textViewResourceId);
		this.albums = albums;
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return albums.size();
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {

		final Album bean = albums.get(position);

		if (row == null) {
			row = mInflater.inflate(R.layout.albumitem, parent, false);

			albumHolder = new AlbumHolder();

			albumHolder.coverImage = (ImageView) row.findViewById(R.id.coveralbum);

			albumHolder.albumName = (TextView) row.findViewById(R.id.albumname);
			albumHolder.photoCount = (TextView) row.findViewById(R.id.photocount);

			row.setTag(albumHolder);
		} else {
			albumHolder = (AlbumHolder) row.getTag();
		}
		Log.d("LifeView ", "Cover Photo = " + bean.getCoverPhoto());

		imageLoader.DisplayImage(bean.getCoverPhoto(), albumHolder.coverImage);

		Log.d("LifeView ", "Album Name = " + bean.getName());
		albumHolder.albumName.setText(bean.getName());

		Log.d("LifeView ", "Photos Count = " + bean.getPhotosCount());
		albumHolder.photoCount.setText(String.valueOf(bean.getPhotosCount()) + " Photos");

		return row;
	}

	static class AlbumHolder {
		ImageView coverImage;
		TextView albumName;
		TextView photoCount;
	}
}

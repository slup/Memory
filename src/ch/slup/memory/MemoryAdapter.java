package ch.slup.memory;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemoryAdapter extends BaseAdapter {

	private Context mContext;
	private List<MemoryItem> mMemoryItems;
	private Bitmap mCoverBitmap;

	public MemoryAdapter(Context context, 
			List<MemoryItem> memoryItems, Bitmap coverImage) {
		mContext = context;
		mMemoryItems = memoryItems;
		
		mCoverBitmap = coverImage;
	}

	@Override
	public int getCount() {
		return mMemoryItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mMemoryItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	holder = new ViewHolder();
        	convertView = LayoutInflater.from(mContext).inflate(R.layout.memory_item, null);
        	holder.image = (ImageView) convertView.findViewById(R.id.memory_item_image);
        	holder.coverImage = (ImageView) convertView.findViewById(R.id.memory_item_cover_image);
        	holder.text = (TextView) convertView.findViewById(R.id.memory_item_text);
        	convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        MemoryItem item = (MemoryItem) getItem(position);
        item.setViewHolder(holder);

        holder.text.setText("" + position);
        holder.image.setImageBitmap(item.getImage());
        holder.coverImage.setImageBitmap(mCoverBitmap);
        return convertView;
	}

	class ViewHolder {
		ImageView image;
		ImageView coverImage;
		TextView text;
	}
}

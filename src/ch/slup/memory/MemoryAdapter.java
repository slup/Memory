package ch.slup.memory;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

public class MemoryAdapter extends BaseAdapter {
    // Debugging
    private static final String TAG = "MemoryAdapter";
    private static final boolean D = true;
	
	private Context mContext;
	private List<MemoryItem> mMemoryItems;
	private Bitmap mCoverBitmap;
	private LayoutParams mLayoutParams;

	public MemoryAdapter(Context context, 
			List<MemoryItem> memoryItems, Bitmap coverImage) {
		mContext = context;
		mMemoryItems = memoryItems;
		
		mCoverBitmap = coverImage;
		
		/*
		int [] colors = new int [coverImage.getWidth() * coverImage.getHeight()];
		
		for (int i = 0; i < colors.length; i++) {
			colors[i] = Color.argb(100, 255, 0, 0);
		}
		
		mCoverBitmap = Bitmap.createBitmap(colors, 
				coverImage.getWidth(), 
				coverImage.getHeight(), 
				Bitmap.Config.ARGB_8888);
		*/
		
		mLayoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.MATCH_PARENT);
		
		
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
		
		/*
		//if (D && (position == 0)) { Log.d(TAG, "getView: position: " + position + ", convertView: " + convertView + ", parent: " + parent); }
		if (D && (position == 0)) { Log.d(TAG, "getView: position: " + position + ", convertView: " + convertView); }
		
		MemoryItem item = mMemoryItems.get(position);
        ViewHolder holder;
        
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	holder = new ViewHolder();
        	convertView = LayoutInflater.from(mContext).inflate(R.layout.memory_item, null);
        	holder.image = (ImageView) convertView.findViewById(R.id.memory_item_image);
        	holder.coverImage = (ImageView) convertView.findViewById(R.id.memory_item_cover_image);
        	convertView.setTag(holder);
        	//item.setViewHolder(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        
        //item.setPosition(position);

        holder.image.setImageBitmap(item.getImage());
        holder.coverImage.setImageBitmap(mCoverBitmap);
        
        if (item.uncovered()) {
        	holder.coverImage.setVisibility(View.INVISIBLE);
        }

        if (D && (position == 0)) { Log.d(TAG, "getView: position: " + position + ", convertView: " + convertView + ", holder: " + holder); }
        
        return convertView;
        */
		/*
        ViewHolder holder;
        ImageSwitcher switcher;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	holder = new ViewHolder();
        	switcher = new ImageSwitcher(mContext);
        	
        	holder.coverImage = new ImageView(mContext);
        	switcher.addView(holder.coverImage, mLayoutParams);
        	
        	holder.image = new ImageView(mContext);
        	switcher.addView(holder.coverImage, mLayoutParams);
        	
        	switcher.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        	switcher = (ImageSwitcher) convertView;
        }
        
        if (D) { Log.d(TAG, "getView: position: " + position + ", convertView: " + convertView + ", parent: " + parent); }
        
        MemoryItem item = (MemoryItem) getItem(position);
        //item.setViewHolder(holder);

        //holder.text.setText("" + position);
        holder.image.setImageBitmap(item.getImage());
        holder.coverImage.setImageBitmap(mCoverBitmap);
		
        switcher.set
        
        return switcher;*/
		
		
		// IMAGESWITCHER SOLUTION
		
		ImageSwitcher switcher;
		MemoryItem item = mMemoryItems.get(position);
		
    	switcher = new ImageSwitcher(mContext);
    	// animates the whole memoryview, not just the switcher :(
    	//switcher.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
    	//switcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
    	
    	ImageView coverImage = new ImageView(mContext);
    	coverImage.setImageBitmap(mCoverBitmap);
    	switcher.addView(coverImage, MemoryItem.MEMORY_COVER_IMAGE_ID, mLayoutParams);
    	
    	ImageView image = new ImageView(mContext);
    	image.setImageBitmap(item.getImage());
    	switcher.addView(image, MemoryItem.MEMORY_IMAGE_ID, mLayoutParams);
		
    	if (item.uncovered()) {
    		switcher.setDisplayedChild(MemoryItem.MEMORY_IMAGE_ID);
    	} else {
    		switcher.setDisplayedChild(MemoryItem.MEMORY_COVER_IMAGE_ID);	
    	}
    	
    	
    	item.setSwitcher(switcher);
    	
    	if (D) { Log.d(TAG, "getView: position: " + position + ", convertView: " + convertView + ", parent: " + parent); }
    	return switcher;
	}
	
	class ViewHolder {
		ImageView image;
		ImageView coverImage;
		
		@Override
		public boolean equals(Object o) {
		     if (this == o) {
		       return true;
		     }
		     if (!(o instanceof ViewHolder)) {
		       return false;
		     }

		     ViewHolder that = (ViewHolder) o;
		     return (coverImage.equals(that.coverImage) &&
		             image.equals(that.image));
		}

		@Override
		public int hashCode() {
		     // Start with a non-zero constant.
		     int result = 17;

		     // Include a hash for each field.
		     result = 31 * result + coverImage.hashCode();
		     result = 31 * result + image.hashCode();
		     return result;
		}
		
		
	}

	public void uncover(int position) {
		mMemoryItems.get(position).uncover();
		notifyDataSetChanged();
	}
	
	public void cover(int position) {
		mMemoryItems.get(position).cover();
		notifyDataSetChanged();
	}
}

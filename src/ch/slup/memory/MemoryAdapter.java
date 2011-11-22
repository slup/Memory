package ch.slup.memory;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
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
		
		ImageSwitcher switcher;
		
		MemoryItem item = mMemoryItems.get(position);
		
		if (null == convertView) {
	    	switcher = new ImageSwitcher(mContext);
	    	// animates the whole memoryview, not just the switcher :(
	    	//switcher.setInAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
	    	//switcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
	    	
	    	ImageView coverImage = new ImageView(mContext);
	    	switcher.addView(coverImage, MemoryItem.MEMORY_COVER_IMAGE_ID, mLayoutParams);
	    	
	    	ImageView image = new ImageView(mContext);
	    	switcher.addView(image, MemoryItem.MEMORY_IMAGE_ID, mLayoutParams);
	    	
		} else {
			switcher = (ImageSwitcher) convertView;
		}
		
		((ImageView)switcher.getChildAt(MemoryItem.MEMORY_IMAGE_ID)).setImageBitmap(item.getImage());
		((ImageView)switcher.getChildAt(MemoryItem.MEMORY_COVER_IMAGE_ID)).setImageBitmap(mCoverBitmap);
		
    	if (item.uncovered()) {
    		switcher.setDisplayedChild(MemoryItem.MEMORY_IMAGE_ID);
    	} else {
    		switcher.setDisplayedChild(MemoryItem.MEMORY_COVER_IMAGE_ID);	
    	}
    	
    	item.setPosition(position);
    	
    	if (D) { Log.d(TAG, "getView: position: " + position + ", convertView: " + convertView + ", parent: " + parent); }
    	
    	return switcher;
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

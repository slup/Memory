package ch.slup.memory;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageSwitcher;
import ch.slup.memory.MemoryAdapter.ViewHolder;

public class MemoryItem {
	
	public static int MEMORY_COVER_IMAGE_ID = 0;
	public static int MEMORY_IMAGE_ID = 1;
	
	private int position;
	private String imageName;
	private Bitmap image;
	private boolean active;
	private boolean uncovered;
	public ViewHolder viewHolder;
	private ImageSwitcher mImageSwitcher;
	
	
	public MemoryItem (String imageName, Bitmap image) {
		this.imageName = imageName;
		this.image = image;
		this.active = true;
		this.uncovered = false;
		this.position = 0;
		this.viewHolder = null;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setPosition(int pos) {
		this.position = pos;
	}
	
	public void setViewHolder(ViewHolder viewHolder) {
		this.viewHolder = viewHolder;
	}
	
	public void hideCover() {//hideCover(View v) {
		//v.findViewById(R.id.memory_item_cover_image).setVisibility(View.INVISIBLE);
		//viewHolder.coverImage.setVisibility(View.INVISIBLE);
		mImageSwitcher.setDisplayedChild(MEMORY_IMAGE_ID);
	}
	
	public void showCover() {//showCover(View v) {
		//v.findViewById(R.id.memory_item_cover_image).setVisibility(View.VISIBLE);
		//viewHolder.coverImage.setVisibility(View.VISIBLE);
		mImageSwitcher.setDisplayedChild(MEMORY_COVER_IMAGE_ID);
	}
	

	public void uncover() {
		uncovered = true;
	}
	
	public void cover() {
		uncovered = false;
	}
	
	public boolean uncovered() {
		return uncovered;
	}
	
	public void pairMatched() {
		active = false;
	}
	
	public boolean active() {
		return active;
	}
	
	public boolean isPair(MemoryItem item) {
	     return (imageName.equals(item.imageName));
	}

	@Override
	public boolean equals(Object o) {
	     if (this == o) {
	       return true;
	     }
	     if (!(o instanceof MemoryItem)) {
	       return false;
	     }

	     MemoryItem that = (MemoryItem) o;
	     return (//position == that.position &&
	    		 imageName.equals(that.imageName) &&
	             image.equals(that.image) &&
	             viewHolder.equals(that.viewHolder));
	}

	@Override
	public int hashCode() {
	     // Start with a non-zero constant.
	     int result = 17;

	     // Include a hash for each field.
	     result = 31 * result + imageName.hashCode();
	     result = 31 * result + image.hashCode();
	     //result = 31 * result + position;
	     result = 31 * result + viewHolder.hashCode();
	     return result;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + imageName;
	}

	public void setSwitcher(ImageSwitcher switcher) {
		// TODO Auto-generated method stub
		mImageSwitcher = switcher;
	}
}

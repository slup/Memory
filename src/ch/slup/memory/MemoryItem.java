package ch.slup.memory;

import android.graphics.Bitmap;
import android.view.View;
import ch.slup.memory.MemoryAdapter.ViewHolder;

public class MemoryItem {
	
	private int position;
	private String imageName;
	private Bitmap image;
	private boolean uncovered;
	public ViewHolder viewHolder;
	
	public MemoryItem (String imageName, Bitmap image) {
		this.imageName = imageName;
		this.image = image;
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
		viewHolder.coverImage.setVisibility(View.INVISIBLE);
	}
	
	public void showCover() {//showCover(View v) {
		//v.findViewById(R.id.memory_item_cover_image).setVisibility(View.VISIBLE);
		viewHolder.coverImage.setVisibility(View.VISIBLE);
	}
	
	public void pairMatched() {
		uncovered = true;
	}
	
	public boolean active() {
		return !uncovered;
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
}

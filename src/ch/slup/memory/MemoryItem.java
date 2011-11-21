package ch.slup.memory;

import android.graphics.Bitmap;
import android.view.View;
import ch.slup.memory.MemoryAdapter.ViewHolder;

public class MemoryItem {
	
	private String imageName;
	private Bitmap image;
	private boolean unCovered;
	public ViewHolder viewHolder;
	
	public MemoryItem (String imageName, Bitmap image) {
		this.imageName = imageName;
		this.image = image;
		this.unCovered = false;
		this.viewHolder = null;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setViewHolder(ViewHolder viewHolder) {
		this.viewHolder = viewHolder;
	}
	
	public void hideCover() {
		viewHolder.coverImage.setVisibility(View.INVISIBLE);
	}
	
	public void showCover() {
		viewHolder.coverImage.setVisibility(View.VISIBLE);
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
	     return (imageName.equals(that.imageName) &&
	             image.equals(that.image));
	}

	@Override
	public int hashCode() {
	     // Start with a non-zero constant.
	     int result = 17;

	     // Include a hash for each field.
	     result = 31 * result + imageName.hashCode();
	     result = 31 * result + image.hashCode();
	     return result;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + imageName;
	}
}

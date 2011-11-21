package ch.slup.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import ch.slup.memory.MemoryAdapter.ViewHolder;

public class MemoryView extends GridView {
	
    // Debugging
    private static final String TAG = "MemoryView";
    private static final boolean D = true;
	
    private enum State {
    	NONE_UNCOVERED, ONE_UNCOVERED,
    }
    
    private State mCurrentState = State.NONE_UNCOVERED;
    private MemoryItem mFirstUncovered = null;
    private View mFirstView = null;
	
	private static String IMAGES_DIR = "memory_images";
	private static String COVER_IMAGE = "cover.jpg";
	private int mImageCount = 10;
	private List<MemoryItem> mImageItems;
	private Handler mHandler;

	public MemoryView(Context context) {
		super(context);
		init();
	}

	public MemoryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MemoryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		
		
		
		mHandler = new Handler(getContext().getMainLooper());
		
		enumerateImages();
		
        setAdapter(new MemoryAdapter(getContext(), mImageItems, loadImage(COVER_IMAGE)));

        setOnItemClickListener(mItemClickListener);
	}
	
	private void enumerateImages() {
		AssetManager asm = getContext().getAssets();
		try {
			String [] images = asm.list(IMAGES_DIR);
			
			if (images.length < mImageCount) {
				if (D) { Log.e(TAG, "Not enough images for all fields, reducing fields."); }
			}
			
			/*
			List<String> shuffleNames = new ArrayList<String>(Arrays.asList(images));
			Collections.shuffle(shuffleNames);
			*/
			//List<String> imageNames = new ArrayList<String>(shuffleNames.subList(0, mImageCount));
			
			List<String> imageNames = new ArrayList<String>();
			
			mImageItems = new ArrayList<MemoryItem>();
			
			Random rand = new Random();
			while (imageNames.size() < mImageCount) {
				int index = rand.nextInt(images.length);
				String oneImage = IMAGES_DIR + "/" + images[index];
				if (!imageNames.contains(oneImage)) {
					imageNames.add(oneImage);
					mImageItems.add(new MemoryItem(images[index], loadImage(oneImage)));
				}
			}

			List<MemoryItem> doubleItems = new ArrayList<MemoryItem>(mImageItems);
			mImageItems.addAll(doubleItems);
			Collections.shuffle(mImageItems);
			
			/*
			List<String> doubleImages = new ArrayList<String>(imageNames);
			imageNames.addAll(doubleImages);
			Collections.shuffle(imageNames);*/
			
			//for (String path : )
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Bitmap loadImage(String path) {
		try {
			return BitmapFactory.decodeStream(getContext().getAssets().open(path));
		} catch (IOException e) {
			return null;
		}
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			MemoryAdapter.ViewHolder holder = (ViewHolder) v.getTag();
			holder.coverImage.setVisibility(INVISIBLE);
			
			if (State.NONE_UNCOVERED == mCurrentState) {
				mCurrentState = State.ONE_UNCOVERED;
				mFirstUncovered = (MemoryItem) parent.getAdapter().getItem(position);
				mFirstView = mFirstUncovered.viewHolder.coverImage;
			} else if (State.ONE_UNCOVERED == mCurrentState) {
				mCurrentState = State.NONE_UNCOVERED;
				
				final MemoryItem secondUncovered = (MemoryItem) parent.getAdapter().getItem(position);
				final View secondView = secondUncovered.viewHolder.coverImage;
				
				if (!secondUncovered.equals(mFirstUncovered)) {
					// cover again after 500 ms
					
					/*
					synchronized (MemoryView.this) {
						try {
							wait(500);
						} catch (InterruptedException e) {
							// if interrupted, just resume with covering
						}	
					}
					
					mFirstUncovered.showCover();
					secondUncovered.showCover();
					
					*/
					
					boolean didPost = mHandler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							
							if (D) { Log.e(TAG, "postRunnable: running"); }
							
							//mFirstUncovered.showCover();
							//mFirstUncovered = null;
							//secondUncovered.showCover();
							mFirstView.setVisibility(VISIBLE);
							mFirstView = null;
							secondView.setVisibility(VISIBLE);
						}
					}, 500);
					
					if (D) { Log.e(TAG, "postRunnable: " + didPost); }
					
					/*
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								wait(500);
							} catch (InterruptedException e) {
								// if interrupted, just resume with covering
							}
							
							
							
						}
					}).start();*/
				} else {
					 // leave both uncovered
				}
			}
			
            Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
		}
	};
}

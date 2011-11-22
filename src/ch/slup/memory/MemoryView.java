package ch.slup.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class MemoryView extends GridView {
	
    // Debugging
    private static final String TAG = "MemoryView";
    private static final boolean D = true;
	
    private enum State {
    	NONE_UNCOVERED, ONE_UNCOVERED, TWO_UNCOVERED,
    }
    
    private static final int UNCOVER_TIME = 500; //ms
    
    private State mCurrentState = State.NONE_UNCOVERED;
    private static MemoryItem mFirstUncovered = null;
    private static MemoryItem mSecondUncovered = null;
    private View mFirstView = null;
	
	private static String IMAGES_DIR = "memory_images";
	private static String COVER_IMAGE = "cover.jpg";
	private int mImageCount = 10;
	private List<MemoryItem> mImageItems;
	private int mPairsUncovered;
	private MemoryAdapter mMemoryAdapter;

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
		
		mPairsUncovered = 0;
		
		enumerateImages();
		
		mMemoryAdapter = new MemoryAdapter(getContext(), mImageItems, loadImage(COVER_IMAGE));
		
        setAdapter(mMemoryAdapter);

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
					mImageItems.add(new MemoryItem(images[index], loadImage(oneImage)));
				}
			}

			//List<MemoryItem> doubleItems = new ArrayList<MemoryItem>(mImageItems);
			//mImageItems.addAll(doubleItems);
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


	static View first = null;
	static View second = null;
	
	static int firstPosition = -1;
	static int secondPosition = -1;
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			
			
			MemoryItem item = (MemoryItem) parent.getAdapter().getItem(position);
			
			if (D) { Log.d(TAG, "onItemClick: position: " + position + ", id: " + id + ", v: " + view + ", viewHolder: " + item.viewHolder); }
			
			
			if (State.NONE_UNCOVERED == mCurrentState
					&& item.active()) {
				mCurrentState = State.ONE_UNCOVERED;
				firstPosition = position;
				mMemoryAdapter.uncover(firstPosition);
				mFirstUncovered = item;
				//mFirstUncovered.hideCover();
			} else if (State.ONE_UNCOVERED == mCurrentState 
					&& item.active() 
					&& position != firstPosition
					/*&& !item.equals(mFirstUncovered)*/) {
				mCurrentState = State.TWO_UNCOVERED;
				
				secondPosition = position;
				mMemoryAdapter.uncover(secondPosition);
				mSecondUncovered = item;
				//mSecondUncovered.hideCover();

				//if (secondPosition != firstPosition) {
				if (!mSecondUncovered.isPair(mFirstUncovered)) {
					// cover again after 500 ms
					
					boolean didPost = postDelayed(new Runnable() {
						
						@Override
						public void run() {
							
							if (D) { Log.d(TAG, "postRunnable: running"); }
							
							synchronized (MemoryView.this) {
								/*
								mFirstUncovered.showCover();
								mFirstUncovered = null;
								mSecondUncovered.showCover();
								mSecondUncovered = null;
								*/
								mMemoryAdapter.cover(firstPosition);
								firstPosition = -1;
								mFirstUncovered = null;
								mMemoryAdapter.cover(secondPosition);
								secondPosition = -1;
								mSecondUncovered = null;
								
								mCurrentState = State.NONE_UNCOVERED;	
							}
						}
					}, UNCOVER_TIME);
					
					if (D) { Log.d(TAG, "postRunnable: " + didPost); }
					
				} else {
					 // leave both uncovered
					/*
					mFirstUncovered.pairMatched();
					mFirstUncovered = null;
					mSecondUncovered.pairMatched();
					mSecondUncovered = null;
					*/
					firstPosition = -1;
					mFirstUncovered.pairMatched();
					mFirstUncovered = null;
					secondPosition = -1;
					mSecondUncovered.pairMatched();
					mSecondUncovered = null;
					
					mCurrentState = State.NONE_UNCOVERED;
					
					mPairsUncovered++;
				}
			} else if (!(State.TWO_UNCOVERED == mCurrentState)) {
				if (-1 != firstPosition) {
					mMemoryAdapter.cover(firstPosition);
					firstPosition = -1;
					mFirstUncovered = null;
				}

				/*if (null != mFirstUncovered) {
					mFirstUncovered.showCover();
					mFirstUncovered = null;
				}*/
				
				mCurrentState = State.NONE_UNCOVERED;
			}
			
			if (D) { Log.d(TAG, "onItemClick: first: " + mFirstUncovered + ", second: " + mSecondUncovered); }
		}
	};

		/*
		@Override
		public void onItemClick(AdapterView<?> parent, final View v, int position, long id) {
			//MemoryAdapter.ViewHolder holder = (ViewHolder) v.getTag();
			//holder.coverImage.setVisibility(INVISIBLE);
			
			MemoryItem item = (MemoryItem) parent.getAdapter().getItem(position);
			
			if (D) { Log.d(TAG, "onItemClick: position: " + position + ", id: " + id + ", v: " + v + ", viewHolder: " + item.viewHolder); }
			
			
			if (State.NONE_UNCOVERED == mCurrentState
					&& item.active()) {
				mCurrentState = State.ONE_UNCOVERED;
				//first = v.findViewById(R.id.memory_item_cover_image);
				//first.setVisibility(INVISIBLE);
				mFirstUncovered = item;
				mFirstUncovered.hideCover();
				//mFirstView = mFirstUncovered.viewHolder.coverImage;
			} else if (State.ONE_UNCOVERED == mCurrentState 
					&& item.active() 
					&& !item.equals(mFirstUncovered)) {
				mCurrentState = State.TWO_UNCOVERED;
				//second = v.findViewById(R.id.memory_item_cover_image);
				//second.setVisibility(INVISIBLE);
				
				mSecondUncovered = item;
				mSecondUncovered.hideCover();

				if (!mSecondUncovered.isPair(mFirstUncovered)) {
					// cover again after 500 ms
					
					
					boolean didPost = postDelayed(new Runnable() {
						
						@Override
						public void run() {
							
							if (D) { Log.d(TAG, "postRunnable: running"); }
							
							synchronized (MemoryView.this) {
								mFirstUncovered.showCover();
								mFirstUncovered = null;
								mSecondUncovered.showCover();
								mSecondUncovered = null;
								
								mCurrentState = State.NONE_UNCOVERED;	
							}
						}
					}, UNCOVER_TIME);
					
					if (D) { Log.d(TAG, "postRunnable: " + didPost); }
					
				} else {
					 // leave both uncovered
					mFirstUncovered.pairMatched();
					mFirstUncovered = null;
					mSecondUncovered.pairMatched();
					mSecondUncovered = null;
					mCurrentState = State.NONE_UNCOVERED;
					mPairsUncovered++;
				}
			} else if (!(State.TWO_UNCOVERED == mCurrentState)) {
				if (null != mFirstUncovered) {
					mFirstUncovered.showCover();
					mFirstUncovered = null;
				}
				
				mCurrentState = State.NONE_UNCOVERED;
			}
			
			if (D) { Log.d(TAG, "onItemClick: first: " + mFirstUncovered + ", second: " + mSecondUncovered); }
            //Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
		}
		*/
}

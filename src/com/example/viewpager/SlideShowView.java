package com.example.viewpager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class SlideShowView extends FrameLayout {

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private List<ImageView> imageViewList = new ArrayList<ImageView>();

	private List<View> dotViewList = new ArrayList<View>();

	private ViewPager viewPager;

	private int currentItem = 0;

	private ScheduledExecutorService scheduledExecutorService;

	private String[] imageUrls;

	private int imageCount;
	
	private boolean autoPlay;

	private int delay;

	private int period;

	private static final int AutoPlay_ItemChanged = 0x1;

	private static final int LoadFinished = 0x2;

	private static final String TAG = SlideShowView.class.getSimpleName();

	ImageLoadingListener loadingListener;
	

	static class SlideShowHandler extends Handler {

		WeakReference<SlideShowView> view;

		public SlideShowHandler(SlideShowView view) {
			this.view = new WeakReference<SlideShowView>(view);
		}

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case AutoPlay_ItemChanged:

				super.handleMessage(msg);
				int curItem = (Integer) msg.obj;
				view.get().scrollToItem(curItem);

				break;

			case LoadFinished:

				super.handleMessage(msg);

				view.get().initUI();
				break;

			}

		}
	}

	SlideShowHandler handler;

	private void scrollToItem(int item) {
		if (viewPager != null) {
			viewPager.setCurrentItem(currentItem);
		}
	}

	private void initUI() {
		Log.i(TAG, "complete all loading and init UI");

		LayoutInflater.from(this.getContext()).inflate(
				R.layout.layout_slideshow, this, true);

		LinearLayout dotLayout = (LinearLayout) this
				.findViewById(R.id.dotLayout);

		dotLayout.removeAllViews();

		for (int i = 0; i < imageUrls.length; i++) {
			ImageView dotView = new ImageView(this.getContext());

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			params.leftMargin = 4;
			params.rightMargin = 4;

			dotLayout.addView(dotView, params);
			dotViewList.add(dotView);
		}

		viewPager = (ViewPager) this.findViewById(R.id.viewPager);
		viewPager.setFocusable(true);

		viewPager.setAdapter(new SlideShowPageAdapter());
		viewPager.addOnPageChangeListener(new SlideShowPageChangeListener());

	}

	private class SlideShowPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		public Object instantiateItem(View container, int position) {
			ImageView imageView = imageViewList.get(position);

			((ViewPager) container).addView(imageView);

			return imageView;
		}

		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViewList.get(position));
		}

	}

	private class SlideShowPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			switch (state) {
			/*
			 * Indicates that the pager is in and idle,settled state, The
			 * current page is fully in view and no animation is in progress
			 */
			case ViewPager.SCROLL_STATE_IDLE:

				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !autoPlay) {
					viewPager.setCurrentItem(0);
				}

				else if (viewPager.getCurrentItem() == 0 && !autoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}

				break;

			case ViewPager.SCROLL_STATE_DRAGGING:
				autoPlay = false;
				break;

			case ViewPager.SCROLL_STATE_SETTLING:
				autoPlay = true;
				break;
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub

			currentItem = position;

			for (int i = 0; i < dotViewList.size(); i++) {
				if (i == position) {
					dotViewList.get(i).setBackgroundResource(R.drawable.red);
				} else {
					dotViewList.get(i).setBackgroundResource(
							R.drawable.whitedot);
				}
			}
		}

	}

	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "run in slideshow task " + currentItem);
			currentItem = (currentItem + 1) % imageCount;
			Message msg = handler.obtainMessage();
			msg.what = AutoPlay_ItemChanged;
			msg.obj = currentItem;
			handler.sendMessage(msg);
		}

	}
	
	
	

	class SlideShowImageLoadingListener implements ImageLoadingListener {

		private int loadCount = 0;

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// TODO Auto-generated method stub
			Log.i(TAG, "start loading " + imageUri);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			Log.i(TAG, "fail loading " + imageUri);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// TODO Auto-generated method stub
			Log.i(TAG, "complete loading " + imageUri);
			ImageView imageView = new ImageView(SlideShowView.this.getContext());
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setImageBitmap(loadedImage);
			imageViewList.add(imageView);

			Log.i(TAG, "current loadCount " + loadCount + " imageCount "
					+ imageCount);
			synchronized (SlideShowView.this) {
				if (++loadCount == imageCount) {
					Message msg = handler.obtainMessage();
					msg.what = LoadFinished;
					handler.sendMessage(msg);
					Log.i(TAG, "send load finished message ");
				}
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			Log.i(TAG, "cancel loading " + imageUri);
		}

	}

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.handler = new SlideShowHandler(this);
		loadSlideShowParameter();
		configImageLoader();
		if (autoPlay) {
			startPlay();
		}
	}

	private void configImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getContext()).build();
		imageLoader.init(config);
		loadingListener = new SlideShowImageLoadingListener();
		for (int i = 0; i < imageUrls.length; i++) {
			imageLoader.loadImage(imageUrls[i], loadingListener);
		}
	}

	private void startPlay() {

		Log.i(TAG, " startPlay ");

		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(),
				delay, period, TimeUnit.SECONDS);

	}

	private void loadSlideShowParameter() {

		Resources res = this.getContext().getResources();
		imageUrls = res.getStringArray(R.array.urls);
		imageCount = imageUrls.length;
		String autoPlayParameter = res.getString(R.string.autoPlay);
		autoPlay = Boolean.parseBoolean(autoPlayParameter);
		delay = Integer.parseInt(res.getString(R.string.delay));
		period = Integer.parseInt(res.getString(R.string.period));
	}

}

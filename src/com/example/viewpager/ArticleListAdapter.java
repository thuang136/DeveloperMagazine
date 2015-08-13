package com.example.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;

import com.example.domain.ArticleItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ArticleListAdapter extends BaseAdapter {

	private List<ArticleItem> articleItems;

	private LayoutInflater layoutInflater;

	private Context context;

	private static final int SLIDESHOW_TYPE = 0;
	private static final int ITEM_TYPE = 1;
	private static final int TYPE_COUNT = 2;

	public ArticleListAdapter(Context context, List<ArticleItem> items) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		this.articleItems = items;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return articleItems.size();
	}

	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	public int getItemViewType(int position) {
		return position == 0 ? SLIDESHOW_TYPE : ITEM_TYPE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return articleItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			if (position > 0) {
				convertView = this.layoutInflater.inflate(
						R.layout.layout_article1, null);
				holder.titleView = (TextView) convertView
						.findViewById(R.id.tilte);
				holder.hostView = (TextView) convertView
						.findViewById(R.id.host);
				holder.imgView = (ImageView) convertView
						.findViewById(R.id.articleIcon);
			} else {

				convertView = new SlideShowView(this.context);
				holder.slideView = (SlideShowView) convertView;

			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position > 0) {
			ArticleItem item = this.articleItems.get(position);

			holder.titleView.setText(item.getTitle());
			holder.hostView.setText(item.getHost());
			ImageLoader.getInstance().displayImage(item.getIconUrl(),
					holder.imgView);
		} else {
			convertView = (SlideShowView) holder.slideView;
		}

		return convertView;
	}

	public final class ViewHolder {
		public TextView titleView;
		public TextView hostView;
		public ImageView imgView;
		public SlideShowView slideView;
	}

}

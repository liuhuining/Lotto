package com.qf.liuyong.lotto_android.view.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.qf.liuyong.lotto_android.R;
import com.qf.liuyong.lotto_android.model.bean.HomePageResult;

import java.util.List;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class IndustryFilterContentListAdapter extends BaseAdapter {

    private List<HomePageResult.IndustryListBean> industryListBeen;
    private int selectIndex = -1;

    public IndustryFilterContentListAdapter(List<HomePageResult.IndustryListBean> industryListBeen) {
        this.industryListBeen = industryListBeen;
    }

    @Override
    public int getCount() {
        return industryListBeen == null ? 0 : industryListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return industryListBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.filter_item_layout, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.textView.setText(industryListBeen.get(position).getName());
        if (position == selectIndex) {
            vh.checkBox.setChecked(true);
            vh.imageView.setVisibility(View.VISIBLE);
            vh.textView.setTextColor(Color.parseColor("#ff7300"));
        } else {
            vh.checkBox.setChecked(false);
            vh.imageView.setVisibility(View.INVISIBLE);
            vh.textView.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }

    public void clickItem(int position) {
        selectIndex = position;
        changeText.changeIndustryFilteText(industryListBeen.get(position).getName(), industryListBeen.get(position).getType());
        notifyDataSetChanged();
    }

    public interface ChangeText {
        void changeIndustryFilteText(String string, String type);
    }

    private ChangeText changeText;

    public void setChangeTextListner(ChangeText changeText) {
        this.changeText = changeText;
    }

    private class ViewHolder {
        public final View root;
        public final TextView textView;
        public final CheckBox checkBox;
        public final ImageView imageView;

        public ViewHolder(View root) {
            this.root = root;
            textView = (TextView) root.findViewById(R.id.textview);
            checkBox = (CheckBox) root.findViewById(R.id.checkbox);
            imageView = (ImageView) root.findViewById(R.id.imageview);
        }
    }
}

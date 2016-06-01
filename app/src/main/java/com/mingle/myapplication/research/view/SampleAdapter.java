package com.mingle.myapplication.research.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mingle.myapplication.R;
import com.mingle.myapplication.research.model.SampleData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by ee5188 on 2016-05-31.
 */
public class SampleAdapter  extends BaseAdapter {
    protected Context mContext = null;
    private LayoutInflater mInflater = null;
    private ArrayList<SampleData> mDataList = null;
    private DisplayImageOptions options;

    public SampleAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.eye) // 로딩중에 나타나는 이미지
                .showImageForEmptyUri(R.drawable.eye) // 값이 없을때
                .showImageOnFail(R.drawable.eye) // 에러 났을때 나타나는 이미지
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
    }

    public void setDataList(ArrayList<SampleData> list){
        this.mDataList = list;
    }

    @Override
    public int getCount() {
        if(mDataList == null){
            return 0;
        }
        return mDataList.size();
    }

    @Override
    public SampleData getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        final SampleData infoData = getItem(position);

        if(convertView == null){
            holder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.view_item, parent, false);
            holder.lmageView  =  (com.makeramen.RoundedImageView) convertView.findViewById(R.id.imageView);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.mallName = (TextView) convertView.findViewById(R.id.mallName);
            holder.lprice = (TextView) convertView.findViewById(R.id.lprice);
            holder.link_ImageView = (Button) convertView.findViewById(R.id.linkImageView);
            convertView.setTag(holder);
        }else{
            holder = (ChildHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(infoData.getImage(), holder.lmageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
        holder.title.setText(Html.fromHtml(infoData.getTitle()));
        holder.mallName.setText(infoData.getMallName());
        holder.lprice.setText("최저가:" + infoData.getLprice()+"원");
        holder.link_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoData.getLink()));
                mContext.startActivity(browserIntent);
            }
        });

        return convertView;
    }

    public class ChildHolder{
        com.makeramen.RoundedImageView lmageView = null;
        TextView title = null;
        TextView mallName = null;
        TextView lprice = null;
        Button link_ImageView = null;
    }
}

package com.easemob.easeui.customer.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.activity.DetailActivity;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DensityUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2015/11/25.
 * 用户浏览轨迹自定义消息类型
 */
public class TrackChatRow extends EaseChatRow {

    private ImageView mAvatarView;
    private ImageView mTrackImageView;
    private TextView mTrackTitle;
    private TextView mShopDesc;

    public TrackChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE
                ? R.layout.widget_chat_row_receive_track
                : R.layout.widget_chat_row_send_track, this);
    }

    /**
     * 初始化自定义 ChatRow 控件
     */
    @Override
    protected void onFindViewById() {
        mAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        mTrackImageView = (ImageView) findViewById(R.id.img_track_image);
        mTrackTitle = (TextView) findViewById(R.id.text_track_title);
        mShopDesc = (TextView) findViewById(R.id.text_shop_desc);
    }

    @Override
    protected void onUpdateView() {

    }

    /**
     * 为控件设置内容
     */
    @Override
    protected void onSetUpView() {
        try {
            JSONObject jsonObject = message.getJSONObjectAttribute(CustomerConstants.C_ATTR_KEY_MSGTYPE);
            if (jsonObject.has(CustomerConstants.C_ATTR_TRACK)) {
                JSONObject track = jsonObject.getJSONObject(CustomerConstants.C_ATTR_TRACK);
                String title = track.getString("title");
                String price = track.getString("price");
                String desc = track.getString("desc");
                String imgUrl = track.getString("img_url");
                String itemUrl = track.getString("item_url");
                // 设置商品图片显示
                Picasso.with(context)
                        .load(imgUrl)
                        .resize(DensityUtil.dip2px(context, 120), DensityUtil.dip2px(context, 120))
                        .placeholder(R.mipmap.ic_avatar_01)
                        .into(mTrackImageView);

                mTrackTitle.setText(title);
                mShopDesc.setText(desc);
                this.setTag(itemUrl);
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        context.startActivity(intent);

    }
}

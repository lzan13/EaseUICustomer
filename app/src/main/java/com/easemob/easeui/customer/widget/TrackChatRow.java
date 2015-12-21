package com.easemob.easeui.customer.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.activity.DetailActivity;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.fragment.ChatFragment;
import com.easemob.easeui.customer.util.MLSPUtil;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2015/11/25.
 * 用户轨迹 ChatRow，用户浏览轨迹自定义消息类型，实现了展示用户浏览商品轨迹，
 * 由聊天界面自定义ChatRow提供者中 {@link ChatFragment.CustomChatRowProvider}调用
 */
public class TrackChatRow extends EaseChatRow {

    private ImageView mAvatarView;
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mDescView;
    private TextView mPriceView;

    public TrackChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    /**
     * 根据消息类型填充布局
     */
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
        mImageView = (ImageView) findViewById(R.id.img_image);
        mTitleView = (TextView) findViewById(R.id.text_title);
        mDescView = (TextView) findViewById(R.id.text_desc);
        mPriceView = (TextView) findViewById(R.id.text_price);
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
                // 设置聊天气泡用户头像
                Glide.with(context)
                        .load((String) MLSPUtil.get(context, CustomerConstants.C_USER_KEY_AVATAR, ""))
                        .placeholder(R.mipmap.ic_avatar_02)
                        .into(mAvatarView);
                // 设置商品图片显示
                Glide.with(context)
                        .load(imgUrl)
                        .placeholder(R.mipmap.ic_avatar_01)
                        .into(mImageView);

                mTitleView.setText(title);
                mDescView.setText(desc);
                mPriceView.setText(price);
                this.setTag(itemUrl);
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 聊天气泡点击事件
     */
    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        context.startActivity(intent);

    }
}

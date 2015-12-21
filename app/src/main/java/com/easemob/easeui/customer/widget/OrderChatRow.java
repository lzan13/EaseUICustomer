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
 * 订单类消息ChatRow
 * 由聊天界面自定义ChatRow提供者中 {@link ChatFragment.CustomChatRowProvider}调用
 */
public class OrderChatRow extends EaseChatRow {

    private ImageView mAvatarView;
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mOrderTitleView;
    private TextView mDescView;
    private TextView mPriceView;

    public OrderChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    /**
     * 根据消息类型填充布局
     */
    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE
                ? R.layout.widget_chat_row_receive_order
                : R.layout.widget_chat_row_send_order, this);
    }

    /**
     * 初始化自定义 ChatRow 控件
     */
    @Override
    protected void onFindViewById() {
        mAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        mImageView = (ImageView) findViewById(R.id.img_image);
        mTitleView = (TextView) findViewById(R.id.text_title);
        mOrderTitleView = (TextView) findViewById(R.id.text_order_title);
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
            if (jsonObject.has(CustomerConstants.C_ATTR_ORDER)) {
                JSONObject order = jsonObject.getJSONObject(CustomerConstants.C_ATTR_ORDER);
                String title = order.getString("title");
                String orderTitle = order.getString("order_title");
                String price = order.getString("price");
                String desc = order.getString("desc");
                String imgUrl = order.getString("img_url");
                String itemUrl = order.getString("item_url");
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
                mOrderTitleView.setText(orderTitle);
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
     * TODO 点击事件暂时无效
     */
    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent();
        intent.setClass(context, DetailActivity.class);
        context.startActivity(intent);

    }
}

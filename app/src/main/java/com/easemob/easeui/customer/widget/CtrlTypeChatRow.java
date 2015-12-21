package com.easemob.easeui.customer.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.customer.R;
import com.easemob.easeui.customer.application.CustomerConstants;
import com.easemob.easeui.customer.fragment.ChatFragment;
import com.easemob.easeui.customer.entity.EnquiryEntity;
import com.easemob.easeui.customer.util.MLLog;
import com.easemob.easeui.customer.util.MLSPUtil;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.exceptions.EaseMobException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzan13 on 2015/11/25.
 * 满意度评价类型消息 ChatRow
 * 实现了点击弹出评价Dialog，并发送评价信息
 * 由聊天界面自定义ChatRow提供者中 {@link ChatFragment.CustomChatRowProvider}调用
 */
public class CtrlTypeChatRow extends EaseChatRow {

    // 自定义回调接口
    private CustomerChatRowListener mChatRowListener;
    private EnquiryEntity mEnquiryEntity;

    private View mChatRowView;
    private ImageView mAvatarView;
    private TextView mEnquiryTitleView;
    private TextView mEnquiryDetailView;
    private TextView mEnquirySummaryView;

    public CtrlTypeChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);

    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE
                ? R.layout.widget_chat_row_receive_enquiry
                : R.layout.widget_chat_row_send_enquiry, this);
    }

    @Override
    protected void onFindViewById() {
        mChatRowView = findViewById(R.id.layout_chat_row);
        mAvatarView = (ImageView) findViewById(R.id.iv_userhead);
        mEnquiryTitleView = (TextView) findViewById(R.id.text_enquiry_title);
        mEnquiryDetailView = (TextView) findViewById(R.id.text_enquiry_detail);
        mEnquirySummaryView = (TextView) findViewById(R.id.text_enquiry_summary);

        mChatRowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.direct == EMMessage.Direct.RECEIVE) {
                    showDialogEvaluate();
                }
            }
        });
    }

    @Override
    protected void onUpdateView() {

    }

    /**
     * 重写方法，实现了CtrlTypeChatRow内容的显示
     */
    @Override
    protected void onSetUpView() {
        try {
            JSONObject jsonObject = message.getJSONObjectAttribute(CustomerConstants.C_ATTR_KEY_WEICHAT);
            if (jsonObject.has(CustomerConstants.C_ATTR_CTRLTYPE)) {
                String ctrlType = jsonObject.getString(CustomerConstants.C_ATTR_CTRLTYPE);

                JSONObject ctrlArgs = jsonObject.getJSONObject("ctrlArgs");
                int inviteId = ctrlArgs.getInt("inviteId");
                String serviceSessionId = ctrlArgs.getString("serviceSessionId");
                String detail = ctrlArgs.getString("detail");
                String summary = ctrlArgs.getString("summary");

                mEnquiryEntity = new EnquiryEntity(inviteId, serviceSessionId, detail, summary);

                MLLog.i(String.valueOf(inviteId));
                MLLog.i(serviceSessionId);
                MLLog.i(detail);
                MLLog.i(summary);

                // 设置聊天气泡用户头像
                Glide.with(context)
                        .load((String) MLSPUtil.get(context, CustomerConstants.C_USER_KEY_AVATAR, ""))
                        .placeholder(R.mipmap.ic_avatar_02)
                        .into(mAvatarView);
                mAvatarView.setVisibility(View.GONE);

                mEnquiryTitleView.setText("客服满意度");
                if (mEnquiryEntity.getDetail().equals("null")
                        || mEnquiryEntity.getDetail().equals("")
                        || mEnquiryEntity.getDetail() == null) {
                    if (message.direct == EMMessage.Direct.RECEIVE) {
                        mEnquirySummaryView.setVisibility(View.GONE);
                        mEnquiryDetailView.setText("请对我的服务做出评价，谢谢");
                    } else {
                        mEnquirySummaryView.setText("星级：" + mEnquiryEntity.getSummary());
                        mEnquiryDetailView.setVisibility(View.GONE);
                    }
                } else {
                    mEnquirySummaryView.setText(mEnquiryEntity.getSummary());
                    mEnquiryDetailView.setText(mEnquiryEntity.getDetail());
                }
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onBubbleClick() {
//        showDialogEvaluate();
    }

    /**
     * 显示满意度评价Dialog，点击确定调用下边自定义的回调方法，在ChatFragment里发送评价消息
     */
    private void showDialogEvaluate() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        MLLog.d("showDialogEvaluate");
        View view = inflater.inflate(R.layout.widget_enquiry, null);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_enquiry);
        final EditText editText = (EditText) view.findViewById(R.id.edit_enquiry);
        dialog.setView(view);
        dialog.setTitle(R.string.dialog_title_evaluate);
        dialog.setNegativeButton("取消", null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String detail = editText.getText().toString();
                String summary = String.valueOf(ratingBar.getRating());
                mEnquiryEntity.setDetail(detail);
                mEnquiryEntity.setSummary(summary);
                mChatRowListener.onChatRowInteraction(mEnquiryEntity);
            }
        });
        dialog.show();

    }

    /**
     * 设置回调的接口实现类
     *
     * @param mChatRowListener
     */
    public void setmChatRowListener(CustomerChatRowListener mChatRowListener) {
        this.mChatRowListener = mChatRowListener;
    }

    /**
     * 自定义回调接口，实现在ChatRow发送评价消息
     */
    public interface CustomerChatRowListener {
        public void onChatRowInteraction(EnquiryEntity enquiryEntity);
    }
}

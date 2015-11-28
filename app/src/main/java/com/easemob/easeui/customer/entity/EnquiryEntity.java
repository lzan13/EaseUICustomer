package com.easemob.easeui.customer.entity;

import com.easemob.easeui.customer.widget.CtrlTypeChatRow;

/**
 * Created by lzan13 on 2015/11/26 17:24.
 * 满意度调查 类型消息的的实体类 供自定义消息CtrlTypeChatRow{@link CtrlTypeChatRow}调用
 */
public class EnquiryEntity {
    private int inviteId;
    private String serviceSessionId;
    private String detail;
    private String summary;

    /**
     * 空的构造函数
     */
    public EnquiryEntity() {

    }


    /**
     * 重载构造函数，传进实体类需要的参数
     *
     * @param inviteId
     * @param serviceSessionId
     * @param detail
     * @param summary
     */
    public EnquiryEntity(int inviteId, String serviceSessionId, String detail, String summary) {
        setInviteId(inviteId);
        setServiceSessionId(serviceSessionId);
        setDetail(detail);
        setSummary(summary);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getInviteId() {
        return inviteId;
    }

    public void setInviteId(int inviteId) {
        this.inviteId = inviteId;
    }

    public String getServiceSessionId() {
        return serviceSessionId;
    }

    public void setServiceSessionId(String serviceSessionId) {
        this.serviceSessionId = serviceSessionId;
    }
}

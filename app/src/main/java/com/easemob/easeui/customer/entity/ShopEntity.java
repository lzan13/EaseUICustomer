package com.easemob.easeui.customer.entity;

/**
 * Created by lzan13 on 2015/11/25.
 * 商品实体类，模拟商城软件的一些数据
 */
public class ShopEntity {
    private int item;
    private String shopTitle;
    private String shopDesc;
    private String shopPrice;
    private String shopImageUrl;
    private String shopUrl;

    public ShopEntity(int item) {
        setItem(item);
        switch (item) {
            case 1:
                setShopTitle("XXX吸尘器");
                setShopDesc("质量首选，全国销量第一品牌，累计1000用户选择！");
                setShopPrice("698￥");
                setShopImageUrl("http://tools.melove.net/test/image/shop_01.png");
                setShopUrl("http://lzan13.melove.net/shop/shop_01.html");
                break;
            case 2:
                setShopTitle("笨精灵惠系列喜庆装无芯卷纸");
                setShopDesc("原浆纯品 细腻柔韧 温柔呵护 放心安心");
                setShopPrice("22.22￥");
                setShopImageUrl("http://tools.melove.net/test/image/shop_02.jpg");
                setShopUrl("http://lzan13.melove.net/shop/shop_02.html");
                break;
            case 3:
                setShopTitle("四川安岳柠檬");
                setShopDesc("四川安岳，柠檬之乡，充足阳光带来的新鲜美味");
                setShopPrice("88.9￥");
                setShopImageUrl("http://tools.melove.net/test/image/shop_03.jpg");
                setShopUrl("http://lzan13.melove.net/shop/shop_03.html");
                break;
            case 4:
                setShopTitle("精准版电子圆称");
                setShopDesc("美丽是永恒的主题，体重管理，轻而易举；环保省点 大屏显示 精准传感 感应开关");
                setShopPrice("24.9￥");
                setShopImageUrl("http://tools.melove.net/test/image/shop_04.jpg");
                setShopUrl("http://lzan13.melove.net/shop/shop_04.html");
                break;
            case 5:
                setShopTitle("璐迪尔温馨加长枕头");
                setShopDesc("宝宝的优质睡眠，从璐迪尔开始，特别加长");
                setShopPrice("19.9￥");
                setShopImageUrl("http://tools.melove.net/test/image/shop_05.jpg");
                setShopUrl("http://lzan13.melove.net/shop/shop_05.html");
                break;
            case 6:
                setShopTitle("正宗阳澄湖大闸蟹");
                setShopDesc("自然的恩赐，豪华礼盒包装，送礼首选");
                setShopPrice("188.9￥");
                setShopImageUrl("http://tools.melove.net/test/image/shop_06.jpg");
                setShopUrl("http://lzan13.melove.net/shop/shop_06.html");
                break;
        }
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getShopDesc() {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc) {
        this.shopDesc = shopDesc;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }
}

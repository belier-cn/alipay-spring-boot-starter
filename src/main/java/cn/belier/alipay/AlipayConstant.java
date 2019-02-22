package cn.belier.alipay;

/**
 * 支付宝支付常量
 *
 * @author belier
 * @date 2018/12/7
 */
public interface AlipayConstant {

    /**
     * 正式环境网关地址
     */
    String PROD_TRADE_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 沙箱环境网关地址
     */
    String DEV_TRADE_URL = "https://openapi.alipaydev.com/gateway.do";

    /**
     * json格式
     */
    String FORMAT_JSON = "JSON";
}

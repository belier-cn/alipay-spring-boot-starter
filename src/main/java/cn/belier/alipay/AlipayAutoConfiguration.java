package cn.belier.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付自动装配
 *
 * @author belier
 * @date 2018/11/14
 */
@Configuration
@ConditionalOnClass(AlipayClient.class)
@ConditionalOnProperty(name = "alipay.enabled", matchIfMissing = true)
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayAutoConfiguration {

    private final AlipayProperties aliPayProperties;

    @Autowired
    public AlipayAutoConfiguration(AlipayProperties aliPayProperties) {
        this.aliPayProperties = aliPayProperties;
    }

    @Bean
    @ConditionalOnMissingBean(AlipayClient.class)
    public AlipayClient alipayClient() {

        return new DefaultAlipayClient(aliPayProperties.getTradeUrl(), aliPayProperties.getAppId()
                , aliPayProperties.getPrivateKey(), AlipayConstant.FORMAT_JSON
                , aliPayProperties.getCharset(), aliPayProperties.getPublicKey()
                , aliPayProperties.getSignType().name());
    }


}
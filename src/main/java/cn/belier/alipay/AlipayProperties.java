package cn.belier.alipay;

import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 支付宝支付配置
 *
 * @author H J
 * @date 2018/5/22
 */
@Slf4j
@Data
@ConfigurationProperties("alipay")
public class AlipayProperties {


    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

    private boolean enabled = true;

    /**
     * 支付宝应用ID
     */
    private String appId;


    /**
     * 是否是开发环境
     */
    private boolean dev = false;

    /**
     * 私钥
     */
    @Setter(AccessLevel.NONE)
    private String privateKey;

    /**
     * 私钥所在文件 绝对路径或者以classpath:开头的类路径
     */
    private String privateKeyPath = "classpath:/alipay/private.key";

    /**
     * 公钥
     */
    @Setter(AccessLevel.NONE)
    private String publicKey;

    /**
     * 公钥所在文件 绝对路径或者以classpath:开头的类路径
     */
    private String publicKeyPath = "classpath:/alipay/public.key";

    /**
     * 字符格式
     */
    private String charset = StandardCharsets.UTF_8.name();

    /**
     * 签名算法
     */
    private AlipaySignTypeEnum signType = AlipaySignTypeEnum.RSA2;


    /**
     * 获取私钥字符串
     */
    public String getPrivateKey() {

        if (this.privateKey == null) {
            this.privateKey = getKey(privateKeyPath);
        }
        return this.privateKey;
    }

    /**
     * 获取公钥字符串
     */
    public String getPublicKey() {

        if (this.publicKey == null) {
            this.publicKey = getKey(publicKeyPath);
        }
        return this.publicKey;
    }

    /**
     * 获取网关地址
     */
    public String getTradeUrl() {
        return this.dev ? AlipayConstant.DEV_TRADE_URL : AlipayConstant.PROD_TRADE_URL;
    }

    /**
     * 根据路径获取文本的值
     *
     * @param path 路径
     * @return 文本
     */
    private String getKey(String path) {

        try {

            @Cleanup
            InputStream inputStream = path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)
                    ? this.patternResolver.getResource(path).getInputStream()
                    : new FileInputStream(path);

            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8).trim();

        } catch (IOException e) {
            throw new RuntimeException("获取支付宝的key发生IO异常", e);
        }

    }
}

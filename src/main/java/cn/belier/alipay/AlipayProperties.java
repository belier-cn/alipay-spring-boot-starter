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
     * 网关地址
     */
    private String tradeUrl;

    /**
     * 支付宝应用ID
     */
    private String appId;

    /**
     * 签约的支付宝账号对应的支付宝唯一用户号，以2088开头的16位纯数字组成
     */
    private String pid;

    /**
     * 是否是开发环境
     */
    private boolean dev = false;

    /**
     * 应用私钥
     */
    @Setter(AccessLevel.NONE)
    private String privateKey;

    /**
     * 私钥所在文件 绝对路径或者以classpath:开头的类路径
     */
    private String privateKeyPath;

    /**
     * 支付宝公钥
     */
    @Setter(AccessLevel.NONE)
    private String publicKey;

    /**
     * 公钥所在文件 绝对路径或者以classpath:开头的类路径
     */
    private String publicKeyPath;

    /**
     * 字符格式
     */
    private String charset = StandardCharsets.UTF_8.name();

    /**
     * 签名算法
     */
    private AlipaySignTypeEnum signType = AlipaySignTypeEnum.RSA2;

    public String getPrivateKeyPath() {

        if (this.privateKeyPath == null) {
            this.privateKeyPath = this.dev ? "classpath:/alipay/dev/private.key" : "classpath:/alipay/private.key";
        }

        return this.privateKeyPath;

    }

    public String getPublicKeyPath() {

        if (this.publicKeyPath == null) {
            this.publicKeyPath = this.dev ? "classpath:/alipay/dev/public.key" : "classpath:/alipay/public.key";
        }

        return this.publicKeyPath;

    }

    /**
     * 获取私钥字符串
     */
    public String getPrivateKey() {

        if (this.privateKey == null) {
            this.privateKey = getKey(this.getPrivateKeyPath());
        }
        return this.privateKey;
    }

    /**
     * 获取公钥字符串
     */
    public String getPublicKey() {

        if (this.publicKey == null) {
            this.publicKey = getKey(this.getPublicKeyPath());
        }
        return this.publicKey;
    }

    /**
     * 获取网关地址
     */
    public String getTradeUrl() {

        if (this.tradeUrl == null) {
            this.tradeUrl = this.dev ? AlipayConstant.DEV_TRADE_URL : AlipayConstant.PROD_TRADE_URL;
        }

        return this.tradeUrl;

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

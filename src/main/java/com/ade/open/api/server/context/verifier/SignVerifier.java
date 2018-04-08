package com.ade.open.api.server.context.verifier;

import com.ade.open.api.server.context.Context;
import com.ade.open.api.server.context.ReturnCode;
import com.ade.open.api.server.persistent.TOpenSecret;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liyang on 18-2-7.
 */
public class SignVerifier extends Verifier {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public SignVerifier(Context context) {
        super(context);
    }

    @Override
    public ReturnCode check() {
        ReturnCode code = super.check();
        if (ReturnCode.success(code)) {
            code = checkSign();
        }
        return code;
    }

    private ReturnCode checkSign() {
        TOpenSecret tOpenSecret = getOpenSecret();
        if (Objects.isNull(tOpenSecret)) {
            return ReturnCode.CUSTOMER_NOT_EXIST;
        }
        String key = tOpenSecret.getSecretValue();
        if (StringUtils.isEmpty(key)) {
            return ReturnCode.CUSTOMER_DATA_EXCEPTION;
        }
        String originSign = getParameter("sign", String.class);
        String calcSign = sign(getMap(), key);
        if (originSign.equalsIgnoreCase(calcSign)) {
            return ReturnCode.SUCCESS;
        } else {
            return ReturnCode.INVALID_SIGN;
        }
    }

    private String sign(Map<String, Object> map, String key) {
        LOG.info("[sign map] {}", map);
        String kv = map.entrySet().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getKey()))
                .filter(e -> e.getValue() instanceof String)
                .filter(e -> StringUtils.isNotEmpty(e.getValue().toString()))
                .filter(e -> !"sign".equalsIgnoreCase(e.getKey().trim()))
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
        kv = kv + "&key=" + key;
        LOG.info("[sign  kv] {}", kv);
        String sign = md5(kv);
        LOG.info("[sign md5] {}", sign);
        return sign;
    }

    private final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private String md5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

}

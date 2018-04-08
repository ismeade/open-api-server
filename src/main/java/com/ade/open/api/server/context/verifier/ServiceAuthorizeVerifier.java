package com.ade.open.api.server.context.verifier;

import com.ade.open.api.server.context.Context;
import com.ade.open.api.server.context.ReturnCode;
import com.ade.open.api.server.persistent.TOpenSecret;
import com.ade.open.api.server.persistent.TOpenSecretService;
import com.ade.open.api.server.persistent.TOpenService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by liyang on 18-2-5.
 */
public class ServiceAuthorizeVerifier extends Verifier {

    public ServiceAuthorizeVerifier(Context context) {
        super(context);
    }

    @Override
    public ReturnCode check() {
        ReturnCode code = super.check();
        if (ReturnCode.success(code)) {
            code = checkAuthorize();
        }
        return code;
    }

    private ReturnCode checkAuthorize() {
        TOpenSecret tOpenSecret = getOpenSecret();
        if (Objects.isNull(tOpenSecret)) {
            return ReturnCode.CUSTOMER_NOT_EXIST;
        }
        Integer enable = tOpenSecret.getEnable();
        if (Objects.isNull(enable) || !enable.equals(1)) {
            return ReturnCode.SECRET_DISABLE;
        }
        List<String> serviceList = getServiceList(tOpenSecret);
        String service = getParameter("service", String.class);
        if (serviceList.contains(service)) {
            return ReturnCode.SUCCESS;
        } else {
            return ReturnCode.CUSTOMER_NO_AUTHORITY;
        }
    }

    private List<String> getServiceList(TOpenSecret tOpenSecret) {
        return Optional.of(tOpenSecret)
                .map(TOpenSecret::getSecretService)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(TOpenSecretService::getService)
                .map(TOpenService::getServiceName)
                .collect(Collectors.toList());
    }


}

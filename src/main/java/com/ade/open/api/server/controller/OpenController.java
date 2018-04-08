package com.ade.open.api.server.controller;

import com.ade.open.api.server.context.Context;
import com.ade.open.api.server.context.JsonContext;
import com.ade.open.api.server.context.ReturnCode;
import com.ade.open.api.server.context.verifier.ServiceAuthorizeVerifier;
import com.ade.open.api.server.context.verifier.SignVerifier;
import com.ade.open.api.server.service.RequestSynchronizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liyang on 18-2-5.
 */
@RestController
@RequestMapping(value = "/")
public class OpenController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RequestSynchronizer requestSynchronizer;

    @RequestMapping(value = "heart", method = RequestMethod.HEAD)
    public String heart() {
        return "heart";
    }

    @RequestMapping(value = "service", method = RequestMethod.POST)
    public String servicePost(@RequestBody String body) {
        LOG.info("[body] {}", body);
        Context context = JsonContext.create(body);
        SignVerifier signVerifier = new SignVerifier(context);
        ServiceAuthorizeVerifier serviceAuthorizeVerifier = new ServiceAuthorizeVerifier(signVerifier);
        ReturnCode code = serviceAuthorizeVerifier.check();
        if (ReturnCode.success(code)) {
            return requestSynchronizer.send(context);
        } else {
            return code.toJson();
        }
    }

    @RequestMapping(value = "service", method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.DELETE})
    public String otherRequestMethod() {
        return ReturnCode.METHOD_ONLY_POST.toJson();
    }

}

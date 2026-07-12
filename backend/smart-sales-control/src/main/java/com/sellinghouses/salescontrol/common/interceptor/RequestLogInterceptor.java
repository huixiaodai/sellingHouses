package com.sellinghouses.salescontrol.common.interceptor;

import com.sellinghouses.salescontrol.common.context.LoginUserContext;
import com.sellinghouses.salescontrol.common.context.LoginUserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final String START_TIME_ATTRIBUTE = RequestLogInterceptor.class.getName() + ".START_TIME";

    public static final String USER_ID_ATTRIBUTE = RequestLogInterceptor.class.getName() + ".USER_ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        long costMillis = startTime == null ? 0L : System.currentTimeMillis() - startTime;
        LoginUserContext loginUser = LoginUserHolder.get();
        Long userId = loginUser == null ? (Long) request.getAttribute(USER_ID_ATTRIBUTE) : loginUser.getUserId();
        log.info("request method={}, uri={}, userId={}, costMillis={}, status={}",
                request.getMethod(),
                request.getRequestURI(),
                userId,
                costMillis,
                response.getStatus());
    }
}

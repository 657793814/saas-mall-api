package com.liuzd.soft.interceptor;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.ImmutableSet;
import com.liuzd.soft.annotation.NoLogin;
import com.liuzd.soft.config.DynamicDataSource;
import com.liuzd.soft.consts.GlobalConstant;
import com.liuzd.soft.context.ThreadContextHolder;
import com.liuzd.soft.dto.token.TokenInfo;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.service.impl.BuyerServiceImpl;
import com.liuzd.soft.utils.SignUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;


/**
 * @description web拦截器，拦截请求设置动态数据源
 * @author:liuzd01
 * @date:2023/10/18
 * @email:liuzd2025@qq.com
 **/
@Slf4j
@Data
public class MyWebInterceptor implements HandlerInterceptor {

    private static final Set<String> DEFAULT_NO_TENANT_CODE_URIS = ImmutableSet.of(
            "/error",
            "/favicon.ico",
            "/swagger-ui.html",
            "/csrf"
    );

    // 无租户信息的接口前缀
    private static final Set<String> DEFAULT_NO_TENANT_CODE_URI_PREFIXES = ImmutableSet.of(
            "/swagger",
            "/actuator",
            "/test"
    );

    // 无需token的接口前缀
    private static final Set<String> NO_TOKEN_URI_PREFIXES = ImmutableSet.of(
            "/login",
            "/refresh_token",
            "/actuator",
            "/minio"
    );

    private DynamicDataSource dynamicDataSource;
    private BuyerServiceImpl buyerServiceImpl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if (!checkNoLogin(handler)) {
            verifyToken(request);
        }

        //设置TraceId
        setTraceId(request);

        return true;
    }

    private boolean checkNoLogin(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //1.获取目标类上的目标注解（可判断目标类是否存在该注解）
            NoLogin annotationInClass = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), NoLogin.class);
            if (ObjectUtil.isNotNull(annotationInClass) && annotationInClass.value()) {
                return true;
            }

            //2.获取目标方法上的目标注解（可判断目标方法是否存在该注解）
            NoLogin annotationInMethod = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NoLogin.class);
            if (ObjectUtil.isNotNull(annotationInMethod) && annotationInMethod.value()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证登录态
     *
     * @param request
     */
    private void verifyToken(HttpServletRequest request) {
        if (matchUriPrefix(request.getRequestURI(), NO_TOKEN_URI_PREFIXES)) {
            return;
        }

        String token = request.getParameter(GlobalConstant.REQUEST_PARAM_TOKEN_KEY);
        long timestamp = Long.parseLong(request.getParameter(GlobalConstant.REQUEST_PARAM_TIMESTAMP_KEY));
        String randStr = request.getParameter(GlobalConstant.REQUEST_PARAM_RAND_STR_KEY);
        String sign = request.getParameter(GlobalConstant.REQUEST_PARAM_SIGN_KEY);

        //10分钟实效性，防抓链接攻击
        if (timestamp - (System.currentTimeMillis() / 1000) > 600) {
            throw MyException.exception(RetEnums.VERIFY_SIGN_TIMESTAMP_FAIL);
        }

        //校验sign
        if (!SignUtils.verify(randStr, timestamp, sign, token)) {
            throw MyException.exception(RetEnums.VERIFY_SIGN_FAIL);
        }

        //通过token 获取登录信息
        TokenInfo tokenInfo = buyerServiceImpl.getTokenInfo(token);
        if (ObjectUtils.isEmpty(tokenInfo)) {
            throw MyException.exception(RetEnums.LOGIN_EXPIRE);
        }

        //校验成功 将tokenInfo 设置到线程上下文中
        ThreadContextHolder.put(GlobalConstant.LOGIN_USER_INFO, tokenInfo);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除MDC中的traceId，防止内存泄漏
        MDC.remove(GlobalConstant.LOGTRACE_TRACEID);
        // 清除ThreadContextHolder中的内容
        ThreadContextHolder.clear();
    }

    /**
     * 设置日志链路的traceId
     */
    private void setTraceId(HttpServletRequest request) {
        //判断请求头是否由traceId
        String traceId = request.getHeader(GlobalConstant.LOGTRACE_FAST_TRACEID);
        if (!StringUtils.hasText(traceId)) {
            traceId = generateTrackId();
        }

        ThreadContextHolder.put(GlobalConstant.LOGTRACE_FAST_TRACEID, traceId);
        ThreadContextHolder.put(GlobalConstant.LOGTRACE_TRACEID, traceId);

        // 将traceId放入MDC中，以便在日志中输出
        MDC.put(GlobalConstant.LOGTRACE_TRACEID, traceId);
        log.debug("Setting traceId in MDC: {}", traceId);
    }

    /**
     * 生成traceId
     *
     * @return
     */
    public static String generateTrackId() {
        return UUID.randomUUID().toString().replace("-", "") +
                "." + Thread.currentThread().getId() + "." + System.currentTimeMillis();
    }


    /**
     * 判断uri是否匹配
     *
     * @param uri
     * @param prefixes
     * @return
     */
    private boolean matchUriPrefix(String uri, Collection<String> prefixes) {
        if (!StringUtils.hasText(uri) || CollectionUtils.isEmpty(prefixes)) {
            return false;
        }
        for (String prefix : prefixes) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}

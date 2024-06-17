package com.yuuuuy.eshop.filter;

import cn.hutool.json.JSONException;
import com.yuuuuy.eshop.security.SecurityUser;
import com.yuuuuy.eshop.utils.JWTUtils;
import com.yuuuuy.eshop.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@CrossOrigin
@Slf4j
@Component
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtils redisUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取JWT
        String token = request.getHeader("token");
        //检查是否为空
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(request, response);
            return;
        }
        //检查是否过期
        if(!JWTUtils.validate(token)){
            filterChain.doFilter(request, response);
            return;
        }
        //解析JWT获取id
        String id;
        try {
            id = JWTUtils.getID(token);
            if (id == null) {
                throw new RuntimeException("ID is null in JWT token");
            }
        }catch (JSONException jsonException){
            throw new RuntimeException("jwt error");
        }
        //用id获取redis中的数据
        String redisKey = "login:" + id;
        SecurityUser securityUser = (SecurityUser)redisUtils.getCacheObject(redisKey);
        //是否获取到数据
        if(Objects.isNull(securityUser)){
            throw new RuntimeException("用户未登录");
        }
        //将数据存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}

package com.xxxx.server.config.security;

import com.xxxx.server.mapper.AdminMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * @description: Specurity配置类
 * @author: 吉祥
 * @created: 2021/10/22 12:38
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private CustomFilter customFilter;
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/login",
                "/admin/logout",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha",
                "/ws/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭csrf
        http.csrf()
                .disable()
                //关闭session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
//                //允许不登录访问
//                .antMatchers("/login","/admin/logout").permitAll()
//                //放行Swagger2
//                .antMatchers(
//                        "/css/**",
//                        "/js/**",
//                        "/index.html",
//                        "favicon.ico",
//                        "/doc.html",
//                        "/webjars/**",
//                        "/swagger-resources/**",
//                        "/v2/api-docs/**",
//                        "/captcha",
//                        "/ws/**").permitAll()
                //下面那样也可
                /**
                 * @Override
                 *        public void configure(WebSecurity web) throws Exception {
                 * 		web.ignoring().antMatchers(
                 * 				"/login",
                 * 				"/logout",
                 * 				"/css/**",
                 * 				"/js/**",
                 * 				"/index.html",
                 * 				"favicon.ico",
                 * 				"/doc.html",
                 * 				"/webjars/**",
                 * 				"/swagger-resources/**",
                 * 				"/v2/api-docs/**",
                 * 				"/captcha",
                 * 				"/ws/**"
                 * 		);
                 *    }
                 */
                //除了上面都需要认证
                .anyRequest().authenticated()
                //动态配置权限
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //禁用缓存
                .headers().cacheControl();
        //添加jwt拦截器
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)//未授权返回结果
                .authenticationEntryPoint(restAuthorizationEntryPoint);//未登录返回结果
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                Admin admin = adminService.getAminInfoByUserName(s);
                if (admin != null){
                    List<Role> rolesByAdminId = adminService.getRolesByAdminId(admin.getId());
                    admin.setRoles(rolesByAdminId);
                    return admin;
                }
                return null;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }
}


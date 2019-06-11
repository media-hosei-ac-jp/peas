package jp.ac.hosei.media.peas.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//corsフィルタ
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConfigurationProperties(prefix="settings")
public class SimpleCORSFilter implements Filter {
	@Setter
	private String frontendUrl;
	
	@Setter
	private String csrfHeaderName;
	
	@Setter
	private boolean wildcardCORSOrigin;
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)response;
        HttpServletRequest req = (HttpServletRequest)request;
        
        String origin;
        if(wildcardCORSOrigin) {
        	origin = req.getHeader("Origin");
        }
        else {
        	origin = frontendUrl;
        }
        
        res.setHeader("Access-Control-Allow-Origin", origin);
        res.setHeader("Access-Control-Allow-Credentials", "true"); //for xhr withCredential
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "0");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Authorization, " + csrfHeaderName);
        res.setHeader("Access-Control-Expose-Headers", csrfHeaderName);
        
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) { //preflight errorの403 回避
            res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }            
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}

}

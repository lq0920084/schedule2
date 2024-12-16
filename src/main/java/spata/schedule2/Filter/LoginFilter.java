package spata.schedule2.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginFilter implements Filter {
    private final String[] WEB_REDIRECT = { "/","/web*"};
    private final String[] WHITE_LIST = {"/web/login","/web/logout","/web/signup","/schedule/login","/user*","/schedule/logout","/schedule/signup"};
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;
        HttpSession session = httprequest.getSession(false);
        log.info("로그인 체크");
        if(isWhiteList(httprequest.getRequestURI())){
            log.info("화이트리스트입니다.");
            chain.doFilter(httprequest,response);
        } else{
            log.info("화이트리스트가 아닙니다.");
            if(session==null){
                if(isUserOrSchedule(httprequest.getRequestURI())){
                httpresponse.sendRedirect("/web/login");
                return;
                }else {
                    httpresponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    chain.doFilter(httprequest,httpresponse);
                }
            }else {
                chain.doFilter(httprequest,response);
            }

        }

    }
    private boolean isWhiteList(String requestURI){
        return PatternMatchUtils.simpleMatch(this.WHITE_LIST,requestURI);
    }
    private boolean isUserOrSchedule(String requestURI){
        return PatternMatchUtils.simpleMatch(this.WEB_REDIRECT,requestURI);
    }
}

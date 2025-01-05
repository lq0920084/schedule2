package spata.schedule2.Filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import spata.schedule2.Exception.LoginWebException;
import spata.schedule2.dto.LoginRequestDto;
import spata.schedule2.dto.LoginResponseDto;
import spata.schedule2.service.UserService;

import java.io.IOException;
import java.net.URLDecoder;
import org.json.JSONObject;
@Slf4j
@RequiredArgsConstructor

public class LoginFilter implements Filter {
    private final UserService userService;
    private final String[] WEB_REDIRECT = {"/", "/web*"};
    private final String[] WHITE_LIST = {"/web/login", "/web/logout", "/web/signup", "/api/schedule/login", "/api/schedule/logout", "/api/user*"};
    private final String[] WEB_LOGIN = {"/web/login"};
    private final String[] API_LOGIN = {"/api/user/login"};

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;
        HttpSession session = httprequest.getSession(false);
        log.info("로그인 체크");
        //화이트리스트인지 여부를 체크합니다.
        if (isWhiteList(httprequest.getRequestURI())) {
            log.info("화이트리스트입니다.");
            //웹 로그인을 시도하는지 api 로그인을 시도하는지 구분합니다.
            if (isWebLogin(httprequest.getRequestURI()) && httprequest.getMethod().equals("POST")) {
                try {
                    LoginResponseDto user = userService.userLogin(loginServletToDto(request, true));
                    HttpSession newLoginSession = httprequest.getSession();
                    //세션에 userid정보를 넣습니다.
                    newLoginSession.setAttribute("userid", user.getUserId());
                    chain.doFilter(httprequest, response);
                } catch (LoginWebException e) {
                    //핸들러익셉션리졸버로 오류를 넣어 컨트롤러로 보냅니다.
                    ModelAndView model = handlerExceptionResolver.resolveException(httprequest, (HttpServletResponse) response,null,e);
                    request.setAttribute("message", model.getModel().get("message"));
                    request.setAttribute("checkSignIn", model.getModel().get("checkSignIn"));
                    request.setAttribute("signInUrl", model.getModel().get("signInUrl"));
                    request.getRequestDispatcher(model.getViewName()).forward(request,response);
                }
            } else if (isApiLogin(httprequest.getRequestURI()) && httprequest.getMethod().equals("POST")) {
                try{
                LoginResponseDto user = userService.userLogin(loginServletToDto(request, false));
                HttpSession newLoginSession = httprequest.getSession();
                newLoginSession.setAttribute("userid", user.getUserId());
                chain.doFilter(httprequest, response);
                }catch(ResponseStatusException e){
                    httprequest.setAttribute("statusCode", e.getStatusCode());
                    httprequest.setAttribute("message", e.getReason());
                    chain.doFilter(httprequest, response);
                }
            } else {
                chain.doFilter(httprequest, response);
            }
        } else {
            log.info("화이트리스트가 아닙니다.");
            if (session == null) {
                if (isUserOrSchedule(httprequest.getRequestURI())) {
                    httpresponse.sendRedirect("/web/login");
                } else {
                    httpresponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                    chain.doFilter(httprequest, httpresponse);
                }
            } else {
                chain.doFilter(httprequest, response);
            }

        }

    }

    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(this.WHITE_LIST, requestURI);
    }

    private boolean isUserOrSchedule(String requestURI) {
        return PatternMatchUtils.simpleMatch(this.WEB_REDIRECT, requestURI);
    }

    private boolean isWebLogin(String requestURI) {
        return PatternMatchUtils.simpleMatch(this.WEB_LOGIN, requestURI);
    }

    private boolean isApiLogin(String requestURI) {
        return PatternMatchUtils.simpleMatch(this.API_LOGIN, requestURI);
    }

    private LoginRequestDto loginServletToDto(ServletRequest request, boolean ui) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int byteRead;
        while ((byteRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, byteRead));
        }
        if(URLDecoder.decode(stringBuilder.toString(), "UTF-8").contains("{")){
            String jsonString = URLDecoder.decode(stringBuilder.toString(), "UTF-8");
            JSONObject jsonObject = new JSONObject(jsonString);
            return new LoginRequestDto(ui,(String)jsonObject.get("email"),(String)jsonObject.get("password"));
        }else{
        String[] requestbody = URLDecoder.decode(stringBuilder.toString(), "UTF-8").split("&");
        String email = requestbody[0].substring(requestbody[0].indexOf("=") + 1);
         String password = requestbody[1].substring(requestbody[1].indexOf("=") + 1);
        return new LoginRequestDto(ui, email, password);
        }
    }
}

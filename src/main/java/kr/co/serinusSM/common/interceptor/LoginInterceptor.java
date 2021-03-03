package kr.co.serinusSM.common.interceptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("loginInterceptor")
public class LoginInterceptor extends HandlerInterceptorAdapter {
    protected Log log = LogFactory.getLog(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("shopInfo:" + request.getSession().getAttribute("shopInfo"));

        try {
            //userData 세션key를 가진 정보가 널일경우 로그인페이지로 이동
            if(request.getSession().getAttribute("shopInfo") == null ){
                System.out.println("can not search session ... ");
//                response.sendRedirect("/loginChoice.do");
                response.sendRedirect("/login");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
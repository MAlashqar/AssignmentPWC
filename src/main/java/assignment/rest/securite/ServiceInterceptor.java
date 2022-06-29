package assignment.rest.securite;

import java.io.Writer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ServiceInterceptor implements HandlerInterceptor {
	private static Logger log = LoggerFactory.getLogger(ServiceInterceptor.class);
	@Autowired
	Jwt jwt;
@Autowired
ObjectMapper objectMapper;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		log.info("[preHandle] " + "[ Req Method :" + request.getMethod() + "]" + "[Req URL :" + request.getRequestURI()
				+ "]" + "[Req Param :" + getParameters(request) + "]");
		String apiName = request.getRequestURI().split("/")[request.getRequestURI().split("/").length-1 ];
		List<String> apiSkip = Arrays.asList("addNewPatient", "addNewDoctor", "login");
		if (!apiSkip.contains(apiName)) {
			String token = request.getHeader("token");
			
			if (token == null || token.isEmpty()) {
				Map<String, String> result = new HashMap<String, String>();
				result.put("errorCode", "400");
				result.put("errorMessage", "Token Not Found ,please put token in header with name (token)");
				response.setStatus(400);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				try (final Writer writer = response.getWriter()) {
					writer.write(objectMapper.writeValueAsString(result));
				}
				return false;
			}
			String tokenCheck = jwt.validateToken(token);
			if (tokenCheck.equalsIgnoreCase("valid")) {
				return true;
			} else {
				Map<String, String> result = new HashMap<String, String>();
				result.put("errorCode", "400");
				result.put("errorMessage", tokenCheck);
				response.setStatus(400);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				try (final Writer writer = response.getWriter()) {
				//	writer.write(result.toString());
				writer.write(objectMapper.writeValueAsString(result));
				}
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	private String getParameters(HttpServletRequest request) {
		StringBuffer posted = new StringBuffer();
		Enumeration<?> e = request.getParameterNames();
		if (e != null) {
			posted.append("?");
		}
		while (e.hasMoreElements()) {
			if (posted.length() > 1) {
				posted.append("&");
			}
			String curr = (String) e.nextElement();
			posted.append(curr + "=");
			if (curr.contains("password") || curr.contains("pass") || curr.contains("pwd")) {
				posted.append("*****");
			} else {
				posted.append(request.getParameter(curr));
			}
		}
		String ip = request.getHeader("X-FORWARDED-FOR");
		String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
		if (ipAddr != null && !ipAddr.equals("")) {
			posted.append("& IP Address=" + ipAddr);
		}
		return posted.toString();
	}

	private String getRemoteAddr(HttpServletRequest request) {
		String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
		if (ipFromHeader != null && ipFromHeader.length() > 0) {
			log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
			return ipFromHeader;
		}
		return request.getRemoteAddr();
	}

}

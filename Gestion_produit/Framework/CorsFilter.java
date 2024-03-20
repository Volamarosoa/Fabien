package Framework;

import javax.servlet.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Allow requests from any origin. You can restrict this to specific origins.
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");

        // Allow the following HTTP methods.
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        // Allow the following headers.
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Allow credentials (e.g., cookies).
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed.
    }

    @Override
    public void destroy() {
        // Cleanup code if needed.
    }
}

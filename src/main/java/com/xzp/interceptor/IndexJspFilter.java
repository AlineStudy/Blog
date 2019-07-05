package com.xzp.interceptor;

import com.xzp.dao.UserContentMapper;
import com.xzp.model.UserContent;
import com.xzp.utils.PageHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

public class IndexJspFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    /**
     * filter 初始化时，注解的 bean 还没初始化，加 @Autowired 注解不会起作用
     * ，所以通过 ApplicationContext 手动获取 UserContentMapper 对象。
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     *
     *
     * 主要增加了根据上传时间倒排序的条件：
     *
     * 通过 Example 的有参构造获取 Example 对象，参数就是你要操作的对象的 class 文件，
     * 通过 e.setOrderByClause("rpt_time DESC") 设置排序的字段和排序规则，如果不写，默认是 ASC 升序的。
     *
     * 然后通过 userContentMapper.selectByExample(e) 方法将查询条件 example 对象传入即可实现根据发布时间倒排序。
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("自定义过滤器");
        ServletContext context = servletRequest.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
        UserContentMapper userContentMapper = applicationContext.getBean(UserContentMapper.class);
        PageHelper.startPage(null,null);//开始分页


        List<UserContent> list = userContentMapper.findByJoin(null);
        PageHelper.Page endPage = PageHelper.endPage();
        servletRequest.setAttribute("page",endPage);

        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {

    }
}

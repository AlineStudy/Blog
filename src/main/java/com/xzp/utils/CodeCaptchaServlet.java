package com.xzp.utils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


/**
 * 验证码的生成
 */

@WebServlet("/captchaServlet")
public class CodeCaptchaServlet extends HttpServlet {

    public static final String VERCODE_KEY = "VERCODE_KEY";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getSession().removeAttribute(VERCODE_KEY);
        //首先设置页面不缓存
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires", 0);

        //在内存中创建图像
        int iWidth = 55 , iHeight = 18;
        BufferedImage image = new BufferedImage(iWidth,iHeight,BufferedImage.TYPE_INT_BGR);
        //获取图像上下文
        Graphics graphics = image.getGraphics();
        //设定背景颜色
        graphics.setColor(Color.white);
        graphics.fillRect(0,0,iWidth,iHeight);
        //画边框
        graphics.setColor(Color.black);
        graphics.drawRect(0,0,iWidth-1,iHeight-1);
        //取随机产生的认证码(4个数字)
        int intCount = new Random().nextInt(9999);
        if(intCount <1000)
            intCount+=1000;
        String rand=intCount+"";

        //将认证码显示到图片中
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        graphics.drawString(rand, 5, 15);
        //随机产生88个干扰点,使图象中的认证码不易被其它程序探测到
        Random random = new Random();
        for (int iIndex = 0; iIndex < 100; iIndex++) {
            int x = random.nextInt(iWidth);
            int y = random.nextInt(iHeight);
            graphics.drawLine(x, y, x, y);
        }
        // 将生成的随机字符串写入session
        // request.getSession().setAttribute(LOGIN_VERCODE_KEY, rand);
        request.getSession().setAttribute(VERCODE_KEY, rand);
        //图象生效
        graphics.dispose();
        //输出图象到页面
        ImageIO.write(image, "JPEG", response.getOutputStream());

    }
}

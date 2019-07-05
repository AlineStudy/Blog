package com.xzp.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 逆向工程生成model类，mapper接口,以及xml文件
 */

public class Generator {

    public void generator() throws Exception{
        List<String> warning = new ArrayList<>();
        boolean overwrite = true;

        //指向逆向工程配置信息文件
        InputStream is = Generator.class.getClassLoader().getResource("generatorConfig.xml").openStream();
        ConfigurationParser parser = new ConfigurationParser(warning);
        Configuration config = parser.parseConfiguration(is);
        is.close();
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback,warning);
        myBatisGenerator.generate(null);
    }

    public static void main(String[] args) {
        try {
            Generator generator = new Generator();
            generator.generator();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

package cn.itcast.dao.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestOutput {
	@Test
	public void test1() throws Exception{
		// 配置对象,配置模板位置
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		// 指定ftl/html文件位置
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/templates"));
		// 获取模板对象
		Template template = configuration.getTemplate("hello.ftl");
		// 动态数据对象
		Map<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("title", "我是你爸爸");
		paramterMap.put("msg", "这是第一个Freemarker案例!");
		
		// 合并输出
		template.process(paramterMap, new PrintWriter(System.out));
	}
}

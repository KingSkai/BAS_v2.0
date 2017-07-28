package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("all")
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion>{
	
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {

		// 基于WebService 获取 bos_management的 活动列表 数据信息
/*		PageBean<Promotion> pageBean = WebClient
				.create("http://localhost:9001/bos_management/services/promotionService/pageQuery?page="
						+ page + "&rows=" + rows)
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);*/
		System.out.println("~~~~~~~~~~~~~~~~~~~~方法执行~~~~~~~~~~~~~~~~~~");
		PageBean<Promotion> pageBean = WebClient.create("http://localhost:9001/bos_management/services/promotionService/pageQuery?page="
				+ page + "&rows=" + rows).accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		
		ActionContext.getContext().getValueStack().push(pageBean);

		return SUCCESS;
	}
}

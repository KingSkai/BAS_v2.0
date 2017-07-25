package cn.itcast.bos.web.action;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos_fore.utils.SmsUtils;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

	private static final long serialVersionUID = 1L;

	@Action(value = "customer_sendSms")
	public String sendSms() throws IOException {
		// 手机号保存在Customer对象
		// 生成短信验证码
		String randomCode = RandomStringUtils.randomNumeric(4);
		// 将短信验证码 保存到session
		ServletActionContext.getRequest().getSession()
				.setAttribute(model.getTelephone(), randomCode);

		System.out.println("生成手机验证码为：" + randomCode);
		// 编辑短信内容
		final String msg = "尊敬的用户您好，本次获取的验证码为：" + randomCode
				+ ",服务电话：4006184000";

		// 暂时注释验证码校验
		//String result = SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
		String result = "000/xxxx";
		if (result.startsWith("000")) {
			// 发送成功
			return NONE;
		} else {
			// 发送失败
			throw new RuntimeException("短信发送失败, 信息吗:" + result);
		}
		/*// 调用MQ服务，发送一条消息
		jmsTemplate.send("bos_sms", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});*/

	}
	
	private String checkcode;
	
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	@Action(value="customer_regist", results={
			@Result(name="success",type="redirect",location="signup-success.html"),
			@Result(name="input",type="redirect",location="signup.html")
	})
	public String customerRegister() {
		// 从session中获取保存的验证码
		String sessionCheckCode = (String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		// 判断输入的验证码 和 服务器返回的验证码是否相同
		if (sessionCheckCode==null || !sessionCheckCode.endsWith(checkcode)) {
			System.out.println("短信验证码有误");
			return INPUT;
		} 
		// 调用WebService 连接CRM 保存客户信息
		WebClient.create("http://localhost:9002/crm_management/services/customerService/customer").type(MediaType.APPLICATION_JSON).post(model);
		System.out.println("客户注册成功");
		return SUCCESS;
	}
	
	
	
	
}

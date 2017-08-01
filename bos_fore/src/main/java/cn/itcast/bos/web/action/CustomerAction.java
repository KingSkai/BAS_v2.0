package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.constant.Constants;
import cn.itcast.bos_fore.utils.MailUtils;
import cn.itcast.bos_fore.utils.SmsUtils;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("jmsQueueTemplate")
	// 按照名称注入
	private JmsTemplate jmsTemplate;

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

		// 调用MQ服务，发送一条消息
		jmsTemplate.send("bos_sms", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", model.getTelephone());
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});
		return NONE;
	}

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private String checkcode;

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	@Action(value = "customer_regist", results = {
			@Result(name = "success", type = "redirect", location = "signup-success.html"),
			@Result(name = "input", type = "redirect", location = "signup.html") })
	public String customerRegister() {
		// 从session中获取保存的验证码
		String sessionCheckCode = (String) ServletActionContext.getRequest()
				.getSession().getAttribute(model.getTelephone());
		// 判断输入的验证码 和 服务器返回的验证码是否相同
		if (sessionCheckCode == null || !sessionCheckCode.endsWith(checkcode)) {
			System.out.println("短信验证码有误");
			return INPUT;
		}
		// 调用WebService 连接CRM 保存客户信息
		WebClient
				.create("http://localhost:9002/crm_management/services/customerService/customer")
				.type(MediaType.APPLICATION_JSON).post(model);
		System.out.println("客户注册成功");

		// 发送一份激活邮件
		// 生成激活码
		String activeCode = RandomStringUtils.randomNumeric(32);

		// 将激活码保存到redis中
		redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 24,
				TimeUnit.HOURS);

		// 调用MailUtils发送激活邮件
		String contentString = "尊敬的用户您好, 请于24小时内进行邮箱的绑定, 点击下面的地址进行绑定: <br/><a href='"
				+ MailUtils.activeUrl
				+ "?telephone="
				+ model.getTelephone()
				+ "&activecode=" + activeCode + "'>速运快递邮箱绑定地址</a>";
		MailUtils.sendMail("速运快递激活邮件", contentString, model.getEmail());
		return SUCCESS;
	}

	// "http://localhost:9003/bos_fore/customer_activeMail?telephone=18506835930&activecode=89938306161451684885234990393585"

	// 属性驱动, 获取验证邮箱链接中的激活码
	private String activecode;

	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}

	@Action(value = "customer_activeMail")
	public String activeMail() throws IOException {

		// 获取redis中的验证码
		String activeCodeRedis = redisTemplate.opsForValue().get(
				model.getTelephone());
		// 判断验证码是否相同
		if (activeCodeRedis == null || !activeCodeRedis.equals(activecode)) {
			// 如果验证不通过
			ServletActionContext.getResponse().getWriter()
					.println("激活码有误,请重新登录邮箱并完成激活!");
		} else {
			// 激活码有效
			// 防止重复绑定
			// 调用CRM WebService 查询客户信息, 判断是否已经绑定
			Customer customer = WebClient
					.create("http://localhost:9002/crm_management/services/"
							+ "customerService/customer/telephone/"
							+ model.getTelephone())
					.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			if (customer.getType() == null || customer.getType() != 1) {
				// 没有绑定,进行绑定
				WebClient.create(
						"http://localhost:9002/crm_management/services/"
								+ "customerService/customer/updatetype/"
								+ model.getTelephone()).get();
				ServletActionContext.getResponse().getWriter().print("邮箱绑定成功!");
			} else {
				// 已经绑定过
				ServletActionContext.getResponse().getWriter()
						.println("邮箱已绑定过,无需重复绑定!");
			}
			// 删除redis的激活码
			redisTemplate.delete(model.getTelephone());
		}
		return NONE;
	}
	
	/**
	 * 用户登录方法
	 */
	@Action(value="customer_login", results={
			@Result(name="login", location="login.html", type="redirect"),
			@Result(name="success", location="index.html#myhome",type="redirect")})
	public String login() {
		Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_URL+
				 "/services/customerService/customer/login?telephone="
				 + model.getTelephone() + "&password="
				 + model.getPassword())
				 .accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if (customer == null) {
			System.out.println("登录失败!");
			// 登录失败
			return LOGIN;
		} else {
			// 登录成功
			System.out.println("登录成功!");
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}
	}

}

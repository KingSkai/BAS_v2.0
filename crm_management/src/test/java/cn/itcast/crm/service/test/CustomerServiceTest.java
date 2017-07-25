package cn.itcast.crm.service.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerServiceTest {

	@Autowired
	private CustomerService customerService;

	// 未关联的客户列表
	@Test
	public void testFindNoAssociationCustomers() {
		System.out.println(customerService.findNoAssociationCustomers());
	}

	// 已关联的客户列表
	@Test
	public void testFindHasAssociationFixedAreaCustomers() {
		System.out.println(customerService
				.findHasAssociationFixedAreaCustomers("dq001"));
	}

	// 进行定区和客户的关联(修改)
	@Test
	public void testAssociationCustomersToFixedArea() {
		customerService.associationCustomersToFixedArea("1,2", "dq001");
	}

}

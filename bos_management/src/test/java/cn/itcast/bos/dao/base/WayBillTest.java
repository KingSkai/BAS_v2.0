package cn.itcast.bos.dao.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.bos.service.take_delivery.WayBillService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class WayBillTest {
	
	@Autowired
	private WayBillService wayBillService;
	
	@Test
	public void wayBillTest () {
		wayBillService.syncIndex();
	}
}

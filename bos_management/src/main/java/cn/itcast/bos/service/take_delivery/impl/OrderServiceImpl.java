package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Override
	public void saveOrder(Order order) {
		System.out.println(order);
	}

}

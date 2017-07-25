package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.base.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService{

	// 注入DAO
	@Autowired
	private FixedAreaRepository fixedAreaReopsitory;
	
	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaReopsitory.save(fixedArea);
	}

	@Override
	public Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable) {
		return fixedAreaReopsitory.findAll(specification, pageable);
	}

	@Autowired
	private CourierRepository courierRepository;
	
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	@Override
	public void associationCourierToFixedArea(FixedArea model,
			Integer courierId, Integer takeTimeId) {
		FixedArea fixedArea = fixedAreaReopsitory.findOne(model.getId());
		Courier courier = courierRepository.findOne(courierId);
		TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
		// 快递员关联到定区上
		fixedArea.getCouriers().add(courier);
		// 将收派事件 关联到快递员上
		// 一对多的关系, 多个快递员可以使用相同的收派时间
		courier.setTakeTime(takeTime);
	}

}

package cn.itcast.bos.service.take_delivery.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService{

	@Autowired
	PromotionRepository promotionRepository;
	
	@Override
	public void save(Promotion model) {
		promotionRepository.save(model);
	}

	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}

	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		
		// 封装到Page对象
		PageBean<Promotion> pageBean = new PageBean<Promotion>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}

}

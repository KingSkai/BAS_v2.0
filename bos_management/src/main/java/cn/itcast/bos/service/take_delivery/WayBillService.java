package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	// 保存运单
	void save(WayBill wayBill);

	// 无条件 分页查询
	Page<WayBill> findPageData(WayBill wayBill, Pageable pageable);

	// 根据运单号查询
	WayBill findByWayBillNum(String wayBillNum);

	void syncIndex();
}

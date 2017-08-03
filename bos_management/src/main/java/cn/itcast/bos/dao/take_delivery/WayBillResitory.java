package cn.itcast.bos.dao.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillResitory extends JpaRepository<WayBill, Integer>{
	
	WayBill findByWayBillNum(String wayBillNum);

	Page<WayBill> findByWayBillNum(WayBill wayBill, Pageable pageable);

}

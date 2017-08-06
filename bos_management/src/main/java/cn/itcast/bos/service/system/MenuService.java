package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Menu;

public interface MenuService {

	List<Menu> findAll();

	void save(Menu model);
	
}

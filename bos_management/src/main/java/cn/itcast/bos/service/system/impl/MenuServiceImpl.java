package cn.itcast.bos.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;


@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	public List<Menu> findAll() {
		return menuRepository.findAll();
	}

	@Override
	public void save(Menu menu) {
		// 防止用户没有选中父菜单
		if (menu.getParentMenu() != null && menu.getParentMenu().getId() == 0) {
			menu.setParentMenu(null);
		}
		// 调用DAO
		menuRepository.save(menu);
	}

	@Override
	public List<Menu> findByUser(User user) {
		if (user.getUsername().equals("admin")) {
			return menuRepository.findAll();
		} else {
			return menuRepository.findByUser(user.getId());
		}
	}

}

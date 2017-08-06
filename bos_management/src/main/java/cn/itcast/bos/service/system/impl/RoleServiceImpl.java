package cn.itcast.bos.service.system.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public List<Role> findByUser(User user) {
		if (user.getUsername().equals("admin")) {
			// 返回所有权限
			return roleRepository.findAll();
		} else {
			return roleRepository.findByUser(user.getId());
		}
	}

	@Override
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Override
	public void save(Role role, String[] permissionIds, String menuIds) {
		// 角色信息
		roleRepository.save(role);
		if (permissionIds != null) {
			System.out.println(permissionIds);
			// 关联权限
			for (String permissionid : permissionIds) {
				Permission permission = permissionRepository.findOne(Integer.parseInt(permissionid));
				System.out.println("permissionid:" +  permissionid);
				System.out.println("Permission:" +  permission);
				role.getPermissions().add(permission);
			}
		}
		if (StringUtils.isNotBlank(menuIds)) {
			// 关联菜单
			String[] menuArr = menuIds.split(",");
			for (String menuId : menuArr) {
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
				role.getMenus().add(menu);
			}
		}
	}

}

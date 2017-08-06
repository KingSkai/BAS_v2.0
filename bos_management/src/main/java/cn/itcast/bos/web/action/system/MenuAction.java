package cn.itcast.bos.web.action.system;

import java.util.List;



import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;

import com.opensymphony.xwork2.ActionContext;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {
	
	@Autowired
	private MenuService menuService;
	
	@Action(value = "menu_list", results = { @Result(name = "success", type = "json") })
	public String list() {
		// 调用业务层, 查询所有菜单数据
		List<Menu> menus = menuService.findAll();
		// 压入值栈
		ActionContext.getContext().getValueStack().push(menus);
		return SUCCESS;
	}
	
	@Action(value="menu_save", results={@Result(name="success",type="redirect",location="pages/system/menu.html")})
	public String save(){
		// 调用业务层, 保存菜单数据
		menuService.save(model);
		return SUCCESS;
	}
}

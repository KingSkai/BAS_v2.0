package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Scope("prototype")
@Namespace("/")
@Controller
public class FixedAreaAction extends BaseAction<FixedArea>{
	
	// 注入Service
	@Autowired
	private FixedAreaService fixedAreaService;
	
	// 保存定区
	@Action(value="fixedArea_save",
			results={@Result(name="success", type="redirect", location="./pages/base/fixed_area.html")})
	public String save() {
		fixedAreaService.save(model);
		return SUCCESS;
	}
	
	// 分页查询
	@Action(value="fixedArea_pageQuery", results={@Result(name="success",type="json")})
	public String pageQuery(){
		// 构造分页查询对象
		Pageable pageable = new PageRequest(page - 1, rows);
		// 构造条件查询对象
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				// 构造查询条件
				if (StringUtils.isNotBlank(model.getId())) {
					// 根据定区编号查询 等值
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(model.getCompany())) {
					// 根据定区编号查询 模糊查询
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + model.getCompany() + "%");
					list.add(p2);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		// 调用业务层
		Page<FixedArea> pageDataPage = fixedAreaService.findPageData(specification, pageable);
		// 将查询结果压栈
		pushPageDataToValueStack(pageDataPage);
		return SUCCESS;
	}
	
}

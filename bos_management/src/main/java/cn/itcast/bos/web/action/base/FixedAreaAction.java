package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
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
import cn.itcast.bos.service.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class FixedAreaAction extends BaseAction<FixedArea>{
	@Autowired
	private FixedAreaService fixedAreaService;
	//保存添加定区
	@Action(value="fixedArea_save",results={@Result(name="success",location="./pages/base/fixed_area.html",type="redirect")})
	public String save(){
		System.out.println(model);
		fixedAreaService.save(model);
		return SUCCESS;
	}
	//分页查询
	@Action(value="fixedArea_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//分页查询
		Pageable pageable = new PageRequest(page-1,rows);
		//条件查询 spa
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//弄个集合
				List<Predicate> list = new ArrayList<Predicate>();
				//构造查询条件
				if(StringUtils.isNotBlank(model.getId())){
					//根据定区编号查询值
					Predicate p1 = cb.equal(root.get("id").as(String.class),model.getId());
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//调用业务层
		Page<FixedArea> pageData = fixedAreaService.findPageData(specification,pageable);
		//将查询结果压栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}

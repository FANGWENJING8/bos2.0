package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.StandardService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class StandardAction extends BaseAction<Standard>{
	
	@Autowired
	private StandardService standardService;
	//添加
	@Action(value="standard_save",results={@Result(name="success",location="./pages/base/standard.html",type="redirect")})
	public String save(){
		System.out.println("添加....");
		standardService.save(model);
		return SUCCESS;
	}
	//分页列表
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//调用业务层,查询数据结果
		//1.调用业务层查询当前页数据
		Pageable pageable =new PageRequest(page-1,rows);
		Page<Standard> pageData = standardService.findPageData(pageable);
		//压入值栈
		pushPageDataToValueStack(pageData);		
		return SUCCESS;
	}
	//查询所有的收派标准方法
	@Action(value="standard_findAll",
			results={@Result(name="success",type="json")})
	public String findAll(){
		List<Standard> standards = standardService.findAll();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
}

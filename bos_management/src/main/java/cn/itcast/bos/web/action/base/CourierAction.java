package cn.itcast.bos.web.action.base;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;


import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;
import cn.itcast.bos.web.action.common.BaseAction;
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class CourierAction extends BaseAction<Courier>{
	
	//注入Service
	@Autowired
	private CourierService courierService;
	
	//添加快递员
	@Action(value="courier_save",results={@Result(name="success",
			location="./pages/base/courier.html",type="redirect")})
	public String save(){
		courierService.save(model);
	return	SUCCESS;
	}
	//分页查询
	@Action(value="courier_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//封装pageQuery
		Pageable pageable =  new PageRequest(page-1,rows);
		//调用业务层
		Page<Courier> pageData = courierService.findPageData(pageable);
		//压入值栈
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	//属性驱动
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	//作废操作
	@Action(value="courier_delBatch",results={@Result(name="success",location="./pages/base/courier.html",type="redirect")})
	public String delBatch(){
		//按照,分割ids
		String[] idArray = ids.split(",");
		//调用业务层,批量作废
		courierService.updateBatch(idArray);
		return SUCCESS;
	}
	

}

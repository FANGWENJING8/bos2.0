package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.procedure.internal.ProcedureCallMementoImpl.ParameterMemento;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;

//抽取Action公共代码 简化开发
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	//模型驱动
	protected T model;
	@Override
	public T getModel() {
		return model;
	}
	//构造器完成model实例化
	public BaseAction(){
	//构造自雷Action对象,获取继承父类型的泛型
	Type superclass = this.getClass().getGenericSuperclass();
	//获取类型第一个泛型参数
	ParameterizedType parameterizedType=(ParameterizedType) superclass;
	Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
	try {
		model=modelClass.newInstance();
	} catch (Exception e) {
		e.printStackTrace();
		System.out.println("模型构造失败...");
	}
	}
	protected int page;
	protected int rows;
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	//将分页查询结果数据 压入值栈方法
	protected void pushPageDataToValueStack(Page<T>pageData){
		//根据查询结果 封装datagrid需要数据格式
		Map<String,Object> result = new HashMap<String,Object>();
			result.put("total",pageData.getTotalElements());
			result.put("rows",pageData.getContent());
			
		//压入值栈
			ActionContext.getContext().getValueStack().push(result);
		
	}
}

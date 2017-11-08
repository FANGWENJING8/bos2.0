package cn.itcast.bos.web.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Vehicle;
import cn.itcast.bos.service.VehicleService;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class VehicleAction extends BaseAction<Vehicle>{
	@Autowired
	private VehicleService vehicleService;
	@Action(value="vehicle_save",results={@Result(name="success",location="./pages/base/vehicle.html",type="redirect")})
	public String save(){
		vehicleService.save(model);
		return SUCCESS;
	}
}

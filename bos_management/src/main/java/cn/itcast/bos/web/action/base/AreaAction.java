package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;
@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
public class AreaAction extends BaseAction<Area>{
	//注入业务层对象
	@Autowired
	private AreaService areaService;
	//接受上传文件
	private File file;
	
	public void setFile(File file) {
		this.file = file;
	}

	//批量区域数据导入
	@Action(value="area_batchImport")
	public String batchImport() throws IOException, IOException{
		List<Area> areas = new ArrayList<Area>();
		
		//编写解析代码逻辑
		//基于.xls格式解析 HSSF
		//1.加载Excel文件对象
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
		//读取一个sheet
		XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
		//读取sheet中每一行
		for(Row row : sheet) {
			//一行数据 对应一个区域对象
			if(row.getRowNum()==0){
				//第一行跳过
				continue;
			}
			//跳过空行
			if(row.getCell(0)==null 
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			//基于pinyin4j生成城市编码和简码
			//省 城市,区 然后分别都subString切一下0,到length-1
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province=province.substring(0,province.length()-1);
			city=city.substring(0, city.length()-1);
			district=district.substring(0, district.length()-1);
			
			//简码
			//生成pinyin数组
			String[] headArray = PinYin4jUtils.getHeadByString(province + city
					+ district);
			StringBuffer buffer = new StringBuffer();
			//遍历
			for (String headStr : headArray) {
				buffer.append(headStr);
			}
			String shortcode = buffer.toString();
			//再setShortcode加进去
			area.setShortcode(shortcode);
			//城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city,"");
			area.setCitycode(citycode);
			
			areas.add(area);
		}
		//调用业务层
		areaService.saveBatch(areas);
		return NONE;
	}

	//分页查询
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//构造分页查询对象
		Pageable pageable = new PageRequest(page-1, rows);
		//构造条件查询对象
		Specification<Area> specification = new Specification<Area>() {

		@Override
		public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query,
				CriteriaBuilder cb) {
			//创建保存条件集合对象
			List<Predicate> list =new ArrayList<Predicate>();
			//添加条件
			if(StringUtils.isNotBlank(model.getProvince())){
				//根据省份查询 模糊
			Predicate p1 = cb.like(root.get("province").as(String.class), 
						"%"+model.getProvince()+"%");
			list.add(p1);
			}
			if(StringUtils.isNotBlank(model.getCity())){
				//根据城市查询 模糊
			Predicate p2 = cb.like(root.get("city").as(String.class), 
						"%"+model.getCity()+"%");
			list.add(p2);
			}
			if(StringUtils.isNotBlank(model.getDistrict())){
				//根据县区查询 模糊
			Predicate p3 = cb.like(root.get("district").as(String.class), 
						"%"+model.getDistrict()+"%");
			list.add(p3);
			}
			return cb.and(list.toArray(new Predicate[0]));
		}
	};
	//调用业务层 完成查询
	Page<Area> pageData = areaService.findPageData(specification,pageable);
	//压入值栈
	pushPageDataToValueStack(pageData);
	
	return SUCCESS;
	}

}

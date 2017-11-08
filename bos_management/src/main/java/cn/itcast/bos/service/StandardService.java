package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Standard;

public interface StandardService {
	public void save(Standard model);
	
	//分页查询
	public Page<Standard> findPageData(Pageable pageable);
	//查询所有的收派标准
	public List<Standard> findAll();


}

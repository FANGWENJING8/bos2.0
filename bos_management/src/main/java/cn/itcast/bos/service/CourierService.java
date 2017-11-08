package cn.itcast.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {
	public void save(Courier model);
	//分页查询到额方法
	public Page<Courier> findPageData(Pageable pageable);
	public void updateBatch(String[] idArray);
}

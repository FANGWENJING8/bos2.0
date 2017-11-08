package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
//注入Dao对象
	@Autowired
	private CourierRepository courierRepository;
	@Override
	public void save(Courier courier) {
		courierRepository.save(courier);
	}
	@Override
	public Page<Courier> findPageData(Pageable pageable) {
		
		return courierRepository.findAll(pageable);
	}
	@Override
	public void updateBatch(String[] idArray) {
		for(String idStr : idArray){
			Integer id = Integer.parseInt(idStr);
			courierRepository.updateDelTag(id);
		}
	}

}

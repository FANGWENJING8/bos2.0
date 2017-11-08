package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.VehicleRepository;
import cn.itcast.bos.domain.base.Vehicle;
import cn.itcast.bos.service.VehicleService;
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;
	@Override
	public void save(Vehicle vehicle) {
		vehicleRepository.save(vehicle);
	}

}

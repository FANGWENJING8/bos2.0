package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer>,
	JpaSpecificationExecutor<Vehicle>{

}

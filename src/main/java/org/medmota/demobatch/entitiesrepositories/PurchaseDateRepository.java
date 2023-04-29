package org.medmota.demobatch.entitiesrepositories;

import java.util.Date;

import org.medmota.demobatch.entities.PurchaseDate;
import org.medmota.demobatch.entities.Purchaser;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PurchaseDateRepository extends CrudRepository<PurchaseDate, Long> {

	@Query("select * from DIM_EMPLOYEE_PURCHASER where EMAIL = :email")
	public Purchaser findByEmail(@Param("email") String email);
	
	@Query("select * from DIM_EMPLOYEE_PURCHASER where DATE_TIME = :email")
	public PurchaseDate findByDate(@Param("email") Date email);

}

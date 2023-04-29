package org.medmota.demobatch.entitiesrepositories;

import org.medmota.demobatch.entities.Supplier;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SupplierRepository extends CrudRepository<Supplier, Long> {

	@Query("select supplier.id, supplier.name, supplier.address from DIM_SUPPLIER supplier where name = :name ")
	public Supplier findByName(@Param("name") String name);

}

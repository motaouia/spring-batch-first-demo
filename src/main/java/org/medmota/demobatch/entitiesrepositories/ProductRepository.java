package org.medmota.demobatch.entitiesrepositories;

import org.medmota.demobatch.entities.Product;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Long> {
	@Query("select product.id, product.name, product.prdt_type, product.ean_code from DIM_PRODUCT product where EAN_CODE = :eanCode ")
	public Product findByEanCode(@Param("eanCode") String eanCode);

}

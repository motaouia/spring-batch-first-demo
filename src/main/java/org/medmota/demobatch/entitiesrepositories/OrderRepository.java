package org.medmota.demobatch.entitiesrepositories;

import org.medmota.demobatch.entities.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long>  {

}

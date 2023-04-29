package org.medmota.demobatch.writer;

import java.util.List;

import org.medmota.demobatch.dto.ConvertedInputData;
import org.medmota.demobatch.entities.Product;
import org.medmota.demobatch.entities.PurchaseDate;
import org.medmota.demobatch.entities.Purchaser;
import org.medmota.demobatch.entities.Supplier;
import org.medmota.demobatch.entitiesrepositories.OrderRepository;
import org.medmota.demobatch.entitiesrepositories.ProductRepository;
import org.medmota.demobatch.entitiesrepositories.PurchaseDateRepository;
import org.medmota.demobatch.entitiesrepositories.PurchaserRepository;
import org.medmota.demobatch.entitiesrepositories.SupplierRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchWriter implements ItemWriter<ConvertedInputData>  {
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private PurchaserRepository purchaserRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private PurchaseDateRepository purchaseDateRepository;
	
	@Autowired
	private OrderRepository commandRepository;

	@Override
	public void write(List<? extends ConvertedInputData> items) throws Exception {
		items.stream().forEach(item -> {
			Supplier supplier = null;
			Purchaser purchaser = null;
			Product product = null;
			PurchaseDate purchaseDate = null;
			if(item.getOrder().getSupplierId() == null) {
				supplier = supplierRepository.save(item.getSupplier());
				item.getOrder().setSupplierId(supplier.getId());
			}
			if(item.getOrder().getPurchaserId() == null) {
				purchaser = purchaserRepository.save(item.getPurchaser());
				item.getOrder().setPurchaserId(purchaser.getId());
			}
			if(item.getOrder().getProductId() == null) {
				product = productRepository.save(item.getProduct());
				item.getOrder().setProductId(product.getId());
			}
			if(item.getOrder().getDateId() == null) {
				purchaseDate = purchaseDateRepository.save(item.getPurchaseDate());
				item.getOrder().setDateId(purchaseDate.getId());
			}
			commandRepository.save(item.getOrder());
		});

		
	}

}

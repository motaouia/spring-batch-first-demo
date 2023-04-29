package org.medmota.demobatch.processor;

import org.medmota.demobatch.dto.ConvertedInputData;
import org.medmota.demobatch.dto.InputData;
import org.medmota.demobatch.entities.Order;
import org.medmota.demobatch.entities.Product;
import org.medmota.demobatch.entities.PurchaseDate;
import org.medmota.demobatch.entities.Purchaser;
import org.medmota.demobatch.entities.Supplier;
import org.medmota.demobatch.entitiesrepositories.ProductRepository;
import org.medmota.demobatch.entitiesrepositories.PurchaseDateRepository;
import org.medmota.demobatch.entitiesrepositories.PurchaserRepository;
import org.medmota.demobatch.entitiesrepositories.SupplierRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class BatchProcessor implements ItemProcessor<InputData, ConvertedInputData> {

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private PurchaserRepository purchaserRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PurchaseDateRepository purchaseDateRepository;

	@Override
	public ConvertedInputData process(InputData item) throws Exception {
		ConvertedInputData convertedInputData = new ConvertedInputData();
		Order order = new Order();

		Supplier supplier = supplierRepository.findByName(item.getSupplierName());
		Purchaser purchaser = purchaserRepository.findByEmail(item.getPurchaserEmail());
		Product product = productRepository.findByEanCode(item.getProductEanCode());
		PurchaseDate purchaseDate = purchaseDateRepository.findByDate(item.getTransactionDate());

		if (supplier == null) {
			supplier = Supplier.of(null, item.getSupplierName(), item.getSupplierAddress());
			convertedInputData.setSupplier(supplier);
		} else {
			order.setSupplierId(supplier.getId());
		}

		if (purchaser == null) {
			purchaser = Purchaser.of(null, item.getPurchaserFirstName(), item.getPurchaserLastName(),
					item.getPurchaserEmail());
			convertedInputData.setPurchaser(purchaser);
		} else {
			order.setPurchaserId(purchaser.getId());
		}

		if (product == null) {
			product = Product.of(null, item.getProductName(), item.getProductType(), item.getProductEanCode());
			convertedInputData.setProduct(product);
			order.setQuantity(item.getProductQuantity());
			order.setAmount(item.getProductAmount() * item.getProductQuantity());
		} else {
			order.setProductId(product.getId());
			order.setQuantity(item.getProductQuantity());
			order.setAmount(item.getProductAmount() * item.getProductQuantity());
		}

		if (purchaseDate == null) {
			purchaseDate = PurchaseDate.of(null, item.getTransactionDate());
			convertedInputData.setPurchaseDate(purchaseDate);
		} else {
			order.setDateId(purchaseDate.getId());
		}

		convertedInputData.setOrder(order);

		return convertedInputData;
	}

}

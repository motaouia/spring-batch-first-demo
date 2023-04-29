package org.medmota.demobatch.dto;

import org.medmota.demobatch.entities.Order;
import org.medmota.demobatch.entities.Product;
import org.medmota.demobatch.entities.PurchaseDate;
import org.medmota.demobatch.entities.Purchaser;
import org.medmota.demobatch.entities.Supplier;

import lombok.Data;

@Data
public class ConvertedInputData {
	private Supplier supplier;

	private Purchaser purchaser;

	private Product product;

	private PurchaseDate purchaseDate;

	private Order order;

}

package com.example.demo.clasesMercadoPago;

import java.math.BigDecimal;

public class Item {
	 /** Item code. */
	public String id;

	  /** Item name. */
	public String title;

	  /** Long item description. */
	public String description;

	  /** Image URL. */
	public String pictureUrl;

	  /** Category of the item. */
	public String categoryId;

	  /** Item's quantity. */
	public Integer quantity;

	  /** Unit price. */
	public BigDecimal unitPrice;

	  /** Currency ID. ISO_4217 code. */
	public String currencyId;

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public String getCurrencyId() {
		return currencyId;
	}
}

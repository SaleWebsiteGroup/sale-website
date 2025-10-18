package vn.project.Service;

import java.util.List;

import vn.project.Entity.Products;

public class ProductList {
	private List<Products> products;

	public ProductList(List<Products> products) {
		this.products = products;
	}

	public ProductIterator iterator() {
		return new ProductListIterator();
	}

	private class ProductListIterator implements ProductIterator {
		private int index = 0;

		@Override
		public boolean hasNext() {
			return index < products.size();
		}

		@Override
		public Products next() {
			if (hasNext()) {
				return products.get(index++);
			}
			return null;
		}
	}
}

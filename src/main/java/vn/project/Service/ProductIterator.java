package vn.project.Service;

import vn.project.Entity.Products;

public interface ProductIterator {
	boolean hasNext();

	Products next();
}

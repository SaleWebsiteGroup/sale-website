package vn.project.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Brands;
import vn.project.Entity.Categories;
import vn.project.Entity.Products;
import vn.project.Entity.Suppliers;
import vn.project.Repository.IBrandRepository;
import vn.project.Repository.ICategoryRepositoy;
import vn.project.Repository.IProductRepository;
import vn.project.Repository.ISupplierRepository;
import vn.project.Service.IProductService;

@Service
public class ProductService implements IProductService {

	@Autowired
	IProductRepository productRepository;

	@Autowired
	IBrandRepository brandRepository;

	@Autowired
	ISupplierRepository supplierRepository;

	@Autowired
	ICategoryRepositoy categoryRepository;

	public ProductService(IProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public <S extends Products> S save(S entity) {
		return productRepository.save(entity);
	}

	@Override
	public List<Products> findAll() {
		return productRepository.findAll();
	}

	@Override
	public List<ProductsDTO> findAllDTO(){
		List<Products> list = findAll();

		return ConvertProductToProductDTO(list);
	}

	@Override
	public void deleteById(int id) {
		Optional<Products> product = productRepository.findById(id);
		if (!product.isEmpty()) {
			Products pro = product.get();
			productRepository.deleteById(pro.getProductid());
		}
	}

	@Override
	public void delete(Products product) {
		Optional<Products> productpresent = productRepository.findById(product.getProductid());
		if (!productpresent.isEmpty()) {
			Products productdirect = productpresent.get();
			productRepository.delete(productdirect);
		}

	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();
	}

	@Override
	public List<Products> findByName(String name) {
		return productRepository.findByProductname(name);
	}
	
	

	@Override
	public List<ProductsDTO> findByProductnameContainingDTO(String productname) {
		return ConvertProductToProductDTO(productRepository.findByProductnameContaining(productname));
	}

	@Override
	public Optional<Products> findById(int id) {
		return productRepository.findByProductid(id);
	}

	@Override
	public List<Products> findbySupplier(String supplier) {
		List<Suppliers> listsupplier = supplierRepository.findBySuppliernameContaining(supplier);
		if (!listsupplier.isEmpty()) {
			Suppliers supplierpresent = listsupplier.getFirst();
			return productRepository.findBySupplierid(supplierpresent.getSupplierid());
		}
		return null;
	}

	@Override
	public List<Products> findbyBrand(String brand) {
		List<Brands> listbrand = brandRepository.findByBrandnameContaining(brand);
		if (!listbrand.isEmpty()) {
			Brands brandpresent = listbrand.getFirst();
			return productRepository.findByBrandid(brandpresent.getBrandid());
		}
		return null;
	}

	@Override
	public void createProduct(Products product) {
		productRepository.save(product);
	}

	@Override
	public List<ProductsDTO> findbyBrandDTO(String brand) {
		try {
			List<Products> products = findbyBrand(brand);
			List<ProductsDTO> productDTO = ConvertProductToProductDTO(products);
			return productDTO;
		}catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public List<Products> findbyCategory(String category) {
		List<Categories> listcategory = categoryRepository.findByCategorynameContaining(category);
		if (!listcategory.isEmpty()) {
			Categories categorypresent = listcategory.getFirst();
			return productRepository.findByCategoryid(categorypresent.getCategoryid());
		}
		return null;
	}


	@Override
	public List<ProductsDTO> findbyCategoryDTO(String category){
		List<Products> products = findbyCategory(category);
		List<ProductsDTO> productDTO = ConvertProductToProductDTO(products);

		return productDTO;
	}


	@Override
	public ProductsDTO findbyIdDTO(int id) {
		try {
			Products product = findById(id).get();
			ProductsDTO productdto = new ProductsDTO();

			productdto.setProductid(product.getProductid());
			productdto.setProductname(product.getProductname());
			productdto.setDescription(product.getDescription());
			productdto.setImageurl(product.getImageurl());
			productdto.setPrice(Long.toString(product.getPrice()));
			productdto.setStockquantity(String.valueOf(product.getStockquantity()));
			Brands brand1 = brandRepository.findById(product.getBrandid()).isPresent()
					? brandRepository.findById(product.getBrandid()).get()
					: null;
			if (brand1 != null) {
				productdto.setBrand(brand1.getBrandname());
			} else {
				productdto.setBrand("Unknow brand name");
			}

			Categories cate = categoryRepository.findById(product.getCategoryid()).isPresent()
					? categoryRepository.findById(product.getCategoryid()).get()
					: null;
			if (cate != null) {
				productdto.setCategory(cate.getCategoryname());
			} else {
				productdto.setCategory("Unknow category name");
			}
			Suppliers supplier = supplierRepository.findById(product.getSupplierid()).isPresent()
					? supplierRepository.findById(product.getSupplierid()).get()
					: null;
			if (supplier != null) {
				productdto.setSupplier(supplier.getSuppliername());
			}else {
				productdto.setSupplier("Unknow supplier name");
			}

			return productdto;
		}
		catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<ProductsDTO> ConvertProductToProductDTO(List<Products> products){
		List<ProductsDTO> productDTO = new ArrayList<>();
		
		if(products.isEmpty()) {
			return null;
		}

		for (Products product : products) {
			ProductsDTO productdto = new ProductsDTO();
			productdto.setProductid(product.getProductid());
			productdto.setProductname(product.getProductname());
			productdto.setDescription(product.getDescription());
			productdto.setImageurl(product.getImageurl());
			productdto.setPrice(Long.toString(product.getPrice()));
			productdto.setStockquantity(String.valueOf(product.getStockquantity()));

			Brands brand1 = brandRepository.findById(product.getBrandid()).isPresent()
					? brandRepository.findById(product.getBrandid()).get()
					: null;
			if (brand1 != null) {
				productdto.setBrand(brand1.getBrandname());
			} else {
				productdto.setBrand("Unknow brand name");
			}

			Categories cate = categoryRepository.findById(product.getCategoryid()).isPresent()
					? categoryRepository.findById(product.getCategoryid()).get()
					: null;
			if (cate != null) {
				productdto.setCategory(cate.getCategoryname());
			} else {
				productdto.setCategory("Unknow category name");
			}
			Suppliers supplier = supplierRepository.findById(product.getSupplierid()).isPresent()
					? supplierRepository.findById(product.getSupplierid()).get()
					: null;
			if (supplier != null) {
				productdto.setSupplier(supplier.getSuppliername());
			}else {
				productdto.setSupplier("Unknow supplier name");
			}

			productDTO.add(productdto);
		}
		return productDTO;

	}

}

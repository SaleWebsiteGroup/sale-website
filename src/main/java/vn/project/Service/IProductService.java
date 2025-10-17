package vn.project.Service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Products;

@Service
public interface IProductService {

	public <S extends Products> S save(S entity);

	public List<Products> findAll();

	public void deleteById(int id);

	public void delete(Products product);

	public void deleteAll();

	public List<Products> findByName(String name);

	public Optional<Products> findById(int id);

	public List<Products> findbySupplier(String supplier);

	public List<Products> findbyBrand(String brand);

	public void createProduct(Products product);

	List<ProductsDTO> findbyBrandDTO(String brand);

	List<ProductsDTO> findbyCategoryDTO(String category);

	List<Products> findbyCategory(String category);

	ProductsDTO findbyIdDTO(int id);

	List<ProductsDTO> findAllDTO();

	List<ProductsDTO> findByProductnameContainingDTO(String productname);

	List<ProductsDTO> ConvertProductToProductDTO(List<Products> products);

}

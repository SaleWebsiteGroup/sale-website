package vn.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.project.Entity.Order_Products;
import vn.project.Entity.Products;


@Repository
public interface IOrder_ProductsRepository extends JpaRepository<Order_Products, Integer>{

	void deleteByProduct(Products product);
}

package vn.project.Controllers.CategoryControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.project.DTO.ProductsDTO;
import vn.project.Service.IProductService;
import vn.project.Entity.Products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
public class AllProductController {

	@Autowired(required = true)
	IProductService productservice;

	// Code cũ chưa có phaan trang
//	@GetMapping("/allproduct")
//	public String index(Model model) {
//
//		List<ProductsDTO> list = productservice.findAllDTO();
//		model.addAttribute("products", list);
//		return "category/allproduct";
//	}

	@GetMapping("/allproduct")
	public String index(
			Model model,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "9") int size,
			@RequestParam(required = false) String sort
	) {
		Page<ProductsDTO> allProductPage;

		if ("priceDesc".equals(sort)) {
			allProductPage = productservice.findAllSortedByPriceDesc(PageRequest.of(page, size));
		} else if ("priceAsc".equals(sort)) {
			allProductPage = productservice.findAllSortedByPriceAsc(PageRequest.of(page, size));
		} else {
			allProductPage = productservice.findAllDTO(PageRequest.of(page, size));
		}

		model.addAttribute("allProductPage", allProductPage);
		model.addAttribute("currentSort", sort);

		return "category/allproduct";
	}
}

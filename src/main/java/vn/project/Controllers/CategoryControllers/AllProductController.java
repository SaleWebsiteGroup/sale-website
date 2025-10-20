package vn.project.Controllers.CategoryControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.project.DTO.ProductsDTO;
import vn.project.Service.IProductService;
import vn.project.Service.IBrandService;

@Controller
@RequestMapping("/product")
public class AllProductController {

	@Autowired(required = true)
	IProductService productservice;

	@Autowired
	IBrandService brandService;

	@GetMapping("/allproduct")
	public String index(Model model, @RequestParam(name = "sortField", defaultValue = "productid") String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
			@RequestParam(required = false) List<String> brands,
			@RequestParam(required = false) String priceRange) {

		Long minPrice = null;
		Long maxPrice = null;

		if (priceRange != null && !priceRange.isEmpty()) {
			try {
				String[] prices = priceRange.split(";");
				minPrice = Long.parseLong(prices[0]);
				maxPrice = Long.parseLong(prices[1]);
			} catch (Exception e) {
				// Ignore if parsing fails
			}
		}

		List<ProductsDTO> list = productservice.findAllDTO(sortField, sortDir, brands, minPrice, maxPrice);
		model.addAttribute("products", list);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("brands", brandService.findAll());
		model.addAttribute("selectedBrands", brands);
		model.addAttribute("priceRange", priceRange);
		return "category/allproduct";
	}
}

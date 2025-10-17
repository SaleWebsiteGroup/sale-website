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

@Controller
@RequestMapping("/product")
public class FindProductController {
	
	@Autowired
	IProductService productService;
	
	@GetMapping("findproduct")
	public String findByProductNameContaining(@RequestParam String query, Model model) {
		try {
			List<ProductsDTO> products = productService.findByProductnameContainingDTO(query);
			
			model.addAttribute("products", products);
			
			return "category/findproduct";
			
			
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Đã xảy ra lỗi");
			return "rediect:/anyerror";
		}
	}
}

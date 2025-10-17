package vn.project.Controllers.CategoryControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.project.DTO.ProductsDTO;
import vn.project.Service.IProductService;

@Controller
@RequestMapping("/product")
public class ProductDetailController {

	@Autowired(required = true)
	IProductService productService;

	@GetMapping("/productdetail/{id}")
	public String index(@PathVariable String id, Model model, @ModelAttribute String messageSuccess) {
		try {
			int idproduct = Integer.valueOf(id);
			ProductsDTO product = productService.findbyIdDTO(idproduct);
			model.addAttribute("products", product);
			if(!messageSuccess.isEmpty())
				model.addAttribute("messageSuccess", messageSuccess);
			
			return "category/productdetail";

		} catch (Exception e) {
			model.addAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/505";
		}
	}
}

package vn.project.Controllers.CategoryControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.project.DTO.ProductsDTO;
import vn.project.Service.IProductService;

@Controller
@RequestMapping("/product")
public class WomenProductController {

	@Autowired(required=true)
	IProductService productservice;

    @GetMapping("/womenproduct")
    public String index(Model model) {

    	List<ProductsDTO> list = productservice.findbyCategoryDTO("Nữ");
    	/*System.out.println(list);*/
    	model.addAttribute("products", list);
		 return "category/womenproduct";
    }
}

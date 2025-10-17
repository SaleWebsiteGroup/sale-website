package vn.project.Controllers.AdminControllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.project.DTO.ProductsDTO;
import vn.project.Entity.Products;
import vn.project.Service.IBrandService;
import vn.project.Service.ICartService;
import vn.project.Service.ICategoryService;
import vn.project.Service.IProductService;
import vn.project.Service.ISupplierService;


@Controller
@RequestMapping("/vendor")
public class ProductManagementController {

	@Autowired
	IProductService productService;

	@Autowired
	IBrandService brandService;

	@Autowired
	ICategoryService cateService;

	@Autowired
	ISupplierService supplierService;

	@Autowired
	ICartService cartService;

	@GetMapping("/productmanager")
	public String manageProducts(Model model) {
		try {

			List<ProductsDTO> listProductsDTOs = productService.findAllDTO();
			model.addAttribute("productlist", listProductsDTOs);
			model.addAttribute("brands", brandService.findAll());
			model.addAttribute("category", cateService.findAll());
			model.addAttribute("supplier", supplierService.findAll());

			return "admin/productmanagement";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/403";
		}
	}

	@GetMapping("/productmanager/delete/{id}")
	public String deleteProduct(@PathVariable("id") String idproduct, Model model) {
		try {
			int id = Integer.valueOf(idproduct);

			Optional<Products> product = productService.findById(id);

			if(product.isPresent()) {
				cartService.deleteAllByproductid(id);
				productService.delete(product.get());
			}

			return "redirect:/vendor/productmanager";


		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/anyerror";
		}

	}

	@PostMapping("/productmanager/add")
	public String createProduct(@ModelAttribute ProductsDTO productsDTO, Model model) {
		try {
			System.out.println(productsDTO);
			Products newproduct = new Products();
			newproduct.setProductname(productsDTO.getProductname());
			newproduct.setBrandid(brandService.findIdByBrandnameContaining(productsDTO.getBrand()).get());
			newproduct.setCategoryid(cateService.findidByCategorynameContaining(productsDTO.getCategory()).get());
			newproduct.setSupplierid(supplierService.findidBySuppliernameContaining(productsDTO.getSupplier()).get());
			newproduct.setDescription(productsDTO.getDescription());
			newproduct.setImageurl(productsDTO.getImageurl());
			newproduct.setPrice(Long.valueOf(productsDTO.getPrice()));
			newproduct.setStockquantity(Integer.valueOf(productsDTO.getStockquantity()));
			productService.save(newproduct);
			return "redirect:/vendor/productmanager";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/anyerror";
		}
	}

	@PostMapping("/productmanager/edit")
	public String updateProduct(@ModelAttribute ProductsDTO product, RedirectAttributes redirectAttributes) {
		try {
			System.out.print(product);

			if(product != null) {
				Integer brandid = brandService.findIdByBrandnameContaining(product.getBrand()).get();
				Integer cateid = cateService.findidByCategorynameContaining(product.getCategory()).get();
				Integer supplierid = supplierService.findidBySuppliernameContaining(product.getSupplier()).get();

				Products editproduct = Products.builder()
						.productid(product.getProductid())
						.brandid(brandid)
						.categoryid(cateid)
						.description(product.getDescription())
						.imageurl(product.getImageurl())
						.price(Integer.valueOf(product.getPrice()))
						.productname(product.getProductname())
						.stockquantity(Integer.valueOf(product.getStockquantity()))
						.supplierid(supplierid)
						.build();

				productService.save(editproduct);
				redirectAttributes.addFlashAttribute("message", "Chỉnh sửa thành công");
				return "redirect:/vendor/productmanager";
			}else {
				redirectAttributes.addFlashAttribute("message", "Không tìm thấy sản phẩm");
				return "redirect:/vendor/productmanager";
			}
		}catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Đã xảy ra lỗi");
			return "redirect:/anyerror";
		}

	}

}

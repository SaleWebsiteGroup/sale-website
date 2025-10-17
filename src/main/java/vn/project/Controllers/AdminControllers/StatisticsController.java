package vn.project.Controllers.AdminControllers;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.project.Entity.Order_Products;
import vn.project.Entity.Orders;
import vn.project.Entity.Users;
import vn.project.Service.IOrderService;
import vn.project.Service.IOrder_ProductService;
import vn.project.Service.IUserService;

@Controller
@RequestMapping("/vendor")
public class StatisticsController {
	
	@Autowired
	IOrderService orderService;
	
	@Autowired
	IOrder_ProductService order_ProductService;
	
	@Autowired
	IUserService userService;
	
    @GetMapping("/statistics")
    public String statistics(Model model) {
    	
    	// Tong doanh thu
    	BigInteger revenue = BigInteger.ZERO;
    	
    	List<Orders> listorder = orderService.findAll();
    	
    	if(!listorder.isEmpty()) {
    		for(Orders order : listorder) {
        		revenue = revenue.add(BigInteger.valueOf(order.getTotalamount())); 
        	}
    		model.addAttribute("revenue", revenue);
    	}
    	
    	// Tong so san phamr bans duoc
    	
    	Integer saleproduct = 0;
    	
    	List<Order_Products> listorderProducts = order_ProductService.findAll();
    	
    	if(!listorderProducts.isEmpty()) {
    		for(Order_Products order_Products : listorderProducts) {
    			saleproduct = saleproduct + order_Products.getQuantity();
    		}
    		model.addAttribute("saleproduct", saleproduct);
    	}
    	
    	// Tong so khach hang
    	Long guestquantity = 0L;
    	guestquantity = userService.count();
    	model.addAttribute("guestquantity", guestquantity);
    	
    	//doanh thu theo thang
    	LocalDateTime localDateTime = LocalDateTime.now();
    	Map<Month, Long> RevenueForMonth = new HashMap<>();
    	
    	for(int i = 0; i < 6; i++) {
    		Long newRevenue = 0L;
    		LocalDateTime startOfMonth = localDateTime.minusMonths(i).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).truncatedTo(ChronoUnit.DAYS);
            
            List<Orders> list = orderService.findByOrderdateBetween(startOfMonth, endOfMonth);
            newRevenue = returnRevenue(list);
            
            RevenueForMonth.put(startOfMonth.getMonth(), newRevenue);
    	}
    	model.addAttribute("RevenueForMonth", RevenueForMonth); // lấy trên view bằng revenue : RevenueForMonth, revenue.key với revenue.value
    	
    	
    	//Doanh thu theo ngay, 7 ngày gan nhat
    	Map<DayOfWeek, Long> RevenueForWeek = new HashMap<>();
    	
    	for(int i = 0; i < 8; i++) {
    		Long newRevenue = 0L;
    		LocalDateTime daystart = localDateTime.minusDays(i).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime dayend = daystart.plusDays(1).truncatedTo(ChronoUnit.DAYS);
            
            List<Orders> list = orderService.findByOrderdateBetween(daystart, dayend);
            newRevenue = returnRevenue(list);
            
            RevenueForWeek.put(daystart.getDayOfWeek(), newRevenue);
    	}
    	model.addAttribute("RevenueForWeek", RevenueForWeek);
    	
    	
    	// Doanh thu theo quys
    	Map<Integer, Long> RevenueForQuarter = new HashMap<>();
    	RevenueForQuarter = getRevenueForQuarters(localDateTime.getYear());
    	model.addAttribute("RevenueForQuarter", RevenueForQuarter);
    	
        return "admin/statistics";
    }
    
    private Long returnRevenue(List<Orders> list) {
    	if(list.isEmpty()) {
    		return 0L;
    	}
    	
    	Long Revenue = 0L; 
    	
    	for(Orders order : list) {
    		Revenue = Revenue + order.getTotalamount();
    	}
    	return Revenue;
    }
    
    public Map<Integer, Long> getRevenueForQuarters(int year) {
        Map<Integer, Long> revenueForQuarters = new HashMap<>();

        // Quý 1: Từ 01/01 đến 31/03
        LocalDateTime startOfQ1 = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0, 0, 0);
        LocalDateTime endOfQ1 = LocalDateTime.of(year, Month.MARCH, 31, 23, 59, 59, 999999999);
        long revenueQ1 = returnRevenue(orderService.findByOrderdateBetween(startOfQ1, endOfQ1));
        revenueForQuarters.put(1, revenueQ1);

        // Quý 2: Từ 01/04 đến 30/06
        LocalDateTime startOfQ2 = LocalDateTime.of(year, Month.APRIL, 1, 0, 0, 0, 0);
        LocalDateTime endOfQ2 = LocalDateTime.of(year, Month.JUNE, 30, 23, 59, 59, 999999999);
        long revenueQ2 = returnRevenue(orderService.findByOrderdateBetween(startOfQ2, endOfQ2));
        revenueForQuarters.put(2, revenueQ2);

        // Quý 3: Từ 01/07 đến 30/09
        LocalDateTime startOfQ3 = LocalDateTime.of(year, Month.JULY, 1, 0, 0, 0, 0);
        LocalDateTime endOfQ3 = LocalDateTime.of(year, Month.SEPTEMBER, 30, 23, 59, 59, 999999999);
        long revenueQ3 = returnRevenue(orderService.findByOrderdateBetween(startOfQ3, endOfQ3));
        revenueForQuarters.put(3, revenueQ3);

        // Quý 4: Từ 01/10 đến 31/12
        LocalDateTime startOfQ4 = LocalDateTime.of(year, Month.OCTOBER, 1, 0, 0, 0, 0);
        LocalDateTime endOfQ4 = LocalDateTime.of(year, Month.DECEMBER, 31, 23, 59, 59, 999999999);
        long revenueQ4 = returnRevenue(orderService.findByOrderdateBetween(startOfQ4, endOfQ4));
        revenueForQuarters.put(4, revenueQ4);

        return revenueForQuarters;
    }
}


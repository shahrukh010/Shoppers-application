package com.shopme.shopingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class ShopingCartRestController {

	@Autowired
	private ShopingCartService cartService;

	@Autowired
	private CustomerService customerService;

	/*
	 *here you can see addProduct end point is inside RestController 
	 *because of without reload page user can add/remove his product
	 *so based on situation we can decide where we have to use RestController or Controller
	 * 
	 * 
	 */
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable(name = "productId") Integer productId,
			@PathVariable(name = "quantity") Integer quantity, HttpServletRequest request) {

		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updateQuantity = cartService.addProduct(productId, quantity, customer);

			return " you just added " + updateQuantity + "item to your cart";
		} catch (CustomerNotFoundException ex) {
			return "You must login to add product in to cart";
		} catch (ShopingCartException ex) {

			return "You can not add more then " + 5 + " items";
		}

	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {

		String customerEmail = Utility.getEmailOfAuthenticatedCustomer(request);

		if (customerEmail == null)
			throw new CustomerNotFoundException("Customer Not Found");
		else {
			return customerService.getCustomerByEmail(customerEmail);

		}
	}

}
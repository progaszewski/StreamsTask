package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import services.CustomerService;
import services.CustomerServiceInterface;
import entities.Customer;
import entities.Product;

public class CustomerServiceTests {

	@Test
	public void testFindByName() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		List<Customer> res = cs.findByName("Customer: 1");
		
		assertNotNull("Result can't be null", res);
		assertEquals(1, res.size());
		//System.out.println(res.get(0).getName());
	}
	
	@Test
	public void testFindByField() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		List<Customer> result = cs.findByField("id", 3);
		assertNotNull("Result can't be null", result);
		assertEquals(1, result.size());
		//System.out.println(res.get(0).getName());
	}
	@Test
	public void testCustomersWhoBoughtMoreThan() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		List<Customer> result = cs.customersWhoBoughtMoreThan(6);
		assertNotNull("Result can't be null", result);
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testCustomersWhoSpentMoreThan() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		List<Customer> result = cs.customersWhoSpentMoreThan(2.0);
		assertNotNull("Result can't be null", result);
		assertTrue(result.size() > 0);
		for(Customer c : result){
			double sum = 0;
			for(Product p : c.getBoughtProducts()){
				sum += p.getPrice();
			}

			assertTrue(sum > 2.0);
		}
		

	}
	
	@Test
	public void testCustomersWithNoOrders() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		List<Customer> result = cs.customersWithNoOrders();
		assertNotNull("Result can't be null", result);
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testAddProductToAllCustomers() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		List<Customer> result = cs.customersWhoBoughtMoreThan(0);
		int oldSize = result.size();
		
		cs.addProductToAllCustomers(new Product(0, "Product: 0", 1.99));
		result = cs.customersWhoBoughtMoreThan(0);
		assertNotNull("Result can't be null", result);
		assertTrue(result.size() > oldSize);
	}
	
	@Test
	public void testAvgOrders() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		double result = cs.avgOrders(false);
		System.out.println(result);
		
		assertTrue(result > 0.0);
		
	}
	
	@Test
	public void testWasProductBought() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		boolean result1 = cs.wasProductBought(new Product(4, "Product: 4", 0.4));
		boolean result2 = cs.wasProductBought(new Product(-1, "N/A", 99999.99));
		assertTrue(result1);
		assertTrue(!result2);
	}
	
	@Test
	public void testMostPopularProduct() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		List<Product> result = cs.mostPopularProduct();
		
		System.out.println(result);
		
		assertNotNull("Result can't be null", result);
		assertTrue(result.size() > 0);
	}
	
	@Test
	public void testCountBuys() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		int result = cs.countBuys(new Product(4, "Product: 4", 0.4));
		
		assertEquals(7, result);
	}
	
	@Test
	public void testCustomersWhoBought() {
		CustomerServiceInterface cs = new CustomerService(DataProducer.getTestData(10));
		
		int result = cs.countCustomersWhoBought(new Product(4, "Product: 7", 0.4));
		assertEquals(0, result);
	}
	
}

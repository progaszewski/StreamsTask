package services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import entities.Customer;
import entities.Product;

public class CustomerService implements CustomerServiceInterface {

	private List<Customer> customers;

	public CustomerService(List<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public List<Customer> findByName(String name) {
		Optional<Customer> findCustomer = this.customers.stream().filter(c -> c.getName().equals(name)).findAny();
		
		if(findCustomer.isPresent()){
			Customer cust = findCustomer.get();
			return new ArrayList<Customer>(){{
				add(cust);
			}};
		}
		
		return null;
	}

	@Override
	public List<Customer> findByField(String fieldName, Object value) {
		Optional<Customer> findCustomer = this.customers.stream()
				.filter(c -> {
					
						try {
							Field f = Customer.class.getDeclaredField(fieldName);
							f.setAccessible(true);
							Object object = f.get(c);
							//if(object != null){
								return object.equals(value);
							//}
							//return false;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}					
					return false;
				}).findAny();
		if(findCustomer.isPresent()){
			return new ArrayList<Customer>(){
				{
					add(findCustomer.get());
				}
			};
		}
		
		return null;
	}

	@Override
	public List<Customer> customersWhoBoughtMoreThan(int number) {
		return this.customers.stream()
				.filter(c -> {
					if(number < c.getBoughtProducts().stream().count()){
						return true;
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> customersWhoSpentMoreThan(double price) {
		return this.customers.stream()
				.filter(c -> {
					if(price < c.getBoughtProducts().stream().mapToDouble(p -> p.getPrice()).sum()){
						return true;
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<Customer> customersWithNoOrders() {
		return this.customers.stream()
				.filter(c -> {
					if(0 == c.getBoughtProducts().stream().count()){
						return true;
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
	}

	@Override
	public void addProductToAllCustomers(Product p) {
		this.customers.stream()
		.forEach(c -> c.addProduct(p));
	}

	@Override
	public double avgOrders(boolean includeEmpty) {
		// TODO Auto-generated method stub
		 OptionalDouble aO = this.customers.stream()
				.mapToDouble(c -> c.getBoughtProducts().stream().mapToDouble(p -> p.getPrice()).sum()
				).average();
		 
		 if(aO.isPresent()){
			 return aO.getAsDouble();
		 }
		 
		 return 0.0;
	}

	@Override
	public boolean wasProductBought(Product p) {

		return this.customers.stream().anyMatch(c -> c.getBoughtProducts().stream().anyMatch(pro -> pro.equals(p)));
		
	}

	@Override
	public List<Product> mostPopularProduct() {
		// TODO Auto-generated method stub

		Product pro = this.customers.stream().reduce((s, z) -> {
			
			s.getBoughtProducts().addAll(z.getBoughtProducts());
			return s;
		}).get().getBoughtProducts().stream().collect(Collectors.groupingBy(Product::getId))
		.entrySet().stream().max(Comparator.comparing(e -> e.getValue().size())).map(e -> e.getValue().get(0)).get();
		
		return new ArrayList<Product>(){
			{
				add(pro);
			}
		};
	}

	@Override
	public int countBuys(Product p) {

		return (int) this.customers.stream().mapToLong(c -> c.getBoughtProducts().stream().filter(pro -> pro.equals(p)).count()).sum();
	}

	@Override
	public int countCustomersWhoBought(Product p) {
		
		return (int) this.customers.stream().filter(c -> c.getBoughtProducts().stream().anyMatch(pro -> pro.equals(p))).count();
	}

}

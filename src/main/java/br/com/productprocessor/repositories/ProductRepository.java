package br.com.productprocessor.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.productprocessor.models.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

	boolean existsByProductAndQuantityAndPriceAndTypeAndIndustryAndOrigin(String product, int quantity, String price,
																			String type, String industry, String origin);

	Page<Product> findByProduct(String product, Pageable pageable);
	
	Page<Product> findAll(Pageable pageable);

	List<Product> findByProductOrderById(String productName);

}

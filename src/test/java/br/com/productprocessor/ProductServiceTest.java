package br.com.productprocessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.productprocessor.controllers.dto.Lojista;
import br.com.productprocessor.models.Product;
import br.com.productprocessor.repositories.ProductRepository;
import br.com.productprocessor.services.ProductService;

public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllProducts() {
    	Pageable pageable = PageRequest.of(0, 10, Sort.by("product"));
        Page<Product> expectedProducts = createProducts(pageable);
        
        when(repository.findAll(pageable)).thenReturn((Page<Product>) expectedProducts);
        Iterable<Product> actualProducts = service.findAll(pageable);

        assertThat(actualProducts).isEqualTo(expectedProducts);
        verify(repository).findAll(pageable);
    }

    @Test
    void shouldSaveProduct() {
        Product product = createProduct("Product 1");
        Product expectedProduct = createProduct("Product 1");
        when(repository.save(product)).thenReturn(expectedProduct);

        Product actualProduct = service.save(product);

        assertThat(actualProduct).isEqualTo(expectedProduct);
        verify(repository).save(product);
    }

    @Test
    void shouldHandleDuplicateKeyException() {
        Product product = createProduct("Product 1");
        when(repository.save(product)).thenThrow(new DuplicateKeyException(""));

        Product actualProduct = service.save(product);

        assertThat(actualProduct).isNull();
        verify(repository).save(product);
    }
    
    @Test
    public void testCalculateProductDivision() {
        String productName = "Product1";
        int quantidadeLojas = 2;
        List<Product> products = new ArrayList<>();
        products.add(createProduct(productName));
        products.add(createProduct(productName));
        when(repository.findByProductOrderById(anyString())).thenReturn(products);
        List<Lojista> lojistas = service.calculateProductSplit(productName, quantidadeLojas);
        assertEquals(2, lojistas.size());
        assertEquals(4, lojistas.get(0).getQtde());
        assertEquals(80.00, lojistas.get(0).getFinanceiro());
        assertEquals(20.00, lojistas.get(0).getPrecoMedio());
        
        assertEquals(4, lojistas.get(1).getQtde());
        assertEquals(80.00, lojistas.get(1).getFinanceiro());
        assertEquals(20.0, lojistas.get(1).getPrecoMedio());
    }
    
    @Test
    public void testCalculateProductDivisionWithRemaining() {
        String productName = "Product1";
        int quantidadeLojas = 2;
        List<Product> products = new ArrayList<>();
        products.add(createProduct(productName));
        products.add(createProductForRemaining(productName));
        when(repository.findByProductOrderById(anyString())).thenReturn(products);
        List<Lojista> lojistas = service.calculateProductSplit(productName, quantidadeLojas);
        assertEquals(2, lojistas.size());
        assertEquals(5, lojistas.get(0).getQtde());
        assertEquals(100.00, lojistas.get(0).getFinanceiro());
        assertEquals(20.00, lojistas.get(0).getPrecoMedio());
        
        assertEquals(4, lojistas.get(1).getQtde());
        assertEquals(80.00, lojistas.get(1).getFinanceiro());
        assertEquals(20.0, lojistas.get(1).getPrecoMedio());
    }


    private Product createProduct(String product) {
        return new Product(product, 4, "$20.00", "Type 1", "Industry 1", "Origin 1");
    }
    
    private Product createProductForRemaining(String product) {
        return new Product(product, 5, "$20.00", "Type 1", "Industry 1", "Origin 1");
    }

    private Page<Product> createProducts(Pageable pageable) {

    	Page<Product> page = new PageImpl<>(List.of(createProduct("Product 1"), createProduct("Product 2")), pageable, 2);
        return page;
    }
    
}
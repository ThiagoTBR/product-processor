package br.com.productprocessor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.com.productprocessor.controllers.ProductController;
import br.com.productprocessor.controllers.dto.Lojista;
import br.com.productprocessor.models.Product;
import br.com.productprocessor.services.ProductService;

public class ProductControllerTest {
	
	@Mock
    private ProductService productService;
	
    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return list of products")
    public void testGetProducts() {
        // given
        int page = 0;
        int size = 50;
        String sort = "product";

        List<Product> products = new ArrayList<>();
        products.add(new Product("Product1", 10, "19.99", "Type 1", "Industry 1", "Origin 1"));
        products.add(new Product("Product2", 20, "29.99", "Type 2", "Industry 2", "Origin 2"));
        products.add(new Product("Product3", 30, "39.99", "Type 3", "Industry 3", "Origin 3"));

        Page<Product> pageProducts = new PageImpl<>(products);

        when(productService.findAll(PageRequest.of(page, size, Sort.by(sort)))).thenReturn(pageProducts);

        // when
        Page<Product> result = productController.getProducts(page, size, sort);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(3);
        assertThat(result.getContent().get(0).getProduct()).isEqualTo("Product1");
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(10);
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(19.99);
        assertThat(result.getContent().get(0).getType()).isEqualTo("Type 1");
        assertThat(result.getContent().get(0).getIndustry()).isEqualTo("Industry 1");
        assertThat(result.getContent().get(0).getOrigin()).isEqualTo("Origin 1");
        
        assertThat(result.getContent().get(1).getProduct()).isEqualTo("Product2");
        assertThat(result.getContent().get(1).getQuantity()).isEqualTo(20);
        assertThat(result.getContent().get(1).getPrice()).isEqualTo(29.99);
        assertThat(result.getContent().get(1).getType()).isEqualTo("Type 2");
        assertThat(result.getContent().get(1).getIndustry()).isEqualTo("Industry 2");
        assertThat(result.getContent().get(1).getOrigin()).isEqualTo("Origin 2");
        
        assertThat(result.getContent().get(2).getProduct()).isEqualTo("Product3");
        assertThat(result.getContent().get(2).getQuantity()).isEqualTo(30);
        assertThat(result.getContent().get(2).getPrice()).isEqualTo(39.99);
        assertThat(result.getContent().get(2).getType()).isEqualTo("Type 3");
        assertThat(result.getContent().get(2).getIndustry()).isEqualTo("Industry 3");
        assertThat(result.getContent().get(2).getOrigin()).isEqualTo("Origin 3");
    }

    @Test
    @DisplayName("Should return product by name")
    public void testFindByProductName() {
        // given
        int page = 0;
        int size = 50;
        String sort = "product";
        String productName = "product1";

        List<Product> products = new ArrayList<>();
        products.add(new Product("Product1", 10, "$19.99", "Type 1", "Industry 1", "Origin 1"));

        Page<Product> pageProducts = new PageImpl<>(products);

        when(productService.findByProductName(productName, PageRequest.of(page, size, Sort.by(sort))))
                .thenReturn(pageProducts);

        // when
        Page<Product> result = productController.findByProductName(page, size, sort, productName);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getProduct()).isEqualTo("Product1");
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(10);
        assertThat(result.getContent().get(0).getPrice()).isEqualTo(19.99);
        assertThat(result.getContent().get(0).getType()).isEqualTo("Type 1");
        assertThat(result.getContent().get(0).getIndustry()).isEqualTo("Industry 1");
        assertThat(result.getContent().get(0).getOrigin()).isEqualTo("Origin 1");
    }
    
    @Test
    public void testCalculateProductSplit() throws Exception {
    	
        List<Lojista> lojistas = new ArrayList<>();
        lojistas.add(new Lojista("Loja1", "Product1", 10, 10.0, 1.00));
        lojistas.add(new Lojista("Loja2", "Product1", 10, 10.0, 1.00));
        
        // Configura o MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        
        when(productService.calculateProductSplit(anyString(), anyInt())).thenReturn(lojistas);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/productCalc")
                .param("productName", "Product1")
                .param("quantidadeLojas", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("[{\"loja\":\"Loja1\",\"product\":\"Product1\",\"qtde\":10,\"financeiro\":10.0,\"precoMedio\":1.0},"
        																+ "{\"loja\":\"Loja2\",\"product\":\"Product1\",\"qtde\":10,\"financeiro\":10.0,\"precoMedio\":1.0}]");
    }

}

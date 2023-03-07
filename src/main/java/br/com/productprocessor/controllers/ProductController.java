package br.com.productprocessor.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.productprocessor.controllers.dto.Lojista;
import br.com.productprocessor.models.Product;
import br.com.productprocessor.services.ProductService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @ApiOperation(value = "Retorna uma lista de produtos")
    @GetMapping("/products")
    public Page<Product> getProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "50") int size,
                                     @RequestParam(value = "sort", defaultValue = "product") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return productService.findAll(pageable);
    }
    
    
    @ApiOperation(value = "Retorna um produto a partir do product")
    @GetMapping("/{productName}")
    public Page<Product> findByProductName(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @RequestParam(value = "sort", defaultValue = "product") String sort,
    		@PathVariable String productName) {
    	Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    	return productService.findByProductName(productName, pageable);

    }
    
    @ApiOperation(value = "Retorna o cáuculo dos produtos dividido pela quantidade de lojas informada")
    @GetMapping("/productCalc")
    public List<Lojista> calculateProductSplit(@RequestParam("productName") String productName, @RequestParam("quantidadeLojas") int quantidadeLojas) {
    	return productService.calculateProductSplit(productName, quantidadeLojas);
    }
    
    @PostMapping("/startReading")
    public ResponseEntity<String> startReadingFiles() {
        try {
        	productService.readAndStoreProducts();
            return ResponseEntity.ok("Leitura de arquivos concluída.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao iniciar a leitura de arquivos: " + e.getMessage());
        }
    }
}


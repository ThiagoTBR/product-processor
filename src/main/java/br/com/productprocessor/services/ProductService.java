package br.com.productprocessor.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.productprocessor.controllers.dto.Lojista;
import br.com.productprocessor.models.DataFile;
import br.com.productprocessor.models.Product;
import br.com.productprocessor.repositories.ProductRepository;

@Service
public class ProductService {
	
    @Autowired
    private ProductRepository repository;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    
    
    public void readAndStoreProducts() {
    	
    	ObjectMapper mapper = new ObjectMapper();
		TypeReference<DataFile> typeReference = new TypeReference<DataFile>(){};
		 LocalDateTime antes = LocalDateTime.now();
		
		String files[] = {"data_1.json", "data_2.json", "data_3.json", "data_4.json"};
		
		if(files != null) {
		    for(String file : files) {
		    	InputStream inputStream = TypeReference.class.getResourceAsStream("/static/"+file);
		        try {
		        	DataFile products = mapper.readValue(inputStream,typeReference);
		        	products.getData().parallelStream()
		        	.forEach(
		        			product -> {
		        				this.save(product);
		        			});
		        	
		        	LOGGER.info("File Saved!");
		        } catch (IOException e){
		        	LOGGER.info("Unable to save file: " + e.getMessage());
		        }
		    }
		}
		LocalDateTime depois = LocalDateTime.now();
		LOGGER.info("Horario antes: " + antes);
		LOGGER.info("Horario depois: " + depois);
		LOGGER.info("Tempo decorrido (segundos): " + Duration.between(antes, depois).getSeconds());
		
    	
    }
    
	
	public Page<Product> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Product save(Product product) {
		
		try {
			return repository.save(product);
        } catch (DuplicateKeyException e) {
            LOGGER.error("Erro ao salvar o produto {}. O produto já existe na base de dados.", product.getId());
            return null;
        }

	}

	public Page<Product> findByProductName(String productName, Pageable pageable) {
		 return repository.findByProduct(productName, pageable);
	}

	public List<Lojista> calculateProductSplit(String productName, int quantidadeLojas) {
		
		List<Product> products =  repository.findByProductOrderById(productName);
		
		List<Lojista> lojistas = new ArrayList<>();
		
		for (int i = 0; i < quantidadeLojas; i++) {
			Lojista lojista = new Lojista();
			lojista.setLoja("Loja"+i);
			lojista.setProduct(productName);
			lojistas.add(lojista);
		}
		
		products.stream().forEach(product -> {
			calculateProductDivision(product, lojistas);
		});
	    
		
		return lojistas;
	}

	private void calculateProductDivision(Product product, List<Lojista> lojistas) {
		int resto = product.getQuantity() % lojistas.size();
		if(resto == 0) {
			lojistas.stream().forEach(loja -> {
				distributeProductByStore(product, loja, lojistas.size(), resto);
			});
		}else{
			
			lojistas.stream().forEach(loja -> {
				distributeProductByStore(product, loja, lojistas.size(), resto);
			});
			
			String lojaMenorQuantidade = Collections.min(lojistas, Comparator.comparing(l -> l.getQtde())).getLoja();
			
			lojistas.stream()
		    .filter(loja -> loja.getLoja().equals(lojaMenorQuantidade)).findFirst().ifPresent(
		    			loja -> {
		    				distributeProductsRemaining(product, loja, resto);
		    			}
		    		);	
		}
		
	}

	private void distributeProductByStore(Product product, Lojista loja, int qtdLojas, int resto) {
		int qtdAdicionar = ((product.getQuantity() - resto) / qtdLojas);
		double financeiroAdicionar = (product.getPrice() * qtdAdicionar);
		
		loja.setQtde(qtdAdicionar + loja.getQtde());
		loja.setFinanceiro(financeiroAdicionar + loja.getFinanceiro());
		loja.setPrecoMedio(loja.getFinanceiro() / loja.getQtde());
	}
	
	private void distributeProductsRemaining(Product product, Lojista loja, int resto) {
		loja.setQtde(resto + loja.getQtde());
		loja.setFinanceiro((product.getPrice() * resto) + loja.getFinanceiro());
		loja.setPrecoMedio(loja.getFinanceiro() / loja.getQtde());
	}

}


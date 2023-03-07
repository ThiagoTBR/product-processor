package br.com.productprocessor.models;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Document(collection="product")
public class Product implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private ObjectId  id;
	
	String product;
	
	int quantity;
	
	double price;
	
	String type;
	
	String industry;
	
	String origin;
	
	public Product() {
	}

	public Product(String product, int quantity, String price, String type, String industry,String origin) {
		this.product = product;
		this.quantity = quantity;
		this.price = Double.parseDouble(price.replace("$", ""));
		this.type = type;
		this.industry = industry;
		this.origin = origin;
	}
	
	@JsonProperty
	public void setPrice(String price) {
		this.price = Double.parseDouble(price.replace("$", ""));
	}
	
}

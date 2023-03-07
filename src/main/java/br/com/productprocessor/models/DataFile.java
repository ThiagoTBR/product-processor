package br.com.productprocessor.models;

import java.util.List;


public class DataFile{
	
	
	public DataFile() {
	}

	private List<Product> data;

	public List<Product> getData() {
		return data;
	}

	public void setData(List<Product> data) {
		this.data = data;
	}

}

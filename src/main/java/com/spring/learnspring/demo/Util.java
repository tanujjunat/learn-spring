package com.spring.learnspring.demo;

public class Util {
	
	Pojo pojo;
	
	String data;
	
	@Autowired
	public Util(Pojo pojo, String data) {
		this.pojo = pojo;
		this.data = data;
	}
	
	public void anotherFunction() {
		System.out.println(pojo);
	}

}

package test.view.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="myview")
public class MyView {

	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "MyView [name=" + name + ", age=" + age + "]";
	}
	
}

package test.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eweb4j.config.LogFactory;
import org.eweb4j.mvc.action.Validation;
import org.eweb4j.mvc.config.JAXWSUtil;
import org.eweb4j.mvc.validator.annotation.Required;
import org.eweb4j.mvc.validator.annotation.Validate;

import test.po.Pet;

@Path("/test")
public class PetControl {

	public static void main(String[] args){
		System.out.println(JAXWSUtil.hasPath(PetControl.class));
	}
	
	@Required
	private int age;

	private Pet pet;


	@Validate({"pet.master.name","pet.name", "age"})
	public String doHelloWorld(Validation val){
		StringBuilder info = new StringBuilder();
		Map<String, Map<String,String>> errors = val.getErrors();
		for (Iterator<Entry<String,Map<String,String>>> it = errors.entrySet().iterator(); it.hasNext(); ){
			Entry<String,Map<String,String>> en = it.next();
			/* 验证器类型 */
			String validatorName = en.getKey();
			info.append("<br />\n").append(validatorName);
			Map<String,String> error = en.getValue();
			for (Iterator<Entry<String,String>> it2 = error.entrySet().iterator(); it2.hasNext(); ){
				Entry<String,String> entry = it2.next();
				/* 参数名 */
				String paramName = entry.getKey();
				/* 验证输出信息 */
				String message = entry.getValue();
				
				info.append("<br />&nbsp;&nbsp;\n\t").append(paramName).append(", ").append(message);
				LogFactory.getMVCLogger(PetControl.class).info(info.toString());
			}
		}
		
		return info.toString();
	}

	@GET
	public String doEdit(@PathParam("id") int id) {
		return "";
	}

	public String doUriBindIdAndAgeAtGetOrPut(@PathParam("id") int id) {
		return "do get";
	}

	public String doAtDelete() {
		return "do At delete";
	}

	@POST
	@Path("fuck")
	public String doHelloAtDelete() {
		return "";
	}

	@Path("test")
	public String doHelloWorldAtPost() {

		return "hello world At post";
	}

	public String doHelloWorldAtGetOrPost() {

		return "hello world At get or post";
	}

	public String doHelloWorldAtGet() {

		return "hello world At get";
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) { 
		this.age = age;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

}

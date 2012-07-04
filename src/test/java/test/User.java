package test;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eweb4j.orm.Model;

@Entity
@Table(name = "t_user")
public class User extends Model {
	
	public final static User inst = new User();
	
	@ManyToOne(cascade={CascadeType.ALL})
	private String account;
	private String password;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [account=" + account + ", password=" + password + ", id="
				+ id + "]";
	}

}

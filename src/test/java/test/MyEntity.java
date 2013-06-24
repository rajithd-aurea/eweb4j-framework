package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2013-6-19 下午02:48:41
 */
@Entity
@Table(name="t_my_entity")
public class MyEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Column(nullable=false)
	private byte[] data;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "MyEntity [id=" + this.id + ", data="
				+ Arrays.toString(this.data) + "]";
	}
	
}

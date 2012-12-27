package test.po;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TODO
 * @author weiwei l.weiwei@163.com
 * @date 2012-12-26 下午06:55:25
 */
@Entity
@Table(name="t_pojo")
public class MyPojo {

	@Column(name="_time")
	private Time time;
	
	@Column(name="_stamp")
	private Timestamp stamp;
	
	@Column(name="_date")
	private Date date;
	
	public Time getTime() {
		return this.time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public Timestamp getStamp() {
		return this.stamp;
	}
	public void setStamp(Timestamp stamp) {
		this.stamp = stamp;
	}
	public Date getDate() {
		return this.date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "MyPojo [time=" + this.time + ", stamp=" + this.stamp
				+ ", date=" + this.date + "]";
	}
	
}

package org.eweb4j.mvc.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eweb4j.mvc.config.bean.InterConfigBean;
import org.eweb4j.util.StringUtil;

/**
 * 拦截器优先级，数字越大，优先级越低，数字越小，优先级越高。
 * @author weiwei
 *
 */
public class InterPriorityComparator implements Comparator<InterConfigBean>{

	public int compare(InterConfigBean ic1, InterConfigBean ic2) {
		
		String pr1 = ic1.getPriority();
		String pr2 = ic2.getPriority();
		int p1 = 0;
		if (StringUtil.isNumeric(pr1))
			p1 = Integer.parseInt(pr1);
		
		int p2 = 0;
		if (StringUtil.isNumeric(pr2))
			p2 = Integer.parseInt(pr2);
		
		int result = p2 - p1;
		if (result > 0)
			return -1;
		if (result < 0)
			return 1;
		
		return 0;
	}
	
	public static void main(String[] args){
		List<InterConfigBean> list = new ArrayList<InterConfigBean>();
		
		InterConfigBean i1 = new InterConfigBean();
		i1.setPriority("1");
		list.add(i1);
		
		InterConfigBean i2 = new InterConfigBean();
		i2.setPriority("5");
		list.add(i2);
		
		InterConfigBean i3 = new InterConfigBean();
		i3.setPriority("2");
		list.add(i3);
		
		InterConfigBean i4 = new InterConfigBean();
		i4.setPriority("7");
		list.add(i4);
		
		System.out.println(list);
		
		Collections.sort(list, new InterPriorityComparator());
		
		System.out.println(list);
	}

}

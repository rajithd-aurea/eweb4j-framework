package test.cache;

import org.eweb4j.config.EWeb4JConfig;

public class TestCache {
	public static void main(String[] args) throws InterruptedException {
		String error = EWeb4JConfig.start();
		if (error == null) {
			int i = 0;
			long start = System.currentTimeMillis();
			for (;i < 300; i++) {
//				if (pets != null)
//					System.out.println(pets);
			}
			System.out.println(System.currentTimeMillis()-start);
		} else
			System.out.println(error);
	}
}

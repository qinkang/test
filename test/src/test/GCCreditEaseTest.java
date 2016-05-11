package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

//count:15771972

public class GCCreditEaseTest implements Runnable{
	public AtomicLong counts;

	public GCCreditEaseTest(AtomicLong counts) {
		this.counts = counts;
	}

	public static void main(String[] args) throws InterruptedException {
		AtomicLong counts = new AtomicLong(0);
		List<Thread> tList = new ArrayList<Thread>();
		for (int i = 0; i < 5; i++) {
			GCCreditEaseTest kill = new GCCreditEaseTest(counts);
			Thread t = new Thread(kill);
			t.start();
			tList.add(t);
		}
		for (int i = 0; i < tList.size(); i++) {
			tList.get(i).join();
		}
		System.out.println("count:" + counts.longValue());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		int k = 0;
		while (true) {
			k++;
			System.out.println("times:" + (System.currentTimeMillis() - start));
			List<String> outOfMemList = new ArrayList<String>(500000);
			int flag = 0;
			Long start2 = System.currentTimeMillis();
			for (int j = 0; j < 500000; j++) {
				if (System.currentTimeMillis() - start >= 1000 * 60) {
					flag = 1;
					break;
				}
				counts.incrementAndGet();

				outOfMemList.add(new String(new Date().toLocaleString())
						+ new String(new Date().toLocaleString())
						+ new String(new Date().toLocaleString()));
				if (j % 100000 == 0) {
					// System.out.println(threadLocal.get());
					System.out.println(k + "|" + "list-size:"
							+ outOfMemList.size());
					System.out.println(k + "|" + "run :" + j);
				}

			}
			System.out.println("for:" + (System.currentTimeMillis() - start2));
			if (flag == 1) {
				break;
			}
		}
		// System.out.println("thread-exit-" +
		// Thread.currentThread().getName());
	}

}

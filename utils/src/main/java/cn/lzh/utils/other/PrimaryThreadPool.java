package cn.lzh.utils.other;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimaryThreadPool {
	private ExecutorService service;

	private PrimaryThreadPool() {
		int num = Runtime.getRuntime().availableProcessors();
		service = Executors.newFixedThreadPool(num * 2);
	}

	private static PrimaryThreadPool manager;

	public static PrimaryThreadPool getInstance() {
		if (manager == null) {
			synchronized (PrimaryThreadPool.class) {
				if (manager == null) {
					manager = new PrimaryThreadPool();
				}
			}
		}
		return manager;
	}

	public void addTask(Runnable runnable) {
		service.execute(runnable);
	}

	public ExecutorService getExecutors() {
		return service;
	}
}

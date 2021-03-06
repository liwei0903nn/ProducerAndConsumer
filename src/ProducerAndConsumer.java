import java.util.PriorityQueue;

public class ProducerAndConsumer {
	private int queueSize = 10;
	private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);

	public static void main(String[] args) {
		ProducerAndConsumer producerAndConsumer = new ProducerAndConsumer();
		Consumer consumer = producerAndConsumer.new Consumer();
		Producer producer = producerAndConsumer.new Producer();
		producer.start();
		consumer.start();
	}

	class Producer extends Thread {
		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					if (queue.size() == queueSize) {
						try {
							System.out.println("产品队列满，等待消费");
							queue.wait();
						} catch (InterruptedException e) {
							queue.notify();
							e.printStackTrace();
						}
					}
					queue.offer(1);
					queue.notifyAll();
					System.out.println("生产一个新产品，还有" + (queueSize - queue.size()) + "个空位，现有 "+queue.size()+" 个产品");
					
					try {
						//模拟生产的耗时操作
						Thread.sleep(25);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	class Consumer extends Thread {
		@Override
		public void run() {
			while (true) {
				synchronized (queue) {
					if (queue.size() == 0) {
						try {
							System.out.println("产品队列空，等待生产");
							queue.wait();
						} catch (InterruptedException e) {
							queue.notify();
							e.printStackTrace();
						}
					}
					queue.poll();
					queue.notify();
					System.out.println("消耗一个新产品，还有" + queue.size() + "个产品，现有 "+queue.size()+" 个产品");
					
					try {
						//模拟消费的耗时操作
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

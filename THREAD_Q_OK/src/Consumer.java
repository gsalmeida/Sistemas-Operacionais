class Consumer implements Runnable { 
	int idConsumidor;
	Q q;
	Consumer(Q q, int idConsumidor) {
		this.idConsumidor = idConsumidor;
		this.q = q;
		new Thread(this, "Consumer").start(); 
	} 
	public void run() { 
		while(true) {
			q.get(this.idConsumidor);
		}
	} 
}

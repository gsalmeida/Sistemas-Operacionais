class Producer implements Runnable {
	int idProdutor;
	Q q; 
	Producer(Q q, int idProdutor) {
		this.idProdutor = idProdutor;
		this.q = q; 
		new Thread(this, "Producer").start(); 
	} 
	public void run() { 
		int i = 1;
		while(true) {
			q.put(i++, this.idProdutor);
		} 
	} 
}

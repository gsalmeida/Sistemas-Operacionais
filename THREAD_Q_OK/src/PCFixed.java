class PCFixed { 
	public static void main(String args[]) { 
		Q q = new Q();
		new Producer(q, 1);
		new Producer(q, 2);
		new Producer(q, 3);
		new Producer(q, 4);
		new Producer(q, 5);
		
		new Consumer(q, 20);
		new Consumer(q, 30);
		new Consumer(q, 50);
		new Consumer(q, 80);
		
		System.out.println("Press Control-C to stop."); 
	} 
}



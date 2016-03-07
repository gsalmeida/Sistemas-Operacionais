//// A correct implementation of a producer and consumer. 
//class Q2 { 
////	private int n;
//	boolean valueSet = false;
//	
//	
//	int filaDeDadosInt[] = new int[10];
//	public boolean filaEstaCheia() {
//		boolean validacao = true;
//		for(int a = 0; ((a < this.filaDeDadosInt.length) && (validacao)); a++) {
//			if(this.filaDeDadosInt[a] == 0) validacao = false;
//		}
//		return validacao;
//	}
//	
//	
//	//aumentaUmaPosicaoDeCadaElementoDaFilaEZeraOPrimeiraParaInsercao
//	private void aumentaUmaPosicaoDeCadaElementoDaFila() {
//		if(!filaEstaCheia()) for(int a = (this.filaDeDadosInt.length -1); a > 0; a--) this.filaDeDadosInt[a] = this.filaDeDadosInt[a -1];
//		this.filaDeDadosInt[0] = 0;
//	}
//	
//	public int qtdeDeElementosNaFila() {
//		int qtde = 0;
//		int a = 0;
//		for(; (a < this.filaDeDadosInt.length); a++) if(this.filaDeDadosInt[a] != 0) qtde++;
//		return qtde;
//	}
//	
//	//insere sempre no inicio da fila, respeitando a regra.
//	public void insereNaFila(int n) {
//		if(qtdeDeElementosNaFila() == 0) this.filaDeDadosInt[0] = n;
//		else {
//			if(!filaEstaCheia()) {
//				aumentaUmaPosicaoDeCadaElementoDaFila();
//				this.filaDeDadosInt[0] = n;
//			}
//		}
//		
//	}
//	
//	//obtem e remove o elemento da fila para ser usado no método "synchronized int get()"
//	public int getAndRemoveElementoDaFila() {
//		int retorno = 0;
//		for(int a = (this.filaDeDadosInt.length -1); a >= 0; a--) 
//			if(this.filaDeDadosInt[a] != 0) {
//				 retorno = this.filaDeDadosInt[a];
//				 this.filaDeDadosInt[a] = 0;
//			}
//		return retorno; 
//	}
//	
//
//	synchronized int get(int idConsumidor) {
//		if( (!valueSet) || (qtdeDeElementosNaFila() == 0) ) 
//			try { 
//				wait(); 
//			} catch(InterruptedException e) { 
//				System.out.println("InterruptedException caught"); 
//			}
//		
//		int valorObtido = 0;
//		if(qtdeDeElementosNaFila() > 0) {
//			valorObtido = getAndRemoveElementoDaFila();
//			System.out.println("Got (get() > CONSUMIDOR => "+idConsumidor+"): " + valorObtido);
//		}
//		else {
//			System.out.println("Got (get() > CONSUMIDOR => "+idConsumidor+"): NAO OBTEVE, POIS A LISTA ESTÁ VAZIA");
//			notify();
//		}
//		valueSet = false; 
//		notify(); 
////		return n;
//		return valorObtido;
//	}
//	
//	synchronized void put(int n, int idProdutor) { 
//		if(valueSet && (!filaEstaCheia()) ) { 
//			try { 
//				wait(); 
//			} catch(InterruptedException e) { 
//				System.out.println("InterruptedException caught"); 
//			} 
//		}
////		this.n = n;
//		insereNaFila(n);
//		valueSet = true; 
////		System.out.println("Put (put()): " + n);
//		System.out.println("Put (put() > PRODUTOR => "+idProdutor+"): " + this.filaDeDadosInt[0]); // prova que foi inserido na primeira posição do vetor.
//		notify(); 
//	}
//}
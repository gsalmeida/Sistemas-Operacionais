// A correct implementation of a producer and consumer. 
class Q { 
//	private int n;
	boolean valueSet = false;
	private int filaDeDadosInt[] = new int[10];
		
	public boolean filaEstaCheia() {
		boolean validacao = true;
		for(int a = 0; ((a < this.filaDeDadosInt.length) && (validacao)); a++) if(this.filaDeDadosInt[a] == 0) validacao = false;
		return validacao;
	}
	
	public boolean filaEstaVazia() {
		for(int a = 0; (a < this.filaDeDadosInt.length); a++) if(this.filaDeDadosInt[a] != 0) return false;
		return true;
	}
	
	public int qtdeDeElementosNaFila() {
		int qtde = 0;
		int a = 0; 
		for(; (a < this.filaDeDadosInt.length); a++) if(this.filaDeDadosInt[a] != 0) qtde++;
		return qtde;
	}
	
	
	//Outra solução para este método. Em bem mais linhas, é verdade. E menos eficiente.
	public void insereNaFila(int n) { 
		if(qtdeDeElementosNaFila() == 0) this.filaDeDadosInt[0] = n;
		else {
			if(!filaEstaCheia()) {
				for(int a = (this.filaDeDadosInt.length -1); a >= 0; a--) 
					if(this.filaDeDadosInt[a] != 0) {
						if(a != (this.filaDeDadosInt.length -1)) filaDeDadosInt[a+1] = n;
						break; 
					}
			} else System.out.println("Fila cheia. Não persistiu dados na fila.");
		}
	}
	
	
	
	private void diminuiUmaPosicaoDeCadaElementoDaFila() {
		int a = 0;
		for(; a < (this.filaDeDadosInt.length -1); a++) this.filaDeDadosInt[a] = this.filaDeDadosInt[a+1];
		this.filaDeDadosInt[a] = 0;
	}
	
	//obtem e remove o primeiro elemento encontrado na fila para ser usado no método "synchronized int get()"
	public int getAndRemoveElementoDaFila() {
		int retorno = 0;
		for(int a = 0; a < this.filaDeDadosInt.length; a++) 
			if(this.filaDeDadosInt[a] != 0) {
				 retorno = this.filaDeDadosInt[a];
				 this.filaDeDadosInt[a] = 0;
				 diminuiUmaPosicaoDeCadaElementoDaFila();
				 break;
			}
		return retorno; 
	}
	
	public void mostrarFilaDeDados() {
		for(int a = 0; a < this.filaDeDadosInt.length; a++) System.out.println(a+": "+this.filaDeDadosInt[a]);
	}
	
	
	synchronized int get(int idConsumidor) {
//		if( (!valueSet) || (qtdeDeElementosNaFila() == 0) )
		if( (!valueSet) || (filaEstaVazia()) )
			try { 
				wait(); 
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			}
		
		int valorObtido = 0;
//		if(qtdeDeElementosNaFila() > 0) {
		if(!filaEstaVazia()) {
			valorObtido = getAndRemoveElementoDaFila();
			System.out.println("Got (get() > CONSUMIDOR => "+idConsumidor+"): " + valorObtido);
		}
		else {
			System.out.println("Got (get() > CONSUMIDOR => "+idConsumidor+"): NAO OBTEVE, POIS A LISTA ESTÁ VAZIA. NOTIFICOU AO PRODUTOR.");
			notify();
		}
		valueSet = false; 
		notify(); 
//		return n;
		return valorObtido;
	}
	
	synchronized void put(int n, int idProdutor) { 
		if(valueSet && (!filaEstaCheia()) ) { 
			try { 
				wait(); 
			} catch(InterruptedException e) { 
				System.out.println("InterruptedException caught"); 
			} 
		}
//		this.n = n;
		insereNaFila(n);
		valueSet = true; 
//		System.out.println("Put (put()): " + n);
		for(int a = (this.filaDeDadosInt.length - 1); a >= 0; a--) {
			if(this.filaDeDadosInt[a] != 0) {
				System.out.println("Put (put() > PRODUTOR => "+idProdutor+"): " + this.filaDeDadosInt[a]);
				break;
			}
		}
//		System.out.println("Put (put() > PRODUTOR => "+idProdutor+"): " + this.filaDeDadosInt[0]); // prova que foi inserido na primeira posição do vetor.
		notify(); 
	}

}
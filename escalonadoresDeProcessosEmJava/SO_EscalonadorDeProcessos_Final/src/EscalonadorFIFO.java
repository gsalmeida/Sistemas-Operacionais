import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class EscalonadorFIFO extends Escalonador implements Runnable {
	
	//incrementa 0,001 segundos a cada laço do for. Deve considerar as três casas decimais depois do virgula.
	// Entre 0,4 segundos e 5 segundos, Todos os processos devem estar prontos para serem executados.
	private double tempoEscalonadorFIFO = 0; 	//incrementa 0,001 segundos a cada laço do for. 
	private int qtdeTrocaDeContextoFIFO = 0; // em millisegundos.
	private double valorTrocaDeContextoFIFO = 0;
	private double intervaloDeTempoFIFOEmSegundos = 0.1;
	
	
//	private double trocaDeContextoFIFO;
//    
//    public double getTrocaDeContextoFIFO() {
//    	return trocaDeContextoFIFO;
//    }
//    
//    public void setTrocaDeContextoFIFO(double trocaDeConextoFIFO) {
//    	this.trocaDeContextoFIFO = trocaDeConextoFIFO;
//    }
	
	
	public EscalonadorFIFO(String arquivoCSVComProcessos, int qtde) {
		super();
		try {
			preencheListaDeProcessosCriados(arquivoCSVComProcessos, qtde);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void iniciar() {
		run();
	}
	
	@Override
	public void run() {
		BufferedWriter buffWrite = null;
		String linha = "";
		Scanner in = new Scanner(System.in);
		try {
			buffWrite = new BufferedWriter(new FileWriter(new File("saidaFIFO.html")));
		} catch (IOException e) { e.printStackTrace(); }
		linha += "<html>";
		while (!completouTodosProcessos()) {
			try {
				Thread.sleep(1000-(1/10));
				tempoEscalonadorFIFO += getIntervaloDeTempoFIFOEmSegundos();
//				tempoEscalonadorFIFO += 
				System.out.println("TEMPO ESCALONADO: "+tempoEscalonadorFIFO+" segundos.");
				linha += "<BR/><BR/><b style='color:blue;'>TEMPO ESCALONADO: "+tempoEscalonadorFIFO+" segundos.</b><BR/><BR/>";
				if (!completouTodosProcessos()) { // 1 segundo = 1000 milisegundos // 0,0001 s = 0,1 milisegundos //troca de contexto
					setTempoEscalonado(tempoEscalonadorFIFO);
					this.executarPassoFIFO();
					
					linha += strHTMLArquivoSaida();
					
					System.out.println("TEMPO ESCALONADO: "+tempoEscalonadorFIFO+" segundos.");
					linha += "<BR/><BR/><b style='color:blue;'>TEMPO ESCALONADO: "+tempoEscalonadorFIFO+" segundos.</b><BR/><BR/>";
				}
				qtdeTrocaDeContextoFIFO++;
//				valorTrocaDeContextoFIFO += (1/10);
				valorTrocaDeContextoFIFO += 0.1;
				Thread.sleep((1/10));
//				Thread.sleep((int) getTrocaDeContexto());
//					Thread.sleep(0.1);
//					Thread.sleep(1); // Thread.sleep(10); //não tem como definir 0,10 ms aqui // Thread.sleep(getTrocaDeContextoFIFO());
//					Thread.sleep((int) getTrocaDeContexto()); 0,10 milisegundos = 0,0001 segundos
			
				if (completouTodosProcessos()) {
//		            jTextFieldTotalTt.setText(escalonador.getTotalTurnaroundTime() + "");
//		            jTextFieldMediaTt.setText(escalonador.getMediaTurnaroundTime() + "");
		            JOptionPane.showMessageDialog(null, "Execucao Concluida!");
		            //JOptionPane.showMessageDialog(null, "getTotalTurnaroundTime: "+getTotalTurnaroundTime() + "");
		            //JOptionPane.showMessageDialog(null, "getMediaTurnaroundTime"+getMediaTurnaroundTime() + "");
		        }
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		linha += "</html>";
		try {
			buffWrite.append(linha + "\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			buffWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int executarPassoFIFO() {
		int mudouEstadoDeProcessos = 0;
		aumentarTempoDeEsperaDeProcessosProntos((0.9)+getIntervaloDeTempoFIFOEmSegundos()); //aumenta o tempo de espera de todos os processos.
        if(!this.getListaDeProcessosEmExecucao().isEmpty()) getListaDeProcessosEmExecucao().get(0).diminuirTempo((0.9)+getIntervaloDeTempoFIFOEmSegundos()); //diminui o tempo do primeiro processo se a lista nao estiver vazia. parte do primeiro.
        for(int a = 0; a <= getListaDeProcessosCriados().size() -1; a++) {
        		// Entra aqui somente se o arrival time (tempo de chegada) de algum processo da fila de processos a chegar for igual ao tempo
                // Se a lista de processos em execução estiver vazia, adiciona diretamente para lista de processos em execução
                if(getListaDeProcessosEmExecucao().isEmpty()) { // se nao existir processo em execucao.
//                	if (getListaDeProcessosCriados().get(0).getTempoDeChegada() == tempoEscalonadorFIFO) { 
                	// No FIFO se não houver nenhum outro processo a executar, então ele executa, visando sempre manter o preocessador ocupado o máximo possível. 
                		getListaDeProcessosEmExecucao().add(getListaDeProcessosCriados().get(0));
                		getListaDeProcessosCriados().remove(0);
                		a--;
                		mudouEstadoDeProcessos = 1;
//                	}
                } else { // se existir processo em execucao 
                    // Se já existir um processo em execução, adicionar este outro processo a lista de processos prontos a serem executados
//                	if (getListaDeProcessosCriados().get(0).getTempoDeChegada() == tempoEscalonadorFIFO) {
                		getListaDeProcessosProntos().add(getListaDeProcessosCriados().get(0)); //adiciona em "lista de processos prontos" para serem executados.
                		getListaDeProcessosCriados().remove(0);
                		a--;
                		mudouEstadoDeProcessos = 1;
//                	}
                }
        }
        
        // Verifica se o processo em execução acabou, caso positivo, sobe um novo processo da fila de processos
        // aguardando, e move o processo em execução para fila de processos executados
        if(!getListaDeProcessosEmExecucao().isEmpty()) {
            if(getListaDeProcessosEmExecucao().get(0).getTempo() == 0) {
                getListaDeProcessosEmExecucao().get(0).setTempoTotal(getListaDeProcessosEmExecucao().get(0).getTempoInicio() + getListaDeProcessosEmExecucao().get(0).getTempoDeEspera());
                getListaDeProcessosConcluidos().add(getListaDeProcessosEmExecucao().get(0));
                getListaDeProcessosEmExecucao().remove(0);
                if(getListaDeProcessosProntos().size() > 0) {
//	                    organizarListaDeProcessosProntos();
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosProntos().get(0));
                    getListaDeProcessosProntos().remove(0);
                }
                mudouEstadoDeProcessos = 1;   
            }            
        }
        return mudouEstadoDeProcessos;
	}

	 
	 public List<Processo> getListaDeProcessosEmExecucao() {
		 //return getListaDeProcessosEmExecucao(); // Não sei o que aconteceria aqui com esta linha descomentada. Recursividade, talvez. Nulo de retorno.
		return super.getListaDeProcessosEmExecucao();
	}

	public double getTempoEscalonadorFIFO() {
		return tempoEscalonadorFIFO;
	}

	public void setTempoEscalonadorFIFO(double tempoEscalonadorFIFO) {
		this.tempoEscalonadorFIFO = tempoEscalonadorFIFO;
	}

	public int getQtdeTrocaDeContextoFIFO() {
		return qtdeTrocaDeContextoFIFO;
	}

	public void setQtdeTrocaDeContextoFIFO(int qtde) {
		this.qtdeTrocaDeContextoFIFO = qtde;
	}

	public double getIntervaloDeTempoFIFOEmSegundos() {
		return intervaloDeTempoFIFOEmSegundos;
	}

	public void setIntervaloDeTempoFIFOEmSegundos(double intervaloDeTempoEmSegundos) {
		this.intervaloDeTempoFIFOEmSegundos = intervaloDeTempoEmSegundos;
	}

	public double getValorTrocaDeContextoFIFO() {
		return valorTrocaDeContextoFIFO;
	}

	public void setValorTrocaDeContextoFIFO(double valorTrocaDeContexto) {
		this.valorTrocaDeContextoFIFO = valorTrocaDeContexto;
	}


	public double tempoTotalDeLatencia() { // Tempo total de latência => soma dos tempos de troca de contexto. Overhead.
		return valorTrocaDeContextoFIFO;
	}
	
	public double getTo() {
		return tempoTotalDeLatencia();
	}
	
	public double tempoTotalDeProcessamentoBruto() { //(Tt)- Tempo total de processamento bruto (Tp + To).
		return getTp()+getTo();
	}
	
	public double getTt() { //(Tt)- Tempo total de processamento bruto (Tp + To).
		return tempoTotalDeProcessamentoBruto();
	}
	
	public double getOcupacaoDoProcessador() { // Ocupação do processador (Tp/Tt) (%)
		return (getTp() / getTt());
	}
	
	public double getOverheadDeProcessamento() { // Overhead de Processamento (To/Tt) (%).
		return (getTo() / getTt());
	}
	

	 
}




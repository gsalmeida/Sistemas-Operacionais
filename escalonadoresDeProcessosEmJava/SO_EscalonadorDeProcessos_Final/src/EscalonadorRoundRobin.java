import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class EscalonadorRoundRobin extends Escalonador implements Runnable {

	//quantum Round Robin = 100ms.

	//incrementa 0,001 segundos a cada laço do for. Deve considerar as três casas decimais depois do virgula.
	// Entre 0,4 segundos e 5 segundos, Todos os processos devem estar prontos para serem executados.
	private double tempoEscalonadorRoundRobin = 0; 	//incrementa 0,001 segundos a cada laço do for.
	
	private int qtdeTrocaDeContextoRoundRobin = 0; // em millisegundos.
	private double valorTrocaDeContextoRoundRobin = 0;
	private double intervaloDeTempoRoundRobinEmSegundos = 0.1;
	
	private double quantumRoundRobin;
	
	
//	private double trocaDeContextoRoundRobin;
//    
//    public double getTrocaDeContextoRoundRobin() {
//    	return trocaDeContextoRoundRobin;
//    }
//    
//    public void setTrocaDeContextoRoundRobin(double trocaDeConextoRoundRobin) {
//    	this.trocaDeContextoRoundRobin = trocaDeConextoRoundRobin;
//    }
	

	public EscalonadorRoundRobin(String arquivoCSVComProcessos, int qtde, double quantumRoundRobin) {
		super();
		try {
			preencheListaDeProcessosCriados(arquivoCSVComProcessos, qtde);
			this.quantumRoundRobin = quantumRoundRobin; // setQuantumRoundRobin(quantumRoundRobin); Assim como comentado, se o set muda, aqui também muda. 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void iniciar() {
		run();
	}
	

	@Override
	public void run() {
		boolean inicio = true;
		BufferedWriter buffWrite = null;
		String linha = "";
		Scanner in = new Scanner(System.in);
		try {
			buffWrite = new BufferedWriter(new FileWriter(new File("saidaRoundRobin.html")));
		} catch (IOException e) { e.printStackTrace(); }
		linha += "<html>";
		
		String vetorTempoEscalonadorSJFFormatadoAux[];
		String tempoEscalonadorSJFFormatado = "";
		
		while (!completouTodosProcessos()) {
			try {
				tempoEscalonadorRoundRobin += getIntervaloDeTempoRoundRobinEmSegundos();
//				JOptionPane.showMessageDialog(null, "Valor Da Conversão: "+ Double.parseDouble(String.valueOf(getIntervaloDeTempoSJFEmSegundos()).toString().substring(0,3)));
//				JOptionPane.showMessageDialog(null, "tempoEscalonadorSJF: "+tempoEscalonadorSJF);
				Thread.sleep(1-(10/100));
				linha += "<HR STYLE='COLOR: ORANGE;' />";
					vetorTempoEscalonadorSJFFormatadoAux = String.valueOf(tempoEscalonadorRoundRobin).toString().split(".");
					tempoEscalonadorSJFFormatado = String.valueOf(tempoEscalonadorRoundRobin);
					if(vetorTempoEscalonadorSJFFormatadoAux.length == 2) {
						if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 1) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + vetorTempoEscalonadorSJFFormatadoAux[1]; 
						else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 2) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2]; 
						else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() >= 3) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2] + vetorTempoEscalonadorSJFFormatadoAux[3];
					}
					Thread.sleep(1000-(10/100));
					linha += "<BR/><BR/><b style='color:blue;'>TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+" SEGUNDOS.</b><BR/><BR/>";
					System.out.println("TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+" SEGUNDOS.");
				
				if (!completouTodosProcessos()) { // 1 segundo = 1000 milisegundos // 0,0001 s = 0,1 milisegundos //troca de contexto
					setTempoEscalonado(tempoEscalonadorRoundRobin);
//					System.out.println("------------>tempoEscalonadorSJF---> "+tempoEscalonadorSJF);
//					System.out.println("------------>tempoEscalonadorSJF---> "+String.valueOf(tempoEscalonadorSJF).toString().substring(0,3));
					if(inicio) { 
						linha += strHTMLArquivoSaida();
						inicio = false;
						Collections.sort(getListaDeProcessosCriados());
						linha += "<BR/><BR/><b style='color:BLACK;'>VETOR ORDENADO:</b><BR/><BR/>";
						linha += strHTMLArquivoSaida();
						setTempoEscalonado(0);
					} 
					
					this.executarPassoRoundRobin(getQuantumRoundRobin());
					linha += strHTMLArquivoSaida();
					
					qtdeTrocaDeContextoRoundRobin++;
//					valorTrocaDeContextoRoundRobin += (1/10);
					valorTrocaDeContextoRoundRobin += 0.1;
					Thread.sleep((1/10));
//					Thread.sleep(0.1);
//					Thread.sleep(1); // Thread.sleep(10); //não tem como definir 0,10 ms aqui // Thread.sleep(getTrocaDeContextoFIFO());
//					Thread.sleep((int) getTrocaDeContextoSJF()); // 0,001 segundos
				}
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
		
		vetorTempoEscalonadorSJFFormatadoAux = String.valueOf(getTempoEscalonado()).toString().split(".");
		tempoEscalonadorSJFFormatado = String.valueOf(getTempoEscalonado());
		if(vetorTempoEscalonadorSJFFormatadoAux.length == 2) {
			if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 1) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + vetorTempoEscalonadorSJFFormatadoAux[1]; 
			else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 2) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2]; 
			else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() >= 3) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2] + vetorTempoEscalonadorSJFFormatadoAux[3];
		}
		
		System.out.println("TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado);
		linha += "<BR/><BR/><b style='color:blue;'>TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+"</b><BR/><BR/>";
		
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

	
	
    // Executa um step do aloritmo Round-Robin, recebendo o inteiro Quantum
    public void executarPassoRoundRobin(double quantumParametro) {
    	quantumParametro = (quantumParametro * 10); // se for 100ms = 0,1 segundos. Será 1 segundo, ou seja, 1.0.
        organizarListaDeProcessosCriados();
        aumentarTempoDeEsperaDeProcessosProntos((0.9)+getIntervaloDeTempoRoundRobinEmSegundos());
        if(!getListaDeProcessosEmExecucao().isEmpty()) {
            getListaDeProcessosEmExecucao().get(0).diminuirTempo((0.9)+getIntervaloDeTempoRoundRobinEmSegundos());
            getListaDeProcessosEmExecucao().get(0).aumentarTempoDeExecucao();
        }
        for(int i = 0; i <= getListaDeProcessosCriados().size() -1; i++) {
//            if(getListaDeProcessosCriados().get(0).getTempoDeChegada() == getTempoEscalonado()) {
        	 if( 
             		(String.valueOf(getListaDeProcessosCriados().get(i).getTempoDeChegada()).toString().substring(0,3))
             		.equals
             		(String.valueOf(getTempoEscalonado()).toString().substring(0,3)) 
               ) {
        		 
                if(getListaDeProcessosEmExecucao().isEmpty()) {
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosCriados().get(0));
                    getListaDeProcessosEmExecucao().get(0).setTempoDeExecucao(0);
                    getListaDeProcessosCriados().remove(0);
                    i--;
                } else {
                    // Se já tiver um processo em execução, adicionar o processo a lista de processos aguardando
                    if(getListaDeProcessosCriados().get(0).getTempo() != 0){
                        getListaDeProcessosProntos().add(getListaDeProcessosCriados().get(0));
                        getListaDeProcessosCriados().remove(0);
                        i--;
                    } else {
                        getListaDeProcessosConcluidos().add(getListaDeProcessosCriados().get(0));
                        getListaDeProcessosCriados().remove(0);
                    }
                }
            }else if(getListaDeProcessosCriados().get(i).getTempoDeChegada() > getTempoEscalonado()) break;
        }

        // Verifica se o processo em execução acabou, caso positivo, sobre um novo processo da fila de processos
        // esperando, e move o processo em execução para fila de processos executados
        if(!getListaDeProcessosEmExecucao().isEmpty()) {
            if(getListaDeProcessosEmExecucao().get(0).getTempo() == 0) {
                getListaDeProcessosEmExecucao().get(0).setTempoTotal(getListaDeProcessosEmExecucao().get(0).getTempoInicio() + getListaDeProcessosEmExecucao().get(0).getTempoDeEspera());
                getListaDeProcessosConcluidos().add(getListaDeProcessosEmExecucao().get(0));
                getListaDeProcessosEmExecucao().remove(0);
                if(getListaDeProcessosProntos().size() > 0) {
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosProntos().get(0));
                    getListaDeProcessosEmExecucao().get(0).setTempoDeExecucao(0);
                    getListaDeProcessosProntos().remove(0);
                }
            }
            // Verifica se o tempo de processo em execução é igual ao quantum, caso positivo, move o processo para wait
            // e adiciona um novo processo para execução
            if(getListaDeProcessosEmExecucao().size() > 0) {
                if(getListaDeProcessosEmExecucao().get(0).getTempoDeExecucao() == quantumParametro) {
                    getListaDeProcessosProntos().add(getListaDeProcessosEmExecucao().get(0));
                    getListaDeProcessosEmExecucao().remove(0);
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosProntos().get(0));
                    getListaDeProcessosEmExecucao().get(0).setTempoDeExecucao(0);
                    getListaDeProcessosProntos().remove(0);
                }
            }
        }
    }

	public int getQtdeTrocaDeContextoRoundRobin() {
		return qtdeTrocaDeContextoRoundRobin;
	}

	public void setQtdeTrocaDeContextoRoundRobin(int qtdeTrocaDeContextoRoundRobin) {
		this.qtdeTrocaDeContextoRoundRobin = qtdeTrocaDeContextoRoundRobin;
	}

	public double getValorTrocaDeContextoRoundRobin() {
		return valorTrocaDeContextoRoundRobin;
	}

	public void setValorTrocaDeContextoRoundRobin(double valorTrocaDeContextoRoundRobin) {
		this.valorTrocaDeContextoRoundRobin = valorTrocaDeContextoRoundRobin;
	}

	public double getIntervaloDeTempoRoundRobinEmSegundos() {
		return intervaloDeTempoRoundRobinEmSegundos;
	}

	public void setIntervaloDeTempoRoundRobinEmSegundos(double intervaloDeTempoRoundRobinEmSegundos) {
		this.intervaloDeTempoRoundRobinEmSegundos = intervaloDeTempoRoundRobinEmSegundos;
	}

	public double getQuantumRoundRobin() {
		return quantumRoundRobin;
	}

	//em ms = millisegundos.
	public void setQuantumRoundRobin(double quantumRoundRobin) {
		this.quantumRoundRobin = quantumRoundRobin;
	}


	public double tempoTotalDeLatencia() { // Tempo total de latência => soma dos tempos de troca de contexto. Overhead.
		return valorTrocaDeContextoRoundRobin;
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

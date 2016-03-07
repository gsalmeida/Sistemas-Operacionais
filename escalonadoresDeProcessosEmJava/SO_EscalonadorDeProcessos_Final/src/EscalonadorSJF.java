import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class EscalonadorSJF extends Escalonador  implements Runnable {
	//incrementa 0,001 segundos a cada laço do for. Deve considerar as três casas decimais depois do virgula.
	// Entre 0,4 segundos e 5 segundos, Todos os processos devem estar prontos para serem executados.
	private double tempoEscalonadorSJF = 0; 	//incrementa 0,001 segundos a cada laço do for.
	
	private int qtdeTrocaDeContextoSJF = 0; // em millisegundos.
	private double valorTrocaDeContextoSJF = 0;
	private double intervaloDeTempoSJFEmSegundos = 0.1;
	
	
//	private double trocaDeContextoSJF;
//    
//    public double getTrocaDeContextoSJF() {
//    	return trocaDeContextoSJF;
//    }
//    
//    public void setTrocaDeContextoSJF(double trocaDeConextoSJF) {
//    	this.trocaDeContextoSJF = trocaDeConextoSJF;
//    }
	

	public EscalonadorSJF(String arquivoCSVComProcessos, int qtde) {
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
	
	public void iniciarPreemptivo() {
		runPreemptivo();
	}
	

	
	
	@Override
	public void run() {
		boolean inicio = true;
		BufferedWriter buffWrite = null;
		String linha = "";
		Scanner in = new Scanner(System.in);
		try {
			buffWrite = new BufferedWriter(new FileWriter(new File("saidaSJF.html")));
		} catch (IOException e) { e.printStackTrace(); }
		linha += "<html>";
		
		String vetorTempoEscalonadorSJFFormatadoAux[];
		String tempoEscalonadorSJFFormatado = "";
		
		while (!completouTodosProcessos()) {
			try {
				tempoEscalonadorSJF += getIntervaloDeTempoSJFEmSegundos();
//				JOptionPane.showMessageDialog(null, "Valor Da Conversão: "+ Double.parseDouble(String.valueOf(getIntervaloDeTempoSJFEmSegundos()).toString().substring(0,3)));
//				JOptionPane.showMessageDialog(null, "tempoEscalonadorSJF: "+tempoEscalonadorSJF);
				Thread.sleep(1-(10/100));
				linha += "<HR STYLE='COLOR: ORANGE;' />";
					vetorTempoEscalonadorSJFFormatadoAux = String.valueOf(tempoEscalonadorSJF).toString().split(".");
					tempoEscalonadorSJFFormatado = String.valueOf(tempoEscalonadorSJF);
					if(vetorTempoEscalonadorSJFFormatadoAux.length == 2) {
						if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 1) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + vetorTempoEscalonadorSJFFormatadoAux[1]; 
						else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 2) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2]; 
						else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() >= 3) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2] + vetorTempoEscalonadorSJFFormatadoAux[3];
					}
					Thread.sleep(1000-(10/100));
					linha += "<BR/><BR/><b style='color:blue;'>TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+" SEGUNDOS.</b><BR/><BR/>";
					System.out.println("TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+" SEGUNDOS.");
				
				if (!completouTodosProcessos()) { // 1 segundo = 1000 milisegundos // 0,0001 s = 0,1 milisegundos //troca de contexto
					setTempoEscalonado(tempoEscalonadorSJF);
//					System.out.println("------------>tempoEscalonadorSJF---> "+tempoEscalonadorSJF);
//					System.out.println("------------>tempoEscalonadorSJF---> "+String.valueOf(tempoEscalonadorSJF).toString().substring(0,3));
					if(inicio) { 
						linha += strHTMLArquivoSaida();
						Collections.sort(getListaDeProcessosCriados());
						linha += "<BR/><BR/><b style='color:BLACK;'>VETOR ORDENADO:</b><BR/><BR/>";
						linha += strHTMLArquivoSaida();
						setTempoEscalonado(0);
						inicio = false;
					} 
					
					this.executarPassoSJFNaoPreemptivo(); linha += strHTMLArquivoSaida();
					
					qtdeTrocaDeContextoSJF++;
					valorTrocaDeContextoSJF += (1/10);
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

	public void runPreemptivo() {
		boolean inicio = true;
		BufferedWriter buffWrite = null;
		String linha = "";
		Scanner in = new Scanner(System.in);
		try {
			buffWrite = new BufferedWriter(new FileWriter(new File("saidaSJF.html")));
		} catch (IOException e) { e.printStackTrace(); }
		linha += "<html>";
		
		String vetorTempoEscalonadorSJFFormatadoAux[];
		String tempoEscalonadorSJFFormatado = "";
		
		while (!completouTodosProcessos()) {
			try {
				tempoEscalonadorSJF += getIntervaloDeTempoSJFEmSegundos();
//				JOptionPane.showMessageDialog(null, "Valor Da Conversão: "+ Double.parseDouble(String.valueOf(getIntervaloDeTempoSJFEmSegundos()).toString().substring(0,3)));
//				JOptionPane.showMessageDialog(null, "tempoEscalonadorSJF: "+tempoEscalonadorSJF);
				Thread.sleep(1-(10/100));
				linha += "<HR STYLE='COLOR: ORANGE;' />";
					vetorTempoEscalonadorSJFFormatadoAux = String.valueOf(tempoEscalonadorSJF).toString().split(".");
					tempoEscalonadorSJFFormatado = String.valueOf(tempoEscalonadorSJF);
					if(vetorTempoEscalonadorSJFFormatadoAux.length == 2) {
						if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 1) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + vetorTempoEscalonadorSJFFormatadoAux[1]; 
						else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() == 2) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2]; 
						else if(vetorTempoEscalonadorSJFFormatadoAux[1].length() >= 3) tempoEscalonadorSJFFormatado = vetorTempoEscalonadorSJFFormatadoAux[0] + "." + vetorTempoEscalonadorSJFFormatadoAux[1] + vetorTempoEscalonadorSJFFormatadoAux[2] + vetorTempoEscalonadorSJFFormatadoAux[3];
					}
					Thread.sleep(1000-(10/100));
					linha += "<BR/><BR/><b style='color:blue;'>TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+" SEGUNDOS.</b><BR/><BR/>";
					System.out.println("TEMPO ESCALONADO: "+tempoEscalonadorSJFFormatado+" SEGUNDOS.");
				
				if (!completouTodosProcessos()) { // 1 segundo = 1000 milisegundos // 0,0001 s = 0,1 milisegundos //troca de contexto
					setTempoEscalonado(tempoEscalonadorSJF);
//					System.out.println("------------>tempoEscalonadorSJF---> "+tempoEscalonadorSJF);
//					System.out.println("------------>tempoEscalonadorSJF---> "+String.valueOf(tempoEscalonadorSJF).toString().substring(0,3));
					if(inicio) {
						linha += strHTMLArquivoSaida();
						Collections.sort(getListaDeProcessosCriados());
						linha += "<BR/><BR/><b style='color:BLACK;'>VETOR ORDENADO:</b><BR/><BR/>";
						linha += strHTMLArquivoSaida();
						setTempoEscalonado(0);
						inicio = false;
					}
					
					this.executarPassoSJFPreemptivo(); linha += strHTMLArquivoSaida();
					
					qtdeTrocaDeContextoSJF++;
//					valorTrocaDeContextoSJF += (1/10);
					valorTrocaDeContextoSJF += 0.1;
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
	
	
    // Executa um step do algoritmo SJF Não-Preemptivo
    public int executarPassoSJFNaoPreemptivo() {
    	int retorno = 0;
    	Collections.sort(getListaDeProcessosCriados());
        aumentarTempoDeEsperaDeProcessosProntos((0.9)+getIntervaloDeTempoSJFEmSegundos());
        if(!getListaDeProcessosEmExecucao().isEmpty()) getListaDeProcessosEmExecucao().get(0).diminuirTempo((0.9)+getIntervaloDeTempoSJFEmSegundos());
        
        for(int a = 0; a <= getListaDeProcessosCriados().size() -1; a++) {
        	//JOptionPane.showMessageDialog(null, getListaDeProcessosCriados().get(0).getTempoDeChegada());
        	//JOptionPane.showMessageDialog(null, "TEMPO ESCALONADO: "+getTempoEscalonado());
//            if(getListaDeProcessosCriados().get(a).getTempoDeChegada() == getTempoEscalonado()) {
            if( 
            		(String.valueOf(getListaDeProcessosCriados().get(a).getTempoDeChegada()).toString().substring(0,3))
            		.equals
            		(String.valueOf(getTempoEscalonado()).toString().substring(0,3)) 
              ) {
            	
            //if(getListaDeProcessosCriados().get(0).getTempoDeChegada() == tempoEscalonadorSJF) {
                // Entra aqui somente se o arrival time de algum processo da fila de processos a chegar for igual ao tempo
                // Se a lista de processos em execução estiver vazia, adiciona diretamente para lista de processos em execução
                if(getListaDeProcessosEmExecucao().isEmpty()) {
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosCriados().get(a));
                    getListaDeProcessosCriados().remove(a);
                    a--;
                } else {
                    // Se já existir um processo em execução, adicionar o processo a lista de processos aguardando / em espera
                    getListaDeProcessosProntos().add(getListaDeProcessosCriados().get(a));
                    getListaDeProcessosCriados().remove(a);
                    a--;
                    //SE DESCOMENTAR FICA PREEMPTIVO 
                    // Verifica se o tempo de processador do processo em execução ainda é menor que o tempo de processador
                    // dos processsos aguardando para execução, caso negativo, substitui
//                    organizarListaDeProcessosProntos();
//                    int PTEmExec = listaDeProcessosEmExecucao.get(0).getTempo();
//                    for(int j = 0; j <= listaDeProcessosProntos.size() -1; j++){
//                        if(listaDeProcessosProntos.get(j).getTempo() < PTEmExec){
//                            listaDeProcessosProntos.add(listaDeProcessosEmExecucao.get(0));
//                            listaDeProcessosEmExecucao.remove(0);
//                            listaDeProcessosEmExecucao.add(listaDeProcessosProntos.get(j));
//                            listaDeProcessosProntos.remove(j);
//                        }
//                    }
                    
                }
            } else if(getListaDeProcessosCriados().get(a).getTempoDeChegada() > tempoEscalonadorSJF) break;
            //} else if(getListaDeProcessosCriados().get(a).getTempoDeChegada() > getTempoEscalonado()) break;
            retorno = 1;
        }
        
        // Verifica se o processo em execução acabou, caso positivo, sobe um novo processo da fila de processos
        // aguardando, e move o processo em execução para fila de processos executados
        if(!getListaDeProcessosEmExecucao().isEmpty()) {
            if(getListaDeProcessosEmExecucao().get(0).getTempo() == 0) {
                getListaDeProcessosEmExecucao().get(0).setTempoTotal(getListaDeProcessosEmExecucao().get(0).getTempoInicio() + getListaDeProcessosEmExecucao().get(0).getTempoDeEspera());
                getListaDeProcessosConcluidos().add(getListaDeProcessosEmExecucao().get(0));
                getListaDeProcessosEmExecucao().remove(0);
                if(getListaDeProcessosProntos().size() > 0) {
                    organizarListaDeProcessosProntos();
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosProntos().get(0));
                    getListaDeProcessosProntos().remove(0);
                }
                retorno = 1;   
            }            
        }
        return retorno;
    }
    
    
    
    // Executa um step do algoritmo SJF Preemptivo
    public void executarPassoSJFPreemptivo() {
    	Collections.sort(getListaDeProcessosCriados());
        aumentarTempoDeEsperaDeProcessosProntos((0.9)+getIntervaloDeTempoSJFEmSegundos());
        if(!getListaDeProcessosEmExecucao().isEmpty()) getListaDeProcessosEmExecucao().get(0).diminuirTempo((0.9)+getIntervaloDeTempoSJFEmSegundos());
        
        for(int a = 0; a <= getListaDeProcessosCriados().size() -1; a++) {
//            if(getListaDeProcessosCriados().get(0).getTempoDeChegada() == getTempoEscalonado()) {
        	if( 
            		(String.valueOf(getListaDeProcessosCriados().get(a).getTempoDeChegada()).toString().substring(0,3))
            		.equals
            		(String.valueOf(getTempoEscalonado()).toString().substring(0,3)) 
              ) {
                // Entra aqui somente se o arrival time de algum processo da fila de processos a chegar for igual ao tempo
                // Se a lista de processos em execução estiver vazia, adiciona diretamente para lista de processos em execução
                if(getListaDeProcessosEmExecucao().isEmpty()) {
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosCriados().get(0));
                    getListaDeProcessosCriados().remove(0);
                    a--;
                } else {
                    // Se já existir um processo em execução, adicionar o processo a lista de processos aguardando / em espera
                    getListaDeProcessosProntos().add(getListaDeProcessosCriados().get(0));
                    getListaDeProcessosCriados().remove(0);
                    a--;
                    // Verifica se o tempo de processador do processo em execução ainda é menor que o tempo de processador
                    // dos processsos aguardando para execução, caso negativo, substitui
                    organizarListaDeProcessosProntos();
                    double PTEmExec = getListaDeProcessosEmExecucao().get(0).getTempo();
                    for(int j = 0; j <= getListaDeProcessosProntos().size() -1; j++) {
                        if(getListaDeProcessosProntos().get(j).getTempo() < PTEmExec) {
                            getListaDeProcessosProntos().add(getListaDeProcessosEmExecucao().get(0));
                            getListaDeProcessosEmExecucao().remove(0);
                            getListaDeProcessosEmExecucao().add(getListaDeProcessosProntos().get(j));
                            getListaDeProcessosProntos().remove(j);
                        }
                    }
                    
                }
                
            } else if(getListaDeProcessosCriados().get(a).getTempoDeChegada() > getTempoEscalonado()) break;
        }

        // Verifica se o processo em execução acabou, caso positivo, sobe um novo processo da fila de processos
        // aguardando, e move o processo em execução para fila de processos executados
        if(!getListaDeProcessosEmExecucao().isEmpty()) {
            if(getListaDeProcessosEmExecucao().get(0).getTempo() == 0) {
                getListaDeProcessosEmExecucao().get(0).setTempoTotal(getListaDeProcessosEmExecucao().get(0).getTempoInicio() + getListaDeProcessosEmExecucao().get(0).getTempoDeEspera());
                getListaDeProcessosConcluidos().add(getListaDeProcessosEmExecucao().get(0));
                getListaDeProcessosEmExecucao().remove(0);
                if(getListaDeProcessosProntos().size() > 0) {
                    organizarListaDeProcessosProntos();
                    getListaDeProcessosEmExecucao().add(getListaDeProcessosProntos().get(0));
                    getListaDeProcessosProntos().remove(0);
                }
            }
        }
    }

	public int getQtdeTrocaDeContextoSJF() {
		return qtdeTrocaDeContextoSJF;
	}

	public void setQtdeTrocaDeContextoSJF(int qtdeTrocaDeContextoSJF) {
		this.qtdeTrocaDeContextoSJF = qtdeTrocaDeContextoSJF;
	}

	public double getValorTrocaDeContextoSJF() {
		return valorTrocaDeContextoSJF;
	}

	public void setValorTrocaDeContextoSJF(double valorTrocaDeContextoSJF) {
		this.valorTrocaDeContextoSJF = valorTrocaDeContextoSJF;
	}

	public double getIntervaloDeTempoSJFEmSegundos() {
		return intervaloDeTempoSJFEmSegundos;
	}

	public void setIntervaloDeTempoSJFEmSegundos(double intervaloDeTempoSJFEmSegundos) {
		this.intervaloDeTempoSJFEmSegundos = intervaloDeTempoSJFEmSegundos;
	}

	public double tempoTotalDeLatencia() { // Tempo total de latência => soma dos tempos de troca de contexto. Overhead.
		return valorTrocaDeContextoSJF;
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

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Principal {

	public String menuPrincipal() {
		String retorno = "";
		retorno += "1 - CRIAR 10 PROCESSOS\n";
		retorno += "2 - CRIAR 100 PROCESSOS\n";
		retorno += "3 - CRIAR 1000 PROCESSOS\n";
		retorno += "\nEXECUTAR PROCESSOS\n";
		retorno += "4 - FIFO (NÃO PREEMPTIVO)\n";
		retorno += "5 - SJF (NÃO PREEMPTIVO)\n";
		retorno += "6 - SJF (PREEMPTIVO)\n";
		retorno += "7 - ROUND ROBIN (PREEMPTIVO)\n";
		retorno += "8 - FILAS DE PRIORIDADE DINÂMICA (PREEMPTIVO)\n";
		return retorno;
	}

	public void criarArquivoCSVResultado(String nomeDoArquivo, Escalonador escalonador, List<String> listaDeValores) throws IOException {
		BufferedWriter buffWrite = null;
		try {
			buffWrite = new BufferedWriter(new FileWriter(new File(nomeDoArquivo)));
		} catch (IOException e) { e.printStackTrace(); }
		
		String linha = "";
		Scanner in = new Scanner(System.in);
		buffWrite.append("PID;TEMPO DE CHEGADA; TEMPO DE EXECUCAO; TEMPO DE ESPERA; TEMPO TOTAL"+ "\n"); //PRIMEIRA LINHA.
		for(int a = 0; a < escalonador.getListaDeProcessosConcluidos().size(); a++) {
			linha = escalonador.getListaDeProcessosConcluidos().get(a).getId()+";"+escalonador.getListaDeProcessosConcluidos().get(a).getTempoDeChegada()+";"+
					escalonador.getListaDeProcessosConcluidos().get(a).getTempoInicio()+";"+escalonador.getListaDeProcessosConcluidos().get(a).getTempoDeEspera()+";"+
					escalonador.getListaDeProcessosConcluidos().get(a).getTempoTotal();
			buffWrite.append(linha + "\n");
		}
		
		buffWrite.append("tp; to; tt; qtdeTrocaContexto; OcupacaoDoProcessador; OverheadDeProcessamento; tempoMedioDeEspera; quantum\n");
		for(int a =0; a < listaDeValores.size(); a++) {
			linha = listaDeValores.get(a).toString();
			buffWrite.append(linha + ";");
		}
		buffWrite.append("\n");
		//ULTIMA LINHA.
		buffWrite.close();
	}

	
	private void criarProcessos(String arquivoCSVComProcessos, int qtde) throws IOException {
		System.out.println("DEZ PROCESSOS.");
		
	    double valorAleatorioTempoDeChegada = (double) (0.4 + (Math.random() * 4.6));
		double valorAleatorioTempoDeExecucao = (double) (0.4 + (Math.random() * 4.6));
		
		BufferedWriter buffWrite = null;
		
		try {
			buffWrite = new BufferedWriter(new FileWriter(new File(arquivoCSVComProcessos)));
		} catch (IOException e) { e.printStackTrace(); }
		
		String linha = "";
		Scanner in = new Scanner(System.in);
		
		for(int a = 1; a <= qtde; a++) {
			valorAleatorioTempoDeChegada = (double) (0.4 + (Math.random() * 4.6));
			valorAleatorioTempoDeExecucao = (double) (0.4 + (Math.random() * 4.6));
			
			String strValorAleatorioTempoDeChegada = String.valueOf(valorAleatorioTempoDeChegada);
			strValorAleatorioTempoDeChegada = strValorAleatorioTempoDeChegada.substring(0, 5);
			
			String strValorAleatorioTempoDeExecucao = String.valueOf(valorAleatorioTempoDeExecucao);
			strValorAleatorioTempoDeExecucao = strValorAleatorioTempoDeExecucao.substring(0, 5);
			
			linha = a+";"+strValorAleatorioTempoDeChegada+";"+strValorAleatorioTempoDeExecucao;
			buffWrite.append(linha + "\n");
		}
		buffWrite.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		
		// CRIA OS 10 PROCESSOS EM UM ARQUIVO FORMATO CSV E EXECUTA. PREENCHE A
		// LISTA DE PROCESSOS CRIADOS A PARTIR DO CSV.
		Principal principal = new Principal();
		String menu = JOptionPane.showInputDialog(principal.menuPrincipal());
		
		EscalonadorRoundRobin escalonadorRoundRobin;
		EscalonadorFIFO escalonadorFIFO;
		EscalonadorSJF escalonadorSJF;
		EscalonadorFilasDePrioridadeDinamica escalonadorFilasDePrioridadeDinamica;
		
		double trocaDeContexto = 0;
		List<String> listaDeValoresResultados;
		boolean iniciouAlgumAlgoitmo = false;
		int menuInt = 0;
		int qtdeDeProcessos = 0;
		while (!iniciouAlgumAlgoitmo) {
			if (menu.equals("1") || menu.equals("2") || menu.equals("3") || menu.equals("4") || menu.equals("5")
					|| menu.equals("6") || menu.equals("7") || menu.equals("8")) {
				menuInt = Integer.parseInt(menu);
				switch (menuInt) {
				case 1:
					qtdeDeProcessos = 10;
					principal.criarProcessos("processosCSV.txt", qtdeDeProcessos);
					JOptionPane.showMessageDialog(null, "DEZ PROCESSOS CRIADOS COM SUCESSO.");
					menu = JOptionPane.showInputDialog(principal.menuPrincipal());
					break;
				case 2:
					qtdeDeProcessos = 100;
					principal.criarProcessos("processosCSV.txt", qtdeDeProcessos);
					JOptionPane.showMessageDialog(null, "CEM PROCESSOS CRIADOS COM SUCESSO.");
					menu = JOptionPane.showInputDialog(principal.menuPrincipal());
					break;
				case 3:
					qtdeDeProcessos = 1000;
					principal.criarProcessos("processosCSV.txt", qtdeDeProcessos);
					JOptionPane.showMessageDialog(null, "MIL PROCESSOS CRIADOS COM SUCESSO.");
					menu = JOptionPane.showInputDialog(principal.menuPrincipal());
					break;
				case 4:
					escalonadorFIFO = new EscalonadorFIFO("processosCSV.txt", qtdeDeProcessos);
					escalonadorFIFO.setTrocaDeContexto(1/10); //em millisegundos
//					escalonadorFIFO.setTrocaDeContexto(0.0001);
//					escalonadorFIFO.setIntervaloDeTempoFIFOEmSegundos(0.1);
//					escalonadorFIFO.setIntervaloDeTempoEmMillisegundos(1000); // 0,1 segundos = 100 milisegundos
					escalonadorFIFO.iniciar();
//					trocaDeContexto = escalonadorFIFO.getTrocaDeContexto(); // 1 segundo = 1000 milisegundos // 0,0001 s = 0,1 milisegundos //troca de contexto
					for(int a = 0; a < escalonadorFIFO.getListaDeProcessosCriados().size(); a++) {
						System.out.println("PROCESSO ID: "+escalonadorFIFO.getListaDeProcessosCriados().get(a).getId()+" | "+escalonadorFIFO.getListaDeProcessosCriados().get(a).getTempoDeChegada()+" | "+escalonadorFIFO.getListaDeProcessosCriados().get(a).getTempo());
					}
					
					listaDeValoresResultados = new ArrayList<String>();
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getTp()));
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getTo()));
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getTt()));
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getQtdeTrocaDeContextoFIFO()));
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getOcupacaoDoProcessador()));
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getOverheadDeProcessamento()));
					listaDeValoresResultados.add(String.valueOf(escalonadorFIFO.getTempoMediaDeEspera()));
					
					principal.criarArquivoCSVResultado("saidaFIFO.csv", escalonadorFIFO, listaDeValoresResultados);
					
					System.out.println("(Tp) - Tempo total de processamento útil (soma dos tempos de todos os processos): "+escalonadorFIFO.getTp());
					System.out.println("(To) - Tempo total de latência (soma dos tempos de troca de contexto). Overhead: "+escalonadorFIFO.getTo());
					System.out.println("(Tt)- Tempo total de processamento bruto (Tp + To): "+escalonadorFIFO.getTt());
					System.out.println("Número de troca de contextos: "+escalonadorFIFO.getQtdeTrocaDeContextoFIFO());
					System.out.println("Ocupação do processador (Tp/Tt) (%): "+escalonadorFIFO.getOcupacaoDoProcessador());
					System.out.println("Overhead de Processamento (To/Tt) (%): "+escalonadorFIFO.getOverheadDeProcessamento());
					System.out.println("Tempo média de espera: "+escalonadorFIFO.getTempoMediaDeEspera());
					
					iniciouAlgumAlgoitmo = true;
					break;
				case 5:
					escalonadorSJF = new EscalonadorSJF("processosCSV.txt", qtdeDeProcessos);
					escalonadorSJF.setTrocaDeContexto(1/10); //em millisegundos.
//					escalonadorSJF.setTrocaDeContexto(0.0001);
//					escalonadorSJF.setIntervaloDeTempoSJFEmSegundos(0.1);
					escalonadorSJF.iniciar();
					for(int a = 0; a < escalonadorSJF.getListaDeProcessosCriados().size(); a++) {
						System.out.println("PROCESSO ID: "+escalonadorSJF.getListaDeProcessosCriados().get(a).getId()+" | "+escalonadorSJF.getListaDeProcessosCriados().get(a).getTempoDeChegada()+" | "+escalonadorSJF.getListaDeProcessosCriados().get(a).getTempo());
					}
					listaDeValoresResultados = new ArrayList<String>();
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTp()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTo()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTt()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getQtdeTrocaDeContextoSJF()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getOcupacaoDoProcessador()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getOverheadDeProcessamento()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTempoMediaDeEspera()));
					
					principal.criarArquivoCSVResultado("saidaSJFNaoPreemptivo.csv", escalonadorSJF, listaDeValoresResultados);
					
					System.out.println("(Tp) - Tempo total de processamento útil (soma dos tempos de todos os processos): "+escalonadorSJF.getTp());
					System.out.println("(To) - Tempo total de latência (soma dos tempos de troca de contexto). Overhead: "+escalonadorSJF.getTo());
					System.out.println("(Tt)- Tempo total de processamento bruto (Tp + To): "+escalonadorSJF.getTt());
					System.out.println("Número de troca de contextos: "+escalonadorSJF.getQtdeTrocaDeContextoSJF());
					System.out.println("Ocupação do processador (Tp/Tt) (%): "+escalonadorSJF.getOcupacaoDoProcessador());
					System.out.println("Overhead de Processamento (To/Tt) (%): "+escalonadorSJF.getOverheadDeProcessamento());
					System.out.println("Tempo média de espera: "+escalonadorSJF.getTempoMediaDeEspera());
					
					iniciouAlgumAlgoitmo = true;
					break;
				case 6:
					escalonadorSJF = new EscalonadorSJF("processosCSV.txt", qtdeDeProcessos);
					escalonadorSJF.setTrocaDeContexto(1/10); //em millisegundos.
//					escalonadorSJF.setTrocaDeContexto(0.0001);
//					escalonadorSJF.setIntervaloDeTempoSJFEmSegundos(0.1);
					escalonadorSJF.iniciarPreemptivo();
					for(int a = 0; a < escalonadorSJF.getListaDeProcessosCriados().size(); a++) {
						System.out.println("PROCESSO ID: "+escalonadorSJF.getListaDeProcessosCriados().get(a).getId()+" | "+escalonadorSJF.getListaDeProcessosCriados().get(a).getTempoDeChegada()+" | "+escalonadorSJF.getListaDeProcessosCriados().get(a).getTempo());
					}
					listaDeValoresResultados = new ArrayList<String>();
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTp()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTo()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTt()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getQtdeTrocaDeContextoSJF()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getOcupacaoDoProcessador()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getOverheadDeProcessamento()));
					listaDeValoresResultados.add(String.valueOf(escalonadorSJF.getTempoMediaDeEspera()));
					
					principal.criarArquivoCSVResultado("saidaSJFPreemptivo.csv", escalonadorSJF, listaDeValoresResultados);
					
					System.out.println("(Tp) - Tempo total de processamento útil (soma dos tempos de todos os processos): "+escalonadorSJF.getTp());
					System.out.println("(To) - Tempo total de latência (soma dos tempos de troca de contexto). Overhead: "+escalonadorSJF.getTo());
					System.out.println("(Tt)- Tempo total de processamento bruto (Tp + To): "+escalonadorSJF.getTt());
					System.out.println("Número de troca de contextos: "+escalonadorSJF.getQtdeTrocaDeContextoSJF());
					System.out.println("Ocupação do processador (Tp/Tt) (%): "+escalonadorSJF.getOcupacaoDoProcessador());
					System.out.println("Overhead de Processamento (To/Tt) (%): "+escalonadorSJF.getOverheadDeProcessamento());
					System.out.println("Tempo média de espera: "+escalonadorSJF.getTempoMediaDeEspera());
					
					iniciouAlgumAlgoitmo = true;
					break;
				case 7:
					escalonadorRoundRobin = new EscalonadorRoundRobin("processosCSV.txt", qtdeDeProcessos, 0.1);
//					escalonadorRoundRobin = new EscalonadorRoundRobin("processosCSV.txt", qtdeDeProcessos, 0.3);
					escalonadorRoundRobin.setTrocaDeContexto(1/10); //em millisegundos.
//					escalonadorRoundRobin.setTrocaDeContexto(0.0001);
//					escalonadorRoundRobin.setIntervaloDeTempoRoundRobinEmSegundos(0.1);
					escalonadorRoundRobin.setIntervaloDeTempoRoundRobinEmSegundos(0.1);
					escalonadorRoundRobin.iniciar();
					for(int a = 0; a < escalonadorRoundRobin.getListaDeProcessosCriados().size(); a++) {
						System.out.println("PROCESSO ID: "+escalonadorRoundRobin.getListaDeProcessosCriados().get(a).getId()+" | "+escalonadorRoundRobin.getListaDeProcessosCriados().get(a).getTempoDeChegada()+" | "+escalonadorRoundRobin.getListaDeProcessosCriados().get(a).getTempo());
					}
					
					listaDeValoresResultados = new ArrayList<String>();
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getTp()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getTo()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getTt()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getQtdeTrocaDeContextoRoundRobin()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getOcupacaoDoProcessador()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getOverheadDeProcessamento()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getTempoMediaDeEspera()));
					listaDeValoresResultados.add(String.valueOf(escalonadorRoundRobin.getQuantumRoundRobin()));
					
					principal.criarArquivoCSVResultado("saidaRoundRobin.csv", escalonadorRoundRobin, listaDeValoresResultados);
					
					System.out.println("(Tp) - Tempo total de processamento útil (soma dos tempos de todos os processos): "+escalonadorRoundRobin.getTp());
					System.out.println("(To) - Tempo total de latência (soma dos tempos de troca de contexto). Overhead: "+escalonadorRoundRobin.getTo());
					System.out.println("(Tt)- Tempo total de processamento bruto (Tp + To): "+escalonadorRoundRobin.getTt());
					System.out.println("Número de troca de contextos: "+escalonadorRoundRobin.getQtdeTrocaDeContextoRoundRobin());
					System.out.println("Ocupação do processador (Tp/Tt) (%): "+escalonadorRoundRobin.getOcupacaoDoProcessador());
					System.out.println("Overhead de Processamento (To/Tt) (%): "+escalonadorRoundRobin.getOverheadDeProcessamento());
					System.out.println("Tempo média de espera: "+escalonadorRoundRobin.getTempoMediaDeEspera());
					System.out.println("QUANTUM: "+escalonadorRoundRobin.getQuantumRoundRobin());
					
					iniciouAlgumAlgoitmo = true;
					break;
				case 8:
					JOptionPane.showMessageDialog(null, "8");
					iniciouAlgumAlgoitmo = true;
					break;
				default:
					break;
				}
			} else {
				menu = JOptionPane.showInputDialog("Digite um valor válido.\n" + principal.menuPrincipal());
			}
		}
	}

}


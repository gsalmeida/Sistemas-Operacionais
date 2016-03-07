import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.DefaultTableModel;



public class Escalonador {

	//troca de contexto deve ser 10 us (0,010 ms)
	private double tempoEscalonado = 0; 
    private List<Processo> listaDeProcessosCriados = new ArrayList<Processo>();
    private List<Processo> listaDeProcessosEmExecucao = new ArrayList<Processo>();
    private List<Processo> listaDeProcessosProntos = new ArrayList<Processo>();
    private List<Processo> listaDeProcessosConcluidos = new ArrayList<Processo>();
    
    private double trocaDeContexto;
    
    public double getTrocaDeContexto() {
    	return trocaDeContexto;
    }
    
    public void setTrocaDeContexto(double trocaDeConexto) {
    	this.trocaDeContexto = trocaDeConexto;
    }
    

	public String strHTMLArquivoSaida() {
		String linha = "";
		linha += "<br/><b>PROCESSOS CRIADOS</b><br/>";
		linha += "<table style='border: solid 1px black;'>"+
					"<tr>"+
						"<th width='33%' align='left'>PID</th>"+
						"<th width='33%' align='left'>TempoChegada</th>"+
						"<th width='33%' align='left'>TempoDeexecucao</th>"+
					"</tr>";
		for(int i = 0; i <= getListaDeProcessosCriados().size() -1; i++) {
			linha += "<tr>"+
						"<td width='33%' align='left'>"+getListaDeProcessosCriados().get(i).getId()+"</td>"+
						"<td width='33%' align='left'>"+getListaDeProcessosCriados().get(i).getTempoDeChegada()+"</td>"+
						"<td width='33%' align='left'>"+getListaDeProcessosCriados().get(i).getTempo()+"</td>"+
					"</tr>";
        }
		linha += "</table>";
		
		System.out.println("\n\n\nPROCESSOS CRIADOS\n\n\n");
		imprimirListaDeProcessosCriados();
		System.out.println("\n\n\nPROCESSOS PRONTOS\n\n\n");
		imprimirListaDeProcessosProntos();
		linha += "<br/><b>PROCESSOS PRONTOS</b><br/>";
		linha += "<table style='border: solid 1px black;'>"+
					"<tr>"+
						"<th width='33%' align='left'>PID</th>"+
						"<th width='33%' align='left'>TempoChegada</th>"+
						"<th width='33%' align='left'>TempoDeexecucao</th>"+
					"</tr>";
		for(int i = 0; i <= getListaDeProcessosProntos().size() -1; i++) {
			linha += "<tr>"+
						"<td width='33%' align='left'>"+getListaDeProcessosProntos().get(i).getId()+"</td>"+
						"<td width='33%' align='left'>"+getListaDeProcessosProntos().get(i).getTempoDeChegada()+"</td>"+
						"<td width='33%' align='left'>"+getListaDeProcessosProntos().get(i).getTempo()+"</td>"+
					"</tr>";
        }
		linha += "</table>";
		
		System.out.println("\n\n\nPROCESSOS EM EXECUÇÃO\n\n\n");
		imprimirListaDeProcessosEmExecucao();
		linha += "<br/><b>PROCESSOS EM EXECUCAO</b><br/>";
		linha += "<table style='border: solid 1px black;'>"+
					"<tr>"+
						"<th width='33%' align='left'>PID</th>"+
						"<th width='33%' align='left'>TempoChegada</th>"+
						"<th width='33%' align='left'>TempoDeexecucao</th>"+
					"</tr>";
		for(int i = 0; i <= getListaDeProcessosEmExecucao().size() -1; i++) {
			linha += "<tr>"+
						"<td width='33%' align='left'>"+getListaDeProcessosEmExecucao().get(i).getId()+"</td>"+
						"<td width='33%' align='left'>"+getListaDeProcessosEmExecucao().get(i).getTempoDeChegada()+"</td>"+
						"<td width='33%' align='left'>"+getListaDeProcessosEmExecucao().get(i).getTempo()+"</td>"+
					"</tr>";
        }
		linha += "</table>";
		
		System.out.println("\n\n\nPROCESSOS CONCLUÍDOS\n\n\n");
		imprimirListaDeProcessosConcluidos();
		
		linha += "<br/><b>PROCESSOS CONCLUIDOS</b><br/>";
		linha += "<table style='border: solid 1px black;'>"+
					"<tr>"+
						"<th width='20%' align='left'>PID</th>"+
						"<th width='20%' align='left'>TEMPO DE CHEGADA</th>"+
						"<th width='20%' align='left'>TEMPO DE EXECUCAO</th>"+
						"<th width='20%' align='left'>TEMPO DE ESPERA</th>"+
						"<th width='20%' align='left'>TEMPO TOTAL</th>"+
					"</tr>";
		for(int i = 0; i <= getListaDeProcessosConcluidos().size() -1; i++) {
			linha += "<tr>"+
						"<td width='20%' align='left'>"+getListaDeProcessosConcluidos().get(i).getId()+"</td>"+
						"<td width='20%' align='left'>"+getListaDeProcessosConcluidos().get(i).getTempoDeChegada()+"</td>"+
						"<td width='20%' align='left'>"+getListaDeProcessosConcluidos().get(i).getTempoInicio()+"</td>"+
						"<td width='20%' align='left'>"+getListaDeProcessosConcluidos().get(i).getTempoDeEspera()+"</td>"+
						"<td width='20%' align='left'>"+getListaDeProcessosConcluidos().get(i).getTempoTotal()+"</td>"+
					"</tr>";
        }
		linha += "</table>";
		return linha;
	}
	
    
    
    
    public void preencheListaDeProcessosCriados(String arquivoCSVComProcessos, int qtde) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(new File(arquivoCSVComProcessos)));
		String linha = "";
		while (true) {
			linha = buffRead.readLine();
			if (linha != null) {
				System.out.println(linha);
				String[] linhaSplit = linha.split(";");
				String idProcesso = linhaSplit[0];
				
				double tempoDeChegadaDoProcesso = 0;
				tempoDeChegadaDoProcesso = Double.parseDouble(linhaSplit[1]);
				
				double tempoDeExecucaoDoProcesso = 0;
				tempoDeExecucaoDoProcesso = Double.parseDouble(linhaSplit[2]);
				
				adicionarProcesso(idProcesso, tempoDeChegadaDoProcesso, tempoDeExecucaoDoProcesso);
			} else
				break;
		}
		buffRead.close();
	}
    
    
    
    /*
    	Métodos Get e Set.
    */
	public double getTempoEscalonado() {
		return tempoEscalonado;
	}

	public void setTempoEscalonado(double tempoEscalonado) {
		this.tempoEscalonado = tempoEscalonado;
//		this.tempoEscalonado += tempoEscalonado;
	}
    
    public List<Processo> getListaDeProcessosCriados() {
		return listaDeProcessosCriados;
	}

	public void setListaDeProcessosCriados(List<Processo> listaDeProcessosCriados) {
		this.listaDeProcessosCriados = listaDeProcessosCriados;
	}

	public List<Processo> getListaDeProcessosEmExecucao() {
		return listaDeProcessosEmExecucao;
	}

	public void setListaDeProcessosEmExecucao(List<Processo> listaDeProcessosEmExecucao) {
		this.listaDeProcessosEmExecucao = listaDeProcessosEmExecucao;
	}

	public List<Processo> getListaDeProcessosConcluidos() {
		return listaDeProcessosConcluidos;
	}

	public void setListaDeProcessosConcluidos(List<Processo> listaDeProcessosConcluidos) {
		this.listaDeProcessosConcluidos = listaDeProcessosConcluidos;
	}

	public List<Processo> getListaDeProcessosProntos() {
		return listaDeProcessosProntos;
	}

	public void setListaDeProcessosProntos(List<Processo> listaDeProcessosProntos) {
		this.listaDeProcessosProntos = listaDeProcessosProntos;
	}
	/*
	Fim métodos Get e Set.
	*/
		
    
    public boolean adicionarProcesso(String idProcesso, double tempoDeChegadaDoProcesso, double tempoDeExecucaoDoProcesso) {
        boolean existeProcessoIdentico = false;
        for(int a = 0; a <= getListaDeProcessosCriados().size() -1; a++) 
        	if(idProcesso.compareTo(getListaDeProcessosCriados().get(a).getId()) == 0) {
        		existeProcessoIdentico = true;
        		break;
        	}
        
        if(!existeProcessoIdentico) {
            Processo processoNovo = new Processo();
            processoNovo.setId(idProcesso);
            processoNovo.setTempo(tempoDeExecucaoDoProcesso);
            processoNovo.setTempoDeChegada(tempoDeChegadaDoProcesso);
            processoNovo.setTempoInicio(tempoDeExecucaoDoProcesso);
            getListaDeProcessosCriados().add(processoNovo);
            return true;
        }
        return false;
    }

    public Processo getListaDeProcessosCriados(int indice) {
        return listaDeProcessosCriados.get(indice);
    }

    public int getQtdeDeProcessosCriados() {
        return getListaDeProcessosCriados().size();
    }

    public void zerarTodasListasDeProcessos() {
        getListaDeProcessosCriados().clear();
        getListaDeProcessosProntos().clear();
        getListaDeProcessosEmExecucao().clear();
        getListaDeProcessosConcluidos().clear();
    }

    //ordena os processos pelo tempo de processamento.
    public void organizarListaDeProcessosProntos() {
        for(int i = 0; i < getListaDeProcessosProntos().size() -1; i++) {
            boolean alterou = true;
            while(alterou) {
                alterou = false;
                if(getListaDeProcessosProntos().get(i).getTempo() > getListaDeProcessosProntos().get(i+1).getTempo()) {
                    Processo processoTemp = getListaDeProcessosProntos().get(i+1);
                    getListaDeProcessosProntos().set(i+1, getListaDeProcessosProntos().get(i));
                    getListaDeProcessosProntos().set(i, processoTemp);
                    alterou = true;
                }
            }
        }
    }

    public void imprimirListaDeProcessosCriados() {
    	System.out.println("PID | TEMPO DE CHEGADA | TEMPO DE EXECUÇAO");
        for(int i = 0; i <= getListaDeProcessosCriados().size() -1; i++) {
        	System.out.println(getListaDeProcessosCriados().get(i).getId()+ "| "+
            					getListaDeProcessosCriados().get(i).getTempoDeChegada()+ " | "+ 
            					getListaDeProcessosCriados().get(i).getTempo());
        }
    }

    public void imprimirListaDeProcessosProntos() {
    	System.out.println("PID | TEMPO DE CHEGADA | TEMPO DE EXECUÇAO");
        for(int i = 0; i <= getListaDeProcessosProntos().size() -1; i++) {
        	System.out.println(getListaDeProcessosProntos().get(i).getId()+ "| "+
			        			getListaDeProcessosProntos().get(i).getTempoDeChegada()+ " | "+ 
			        			getListaDeProcessosProntos().get(i).getTempo());
        }
    }
    
    public void imprimirListaDeProcessosEmExecucao() {
    	System.out.println("PID | TEMPO DE CHEGADA | TEMPO DE EXECUÇAO");
        for(int i = 0; i <= getListaDeProcessosEmExecucao().size() -1; i++) {
        	System.out.println(getListaDeProcessosEmExecucao().get(i).getId()+ "| "+
        						getListaDeProcessosEmExecucao().get(i).getTempoDeChegada()+ " | "+ 
        						getListaDeProcessosEmExecucao().get(i).getTempo());
        }
    }
    
    public void imprimirListaDeProcessosConcluidos() {
    	System.out.println("PID | TEMPO DE CHEGADA | TEMPO DE EXECUÇAO | TEMPO DE ESPERA | TEMPO TOTAL");
        for(int i = 0; i <= getListaDeProcessosConcluidos().size() -1; i++) {
        	System.out.println(getListaDeProcessosConcluidos().get(i).getId()+ "| "+
        						getListaDeProcessosConcluidos().get(i).getTempoDeChegada()+ " | "+
        						getListaDeProcessosConcluidos().get(i).getTempoInicio()+ " | "+
        						getListaDeProcessosConcluidos().get(i).getTempoDeEspera()+ "| "+
        						getListaDeProcessosConcluidos().get(i).getTempoTotal());
        }
    }
        
    
    public boolean completouTodosProcessos() {
        if(getListaDeProcessosCriados().isEmpty() && getListaDeProcessosEmExecucao().isEmpty() && getListaDeProcessosProntos().isEmpty()) return true;
        return false;
    }

    public void aumentarTempoDeEsperaDeProcessosProntos(double tempoIncrementoDeEspera) {
        for(int i = 0; i <= getListaDeProcessosProntos().size() -1; i++) {
            getListaDeProcessosProntos().get(i).aumentarTempoDeEspera(tempoIncrementoDeEspera);
        }
    }

    public void organizarListaDeProcessosConcluidos() {
        for(int i = 0; i < getListaDeProcessosConcluidos().size() -1; i++) {
            boolean alterou = true;
            while(alterou) {
                alterou = false;
                if(getListaDeProcessosConcluidos().get(i).getId().compareTo(getListaDeProcessosConcluidos().get(i+1).getId()) == 1) {
                    Processo pAux = getListaDeProcessosConcluidos().get(i+1);
                    getListaDeProcessosConcluidos().set(i+1, getListaDeProcessosConcluidos().get(i));
                    getListaDeProcessosConcluidos().set(i, pAux);
                    alterou = true;
                }
            }
        }
    }

    public void organizarListaDeProcessosCriados() {
        for(int i = 0; i < getListaDeProcessosCriados().size() -1; i++) {
            boolean alterou = true;
            while(alterou) {
                alterou = false;
                if(getListaDeProcessosCriados().get(i).getTempoDeChegada() > getListaDeProcessosCriados().get(i+1).getTempoDeChegada()) {
                    Processo pAux = getListaDeProcessosCriados().get(i+1);
                    getListaDeProcessosCriados().set(i+1, getListaDeProcessosCriados().get(i));
                    getListaDeProcessosCriados().set(i, pAux);
                    alterou = true;
                }
            }
        }
    }

//    public double getTotalTurnaroundTime() { // tempoTotal
    public double getTempoTotalDeProcessamento() { // tempoTotal
    	double tempoTotal = 0;
        for(int i = 0; i <= getListaDeProcessosConcluidos().size() -1; i++) {
            tempoTotal = tempoTotal + getListaDeProcessosConcluidos().get(i).getTempoTotal();
        }
        return tempoTotal;
    }

//    public double getMediaTurnaroundTime() { // média do tempo total
   	public double getMediaDoTempoTotalDeProcessamento() { // média do tempo total
        return (double) getTempoTotalDeProcessamento() / getListaDeProcessosConcluidos().size();
    }
	
    public double tempoTotalDeProcessamentoUtil() { // Tempo total de processamento útil => soma dos tempos de todos os processos
		double tempoTotal = 0;
		for(int a = 0; a < getListaDeProcessosConcluidos().size(); a++) {
			Processo processoAux = getListaDeProcessosConcluidos().get(a);
			tempoTotal += processoAux.getTempoInicio();
//			tempoTotal += processoAux.getTempoTotal();  // tempo inicio - tempo de espera.
		}
		return tempoTotal;
    }
    
    public double getTp() { //	Tempo total de processamento útil => soma dos tempos de todos os processos
		return tempoTotalDeProcessamentoUtil();
	}
    
    public double getTempoMediaDeEspera() {
		double tempoTotal = 0;
		int qtdeRegistros = getListaDeProcessosConcluidos().size();
		for(int a = 0; a < qtdeRegistros; a++) {
			Processo processoAux = getListaDeProcessosConcluidos().get(a);
			tempoTotal += processoAux.getTempoDeEspera();
		}
		return (tempoTotal / qtdeRegistros);
	}
        
}



public class Processo implements Comparable<Processo> {

    private String id;
    
    //TEMPO EM QUE O PROCESSO CHEGA AO PROCESSADOR PARA SER PROCESSADO.
    private double tempoDeChegada; //em torno de 400 ms a 5 s de tempo.
    
    //TEMPO DE EXECUÇÃO DO PROCESSO NO PROCESSADOR. ESTE DIMINUI A CADA PASSO DE ALGORITMO DE ESCALONAMENTO.
    private double tempoDeExecucao = 0; //em torno de 400 ms a 5 s de tempo.
    
    //mesmo tempo de execução. variável auxiliar.
    private double tempo;
    
//    mesmo tempo de execução. 
//    Guarda o tempo de execução do processo, visto que as variáveis tempo e tempo de execução perdem seus valores.
    private double tempoInicio;
    
    
    private double tempoDeEspera = 0;
    private double tempoTotal;
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTempoDeChegada() {
        return tempoDeChegada;
    }

    public void setTempoDeChegada(double tempoDeChegada) {
        this.tempoDeChegada = tempoDeChegada;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempoDeExecucaoDoProcesso) {
        this.tempo = tempoDeExecucaoDoProcesso;
    }

    public double getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(double tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public double getTempoDeEspera() {
        return tempoDeEspera;
    }

    public void setTempoDeEspera(double tempoDeEspera) {
        this.tempoDeEspera = tempoDeEspera;
    }

    public void diminuirTempo(double tempoParametro) {
    	//if(tempo > 0) tempo = (tempo - tempoParametro);
//    	tempo -= 0.001;
    	tempo -= tempoParametro;
        if(tempo <= 0) tempo = 0;
    }
    
    public void setDiminuirTempo(double tempoEscalonado) {
        tempo -= tempoEscalonado;
    }
    
    public void diminuirTempo() {
        tempo--;
    }

    public void aumentarTempoDeEspera(double tempoEsperado) {
        tempoDeEspera += tempoEsperado;
    }
    
    public void aumentarTempoDeEspera() {
        tempoDeEspera++;
    }

    public double getTempoInicio() {
        return tempoInicio;
    }

    public void setTempoInicio(double tempoInicio) {
        this.tempoInicio = tempoInicio;
    }

    public void aumentarTempoDeExecucao(double tempoExecutado) {
    	tempoDeExecucao = (tempoDeExecucao + tempoExecutado);
    }
    
    public void aumentarTempoDeExecucao() {
        tempoDeExecucao++;
    }
    public double getTempoDeExecucao() {
        return tempoDeExecucao;
    }

    public void setTempoDeExecucao(double tempoDeExecucao) {
        this.tempoDeExecucao = tempoDeExecucao;
    }
    
    //compara o tempo dos processos. Compara para usar no Collections.sort(objetoContendolistaDeProcessos);
    // objetoContendolistaDeProcessos = Por exemplo: listaDeProcessosProntos, listaDeProcessosConcluidos, 
    // listaDeProcessosEmExecucao, listaDeProcessosCriados, listaDeProcessosProntos. Ex: Collections.sort(listaDeProcessosCriados);
    //retorna 1 se o tempo this.at do objeto Processo parâmetro for menor, 
    //ou se os tempos forem iguais this.at, retorna 1 se o this.pt do Processo parâmetro for menor 
    //e se também forem iguais this.pt, neste caso retorna 0. Todos os demais caso retorna -1.
    public int compareTo(Processo processoParametro) {
        if(this.tempoDeChegada == processoParametro.getTempoDeChegada()) {
            if(this.tempo < processoParametro.getTempo()) {
                return -1;
            } else if(this.tempo > processoParametro.getTempo()) {
                return 1;
            } else return 0; // são iguais no tempoDeChegada e no tempo.
        } else if(this.tempoDeChegada < processoParametro.getTempoDeChegada()) {
            return -1;
        } else return 1;
    }
}


package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import br.ufpb.dcx.aps.escalonador.comando.ComandoEscalonador;

public class EscalonadorBase {

	protected TipoEscalonador tipoEscalonador;
	protected int tick;
	protected int quantum, controlador, tempoRodando, tempoFixo;
	protected String rodando, processoFinalizado, processoBloqueado, processoRetomado;

	protected ArrayList<String> processosBloqueados;
	protected Queue<String> listaProcessos; // Queue add um elemento na lista atravez o modo FIFO
	protected List<String> fila = new ArrayList<String>();
	protected List<String> processosRetomados = new ArrayList<String>();
	protected List<Integer> filaDuracao = new ArrayList<Integer>();

//	public String execute(ComandoEscalonador comando) {
//		comando.setTipoEscalonador(this);
//		comando.executar();
//		return null;
//	}

	public void tick() {
		this.tick++;
	}

	public String getStatus() {
		return this.getStatus();
	}

	public void adicionarProcesso(String nomeProcesso) {

	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {

	}

	public void finalizarProcesso(String nomeProcesso) {

	}

	public void bloquearProcesso(String nomeProcesso) {

	}

	public void retomarProcesso(String nomeProcesso) {

	}

	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao) {

	}

	public TipoEscalonador getTipoEscalonador() {
		return tipoEscalonador;
	}

	public void setTipoEscalonador(TipoEscalonador tipoEscalonador) {
		this.tipoEscalonador = tipoEscalonador;
	}
//	public TipoEscalonador escalonadorRoundRobin() {
//		return TipoEscalonador.RoundRobin;
//	}
//	public TipoEscalonador escalonadorPrioridade() {
//		return TipoEscalonador.Prioridade;
//	}
//	public TipoEscalonador escalonadorMaisCurtoPrimeiro() {
//		return TipoEscalonador.MaisCurtoPrimeiro;
//	}	
}
package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FachadaEscalonador {

	private TipoEscalonador tipoEscalonador;
	private int tick;
	private int quantum;
	private int controlador;

	private String rodando;
	private String processoFinalizado;
	private String processoBloqueado;
	private String processoRetomado;

	private String rodandoPrioridade;
	private int prioridade;

	// Prioridade Lists
	private List<String> filaPrioridade = new ArrayList<String>();
	private List<String> listaProcessosPrioridade = new ArrayList<String>();

	// RoundRobin Lists
	private ArrayList<String> processosBloqueados;
	private Queue<String> listaProcessos;
	private List<String> fila = new ArrayList<String>();
	private List<String> processosRetomados = new ArrayList<String>();

	public FachadaEscalonador(TipoEscalonador tipoEscalonador) {

		if (tipoEscalonador == null)
			throw new EscalonadorException();
		this.quantum = 3;
		this.tick = 0;
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcessos = new LinkedList<String>();
		this.processosBloqueados = new ArrayList<String>();
	}

	public FachadaEscalonador(TipoEscalonador roundrobin, int quantum) {

		if (quantum <= 0)
			throw new EscalonadorException();
		this.quantum = quantum;
		this.tick = 0;
		this.tipoEscalonador = roundrobin;
		this.listaProcessos = new LinkedList<String>();
		this.processosBloqueados = new ArrayList<String>();
	}

	public FachadaEscalonador(TipoEscalonador Prioridade, int quantum, int prioridade) {

		if (quantum <= 0)
			throw new EscalonadorException();
		this.quantum = quantum;
		this.tick = 0;
		this.prioridade = prioridade;
		this.tipoEscalonador = Prioridade;
		this.listaProcessosPrioridade = new LinkedList<String>();
		this.processosBloqueados = new ArrayList<String>();
	}

	public String getStatus() {

////////////////////////////////////////////ROUNDROBIN/////////////////////////////////

		if (tipoEscalonador == TipoEscalonador.RoundRobin) {

			String resultado = "Escalonador " + this.tipoEscalonador + ";";
			resultado += "Processos: {";
			if (rodando != null)
				resultado += "Rodando: " + this.rodando;
			if (listaProcessos.size() > 0 || fila.size() > 0) {

				if (rodando != null)
					resultado += ", ";
				if (fila.size() > 0) {
					resultado += "Fila: " + this.fila.toString();
				} else {
					resultado += "Fila: " + this.listaProcessos.toString();
				}
			}
			if (this.processosBloqueados.size() > 0) {

				if (rodando != null || listaProcessos.size() > 0)
					resultado += ", ";
				resultado += "Bloqueados: " + this.processosBloqueados.toString();
			}

			resultado += "};Quantum: " + this.quantum + ";";
			resultado += "Tick: " + this.tick;
			return resultado;

//////////////////////////PRIORIDADE//////////////////////////////////////////////////

		} else if (tipoEscalonador == TipoEscalonador.Prioridade) {

			String resultado = "Escalonador " + this.tipoEscalonador + ";";
			resultado += "Processos: {";
			if (rodandoPrioridade != null)
				resultado += "Rodando: " + this.rodandoPrioridade;
			if (listaProcessosPrioridade.size() > 0 || filaPrioridade.size() > 0) {

				if (rodandoPrioridade != null)
					resultado += ", ";
				if (filaPrioridade.size() > 0) {
					System.err.println(filaPrioridade);
					resultado += "Fila: " + this.filaPrioridade.toString();
				} else {
					resultado += "Fila: " + this.listaProcessosPrioridade.toString();
				}
			}
			if (this.processosBloqueados.size() > 0) {

				if (rodandoPrioridade != null || listaProcessosPrioridade.size() > 0)
					resultado += ", ";
				resultado += "Bloqueados: " + this.processosBloqueados.toString();
			}

			resultado += "};Quantum: " + this.quantum + ";";
			resultado += "Tick: " + this.tick;
			System.out.println(resultado);
			return resultado;

		} else {
			return null;
		}
	}

	public void tick() {

		this.tick++;
		if (this.controlador > 0 && (this.controlador + this.quantum) == this.tick) {
			this.listaProcessos.add(rodando);
			this.rodando = this.listaProcessos.poll();
			this.controlador = this.tick;
		}
		if (processoFinalizado != null) {
			if (this.rodando == this.processoFinalizado) {
				this.rodando = null;
			} else {
				this.listaProcessos.remove(processoFinalizado);
			}
		}

		if (this.rodando == null) {
			if (tipoEscalonador == TipoEscalonador.RoundRobin) {
				if (this.listaProcessos.size() != 0) {
					this.rodando = this.listaProcessos.poll();
					if (listaProcessos.size() > 0)
						this.controlador = this.tick;
				}
				
				//PRIORIDADE ABAIXO
			} else if (tipoEscalonador == TipoEscalonador.Prioridade) {
				if (this.listaProcessosPrioridade.size() != 0) {
					this.rodandoPrioridade = this.listaProcessosPrioridade.get(0);
					if (listaProcessosPrioridade.size() > 0)
						this.controlador = this.tick;
				}
			}

		}
		if (this.controlador == 0 && this.rodando != null && listaProcessos.size() > 0)
			this.controlador = this.tick;
		if (processoBloqueado != null) {
			if (this.rodando == this.processoBloqueado) {
				this.rodando = null;
				this.processosBloqueados.add(processoBloqueado);
				if (listaProcessos.size() > 0) {
					rodando = listaProcessos.poll();
				} else {
					rodando = null;
				}
			}
			processoBloqueado = null;
		}
		if (processosRetomados.size() > 0) {
			for (int k = 0; k < processosRetomados.size(); k++) {
				String retomar = processosRetomados.get(k);
				if (processosBloqueados.contains(retomar)) {
					if (rodando == null) {
						rodando = retomar;
					} else {
						this.listaProcessos.add(retomar);
					}
					this.processosBloqueados.remove(retomar);
				}
			}
		}
	}

	public void adicionarProcesso(String nomeProcesso) {

		if (nomeProcesso == null) {
			throw new EscalonadorException();
		}
		if (listaProcessos.contains(nomeProcesso)) {
			throw new EscalonadorException();
		} else {
			this.listaProcessos.add(nomeProcesso);
		}
	}

	public void adicionarProcesso(String nomeProcesso, int prioridade) {

		if (tipoEscalonador == TipoEscalonador.RoundRobin)
			throw new EscalonadorException();
		if (tipoEscalonador == TipoEscalonador.MaisCurtoPrimeiro)
			throw new EscalonadorException();

		this.listaProcessosPrioridade.add(nomeProcesso);

	}

	public void finalizarProcesso(String nomeProcesso) {

		if (!listaProcessos.contains(nomeProcesso) && rodando == null) {
			throw new EscalonadorException();

		} else {
			this.processoFinalizado = nomeProcesso;
		}
	}

	public void bloquearProcesso(String nomeProcesso) {
		if (!listaProcessos.contains(nomeProcesso) && rodando == null) {
			throw new EscalonadorException();
		}
		if (rodando != nomeProcesso) {
			throw new EscalonadorException();
		} else {
			this.processoBloqueado = nomeProcesso;
		}

	}

	public void retomarProcesso(String nomeProcesso) {
		if (!processosBloqueados.contains(nomeProcesso)) {
			throw new EscalonadorException();
		} else {
			processosRetomados.add(nomeProcesso);
		}
	}

	public void adicionarProcessoTempoFixo(String string, int duracao) {

	}
}

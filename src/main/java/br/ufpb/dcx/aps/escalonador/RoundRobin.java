package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;

public class RoundRobin extends EscalonadorBase {

	public RoundRobin() {

		if (tipoEscalonador == null)
			throw new EscalonadorException();
		if (tipoEscalonador != TipoEscalonador.RoundRobin)
			throw new EscalonadorException();

		this.processosBloqueados = new ArrayList<>();
		this.processosRetomados = new ArrayList<>();
		this.fila = new ArrayList<>();
		this.quantum = 0;
		this.tipoEscalonador = TipoEscalonador.RoundRobin;
	}

	public RoundRobin(int quantum) {

		if (tipoEscalonador == null)
			throw new EscalonadorException();
		if (tipoEscalonador != TipoEscalonador.RoundRobin)
			throw new EscalonadorException();
		if (quantum <= 0)
			throw new EscalonadorException();

		this.processosBloqueados = new ArrayList<>();
		this.processosRetomados = new ArrayList<>();
		this.fila = new ArrayList<>();
		this.quantum = quantum;
		this.tipoEscalonador = TipoEscalonador.RoundRobin;
	}

	public String getStatus() {

		return null;
	}
	public void tick() {

		this.tick++;

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
}
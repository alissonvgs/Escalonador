package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;

public class Fifo extends EscalonadorBase {

	public Fifo() {

		if (tipoEscalonador == null)
			throw new EscalonadorException();
		if (tipoEscalonador != TipoEscalonador.Fifo)
			throw new EscalonadorException();

		this.processosBloqueados = new ArrayList<String>();
		this.processosRetomados = new ArrayList<String>();
		this.fila = new ArrayList<>();
		this.quantum = 0;
		this.tipoEscalonador = TipoEscalonador.Fifo;
	}
	
	public Fifo(int quantum) {

		if (tipoEscalonador == null)
			throw new EscalonadorException();
		if (tipoEscalonador != TipoEscalonador.Fifo)
			throw new EscalonadorException();
		if (quantum <= 0)
			throw new EscalonadorException();

		this.processosBloqueados = new ArrayList<>();
		this.processosRetomados = new ArrayList<>();
		this.fila = new ArrayList<>();
		this.quantum = quantum;
		this.tipoEscalonador = TipoEscalonador.Fifo;
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
package br.ufpb.dcx.aps.escalonador;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class RoundRobinLogica implements EscalonadorFuncoes {

	protected TipoEscalonador tipoEscalonador;
	private int tick;
	protected int quantum, controlador, tempoRodando, tempoFixo;
	protected String rodando, processoFinalizado, processoBloqueado, processoRetomado;

	protected ArrayList<String> processosBloqueados;
	protected Queue<String> listaProcessos; // Queue add um elemento na lista atravez o modo FIFO
	protected List<String> fila = new ArrayList<String>();
	protected List<String> processosRetomados = new ArrayList<String>();
	protected List<Integer> filaDuracao = new ArrayList<Integer>();

	@Override
	public String getStatus() {
		System.err.println("getstauts >");

		String resultado = "Escalonador " + this.tipoEscalonador + ";";
		resultado += "Processos: {";

		if (rodando != null)
			resultado += "Rodando: " + this.rodando;

		if (listaProcessos.size() > 0 || fila.size() > 0) {

			if (rodando != null)
				resultado += ", ";
		}
		if (this.processosBloqueados.size() > 0) {

			if (rodando != null || listaProcessos.size() > 0)
				resultado += ", ";
			resultado += "Bloqueados: " + this.processosBloqueados.toString();
		}

		resultado += "Tick: " + this.tick;
		System.out.println(resultado);
		return resultado;
	}

	@Override
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
			if (this.listaProcessos.size() != 0) {
				this.rodando = this.listaProcessos.poll();
				if (listaProcessos.size() > 0)
					this.controlador = this.tick;
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
		if (fila.size() > 0) {
			if (rodando == null) {
				rodando = fila.remove(0);
				tempoRodando = filaDuracao.remove(0);
				tempoFixo = this.tick + tempoRodando;
			}
		}
	}

	@Override
	public void adicionarProcesso(String nomeProcesso) {
		if (nomeProcesso == null)
			throw new EscalonadorException();

		if (tipoEscalonador == TipoEscalonador.Prioridade)
			throw new EscalonadorException();

		if (tipoEscalonador == TipoEscalonador.MaisCurtoPrimeiro)
			throw new EscalonadorException();

		if (listaProcessos.contains(nomeProcesso)) {
			throw new EscalonadorException();

		} else {

			this.listaProcessos.add(nomeProcesso);
		}

	}

	@Override
	public void finalizarProcesso(String nomeProcesso) {
		if (!listaProcessos.contains(nomeProcesso) && rodando == null) {
			throw new EscalonadorException();

		} else {

			this.processoFinalizado = nomeProcesso;
		}

	}

	@Override
	public void bloquearProcesso(String nomeProcesso) {
		if (!listaProcessos.contains(nomeProcesso) && rodando == null)

			throw new EscalonadorException();

		if (rodando != nomeProcesso) {

			throw new EscalonadorException();

		} else {
			this.processoBloqueado = nomeProcesso;
		}

	}

	@Override
	public void retomarProcesso(String nomeProcesso) {
		if (!processosBloqueados.contains(nomeProcesso)) {
			throw new EscalonadorException();
		} else {
			processosRetomados.add(nomeProcesso);
			System.err.println("Processo retomado:" + nomeProcesso);
		}

	}

	@Override
	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao) {
		if (this.fila.contains(nomeProcesso) || nomeProcesso == null)
			throw new EscalonadorException();

		if (duracao < 1)
			throw new EscalonadorException();

		int maisCurto = Integer.MAX_VALUE;

		if (this.fila.size() == 0) {

			this.fila.add(nomeProcesso);
			this.filaDuracao.add(duracao);

		} else {

			int menorPosicao = 0;

			this.fila.add(nomeProcesso);
			this.filaDuracao.add(duracao);

			for (int i = 0; i < this.filaDuracao.size(); i++) {

				if (this.filaDuracao.get(i) < maisCurto) {
					maisCurto = this.filaDuracao.get(i);
					menorPosicao = i;
				}
			}
			if (menorPosicao > 0) {

				String processoMenor = this.fila.remove(menorPosicao);
				int processoMenorTempo = this.filaDuracao.remove(menorPosicao);
				this.fila.add(0, processoMenor);
				this.filaDuracao.add(0, processoMenorTempo);

				for (int k = 0; duracao < k; k++) {
					this.fila.add(0, nomeProcesso);
					this.filaDuracao.add(0, duracao);

				}
			}
		}

	}

	@Override
	public TipoEscalonador getTipoEscalonador() {
		return tipoEscalonador;
	}

	@Override
	public void setTipoEscalonador(TipoEscalonador tipoEscalonador) {
		this.tipoEscalonador = tipoEscalonador;

	}

	@Override
	public TipoEscalonador escalonadorRoundRobin() {
		return TipoEscalonador.RoundRobin;
	}

	@Override
	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		
	}

	@Override
	public TipoEscalonador escalonadorPrioridade() {
		return null;
	}

	@Override
	public TipoEscalonador escalonadorMaisCurtoPrimeiro() {
		return null;
	}

}

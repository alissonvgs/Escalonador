package br.ufpb.dcx.aps.escalonador;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FachadaEscalonador {
	
	protected TipoEscalonador tipoEscalonador;
	private int tick;
	protected int quantum;
	protected int controlador;
	
	protected String rodando;
	protected String processoFinalizado;
	protected String processoBloqueado;
	protected String processoRetomado;
	
	protected ArrayList<String> processosBloqueados;
	protected Queue<String> listaProcessos; // Queue add um elemento na lista atravez o modo FIFO
	protected List<String> fila = new ArrayList<String>();
	protected List<String> processosRetomados = new ArrayList<String>();

	public FachadaEscalonador( TipoEscalonador tipoEscalonador ){
		
		if( tipoEscalonador == null ) throw new EscalonadorException();	
		this.quantum = 3;
		this.tick = 0;
		this.tipoEscalonador = tipoEscalonador;
		this.listaProcessos = new LinkedList<String>();
		this.processosBloqueados = new ArrayList<String>();
	}
	public FachadaEscalonador( TipoEscalonador roundrobin, int quantum ) {
		
		if(quantum <= 0) throw new EscalonadorException();
		this.quantum = quantum;
		this.tick = 0;
		this.tipoEscalonador = roundrobin;
		this.listaProcessos = new LinkedList<String>();
		this.processosBloqueados = new ArrayList<String>();
	}	
	public String getStatus() {

		System.err.println("getstauts >");

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
			
			if (rodando != null || listaProcessos.size() > 0) resultado += ", ";
			resultado += "Bloqueados: " + this.processosBloqueados.toString();
		}
		
		resultado += "};Quantum: " + this.quantum + ";";
		resultado += "Tick: " + this.tick;
		System.out.println(resultado);
		return resultado;
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
			if (this.listaProcessos.size() != 0) {
				this.rodando = this.listaProcessos.poll();
				if (listaProcessos.size() > 0) this.controlador = this.tick;			
			}
		}
		if (this.controlador == 0 && this.rodando != null && listaProcessos.size() > 0) this.controlador = this.tick;
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
	
	public void adicionarProcesso( String nomeProcesso ) {
			
		if(nomeProcesso == null) {
			throw new EscalonadorException();
		}
		if (listaProcessos.contains(nomeProcesso)) {
			throw new EscalonadorException();
		}else{
			this.listaProcessos.add(nomeProcesso);
		}
	}
	
	public void adicionarProcesso(String nomeProcesso, int prioridade) {
		
		if (tipoEscalonador == TipoEscalonador.RoundRobin) throw new EscalonadorException();	
	}
	public void finalizarProcesso(String nomeProcesso) {
		
		if(!listaProcessos.contains(nomeProcesso) && rodando == null) {
			throw new EscalonadorException();
			
		}else{
			this.processoFinalizado = nomeProcesso;
		}
	}
	public void bloquearProcesso(String nomeProcesso) {
		if(!listaProcessos.contains(nomeProcesso) && rodando == null) {
			throw new EscalonadorException();
		}
		if(rodando != nomeProcesso) {
			throw new EscalonadorException();
		}else {
			this.processoBloqueado = nomeProcesso;
		}
		
	}
	public void retomarProcesso(String nomeProcesso) {
		if(!processosBloqueados.contains(nomeProcesso)) {
			throw new EscalonadorException();
		}else{
			processosRetomados.add(nomeProcesso);
		}
	}
}
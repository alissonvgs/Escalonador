package br.ufpb.dcx.aps.escalonador;

public interface EscalonadorFuncoes {
	public String getStatus();
	public void tick();
	public void adicionarProcesso(String nomeProcesso);
	public void adicionarProcesso(String nomeProcesso, int prioridade);
	public void finalizarProcesso(String nomeProcesso);
	public void bloquearProcesso(String nomeProcesso);
	public void retomarProcesso(String nomeProcesso);
	public void adicionarProcessoTempoFixo(String nomeProcesso, int duracao);
	public TipoEscalonador getTipoEscalonador();
	public void setTipoEscalonador(TipoEscalonador tipoEscalonador);
	public TipoEscalonador escalonadorRoundRobin();
	public TipoEscalonador escalonadorPrioridade();
	public TipoEscalonador escalonadorMaisCurtoPrimeiro();
}

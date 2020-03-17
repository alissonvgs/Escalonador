package br.ufpb.dcx.aps.escalonador.comando;

import br.ufpb.dcx.aps.escalonador.EscalonadorBase;

public interface ComandoEscalonador {
	
	public String executar();

	public void setTipoEscalonador(EscalonadorBase escalonadorBase);

}

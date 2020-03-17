package br.ufpb.dcx.aps.escalonador.comando;

import br.ufpb.dcx.aps.escalonador.EscalonadorBase;
import br.ufpb.dcx.aps.escalonador.FachadaEscalonador;

public class ComandoGetStatus implements ComandoEscalonador {
	
	private FachadaEscalonador escalonador;

	public ComandoGetStatus(FachadaEscalonador escalonador) {
		this.escalonador = escalonador;
	}

	@Override
	public String executar() {
		return null;
	}

	@Override
	public void setTipoEscalonador(EscalonadorBase escalonadorBase) {
		// TODO Auto-generated method stub
		
	}

}

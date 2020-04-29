package br.com.caelum.leilao.dominio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Leilao {

	private String descricao;
	private Calendar data;
	private List<Lance> lances;
	private boolean encerrado;
	private int id;

	public Leilao(String descricao) {
		this(descricao, Calendar.getInstance());
	}

	public Leilao(String descricao, Calendar data) {
		this.descricao = descricao;
		this.data = data;
		this.lances = new ArrayList<Lance>();
	}

	private Lance ultimoLanceDado() {
		return lances.get(lances.size() - 1);
	}

	public String descricao() {
		return descricao;
	}

	public List<Lance> lances() {
		return Collections.unmodifiableList(lances);
	}

	public Calendar data() {
		return (Calendar) data.clone();
	}

	public void encerra() {
		this.encerrado = true;
	}
	
	public boolean isEncerrado() {
		return encerrado;
	}

	public int id() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void propoe(Lance lance) {
		if (lances.isEmpty() || podeDarUmLance(lance.usuario())) {
			lances.add(lance);
		}
	}

	private boolean podeDarUmLance(Usuario usuario) {
		return !ultimoLanceDado().usuario().equals(usuario) 
			&& quantidadeDeLancesDo(usuario) < 5;
	}

	private int quantidadeDeLancesDo(Usuario usuario) {
		int totalLancesPorUsuario = 0;

		for (Lance lancePorUsuario : lances) {
			if (lancePorUsuario.usuario().equals(usuario))
				totalLancesPorUsuario++;
		}
		return totalLancesPorUsuario;
	}

	public void dobraLance(Usuario usuario) {
		Lance ultimoLance = ultimoLanceDo(usuario);
		if (ultimoLance != null) {
			propoe(new Lance(usuario, ultimoLance.valor() * 2));
		}
	}

	private Lance ultimoLanceDo(Usuario usuario) {
		Lance ultimoLance = null;
		for (Lance lance : lances) {
			if (lance.usuario().equals(usuario))
				ultimoLance = lance;
		}

		return ultimoLance;
	}
}

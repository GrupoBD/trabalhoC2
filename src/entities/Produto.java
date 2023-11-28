package entities;

import entities.enums.TipoProduto;

public class Produto {

	private String nome;
	private TipoProduto tipo;
	private int idFornecedor;
	private int idProduto;

	public Produto() {
		
	}
	public Produto(int idProduto) {
		this.idProduto=idProduto;
	}
	
	public Produto(String nome, TipoProduto tipo, int idFornecedor, int idProduto) {
		this.nome = nome;
		this.tipo = tipo;
		this.idFornecedor = idFornecedor;
		this.idProduto = idProduto;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoProduto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProduto tipo) {
		this.tipo = tipo;
	}

	public int getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(int idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	@Override
	public String toString() {
		return "Nome= " + nome + ", tipo=" + tipo + ", idFornecedor=" + idFornecedor+ ", idProduto= "+idProduto;
	}

}
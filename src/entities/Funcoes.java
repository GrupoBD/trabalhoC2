package entities;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import entities.enums.TipoProduto;
import sql.DatabaseConnection;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Funcoes {
	
    private MongoDatabase database;
    private MongoCollection<Document> fornecedorCollection;
    private MongoCollection<Document> produtoCollection;
	private List<Produto> produtos;
	private List<Fornecedor> fornecedores;
	private Connection conexao;

	public Funcoes() {
		produtos = new ArrayList<>();
		fornecedores = new ArrayList<>();
		database = DatabaseConnection.getConnection();
		fornecedorCollection = database.getCollection("fornecedor");
		produtoCollection = database.getCollection("produto");

	}

	public int idFornecedorEncontrado(String razaoSocial) {
        MongoCursor<Document> cursor = fornecedorCollection.find(Filters.eq("razaoSocial", razaoSocial)).iterator();

        if (cursor.hasNext()) {
            Document document = cursor.next();
            return document.getInteger("id_fornecedor");
        } else {
            return 0;
        }
    }
	

	public Boolean buscaFornecedorNome(String nome) {
        MongoCursor<Document> cursor = fornecedorCollection.find(Filters.eq("razaoSocial", nome)).iterator();

        return cursor.hasNext();
	}

	public void adicionarFornecedor(String razaoSocial, int cnpj) {
        Document fornecedorDoc = new Document("razaoSocial", razaoSocial)
        		.append("id_fornecedor", obterMaiorIdFornecedor()+1)
                .append("CNPJ", cnpj);

        fornecedorCollection.insertOne(fornecedorDoc);

        System.out.println("Fornecedor adicionado com sucesso!");

	}

	public void mostrarFornecedores() {
        MongoCursor<Document> cursor = fornecedorCollection.find().iterator();

        System.out.println("Fornecedores: ");

        while (cursor.hasNext()) {
            Document document = cursor.next();
            int idFornecedor = document.getInteger("id_fornecedor");
            String razaoSocial = document.getString("razaoSocial");
            int cnpj = document.getInteger("CNPJ");

            System.out.println("ID: " + idFornecedor + ",Razão Social: " + razaoSocial + ", CNPJ: " + cnpj);
        }
	}

	public void mostrarProdutosEFornecedores() {
        // Mostrar Fornecedores
        MongoCursor<Document> fornecedorCursor = fornecedorCollection.find().iterator();

        System.out.println("Fornecedores: ");
        while (fornecedorCursor.hasNext()) {
            Document fornecedorDoc = fornecedorCursor.next();
            int idFornecedor = fornecedorDoc.getInteger("id_fornecedor");
            String razaoSocial = fornecedorDoc.getString("razaoSocial");
            int cnpj = fornecedorDoc.getInteger("CNPJ");
            System.out.println("ID: " + idFornecedor + ", Razão Social: " + razaoSocial + ", CNPJ: " + cnpj);
        }

        // Mostrar Produtos
        MongoCursor<Document> produtoCursor = produtoCollection.find().iterator();

        System.out.println("Produtos: ");
        while (produtoCursor.hasNext()) {
            Document produtoDoc = produtoCursor.next();
            int idProduto = produtoDoc.getInteger("id_produto");
            String nomeProduto = produtoDoc.getString("nome_produto");
            int idFornecedor = produtoDoc.getInteger("id_fornecedor");
            String tipoProduto = produtoDoc.getString("tipo_produto");

            System.out.println("ID: " + idProduto + ", Nome: " + nomeProduto +
                    ", ID Fornecedor: " + idFornecedor + ", Tipo: " + tipoProduto);
        }

	}
    public int obterMaiorIdProduto() {
        AggregateIterable<Document> aggregation = produtoCollection.aggregate(
                Arrays.asList(
                        new Document("$group", new Document("_id", null).append("maxId", new Document("$max", "$id_produto")))
                )
        );

        Document result = aggregation.first();
            int maxId = result.getInteger("maxId", 0);
            return maxId;

    }
    public int obterMaiorIdFornecedor() {
        AggregateIterable<Document> aggregation = fornecedorCollection.aggregate(
                Arrays.asList(
                        new Document("$group", new Document("_id", null).append("maxId", new Document("$max", "$id_fornecedor")))
                )
        );

        Document result = aggregation.first();
            int maxId = result.getInteger("maxId", 0);
            return maxId;

    }
	

	public void adicionarProduto(String nome, TipoProduto tipo, int id_fornecedor) {
        Document produtoDoc = new Document("id_fornecedor", id_fornecedor)
        		.append("id_produto", obterMaiorIdProduto()+1)                
        		.append("nome_produto", nome)
                .append("tipo_produto", tipo.toString());

        produtoCollection.insertOne(produtoDoc);

        System.out.println("Produto adicionado com sucesso!");
    }

	public void mostrarProdutos() {
        MongoCursor<Document> cursor = produtoCollection.find().iterator();

        System.out.println("Produtos: ");
        while (cursor.hasNext()) {
            Document document = cursor.next();
            int idProduto = document.getInteger("id_produto");
            String nome = document.getString("nome_produto");
            String tipo = document.getString("tipo_produto");
            int idFornecedor = document.getInteger("id_fornecedor");

            System.out.println("ID: " + idProduto + ", Nome: " + nome +
                    ", Tipo: " + tipo + ", ID Fornecedor: " + idFornecedor);
        }
	}

	public boolean deletarFornecedor(int id) {
		 long productCount = produtoCollection.countDocuments(Filters.eq("id_fornecedor", id));

	        if (productCount > 0) {
	            System.out.println("Não é possível remover o fornecedor, pois ele possui produtos cadastrados.");
	            return false;
	        }
	        long deletedCount = fornecedorCollection.deleteOne(Filters.eq("id_fornecedor", id)).getDeletedCount();

	        if (deletedCount > 0) {
	            System.out.println("Fornecedor removido com sucesso!");
	            return true;
	        } else {
	            System.out.println("Fornecedor não encontrado.");
	            return false;
	        }
	}

	public boolean deletarProduto(int idProduto) {
        long deletedCount = produtoCollection.deleteOne(Filters.eq("id_produto", idProduto)).getDeletedCount();

        if (deletedCount > 0) {
            System.out.println("Produto removido com sucesso!");
            return true;
        } else {
            System.out.println("Produto não encontrado.");
            return false;
        }

	}

	public Produto buscarProduto(String nome) {
        Document produtoDoc = produtoCollection.find(Filters.eq("nome_produto", nome)).first();

        if (produtoDoc != null) {
            int idProduto = produtoDoc.getInteger("id_produto");
            String nomeProduto = produtoDoc.getString("nome_produto");
            String tipoProduto = produtoDoc.getString("tipo_produto");
            int idFornecedor = produtoDoc.getInteger("id_fornecedor");

            return new Produto(nomeProduto, TipoProduto.valueOf(tipoProduto), idFornecedor, idProduto);
        } else {
            return null;
        }
	}

	public Produto buscarProdutoId(int id) {
        Document produtoDoc = produtoCollection.find(Filters.eq("id_produto", id)).first();

        if (produtoDoc != null) {
            String nomeProduto = produtoDoc.getString("nome_produto");
            String tipoProduto = produtoDoc.getString("tipo_produto");
            int idFornecedor = produtoDoc.getInteger("id_fornecedor");

            return new Produto(nomeProduto, TipoProduto.valueOf(tipoProduto), idFornecedor, id);
        } else {
            return null;
        }
	}

	public Fornecedor buscarFornecedor(String razaoSocial) {
        Document fornecedorDoc = fornecedorCollection.find(Filters.eq("razaoSocial", razaoSocial)).first();

        if (fornecedorDoc != null) {
            int idFornecedor = fornecedorDoc.getInteger("id_fornecedor");
            String nomeFornecedor = fornecedorDoc.getString("razaoSocial");
            int cnpj = fornecedorDoc.getInteger("cnpj");

            return new Fornecedor(nomeFornecedor, cnpj);
        } else {
            return null;
        }
	}

	public void editarProduto(int idProduto, String nomeNovo, TipoProduto novoTipo, int id_forn) {
        produtoCollection.updateOne(
                Filters.eq("id_produto", idProduto),
                Updates.combine(
                        Updates.set("id_fornecedor", id_forn),
                        Updates.set("nome_produto", nomeNovo),
                        Updates.set("tipo_produto", novoTipo.toString())
                )
        );

        System.out.println("Produto editado com sucesso!");
	}

	public void mostrarProdutosPorTipo() {
        MongoCursor<Document> cursor = produtoCollection.find().sort(Sorts.ascending("tipo_produto")).iterator();

        Map<TipoProduto, List<Produto>> produtosPorTipo = new HashMap<>();

        while (cursor.hasNext()) {
            Document produtoDoc = cursor.next();

            int idProduto = produtoDoc.getInteger("id_produto");
            String nome = produtoDoc.getString("nome_produto");
            TipoProduto tipo = TipoProduto.valueOf(produtoDoc.getString("tipo_produto"));
            int idFornecedor = produtoDoc.getInteger("id_fornecedor");

            Produto produto = new Produto(nome, tipo, idFornecedor, idProduto);

            produtosPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(produto);
        }

        System.out.println("Produtos por Tipo:");
        for (TipoProduto tipo : produtosPorTipo.keySet()) {
            List<Produto> produtosDoTipo = produtosPorTipo.get(tipo);

            System.out.println(tipo + ":");
            for (Produto produto : produtosDoTipo) {
                System.out.println("- " + produto);
            }
        }
    }

	public void fornecedoresPorProduto() {
        AggregateIterable<Document> result = produtoCollection.aggregate(Arrays.asList(
                new Document("$lookup",
                        new Document("from", "fornecedor")
                                .append("localField", "id_fornecedor")
                                .append("foreignField", "id_fornecedor")
                                .append("as", "fornecedor")),
                new Document("$unwind", "$fornecedor"),
                new Document("$project",
                        new Document("razaoSocial", "$fornecedor.razaoSocial")
                                .append("nome_produto", 1)),
                new Document("$sort", new Document("razaoSocial", 1))
        ));
        String fornecedorAtual = null;
        for (Document document : result) {
            String razaoSocial = document.getString("razaoSocial");
            String nomeProduto = document.getString("nome_produto");

            if (!razaoSocial.equals(fornecedorAtual)) {
                System.out.println(razaoSocial + ":");
                fornecedorAtual = razaoSocial;
            }

            System.out.println("- " + nomeProduto);
        }
	}

	public String buscarFornecedorProduto(int id) {
        AggregateIterable<Document> result = produtoCollection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_produto", id)),
                new Document("$lookup",
                        new Document("from", "fornecedor")
                                .append("localField", "id_fornecedor")
                                .append("foreignField", "id_fornecedor")
                                .append("as", "fornecedor")),
                new Document("$unwind", "$fornecedor"),
                new Document("$project", new Document("razaoSocial", "$fornecedor.razaoSocial"))
        ));

        for (Document document : result) {
            return document.getString("razaoSocial");
        }

        return "Produto não encontrado";
	}

	public void editarFornecedor(int id_fornecedor, String razaoSocialNova, int cnpj) {
        fornecedorCollection.updateOne(
        		Filters.eq("id_fornecedor", id_fornecedor),
        		Updates.combine(
        				Updates.set("razaoSocial", razaoSocialNova),
        				Updates.set("CNPJ", cnpj)
        		)
        );
        System.out.println("Fornecedor editado com sucesso!");		
	}

	public Boolean IDbuscaProduto(int id) {
	    Document filter = new Document("id_produto", id);
	    long count = produtoCollection.countDocuments(filter);
	    return count > 0;
	}
	
	public Boolean IDbuscaFornecedor(int id) {
	    Document filter = new Document("id_fornecedor", id);

	    long count = fornecedorCollection.countDocuments(filter);

	    return count > 0;
	}
	
	public boolean listaVazia() {
			if(fornecedores.isEmpty()) {
				return true;
		}
			return false;
	}
	
	
	public int countFornecedor(){
	    long count = fornecedorCollection.countDocuments();
	    return Math.toIntExact(count);
	}
	
	public int countProduto(){
	    long count = produtoCollection.countDocuments();
	    return Math.toIntExact(count);
}
	public void interfaceCount() {
		int forn = countFornecedor();
		int prod = countProduto();
		
		for (int i = 0; i < 100; ++i)  
		       System.out.println(); 
		
		System.out.println("###########################################");
		System.out.println("###########################################");
		System.out.println("##                                       ##");
		System.out.println("##                MERCADO                ##");
		System.out.println("##                                       ##");
		System.out.println("##    TOTAL DE REGISTROS:                ##");
		System.out.println("##    FORNECEDORES: "+ forn +"                    ##");
		System.out.println("##    PRODUTOS: "+ prod +"                        ##");
		System.out.println("##                                       ##");
		System.out.println("##    Criado por: João Pedro             ##");
		System.out.println("##                Pedro Barros           ##");
		System.out.println("##                Bruno Carvalho         ##");
		System.out.println("##                Gabriel Silva          ##");
		System.out.println("##                Tharles Cassiano       ##");
		System.out.println("##                                       ##");
		System.out.println("##    Disciplina: Banco de Dados         ##");
		System.out.println("##                2023/2                 ##");
		System.out.println("##    Professor:  Howard Roatti          ##");
		System.out.println("##                                       ##");
		System.out.println("###########################################");
		System.out.println("###########################################");
		sleep();
		  for (int i = 0; i < 100; ++i)  
		       System.out.println(); 

	}
	
	public void sleep() {
		try { Thread.sleep (3000); } catch (InterruptedException ex) {}
	}
}


	

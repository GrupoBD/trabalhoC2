package application;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import entities.Funcoes;
import entities.Produto;
import entities.enums.TipoProduto;

public class Program {

	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);
		Funcoes funcao = new Funcoes();
		int x;
		do {
			
			funcao.sleep();
			funcao.interfaceCount();
			System.out.println("===================== Bem-vindo ao Vivas Market =============================");
			System.out.println("Selecione o que deseja fazer: ");
			System.out.println(
					"1- Realizar cadastro \n2- Realizar atualização \n3- Realizar exclusão \n4- Gerar relatório \n5- Sair");
			x = sc.nextInt();
			sc.nextLine();
			char numero = String.valueOf(x).charAt(0);
			switch (numero) {
			case '1':
				System.out.println("Você selecionou 1, selecione o que deseja cadastrar:");
				System.out.println("1- Cadastrar produto \n2- Cadastrar fornecedor \n0 - Voltar");
				int y = sc.nextInt();
				sc.nextLine();
				switch (y) {

				case 1:
					System.out.print("Você selecionou produto, digite o nome do produto: ");
					String nome = sc.nextLine();
					while (true) {
						try {
							System.out.print("Digite o tipo: ");
							TipoProduto tipo = TipoProduto.valueOf(sc.next().toUpperCase());
							sc.nextLine();
							try {
								funcao.mostrarFornecedores();
								System.out.print("Informe o id do fornecedor: ");
								int id = sc.nextInt();

								if (funcao.IDbuscaFornecedor(id) == true) {
									funcao.adicionarProduto(nome, tipo, id);
									break;
								} else {
									System.out.println("Produto não adicionado, não existe esse fornecedor.");
									sc.nextLine();
									break;
								}
							} catch (InputMismatchException e) {
								System.out.println("Erro: O ID do produto deve ser um número inteiro.");
								sc.nextLine();
								break;
							}
						} catch (IllegalArgumentException e) {
							System.out.println(
									"Erro: Tipo de produto inválido. Certifique-se de digitar um tipo válido.");
							break;
						}
					}
					break;

				case 2:
					System.out.print("Você selecionou fornecedor");
					System.out.println();
					System.out.print("Digite a razão social: ");
					String razaoSocial = sc.nextLine();

					while (true) {
						try {
							System.out.print("Digite o CNPJ: ");
							int cnpj = sc.nextInt();
							funcao.adicionarFornecedor(razaoSocial, cnpj);
							break;
						} catch (InputMismatchException e) {
							System.out.println("Erro, o CNPJ precisa ser de números inteiros.");
							sc.nextLine();
							break;
						}
					}
					break;
				case 0:
					System.out.println("Pressione Enter");
					break;
					
				default:
					System.out.println("Opção inválida");
					break;

				}
				break;
			case '2':
				System.out.println("Você selecionou 2, selecione o que deseja atualizar:");
				System.out.println("1- Atualizar produto \n2- Atualizar fornecedor \n0 - Voltar");
				y = sc.nextInt();
				switch (y) {
				case 1:

					System.out.println("Você selecionou 1, digite qual produto deseja atualizar");
					funcao.mostrarProdutos();
					sc.nextLine();
					System.out.println("Id do produto que quer editar: ");
					int idBusca = sc.nextInt();
					if (funcao.IDbuscaProduto(idBusca)) {
						sc.nextLine();
						System.out.print("Novo nome: ");
						String nomeNovo = sc.nextLine();
						funcao.mostrarFornecedores();
						System.out.print("ID fornecedor: ");
						int id_forn = sc.nextInt();
						while (true) {
							try {
								System.out.print("Novo tipo: ");
								TipoProduto tipo = TipoProduto.valueOf(sc.next().toUpperCase());
								funcao.editarProduto(idBusca, nomeNovo, tipo,id_forn);
								System.out.println("Produto alterado com sucesso!");
								break;
							} catch (IllegalArgumentException e) {
								System.out.println("Tipo não encontrado, tente novamente.");
							}
						}
					} else {
						System.out.println("Produto não encontrado, aperte enter para continuar");
						break;
					}
					break;
				case 2:
					System.out.println("Você selecionou 2, digite qual fornecedor deseja atualizar");
					funcao.mostrarFornecedores();
					sc.nextLine();
					System.out.print("Fornecedor que quer editar: ");
					int id_fornecedor = sc.nextInt();
					if (funcao.IDbuscaFornecedor(id_fornecedor)) {
						sc.nextLine();
						System.out.print("Nova razao social: ");
						String razaoSocialNova = sc.nextLine();
						while(true) {
						try {
						System.out.print("CNPJ: ");
						int cnpj = sc.nextInt();
						funcao.editarFornecedor(id_fornecedor, razaoSocialNova, cnpj);
						System.out.println("Fornecedor alterado com sucesso!");
						break;
						}
						catch(InputMismatchException e) {
							System.out.println("Digite um CPNJ válido.");
							sc.nextLine();
						}
						}} else {
						System.out.println("Fornecedor não encontrado, aperte enter para continuar.");
						break;
					}
					break;
				case 0:
					break;
				default:
					System.out.println("Opção inválida");
					break;
				}
				break;
			case '3':
				System.out.println("Você selecionou 3, selecione o que deseja excluir: ");
				System.out.println("1- Excluir produto \n2- Excluir fornecedor \n0 - Voltar");
				y = sc.nextInt();
				switch (y) {
				case 1:
					System.out.println("Você selecionou 1, escolha qual produto deseja excluir: ");
					funcao.mostrarProdutos();
					while (true) {
						try {
							System.out.println("id do Produto: ");
							int idProduto = sc.nextInt();
							funcao.deletarProduto(idProduto);
							break;
						}

						catch (InputMismatchException e) {
							System.out.println("Erro, digite um id valido.");
							sc.nextLine();
							
						}
					}
					break;
				case 2:
					System.out.println("Você selecionou 2, escolha qual fornecedor deseja excluir: ");
					funcao.mostrarFornecedores();
					while (true) {
						try {
						System.out.println("Digite o id");
						int idFornecedor = sc.nextInt();
						funcao.deletarFornecedor(idFornecedor);
						break;
						}
						catch(InputMismatchException e) {
						System.out.println("Digite um id valido.");
						sc.nextLine();
						}
					}
					break;
				case 0:
					break;
				default:
					System.out.println("Opção inválida");
					break;
				}
				break;
			case '4':
				System.out.println("Você selecionou 4, selecione o que deseja gerar: ");
				System.out.println(
						"1- Relatório de fornecedor por produto \n2- Buscar fornecedor de um produto \n3- Relatório de produto por tipo"
								+ "\n4- Relatório de fornecedores e produtos \n0 - Voltar");
				y = sc.nextInt();
				switch (y) {
				case 1:
					funcao.fornecedoresPorProduto();
					break;
				case 2:
					funcao.mostrarProdutos();			
					System.out.println("Digite o id do produto");
					int id = sc.nextInt();
					System.out.println(funcao.buscarFornecedorProduto(id));
					break;
				case 3:
					funcao.mostrarProdutosPorTipo();
					break;

				case 4:
					funcao.mostrarProdutosEFornecedores();
					break;
				case 0:
					System.out.println("Pressione Enter");
					break;
				default:
					System.out.println("Opção inválida");
					break;
				}
			

				funcao.sleep();
				break;
				
			case '5':
				System.out.println("Você selecionou sair. \nObrigado e volte sempre!");
				break;

			default:
				System.out.println("Opção inválida");
				break;
			}
			sc.nextLine();
			System.out.println(
					"=============================================================================================");
		} while (x != 5);
		sc.close();
	}
}
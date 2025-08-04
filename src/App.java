// App.java
import java.sql.*;
import java.util.Scanner;


public class App {
    static final String URL = "jdbc:mysql://localhost:3306/java_teste";
    static final String USER = "GabrielBarreto";
    static final String PASS = "Rafa2020##";

    /**
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bem-vindo ao sistema de cadastro de pessoas!");
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Cadastrar pessoa");
            System.out.println("2 - Listar pessoas");
            System.out.println("3 - Editar pessoa");
            System.out.println("4 - Excluir pessoa");
            System.out.println("5 - Reogarnizar IDs");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine(); // limpa buffer

            switch (opcao) {
                case 1 -> cadastrarPessoa(sc);
                case 2 -> listarPessoas();
                case 3 -> editarPessoa(sc);
                case 4 -> excluirPessoa(sc);
                case 5 -> reorganizarIDs();
                case 0 -> {
                    System.out.println("Saindo do sistema...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }
    static void reorganizarIDs() {
        try (Connection conn = conectar()) {
            Statement stmt = conn.createStatement();

            // Etapa 1: Cria variável de controle
            stmt.execute("SET @novo_id = 0");

            // Etapa 2: Atualiza os IDs sequencialmente
            stmt.execute("UPDATE Pessoa SET id = (@novo_id := @novo_id + 1) ORDER BY id");

            // Etapa 3: Atualiza o AUTO_INCREMENT
            stmt.execute("SET @ultimo_id = (SELECT MAX(id) FROM Pessoa)");
            stmt.execute("ALTER TABLE Pessoa AUTO_INCREMENT = @ultimo_id + 1");

            System.out.println("IDs reorganizados com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao reorganizar IDs: " + e.getMessage());
        }
    }

    static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    static void cadastrarPessoa(Scanner sc) {
    System.out.print("Digite o nome: ");
    String nome = sc.nextLine();
    System.out.print("Digite o CPF: ");
    String cpf = sc.nextLine();
    System.out.print("Digite a data de nascimento (dd/mm/aaaa): ");
    String datanascimento = sc.nextLine();
    System.out.print("Digite o telefone: ");
    String telefone = sc.nextLine();
    System.out.print("Digite o e-mail: ");
    String email = sc.nextLine();
    System.out.print("Digite o CEP: ");
    String cep = sc.nextLine();

    
    try (Connection conn = conectar()) {
        String sql = "INSERT INTO Pessoa (nome, cpf, datanascimento, telefone, email, cep) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nome);
        pstmt.setString(2, cpf);
        pstmt.setString(3, datanascimento);
        pstmt.setString(4, telefone);
        pstmt.setString(5, email);
        pstmt.setString(6, cep);
        pstmt.executeUpdate();
        System.out.println("Pessoa cadastrada com sucesso!");
    } catch (SQLException e) {
        System.err.println("Erro ao cadastrar pessoa: " + e.getMessage());
    }
    }

    public static String lerCPF(Scanner sc) {
        String cpf;
        do { 
            System.out.print("Digite o CPF (formato XXX.XXX.XXX-XX): ");
            cpf = sc.nextLine();
            if (!validarCPFFormatado(cpf)) {
                System.out.println("Formato inválido! Tente novamente.");
            }
        } while (!validarCPFFormatado(cpf));
        return cpf;
    }

    public static boolean validarCPFFormatado(String cpf) {
        return cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    static void listarPessoas() {
        try (Connection conn = conectar()) {
            String sql = "SELECT * FROM Pessoa";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n=== Lista de Pessoas ===");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String datanascimento = rs.getString("datanascimento");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String cep = rs.getString("cep");
                System.out.printf("ID: %d | Nome: %s | CPF: %s | Nascimento: %s | Telefone: %s | Email: %s | CEP: %s%n", id, nome, cpf, datanascimento, telefone, email, cep);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pessoas: " + e.getMessage());
        }
    }

    static void editarPessoa(Scanner sc) {
        System.out.print("Digite o ID da pessoa a ser editada: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Novo CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Nova data de nascimento (dd/mm/aaaa): ");
        String datanascimento = sc.nextLine();
        System.out.print("Novo telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Novo e-mail: ");
        String email = sc.nextLine();
        System.out.print("Novo CEP: ");
        String cep = sc.nextLine();

        try (Connection conn = conectar()) {
            String sql = "UPDATE Pessoa SET nome = ?, cpf = ?, datanascimento = ?, telefone = ?, email = ?, cep = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, cpf);
            pstmt.setString(3, datanascimento);
            pstmt.setString(4, telefone);
            pstmt.setString(5, email);
            pstmt.setString(6, cep);
            pstmt.setInt(7, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pessoa editada com sucesso!");
            } else {
                System.out.println("Nenhuma pessoa encontrada com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao editar pessoa: " + e.getMessage());
        }
    }

    static void excluirPessoa(Scanner sc) {
        System.out.print("Digite o ID da pessoa a ser excluída: ");
        int id = sc.nextInt();

        try (Connection conn = conectar()) {
            String sql = "DELETE FROM Pessoa WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pessoa excluída com sucesso!");
            } else {
                System.out.println("Nenhuma pessoa encontrada com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir pessoa: " + e.getMessage());
        }
    }
}

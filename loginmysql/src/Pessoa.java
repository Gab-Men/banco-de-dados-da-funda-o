public class Pessoa {
    private int id;
    private String nome;
    private String cpf;
    private String datanascimento;

    public Pessoa(int id, String nome, String cpf, String datanascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.datanascimento = datanascimento;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDatanascimento() {
        return datanascimento;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDatanascimento(String datanascimento) {
        this.datanascimento = datanascimento;
    }
}

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.*;
import javax.swing.JOptionPane;

public class VoltaRapida {
    private static final String URL = "jdbc:mysql://localhost:3306/apstelemetria?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        PrintScreenThread listenerThread = new PrintScreenThread();
        listenerThread.start();
        TemporizadorJOptionPane temporizador = new TemporizadorJOptionPane();
        int opcao;

        do {
            String input = JOptionPane.showInputDialog(null,
                    "Selecione uma opção:\n1 - Inserir dados da volta.\n2 - Classificação geral.\n3 - Limpar dados inseridos.\n4 - Como funciona o programa. \n0 - Sair do programa.","Volta Rápida!",JOptionPane.PLAIN_MESSAGE);
            opcao = Integer.parseInt(input);
            switch (opcao) {
                case 1:
                String nomeEquipe = JOptionPane.showInputDialog(null, "Digite o nome da equipe:");
                
                String nomePiloto = JOptionPane.showInputDialog(null, "Digite o nome do(a) piloto:");
               
                String caracVeic = JOptionPane.showInputDialog(null, "Digite a cor do veículo:");
                
                String tempoVolta1Str = JOptionPane.showInputDialog(null, "Digite o tempo da primeira volta (em segundos):");
            
                Double tempoVolta1 = Double.parseDouble(tempoVolta1Str);
                String tempoVolta2Str = JOptionPane.showInputDialog(null, "Digite o tempo da segunda volta (em segundos):");
              
                Double tempoVolta2 = Double.parseDouble(tempoVolta2Str);
                Double tempoTotal = tempoVolta1 + tempoVolta2;
                inserirDadosVolta(nomeEquipe, nomePiloto, caracVeic, tempoVolta1, tempoVolta2, tempoTotal);
                break;

                case 2:
                
                StringBuilder mensagem = new StringBuilder("Classificação geral (tempo total do menor para o maior):\n");
           
                JOptionPane.showMessageDialog(null, mensagem.toString(), "Classificação Geral", JOptionPane.INFORMATION_MESSAGE);
            
                List<String> dadosVolta = listarDadosVoltaOrdenados();
             
                mensagem = new StringBuilder();
                for (String dado : dadosVolta) {
                    mensagem.append(dado).append("\n");
                }
             
                break;
                
                case 3:
                apagarDados();
                break;

                case 4:
                JOptionPane.showMessageDialog(null, "O 'Volta Rápida' é um software especializado em armazenar os dados de volta de determinado piloto de forma on-line, incluindo sua equipe, cores de seu veículo e tempos de até 2 (duas) voltas.\nO ranking funciona da seguinte forma: quem for o mais rápido durante as duas voltas seguidas de qualificação, estará em primeiro lugar!\nO programa não permite que as informações inseridas sejam captadas através de 'prints' das telas, e repudia qualquer vazamento que venha a acontecer de seus dados.", "Como funciona?", JOptionPane.INFORMATION_MESSAGE);
                break;
                
                case 0:
                temporizador.exibirMensagemTemporaria("Finalizando processos...\n\n'Na adversidade, alguns desistem, enquanto outros batem recordes.'\n~Ayrton Senna.", 5000);
                CloseTimer exemploTimer = new CloseTimer();
                exemploTimer.run();
    
            }
        } while (opcao != 0);
    }
    
    public static void apagarDados() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String countSql = "SELECT COUNT(*) FROM dados_volta"; // Consulta para contar o número de registros
            try (PreparedStatement countStmt = conn.prepareStatement(countSql);
                 ResultSet rs = countStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) { // Verifica se o resultado da consulta é zero
                    JOptionPane.showMessageDialog(null, "O banco de dados está vazio!", "Atenção!", JOptionPane.WARNING_MESSAGE);
                } else {
                    String deleteSql = "DELETE FROM dados_volta"; // Instrução SQL para apagar dados da tabela
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                        deleteStmt.executeUpdate(); // Executa a instrução SQL
                        JOptionPane.showMessageDialog(null, "Dados apagados com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao limpar os dados do banco de dados: " + e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void inserirDadosVolta(String nomeEquipe, String nomePiloto, String caracVeic, Double tempoVolta1,
            Double tempoVolta2, Double tempoTotal) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO dados_volta (nome_equipe, nome_piloto, carac_veic, tempo_volta1, tempo_volta2, tempo_total) VALUES (?,?,?,?,?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nomeEquipe);
                stmt.setString(2, nomePiloto);
                stmt.setString(3, caracVeic);
                stmt.setDouble(4, tempoVolta1);
                stmt.setDouble(5, tempoVolta2);
                stmt.setDouble(6, tempoTotal);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dados inseridos com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir os dados da volta: " + e.getMessage(), "Erro!", JOptionPane.ERROR_MESSAGE);/*mensagem de erro com o texto "Erro ao inserir os dados da volta: " concatenado com a mensagem de erro real retornada pela exceção SQL., titulo, tipo de mensagem.*/
        }
    }

    public static List<String> listarDadosVoltaOrdenados() {
        DecimalFormat casas = new DecimalFormat("0.000");
        List<String> nomesEquipe = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
    
            String sql = "SELECT nome_equipe, nome_piloto, carac_veic, tempo_volta1, tempo_volta2, tempo_total FROM dados_volta ORDER BY tempo_total ASC ";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    int contador = 1;
                    String message = "";
                    while (rs.next()) {
                        String nomeEquipe = rs.getString("nome_equipe");
                        String nomePiloto = rs.getString("nome_piloto");
                        String caracVeic = rs.getString("carac_veic");
                        Double tempoVolta1 = rs.getDouble("tempo_volta1");
                        Double tempoVolta2 = rs.getDouble("tempo_volta2");
                        Double tempoTotal = rs.getDouble("tempo_total");
    
                        message += "(" + contador + "º) Equipe: " + nomeEquipe + "; Piloto: " + nomePiloto
                                + "; Cor do veículo: "
                                + caracVeic + "; Tempo da 1ª volta: " + casas.format(tempoVolta1)
                                + " segundos; Tempo da 2ª volta: "
                                + casas.format(tempoVolta2) + " segundos; Tempo total: "
                                + casas.format(tempoTotal) + " segundos.\n";
                        contador++;
                    }
                    if (message.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O banco de dados está vazio!", "Aviso!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, message, "Classificação geral:", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar os nomes das equipes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        Collections.sort(nomesEquipe);
        return nomesEquipe;
    }
}

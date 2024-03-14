import javax.swing.*;

public class TemporizadorJOptionPane {

    public void exibirMensagemTemporaria(String mensagem, int tempoExibicao) {
        JOptionPane pane = new JOptionPane(mensagem);
        JDialog dialog = pane.createDialog(null, "Até logo!");
        dialog.setModal(false);
        dialog.setVisible(true);

        Timer timer = new Timer(tempoExibicao, e -> dialog.setVisible(false));
        timer.setRepeats(false);
        timer.start();
    }
}


import java.io.IOException;
import java.util.Arrays;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import javax.swing.JOptionPane;

public class PrintScreenListener implements NativeKeyListener {
    public void start() {
        try {
            // Registra o listener para interceptar eventos de teclado
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException ex) {
            System.err.println("Erro ao registrar o listener: " + ex.getMessage());
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (event.getKeyCode() == NativeKeyEvent.VC_PRINTSCREEN) {
            try {
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "echo off | clip && echo.|clip");
                builder.redirectErrorStream(true);
                Process process = builder.start();
                process.waitFor();
                System.out.println("Comando executado: " + Arrays.toString(builder.command().toArray()));

                // Limpa a área de transferência
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection emptySelection = new StringSelection("");
                clipboard.setContents(emptySelection, null);
                JOptionPane.showMessageDialog(null, "O programa não permite a captura de tela!", "Atenção!", JOptionPane.WARNING_MESSAGE);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        // Não faz nada
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
        // Não faz nada
    }
}






//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.StringSelection;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;

//public class LimparAreaTransf implements KeyListener {
   // @Override
   // public void keyReleased(KeyEvent e) {
        //if (e.getKeyCode() == KeyEvent.VK_PRINTSCREEN) {
           // System.out.println ("Press Print");
            
           // StringSelection emptySelection = new StringSelection("?????");
           // Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
           // clipboard.setContents(emptySelection, null);
       // }
    //}
    
    // Implementações vazias dos outros métodos de KeyListener
   // @Override public void keyTyped(KeyEvent e) {}
//    @Override public void keyPressed(KeyEvent e) {}
//}

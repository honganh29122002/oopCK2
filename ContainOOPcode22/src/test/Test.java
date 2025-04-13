package test;

import control.HocVienController;
import view.AdmintoHVGUI;
public class Test {
    public static void main(String[] args) {
        try {
            // Set look and feel
            javax.swing.UIManager.setLookAndFeel(
                javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	AdmintoHVGUI view = new AdmintoHVGUI();
                new HocVienController(view);
                view.setVisible(true);
            }
        });
    }
}
package dataBase;

import javax.swing.*; 
import java.awt.*;
import control.HocVienController;
import view.AdmintoHVGUI;
public class LoginApp extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginApp() {
        setTitle("Đăng nhập hệ thống");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Tài khoản:"));
        txtUser = new JTextField();
        panel.add(txtUser);

        panel.add(new JLabel("Mật khẩu:"));
        txtPass = new JPasswordField();
        panel.add(txtPass);

        JButton btnLogin = new JButton("Đăng nhập");
        panel.add(new JLabel());
        panel.add(btnLogin);

        getContentPane().add(panel);

        btnLogin.addActionListener(e -> checkLogin());
    }

    private void checkLogin() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());

        User user = TaiKhoanDAO.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(null, "Đăng nhập thành công! Vai trò: " + user.getRole());
            // Mở GUI khác theo vai trò
            switch (user.getRole()) {
                case "admin":
                	AdmintoHVGUI gui = new AdmintoHVGUI();
//                    new AdminGUI().setVisible(true); // cần tạo thêm class AdminGUI
                	new HocVienController(gui); // Khởi tạo controller để gắn sự kiện
                    gui.setVisible(true);
                    break;
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "❌ Sai tên đăng nhập hoặc mật khẩu!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginApp().setVisible(true));
    }
}


package control;

import model.HocVien; 
import model.HocVienDAO;
import view.AdmintoHVGUI;
import view.HocVienDialog;
import view.ThemHocVienDialog;
import dataBase.LoginApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class HocVienController {
	private AdmintoHVGUI view;
    private HocVienDAO hocVienDAO;
    private SimpleDateFormat dateFormat;

    public HocVienController(AdmintoHVGUI view) {
        this.view = view;
        this.hocVienDAO = new HocVienDAO();
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        initController();
    }

    private void initController() {
        view.addShowListListener(new ShowListListener());
        view.addDeleteListener(new DeleteListener());
        view.addInsertListener(new InsertListener());
        view.addSearchListener(new SearchListener());
        view.addEditListener(new EditListener());
        view.addBackListener(new BackListener());
    }

    class ShowListListener implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
            try {
                List<HocVien> hocVienList = hocVienDAO.getAllHocVien();
                
                String[] columnNames = {"Mã HV", "Tên HV", "Ngày sinh", "Email"};
                Object[][] data = new Object[hocVienList.size()][4];
                
                for (int i = 0; i < hocVienList.size(); i++) {
                    HocVien hv = hocVienList.get(i);
                    data[i][0] = hv.getMaHV();
                    data[i][1] = hv.getTenHV();
                    data[i][2] = hv.getNgaySinh() != null ? dateFormat.format(hv.getNgaySinh()) : "";
                    data[i][3] = hv.getEmail();
                }
                
                view.displayHocVienList(data, columnNames);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maHV = view.getSelectedHocVien();        
            if (maHV == null || maHV.isEmpty()) {
                view.showMessage("Vui lòng chọn học viên cần xóa", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Sử dụng hộp thoại xác nhận mới
            int choice = view.showConfirmDialog(
                "Bạn có chắc chắn muốn xóa học viên " + maHV + "?", 
                "Xác nhận xóa"
            );
            
            if (choice == 0) { // 0 tương ứng với nút "Xóa"
            	try {
                boolean success = hocVienDAO.deleteHocVien(maHV);
                
                if (success) {
                    view.showMessage("Xóa học viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    new ShowListListener().actionPerformed(null); // Load lại danh sách học viên
                } else {     
                    view.showMessage("Xóa học viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            		}catch(Exception ex) {
            			view.showMessage("Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            		}
            	}
            // Nếu chọn "Hủy" (choice == 1) thì không làm gì
        }
    }
    class InsertListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            HocVienDialog dialog = new HocVienDialog(view);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                try {
                    String maHV = dialog.getMaHV();
                    String tenHV = dialog.getTenHV();
                    String ngaySinhStr = dialog.getNgaySinh();
                    String email = dialog.getEmail();

                    // Kiểm tra dữ liệu đầu vào
                    if (maHV.isEmpty() || tenHV.isEmpty() || ngaySinhStr.isEmpty() || email.isEmpty()) {
                        view.showMessage("Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Chuyển đổi ngày sinh
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date ngaySinh = sdf.parse(ngaySinhStr);
                    java.sql.Date sqlDate = new java.sql.Date(ngaySinh.getTime());

                    HocVien hv = new HocVien(maHV, tenHV, ngaySinh, email);
                    hocVienDAO.themHocVien(hv);

                    new ShowListListener().actionPerformed(null);
                    view.showMessage("Thêm học viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (ParseException ex) {
                    view.showMessage("Định dạng ngày sinh không hợp lệ (yyyy-MM-dd)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    view.showMessage("Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maHV = JOptionPane.showInputDialog(view, "Nhập mã học viên cần tìm:", "Tìm học viên", JOptionPane.QUESTION_MESSAGE);

            if (maHV != null && !maHV.trim().isEmpty()) {
                HocVien hv = hocVienDAO.timHocVienTheoMa(maHV.trim());
                if (hv != null) {
                    Object[][] data = {
                        { hv.getMaHV(), hv.getTenHV(), dateFormat.format(hv.getNgaySinh()), hv.getEmail() }
                    };
                    String[] columnNames = { "Mã HV", "Tên HV", "Ngày sinh", "Email" };
                    view.displayHocVienList(data, columnNames);
                } else {
                    view.showMessage("Mã học viên không tồn tại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    class EditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String maHV = view.getSelectedHocVien();
            if (maHV == null || maHV.isEmpty()) {
                view.showMessage("Vui lòng chọn học viên cần chỉnh sửa", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            HocVien hv = hocVienDAO.timHocVienTheoMa(maHV);
            if (hv != null) {
                HocVienDialog dialog = new HocVienDialog(view, HocVienDialog.Mode.CHINH_SUA,
                    hv.getMaHV(), hv.getTenHV(), hv.getNgaySinh(), hv.getEmail());
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    try {
                        String tenHV = dialog.getTenHV();
                        String ngaySinhStr = dialog.getNgaySinh();
                        String email = dialog.getEmail();

                        if (tenHV.isEmpty() || ngaySinhStr.isEmpty() || email.isEmpty()) {
                            view.showMessage("Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date ngaySinh = sdf.parse(ngaySinhStr);
                        java.sql.Date sqlDate = new java.sql.Date(ngaySinh.getTime());

                        HocVien updatedHv = new HocVien(maHV, tenHV, ngaySinh, email);
                        hocVienDAO.updateHocVien(updatedHv);

                        new ShowListListener().actionPerformed(null);
                        view.showMessage("Cập nhật học viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (ParseException ex) {
                        view.showMessage("Định dạng ngày sinh không hợp lệ (yyyy-MM-dd)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        view.showMessage("Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                view.showMessage("Không tìm thấy học viên", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LoginApp loginApp = new LoginApp(); // Tạo mới giao diện đăng nhập
            loginApp.setVisible(true); // Hiển thị giao diện đăng nhập
            view.dispose(); // Đóng giao diện AdmintoHVGUI
        }
    }
}

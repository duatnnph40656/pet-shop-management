/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.swing.table;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author dut
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Gọi phương thức của lớp cha để thiết lập các thuộc tính mặc định
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof ImageIcon) {
            // Nếu giá trị là ImageIcon, hiển thị hình ảnh
            ImageIcon icon = (ImageIcon) value;
            label.setIcon(icon);
            label.setText(""); // Đặt văn bản rỗng vì chúng ta chỉ muốn hiển thị hình ảnh
        } else {
            label.setIcon(null); // Đặt icon là null nếu giá trị không phải là hình ảnh
            label.setText(value != null ? value.toString() : ""); // Hiển thị văn bản nếu cần
        }
        
        return label;
    }
}

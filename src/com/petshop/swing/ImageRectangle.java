package com.petshop.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class ImageRectangle extends JComponent {

   private BufferedImage image;

    // Phương thức để đặt ảnh cho ImageRectangle
    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();  // Yêu cầu vẽ lại để hiển thị ảnh mới
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            // Vẽ ảnh vào JComponent với kích thước phù hợp
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}

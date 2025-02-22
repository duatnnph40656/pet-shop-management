package com.petshop.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageRectangle extends JComponent {

    private BufferedImage image;

    // Phương thức để đặt ảnh từ đường dẫn
    // Phương thức để đặt ảnh từ đường dẫn trong resources
    public void setImage(String imagePath) {
        try {
            // Đọc ảnh từ thư mục resources
            InputStream is = getClass().getClassLoader().getResourceAsStream("com/resources/images/" + imagePath);
            if (is != null) {
                this.image = ImageIO.read(is);
                repaint(); // Vẽ lại JComponent
            } else {
                System.out.println("Không tìm thấy file ảnh: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearImage() {
        this.image = null;
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}

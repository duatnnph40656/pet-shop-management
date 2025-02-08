package com.petshop.swing.table;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ModelImage {
    private BufferedImage image;
    private String nameProduct;

    public ModelImage() {
    }

    // Constructor để load ảnh từ file
    public ModelImage(String imagePath, String nameProduct) {
        this.nameProduct = nameProduct;
        try {
            File file = new File(imagePath);
            if (file.exists()) {
                this.image = ImageIO.read(file); // Đọc ảnh từ file
            } else {
                this.image = ImageIO.read(getClass().getResource("/com/petshop/images/01.jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    // Chuyển BufferedImage thành ImageIcon để hiển thị
    public ImageIcon getImageIcon() {
        if (image != null) {
            Image scaledImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Resize ảnh
            return new ImageIcon(scaledImage);
        }
        return null;
    }
}

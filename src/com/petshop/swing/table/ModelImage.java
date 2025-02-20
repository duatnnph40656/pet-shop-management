package com.petshop.swing.table;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ModelImage {

    private BufferedImage image;
    private String nameProduct;

    public ModelImage() {
    }

    // Constructor để load ảnh từ resources
    public ModelImage(String imagePath, String nameProduct) {
        this.nameProduct = nameProduct;
        loadImage(imagePath);
    }

    // Load ảnh từ resources
    private void loadImage(String imagePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("com/resources/images/" + imagePath)) {
            if (is != null) {
                this.image = ImageIO.read(is);
            } else {
                this.image = loadDefaultImage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.image = loadDefaultImage();
        }
    }

    // Load ảnh mặc định từ resources
    private BufferedImage loadDefaultImage() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("com/resources/images/default.jpg")) {
            return (is != null) ? ImageIO.read(is) : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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

    @Override
    public String toString() {
        return ""; // Trả về chuỗi rỗng thay vì tên sản phẩm
    }
}

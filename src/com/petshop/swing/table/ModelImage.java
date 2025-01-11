/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.swing.table;

import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author dut
 */
public class ModelImage {
    
    private BufferedImage image;
    private String tenSPCT;

    public ModelImage() {
    }

    public ModelImage(BufferedImage image, String tenSPCT) {
        this.image = image;
        this.tenSPCT = tenSPCT;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getTenSPCT() {
        return tenSPCT;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setTenSPCT(String tenSPCT) {
        this.tenSPCT = tenSPCT;
    }
    
    
    public ImageIcon getImageIcon() {
        return new ImageIcon(image);
    }
}

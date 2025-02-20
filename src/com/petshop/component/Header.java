package com.petshop.component;

import java.awt.event.ActionListener;
import javax.swing.JLabel;

public class Header extends javax.swing.JPanel {

    public Header() {
        initComponents();
    }

    public void addMenuEvent(ActionListener event) {
        cmdMenu.addActionListener(event);
        lbUserName.setText("");
        lbAcount.setText("");

    }

    public void setUserName(String username) {
        lbUserName.setText(username);
    }

    public void setLbAccount(String roleName) {
        lbAcount.setText(roleName);
    }

    public String getUserName() {
        return lbUserName.getText();
    }

    public String getAccount() {
        return lbAcount.getText();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdMenu = new com.petshop.swing.Button();
        lbUserName = new javax.swing.JLabel();
        lbAcount = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        imageAvatar1 = new com.petshop.swing.ImageAvatar();

        setBackground(new java.awt.Color(255, 255, 255));

        cmdMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/menu.png"))); // NOI18N

        lbUserName.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lbUserName.setForeground(new java.awt.Color(127, 127, 127));
        lbUserName.setText("User Name");

        lbAcount.setForeground(new java.awt.Color(127, 127, 127));
        lbAcount.setText("Admin");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        imageAvatar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/petshop/icon/icons8-circled-user-male-skin-type-1-and-2-30.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 453, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbUserName, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbAcount, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(imageAvatar1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageAvatar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbUserName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbAcount))
                    .addComponent(cmdMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.petshop.swing.Button cmdMenu;
    private com.petshop.swing.ImageAvatar imageAvatar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbAcount;
    private javax.swing.JLabel lbUserName;
    // End of variables declaration//GEN-END:variables
}

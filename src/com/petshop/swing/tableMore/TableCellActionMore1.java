/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.petshop.swing.tableMore;

import com.petshop.swing.table.Action;
import com.petshop.swing.table.ActionMore;
import com.petshop.swing.table.ActionMore1;
import com.petshop.swing.table.ModelAction;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author duat
 */
public class TableCellActionMore1 extends DefaultCellEditor{
    private ModelAction data;

    public TableCellActionMore1() {
        super(new JCheckBox());
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, int i, int i1) {
        data = (ModelAction) o;
        ActionMore1 cell = new ActionMore1(data);
        cell.setBackground(new Color(239, 244, 255));
        return cell;
    }

    //  This method to pass data to cell render when focus lose in cell
    @Override
    public Object getCellEditorValue() {
        return data;
    }
}

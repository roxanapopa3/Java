package com.company;

import javax.swing.*;
import java.awt.*;
//Render custom pentru a afisa magazinul
public class ShopRenderer extends JPanel implements ListCellRenderer<Potion>{

    public JLabel lbIndex = new JLabel();
    public JLabel lbType = new JLabel();
    public JLabel lbCost = new JLabel();
    public JLabel lbRegen = new JLabel();
    public JLabel lbWeight = new JLabel();

    public ShopRenderer(){
        setLayout(new BorderLayout(5,5));
        JPanel text = new JPanel(new GridLayout(0,5));
        text.add(lbIndex);
        text.add(lbType);
        text.add(lbCost);
        text.add(lbRegen);
        text.add(lbWeight);
        add(text,BorderLayout.CENTER);
    }
    //Metoda care returneaza tipul unei potiuni
    public String getType(Potion potion){
        if(potion.getClass().toString().contains("Health"))
            return "Health Potion";
        else if(potion.getClass().toString().contains("Mana"))
            return "Mana Potion";
        return null;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Potion> list,Potion value, int index, boolean isSelected, boolean cellHasFocus) {
        lbIndex.setText(index +" ");
        lbType.setText("Type: " + getType(value));
        lbCost.setText("Cost: " + value.getPrice());
        lbRegen.setText("Regen value: " + value.regenValue());
        lbWeight.setText("Weight: " + value.getWeight());
        lbIndex.setOpaque(true);
        lbType.setOpaque(true);
        lbCost.setOpaque(true);
        lbRegen.setOpaque(true);
        lbWeight.setOpaque(true);
        if(isSelected){
            lbIndex.setBackground(list.getSelectionBackground());
            lbType.setBackground(list.getSelectionBackground());
            lbCost.setBackground(list.getSelectionBackground());
            lbRegen.setBackground(list.getSelectionBackground());
            lbWeight.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());
        }
        else{
            lbIndex.setBackground(list.getBackground());
            lbType.setBackground(list.getBackground());
            lbCost.setBackground(list.getBackground());
            lbRegen.setBackground(list.getBackground());
            lbWeight.setBackground(list.getBackground());
            setBackground(list.getBackground());
        }
        return this;

    }
}
//Render custom pentru a afisa abilitatile unui personaj
class SpellRenderer extends JPanel implements ListCellRenderer<Spell>{

    public JLabel lbIndex = new JLabel();
    public JLabel lbType = new JLabel();

    public SpellRenderer(){
        setLayout(new BorderLayout());
        JPanel text = new JPanel(new GridLayout(0,2));
        text.add(lbIndex);
        text.add(lbType);
        add(text,BorderLayout.CENTER);
    }
    //Metoda care returneaza tipul abilitatii
    public String getType(Spell spell){
        if(spell.getClass().toString().contains("Ice"))
            return "Ice";
        else if(spell.getClass().toString().contains("Fire"))
            return "Fire";
        else if(spell.getClass().toString().contains("Earth"))
            return "Earth";
        return null;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Spell> list,Spell value, int index, boolean isSelected, boolean cellHasFocus) {
        lbIndex.setText(index+ " ");
        lbType.setText("Type: " + getType(value));
        lbIndex.setOpaque(true);
        lbType.setOpaque(true);
        if(isSelected){
            lbIndex.setBackground(list.getSelectionBackground());
            lbType.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());
        }
        else{
            lbIndex.setBackground(list.getBackground());
            lbType.setBackground(list.getBackground());
            setBackground(list.getBackground());
        }
        return this;

    }
}
//Render custom pentru a afisa inventory-ul personajului
class InventoryRenderer extends JPanel implements ListCellRenderer<Potion>{

    public JLabel lbIndex = new JLabel();
    public JLabel lbType = new JLabel();
    public JLabel lbRegen = new JLabel();

    public InventoryRenderer(){
        setLayout(new BorderLayout());
        JPanel text = new JPanel(new GridLayout(0,3));
        text.add(lbIndex);
        text.add(lbType);
        text.add(lbRegen);
        add(text,BorderLayout.CENTER);
    }
    //Metoda care returneaza tipul unei potiuni
    public String getType(Potion potion){
        if(potion.getClass().toString().contains("Health"))
            return "Health Potion";
        else if(potion.getClass().toString().contains("Mana"))
            return "Mana Potion";
        return null;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Potion> list,Potion value, int index, boolean isSelected, boolean cellHasFocus) {
        lbIndex.setText(index+ " ");
        lbType.setText("Type: " + getType(value));
        lbRegen.setText("Regen value: "+value.regenValue());
        lbIndex.setOpaque(true);
        lbType.setOpaque(true);
        lbRegen.setOpaque(true);
        if(isSelected){
            lbIndex.setBackground(list.getSelectionBackground());
            lbType.setBackground(list.getSelectionBackground());
            lbRegen.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());
        }
        else{
            lbIndex.setBackground(list.getBackground());
            lbType.setBackground(list.getBackground());
            lbRegen.setBackground(list.getBackground());
            setBackground(list.getBackground());
        }
        return this;

    }
}
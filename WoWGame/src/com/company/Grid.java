package com.company;

import java.util.ArrayList;
//Interfata care modeleaza tipul unei casute
interface CellElement{
    public char toCharacter();
}
//Clasa care construieste harta
public class Grid extends ArrayList<ArrayList<Cell>> {
    public int length, width;
    public Character current;
    public Cell current_cell;
    private static Grid instance = null;

    private Grid(int len, int wid, Cell curr_cell, Character curr_char){
        this.length=len;
        this.width=wid;
        this.current_cell=curr_cell;
        this.current = curr_char;
    }

    public static Grid getInstance(int len,int width,Cell current_cell, Character character){
        if(instance==null){
            instance = new Grid(len,width,current_cell,character);
        }
        return instance;
    }
    //Metoda care returneaza harta sub forma unei matrice
    public static ArrayList<ArrayList<Cell>> getMap(int len, int wid){
        ArrayList<ArrayList<Cell>> map = new ArrayList<ArrayList<Cell>>();
        for(int i=0;i<len;i++){
            map.add(new ArrayList<Cell>());
            for(int j=0;j<wid;j++) {
                map.get(i).add(new Cell(i, j));
            }
        }
        return map;
    }
    //Metoda care returneaza o harta custom in functie de tipul jocului
    public static ArrayList<ArrayList<Cell>> getCustomMap(String type,ArrayList<Cell> list,int len,int wid) {
        ArrayList<ArrayList<Cell>> map = new ArrayList<ArrayList<Cell>>();
        //Pentru jocul in terminal
        if (type.equals("Terminal")) {
            map = getMap(len, wid);
            map.get(0).get(3).state = Cell.type.SHOP;
            map.get(2).get(0).state = Cell.type.SHOP;
            map.get(3).get(4).state = Cell.type.ENEMY;
            map.get(4).get(4).state = Cell.type.FINISH;
        }
        //Pentru jocul grafic
        else if (type.equals("Graphic")) {
            map = getMap(len, wid);
            for(int i=0;i<list.size();i++) {
                map.get(list.get(i).x).get(list.get(i).y).state = list.get(i).state;
            }

        }
            return map;

    }

    //Metoda pentru mers la stanga
    public void goWest(ArrayList<Cell> list,String type){
        if(this.current_cell.y==0) {
            System.out.println("End of map");
            return;
        }
        this.current_cell = getCustomMap(type,list,this.length,this.width).get(this.current_cell.x).get(this.current_cell.y-1);
    }
    //Metoda pentru mers la dreapta
    public void goEast(ArrayList<Cell> list, String type){
        if (current_cell.y == this.width-1) {
            System.out.println("End of map");
            return;
        }
        this.current_cell = getCustomMap(type,list,this.length,this.width).get(current_cell.x).get(current_cell.y+1);
    }
    //Metoda pentru mers in jos
    public void goSouth(ArrayList<Cell> list,String type){
        if(this.current_cell.x == this.length-1) {
            System.out.println("End of map");
            return;
        }
        this.current_cell = getCustomMap(type,list,this.length,this.width).get(this.current_cell.x+1).get(this.current_cell.y);
    }
    //Metoda pentru mers in sus
    public void goNorth(ArrayList<Cell> list,String type){
        if(this.current_cell.x == 0){
            System.out.println("End of map");
            return;
        }
        this.current_cell = getCustomMap(type,list,this.length,this.width).get(this.current_cell.x-1).get(this.current_cell.y);
    }

}
//Clasa care modeleaza o celula a hartii
class Cell {
    public int x, y;
    //Tipurile posibile de celule
    public enum type {
        EMPTY, ENEMY, SHOP, FINISH
    }

    public CellElement element;
    public boolean visited;
    public type state;

    public Cell(int Ox, int Oy) {
        this.x = Ox;
        this.y = Oy;
        this.visited = false;
        this.state = type.EMPTY;
        this.element = new CellElement() {
            @Override
            public char toCharacter() {
                if (visited) {
                    if (state.toString().equals("EMPTY"))
                        return 'N';
                    else if(state.toString().equals("FINISH"))
                        return 'F';
                    else if(state.toString().equals("SHOP"))
                        return 'S';
                }
                return '?';
            }
        };

    }
}
//Clasa care implementeaza un magazin
class Shop implements CellElement{
    public ArrayList<Potion> potiuni;
    /*Metoda care genereaza un numar random
    de potiuni de tip random ales*/
    public void createPotion(){
        int value = 1 + (int)(Math.random() * 5);
        int maxNrPotions = 2 + (int)(Math.random()*3);
        for(int i=1; i<=maxNrPotions;i++) {
            double prob = Math.random();
            if(prob <= 0.50)
                this.potiuni.add(new HealthPotion(value*2,value,value*5));
            else
                this.potiuni.add(new ManaPotion(value*2,value,value*5));

        }
    }
    public Shop(){
        this.potiuni = new ArrayList<Potion>();
        createPotion();
    }
    //Metoda care returneaza o potiune dupa un index dat
    public Potion getPotion(int index){
        if(index >= this.potiuni.size() || index < 0){
            System.out.println("Please insert correct index");
            return null;
        }
        return this.potiuni.get(index);
    }
    //Metoda de afisare a magazinului
    public void printShop(){
        if(this.potiuni.isEmpty()){
            System.out.println("No potions available");
            return;
        }
        for (int i=0;i<this.potiuni.size();i++){
            if(this.potiuni.get(i).getClass().toString().contains("Health"))
                System.out.println(i + " " + "Health Potion "+ " " + "Regen value: " + getPotion(i).regenValue() + " " + "Cost: " + getPotion(i).getPrice() + " Weight: " + getPotion(i).getWeight());
            else
                System.out.println(i + " " + "Mana Potion "+ " " + "Regen value: " + getPotion(i).regenValue() + " " + "Cost: " + getPotion(i).getPrice() + " Weight: " + getPotion(i).getWeight());

        }
    }
    @Override
    public char toCharacter() {
        return 'S';
    }
}

package com.company;

import java.util.ArrayList;
import java.util.Scanner;
//Clasa care modeleaza jocul in terminal
public class terminalGame{
    public Game game;
    public Grid grid;
    public ArrayList<ArrayList<Cell>> map;
    public Enemy enemy;
    public Shop shop;
    public int expGained, enemiesKilled, coinsEarned;

    public terminalGame(Cell curr_cell, Character character,Game game){
        this.game = game;
        this.grid = Grid.getInstance(5,5,curr_cell,character);
        this.map = Grid.getCustomMap("Terminal",new ArrayList<Cell>(),5,5);
        this.enemy = new Enemy();
        this.shop = new Shop();
        this.expGained=0;
        this.enemiesKilled=0;
        this.coinsEarned=0;

    }
    /*Metoda care afiseaza poveste din
    casuta respectiva*/
    public void printStory(int index) {
        if (!this.grid.current_cell.visited) {
            System.out.println(this.game.stories.get(this.grid.current_cell.state).get(index));
        }
    }
    //Metoda care afiseaza harta sub forma unei matrice
    public void printMap(){
        for(int i=0;i<this.grid.length;i++) {
            for (int j = 0; j < this.grid.width; j++) {
                if (i == this.grid.current_cell.x && j == this.grid.current_cell.y) {
                    if(this.map.get(i).get(j).element.toCharacter()=='?') {
                        System.out.print("P ");
                        continue;
                    }
                    else System.out.print("P");
                }
                System.out.print(this.map.get(i).get(j).element.toCharacter() + " ");
            }
            System.out.print("\n");
        }
    }
    //Metoda care actualizeaza harta dupa fiecare mutare
    public void updateMap(Cell cell){
        this.map.get(cell.x).get(cell.y).visited = true;
    }
}
//Clasa care modeleaza o mutare in jocul din terminal
class Mutare{

    public Mutare(terminalGame game, Grid grid){
        this.showOptions(game, grid);
    }
    //Metoda care afiseaza optiunile de miscare
    public void showMovingOptions(Grid grid, int option){
        switch (option) {
            case 1:
                grid.goNorth(new ArrayList<Cell>(),"Terminal");
                break;
            case 2:
                grid.goSouth(new ArrayList<Cell>(),"Terminal");
                break;
            case 3:
                grid.goEast(new ArrayList<Cell>(),"Terminal");
                break;
            case 4:
                grid.goWest(new ArrayList<Cell>(),"Terminal");
                break;
            default:
                System.out.println("Please input valid option");
                break;
        }
    }
    //Metoda care afiseaza optiunile aferente casutei respective
    public void showOptions(terminalGame game,Grid grid){
        Scanner sc = new Scanner(System.in);
        //Optiunile pentru o casuta goala
        if(grid.current_cell.state.toString().equals("EMPTY") || (grid.current_cell.state.toString().equals("ENEMY") && game.enemy.health==0)) {
            //Daca am ucis un inamic, crestem contoarele
            if(grid.current_cell.state.toString().equals("ENEMY") && game.enemy.health==0){
                game.enemiesKilled++;
                game.expGained+=5;
                game.coinsEarned+=10;
            }
            System.out.println("1. Go North\n2. Go South\n3. Go East\n4. Go West");
            System.out.println("Please input option: ");
            int option = sc.nextInt();
            showMovingOptions(grid,option);
        }
        //Optiunile pentru celula de tip SHOP
        else if(grid.current_cell.state.toString().equals("SHOP")){
            System.out.println("Coins: " + grid.current.potions.coins);
            System.out.println("1.Buy potions\n2.Go North\n3.Go South\n4.Go East\n5.Go West");
            System.out.println("Please input option: ");
            int option = sc.nextInt();
            switch (option) {
                /*Daca alegem sa cumparam o potiune,
                afisam potiunile disponibile in magazin*/
                case 1:
                    game.shop.printShop();
                    System.out.println("Please input index: ");
                    int index = sc.nextInt();
                    grid.current.buyPotion(game.shop.getPotion(index));
                    break;
                case 2:
                    grid.goNorth(new ArrayList<Cell>(),"Terminal");
                    break;
                case 3:
                    grid.goSouth(new ArrayList<Cell>(),"Terminal");
                    break;
                case 4:
                    grid.goEast(new ArrayList<Cell>(),"Terminal");
                    break;
                case 5:
                    grid.goWest(new ArrayList<Cell>(),"Terminal");
                    break;
                default:
                    System.out.println("Please input valid option");
                    break;
            }
        }
        //Optiunile pentru casuta cu inamic
        else if(grid.current_cell.state.toString().equals("ENEMY") && game.enemy.health!=0) {
            /*Daca viata personajului ajunge la 0, afisam
             * o fereastra de final*/
            if(grid.current.health==0){
                new finalScreen(game.expGained,game.enemiesKilled,game.coinsEarned,grid.current);
            }
            //Altfel afisam optiunile aferente
            System.out.println("Enemy life: " + game.enemy.health);
            System.out.println("Character life: " + grid.current.health);
            System.out.println("1.Use spell\n2.Attack\n3.Use potion");
            System.out.println("Please input option: ");
            int option_en = sc.nextInt();
            switch (option_en) {
                /*Daca alegem sa folosim o abilitate pentru a ataca,
                * afisam abilitatile disponibile*/
                    case 1:
                        grid.current.printAbilities();
                        System.out.println("Please input index: ");
                        int index = sc.nextInt();
                        game.enemy.accept(grid.current.getSpell(index));
                        grid.current.useSpell(grid.current.getSpell(index), game.enemy);
                        grid.current.accept(game.enemy.getRandomSpell());
                        break;
                    /*Daca alegem sa atacam normal*/
                    case 2:
                        game.enemy.receiveDamage(grid.current.getDamage());
                        grid.current.accept(game.enemy.getRandomSpell());
                        break;
                    /*Daca alegem sa folosim o potiune, afisam
                    * potiunile disponibile din inventory*/
                    case 3:
                        grid.current.potions.printInventory();
                        System.out.println("Please input index: ");
                        int ind = sc.nextInt();
                        grid.current.usePotion(ind);
                        break;
                    default:
                        System.out.println("Please input valid option");
                        break;
                }
        }

    }


}

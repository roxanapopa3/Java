package com.company;

import java.util.ArrayList;
//Clasa care modeleaza o entitate din joc
abstract class Entity implements Element<Entity>{
    public ArrayList<Spell> abilitati;
    public int health = 100;
    public int maxHealth= 100;
    public int mana = 100;
    public int maxMana = 100;
    public boolean fire,ice,earth;
    //Metoda de regenerare a vietii
    public void regenHealth(int regen){
        if(this.health+regen >= maxHealth)
            this.health=maxHealth;
        else this.health+=regen;
    }
    //Metoda de regenerare a manei
    public void regenMana(int regen){
        if(this.mana+regen >= maxMana)
            this.mana=maxMana;
        else this.mana+=regen;
    }
    //Metoda de utilizare a unei abilitati
    public void useSpell(Spell s, Entity en){
        if(this.mana < s.manaCost){
            System.out.println("Not enough mana");
            return;
        }
        this.mana-=s.manaCost;
        s.visit(en);
        this.abilitati.remove(s);
    }
    /*Metoda care genereaza un numar random
    de abilitati de tip random ales*/
    public void getAbilities(){
        int maxNrAbilities = 2 + (int)(Math.random()*3);
        for(int i=1; i<=maxNrAbilities;i++) {
            double prob = Math.random();
            if(prob <= 0.33)
                this.abilitati.add(new Ice());
            else if( 0.33 < prob && prob <= 0.66)
                this.abilitati.add(new Fire());
            else this.abilitati.add(new Earth());

        }
    }
    //Metoda care returneaza o abilitate dupa un index dat
    public Spell getSpell(int index){
        return this.abilitati.get(index);
    }
    //Metoda de afisare a abilitatilor unui personaj
    public void printAbilities(){
        if(this.abilitati.isEmpty()){
            System.out.println("No spells available");
            return;
        }
        for(int i=0;i<this.abilitati.size();i++) {
            if(this.abilitati.get(i).getClass().toString().contains("Fire"))
                System.out.println(i + " " + "Fire ");
            else if(this.abilitati.get(i).getClass().toString().contains("Earth"))
                System.out.println(i + " " + "Earth ");
            else if(this.abilitati.get(i).getClass().toString().contains("Ice"))
                System.out.println(i + " " + "Ice ");
        }
    }
    /*Metoda care modeleaza felul in care primeste damage
    * personajul*/
    abstract public void receiveDamage(int dmg);
    /*Metoda care calculeaza damage-ul pe care il da
    * personajul*/
    abstract public int getDamage();
}
//Clasa care modeleaza inventory-ul personajului
class Inventory {
    public ArrayList<Potion> potiuni;
    public int maxWeight;
    public int coins;

    public Inventory() {
        this.potiuni = new ArrayList<>();
        this.maxWeight = 10;
        this.coins = 20;
    }
    //Metoda de adaugare a unei potiuni
    public void addPotion(Potion x) {
        potiuni.add(x);
    }
    //Metoda de stergere a unei potiuni
    public void removePotion(Potion x) {
        potiuni.remove(x);
    }
    //Metoda care calculeaza greutatea disponibila
    public int weightLeft() {
        int currWeight = maxWeight;
        for (int i = 0; i < potiuni.size(); i++)
            currWeight -= potiuni.get(i).getWeight();
        if (currWeight <= 0)
            return 0;
        return currWeight;
    }
    //Metoda de afisare a invetory-ului
    public void printInventory() {
        for (int i = 0; i < this.potiuni.size(); i++) {
            if (this.potiuni.get(i).getClass().toString().contains("Health"))
                System.out.println(i + " " + "Health Potion " + " " + "Regen value: " + this.potiuni.get(i).regenValue());
            System.out.println(i + " " + "Mana Potion " + " " + "Regen value: " + this.potiuni.get(i).regenValue());
        }

    }
}
//Metoda care modeleaza un personaj
abstract public class Character extends Entity{
    public String name;
    public int x,y;
    public Inventory potions;
    public int exp,level;
    public int strength, charisma, dexterity;
    //Metoda de cumparare a unei potiuni
    public void buyPotion(Potion x){
        if(this.potions.coins < x.getPrice() && potions.weightLeft() < x.getWeight()){
            System.out.println("Can't buy potion");
        }
        else {
            this.potions.addPotion(x);
            this.potions.coins-=x.getPrice();
        }
    }
    /*Metoda care returneaza o potiune in functie
    de un index dat*/
    public Potion getPotion(int index){
        return this.potions.potiuni.get(index);
    }
    //Metoda de utilizare a unei potiuni
    public void usePotion(int index){
        Potion potiune = getPotion(index);
        if(potiune!=null){
            if(potiune.getClass().toString().contains("Health")){
                regenHealth(potiune.regenValue());
                potiune.usePotion();
                this.potions.removePotion(potiune);
            }
            else if(potiune.getClass().toString().contains("Mana")){
                regenMana(potiune.regenValue());
                potiune.usePotion();
                this.potions.removePotion(potiune);
            }
        }
    }
    //Metoda pentru cresterea experientei curente
    public void increaseExp(int val){
        this.exp+=val;
        if(this.exp >= 100){
            this.level++;
            this.exp=this.exp%100;
        }
    }
    /*Metoda care returneaza experienta
    sub forma de string
     */
    public String ExptoString(){
        return this.exp+ " ";
    }
    /*Metoda care returneaza nivelul
    sub forma de string
     */
    public String LeveltoString() {
        return this.level + " ";
    }
}
//Clasa care modeleaza abilitatile
abstract class Spell implements Visitor<Entity>{
    public int damage, manaCost;
}
//Clasa pentru abilitatea de tip Fire
class Fire extends Spell{
    public Fire(){
        this.damage = 20;
        this.manaCost = 25;
    }
    /*Metoda care modeleaza cum afecteaza
    * abilitatea inamicul*/
    @Override
    public void visit(Entity entity) {
        if (entity.fire) {
            System.out.println("Imun");
        }
        else entity.receiveDamage(damage);
    }
}
//Clasa pentru abilitatea de tip Ice
class Ice extends Spell{
    public Ice(){
        this.damage = 15;
        this.manaCost = 20;
    }
    /*Metoda care modeleaza cum afecteaza
     * abilitatea inamicul*/
    @Override
    public void visit(Entity entity) {
        if(entity.ice){
            System.out.println("Imun");
        }
        else entity.receiveDamage(damage);

    }
}
//Clasa pentru abilitatea de tip Earth
class Earth extends Spell{
    public Earth() {
        this.damage = 10;
        this.manaCost = 10;
    }
    /*Metoda care modeleaza cum afecteaza
     * abilitatea inamicul*/
    @Override
    public void visit(Entity entity) {
        if(entity.earth){
            System.out.println("Imun");
        }
        else entity.receiveDamage(damage);
    }
}
//Clasa pentru personaj de tip Warrior
class Warrior extends Character {
    public int maxWeight=100;
    public int strength = 2;
    public int dexterity = charisma= 1;
    public int x = y =0;

    public Warrior(String nume,int lvl, int experience){
        super();
        this.name = nume;
        this.level = lvl;
        this.exp = experience;
        this.potions = new Inventory();
        this.abilitati = new ArrayList<>();
        this.fire =true;
        this.ice=false;
        this.earth=false;
        this.getAbilities();
    }

    //Metoda pentru primit damage
    @Override
    public void receiveDamage(int dmg) {
        double prob = Math.random();
        if(prob <= 0.5)
            this.health -= (dexterity+charisma)*dmg;
        else this.health -= (dexterity+charisma)*dmg*2;
        if(this.health < 0)
            this.health=0;
    }
    //Metoda pentru calculul damage-ului atacului
    @Override
    public int getDamage() {
        double prob = Math.random();
        if(prob >= 0.5)
            return strength*20;
        else return strength*10;
    }

    //Metoda care permite sa fie atacat
    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }
}
//Clasa pentru personaj de tip Rogue
class Rogue extends Character{
    public int maxWeight=70;
    public int strength = charisma = 1;
    public int dexterity = 2;
    public int x = y =0;

    public Rogue(String nume,int lvl, int experience){
        super();
        this.name = nume;
        this.level = lvl;
        this.exp = experience;
        this.potions = new Inventory();
        this.abilitati = new ArrayList<>();
        this.fire =false;
        this.ice=false;
        this.earth=true;
        this.getAbilities();
    }
    //Metoda pentru primit damage
    @Override
    public void receiveDamage(int dmg) {
        double prob = Math.random();
        if(prob <=0.5)
            this.health -= (strength+charisma)*dmg;
        else this.health -= (strength+charisma)*dmg*2;
        if(this.health < 0)
            this.health=0;
    }
    //Metoda pentru calculul damage-ului atacului
    @Override
    public int getDamage() {
        double prob = Math.random();
        if(prob >= 0.5)
            return dexterity*20;
        return dexterity*10;
    }
    //Metoda care permite sa fie atacat
    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }
}
//Clasa pentru personaj de tip Mage
class Mage extends Character{
    public int maxWeight=40;
    public int strength = dexterity =1;
    public int charisma= 2;
    public int x = y =0;

    public Mage(String nume,int lvl, int experience){
        super();
        this.name = nume;
        this.level = lvl;
        this.exp = experience;
        this.potions = new Inventory();
        this.abilitati = new ArrayList<>();
        this.fire =false;
        this.ice=true;
        this.earth=false;
        this.getAbilities();
    }
    //Metoda pentru primit damage
    @Override
    public void receiveDamage(int dmg) {
        double prob = Math.random();
        if(prob <= 0.5)
            this.health -= (dexterity+strength)*dmg;
        else this.health -= (dexterity+strength)*dmg*2;
        if(this.health < 0)
            this.health=0;
    }
    //Metoda pentru calculul damage-ului atacului
    @Override
    public int getDamage() {
        double prob = Math.random();
        if(prob >= 0.5)
            return charisma*20;
        return charisma*10;
    }
    //Metoda care permite sa fie atacat
    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }
}
//Clasa pentru instantierea unui personaj
class Personaj{

    public Character createCharacter(String Type, String name, int level, int exp){
        if(Type.equalsIgnoreCase("Warrior")){
            return new Warrior(name,level,exp);

        } else if(Type.equalsIgnoreCase("Rogue")){
            return new Rogue(name,level,exp);

        } else if(Type.equalsIgnoreCase("Mage")){
            return new Mage(name,level,exp);
        }
       else return null;
    }

}
//Clasa care construieste un inamic
class Enemy extends Entity implements CellElement {

    private boolean getRandBoolean(){
        return Math.random() < 0.5;
    }

    public Enemy(){
        this.health = 10+(int)(Math.random()*((this.maxHealth-10)+1));
        this.mana = 10+(int)(Math.random()*((this.maxMana-10)+1));
        this.abilitati = new ArrayList<>();
        this.fire = getRandBoolean();
        this.ice = getRandBoolean();
        this.earth = getRandBoolean();
        this.getAbilities();
    }
    //Metoda pentru primit damage
    @Override
    public void receiveDamage(int dmg) {
        double prob = Math.random();
        if(prob >= 0.5){
            if(this.health >= dmg)
                this.health-=dmg;
            else this.health = 0;
        }
    }
    //Metoda pentru calculul damage-ului atacului
    @Override
    public int getDamage() {
        int dmg = 10;
        double prob = Math.random();
        if(prob>=0.5)
            return dmg*2;
        return dmg;
    }
    //Metoda care returneaza o abilitate random a inamicului
    public Spell getRandomSpell() {
        int prob = (int) (Math.random() * this.abilitati.size());
        return this.abilitati.get(prob);
    }
    //Metoda care permite sa fie atacat
    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    @Override
    public char toCharacter() {
        return 'E';
    }
}

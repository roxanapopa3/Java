package com.company;
//Interfata care modeleaza o potiune
public interface Potion{
    public void usePotion();
    public int getPrice();
    public int regenValue();
    public int getWeight();
}
//Clasa care construieste o potiune de viata
class HealthPotion implements Potion{
    private int price, weight, regenValue;

    public HealthPotion(int pr,int wght, int regen){
        this.price = pr;
        this.weight = wght;
        this.regenValue = regen;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int regenValue() {
        return regenValue;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public void usePotion() {
        weight = 0;
    }
}
//Clasa care construieste o potiune de mana
class ManaPotion implements Potion{
    private int price, weight, regenValue;

    public ManaPotion(int pr,int wght, int regen){
        this.price = pr;
        this.weight = wght;
        this.regenValue = regen;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int regenValue() {
        return regenValue;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public void usePotion() {
        weight = 0;
    }
}

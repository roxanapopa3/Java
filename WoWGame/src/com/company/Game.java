package com.company;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//Clasa care implementeaza jocul
public class Game{
    public Map<Cell.type,ArrayList<String>> stories;
    public ArrayList<Account> jucatori;
    private static Game instance = null;

    private Game(){
        this.stories = new HashMap<Cell.type,ArrayList<String>>();
        this.jucatori = new ArrayList<Account>();
    }

    public static Game getInstance(){
        if(instance==null)
            instance = new Game();
        return instance;
    }
    //Metoda care incarca datele parsate din JSON
    public void readFromJsonFile(){
        try{
            String text = Files.readString(Paths.get("C:\\Users\\roxan\\IdeaProjects\\Tema_POO\\accounts.json"));
            String text_stories = Files.readString(Paths.get("C:\\Users\\roxan\\IdeaProjects\\Tema_POO\\stories.json"));
            JSONObject obj = new JSONObject(text);
            JSONArray arr = obj.getJSONArray("accounts");
            JSONObject obj_stories = new JSONObject(text_stories);
            JSONArray arr_story = obj_stories.getJSONArray("stories");
            ArrayList<String> enemy = new ArrayList<String>();
            ArrayList<String> finish = new ArrayList<String>();
            ArrayList<String> shop = new ArrayList<String>();
            ArrayList<String> empty = new ArrayList<String>();
            for(int i=0;i<arr_story.length();i++){
                String type = arr_story.getJSONObject(i).getString("type");
                String story = arr_story.getJSONObject(i).getString("value");
                if(type.equals(Cell.type.ENEMY.toString()))
                    enemy.add(story);
                else if(type.equals(Cell.type.SHOP.toString()))
                    shop.add(story);
                else if (type.equals(Cell.type.EMPTY.toString()))
                    empty.add(story);
                else if (type.equals(Cell.type.FINISH.toString()))
                    finish.add(story);
            }
            this.stories.put(Cell.type.ENEMY,enemy);
            this.stories.put(Cell.type.EMPTY,empty);
            this.stories.put(Cell.type.SHOP,shop);
            this.stories.put(Cell.type.FINISH,finish);
            for(int i = 0; i < arr.length(); i++){
                String name = arr.getJSONObject(i).getString("name");
                String country = arr.getJSONObject(i).getString("country");
                String email = arr.getJSONObject(i).getJSONObject("credentials").getString("email");
                String pass = arr.getJSONObject(i).getJSONObject("credentials").getString("password");
                JSONArray list = arr.getJSONObject(i).getJSONArray("favorite_games");
                ArrayList<String> games_list=new ArrayList<String>();
                for(int j=0;j<list.length();j++)
                    games_list.add(list.getString(j));
                int games = arr.getJSONObject(i).getInt("maps_completed");
                JSONArray list2= arr.getJSONObject(i).getJSONArray("characters");
                ArrayList<Character> personaje = new ArrayList<Character>();
                for(int j=0;j<list2.length();j++) {
                    String nume = list2.getJSONObject(j).getString("name");
                    int lvl = list2.getJSONObject(j).getInt("level");
                    String type = list2.getJSONObject(j).getString("profession");
                    int exp = list2.getJSONObject(j).getInt("experience");
                    Personaj pers = new Personaj();
                    Character c = pers.createCharacter(type,nume,lvl,exp);
                    personaje.add(c);
                }
                Account a = new Account(name,country,email,pass,games,games_list,personaje);
                jucatori.add(a);
            }
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
    //Metoda de run care porneste jocul
    public static void main(String args[]){
        Game g =new Game();
        g.readFromJsonFile();
        CreateLoginForm form = new CreateLoginForm(g);
    }

}
/*Exceptia pentru realizarea un obiect Information
fara credentiale sau nume*/
class InformationIncompleteException extends Exception{
    public InformationIncompleteException(String errorMsg){
        super(errorMsg);
    }
}
//Clasa care modeleaza un cont al jucatorului
class Account{
    //Clasa care modelează informatiile jucatorului
    public static class Information{

        private final Credentials credential;
        private final ArrayList<String> fav_games;
        private final String name, country;

        private Information(Builder builder) throws InformationIncompleteException{
            if(builder.credential == null || builder.name==null)
                throw new InformationIncompleteException("Please insert credentials or name");
            this.credential = builder.credential;
            this.fav_games = builder.fav_games;
            this.name = builder.name;
            this.country= builder.country;
        }
        public String getEmail(){
            return credential.email;
        }

        public String getPass(){
            return credential.parola;
        }

        public static class Builder {
            private Credentials credential;
            private ArrayList<String> fav_games;
            private String name, country;

            public Builder(Credentials c) {
                this.credential = new Credentials();
                this.credential = c;
            }

            public Builder findFavGames(ArrayList<String> games) {
                this.fav_games = games;
                return this;
            }

            public Builder withName(String nume) {
                this.name = nume;
                return this;
            }

            public Builder inCountry(String cntry) {
                this.country = cntry;
                return this;
            }

            public Information build() {
                try {
                    return new Information(this);
                }
                catch (InformationIncompleteException ex){
                    System.out.println("Please insert name or credentials");
                }
                return null;
            }
        }
    }

    public Information info;
    public ArrayList<Character> chars;
    public int games_played;

    public Account(String nume, String cntry, String email, String password,int games, ArrayList<String> games_fav, ArrayList<Character> characters){
        Credentials c = new Credentials(email,password);
        this.info = new Information.Builder(c).withName(nume).inCountry(cntry).findFavGames(games_fav).build();
        Collections.sort(info.fav_games);
        this.chars = characters;
    }
}
//Clasa care modeleaza credentialele unui jucator
class Credentials{
    public String email;
    public String parola;

    public Credentials(){
        this.parola = new String();
        this.email = new String();
    }
    public Credentials(String mail,String password){
        this.email = mail;
        this.parola = password;
    }
}



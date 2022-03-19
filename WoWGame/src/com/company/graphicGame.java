package com.company;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
//Clasa care modeleaza jocul grafic
public class graphicGame {
    public Game game;
    public Grid grid;
    public ArrayList<ArrayList<Cell>> map;
    public JFrame frame,statusFrame;
    public JPanel panel,statusPanel;
    public ArrayList<Cell> config;
    public ImageIcon icon;
    public JLabel[][] matrix;
    public JLabel lifeLb, manaLb,coinsLabel,storyLabel;
    public int expGained, enemiesKilled, coinsEarned, steps;

    public graphicGame(Cell curr_cell, Character character,Game game){
        int len = 7 + (int)(Math.random()*4);
        int wid = 7 + (int)(Math.random()*4);
        this.enemiesKilled=0;
        this.expGained=0;
        this.coinsEarned=0;
        this.steps=0;
        this.game=game;
        this.grid = Grid.getInstance(wid,len,curr_cell,character);
        this.config = getMapConfig(wid,len);
        this.map = Grid.getCustomMap("Graphic",config,wid,len);
        this.frame= new JFrame();
        this.frame.setLayout(new BorderLayout());
        this.frame.setSize(700,500);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setTitle("GAME");
        this.panel = new JPanel();
        panel.setLayout(new GridLayout(wid+1,len+1));
        this.frame.getContentPane().add(panel,BorderLayout.CENTER);
        this.panel.setVisible(true);
        this.frame.setVisible(true);
        this.matrix = new JLabel[this.grid.length][this.grid.width];
    }
    /*Metoda care verifica daca o pereche de coordonate
    * se afla deja in lista pentru a nu avea suprepuneri*/
    public boolean isInList(ArrayList<Cell> list,int x, int y){
        for(int i=0;i< list.size();i++)
            if(list.get(i).x==x && list.get(i).y==y)
                return true;
        return false;
    }
    /*Metoda care genereaza random configuratia hartii
    * (de ex: numarul magazinelor,al inamicilor,
    * coordonatele acestora)*/
    public ArrayList<Cell> getMapConfig(int len,int wid){
        ArrayList<Cell> cell_list = new ArrayList<>();
        Cell cell;
        int nrInamici = 4 + (int)(Math.random()*(((len-1)-4)+1));
        while(nrInamici!=0){
            int x = 1+(int)(Math.random()*(len-2));
            int y = 1+(int) (Math.random()*(wid-2));
            cell = new Cell(x,y);
            cell.state= Cell.type.ENEMY;
            if(!isInList(cell_list,x,y)){
                cell_list.add(cell);
                nrInamici--;
            }
        }
        int nrShops = 2 + (int)(Math.random()*5);
        while(nrShops!=0){
            int x = 1+(int)(Math.random()*(len-2));
            int y = 1+(int) (Math.random()*(wid-2));
            cell = new Cell(x,y);
            cell.state= Cell.type.SHOP;
            if(!isInList(cell_list,x,y)){
                cell_list.add(cell);
                nrShops--;
            }
        }
        cell = new Cell(len-1,wid-1);
        cell.state= Cell.type.FINISH;
        cell_list.add(cell);
        return cell_list;

    }
    //Functia de afisare a hartii
    public void printMap(){
        for(int i=0;i<this.grid.length;i++) {
            for (int j = 0; j < this.grid.width; j++) {
                matrix[i][j] = new JLabel();
                matrix[i][j].setSize(100, 100);
                matrix[i][j].setBackground(Color.GREEN);
                matrix[i][j].setBorder(new LineBorder(Color.ORANGE));
                matrix[i][j].setOpaque(true);
                icon = new ImageIcon("icons8-question-mark-48.png");
                matrix[i][j].setIcon(icon);
                panel.add(matrix[i][j]);
            }
        }
    }
    //Functia de update a hartii dupa fiecare mutare
    public void updateMap(){
        if(this.grid.current_cell.state.toString().equals("ENEMY"))
            this.matrix[this.grid.current_cell.x][this.grid.current_cell.y].setIcon(new ImageIcon("icons8-android-os-30.png"));
        else if(this.grid.current_cell.state.toString().equals("SHOP"))
            this.matrix[this.grid.current_cell.x][this.grid.current_cell.y].setIcon(new ImageIcon("icons8-shop-30.png"));
        else if(this.grid.current_cell.state.toString().equals("EMPTY"))
            this.matrix[this.grid.current_cell.x][this.grid.current_cell.y].setIcon(new ImageIcon("icons8-luigi-50.png"));
        else if(this.grid.current_cell.state.toString().equals("FINISH")) {
            this.matrix[this.grid.current_cell.x][this.grid.current_cell.y].setIcon(new ImageIcon("icons8-luigi-50.png"));
            new finalScreen(this.expGained,this.enemiesKilled,this.coinsEarned,this.grid.current);
            this.statusFrame.dispose();
            this.frame.dispose();
        }

    }
    /*Functia care returneaza povestea asociata
    fiecarei casute*/
    public String getStory(int index){
        if (!this.grid.current_cell.visited) {
            return this.game.stories.get(this.grid.current_cell.state).get(index);
        }
        return null;
    }
    /*Functia care construieste fereastra
    cu datele actuale ale personajului*/
    public void statusPanel(){
        statusFrame = new JFrame();
        statusFrame.setLayout(new BorderLayout());
        statusFrame.setSize(600,200);
        statusFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statusFrame.setTitle("CHARACTER STATUS");
        lifeLb = new JLabel("Health: " + grid.current.health);
        manaLb = new JLabel("Mana: "+grid.current.mana);
        coinsLabel = new JLabel("Coins: "+grid.current.potions.coins);
        storyLabel = new JLabel(getStory(this.steps));
        statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(4,1));
        statusPanel.add(storyLabel);
        statusPanel.add(lifeLb);
        statusPanel.add(manaLb);
        statusPanel.add(coinsLabel);
        statusFrame.getContentPane().add(statusPanel,BorderLayout.CENTER);
        statusPanel.setVisible(true);
        statusFrame.setVisible(true);
    }
}
//Clasa care modeleaza o mutare a jocului grafic
class mutareGraphic implements ActionListener {
    public graphicGame game;
    public JButton b1 = new JButton("Go North");
    public JButton b2 = new JButton("Go South");
    public JButton b3 = new JButton("Go East");
    public JButton b4 = new JButton("Go West");
    public JButton b5 = new JButton("Buy potions");
    public JButton b6 = new JButton("Attack");
    public JButton b7 = new JButton("Use potion");

    public mutareGraphic(graphicGame game){
        this.game=game;
        this.showGraphicOptions(game);
    }
    /*Functia care afiseaza optiunile
    pentru o casuta goala*/
    public void showEmptyOptions(){
        this.game.panel.add(this.b1,BorderLayout.LINE_START);
        this.game.panel.add(this.b2);
        this.game.panel.add(this.b3);
        this.game.panel.add(this.b4);
        this.game.panel.add(this.b5);
        this.game.panel.add(this.b6);
        this.game.panel.add(this.b7);;
        this.b1.addActionListener(this);
        this.b2.addActionListener(this);
        this.b3.addActionListener(this);
        this.b4.addActionListener(this);
        this.b5.addActionListener(this);
        this.b6.addActionListener(this);
        this.b7.addActionListener(this);
    }
    /*Metoda care afiseaza optiunile pentru fiecare
    casuta*/
    public void showGraphicOptions(graphicGame game){
            if(this.game.steps >=this.game.game.stories.get(this.game.grid.current_cell.state).size())
                this.game.steps=0;
            //Pentru casutele goale
            if (this.game.grid.current_cell.state.toString().equals("EMPTY")) {
                showEmptyOptions();
                game.statusPanel();
                this.game.steps++;
            }
            //Pentru celulele de tip magazin
            else if(this.game.grid.current_cell.state.toString().equals("SHOP")){
                showEmptyOptions();
                game.statusPanel();
                this.game.steps++;
            }
            //Pentru casutele cu inamici
            else if(this.game.grid.current_cell.state.toString().equals("FINISH")){
                game.statusPanel();
                this.game.steps++;
        }
    }
    /*Functia care creeaza un JList cu potiunile
    dintr-un magazin*/
    private JList<Potion> createPotionList(Shop shop){
        DefaultListModel<Potion> l1 = new DefaultListModel<>();
        for(int i=0;i<shop.potiuni.size();i++){
            l1.addElement(shop.potiuni.get(i));
        }
        JList<Potion> list = new JList<Potion>(l1);
        list.setCellRenderer(new ShopRenderer());
        return list;
    }
    /*Functia care creeaza un JList cu abilitatile unui
    * personaj*/
    private JList<Spell> createSpellList(){
        DefaultListModel<Spell> l1 = new DefaultListModel<>();
        for(int i=0;i<this.game.grid.current.abilitati.size();i++){
            l1.addElement(this.game.grid.current.abilitati.get(i));
        }
        JList<Spell> list = new JList<Spell>(l1);
        list.setCellRenderer(new SpellRenderer());
        return list;
    }
    /*Functia care creeaza un JList cu potiunile
    din inventory-ul personajului*/
    private JList<Potion> createInventoryList(){
        DefaultListModel<Potion> l1 = new DefaultListModel<>();
        for(int i=0;i<this.game.grid.current.potions.potiuni.size();i++){
            l1.addElement(this.game.grid.current.potions.potiuni.get(i));
        }
        JList<Potion> list = new JList<Potion>(l1);
        list.setCellRenderer(new InventoryRenderer());
        return list;
    }
    /*Functia care afiseaza inventory-ul
    * unui personaj*/
    public void showInventory() {
        JFrame invFrame = new JFrame();
        JPanel invPanel = new JPanel();
        JButton button = new JButton("Use potion");
        JList<Potion> listPotion = new JList<>();
        listPotion = createInventoryList();
        JList<Potion> finalListPotion1 = listPotion;
        //Daca alegem sa folosim o potiune
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Folosim potiunea selectata
                if(e.getSource()==button){
                    game.grid.current.usePotion(finalListPotion1.getSelectedIndex());
                    if(finalListPotion1.getSelectedValue().getClass().toString().contains("Mana")) {
                        game.statusFrame.dispose();
                        game.statusPanel();
                    }
                    else if(finalListPotion1.getSelectedValue().getClass().toString().contains("Health")) {
                        game.statusFrame.dispose();
                        game.statusPanel();
                    }
                }
            }
        });
        invFrame.setLayout(new BorderLayout());
        invFrame.setSize(800, 200);
        invFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        invFrame.setTitle("INVENTORY");
        invPanel.setLayout(new GridLayout(2,1));
        invPanel.add(finalListPotion1);
        invPanel.add(button);
        invFrame.getContentPane().add(invPanel, BorderLayout.CENTER);
        invPanel.setVisible(true);
        invFrame.setVisible(true);
    }
    //Metoda care afiseaza magazinul
    public void showShop() {
        Shop shop = new Shop();
        JFrame shopFrame = new JFrame();
        JPanel shopPanel = new JPanel();
        this.game.coinsLabel = new JLabel("Coins: " + game.grid.current.potions.coins);
        JButton button = new JButton("Buy");
        JList<Potion> listPotion = new JList<>();
        listPotion = createPotionList(shop);
        JList<Potion> finalListPotion = listPotion;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Cumparam potiunea selectata
                if(e.getSource()==button){
                    game.grid.current.buyPotion(finalListPotion.getSelectedValue());
                    game.statusFrame.dispose();
                    game.statusPanel();
                }
            }
        });
        shopFrame.setLayout(new BorderLayout());
        shopFrame.setSize(800, 200);
        shopFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shopFrame.setTitle("SHOP");
        shopPanel.setLayout(new GridLayout(2,1));
        shopPanel.add(listPotion);
        shopPanel.add(button);
        shopFrame.getContentPane().add(shopPanel, BorderLayout.CENTER);
        shopPanel.setVisible(true);
        shopFrame.setVisible(true);
    }
    //Metoda care afiseaza abilitatile personajului
    public void showAbilities(Enemy enemy){
        JFrame abilitiesFrame = new JFrame();
        JPanel abilitiesPanel = new JPanel();
        this.game.manaLb = new JLabel("Mana: " + game.grid.current.mana);
        JLabel enemyLabel = new JLabel("Enemy life: " + enemy.health);
        JButton button = new JButton("Use spell");
        JButton button2 = new JButton("Attack");
        JList<Spell> listSpell = new JList<>();
        listSpell = createSpellList();
        JList<Spell> finalListSpell = listSpell;
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Folosim abilitatea selectata
                if(e.getSource()==button){
                    enemy.accept(finalListSpell.getSelectedValue());
                    game.grid.current.useSpell(finalListSpell.getSelectedValue(),enemy);
                    //Daca viata inamicului nu e 0, ataca si el
                    if(enemy.health!=0) {
                        game.grid.current.accept(enemy.getRandomSpell());
                    }
                    //Altfel crestem contoarele
                    else {
                        game.coinsEarned+=10;
                        game.expGained+=5;
                        game.enemiesKilled++;
                    }
                    game.statusFrame.dispose();
                    game.statusPanel();
                    enemyLabel.setText("Enemy life: "+enemy.health);
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Atacam simplu
                if(e.getSource()==button2){
                    enemy.receiveDamage(game.grid.current.getDamage());
                    //Daca viata inamicului nu e 0, ataca si el
                    if(enemy.health!=0) {
                        game.grid.current.accept(enemy.getRandomSpell());
                    }
                    //Altfel crestem contoarele
                    else {
                        game.coinsEarned+=10;
                        game.expGained+=5;
                        game.enemiesKilled++;
                    }
                    enemyLabel.setText("Enemy life: "+enemy.health);
                    game.statusFrame.dispose();
                    game.statusPanel();
                }
            }
        });
        abilitiesFrame.setLayout(new BorderLayout());
        abilitiesFrame.setSize(800, 400);
        abilitiesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        abilitiesFrame.setTitle("ABILITIES");
        abilitiesPanel.setLayout(new GridLayout(5,1));
        abilitiesPanel.add(enemyLabel);
        abilitiesPanel.add(listSpell);
        abilitiesPanel.add(button);
        abilitiesPanel.add(button2);
        abilitiesFrame.getContentPane().add(abilitiesPanel, BorderLayout.CENTER);
        abilitiesFrame.setVisible(true);
        abilitiesPanel.setVisible(true);
    }
    //Metoda pentru atacul inamicului
    public void attackEnemy(){
        Enemy enemy = new Enemy();
        showAbilities(enemy);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Daca alegem sa mergem la nord
        if(e.getSource()==b1){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            this.game.grid.goNorth(this.game.config,"Graphic");
            this.game.updateMap();
        }
        //Daca alegem sa mergem la sud
        else if(e.getSource()==b2){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            this.game.grid.goSouth(this.game.config,"Graphic");
            this.game.updateMap();
        }
        //Daca alegem sa mergem la est
        else if(e.getSource()==b3){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            this.game.grid.goEast(this.game.config,"Graphic");
            this.game.updateMap();
        }
        //Daca alegem sa mergem la vest
        else if(e.getSource()==b4){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            this.game.grid.goWest(this.game.config,"Graphic");
            this.game.updateMap();
        }
        //Daca alegem sa cumparam o potiune
        else if(e.getSource()==b5){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            if(!this.game.grid.current_cell.state.toString().equals("SHOP")) {
                System.out.println("Not a shop");
            }
            else showShop();
            this.game.updateMap();
        }
        //Daca alegem sa atacam
        else if(e.getSource()==b6){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            if(!this.game.grid.current_cell.state.toString().equals("ENEMY")) {
                System.out.println("Nothing to attack");
            }
            else attackEnemy();
            this.game.updateMap();
        }
        //Daca alegem sa folosim o potiune
        else if(e.getSource()==b7){
            this.game.grid.current_cell.visited = true;
            this.game.matrix[this.game.grid.current_cell.x][this.game.grid.current_cell.y].setIcon(null);
            showInventory();
            this.game.updateMap();
        }
    }
}

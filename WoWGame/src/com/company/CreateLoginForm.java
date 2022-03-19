package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*Render custom pentru afisarea listei de caractere dintr-un
cont la inceputul jocului*/
class CharacterRenderer extends JPanel implements ListCellRenderer<Character>{

    public JLabel lbName = new JLabel();
    public JLabel lbProf = new JLabel();
    public JLabel lbExp = new JLabel();
    public JLabel lbLvl = new JLabel();

    public CharacterRenderer(){
        setLayout(new BorderLayout(5,5));
        JPanel text = new JPanel(new GridLayout(0,4));
        text.add(lbName);
        text.add(lbProf);
        text.add(lbExp);
        text.add(lbLvl);
        add(text,BorderLayout.CENTER);

    }

    public String getProfession(Character c){
        if(c.getClass().toString().contains("Warrior"))
            return "Warrior";
        else if(c.getClass().toString().contains("Rogue"))
            return "Rogue";
        else if(c.getClass().toString().contains("Mage"))
            return "Mage";
        return null;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Character> list, Character value, int index, boolean isSelected, boolean cellHasFocus) {
        lbName.setText("Name: " + value.name);
        lbProf.setText("Profession: " + getProfession(value));
        lbExp.setText("Exp: " + value.ExptoString());
        lbLvl.setText("Level: " + value.LeveltoString());
        lbName.setOpaque(true);
        lbProf.setOpaque(true);
        lbExp.setOpaque(true);
        lbLvl.setOpaque(true);
        if(isSelected){
            lbName.setBackground(list.getSelectionBackground());
            lbProf.setBackground(list.getSelectionBackground());
            lbExp.setBackground(list.getSelectionBackground());
            lbLvl.setBackground(list.getSelectionBackground());
            setBackground(list.getSelectionBackground());
        }
        else{
            lbName.setBackground(list.getBackground());
            lbProf.setBackground(list.getBackground());
            lbExp.setBackground(list.getBackground());
            lbLvl.setBackground(list.getBackground());
            setBackground(list.getBackground());
        }
        return this;

    }
}
/*Clasa care construieste formularul de login
la inceputul jocului*/
public class CreateLoginForm extends JFrame implements ActionListener
{
    JButton b1;
    JPanel newPanel;
    JFrame frame;
    JLabel userLabel, passLabel;
    JList<Account> list;
    JScrollPane scrollPane;
    JTextField textField2;
    //Render custom pentru lista de conturi parsata din JSON
    class AccountRenderer extends JPanel implements ListCellRenderer<Account>{

        public JLabel lbEmail = new JLabel();

        public AccountRenderer(){
            setLayout(new BorderLayout(5,5));
            JPanel text = new JPanel(new GridLayout(0,1));
            text.add(lbEmail);
            add(text,BorderLayout.CENTER);

        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Account> list, Account value, int index, boolean isSelected, boolean cellHasFocus) {
            lbEmail.setText(value.info.getEmail());
            lbEmail.setOpaque(true);
            if(isSelected){
                lbEmail.setBackground(list.getSelectionBackground());
                setBackground(list.getSelectionBackground());
                textField2.setText(value.info.getPass());
            }
            else{
                lbEmail.setBackground(list.getBackground());
                setBackground(list.getBackground());
            }
            return this;
        }
    }

    private JList<Account> createList(Game g){
        DefaultListModel<Account> l1 = new DefaultListModel<>();
        for(int i=0;i<g.jucatori.size();i++){
            l1.addElement(g.jucatori.get(i));
        }
        JList<Account> list = new JList<Account>(l1);
        list.setCellRenderer(new AccountRenderer());
        return list;
    }

    public CreateLoginForm(Game g)
    {
        userLabel = new JLabel();
        userLabel.setText("Username");
        passLabel = new JLabel();
        passLabel.setText("Password");
        textField2 = new JPasswordField(10);
        b1 = new JButton("SUBMIT");
        b1.addActionListener(this);
        list = createList(g);
        scrollPane = new JScrollPane(list);
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("LOGIN FORM");
        frame.setVisible(true);
        newPanel = new JPanel(new GridLayout(4, 2));
        newPanel.setVisible(true);
        userLabel.setVerticalAlignment(JLabel.CENTER);
        userLabel.setHorizontalAlignment(JLabel.CENTER);
        newPanel.add(userLabel,BorderLayout.CENTER);
        newPanel.add(scrollPane);
        newPanel.add(passLabel);
        passLabel.setVerticalAlignment(JLabel.CENTER);
        passLabel.setHorizontalAlignment(JLabel.CENTER);
        newPanel.add(textField2);
        newPanel.add(b1);
        frame.add(newPanel,BorderLayout.CENTER);
    }


    public void actionPerformed(ActionEvent ae) {
        Account user = list.getSelectedValue();
        String userValue = user.info.getEmail();
        if(ae.getSource()==b1) {
            frame.dispose();
            NewPage page = new NewPage(user);
            page.setVisible(true);
            JLabel wel_label = new JLabel("Welcome: " + userValue);
            page.getContentPane().add(wel_label,BorderLayout.NORTH);
        }
    }

}
/*Clasa care afiseaza pagina cu caracterele
unui anumit cont inainte de inceperea jocului*/
class NewPage extends JFrame
{
    JList<Character> listaCharacter = new JList<>();
    private JList<Character> createCharList(Account user){
        DefaultListModel<Character> l2 = new DefaultListModel<Character>();
        for(int i=0;i<user.chars.size();i++){
            l2.addElement(user.chars.get(i));
        }
        JList<Character> lista = new JList<Character>(l2);
        lista.setCellRenderer(new CharacterRenderer());
        return lista;
    }

    public NewPage(Account user)
    {
        JPanel panel = new JPanel(new GridLayout(4,1));
        JCheckBox check1, check2;
        listaCharacter = createCharList(user);
        JScrollPane scroll = new JScrollPane(listaCharacter);
        JButton buton = new JButton("SELECT");
        check1 = new JCheckBox("Terminal");
        check2 = new JCheckBox("Graphic");
        buton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game g = Game.getInstance();
                g.readFromJsonFile();
                int i=0;
                Character selected = listaCharacter.getSelectedValue();
                /*Daca se alege variante de joc in terminal, se creeaza
                o noua instanta de joc in terminal*/
                if(check1.isSelected()){
                    dispose();
                    terminalGame terminal = new terminalGame(new Cell(0,0),selected,g);
                    terminal.printMap();
                    /*Facem mutari pana la finalul jocului*/
                    while (!(terminal.grid.current_cell.state.toString().equals("FINISH"))) {
                        terminal.printStory(i);
                        terminal.updateMap(terminal.grid.current_cell);
                        new Mutare(terminal,terminal.grid);
                        terminal.printMap();
                        i++;
                        if(i>=g.stories.get(terminal.grid.current_cell.state).size())
                            i=0;
                    }
                    /*Daca am ajuns la final, afisam o fereastra de final*/
                    if(terminal.grid.current_cell.state.toString().equals("FINISH")){
                        terminal.printStory(i);
                        new finalScreen(terminal.expGained,terminal.enemiesKilled,terminal.coinsEarned,terminal.grid.current);
                    }
                }
                /*Daca este selectata optiunea de joc grafic, se creeaza
                o noua instanta de joc grafic*/
                else if(check2.isSelected()){
                    dispose();
                    graphicGame graphic = new graphicGame(new Cell(0,0),selected,g);
                    graphic.printMap();
                    new mutareGraphic(graphic);
                    graphic.updateMap();
                }
            }
        });
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Welcome");
        setSize(600, 500);
        panel.add(scroll);
        panel.add(check1);
        panel.add(check2);
        panel.add(buton);
        panel.setVisible(true);
        add(panel,BorderLayout.CENTER);
    }
}
//Clasa care modeleaza fereastra de final a jocului
class finalScreen implements ActionListener {
    JButton button;
    JPanel panel;
    JFrame frame;
    JLabel expLabel, levelLabel, enemiesLabel, coinsLabel;
    /*Afisam datele destre evolutia jucatorului, precum
    cati inamici a infrant, experienta adunata, nivelul
    la care a ajuns si banii adunati*/
    public finalScreen(int expGained, int enemiesKilled,int coinsEarned, Character character){
        expLabel = new JLabel();
        expLabel.setText("Experience gained: " + expGained);
        levelLabel = new JLabel();
        levelLabel.setText("Level: " + character.level);
        enemiesLabel = new JLabel();
        enemiesLabel.setText("Enemies killed: " + enemiesKilled);
        coinsLabel = new JLabel();
        coinsLabel.setText("Coins gathered: " + coinsEarned);
        button = new JButton("Close");
        button.addActionListener(this);
        panel = new JPanel(new GridLayout(4, 2));
        panel.add(expLabel);
        panel.add(levelLabel);
        panel.add(enemiesLabel);
        panel.add(coinsLabel);
        panel.add(button);
        panel.setVisible(true);
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("END");
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button)
            frame.dispose();
    }
}

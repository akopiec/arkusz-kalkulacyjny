

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
import java.util.List;

public class ArkuszKalkulacyjnyFrame extends JFrame {

    private static final int WIDTH=1500;

    private static final int HEIGHT=700;

    Priority priority=new Priority();

    JPanel panel1=new JPanel();

    JPanel panel2=new JPanel();

    JButton bold=new JButton("wyróżnji");

    JComboBox<String> background=new JComboBox<>();

    JButton execute=new JButton("wykonaj polecenie");

    JTextField mainCell=new JTextField(50);

    List<JTextField> listWithActualCell=new ArrayList<>();

    Element elementBackground=new Element("przyciskTlo",background);

    Element elementExecute=new Element("przyciskWykonaj",execute);

    Element elementMain=new Element("glówny",mainCell);

    Element elementBold=new Element("wyróżnji",bold);

    List<String> listOfToken=new ArrayList<>();

    List<Integer> listToSort=new ArrayList<>();

    Map<String,JTextField> map=new HashMap<>();

    Component element;

    int codeOfElement;

    boolean isError;

    String errorText;

    List<String> listOfCell=new ArrayList<>();

    String nameOfColumns[]={"A","B","C","D","E","F","G","H","I","J","K","L"};

    public boolean isNumber(String element) {

        try {

            int number = Integer.parseInt(element);

        } catch (NumberFormatException n) {

            return false;
        }
        return true;
    }

    public LinkedList<String> textToQueue(String text) {

        LinkedList<String> queue = new LinkedList<>();

        StringTokenizer stringTokenizer = new StringTokenizer(text, " ");

        while (stringTokenizer.hasMoreTokens()) {

            queue.add(stringTokenizer.nextToken());
        }
        return queue;
    }

    public int max(String text){

        int max;

        listOfToken.clear();

        StringTokenizer tokenizer=new StringTokenizer(text," ");

        while (tokenizer.hasMoreTokens()){

            listOfToken.add(tokenizer.nextToken());

        }

        String cell1=listOfToken.get(1);

        max=Integer.parseInt(map.get(cell1).getText());

        String column1=cell1.substring(0,1);

        String row1=cell1.substring(1,2);

        int row1Int=Integer.parseInt(row1);

        String cell2=listOfToken.get(2);

        String column2=cell2.substring(0,1);

        String row2=cell2.substring(1,2);

        int row2Int=Integer.parseInt(row2);

        if(column1.equals(column2)){

            for(int i=row1Int;i<row2Int+1;i++){

                if(Integer.parseInt(map.get(column1+i).getText())>max){

                    max=Integer.parseInt(map.get(column1+i).getText());
                }
            }
        }
        return max;
    }

    public void sort(String text){

        listOfToken.clear();

        StringTokenizer tokenizer=new StringTokenizer(text," ");

        while(tokenizer.hasMoreElements()){

            listOfToken.add(tokenizer.nextToken());

        }

        String cell1 = listOfToken.get(1);

        String cell2 = listOfToken.get(2);

        String kolumna = cell1.substring(0, 1);

        String row1 = cell1.substring(1, 2);

        int row1Int = Integer.parseInt(row1);

        String row2 = cell2.substring(1, 2);

        int row2Int = Integer.parseInt(row2);

        listOfCell.clear();

        for (int i = row1Int; i < row2Int + 1; i++){

            listOfCell.add("" + kolumna + i);

        }

        listToSort.clear();

        for(String s :listOfCell){

            String column = s.substring(0, 1);

            int row = Integer.parseInt(s.substring(1, 2));

            JTextField jTextField = map.get(""+column+row);

            listToSort.add(Integer.parseInt(jTextField.getText()));

        }

        Collections.sort(listToSort);

        int i=0;

        for(String cell :listOfCell){

            map.get(cell).setText(""+listToSort.get(i)); i=i+1;
        }
    }

    public LinkedList<String> queueToONP(LinkedList<String> entryQueue) {

        codeOfElement=0;

        isError=false;

        LinkedList<String> stack = new LinkedList<>();

        LinkedList<String> outputQueue = new LinkedList<>();

        String element;

        while (entryQueue.size() > 0 && isError==false) {

            element = entryQueue.poll();

            if (element.equals("(")) {

                stack.addLast(element);

            }
            else if (element.equals("+") || element.equals("-") || element.equals("*") || element.equals("/") || element.equals("^")) {

                if (codeOfElement == 0) {

                    isError = true;

                    errorText = "nie można zaczynać od operatora";

                }
                else if (codeOfElement == 2) {

                    isError = true;

                    errorText = "nie można stawiać operatora jeden po drugim";

                }
                else if (codeOfElement == 1) {

                    codeOfElement = 2;
                }

                String topOfStack = stack.peekLast();

                while (topOfStack != null && priority.returnPriority(element) <= priority.returnPriority(topOfStack)) {

                    String toQueue = stack.pollLast();

                    outputQueue.addLast(toQueue);

                    topOfStack = stack.peekLast();

                }
                stack.addLast(element);

            }
            else if (element.equals(")")) {

                String operator = stack.pollLast();

                outputQueue.addLast(operator);

                String operator2 = stack.peekLast();

                while (!operator2.equals("(")) {

                    operator = stack.pollLast();

                    outputQueue.addLast(operator);

                    operator2 = stack.peekLast();
                }
                stack.pollLast();

            }
            else if (isNumber(map.get(element).getText())) {

                if (codeOfElement == 0) {

                    codeOfElement = 1;

                }
                else if (codeOfElement == 2) {

                    codeOfElement = 1;

                }
                else if (codeOfElement == 1) {

                    isError = true;

                    errorText = "nie można postawić liczby po liczbie";

                }
                else if (codeOfElement == 3) {

                    codeOfElement = 2;
                }
                outputQueue.add(element);

            }
            else    {

                errorText=" element "+element+" nie jest prawidłowy";

                isError=true;
            }
        }

        while (!stack.isEmpty()) {

            String emptyStack = stack.pollLast();

            outputQueue.addLast(emptyStack);
        }

        return outputQueue;
    }

    public double fromOnpToResult(LinkedList<String> entryQueue){

        LinkedList<Double> stack = new LinkedList<>();

        double secondaryResult;

        while(!entryQueue.isEmpty()){

            String element = entryQueue.pollFirst();

            if(!element.equals("+") && !element.equals("-") && !element.equals("*") && !element.equals("/")){

                double elementDou=Double.parseDouble(map.get(element).getText());

                stack.addLast(elementDou);
            }

            else {

                double a = stack.pollLast();

                double b = stack.pollLast();

                if(element.equals("+")){

                    secondaryResult =  b+a;

                }

                else if(element.equals("-")){

                    secondaryResult =  b -a;

                }

                else if(element.equals("*")){

                    secondaryResult =  b*a;

                }

                else if(element.equals("/")){

                    secondaryResult =  b/a;

                }
                else {

                    secondaryResult = Math.pow(b, a);
                }

                stack.addLast(secondaryResult);
            }
        }

        double result = stack.peek();

        return result;
    }

    public  class Element implements  FocusListener, KeyListener, ActionListener {

        String nameOfComponent;

        Component component;

        Element(String name,Component componentN){

            nameOfComponent=name;

            component=componentN;
        }
        @Override
        public void keyPressed(KeyEvent e) {

        }
        @Override
        public void keyReleased(KeyEvent e) {

            String text=listWithActualCell.get(0).getText();
            
            mainCell.setText(text);

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            String text=listWithActualCell.get(0).getText();

            mainCell.setText(text);

            String command=actionEvent.getActionCommand();

            if(command.equals("wykonaj")){

                String textToExecute = mainCell.getText();

                StringTokenizer tokenizer = new StringTokenizer(textToExecute, " ");

                String nextToken = tokenizer.nextToken();

                if (nextToken.equals("max")){

                    int max = max(textToExecute);

                    listWithActualCell.get(0).setText("" + max);

                }
                else if (nextToken.equals("sort")){

                    sort(textToExecute);
                }

                else {

                    LinkedList<String> list = textToQueue(textToExecute);

                    list.remove(0);

                    LinkedList<String> entryQueue = queueToONP(list);

                    if (isError) {

                        mainCell.setText(errorText);

                        listWithActualCell.get(0).setText(errorText);
                    }
                    else {

                        double result = fromOnpToResult(entryQueue);

                        listWithActualCell.get(0).setText("" + result);
                    }
                }
            }

            if(command.equals("tlo")){

                if(background.getSelectedItem().equals("tlo żólte")){

                     listWithActualCell.get(0).setBackground(Color.YELLOW);

                }

                else if(background.getSelectedItem().equals("tlo szare")){

                     listWithActualCell.get(0).setBackground(Color.GRAY);

                }

                else if(background.getSelectedItem().equals("tlo zielone")){

                     listWithActualCell.get(0).setBackground(Color.GREEN);

                }

                else {
                     listWithActualCell.get(0).setBackground(Color.WHITE);
                }
            }

            if(command.equals("wyroznienie")){

                Font font=listWithActualCell.get(0).getFont();

                if(font.isBold()){

                    listWithActualCell.get(0).setFont(new Font("SansSerif",Font.PLAIN,14));

                }

                else {listWithActualCell.get(0).setFont(new Font("SansSerif",Font.BOLD,14));

                }
            }
        }
        @Override
        public void focusGained(FocusEvent focusEvent){

             element=(Component) focusEvent.getComponent();

             if(element instanceof JTextField) {

                 if (listWithActualCell.isEmpty()) {

                     listWithActualCell.add((JTextField) element);

                 }
                 else {

                     listWithActualCell.remove(0);

                     listWithActualCell.add((JTextField) element);
                 }
             }

             String text=listWithActualCell.get(0).getText();

             mainCell.setText(text);
        }
        @Override
        public void focusLost(FocusEvent focusEvent){

        }
    }
    public void showCells(){

        for (int n = 0; n < nameOfColumns.length; n++) {

            if (n == 0) {

                JTextField lp = new JTextField("", 3);

                lp.setEditable(false);

                panel1.add(lp);
            }

            JTextField name = new JTextField("" + nameOfColumns[n], 10);

            name.setName("" + n);

            name.setEditable(false);

            name.setBackground(Color.YELLOW);

            panel1.add(name);
        }
        for (int i = 1; i < 8; i++) {

            for (int j = 1; j < 14; j++) {

                if (j == 1) {

                    JTextField lp = new JTextField("" + i, 3);

                    lp.setName("lp" + i);

                    panel1.add(lp);
                }
                else {

                    JTextField name = new JTextField(10);

                    name.setName("" + nameOfColumns[j - 2] + i);

                    Element p = new Element(name.getName(), name);

                    name.addFocusListener(p);

                    name.addActionListener(p);

                    name.addKeyListener(p);

                    map.put("" + nameOfColumns[j - 2] + i, name);

                    panel1.add(name);
                }
            }
        }
    }

    public ArkuszKalkulacyjnyFrame(){

        showCells();

        background.addItem("tlo żólte");

        background.addItem("tlo szare");

        background.addItem("tlo zielone");

        background.addItem("brak tla");

        panel2.add(bold);

        panel2.add(background);

        panel2.add(execute);

        panel2.add(mainCell);

        add(panel2,BorderLayout.NORTH);

        add(panel1,BorderLayout.CENTER);

        setSize(WIDTH,HEIGHT);

        background.addActionListener(elementBackground);

        background.addFocusListener(elementBackground);

        execute.addActionListener(elementExecute);

        execute.setActionCommand("wykonaj");

        background.setActionCommand("tlo");

        bold.setActionCommand("wyroznienie");

        bold.addActionListener(elementBold);

        mainCell.addActionListener(elementMain);

    }
}



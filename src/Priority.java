

public class Priority {

    int priority;

    public int returnPriority(String operator){

        switch (operator){
            case "(":
                priority=0;
                break;
            case "+":
                priority=1;
                break;
            case "-":
                priority=1;
                break;
            case ")":
                priority=1;
                break;
            case "*":
                priority=2;
                break;
            case "/":
                priority=2;
                break;
            case "":
                priority=0;
                break;
            case "^":
                priority=3;
                break;

        }
        return priority;
    }
}

package mealplanner;
import java.util.Scanner;

import static mealplanner.Manager.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CRUDUtils.createTable();

        while (true) {
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            switch (scanner.nextLine()) {
                case "add" -> add();
                case "show" -> show();
                case "plan" -> plan();
                case "save" -> save();
                case "exit" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> {
                }
            }
        }
    }
}
package mealplanner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Manager {
    public static void add() {
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String category = inputCategory();
        System.out.println("Input the meal's name:");
        String name = inputName();
        System.out.println("Input the ingredients:");
        String ingredients = inputIngredients();
        Meal meal = new Meal(category, name, ingredients);
        CRUDUtils.setMealData(meal);
        System.out.println("The meal has been added!");
    }

    public static void show() {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = inputCategory();
        List<Meal> meals = CRUDUtils.getMealDataByCategory(category);

        if (!meals.isEmpty()) {
            System.out.println("Category: " + category);
            meals.forEach(meal -> {
                String[] words = meal.getIngredients().split(",");
                System.out.println("Name: " + meal.getName() + "\nIngredients: " + String.join("\n", words));
            });
        } else {
            System.out.println("No meals found.");
        }
    }

    public static void plan() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] categoryMeal = {"breakfast", "lunch", "dinner"};

        for (String day : daysOfWeek) {
            System.out.println(day);
            for (String category : categoryMeal) {
                //1 block
                //check if dishes for this category are in the database
                List<Meal> meals = CRUDUtils.getMealDataByCategory(category);
                if (!meals.isEmpty()) {
                    meals.sort(Comparator.comparing(Meal::getName));
                    meals.forEach(meal -> System.out.println(meal.getName()));
                } else {
                    System.out.println("No meals found.");
                }

                //2 block
                //check correct input
                System.out.println("Choose the " + category + " for " + day + " from the list above:");
                meals = mealForPlan();

                //3 block
                Meal meal = meals.get(0);
                Plan plan = new Plan(day, meal.getName(), meal.getCategory(), meal.getId());
                CRUDUtils.setPlanData(plan);
            }
            System.out.println("Yeah! We planned the meals for " + day + ".\n");
        }

        for (String day : daysOfWeek) {
            System.out.println(day);
            for (String category : categoryMeal) {
                List<Plan> plans = CRUDUtils.getPlanDataByDay(day, category);

                if (plans.isEmpty()) {
                    System.out.println("No meals found.");
                    return;
                }

                Plan plan = plans.get(0);
                System.out.println(category + ": " + plan.getOption());
            }
            System.out.println(" ");
        }
    }

    public static void save() {
        List<String> ingredients = CRUDUtils.getIngredientsByPlan();

        if (ingredients.isEmpty()) {
            System.out.println("Unable to save. Plan your meals first.");
            return;
        }

        Map<String, Integer> shopList = new HashMap<>();

        ingredients.forEach(i -> {
            if (shopList.containsKey(i)) {
                shopList.put(i, shopList.get(i) + 1);
            } else {
                shopList.put(i, 1);
            }
        });

        Scanner scanner = new Scanner(System.in);
        System.out.println("Input a filename:");
        String fileName = scanner.nextLine();

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            for (Map.Entry<String, Integer> entry : shopList.entrySet()) {
                if (entry.getValue() > 1) {
                    fileWriter.write(entry.getKey() + " x" + entry.getValue() + "\n");
                }else {
                    fileWriter.write(entry.getKey() + "\n");
                }
            }
            fileWriter.close();
            System.out.println("Saved!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //methods for plan

    /**
     * checks if there is meal in the database and if the name of the meal is entered correctly
     */
    public static List<Meal> mealForPlan() {
        String name = inputName();

        //check if meal is in the db
        List<Meal> meals = CRUDUtils.getMealDataByName(name);
        while (meals.isEmpty()) {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            name = inputName();
            meals = CRUDUtils.getMealDataByName(name);
        }
        return meals;
    }

    //general methods
    public static String inputCategory() {
        Scanner scanner = new Scanner(System.in);
        //choose category
        String category = scanner.nextLine();
        while (!category.equalsIgnoreCase("lunch") && !category.equalsIgnoreCase("breakfast") && !category.equalsIgnoreCase("dinner")) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }
        return category;
    }

    public static String inputName() {
        Scanner scanner = new Scanner(System.in);
        //enter meals' name
        String name = scanner.nextLine();
        while (!name.matches("[a-zA-Z ]+")) {
            System.out.println("Wrong format. Use letters only!");
            name = scanner.nextLine();
        }
        return name;
    }

    public static String inputIngredients() {
        Scanner scanner = new Scanner(System.in);
        //enter meals' ingredients
        String ingredients = scanner.nextLine();
        while (!(ingredients.matches("^[a-zA-Z ]+(, [a-zA-Z ]+)*$") || ingredients.matches("^[a-zA-Z ]+(,[a-zA-Z ]+)*$"))) {
            System.out.println("Wrong format. Use letters only!");
            ingredients = scanner.nextLine();
        }
        return ingredients;
    }
}

package mealplanner;

public class Meal {
    private int id;
    private String category;
    private String name;
    private String ingredients;

    public Meal(int id, String category, String name) {
        this.id = id;
        this.category = category;
        this.name = name;
    }

    public Meal(String category, String name, String ingredients) {
        this.category = category;
        this.name = name;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }

    public String print() {
        String[] words = getIngredients().split(",\\s*");
        return String.format("Category: %s\nName: %s\nIngredients: \n%s\n", category, name, String.join("\n", words));
    }
}
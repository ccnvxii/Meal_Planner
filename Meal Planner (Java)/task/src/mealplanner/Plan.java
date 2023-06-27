package mealplanner;

public class Plan {
    private String day;
    private String option;
    private String category;
    private int plan_id;
    private int meal_id;

    public Plan(String day, String option, String category, int meal_id) {
        this.day = day;
        this.option = option;
        this.category = category;
        this.meal_id = meal_id;
    }

    public Plan(String day, String option, String category, int plan_id, int meal_id) {
        this.day = day;
        this.option = option;
        this.category = category;
        this.plan_id = plan_id;
        this.meal_id = meal_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public int getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(int meal_id) {
        this.meal_id = meal_id;
    }
}

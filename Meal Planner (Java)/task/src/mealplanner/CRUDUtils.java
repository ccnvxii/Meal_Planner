package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CRUDUtils {
    private static final String INSERT_MEAL = "insert into meals (meal_id, category, meal) values (?, ?, ?)";
    private static final String INSERT_INGREDIENTS = "insert into ingredients (ingredient, ingredient_id, meal_id) values (?,?,?)";
    private static final String INSERT_PLAN = "insert into plan (day, option, category, meal_id) values (?,?,?,?)";
    private static final String SELECT_MEAL_BY_CATEGORY = "SELECT * FROM meals JOIN ingredients i on meals.meal_id = i.meal_id WHERE category = ?";
    private static final String SELECT_MEAL_BY_NAME = "SELECT * FROM meals WHERE meal = ?";
    private static final String SELECT_MAX_ID_MEAL = "SELECT MAX(meal_id) FROM meals";
    private static final String SELECT_MAX_ID_INGREDIENTS = "SELECT MAX(ingredient_id) FROM ingredients";

    public static void createTable() {
        try (Connection connection = DBUtils.getConnection(); Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals (category VARCHAR(20), meal VARCHAR(30), meal_id INTEGER PRIMARY KEY )");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients(ingredient VARCHAR(60), ingredient_id INTEGER PRIMARY KEY , meal_id INTEGER REFERENCES meals (meal_id)  ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS plan(day VARCHAR(30),option VARCHAR(30), category VARCHAR(20), plan_id SERIAL PRIMARY KEY , meal_id INTEGER REFERENCES meals (meal_id)  ON DELETE CASCADE)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Meal> getMealDataByCategory(String category) {
        List<Meal> meals = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MEAL_BY_CATEGORY)
        ) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            //while collection isn`t empty
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("meal");
                    String ingredient = resultSet.getString("ingredient");
                    meals.add(new Meal(category, name, ingredient));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meals;
    }

    public static List<Meal> getMealDataByName(String name) {
        List<Meal> meals = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MEAL_BY_NAME)
        ) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            //while collection isn`t empty
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("meal_id");
                    name = resultSet.getString("meal");
                    String category = resultSet.getString("category");
                    meals.add(new Meal(id, category, name));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meals;
    }
    public static List<Plan> getPlanDataByDay(String day, String category){
        List<Plan> plans = new ArrayList<>();

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM plan WHERE day = ? AND category = ?");
        ) {
            preparedStatement.setString(1, day);
            preparedStatement.setString(2, category);
            ResultSet resultSet = preparedStatement.executeQuery();

            //while collection isn`t empty
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    int plan_id = resultSet.getInt("plan_id");
                    int meal_id = resultSet.getInt("meal_id");
                    String option = resultSet.getString("option");
                    plans.add(new Plan(day, option, category, plan_id, meal_id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return plans;
    }

    public static List<String> getIngredientsByPlan(){
        List<String> ingredientList = new ArrayList<>();
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT ingredient FROM ingredients i JOIN plan p ON p.meal_id = i.meal_id");
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            //while collection isn`t empty
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    String ingredients = resultSet.getString("ingredient");
                    String[] ingredientArray= ingredients.split(",\\s*");
                    ingredientList.addAll(Arrays.asList(ingredientArray));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredientList;
    }

    public static void setMealData(Meal meal) {
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement mealPrepareStatement = connection.prepareStatement(INSERT_MEAL);
             PreparedStatement ingredientsPrepareStatement = connection.prepareStatement(INSERT_INGREDIENTS)
        ) {
            int meal_id = getNextId(SELECT_MAX_ID_MEAL);
            mealPrepareStatement.setInt(1, meal_id);
            mealPrepareStatement.setString(2, meal.getCategory());
            mealPrepareStatement.setString(3, meal.getName());
            mealPrepareStatement.executeUpdate();

            ingredientsPrepareStatement.setString(1, meal.getIngredients());
            ingredientsPrepareStatement.setInt(2, getNextId(SELECT_MAX_ID_INGREDIENTS));
            ingredientsPrepareStatement.setInt(3, meal_id);
            ingredientsPrepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setPlanData(Plan plan) {
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement planPrepareStatement = connection.prepareStatement(INSERT_PLAN)
        ) {
            planPrepareStatement.setString(1, plan.getDay());
            planPrepareStatement.setString(2, plan.getOption());
            planPrepareStatement.setString(3, plan.getCategory());
            planPrepareStatement.setInt(4, plan.getMeal_id());
            planPrepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getNextId(String sql) {
        int ID;
        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            ID = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ID + 1;
    }
}

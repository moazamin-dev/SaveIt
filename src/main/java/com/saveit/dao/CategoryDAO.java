package com.saveit.dao;

import com.saveit.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @brief Data Access Object for Category entities.
 *
 * This class provides implementation for managing Category records in the database,
 * allowing for creation, retrieval, deletion, and resolution of categories based on
 * names or IDs for specific users.
 */
public class CategoryDAO implements DAO<Category> {

    /** @var Connection connection The active database connection instance */
    private Connection connection;

    /**
     * @brief Constructor for CategoryDAO.
     *
     * Initializes the database connection using the DatabaseConnection singleton.
     */
    public CategoryDAO(){this.connection = DatabaseConnection.getInstance().getConnection();}

    /**
     * @brief Persists a new Category object to the database.
     *
     * @param c The Category object to be saved.
     */
    @Override
    public void save(Category c) {
        String Query = "INSERT INTO Category(category_name,user_id) VALUES(?,?)";
        try(PreparedStatement save = connection.prepareStatement(Query)){
            save.setString(1, c.getName());
            save.setInt(2,c.getUser_id());

            save.executeUpdate();
            System.out.println("category added successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error while saving category: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * @brief Retrieves all categories belonging to a specific user.
     *
     * @param id The ID of the user whose categories are being fetched.
     * @return List<Category> A list of Category objects associated with the user.
     */
    @Override
    public List<Category> getAll(int id) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category " +
                "WHERE user_id = ?";

        try (PreparedStatement getAll = connection.prepareStatement(sql)) {
            getAll.setInt(1, id);

            try (ResultSet rs = getAll.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category();
                    c.setCategoryID(rs.getInt("id"));
                    c.setName(rs.getString("category_name"));
                    c.setUser_id(rs.getInt("user_id"));
                    categories.add(c);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while finding categories: " + ex.getMessage());
            ex.printStackTrace();
        }
        return categories;
    }

    /**
     * @brief Deletes a category record from the database by its unique identifier.
     *
     * @param id The unique ID of the category to be removed.
     */
    @Override
    public void delete(int id) {
        String Query = "DELETE FROM Category WHERE id = ?";
        try(PreparedStatement delete = connection.prepareStatement(Query)){

            delete.setInt(1,id);

            delete.executeUpdate();
            System.out.println("category deleted successfully.");

        } catch (SQLException ex) {
            System.err.println("Database Error when deleting category: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * @brief Finds a specific Category object based on the category ID and user ID.
     *
     * @param user_id The ID of the user who owns the category.
     * @param categoryID The unique ID of the category.
     * @return Category The populated Category object, or an empty Category object if not found.
     */
    public Category resolveID(int user_id , int categoryID){
        Category category = new Category();
        String sql = "SELECT * FROM Category " +
                "WHERE id = ? AND user_id = ?";

        try (PreparedStatement getAll = connection.prepareStatement(sql)) {
            getAll.setInt(1, categoryID);
            getAll.setInt(2,user_id);

            try (ResultSet rs = getAll.executeQuery()) {
                while (rs.next()) {
                    category.setCategoryID(rs.getInt("id"));
                    category.setName(rs.getString("category_name"));
                    category.setUser_id(rs.getInt("user_id"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while resolving categoryID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return category;
    }

    /**
     * @brief Resolves a category by its name for a specific user.
     *
     * If the category name does not exist for the specified user, it automatically
     * creates and saves a new category with that name and returns it.
     *
     * @param user_id The ID of the user.
     * @param Name The name of the category to find or create.
     * @return Category The found or newly created Category object.
     */
    public Category resolveName(int user_id , String Name){
        Category category = new Category();
        String sql = "SELECT * FROM Category " +
                "WHERE category_name = ? AND user_id = ?";

        try (PreparedStatement getAll = connection.prepareStatement(sql)) {
            getAll.setString(1, Name);
            getAll.setInt(2,user_id);

            try (ResultSet rs = getAll.executeQuery()) {
                if (rs.next()) {
                    category = new Category();
                    category.setCategoryID(rs.getInt("id"));
                    category.setName(rs.getString("category_name"));
                    category.setUser_id(rs.getInt("user_id"));
                } else {
                    Category newCat = new Category(Name, user_id);
                    save(newCat);
                    return resolveName(user_id, Name);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Database Error while resolving categoryName: " + ex.getMessage());
            ex.printStackTrace();
        }
        return category;
    }
}
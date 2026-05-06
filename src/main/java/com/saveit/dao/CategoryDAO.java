package com.saveit.dao;

import com.saveit.model.Category;
import com.saveit.model.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements DAO<Category> {

    private Connection connection;

    public CategoryDAO(){this.connection = DatabaseConnection.getInstance().getConnection();}

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

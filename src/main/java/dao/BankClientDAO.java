package dao;

import com.sun.deploy.util.SessionState;
import model.BankClient;
import servlet.ResultServlet;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() {
        List<BankClient> list = new ArrayList<BankClient>();
        String select = "select * from bank_client";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (!resultSet.isLast()){
                resultSet.next();
                list.add(new BankClient(resultSet.getInt("id") , resultSet.getString("name"), resultSet.getString("password"), resultSet.getLong("money")));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean validateClient(String name, String password) {

        String query = "select * from bank_client where name = '" + name + "' AND password = '" + password + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.isLast()){
                statement.close();
                return true;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updateClientsMoney(String name, String password, Long transactValue) {
        BankClient client = getClientByName(name);
        Long mon = client.getMoney();
        mon = mon + transactValue;
        String update = "update bank_client set money = '" + mon + "' where name = '" + name + "' and password = '" + password + "'";
        try {
            Statement statement = connection.createStatement();
            statement.execute(update);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public BankClient getClientById(long id) throws SQLException {
        String query = "select * from bank_client where id = '" + id + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.isLast()) {
                resultSet.next();
                BankClient bankClient = new BankClient(resultSet.getInt("id") , resultSet.getString("name"), resultSet.getString("password"), resultSet.getLong("money"));
                resultSet.close();
                return bankClient;
            }
            statement.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isClientHasSum(String name, Long expectedSum) {

        String query = "select money from bank_client where id = '" + name + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.isLast()) {
                resultSet.next();
                Long money =  resultSet.getLong("money");
                resultSet.close();
                return money > expectedSum ? true : false;
            }
            statement.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_client where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) {
        String query = "select * from bank_client where name = '" + name + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.isLast()) {
                resultSet.next();
                BankClient bankClient = new BankClient(resultSet.getInt("id") , resultSet.getString("name"), resultSet.getString("password"), resultSet.getLong("money"));
                resultSet.close();
                return bankClient;
            }
            statement.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addClient(BankClient client) throws SQLException {

        try {
            Statement statement = connection.createStatement();
            String update = "insert into bank_client (name, password, money) values ('" + client.getName() +"', '" + client.getPassword() +"', '" + client.getMoney() + "')";
            statement.execute(update);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }

    public boolean removeClient (String name){
        try {
            Statement statement = connection.createStatement();
            String update = "delete from bank_client where name =  ('" + name +"')";
            statement.execute(update);
            boolean t = statement.getUpdateCount() > 0;
            statement.close();
            return t;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

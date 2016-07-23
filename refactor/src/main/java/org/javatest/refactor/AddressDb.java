package org.javatest.refactor;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddressDb {
    private static final String JDBC_URL = "jdbc:oracle:thin:@prod";
    private static final String JDBC_USER = "admin";
    private static final String JDBC_PASS = "beefhead";
    private static final String SELECT_ADDRESS_ENTRY = "select * from AddressEntry";
    private static final String SELECT_ADDRESS_ENTRY_BY_NAME = "select * from AddressEntry where name = '%s'";
    private static final String ORACLE_DRIVER = "oracle.jdbc.ThinDriver";
    public static final String INSERT_ADDRESS_ENTRY = "insert into AddressEntry values (?, ?, ?)";
    private int CACHE_SIZE = 50;
    private int THREAD_POOL_SIZE = 18;
    private Executor executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private Connection connection;

    public AddressDb() {
        try {
            Class.forName(ORACLE_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        new Thread().start();
    }

    private Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) return connection;
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void addPerson(Person person) {
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(INSERT_ADDRESS_ENTRY);
            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, person.getName());
            statement.setString(3, person.getPhoneNumber().getNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException err) {
                    throw new RuntimeException(err);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    throw new RuntimeException(err);
                }
            }
        }
    }

    /**
     * Looks up the given person, null if not found.
     */
    public Person findPerson(String name) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(String.format(SELECT_ADDRESS_ENTRY_BY_NAME, name));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String foundName = resultSet.getString("name");
                PhoneNumber phoneNumber = new PhoneNumber(resultSet.getString("phoneNumber"));
                Person person = new Person(foundName, phoneNumber);
                return person;
            } else {
                return new Person("", null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Person> getAll() {
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = getConnection().prepareStatement(SELECT_ADDRESS_ENTRY);
            HashSet persons = new HashSet();

            result = statement.executeQuery();
            List<Person> entries = new LinkedList<Person>();
            while (result.next()) {
                String name = result.getString("name");
                PhoneNumber phoneNumber = new PhoneNumber(result.getString("phoneNumber"));
                Person person = new Person(name, phoneNumber);
                entries.add(person);
                persons.add(person);
            }
            return entries;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException err) {
                    throw new RuntimeException(err);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException err) {
                    throw new RuntimeException(err);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException err) {
                    throw new RuntimeException(err);
                }
            }
        }
    }


}

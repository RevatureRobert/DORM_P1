package DB.ConnectionPool;

import Models.Database;
import Threads.MakeThreadPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

// Using the Baeldung implementation
// https://www.baeldung.com/java-connection-pooling


// Maybe in the future instead of throwing a runtime expection when max size hit i could implment a queue to store all the connections that
// want to get a connection I could do alot of fancy stuff but lord have mercy
public class BasicConnPool implements ConnectionPool2 {

    private static final int MAX_POOL_SIZE = 10;
    private static final int MAX_TIMEOUT = 10;
    private String url;
    private String user;
    private String password;
    private String classforName;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static int INITIAL_POOL_SIZE = 5;

    public BasicConnPool(String url, String user, String password, List<Connection> pool , String classforNAme) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.connectionPool = pool;
        this.classforName = classforNAme;
    }

    public static BasicConnPool create(
            String url, String user,
            String password , String classforName) throws SQLException {

        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(url, user, password ,classforName));
        }

        return new BasicConnPool(url, user, password, pool ,classforName);
    }

    // standard constructors

    @Override
    public Connection getConnection() {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                try {
                    connectionPool.add(createConnection(url, user, password,classforName));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);

        try {
            if(!connection.isValid(MAX_TIMEOUT)){
                try {
                    connection = createConnection(url, user, password,classforName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }
    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    private static Connection createConnection(String url, String user, String password ,String db) throws SQLException {
        Future future = MakeThreadPool.executorService.submit((Callable) () -> {
            Class.forName(db);
            return DriverManager.getConnection(url, user, password);
        });
        try {
            return (Connection) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("");
        return null;
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

}

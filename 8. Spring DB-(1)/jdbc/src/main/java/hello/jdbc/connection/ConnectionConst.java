package hello.jdbc.connection;

public abstract class ConnectionConst { //객체 생성 막기위해 추상
    public static final String URL = "jdbc:h2:tcp://localhost/~/jdbc";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}

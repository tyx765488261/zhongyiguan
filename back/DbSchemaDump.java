import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbSchemaDump {
    public static void main(String[] args) throws Exception {
        String url = System.getProperty("db.url", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        String username = System.getProperty("db.user", "root");
        String password = System.getProperty("db.pass", "123456");

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stTables = conn.createStatement();
             Statement stDdl = conn.createStatement()) {
            try (ResultSet rs = stTables.executeQuery("SHOW FULL TABLES WHERE Table_type = 'BASE TABLE'")) {
                while (rs.next()) {
                    String table = rs.getString(1);
                    System.out.println("===TABLE===" + table);
                    try (ResultSet ddl = stDdl.executeQuery("SHOW CREATE TABLE `" + table + "`")) {
                        if (ddl.next()) {
                            System.out.println(ddl.getString(2));
                        }
                    }
                    System.out.println();
                }
            }
        }
    }
}

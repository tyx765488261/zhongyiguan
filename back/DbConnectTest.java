import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbConnectTest {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";

        long start = System.currentTimeMillis();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            long cost = System.currentTimeMillis() - start;
            DatabaseMetaData meta = conn.getMetaData();
            System.out.println("DB_CONNECT_OK");
            System.out.println("costMs=" + cost);
            System.out.println("product=" + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
            System.out.println("driver=" + meta.getDriverName() + " " + meta.getDriverVersion());
            System.out.println("url=" + meta.getURL());
            System.out.println("user=" + meta.getUserName());

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT 1")) {
                if (rs.next()) {
                    System.out.println("select1=" + rs.getInt(1));
                }
            }
        } catch (Exception e) {
            System.out.println("DB_CONNECT_FAIL");
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }
    }
}


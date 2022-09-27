import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.fail;

public class DockerTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/pkdd99", "CinecubesUser", "Cinecubes");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from loan");
            while (rs.next())
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}

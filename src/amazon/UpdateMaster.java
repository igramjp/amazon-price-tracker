package amazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateMaster {
	private Connection con;
	private PreparedStatement ps;
	private String sql;

    UpdateMaster() {
    	con = null;
    	ps  = null;
    	sql = null;
    }

    public void database(Integer price, Integer points, String sku) {
    	sql = "UPDATE mws_config SET price = ?, points = ?, stamp = CURRENT_TIMESTAMP WHERE sku = ?";

    	try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(Construct.DB, Construct.USER, Construct.PASS);
			con.setAutoCommit(false);

			ps = con.prepareStatement(sql);
			ps.setInt(1, price);
            ps.setInt(2, points);
            ps.setString(3, sku);

			ps.executeUpdate();
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
            if (con != null) {
            	try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            }
		}
    }
}

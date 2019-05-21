package amazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateHistory {
	private Connection con;
	private PreparedStatement ps;
	private String sql;

	UpdateHistory() {
    	con = null;
    	ps  = null;
    	sql = null;
    }

    public void database(String sku, Integer price, Integer points, Integer base, Integer lower_price, Integer lower_points, Integer lower_base) {
    	if(sku == null || price == null || points == null){
    		return;
    	}
    	sql = "INSERT INTO mws_history (sku, price, points, base, lower_price, lower_points, lower_base, stamp) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(Construct.DB, Construct.USER, Construct.PASS);
			con.setAutoCommit(false);

			ps = con.prepareStatement(sql);
			ps.setString(1, sku);
			ps.setInt(2, price);
			ps.setInt(3, points);
			ps.setInt(4, base);
			if(lower_price != null){
				ps.setInt(5, lower_price);
				ps.setInt(6, lower_points);
				ps.setInt(7, lower_base);
			}else{
				ps.setNull(5, java.sql.Types.INTEGER);
				ps.setNull(6, java.sql.Types.INTEGER);
				ps.setNull(7, java.sql.Types.INTEGER);
			}

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
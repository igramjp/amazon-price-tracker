package amazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Configuration {
	private Connection con  = null;
	private Statement stmt  = null;
	private ResultSet rs    = null;
	private String sql;
	private Integer count;

	private List<String> skuList;
	private List<String> asinList;
	private List<Integer> costList;
	private List<Integer> powerList;
	private List<Integer> timesaleList;
	private List<Integer> fromList;
	private List<Integer> toList;
	private List<Integer> sunList;
	private List<Integer> monList;
	private List<Integer> tueList;
	private List<Integer> wedList;
	private List<Integer> thuList;
	private List<Integer> friList;
	private List<Integer> satList;
	private List<Integer> specialList;
	private List<Integer> upList;
	private List<Integer> downList;
	private List<Integer> sameList;
	private List<Integer> fixedList;
	private List<Integer> valuesList;
	private List<Integer> priceList;
	private List<Integer> pointsList;
	private List<Integer> selfcontainedList;
	private List<Integer> pt_upList;
	private List<Integer> pt_downList;
	private List<Integer> pt_sameList;
	private List<Integer> pt_fixedList;
	private List<Integer> pt_valuesList;
	private List<Integer> estore_priceList;
	private List<Integer> estore_pointsList;

	Configuration() {
		skuList      = new ArrayList<String>();
		asinList     = new ArrayList<String>();
		costList     = new ArrayList<Integer>();
		powerList    = new ArrayList<Integer>();
		timesaleList = new ArrayList<Integer>();
		fromList     = new ArrayList<Integer>();
		toList       = new ArrayList<Integer>();
		sunList      = new ArrayList<Integer>();
		monList      = new ArrayList<Integer>();
		tueList      = new ArrayList<Integer>();
		wedList      = new ArrayList<Integer>();
		thuList      = new ArrayList<Integer>();
		friList      = new ArrayList<Integer>();
		satList      = new ArrayList<Integer>();
		specialList  = new ArrayList<Integer>();
		upList       = new ArrayList<Integer>();
		downList     = new ArrayList<Integer>();
		sameList     = new ArrayList<Integer>();
		fixedList    = new ArrayList<Integer>();
		valuesList   = new ArrayList<Integer>();
		priceList    = new ArrayList<Integer>();
		pointsList   = new ArrayList<Integer>();
		pt_upList         = new ArrayList<Integer>();
		pt_downList       = new ArrayList<Integer>();
		pt_sameList       = new ArrayList<Integer>();
		pt_fixedList      = new ArrayList<Integer>();
		pt_valuesList     = new ArrayList<Integer>();
		selfcontainedList = new ArrayList<Integer>();
		estore_priceList  = new ArrayList<Integer>();
		estore_pointsList = new ArrayList<Integer>();

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection(Construct.DB, Construct.USER, Construct.PASS);
			stmt = con.createStatement();
			if(Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).get(Calendar.HOUR_OF_DAY) %4 < 2) {
				sql  = "SELECT * FROM mws_config WHERE power = 1 ORDER BY id ASC";
			} else {
				sql  = "SELECT * FROM mws_config WHERE power = 1 ORDER BY id DESC";
			}
			rs   = stmt.executeQuery(sql);

			count = 0;
			while(rs.next()) {
				skuList.add(rs.getString("sku"));
				asinList.add(rs.getString("asin"));
				costList.add(rs.getInt("cost"));
				powerList.add(rs.getInt("power"));
				timesaleList.add(rs.getInt("timesale"));
				fromList.add(rs.getInt("from"));
				toList.add(rs.getInt("to"));
				sunList.add(rs.getInt("sun"));
				monList.add(rs.getInt("mon"));
				tueList.add(rs.getInt("tue"));
				wedList.add(rs.getInt("wed"));
				thuList.add(rs.getInt("thu"));
				friList.add(rs.getInt("fri"));
				satList.add(rs.getInt("sat"));
				specialList.add(rs.getInt("special"));
				upList.add(rs.getInt("up"));
				downList.add(rs.getInt("down"));
				sameList.add(rs.getInt("same"));
				fixedList.add(rs.getInt("fixed"));
				valuesList.add(rs.getInt("values"));
				priceList.add(rs.getInt("price"));
				pointsList.add(rs.getInt("points"));
				pt_upList.add(rs.getInt("pt_up"));
				pt_downList.add(rs.getInt("pt_down"));
				pt_sameList.add(rs.getInt("pt_same"));
				pt_fixedList.add(rs.getInt("pt_fixed"));
				pt_valuesList.add(rs.getInt("pt_values"));
				selfcontainedList.add(rs.getInt("selfcontained"));
				estore_priceList.add(rs.getInt("estore_price"));
				estore_pointsList.add(rs.getInt("estore_points"));
				count++;
			}
			rs.close();
			stmt.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Integer getCount() {
		return this.count;
	}
	public List<String> getSkuList() {
		return this.skuList;
	}
	public List<String> getAsinList() {
		return this.asinList;
	}
	public List<Integer> getCostList() {
		return this.costList;
	}
	public List<Integer> getPowerList() {
		return this.powerList;
	}
	public List<Integer> getTimesaleList() {
		return this.timesaleList;
	}
	public List<Integer> getFromList() {
		return this.fromList;
	}
	public List<Integer> getToList() {
		return this.toList;
	}
	public List<Integer> getSunList() {
		return this.sunList;
	}
	public List<Integer> getMonList() {
		return this.monList;
	}
	public List<Integer> getTueList() {
		return this.tueList;
	}
	public List<Integer> getWedList() {
		return this.wedList;
	}
	public List<Integer> getThuList() {
		return this.thuList;
	}
	public List<Integer> getFriList() {
		return this.friList;
	}
	public List<Integer> getSatList() {
		return this.satList;
	}
	public List<Integer> getSpecialList() {
		return this.specialList;
	}
	public List<Integer> getUpList() {
		return this.upList;
	}
	public List<Integer> getDownList() {
		return this.downList;
	}
	public List<Integer> getSameList() {
		return this.sameList;
	}
	public List<Integer> getFixedList() {
		return this.fixedList;
	}
	public List<Integer> getValuesList() {
		return this.valuesList;
	}
	public List<Integer> getPriceList() {
		return this.priceList;
	}
	public List<Integer> getPointsList() {
		return this.pointsList;
	}
	public List<Integer> getSelfContainedList() {
		return this.selfcontainedList;
	}
	public List<Integer> getPt_UpList() {
		return this.pt_upList;
	}
	public List<Integer> getPt_DownList() {
		return this.pt_downList;
	}
	public List<Integer> getPt_SameList() {
		return this.pt_sameList;
	}
	public List<Integer> getPt_FixedList() {
		return this.pt_fixedList;
	}
	public List<Integer> getPt_ValuesList() {
		return this.pt_valuesList;
	}
	public List<Integer> getEstore_PriceList() {
		return this.estore_priceList;
	}
	public List<Integer> getEstore_PointsList() {
		return this.estore_pointsList;
	}

	public Boolean isEnabled() {
		return this.count != 0 ? Boolean.TRUE : Boolean.FALSE;
	}
}

package amazon;

public class Construct {
	// common
	static final int ACCESS_INTERVAL = 0;
	static final int SUBMIT_INTERVAL = 200;
	static final int TEA_BREAK = 300000;

	// database
	static final String DB   = "jdbc:mysql://<YOUR DATABASE HOST>?useSSL=&requireSSL=true&verifyServerCertificate=false";
	static final String USER = "<YOUR DATABASE USER>";
	static final String PASS = "<YOUR DATABASE PASSWORD>";

	// params
	static final long VERSION = 1L;
	static final double DEFAULT_RATE  = .8;
	static final double LIMIT_RATE    = .95;
	static final double DISCOUNT_RATE = .0025;
	static final int DISCOUNT         = 10;
	static final String PRODUCT_PAGE  = "http://www.amazon.co.jp/dp/";

	// amazon fullfillment
	public static final String ACCESS_KEY_ID = "<YOUR ACCESS KEY ID>";
	public static final String SECRET_ACCESS_KEY = "<YOUR SECRET ACCESS KEY>";
	public static final String APP_NAME = "<YOUR APPLICATAION NAME>";
	public static final String APP_VERSION = "1";

	// amazon marketplace
	public static final String SELLER_ID = "<YOUR SELLER ID>";
	public static final String MARKETPLACE_ID = "<YOUR MARKETPLACE ID>";
	public static final String ITEM_CONDITION = "New";
	public static final Boolean EXCLUDE_ME = Boolean.valueOf(true);
}

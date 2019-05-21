package amazon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.mws.samples.SubmitFeedSample;
import com.amazonservices.mws.products.samples.GetLowestOfferListingsForSKUSample;
import com.amazonservices.mws.products.samples.GetMyPriceForSKUSample;

public class Main {

	private Long startTime;
	private Configuration conf;
	private List<String> feedSku;
	private List<Integer> feedPrice;
	private List<Integer> feedPoints;
	private Integer idx;
	private Integer max;
	private Algorithm algorithm;
	private Integer currentPrice;
	private List<Integer> lowestPriceList;
	private List<Integer> lowestPointList;
	private List<Integer> lowestList;
	private Integer cost;
	private Double profit;
	private UpdateMaster master;
	private String submitfeedResult;
	private long count;

	Main() {
		this.startTime        = null;
		this.feedSku          = null;
		this.feedPrice        = null;
		this.feedPoints       = null;
		this.idx              = null;
		this.max              = null;
		this.algorithm        = null;
		this.submitfeedResult = null;
	}

	private void process() {
		// start
		startTime = System.currentTimeMillis();
		System.out.println(new Date(startTime));

		// configuration
		conf = new Configuration();
		feedSku = new ArrayList<String>();
		feedPrice = new ArrayList<Integer>();
		feedPoints = new ArrayList<Integer>();

		if(conf.isEnabled()) {
			// initialize
			algorithm = null;
			currentPrice = null;
			cost = null;
			profit = null;

			idx = 0;
			max = conf.getCount();
			for(; idx < max; idx++) {
			try {
				startTime = System.currentTimeMillis();
				++count;

				// algorithm
				algorithm = new Algorithm(conf, idx);

				// product information
				System.out.println("-------------------------------------");
				System.out.println("[" +(idx+1) +"] " +conf.getSkuList().get(idx));
				System.out.println(Construct.PRODUCT_PAGE +conf.getAsinList().get(idx));
				currentPrice = GetMyPriceForSKUSample.main(conf.getSkuList().get(idx));
				if(currentPrice == null){
					System.out.println("AlertMessage : 販売価格取得不可");
					long wait = Construct.ACCESS_INTERVAL - (System.currentTimeMillis() - startTime);
					if (System.currentTimeMillis() - startTime < Construct.ACCESS_INTERVAL) {
						// System.out.println("Wait...");
						Thread.sleep(wait);
					}
					continue;
				}
				cost = conf.getCostList().get(idx);
				BigDecimal bigDecimal = new BigDecimal(String.valueOf(((currentPrice -cost) /(double)currentPrice) *100));
				profit = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				System.out.println("CurrentPrice : " +currentPrice);
				System.out.println("Cost         : " +conf.getCostList().get(idx) +" (" +profit + "%)");
				System.out.println("Points       : " +conf.getPointsList().get(idx));
				System.out.println("");
				System.out.print("Algorithm    : ");
				if(conf.getUpList().get(idx) == 1){System.out.print("Up");};
				if(conf.getDownList().get(idx) == 1){System.out.print("Down");};
				if(conf.getSameList().get(idx) == 1){System.out.print("Same");};
				if(conf.getFixedList().get(idx) == 1){System.out.print("Fixed");};
				System.out.println("");
				System.out.print("Timesale     : ");
				if(conf.getTimesaleList().get(idx) == 1){System.out.print("On");}else{System.out.print("Off");};
				System.out.println("");
				System.out.print("Timezone     : ");
				if(conf.getFromList().get(idx) != null){System.out.print("From " +conf.getFromList().get(idx) +" ");};
				if(conf.getToList().get(idx) != null){System.out.print("To " +conf.getToList().get(idx) +" ");};
				System.out.println("");
				if(conf.getSunList().get(idx) == 1){System.out.print("[Sun]");};
				if(conf.getMonList().get(idx) == 1){System.out.print("[Mon]");};
				if(conf.getTueList().get(idx) == 1){System.out.print("[Tue]");};
				if(conf.getWedList().get(idx) == 1){System.out.print("[Wed]");};
				if(conf.getThuList().get(idx) == 1){System.out.print("[Thu]");};
				if(conf.getFriList().get(idx) == 1){System.out.print("[Fri]");};
				if(conf.getSatList().get(idx) == 1){System.out.print("[Sat]");};
				if(conf.getSunList().get(idx) == 1 || conf.getMonList().get(idx) == 1 || conf.getTueList().get(idx) == 1 || conf.getWedList().get(idx) == 1 || conf.getThuList().get(idx) == 1 || conf.getFriList().get(idx) == 1 || conf.getSatList().get(idx) == 1){
					System.out.println("");
					System.out.println("");
				}

				if (algorithm.isTimesaled()) {
					// 他店価格調査
					lowestList = GetLowestOfferListingsForSKUSample.main(conf.getSkuList().get(idx));
					if(lowestList != null){
						lowestPriceList = new ArrayList<Integer>();
						lowestPointList = new ArrayList<Integer>();
						int half = lowestList.size() / 2;
						for(int i=0; i<lowestList.size(); i++){
							if(i<half){
								lowestPriceList.add(lowestList.get(i));
							}else{
								lowestPointList.add(lowestList.get(i));
							}
						}
					}
					if (algorithm.isComputed(algorithm, lowestPriceList, lowestPointList)) {
						if (conf.getTimesaleList().get(idx) != 1 && conf.getFixedList().get(idx) != 1 && algorithm.getErrorMessage() != null) {
							// 最低制限価格
							System.out.println("AlertMessage : 最低制限価格");
							long wait = Construct.ACCESS_INTERVAL - (System.currentTimeMillis() - startTime);
							if (System.currentTimeMillis() - startTime < Construct.ACCESS_INTERVAL) {
								// System.out.println("Wait...");
								Thread.sleep(wait);
							}
						}
					} else {
						// 原価設定不備
						System.out.println("AlertMessage : 原価設定不備");
						long wait = Construct.ACCESS_INTERVAL - (System.currentTimeMillis() - startTime);
						if (System.currentTimeMillis() - startTime < Construct.ACCESS_INTERVAL) {
							// System.out.println("Wait...");
							Thread.sleep(wait);
						}
						continue;
					}
					feedSku.add(conf.getSkuList().get(idx));
					feedPrice.add(algorithm.getUpdatePrice());
					feedPoints.add(algorithm.getUpdatePoint());

					// update master database
					master = new UpdateMaster();
					master.database(algorithm.getUpdatePrice(), algorithm.getUpdatePoint(), conf.getSkuList().get(idx));
				}
				BigDecimal newDecimal = new BigDecimal(String.valueOf(((algorithm.getUpdatePrice() -cost) /(double)algorithm.getUpdatePrice()) *100));
				profit = newDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
				System.out.println("");
				System.out.println("Updated      : " + algorithm.getUpdatePrice() +" (" +profit + "%)");
				System.out.println("             : " + algorithm.getUpdatePoint() +"pt");
				long wait = Construct.ACCESS_INTERVAL - (System.currentTimeMillis() - startTime);
				if (System.currentTimeMillis() - startTime < Construct.ACCESS_INTERVAL) {
					// System.out.println("Wait...");
					Thread.sleep(wait);
				}
				} catch (Exception e) {
					e.printStackTrace();
					// APIがERRORの場合はSUBMITして終了
					System.out.println("-------------------------------------");
					System.out.println(new Date(System.currentTimeMillis()));
					if(!feedSku.isEmpty()) {
						System.out.println("UpdateProduct: " + feedSku.size());
						System.out.println("-------------------------------------");
						submitfeedResult = new SubmitFeedSample().action(feedSku, feedPrice, feedPoints);
					}
					return;
				}
			System.out.println("count        : " +count);
			}
		}
		System.out.println("-------------------------------------");
		System.out.println(new Date(System.currentTimeMillis()));
		if(!feedSku.isEmpty()) {
			System.out.println("UpdateProduct: " + feedSku.size());
			System.out.println("-------------------------------------");
			submitfeedResult = new SubmitFeedSample().action(feedSku, feedPrice, feedPoints);
		}
	}

	public static void main(String[] args) {
		new Main().process();
	}
}
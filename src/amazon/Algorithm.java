package amazon;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class Algorithm {
	private Configuration conf;
	private Integer idx;
	private TimeZone timezone;
	private Calendar calendar;
	private Integer currentHour;
	private Integer from;
	private Integer to;
	private Boolean isTimesale;
	private Algorithm algorithm;
	private Integer lowerPrice;
	private Integer lowerPoint;
	private Integer lowerBase;
	private Integer diffPrice;
	private Integer diffPoint;
	private Integer cost;
	private Integer limit;
	private Integer discount;
	private Integer up;
	private Integer down;
	private Integer same;
	private Integer fixed;
	private Integer values;
	private Integer pt_up;
	private Integer pt_down;
	private Integer pt_same;
	private Integer pt_fixed;
	private Integer pt_values;
	private Integer selfcontained;
	private Integer estorePrice;
	private Integer estorePoint;
	private Integer estoreBase;

	private String errorMessage;
	private String sku;
	private Integer updatePrice;
	private Integer updatePoint;
	private Integer updateBase;
	private UpdateHistory history;

	Algorithm(Configuration conf, Integer idx) {
		this.conf = conf;
		this.idx = idx;
	}

	public Boolean isTimesaled() {
		// algorithm
		timezone = TimeZone.getTimeZone("Asia/Tokyo");
		calendar = Calendar.getInstance(timezone);
		currentHour = calendar.get(Calendar.HOUR_OF_DAY);
		from = this.conf.getFromList().get(this.idx);
		to = this.conf.getToList().get(this.idx);

		if (this.conf.getTimesaleList().get(this.idx) == 1) {
			switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				this.isTimesale = this.conf.getSunList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			case Calendar.MONDAY:
				this.isTimesale = this.conf.getMonList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			case Calendar.TUESDAY:
				this.isTimesale = this.conf.getTueList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			case Calendar.WEDNESDAY:
				this.isTimesale = this.conf.getWedList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			case Calendar.THURSDAY:
				this.isTimesale = this.conf.getThuList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			case Calendar.FRIDAY:
				this.isTimesale = this.conf.getFriList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			case Calendar.SATURDAY:
				this.isTimesale = this.conf.getSatList().get(this.idx) == 1 ? Boolean.TRUE : Boolean.FALSE;
				break;
			}
			if (this.isTimesale) {
				if (from > to) {
					this.isTimesale = (from <= currentHour && currentHour <= 24) || (0 <= currentHour && currentHour < to) ? Boolean.TRUE : Boolean.FALSE;
				} else {
					this.isTimesale = from <= currentHour && currentHour < to ? Boolean.TRUE : Boolean.FALSE;
				}
			}
		} else {
			this.isTimesale = Boolean.FALSE;
		}

		// time sale
		if (this.isTimesale) {
			if (this.conf.getSpecialList().get(this.idx) == 0) {
				// special time sale price has not been set
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	public Boolean isComputed(Algorithm algorithm, List<Integer> lowestPriceList, List<Integer> lowestPointList) {
		this.algorithm = algorithm;
		if(lowestPriceList != null && lowestPriceList.size() > 0){
			this.lowerPrice = lowestPriceList.get(0);
			this.lowerPoint = lowestPointList.get(0);
			this.lowerBase = this.lowerPrice - this.lowerPoint;
		} else {
			this.lowerPrice = null;
			this.lowerPoint = 0;
			this.lowerBase = null;
		}
		this.cost = this.conf.getCostList().get(this.idx);
		this.up = this.conf.getUpList().get(this.idx);
		this.down = this.conf.getDownList().get(this.idx);
		this.same = this.conf.getSameList().get(this.idx);
		this.fixed = this.conf.getFixedList().get(this.idx);
		this.values = this.conf.getValuesList().get(this.idx);
		this.pt_up = this.conf.getPt_UpList().get(this.idx);
		this.pt_down = this.conf.getPt_DownList().get(this.idx);
		this.pt_same = this.conf.getPt_SameList().get(this.idx);
		this.pt_fixed = this.conf.getPt_FixedList().get(this.idx);
		this.pt_values = this.conf.getPt_ValuesList().get(this.idx);
		this.selfcontained = this.conf.getSelfContainedList().get(this.idx);
		this.estorePrice = this.conf.getEstore_PriceList().get(this.idx);
		this.estorePoint = this.conf.getEstore_PointsList().get(this.idx);

		// update price
		if (algorithm.getUpdatePrice() == null) {
			// validate
			if (this.cost == 0) {
				// cost has not been set
				return Boolean.FALSE;
			} else {
				// default price
				if (this.lowerPrice == null) {
					this.updatePrice = (int)Math.round(this.cost/Construct.DEFAULT_RATE);
					this.updatePoint = 0;
				} else {
					this.limit = (int)Math.round(this.cost/Construct.LIMIT_RATE);
					this.discount = (int)Math.round(this.cost*Construct.DISCOUNT_RATE);

					if (this.up == 1) {
						if ((this.lowerPrice + this.discount) < this.limit) {
							// limit price
							this.errorMessage = "warning! it has set to limit price " + this.limit;
							this.updatePrice = this.limit;
							this.updatePoint = 0;
						} else {
							// up
							this.updatePrice = this.lowerPrice + this.discount;
							this.updatePoint = 0;
							// System.out.println("algorithm type: up");
							// System.out.println("other stores lowest price: " + this.lower);
							// System.out.println("update price: " + this.updatePrice);
						}
					} else if (this.down == 1) {
						if ((this.lowerPrice - this.discount) < this.limit) {
							// limit price
							this.errorMessage = "warning! it has set to limit price " + this.limit;
							this.updatePrice = this.limit;
							this.updatePoint = 0;
						} else {
							// down
							this.updatePrice = this.lowerPrice - this.discount;
							this.updatePoint = 0;
							// System.out.println("algorithm type: down");
							// System.out.println("other stores lowest price: " + this.lower);
							// System.out.println("update price: " + this.updatePrice);
						}
					} else if (this.same == 1) {
						if (this.lowerPrice < this.limit) {
							// limit price alert
							this.errorMessage = "warning! it has set to limit price " + this.limit;
							this.updatePrice = this.limit;
							this.updatePoint = 0;
						} else {
							// same
							this.updatePrice = this.lowerPrice;
							this.updatePoint = 0;
							// System.out.println("algorithm type: same");
							// System.out.println("other stores lowest price: " + this.lower);
							// System.out.println("update price: " + this.updatePrice);
						}
					} else if (this.fixed == 1) {
						if (this.values == 0) {
							// limit price
							this.updatePrice = this.limit;
							this.updatePoint = 0;
						} else {
							// fixed
							this.updatePrice = this.values;
							this.updatePoint = 0;
							// System.out.println("algorithm type: fixed");
							// System.out.println("other stores lowest price: " + this.lower);
							// System.out.println("update price: " + this.updatePrice);
						}
					} else {
						// not set
						return Boolean.FALSE;
					}

					// point algorithm
					if(this.lowerBase > this.limit){
						diffPrice = this.updatePrice - this.lowerBase;
					}else{
						diffPrice = this.updatePrice - this.limit;
					}

					if(diffPrice > 0){
						if (this.pt_up == 1) {
							diffPoint = diffPrice - this.discount;
							if(diffPoint > 0){
								this.updatePoint = diffPoint;
							}
						} else if (this.pt_down == 1) {
							diffPoint = diffPrice + this.discount;
							if(diffPoint > 0){
								this.updatePoint = diffPoint;
							}
						} else if (this.pt_same == 1) {
							diffPoint = diffPrice;
							if(diffPoint > 0){
								this.updatePoint = diffPoint;
							}
						} else if (this.pt_fixed == 1) {
							if(this.pt_values > 0){
								this.updatePoint = this.pt_values;
							}
						}
					}
				}

				// time sale
				if (this.isTimesale) {
					System.out.println("Mode: Now on sale for a limited time!");
					this.updatePrice = this.conf.getSpecialList().get(this.idx);
					this.updatePoint = 0;
					this.updateBase = this.updatePrice - this.updatePoint;
				}

				// eStore
				if(this.selfcontained != 1){
					if(this.estorePrice > 0){
						this.updateBase = this.updatePrice - this.updatePoint;
						this.estoreBase = this.estorePrice - this.estorePoint;

						if(this.updateBase > this.estoreBase){
							this.updatePrice = this.estorePrice;
							if(this.estorePoint > 0){
								this.updatePoint = this.estorePoint;
							}
						}
					}
				}
			}
		}

		// update history database
		this.sku = this.conf.getSkuList().get(this.idx);
		this.updateBase = this.updatePrice - this.updatePoint;
		history = new UpdateHistory();
		history.database(this.sku, this.updatePrice, this.updatePoint, this.updateBase, this.lowerPrice, this.lowerPoint, this.lowerBase);

		return Boolean.TRUE;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}
	public Integer getUpdatePrice() {
		return this.updatePrice;
	}
	public Integer getUpdatePoint() {
		return this.updatePoint;
	}
	public Integer getLowerPrice() {
		return this.lowerPrice;
	}
	public Integer getLowerPoint() {
		return this.lowerPoint;
	}
}
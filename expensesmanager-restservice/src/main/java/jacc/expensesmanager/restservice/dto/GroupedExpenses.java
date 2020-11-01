package jacc.expensesmanager.restservice.dto;

public class GroupedExpenses implements Comparable<GroupedExpenses>{

	public static final String GROUP_SEPARATOR = "-";

	private String groupby;
	private String groupKey;
	private Double amount;
	private int year;
	private int month;
	private String category;

	public String getGroupby() {
		return groupby;
	}

	public void setGroupby(String groupby) {
		this.groupby = groupby;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupDescription, GroupedKey groupKey) {
		this.groupKey = groupDescription;
		setAdditionalProperties(groupDescription, groupKey);
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	// private
	private void setAdditionalProperties(String groupDescription, GroupedKey groupKey) {
		String[] props = groupDescription.split(GROUP_SEPARATOR);

		switch (groupKey) {
		case YEARCATEGORYTOTAL:
			category = props.length == 2 ? props[1] : "";
			year = props.length >= 1 ? Integer.valueOf(props[0]) : 0;
			break;
		case YEARTOTAL:
			year = props.length == 1 ? Integer.valueOf(props[0]) : 0;
			break;
		case MONTHCATEGORYTOTAL:
			category = props.length == 3 ? props[2] : "";
			month = props.length >= 2 ? Integer.valueOf(props[1]) : 0;
			year = props.length >= 1 ? Integer.valueOf(props[0]) : 0;
			break;
		case MONTHTOTAL:
			month = props.length == 2 ? Integer.valueOf(props[1]) : 0;
			year = props.length >= 1 ? Integer.valueOf(props[0]) : 0;
			break;

		default:
			break;
		}

	}

	@Override
	public int compareTo(GroupedExpenses ge) {
		if (this.year > ge.year) {
			return 1;
		} else if (this.year < ge.year) {
			return -1;
		} else if (this.month > ge.month) {
			return 1;
		} else if (this.month < ge.month) {
			return -1;
		} else if (this.category.compareTo(ge.category) > 1) {
			return 1;
		} else if (this.category.compareTo(ge.category) < 1) {
			return -1;
		} else if (this.amount > ge.amount) {
			return 1;
		} else if (this.amount < ge.amount) {
			return -1;
		}
		return 0;
	}

}
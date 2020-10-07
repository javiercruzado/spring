package jacc.expensesmanager.restservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDTO {

	private int id;
	private String categoryId;
	private String categoryName;
	private BigDecimal debit;
	private Integer yearId;
	private Integer monthId;
	private Integer dayId;
	private String note;
	private LocalDate date;
	private String month;

	public String getCategoryId() {
		return categoryId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public Integer getYearId() {
		return yearId;
	}

	public void setYearId(Integer yearId) {
		this.yearId = yearId;
	}

	public Integer getMonthId() {
		return monthId;
	}

	public void setMonthId(Integer monthId) {
		this.monthId = monthId;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
}

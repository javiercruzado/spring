package jacc.expensesmanager.restservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ExpensesRepository {

	public static String QUERY_GLOBAL_SEARCH = "select\n" + "    ti.\"_id\" as id,\n" + "	categoryId,\n"
			+ "	cat.name \"categoryname\",\n" + "	debit,\n" + "	yearId,\n" + "	monthId,\n" + "	dayId,\n"
			+ "	ti.note from TransactionItem ti\n" + "join Category cat on	cat.id = ti.categoryId "
			+ "where (LOWER(cat.name) = :categoryName or :categoryName = '') "
			+ "and (LOWER(ti.note) like :noteLike or :noteLike = '') "
			+ "and :fromDay < dayId AND dayId < :toDay";

	public static String QUERY_CATEGORIES = "select * from Category order by name";
	// Spring Boot will automagically wire this object using application.properties:

	@Autowired
	NamedParameterJdbcTemplate template;

	public List<ExpenseDTO> getExpenses(String categoryName, String noteLike, String fromDay, String toDay) {

		List<ExpenseDTO> expenses = new ArrayList<>();
		SqlParameterSource parameters = new MapSqlParameterSource().addValue("categoryName", categoryName.toLowerCase())
				.addValue("noteLike", "%" + noteLike.toLowerCase() + "%").addValue("fromDay", getDayFromString(fromDay))
				.addValue("toDay", getDayFromString(toDay));
		expenses = template.query(QUERY_GLOBAL_SEARCH, parameters, (RowMapper<ExpenseDTO>) (rs, rowNum) -> {
			ExpenseDTO expenseDTO = new ExpenseDTO();
			expenseDTO.setId(rs.getInt("id"));
			expenseDTO.setCategoryId(rs.getString("categoryId"));
			expenseDTO.setCategoryName(rs.getString("categoryname"));
			expenseDTO.setDebit(BigDecimal.valueOf((double)rs.getInt("debit")/100));
			expenseDTO.setYearId(rs.getInt("yearId"));
			expenseDTO.setMonthId(rs.getInt("monthId"));
			expenseDTO.setDayId(rs.getInt("dayId"));
			expenseDTO.setNote(rs.getString("note"));
			
			int yearId = rs.getInt("yearId");
			int dayOfYear = (rs.getInt("dayId")) % (yearId*1000);
			
			LocalDate date = LocalDate.ofYearDay(yearId, dayOfYear);
			expenseDTO.setDate(date);					
					
			return expenseDTO;
		});

		return expenses;
	}
	
	public List<CategoryDTO> getCategories() {
	    List<CategoryDTO> categories = template.query(
	    		QUERY_CATEGORIES,
	            new BeanPropertyRowMapper<CategoryDTO>(CategoryDTO.class));
	    return categories;
	}
	
	//helpers
	
	/**
	 * 
	 * @param date, format "2020-06-18T03:02:06.851+0000"
	 * @return year * 1000 + day
	 */
	private int getDayFromString(String strDate) {
		LocalDate date = LocalDate.parse(strDate);
		int year = date.getYear();
		int day = date.getDayOfYear();
		return year * 1000 + day;
	}
}

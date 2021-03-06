package jacc.expensesmanager.restservice.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
			+ "and :fromDay <= dayId AND dayId <= :toDay order by yearId, monthId, dayId";

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
			
			BigDecimal amount = new BigDecimal(Double.toString(rs.getInt("debit") / 100));
		    amount = amount.setScale(2, RoundingMode.HALF_UP);
		    
			expenseDTO.setDebit(new BigDecimal(amount.doubleValue()));
			expenseDTO.setYearId(rs.getInt("yearId"));
			// expenseDTO.setMonthId(rs.getInt("monthId"));
			expenseDTO.setDayId(rs.getInt("dayId"));
			expenseDTO.setNote(rs.getString("note"));

			int yearId = rs.getInt("yearId");
			int dayOfYear = (rs.getInt("dayId")) % (yearId * 1000);

			LocalDate date = LocalDate.ofYearDay(yearId, dayOfYear);

			expenseDTO.setDate(date);
			expenseDTO.setMonthId(date.getMonthValue());
			expenseDTO.setMonth(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.US));

			return expenseDTO;
		});

		return expenses;
	}

	public List<CategoryDTO> getCategories() {
		List<CategoryDTO> categories = template.query(QUERY_CATEGORIES,
				new BeanPropertyRowMapper<CategoryDTO>(CategoryDTO.class));
		return categories;
	}

	public List<GroupedExpenses> getExpenses(String category, String noteLike, String fromDate, String toDate,
			String groupBy) {

		List<ExpenseDTO> expenses = getExpenses(category, noteLike, fromDate, toDate);
		List<GroupedExpenses> groupedExpenses = new ArrayList<>();

		List<GroupedKey> test = Arrays.asList(GroupedKey.values());
		Optional<GroupedKey> test2 = test.stream().filter(x -> x.toString().equalsIgnoreCase(groupBy)).findAny();

		GroupedKey caseConstant = test2.isPresent() ? GroupedKey.valueOf(groupBy.toUpperCase()) : GroupedKey.YEARTOTAL;

		switch (caseConstant) {

		case YEARCATEGORYTOTAL:
			Map<String, Double> yearlyCategoryExpenses = expenses.stream()
					.collect(Collectors.groupingBy(
							x -> x.getYearId() + GroupedExpenses.GROUP_SEPARATOR + x.getCategoryName(),
							Collectors.summingDouble(x -> x.getDebit().doubleValue())));
			yearlyCategoryExpenses.forEach((k, v) -> {
				GroupedExpenses ge = new GroupedExpenses();
				ge.setAmount(v);
				ge.setGroupby(groupBy);
				ge.setGroupKey(k.toString(), GroupedKey.YEARCATEGORYTOTAL);
				groupedExpenses.add(ge);
			});

			break;

		case MONTHCATEGORYTOTAL:
			Map<String, Double> monthlyCategoryExpenses = expenses.stream()
					.collect(Collectors.groupingBy(
							x -> x.getYearId() + GroupedExpenses.GROUP_SEPARATOR + x.getMonthId()
									+ GroupedExpenses.GROUP_SEPARATOR + x.getCategoryName(),
							Collectors.summingDouble(x -> x.getDebit().doubleValue())));
			monthlyCategoryExpenses.forEach((k, v) -> {
				GroupedExpenses ge = new GroupedExpenses();
				ge.setAmount(v);
				ge.setGroupby(groupBy);
				ge.setGroupKey(k.toString(), GroupedKey.MONTHCATEGORYTOTAL);
				groupedExpenses.add(ge);
			});

			break;

		case YEARTOTAL:
			Map<Integer, Double> yearlyExpenses = expenses.stream().collect(Collectors.groupingBy(ExpenseDTO::getYearId,
					Collectors.summingDouble(x -> x.getDebit().doubleValue())));
			yearlyExpenses.forEach((k, v) -> {
				GroupedExpenses ge = new GroupedExpenses();
				ge.setAmount(v);
				ge.setGroupby(groupBy);
				ge.setGroupKey(k.toString(), GroupedKey.YEARTOTAL);
				groupedExpenses.add(ge);
			});

			break;

		case MONTHTOTAL:
		default:
			Map<String, Double> montlyExpenses = expenses.stream().collect(
					Collectors.groupingBy(x -> x.getYearId() + GroupedExpenses.GROUP_SEPARATOR + x.getMonthId(),
							Collectors.summingDouble(x -> x.getDebit().doubleValue())));

			montlyExpenses.forEach((k, v) -> {
				GroupedExpenses ge = new GroupedExpenses();
				ge.setAmount(v);
				ge.setGroupby(groupBy);
				ge.setGroupKey(k.toString(), GroupedKey.MONTHTOTAL);
				groupedExpenses.add(ge);
			});
			break;
		}

		return groupedExpenses.stream().sorted((x, y) -> x.compareTo(y)).collect(Collectors.toList());
	}

	// helpers

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

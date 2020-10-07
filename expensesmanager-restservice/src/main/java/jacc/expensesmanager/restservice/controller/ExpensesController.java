package jacc.expensesmanager.restservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jacc.expensesmanager.restservice.dto.CategoryDTO;
import jacc.expensesmanager.restservice.dto.ExpenseDTO;
import jacc.expensesmanager.restservice.dto.ExpensesRepository;
import jacc.expensesmanager.restservice.dto.GroupedExpenses;

@RestController
public class ExpensesController {

	@Autowired
	ExpensesRepository expenseRepo;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/expenses")
	public List<ExpenseDTO> searchExpenses(@RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "noteLike", defaultValue = "") String noteLike,
			@RequestParam(name = "fromDate", defaultValue = "2000-01-01") String fromDate,
			@RequestParam(name = "toDate", defaultValue = "2099-12-31") String toDate) {

		return expenseRepo.getExpenses(category, noteLike, fromDate, toDate);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/categories")
	public List<CategoryDTO> searchExpenses() {
		return expenseRepo.getCategories();
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/expensesByGroup")
	public List<GroupedExpenses> searchExpensesByGroup(
			@RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "noteLike", defaultValue = "") String noteLike,
			@RequestParam(name = "fromDate", defaultValue = "2000-01-01") String fromDate,
			@RequestParam(name = "toDate", defaultValue = "2099-12-31") String toDate,
			@RequestParam(name = "groupBy", defaultValue = "") String groupBy) {

		return expenseRepo.getExpenses(category, noteLike, fromDate, toDate, groupBy);
	}

}

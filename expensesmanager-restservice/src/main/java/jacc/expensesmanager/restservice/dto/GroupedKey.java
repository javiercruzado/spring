package jacc.expensesmanager.restservice.dto;

public enum GroupedKey {
	YEARCATEGORYTOTAL {
		@Override
		public String toString() {
			return "YEARCATEGORYTOTAL";
		}
	},
	MONTHCATEGORYTOTAL {
		@Override
		public String toString() {
			return "MONTHCATEGORYTOTAL";
		}
	},
	YEARTOTAL {
		@Override
		public String toString() {
			return "YEARTOTAL";
		}
	},
	MONTHTOTAL {
		@Override
		public String toString() {
			return "MONTHTOTAL";
		}
	}
}

import React from 'react'
import PropTypes from 'prop-types';

const ExpensesTable = ({ expenses = [] }) => {

    let rows = expenses.map((x, i) =>
        <tr key={i}><td>{x.categoryName}</td><td>{x.debit}</td><td>{x.note}</td><td>{x.date}</td></tr>)

    let expensesTable = <table className="table table-striped table-sm">
        <thead>
            <tr>
                <th>Category</th>
                <th>Amount</th>
                <th>Notes</th>
                <th>Date</th>
            </tr>
        </thead>
        <tbody>{rows}</tbody>
    </table>
    return expensesTable
}

ExpensesTable.propTypes = {
    expenses: PropTypes.array
}

export default ExpensesTable

//TODO: add props types and defaults
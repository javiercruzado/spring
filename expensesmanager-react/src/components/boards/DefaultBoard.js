import React from 'react'

import { LineChart, Line, XAxis } from 'recharts'
import ExpensesTable from '../expenses/ExpensesTable'


const RenderLineChart = ({ expenses = [] }) => {
    let data = []

    expenses.forEach(x => {
        data.push({ name: x.date, value: x.debit })
    })

    return (<LineChart width={1200} height={400} data={data}>
        <XAxis dataKey="name" />
        <Line type="monotone" dataKey="value" stroke="#8884d8" />
    </LineChart>)
}

const DefaultBoard = ({ expenses = [] }) => {
    let defaultBoard =
        <div>
            <RenderLineChart expenses={expenses} />

            <h2>Expenses</h2>
            <div noValidate>

            </div>
            <div className="table-responsive">
                <ExpensesTable expenses={expenses} />
            </div>

        </div>
    return defaultBoard
}

export default DefaultBoard


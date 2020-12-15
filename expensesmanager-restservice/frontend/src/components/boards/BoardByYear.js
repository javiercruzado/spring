import React from 'react'
import { Table } from 'antd';

import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts'


const RenderBarChart = ({ expensesByGroup = [] }) => {
  let data = []

  expensesByGroup.forEach(x => {
    data.push({ name: x.groupKey, amount: x.amount, category: x.category || ''})
  })

  const columns = [
    {
      title: 'Period',
      dataIndex: 'name'
    },
    {
      title: 'Amount',
      dataIndex: 'amount'
    },
    {
      title: 'Category',
      dataIndex: 'category'
    }
  ];


  return (
    <div>
      <BarChart
        width={1200}
        height={400}
        data={data}
        margin={{
          top: 5, right: 30, left: 20, bottom: 5,
        }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Bar dataKey="amount" fill="#8884d8" />
      </BarChart>
      <Table columns={columns} dataSource={data} />
    </div>
  )
}

const BoardByYear = ({ expensesByGroup = [] }) => {
  let board =
    <div>
      <RenderBarChart expensesByGroup={expensesByGroup} />
    </div>
  return board
}

export default BoardByYear
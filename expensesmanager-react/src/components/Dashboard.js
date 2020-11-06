import React from 'react'
import { DatePicker, Select, Space } from 'antd';
import 'antd/dist/antd.css';
import moment from 'moment';

import DefaultBoard from './boards/DefaultBoard'
import BoardByYear from './boards/BoardByYear'

import { VIEW_STATITICS_BYYEAR, VIEW_STATITICS_BYMONTH, VIEW_STATITICS_BYMONTH_CATEGORY } from '../index'

const { Option } = Select

const dateToISOShortDate = (date = new Date()) => {
  return date.toISOString().substr(0, 10);
}

const Board = ({ view, categories, expenses, expensesByGroup, filter, updateFilter }) => {

  let board = []
  if ([VIEW_STATITICS_BYYEAR, VIEW_STATITICS_BYMONTH, VIEW_STATITICS_BYMONTH_CATEGORY].includes(view)) {
    board = <BoardByYear expensesByGroup={expensesByGroup} />
  } else {
    board = <DefaultBoard expenses={expenses} />
  }

  return <main role="main" className="col-md-9 ml-sm-auto col-lg-10 px-md-4">
    <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
      <h1 className="h2">Dashboard</h1>
      <div className="btn-toolbar mb-2 mb-md-0">
        <div className="btn-group mr-2">
          <button type="button" className="btn btn-sm btn-outline-secondary">Share</button>
          <button type="button" className="btn btn-sm btn-outline-secondary">Export</button>
        </div>
        <button type="button" className="btn btn-sm btn-outline-secondary dropdown-toggle">
          <span data-feather="calendar"></span>
        This week
      </button>

      </div>
    </div>
    <div className="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
      <Space direction="horizontal">
        <Select style={{ width: 200 }} placeholder='Select a category ...'
          onChange={(value) => {
            updateFilter({ ...filter, categorySelected: value || '' })
          }}
          allowClear={true}
          onClear={() => {
            updateFilter({ ...filter, categorySelected: '' })
          }
          }
        >
          {categories.map(item => (
            <Option key={item.name} >{item.name}</Option>
          ))}
        </Select>
        <DatePicker
          value={moment(filter.fromDate)}
          onChange={(date) => {
            updateFilter({ ...filter, fromDate: date || new Date() })
          }}></DatePicker>
        <DatePicker onChange={(date) => {
          updateFilter({ ...filter, toDate: date || new Date() })
        }}></DatePicker>
      </Space>
    </div>

    {board}

  </main>
}

export default class DashBoard extends React.Component {

  state = {
    selectedCategory: 'Select a category',
    categories: [],
    expenses: [],
    expensesByGroup: [],
    loading: false,
    filter: {
      categorySelected: '',
      fromDate: new Date(new Date().getFullYear(), new Date().getMonth(), 1)
    }
  }

  componentDidMount() {
    this.getCategories()
    this.getExpenses()
    this.getExpensesByGroup()
  }

  componentDidUpdate(prevProps, prevState) {
    if (prevProps.view !== this.props.view) {
      let date = new Date()
      let updatedDate = new Date(date.getFullYear(), date.getMonth(), 1)

      if (this.props.view === VIEW_STATITICS_BYMONTH_CATEGORY) {
        updatedDate = new Date(date.getFullYear(), date.getMonth(), 1)
      } else if (this.props.view === VIEW_STATITICS_BYMONTH) {
        updatedDate = new Date(date.getFullYear(), 0, 1)
      } else if (this.props.view === VIEW_STATITICS_BYYEAR) {
        updatedDate = new Date(date.getFullYear() - 10, 1, 1)
      }

      this.setState({ filter: { ...this.state.filter, fromDate: updatedDate } },
        () => {
          this.getCategories()
          this.getExpenses()
          this.getExpensesByGroup()
        })
    }
  }

  //fetch
  getCategories = () => {
    var requestOptions = {
      method: 'GET',
      redirect: 'follow'
    }
    this.setState({ loading: true })
    fetch("http://localhost:8080/categories", requestOptions)
      .then(response => {
        return response.json()
      })
      .then(result => {
        this.setState({ categories: result, loading: false })
      }
      )
      .catch(error => console.log('error', error))
      .finally(() => this.setState({ loading: false }))
  }

  getExpenses = () => {

    let categorySelected = encodeURIComponent(this.state.filter.categorySelected);
    let toDate = dateToISOShortDate(this.state.filter.toDate)
    let fromDate = dateToISOShortDate(this.state.filter.fromDate)

    let requestOptions = {
      method: 'GET',
      redirect: 'follow'
    };

    fetch(`http://localhost:8080/expenses?noteLike=&fromDate=${fromDate}&toDate=${toDate}&category=${categorySelected}`, requestOptions)
      .then(response => response.json())
      .then(result => {
        this.setState({ expenses: result })
      }
      )
      .catch(error => console.log('error', error));
  }

  getExpensesByGroup = () => {

    let categorySelected = encodeURIComponent(this.state.filter.categorySelected);
    let toDate = dateToISOShortDate(this.state.filter.toDate)
    let fromDate = dateToISOShortDate(this.state.filter.fromDate)

    let groupBy = 'MONTHTOTAL'

    if (this.props.view === VIEW_STATITICS_BYYEAR) {
      groupBy = 'YEARTOTAL'
    } else if (this.props.view === VIEW_STATITICS_BYMONTH) {
      groupBy = 'MONTHTOTAL'
    } else if (this.props.view === VIEW_STATITICS_BYMONTH_CATEGORY) {
      groupBy = 'MONTHCATEGORYTOTAL'
    }


    let requestOptions = {
      method: 'GET',
      redirect: 'follow'
    };

    fetch(`http://localhost:8080/expensesByGroup?noteLike=&fromDate=${fromDate}&toDate=${toDate}&category=${categorySelected}&groupBy=${groupBy}`,
      requestOptions)
      .then(response => response.json())
      .then(result => {
        console.log(result)
        this.setState({ expensesByGroup: result })
      }
      )
      .catch(error => console.log('error', error));
  }

  updateFilter = (updatedFilter) => {
    this.setState({ filter: updatedFilter },
      () => {
        this.getExpenses()
        this.getExpensesByGroup()
      })
  }

  render() {

    let defaultBoard = <Board
      view={this.props.view}
      categories={this.state.categories}
      expenses={this.state.expenses}
      expensesByGroup={this.state.expensesByGroup}
      filter={this.state.filter}
      updateFilter={this.updateFilter} />

    return defaultBoard
  }

}
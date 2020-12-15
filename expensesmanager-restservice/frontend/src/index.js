import React from 'react'
import ReactDOM from 'react-dom';

import TopNavBar from './components/TopNavBar'
import SideBarMenu from './components/SideBarMenu'
import DashBoard from './components/Dashboard';

export const VIEW_DEFAULT = 1

export const VIEW_STATITICS_BYYEAR = 2

export const VIEW_STATITICS_BYMONTH = 3

export const VIEW_STATITICS_BYYEAR_CATEGORY = 4

export const VIEW_STATITICS_BYMONTH_CATEGORY = 5

export default class ExpenseManager extends React.Component {

    constructor(props) {
        super(props)
        this.state = { view: VIEW_DEFAULT }
    }

    loadView = (newView) => {
        this.setState({ view: newView })
    }

    render() {
        console.log('view: ' + this.state.view)
        return <div>
            <TopNavBar />
            <div className="container-fluid">
                <div className="row">
                    <SideBarMenu
                        view={this.state.view}
                        changeView={this.loadView}
                        reportItems={[
                        { name: 'By Month & Category', view: VIEW_STATITICS_BYMONTH_CATEGORY },
                        { name: 'By Month', view: VIEW_STATITICS_BYMONTH },
                        { name: 'By Year', view: VIEW_STATITICS_BYYEAR }]} />
                    <DashBoard view={this.state.view} 
                        changeView={this.loadView} />
                </div>
            </div>
        </div>
    }
}

ReactDOM.render(<ExpenseManager />, document.getElementById('root'))
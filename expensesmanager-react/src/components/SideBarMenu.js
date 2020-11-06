import React from 'react'

import { VIEW_DEFAULT } from '../index'


const SideBarMenu = ({ reportItems = [], view = f => f, changeView = f => f }) => {

  let listItems = reportItems.map((x, i) => {
    return <li className="nav-item" key={'sbm' + i}>
      <a className="nav-link" href="/#" onClick={() => changeView(x.view)}>
        <span data-feather="file-text"></span> {x.name} </a>
    </li>
  })

  let sideBarMenu =
    <nav id="sidebarMenu" className="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
      <div className="sidebar-sticky pt-3">
        <ul className="nav flex-column">
          <li className="nav-item">
            <a className="nav-link active" href="/#" onClick={() => changeView(VIEW_DEFAULT)}>
              <span data-feather="home"></span>
            Dashboard <span className="sr-only">(current)</span>
            </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/#">
              <span data-feather="file"></span>
            Orders
          </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/#">
              <span data-feather="shopping-cart"></span>
            Products
          </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/#">
              <span data-feather="users"></span>
            Customers
          </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/#">
              <span data-feather="bar-chart-2"></span>
            Reports
          </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/#">
              <span data-feather="layers"></span>
            Integrations
          </a>
          </li>
        </ul>

        <h6 className="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
          <span>Statitics</span>
          <a className="d-flex align-items-center text-muted" href="/#" aria-label="Add a new report">
            <span data-feather="plus-circle"></span>
          </a>
        </h6>
        <ul className="nav flex-column mb-2">
          
          {listItems}
        </ul>
      </div>
    </nav>

  return sideBarMenu
}

export default SideBarMenu

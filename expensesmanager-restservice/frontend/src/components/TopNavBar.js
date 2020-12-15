import React from 'react'

export default class TopNavBar extends React.Component{

    state = {
        companyName: 'SoftJacc PERU',
        actions: [{ search: 'Search' }, { signout: 'Sing out' }]
    }

    navBar = <nav className="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow">
                    <a className="navbar-brand col-md-3 col-lg-2 mr-0 px-3" href="/#">{this.state.companyName}</a>
                    <button className="navbar-toggler position-absolute d-md-none collapsed" type="button" data-toggle="collapse" data-target="#sidebarMenu" aria-controls="sidebarMenu" aria-expanded="false" aria-label="Toggle navigation">
                        <span className="navbar-toggler-icon">
                        </span>
                    </button>
                    <input className="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label={this.state.search}></input>
                    <ul className="navbar-nav px-3">
                        <li className="nav-item text-nowrap">
                            <a className="nav-link" href="/#">{this.state.signout}</a>
                        </li>
                    </ul>
                </nav>

    render(){
        return this.navBar
    }
}

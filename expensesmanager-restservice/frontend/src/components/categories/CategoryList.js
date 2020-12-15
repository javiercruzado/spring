import React from 'react'

const CategoryList = ({ categories = [], filter = {}, updateFilter = f => f }) => {
    let categoryList = []
    categories.forEach(x => {
        categoryList.push(
            <a
            className="dropdown-item"
                href="/#" key={x.name}
                onClick={(event) => {
                    updateFilter({ ...filter, categorySelected: x.name })
                }
                }>{x.name}
            </a>)
    })
    return categoryList
}

export default CategoryList


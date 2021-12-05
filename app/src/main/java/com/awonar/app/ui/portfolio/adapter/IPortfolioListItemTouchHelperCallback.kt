package com.awonar.app.ui.portfolio.adapter

interface IPortfolioListItemTouchHelperCallback {

    fun onClick(position: Int)

    fun onClose(position: Int)
}
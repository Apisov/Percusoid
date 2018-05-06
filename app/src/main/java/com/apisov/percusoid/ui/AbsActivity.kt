package com.apisov.percusoid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.apisov.percusoid.R
import com.apisov.percusoid.util.replaceFragmentInActivity
import com.apisov.percusoid.util.setupActionBar

@SuppressLint("Registered")
abstract class AbsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViewFragment()
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.fragment_container) ?: getFragment().let {
            replaceFragmentInActivity(it, R.id.fragment_container)
        }
    }

    abstract fun getFragment(): Fragment
}
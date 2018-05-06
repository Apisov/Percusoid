package com.apisov.percusoid.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class AutoClearedValues(private val fragment: Fragment?) {
    private val autoClearedValues: MutableList<Any> = mutableListOf()

    private val fragmentLifecycleCallbacks: FragmentManager.FragmentLifecycleCallbacks  by lazy {
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentDestroyed(fm: FragmentManager?, f: Fragment?) {
                autoClearedValues.clear()
                fragment?.fragmentManager?.unregisterFragmentLifecycleCallbacks(this)
            }
        }
    }

    fun add(value: Any) {
        if (autoClearedValues.isEmpty()) {
            fragment?.fragmentManager?.registerFragmentLifecycleCallbacks(
                fragmentLifecycleCallbacks,
                false
            )
        }
        autoClearedValues.add(value)
    }
}
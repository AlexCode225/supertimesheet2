package org.didong.didong

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 * Hold first level navigation on didong
 */
class MainNavigationListener(val activity : AppCompatActivity, val drawerLayout: DrawerLayout ) : NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_main -> {
                val mainIntent = Intent(activity, MainActivity::class.java)
                activity.startActivity(mainIntent)
                return true
            }
            R.id.nav_report -> {
                val reportIntent = Intent(activity, ReportActivity::class.java)
                activity.startActivity(reportIntent)
                return true
            }
            R.id.nav_settings -> {
                val prefIntent = Intent(activity, SettingsActivity::class.java)
                activity.startActivity(prefIntent)
                return true
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}
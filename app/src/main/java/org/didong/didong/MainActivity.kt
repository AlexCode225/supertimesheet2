package org.didong.didong

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import org.didong.didong.events.EventsRecyclerAdapter
import android.content.Intent
import android.support.v7.widget.CardView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import org.didong.didong.events.EventDetailService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DataChangeEventListener {
    var evtRecyclerAdapter : EventsRecyclerAdapter? = null
    val evtService = EventDetailService.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR), 0);
        }

        val currentDateCard = findViewById(R.id.currentdate_card) as CardView
        val currentDate = findViewById(R.id.currentDate) as EditText
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        initEditTextWithTodaysDate(currentDate, dateFormat)
        currentDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(newText: CharSequence?, startPos: Int, endPos: Int, length: Int) {
                val currentDateStr = newText.toString()
                try {
                    val selectedCurrentDate = dateFormat.parse(currentDateStr)
                    evtService.currentDate = selectedCurrentDate
                    evtService.refreshCurrentEventList(this@MainActivity)
                } catch(parseException: ParseException) {
                    // Do nothing because the date input could be edited and partial
                    // if the input is finished thus the date is changed and the change is apply
                }
            }
        })

        val currentDatePicker = findViewById(R.id.currentDatePicker) as DatePicker


        val chooseCurrentDate = findViewById(R.id.chooseCurrentDate) as Button
        chooseCurrentDate.setOnClickListener {
            if(currentDatePicker.visibility == View.VISIBLE) {
                currentDatePicker.visibility = View.GONE
            } else {
                // Init date picker to date of today
                // TODO : Should init with the value of current date field
                val today = Calendar.getInstance()
                currentDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), { _, year, monthOfYear, dayOfMonth ->
                    currentDate.setText("$year-${monthOfYear+1}-$dayOfMonth")
                })
                currentDatePicker.visibility = View.VISIBLE
            }
        }

        val goToday = findViewById(R.id.goToday)
        goToday.setOnClickListener {
            initEditTextWithTodaysDate(currentDate, dateFormat)
        }

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            evtService.createEvent(this)
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val eventsView = findViewById(R.id.events_view) as RecyclerView
        evtRecyclerAdapter = EventsRecyclerAdapter(this)
        eventsView.adapter = evtRecyclerAdapter
        eventsView.layoutManager = LinearLayoutManager(this)
        EventDetailService.instance.listeners.add(this)
    }

    private fun initEditTextWithTodaysDate(editText: EditText, dateFormat: SimpleDateFormat) {
        val today = Calendar.getInstance()
        editText.setText(dateFormat.format(today.time), TextView.BufferType.EDITABLE)
    }

    override fun dataChange(newObject: Any) {
        evtRecyclerAdapter?.reloadEvents()
        evtRecyclerAdapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                val prefIntent = Intent(this, SettingsActivity::class.java)
                startActivity(prefIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
package activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.ams.bookhub.*
import com.google.android.material.navigation.NavigationView
import fragment.DashboardFragment
import fragment.appinfo
import fragment.favrouite
import fragment.profile

class MainActivity : AppCompatActivity() {
    lateinit var dlayout: DrawerLayout
    lateinit var clayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frm: FrameLayout
    lateinit var nav: NavigationView
    var previousitem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dlayout = findViewById(R.id.drawlayout)
        clayout = findViewById(R.id.coorlayout)
        frm = findViewById(R.id.frmlay)
        toolbar = findViewById(R.id.toolbar)
        nav = findViewById(R.id.navlayout)
        setUpToolBar()
        opendashboard()
        val togleham = ActionBarDrawerToggle(
            this@MainActivity,
            dlayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        dlayout.addDrawerListener(togleham)
        togleham.syncState()

        //---------------------------------------------------------------------
        nav.setNavigationItemSelectedListener{
            if(previousitem!=null){
                previousitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousitem=it


            when (it.itemId){
                R.id.dhashboard -> {
                   opendashboard()
                    Toast.makeText(this@MainActivity,"dasboard",Toast.LENGTH_LONG).show()
                    dlayout.closeDrawers()
                }
                R.id.profile -> {
                    supportActionBar?.title="profile"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frmlay,
                        profile()
                    ).commit()
                    Toast.makeText(this@MainActivity,"profile",Toast.LENGTH_LONG).show()
                    dlayout.closeDrawers()
                }
                R.id.fav -> {
                    supportActionBar?.title="favrouites"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frmlay,
                        favrouite()
                    ).commit()
                    Toast.makeText(this@MainActivity,"fav",Toast.LENGTH_LONG).show()
                    dlayout.closeDrawers()
                }
                R.id.abt -> {
                    supportActionBar?.title="About app"
                    supportFragmentManager.beginTransaction().replace(
                        R.id.frmlay,
                        appinfo()
                    ).commit()
                    Toast.makeText(this@MainActivity,"abt app",Toast.LENGTH_LONG).show()
                    dlayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }
    fun setUpToolBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            dlayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun opendashboard(){
        supportFragmentManager.beginTransaction().replace(
            R.id.frmlay,
            DashboardFragment()
        ).addToBackStack("dashboard").commit()
        supportActionBar?.title="dashboard"
        nav.setCheckedItem(R.id.dhashboard)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frmlay)
        when(frag){
            !is DashboardFragment ->opendashboard()

            else->{
                super.onBackPressed()
                dlayout.closeDrawers()

            }
        }
    }
}
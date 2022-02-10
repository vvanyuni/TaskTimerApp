package com.vvanyuni.tasktimer
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.vvanyuni.tasktimer.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), AddEditFragment.OnSaveClicked, MainActivityFragment.OnTaskEdit {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainActivityBinding: ActivityMainBinding

    // Whether the activity is in 2-pane mode
    private var mTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate:starts")
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)
        setContentView(mainActivityBinding.root)
        setSupportActionBar(mainActivityBinding.toolbar)

        mTwoPane = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        Log.d(TAG, "onCreate: twoPane is $mTwoPane")

        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment != null) {
            //There was an existing fragment to edit a task, make sure panes are set correctly
                showEditPane()

        } else {
            task_details_container.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
            mainActivityBinding.mainContent.mainFragment.visibility = View.VISIBLE
        }
        Log.d(TAG,"onCreate: finished")
    }

    private fun showEditPane() {
        task_details_container.visibility = View.VISIBLE
        //hide the left hand pane, if in single pane view
        mainActivityBinding.mainContent.mainFragment.visibility = if (mTwoPane) View.VISIBLE else View.GONE
    }


    private fun removeEditPane(fragment: Fragment? = null) {
        Log.d(TAG, "removeEditPane: Starts")
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
        // set the visibility of the right hand pane
        mainActivityBinding.mainContent.taskDetailsContainer.visibility = if (mTwoPane) View.INVISIBLE else View.GONE
        // and show the left hand pane
        mainActivityBinding.mainContent.mainFragment.visibility = View.VISIBLE

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSaveClicked() {
        Log.d(TAG, "onSaveClicked: called")
        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        removeEditPane(fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.menumain_addTask -> taskEditRequest(null)
//            R.id.menumain_settings -> true
            android.R.id.home -> {
                Log.d(TAG, "onOptionsItemSelected: home button pressed")
                val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
                removeEditPane(fragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onTaskEdit(task: Task) {
        taskEditRequest(task)
    }

    private fun taskEditRequest(task: Task?) {
        Log.d(TAG, "taskEditRequest: starts")

        // Create a new fragment to edit the task
        val newFragment = AddEditFragment.newInstance(task)
        supportFragmentManager.beginTransaction()
            .replace(R.id.task_details_container, newFragment)
            .commit()

        showEditPane()
        Log.d(TAG, "Exiting taskEditRequest")
    }

    override fun onBackPressed() {

        val fragment = supportFragmentManager.findFragmentById(R.id.task_details_container)
        if (fragment == null || mTwoPane) {
            super.onBackPressed()
        } else {
            removeEditPane(fragment)
        }
    }

    // MainActivity Lifecycle callback events - added for logging only
    override fun onStart() {
        Log.d(TAG, "onStart: called")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState: called")
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: called")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }
}
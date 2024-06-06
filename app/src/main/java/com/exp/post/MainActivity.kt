package com.exp.post

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.exp.post.databinding.ActivityMainBinding
import com.exp.post.dbs.ObjectBox
import com.exp.post.dbs.User
import com.exp.post.dbs.User_
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        supportActionBar?.hide()
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val userBox = ObjectBox.store.boxFor(User::class.java)
//        val user = User(name = "fffff")
//        userBox.put(user)
        val all = userBox.all
        Log.d(TAG, "onCreate: all=$all")
//        val users: List<User> = getNewUsers()
//        userBox.put(users)
//        val user = userBox[userId]
//
//        val users = userBox.all
        val query = userBox
            .query(User_.name.equal("fffff"))
            .order(User_.name)
            .build()
        val results = query.find()
        Log.d(TAG, "onCreate: results=$results")
        query.close()

//        val isRemoved = userBox.remove(userId)
//
//        userBox.remove(users)
//// alternatively:
//        userBox.removeByIds(userIds)
//
//        userBox.removeAll()
        //val userCount = userBox.count()
//        store.callInTxAsync(object :Callable<String>{
//            override fun call(): String {
//               val box = ObjectBox.store.boxFor(User::class.java)
//                val name = box.get(2).name;
//                box.remove(1);
//                return "text";
//            }
//
//        }) { result, error ->
//            if (error != null) {
//                System.out.println("Failed to remove user with id " + "userId");
//            } else {
//                System.out.println("Removed user with name: " + result);
//            }
//        }

        navView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (item.itemId==R.id.navigation_dashboard){
                    MovieListActivity.navMovieListActivity(this@MainActivity)
                    return false
                }else{
                    NavigationUI.onNavDestinationSelected(item, navController)
                 return true
                }
            }

        })
//        navView.
    }
    companion object{
        const val TAG="MainActivity"
    }
}
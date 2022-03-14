package org.textbox.testbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import org.textbox.testbox.databinding.ActivityNavBinding


class Nav_Activity : AppCompatActivity() {

    private lateinit var binding : ActivityNavBinding

    private lateinit var toggle : ActionBarDrawerToggle

    private lateinit var drawerLayout:DrawerLayout

    //Firebase Auth
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)


        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Nav_Activity"

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId){

                R.id.navHome -> Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show()
                R.id.navClubs -> Toast.makeText(this,"Clubs",Toast.LENGTH_SHORT).show()
                R.id.navNoticeBoard -> Toast.makeText(this,"Notice Board`",Toast.LENGTH_SHORT).show()
                R.id.navLogout -> logoutUser()
                R.id.navTeamups -> replaceFragment(TeamCollabFragment(),it.title.toString())

            }
            true
        }
        fireAuth = FirebaseAuth.getInstance()
        getData()
    }

    private fun logoutUser(){
        fireAuth.signOut()
        checkUser()
    }

    private fun checkUser() {
        //For Logged in user get the user info and key
        val firebaseUser = fireAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun replaceFragment(fragment: Fragment, title:String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getData(){
        val navView : NavigationView = findViewById(R.id.nav_view)
        val cUser = fireAuth.currentUser
        val uid = cUser?.uid.toString()

        var ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(uid).get().addOnSuccessListener {
            if(it.exists()){
                val firstName = it.child("firstName").value.toString()
                val email = it.child("email").value.toString()
                
                val firstNav : TextView = navView.getHeaderView(0).findViewById(R.id.navHeaderUsername)
                val emailNav : TextView = navView.getHeaderView(0).findViewById(R.id.navHeaderEmail)

                firstNav.text = firstName
                emailNav.text = email
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Unable to read Data",Toast.LENGTH_SHORT).show()
        }
    }
}



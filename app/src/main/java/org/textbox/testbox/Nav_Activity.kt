package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
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



    //Firebase Auth
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase


    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        textView = findViewById(R.id.textView)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Nav_Activity"

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.navHome -> Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show()
                R.id.navClubs -> Toast.makeText(this,"Clubs",Toast.LENGTH_SHORT).show()
                R.id.navNoticeBoard -> Toast.makeText(this,"Notice Board`",Toast.LENGTH_SHORT).show()
                R.id.navLogout -> Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show()

            }

            true
        }
        fireAuth = FirebaseAuth.getInstance()
        getData()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getData(){
        val firebaseUser =  fireAuth.currentUser
        val uid = firebaseUser?.uid
        val ref= FirebaseDatabase.getInstance().getReference("User")
        if (uid != null) {
            ref.child(uid).get().addOnSuccessListener {
                val firstname = it.child("firstName").value.toString()
                textView.text = firstname
            }
        }
    }
}



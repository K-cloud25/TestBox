package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import org.textbox.testbox.databinding.ActivityNavBinding

class Nav_Activity : AppCompatActivity() {

    private lateinit var binding : ActivityNavBinding

    private lateinit var toggle : ActionBarDrawerToggle

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Nav_Activity"

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.navHome -> Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show()
                R.id.navClubs -> Toast.makeText(this,"Clubs",Toast.LENGTH_SHORT).show()
                R.id.navNoticeBoard -> Toast.makeText(this,"Notice Board",Toast.LENGTH_SHORT).show()
                R.id.navLogout -> logoutOfAccount()

            }

            true
        }

    }

    private fun logoutOfAccount(){
        Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

package org.textbox.testbox

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.*
import org.textbox.testbox.databinding.ActivityNavBinding
import java.io.File


class Nav_Activity : AppCompatActivity(), RequestCommunicator {

    private lateinit var firebaseStorage: FirebaseStorage

    lateinit var ImageUri: Uri

    private lateinit var binding : ActivityNavBinding

    private lateinit var toggle : ActionBarDrawerToggle

    private lateinit var drawerLayout:DrawerLayout

    private var IsUser : Boolean = true


    //Firebase Auth
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Nav_Activity"

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId){

                R.id.navHome -> replaceFragment(mainForum_Fragment(),"Forum")
                R.id.navClubs -> Toast.makeText(this,"Clubs",Toast.LENGTH_SHORT).show()
                R.id.navNoticeBoard -> changeNoticeBoard()
                R.id.navAssignment -> startassignment()
                R.id.navLogout -> logoutUser()
                R.id.navTeamups -> replaceFragment(TeamCollabFragment(),it.title.toString())

            }
            true
        }
        fireAuth = FirebaseAuth.getInstance()
        getData()
        disppic()
    }
    private fun startassignment(){
        val intent= Intent(this,assignment::class.java)

        startActivity(intent)
    }

    private fun logoutUser(){
        fireAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
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
        //for nav activity "name=navView.getheaderindex0.find by id (info)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val cUser = fireAuth.currentUser
        val uid = cUser?.uid.toString()

        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(uid).get().addOnSuccessListener {
            if(it.exists()){

                IsUser = true
                Toast.makeText(this,IsUser.toString(),Toast.LENGTH_SHORT).show()

                val firstName = it.child("firstName").value.toString()
                val email = it.child("email").value.toString()

                val firstNav : TextView = navView.getHeaderView(0).findViewById(R.id.navHeaderUsername)
                val emailNav : TextView = navView.getHeaderView(0).findViewById(R.id.navHeaderEmail)

                firstNav.text = firstName
                emailNav.text = email
            }else{

                IsUser = false
                Toast.makeText(this,IsUser.toString(),Toast.LENGTH_SHORT).show()

                val DBref = FirebaseDatabase.getInstance().getReference("Clubs")
                DBref.child(uid).get().addOnSuccessListener {
                    if(it.exists()){
                        val firstName = it.child("clubName").value.toString()
                        val email = it.child("clubEmail").value.toString()

                        val firstNav : TextView = navView.getHeaderView(0).findViewById(R.id.navHeaderUsername)
                        val emailNav : TextView = navView.getHeaderView(0).findViewById(R.id.navHeaderEmail)

                        firstNav.text = firstName
                        emailNav.text = email
                    }
                }
            }
        }.addOnFailureListener {

        }
    }

    private fun disppic(){
        val navView : NavigationView = findViewById(R.id.nav_view)

        val filename = fireAuth.currentUser?.uid
        val Storageref = FirebaseStorage.getInstance().getReference("ProfilePic/$filename")

        val localfile=File.createTempFile("uid","jpg")
        val profile : de.hdodenhof.circleimageview.CircleImageView = navView.getHeaderView(0).findViewById(
            R.id.profilePic)

        Storageref.getFile(localfile).addOnSuccessListener {
            val bitmap=BitmapFactory.decodeFile(localfile.absolutePath)
           profile.setImageBitmap(bitmap)
        }

        profile.setOnClickListener {
            if(IsUser){
                val intent = Intent(this,ChangeProfile::class.java)
                startActivity(intent)
            }else{
                startActivity(Intent(this,ChangeClubProfile::class.java))
            }
        }
    }

    override fun changeFragment(fragment: Fragment,title :String){
        replaceFragment(fragment,title)
    }

    private fun changeNoticeBoard(){
        getData()
        if(IsUser){
            replaceFragment(NoticeBoard_User_Fragment(),"NOTICE BOARD")
        }else{
            replaceFragment(NoticeBoard_Club_Fragment(),"NOTICE BOARD")
        }
    }
}


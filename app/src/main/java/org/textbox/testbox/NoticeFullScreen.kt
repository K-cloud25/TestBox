package org.textbox.testbox

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.databinding.ActivityNoticeFullScreenBinding
import java.io.File

class NoticeFullScreen : AppCompatActivity() {

    private lateinit var binding : ActivityNoticeFullScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeFullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val noticeID = intent.getStringExtra("PosterId").toString()
        putphoto(noticeID)

        var title : String = ""
        var descrip : String = ""
        var links : String = ""
        var extra : String = ""

        val Dbref = FirebaseDatabase.getInstance().getReference("Notice").child(noticeID)
            .get()
            .addOnSuccessListener {
                if(it.exists()){
                    title = it.child("title").value.toString()
                    descrip = it.child("description").value.toString()
                    links = it.child("links").value.toString()
                    extra = it.child("extraInfo").value.toString()
                    binding.MainTitle.text = title
                    binding.MainLinks.text = links
                    binding.MainDescrip.text = descrip
                    binding.MainExt.text = extra

                }
            }

    }

    private fun putphoto(noticeID : String){
        val Storageref = FirebaseStorage.getInstance().getReference("Notice/$noticeID")
        val localfile= File.createTempFile("uid","jpg")
        Storageref.getFile(localfile).addOnSuccessListener {
            val bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
            binding.NoticemainImage.setImageBitmap(bitmap)
        }
    }
}
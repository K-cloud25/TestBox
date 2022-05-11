package org.textbox.testbox

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.typeOf

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NoticeBoard_Club_Fragment : Fragment() {

    private lateinit var PlusBtn : ImageView

    private lateinit var previewPhoto : ImageView

    lateinit var ImageUri : Uri

    private lateinit var noticeRV : RecyclerView
    private lateinit var noticeList : ArrayList<Notice_Class>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_notice_board__club_, container, false)

        //Getting Plus Button
        PlusBtn  = view.findViewById(R.id.addNewNotice)
        PlusBtn.setOnClickListener {
            AddnewNotice(view.context)
        }

        noticeRV = view.findViewById(R.id.noticeRV)

        setupRV()

        return view
    }

    private fun AddnewNotice(context : Context){
        val window = AlertDialog.Builder(context)
        val view_window = layoutInflater.inflate(R.layout.popup_new_notice_layout,null)

        window.setView(view_window)
        val root = window.create()
        root.show()

        previewPhoto= root.findViewById(R.id.previewImage)
        val uploadphotoBtn : Button = root.findViewById(R.id.popUpaddBtn)
        val title : EditText = root.findViewById(R.id.noticeTitle)
        val links   : EditText = root.findViewById(R.id.noticeLinks)
        val descript  : EditText = root.findViewById(R.id.noticeDescrip)
        val extraInfo : EditText = root.findViewById(R.id.noticeInfo)
        val uploadBtn : Button =  root.findViewById(R.id.noticeUploadBtn)

        uploadphotoBtn.setOnClickListener { SelectImage() }

        uploadBtn.setOnClickListener {
            val _title : String = title.text.toString()
            val _links : String = links.text.toString()
            val _desc : String = descript.text.toString()
            val _ext : String = extraInfo.text.toString()

            uploadtoDB(root,root.context,_title,_links,_desc,_ext)
        }
    }

    private fun SelectImage() {
        val intent= Intent()
        intent.type="image/"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode==100 && resultCode== AppCompatActivity.RESULT_OK){
            ImageUri=data?.data!!
            previewPhoto.setImageURI(ImageUri)
        }
    }

    private fun UploadImage(alertD : AlertDialog,root : Context,notiID : String){

        val progress= ProgressDialog(root)
        progress.setMessage("Uploading File ...")
        progress.setCancelable(false)
        progress.show()
        val filename=notiID
        val Storageref = FirebaseStorage.getInstance().getReference("Notice/$filename")
        if( ImageUri!=null){
            Storageref.putFile(ImageUri).addOnSuccessListener {
                previewPhoto.setImageURI(ImageUri)
                progress.dismiss()
                alertD.dismiss()
            }}
    }

    private fun uploadtoDB(alertD : AlertDialog,root : Context, Title:String, Links:String, Descrip:String, Ext:String){

        val firebaseAuth : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val noticeID = Title+firebaseAuth

        UploadImage(alertD,root,noticeID)

        val calender : Calendar = Calendar.getInstance()
        val sampleDate = SimpleDateFormat("dd-MM")
        val date = sampleDate.format(calender.time)

        val obj = Notice_Class(noticeID,Title,Links,Descrip,Ext,date)

        val Dbref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Notice")

        Dbref.child(noticeID).setValue(obj)

    }

    private fun setupRV(){

        val ref = FirebaseDatabase.getInstance().getReference("Notice")
        noticeRV.layoutManager = LinearLayoutManager(view?.context)
        noticeRV.setHasFixedSize(true)

        noticeList = arrayListOf<Notice_Class>()

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    noticeList.clear()
                    for(requestSnapShot in snapshot.children){
                        val request = requestSnapShot.getValue(Notice_Class::class.java)
                        noticeList.add(request!!)
                    }
                    noticeRV.adapter = Notice_Adapter(noticeList)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoticeBoard_Club_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
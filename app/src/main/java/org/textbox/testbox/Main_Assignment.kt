package org.textbox.testbox

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.databinding.ActivityMainAssignmentBinding
import java.util.*

class Main_Assignment : AppCompatActivity() {

    private lateinit var binding : ActivityMainAssignmentBinding
    private lateinit var assRV : RecyclerView
    private lateinit var dbref : DatabaseReference

    private lateinit var pdfArraylist : ArrayList<pdfclass>

    lateinit var uri: Uri
    val PDF :Int=0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val yearPath = intent.getStringExtra("yearPath").toString()
        val branchPath = intent.getStringExtra("branchPath").toString()
        val branchName = intent.getStringExtra("branch").toString()
        val view = layoutInflater.inflate(R.layout.pop_add_assignment_layout,null)
        pdfArraylist = arrayListOf<pdfclass>()

        val Dbref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Assignment/$yearPath/$branchName")



        gettitle()

        val _adapter = pdfAdapter(pdfArraylist)
        binding.assignRV.adapter = _adapter


        binding.addBtn.setOnClickListener {
            AddPDF(binding.root.context,yearPath,branchPath,branchName)
        }
    }

    private fun gettitle() {
        val yearPath = intent.getStringExtra("yearPath").toString()
        val branchName = intent.getStringExtra("branch").toString()

        val Dbref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Assignment/$yearPath/$branchName")

        Dbref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(pdfclass::class.java)
                        pdfArraylist.add(user!!)

                    }
                    assRV.adapter=pdfAdapter(pdfArraylist)

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


    private fun SelectPDF() {

        val intent = Intent()
        intent.setType ("application/pdf")
        intent.setAction (Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent,"Select PDF"),PDF)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(resultCode== AppCompatActivity.RESULT_OK){
            if(requestCode==PDF) {
                uri = data?.data!!
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadDB(alertD: AlertDialog, root: Context, Title:String, notes:String){

        val firebaseAuth : String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val pdfID = Title+firebaseAuth
        val yearPath = intent.getStringExtra("yearPath").toString()
        val branchName = intent.getStringExtra("branch").toString()

        Uploadpdf(alertD,root,pdfID)

        val obj = Ass_Class(pdfID,Title,notes)

        val Dbref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Assignment/$yearPath/$branchName")

        Dbref.child(pdfID).setValue(obj)

    }

    private fun Uploadpdf(alertD: AlertDialog, root: Context, notiID: String){

        val progress= ProgressDialog(root)
        progress.setMessage("Uploading File ...")
        progress.setCancelable(false)
        progress.show()
        val yearPath = intent.getStringExtra("yearPath").toString()
        val branchName = intent.getStringExtra("branch").toString()
        val filename=notiID
        val Storageref = FirebaseStorage.getInstance().getReference("Assignment/$yearPath/$branchName/$filename")
        if( uri!=null){
            Storageref.putFile(uri).addOnSuccessListener {

                progress.dismiss()
                alertD.dismiss()
            }}
    }


    private fun AddPDF(context : Context,yearPath : String,branchPath : String,branchName : String){

        val window = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.pop_add_assignment_layout,null)

        window.setView(view)
        val root = window.create()
        root.show()

        val YearTV : TextView? = root.findViewById(R.id.yearPathTV)
        YearTV?.text = "Year : $yearPath"

        val branchTV : TextView? = root.findViewById(R.id.branchPathTV)
        branchTV?.text = "Branch : $branchName"

        val postpdf : Button? = root.findViewById(R.id.postPdfBtn)
        val assname : EditText? = root.findViewById(R.id.assignNameET)
        val assnotes   : EditText? = root.findViewById(R.id.noteRV)


        val uploadBtn : Button? = root.findViewById(R.id.uploadPdfBtn)
        if (uploadBtn != null) {
            uploadBtn.setOnClickListener{
                SelectPDF()
            }
        }
        if (postpdf != null) {
            postpdf.setOnClickListener {
                val _assname : String = assname?.text.toString()
                val _assnotes : String = assnotes?.text.toString()
                uploadDB(root,root.context,_assname,_assnotes)
            }
        }
    }


}


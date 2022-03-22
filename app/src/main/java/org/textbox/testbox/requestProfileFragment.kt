package org.textbox.testbox

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class requestProfileFragment : Fragment() {

    private var projectNames = ArrayList<String>()
    private var projectIds = ArrayList<String>()

    private lateinit var projectSpinner : Spinner

    private lateinit var mainSelectedID : String

    private lateinit var delistBtn : Button

    private lateinit var applicantRV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_request_profile, container, false)

        applicantRV = view.findViewById(R.id.applicantsListRV)

        delistBtn = view.findViewById(R.id.delistBtn)
        delistBtn.isEnabled = false
        delistBtn.setOnClickListener { delistProject() }

        projectSpinner = view.findViewById(R.id.projectList)
        spinnerSetUp(view.context)

        return view
    }

    private fun spinnerSetUp(context : Context){

        val Dbref = FirebaseDatabase.getInstance().getReference("teamUps")
        var spinnerAdapter : ArrayAdapter<String>

        Dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    projectNames.clear()
                    projectIds.clear()
                    for(requestSnapShot in snapshot.children){
                        val request = requestSnapShot.getValue(requestClass::class.java)
                        val requestID = request?.requestID.toString()
                        if(requestID.contains("_")){
                            val userID = requestID.split("_")[1]
                            if(userID == FirebaseAuth.getInstance().currentUser?.uid.toString()){
                                projectNames.add(request?.projectName.toString())
                                projectIds.add(requestID)
                            }
                        }
                    }
                    if (projectNames.isEmpty()){
                        Toast.makeText(view?.context,"No projects Listed",Toast.LENGTH_SHORT).show()
                    }
                    spinnerAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,projectNames)
                    projectSpinner.adapter = spinnerAdapter
                    projectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            val projectID = projectIds[p2]
                            mainSelectedID = projectID
                            delistBtn.isEnabled = true
                            getApplicantsList(context,mainSelectedID)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Toast.makeText(context,"No Project Selected",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    private fun delistProject(){
        val DBref = FirebaseDatabase.getInstance().getReference("teamUps")
        DBref.child(mainSelectedID).removeValue()
    }

    private fun getApplicantsList(context: Context , projectID:String){
        val DBref = FirebaseDatabase.getInstance().getReference("teamUps").child(projectID)
        val applicationList = ArrayList<String>()

        DBref.child("appliedID").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val IDs = snapshot.value.toString().split(",")
                    for(i in IDs.indices){
                        var applicantID = IDs[i].split("=")[1].trim()

                        if(applicantID.startsWith("{")){
                            applicantID = applicantID.drop(1)
                        }
                        if(applicantID.endsWith("}")){
                            applicantID = applicantID.dropLast(1)
                        }

                        applicationList.add(applicantID)
                    }
                    setupRV(context,applicationList,projectID)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setupRV(context: Context,applicationList : ArrayList<String>,projectID : String){
        var DBref = FirebaseDatabase.getInstance().getReference("user")
        applicantRV.layoutManager = LinearLayoutManager(context)
        applicantRV.setHasFixedSize(true)

        var applicantList = arrayListOf<applicantClass>()
        applicantList.clear()
        for (i in applicationList.indices) {
            DBref.child(applicationList[i]).get().addOnSuccessListener {
                val email = it.child("email").value.toString()
                val firstName = it.child("firstName").value.toString()
                val id = it.child("id").value.toString()
                val lastName = it.child("lastName").value.toString()


                val applicantObj = applicantClass(email, firstName, id, lastName)
                applicantObj.changeProjectID(projectID)
                applicantList.add(applicantObj)
                applicantRV.adapter = applicantAdapter(applicantList)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            requestProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
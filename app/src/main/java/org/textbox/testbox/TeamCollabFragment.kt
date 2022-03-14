package org.textbox.testbox

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TeamCollabFragment : Fragment() {

    private lateinit var teamupRView : RecyclerView
    private lateinit var reqArray : ArrayList<requestClass>

    private lateinit var plusButton : ImageView
    private lateinit var contactBtn : CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_collab, container, false)

        teamupRView = view.findViewById(R.id.teamUpRV)
        plusButton = view.findViewById(R.id.addNewRequest)
        contactBtn = view.findViewById(R.id.userRequestCheck)

        plusButton.setOnClickListener {
            Toast.makeText(view.context,"Add Request", Toast.LENGTH_SHORT).show()
        }

        contactBtn.setOnClickListener {
            Toast.makeText(view.context,"Check Responses", Toast.LENGTH_SHORT).show()
        }

        setUpRV()

        return view
    }

    private fun setUpRV(){

        val ref = FirebaseDatabase.getInstance().getReference("teamUps")
        teamupRView.layoutManager = LinearLayoutManager(view?.context)
        teamupRView.setHasFixedSize(true)

        reqArray = arrayListOf<requestClass>()

        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(requestSnapShot in snapshot.children){
                        val request = requestSnapShot.getValue(requestClass::class.java)
                        reqArray.add(request!!)
                    }
                    teamupRView.adapter = MyAdapter(reqArray)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            TeamCollabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
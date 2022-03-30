package org.textbox.testbox

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.textbox.testbox.R
import java.util.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NoticeBoard_User_Fragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_notice_board__user_, container, false)
        noticeRV = view.findViewById(R.id.noticeRV)

        setupRV()
        return view
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
            NoticeBoard_User_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
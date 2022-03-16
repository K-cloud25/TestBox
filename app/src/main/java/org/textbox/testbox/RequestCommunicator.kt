package org.textbox.testbox

import androidx.fragment.app.Fragment

interface RequestCommunicator {
    fun changeFragment(fragment: Fragment,title:String)
}
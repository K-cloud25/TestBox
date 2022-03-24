package org.textbox.testbox.classes

data class applicantClass(
    val email : String? = null,
    val firstName : String? = null,
    val id : String? = null,
    val lastName : String? = null,
    var projectID : String? = null
){

    fun changeProjectID(_projectID : String){
        projectID = _projectID
    }
}

package eu.tutorials.realtimedatabase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import eu.tutorials.realtimedatabase.ui.theme.RealtimeDatabaseTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealtimeDatabaseTheme {
                RealTimeDatabase()
            }
        }
    }
}

@Composable
fun RealTimeDatabase() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Student")

    val context = LocalContext.current
    var rollno by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text("Enter Student information." ,
            fontSize = 16.sp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value =rollno , onValueChange = {rollno=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
            Text("enter student rollno." , fontSize = 16.sp, color = Color.Yellow)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value =course , onValueChange = {course=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text("enter student course" , fontSize = 16.sp, color = Color.Yellow)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value =name , onValueChange = {name=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text("enter student name" , fontSize = 16.sp, color = Color.Yellow)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = {email=it},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text("Enter Your Email" , fontSize = 16.sp, color = Color.Yellow)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(){

            OutlinedButton(onClick = {
                if(rollno.isNotEmpty() && name.isNotEmpty() && course.isNotEmpty()){
                    val sinfo = StudentInfo(rollno.toInt(),name,course,email)
                        myRef.child(name).setValue(sinfo).addOnSuccessListener {
                            rollno =""
                            name=""
                            course=""
                            email=""
                            Toast.makeText(context, "Recode Inserted", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context,"Please enter value first",Toast.LENGTH_SHORT).show()
                        }
                }else{
                    Toast.makeText(context , "Please Insert Value first",Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Insert", fontSize = 16.sp, color = Color.Cyan)
            }


            OutlinedButton(onClick = {

//                specefic record found using this
//                myRef.child(Swapnil Dhumal).get()



//                this is ues for fetch all data from the data base table
                myRef.get().addOnSuccessListener {
                    val data = StringBuffer()
                    if(it.exists()){
                        it.children.forEach {
                            data.append("Students Roll No="+it.child("rollno").value)
                            data.append("Students Name="+it.child("name").value)
                            data.append(" Student Course="+it.child("course").value)
                            data.append("Email="+it.child("email").value)
                        }
                    }
                    Toast.makeText(context , "Display Value",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context , "Please Insert Value first",Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Display", fontSize = 16.sp, color = Color.Cyan)
            }


            OutlinedButton(onClick = {
                
            }) {
                Text("Update", fontSize = 16.sp, color = Color.Cyan)
            }

            OutlinedButton(onClick = {}) {
                Text("Display", fontSize = 16.sp, color = Color.Cyan)
            }
        }
    }
}

data class StudentInfo(val rollno:Int, val name: String, val course:String, val email: String)
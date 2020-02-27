package com.whatsdue.whatsdue2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.whatsdue.whatsdue2.dataRemote.CourseServiceFactory
import com.whatsdue.whatsdue2.restmodels.Course
import com.whatsdue.whatsdue2.restmodels.responseModel.Wrapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    lateinit var myAPI: INodeJS
    var compositeDisposable = CompositeDisposable()

    companion object {var courseList = mutableListOf<Course>()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG, "onCreate called")

        val retrofit = RetrofitClient.instance
        var userName = findViewById<EditText>(R.id.userNameText)
        var userPassword = findViewById<EditText>(R.id.userPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)

        myAPI = retrofit.create(INodeJS::class.java)

        loginButton.setOnClickListener {
            Log.d(TAG, "loginButton Pressed")
            login(userName.text.toString(),userPassword.text.toString())
        }
    }

    private fun login(username: String, password: String) {
        compositeDisposable.add(myAPI.loginUser(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
                if(message.contains("username")) {
                    var jsonString = message.toString()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("value", jsonString)
                    loadCourses()
                    startActivity(intent)
                    finish()
                }
                else
                    Toast.makeText(this@LoginActivity, message.toString(), Toast.LENGTH_SHORT).show()
            })
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    private fun loadCourses() {
        var i: List<Course?>? = null;
        CourseServiceFactory.makeService().getCoursesList().enqueue(object: Callback<Wrapper?> {
            override fun onFailure(call: Call<Wrapper?>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "WTF ", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Wrapper?>, response: Response<Wrapper?>) {
                val items = response.body()!!.embed.courses
                //items.stream().map(Course::name).forEach(::println)
                courseList.addAll(items)
                courseList.forEach(::println)

            }

        })
    }
}

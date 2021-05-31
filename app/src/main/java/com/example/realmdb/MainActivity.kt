package com.example.realmdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.arch.core.executor.TaskExecutor
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realmdb.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import io.realm.kotlin.where
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private val students = ArrayList<StudentRealm>()
    private lateinit var uiThreadRealm : Realm

    private val binding : ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main).apply {
            lifecycleOwner = this@MainActivity
        }
    }

    private lateinit var adapterStudent : StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(this)

        //open a realm
        val realmName = "my project"
        val config = RealmConfiguration.Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .name(realmName)
            .build()

        uiThreadRealm = Realm.getInstance(config)

        //initData()
        showData()

        //listener
        binding.btnDelete.setOnClickListener {
            deleteAll()
        }

        binding.btnAdd.setOnClickListener {
            if(isValidateInsert()){
                addData()
            }
        }

    }

    private fun initData(){
        val listFirstName = listOf("Alex", "John", "Jona", "Goege", "Mechal")
        val listLastName = listOf("Alex Last", "John Last", "Jona Last", "Goege Last", "Mechal Last")
        val listAge = listOf(23, 12, 44, 23, 12, 3)

        for (i in 0..4) {
            val student = StudentRealm(
                studentID = "1000$i".toInt(),
                firstName = listFirstName[i],
                lastName = listLastName[i],
                age = listAge[i],
                city = "USA",
                gender = "male"
            )
            students.add(student)
        }

        students.forEach { item ->
            uiThreadRealm.executeTransaction {
                it.copyToRealmOrUpdate(item)
            }
        }
    }

    private fun showData(){

        val students : RealmResults<StudentRealm> = uiThreadRealm.where<StudentRealm>().findAll()
        adapterStudent = StudentAdapter(students)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterStudent
        }
    }

    private fun deleteAll(){
        uiThreadRealm.executeTransaction {
            it.deleteAll()
            adapterStudent.notifyDataSetChanged()
        }

    }

    private fun nextId() : Int{
        val id = uiThreadRealm.where<StudentRealm>().max("studentID")
        return id?.toInt()?.plus(1) ?: 10001
    }

    private fun addData(){
        val addStudent = StudentRealm(
            studentID = nextId(),
            firstName = binding.edtFristname.text.toString().trim(),
            lastName = binding.edtLastname.text.toString().trim(),
            age = binding.edtAge.text.toString().trim().toInt(),
            gender = binding.edtGender.text.toString().trim(),
            city = binding.edtCity.text.toString().trim()
        )

        uiThreadRealm.executeTransaction {
            it.insertOrUpdate(addStudent)
            adapterStudent.notifyDataSetChanged()
        }
    }

    private fun isValidateInsert():Boolean{
        when {
            binding.edtFristname.text.toString().isEmpty() -> {
                return false
            }
            binding.edtLastname.text.toString().isEmpty() -> {
                return false
            }
            binding.edtAge.text.toString().isEmpty() -> {
                return false
            }
            binding.edtGender.text.toString().isEmpty() -> {
                return false
            }
            binding.edtCity.text.toString().isEmpty() -> {
                return false
            }
            else -> {
                return true
            }
        }

    }
}


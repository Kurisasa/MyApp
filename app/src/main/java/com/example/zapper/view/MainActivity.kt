package com.example.zapper.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapper.R
import com.example.zapper.adapter.PersonAdapter
import com.example.zapper.model.Person
import com.example.zapper.model.PersonResponse
import com.example.zapper.repository.api.ApiMethods
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), PersonAdapter.Listener {

    private var myAdapter: PersonAdapter? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var peopleArrayList: List<Person>? = null
    private var personID: Int = 0
    private val BASE_URL = "https://demo9790103.mockable.io/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myCompositeDisposable = CompositeDisposable()
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        people_list.layoutManager = layoutManager
    }

    private fun loadData() {
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ApiMethods::class.java)

        myCompositeDisposable?.add(requestInterface.getData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe( { result -> handleResponse(result) },
            { error -> showError(error.message) }))

    }

    private fun handleResponse(peopleList: PersonResponse) {
        //store person list on sharedPreferences
        val sharedPreferences = getSharedPreferences("PERSON", Context.MODE_PRIVATE)
        peopleArrayList = peopleList.persons
        val gson = Gson()
        val json = gson.toJson(peopleArrayList);
        val editor = sharedPreferences.edit()
        editor.putString("Set",json );
        editor.apply()

        //read stored person list from sharedPreferences
        val getdata = sharedPreferences.getString("Set", "");
        if (json.isEmpty()) {
            Toast.makeText(this,"List is Empty",Toast.LENGTH_LONG).show();
        } else {
            val type = object : TypeToken<List<Person>>() {

            }.type
            val arrPackageData: List<Person>  = gson.fromJson(getdata, type)

            //pass my data to an adapter to view it on the list
            myAdapter = PersonAdapter(arrPackageData, this)
            people_list.adapter = myAdapter
        }
    }

    private fun showError(messageError: String?) {
        Toast.makeText(this, messageError, Toast.LENGTH_LONG).show()
    }


    override fun onItemClick(person: Person) {
        personID = person.id
        Toast.makeText(this, "You clicked: ${person.name}", Toast.LENGTH_LONG).show()
    }

    fun openDetailsActivity(message: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("message", message)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        myCompositeDisposable?.clear()
    }
}
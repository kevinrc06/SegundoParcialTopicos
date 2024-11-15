package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class consultaFragment : Fragment() {
    private var listaPersonaje = mutableListOf<Personaje>()
    private lateinit var rvList : RecyclerView
    private lateinit var adapter : ListaPersonasAdapter
    private var dataBaseHelper : DataBaseHelper? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consulta, container, false)
        initComponent(view)
        initListener()
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initComponent(view :View){
        dataBaseHelper = DataBaseHelper(view.context)
        rvList =  view.findViewById(R.id.rvPersonajes)
        adapter = ListaPersonasAdapter(listaPersonaje)
        rvList.layoutManager = LinearLayoutManager(view.context)
        rvList.adapter = adapter

    }

    private fun initListener(){

        dataBaseHelper?.let {

            val lp = it.selectAllPersonajes()
            Log.i("Datos" , lp.toString())
            listaPersonaje.clear()
            listaPersonaje.addAll(lp)
            adapter.notifyDataSetChanged()

        }


    }

}
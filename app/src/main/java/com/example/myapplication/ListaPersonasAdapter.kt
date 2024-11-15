package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListaPersonasAdapter (private val personajeList : List<Personaje>) : RecyclerView.Adapter<ListaPersonajesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaPersonajesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListaPersonajesViewHolder(layoutInflater.inflate(R.layout.item_personajes,parent,false))
    }

    override fun getItemCount(): Int = personajeList.size


    override fun onBindViewHolder(holder: ListaPersonajesViewHolder, position: Int) {
        holder.render(personajeList[position])
    }
}
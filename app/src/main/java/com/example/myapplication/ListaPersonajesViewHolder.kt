package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ListaPersonajesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val context = view.context
    val tvNombre = view.findViewById<TextView>(R.id.nombrePersonaje)
    val tvDescripcion = view.findViewById<TextView>(R.id.descripcionImagen)
    val imagen = view.findViewById<ImageView>(R.id.imagenPersonaje)
    val cgTipo = view.findViewById<ChipGroup>(R.id.Tipos)

    fun render(personaje : Personaje){
        tvNombre.text = personaje.nombre
        tvDescripcion.text = personaje.descripcion
      if (personaje.principal== 1){
            generateChip("Principal")
        }
        if (personaje.secundario == 1){
            generateChip("Secundario")
        }
        if (personaje.extra == 1){
            generateChip("Extra")
        }

        setBase64ImageToImageView(personaje.imagen,imagen)


    }

    private fun generateChip(text : String  ) {

        val chip = Chip(cgTipo.context)
        chip.text = text
        cgTipo.addView(chip)
    }
    fun setBase64ImageToImageView(base64String: String, imageView: ImageView) {
        try {
            // Decodificar la cadena Base64 en un arreglo de bytes
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)

            // Convertir los bytes en un Bitmap
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            // Establecer el Bitmap en el ImageView
            imageView.setImageBitmap(bitmap)

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            // Manejar el error en caso de que la cadena Base64 sea inválida
        }
    }
}
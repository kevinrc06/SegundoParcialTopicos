package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context)  : SQLiteOpenHelper(context,"personajes.db",null,1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = (""" CREATE TABLE PERSONAJES (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            NAME TEXT,
            DESCRIPCION TEXT,
            PRINCIPAL INTEGER,
            SECUNDARIO INTEGER,
            EXTRA INTEGER,
            IMG TEXT
            ) """)
        p0?.execSQL(createTable)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }


     fun insertPersonaje(name : String, description : String, principal: Int, secundario :Int, otros : Int, img : String) : String{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put("NAME", name)
        contentValues.put("DESCRIPCION", description)
         contentValues.put("PRINCIPAL", principal)
         contentValues.put("SECUNDARIO", secundario)
         contentValues.put("EXTRA", otros)
         contentValues.put("IMG", img)

        val result = db.insert("PERSONAJES", null, contentValues  )
        return if (result == (-1).toLong() ) "Existe una falla" else "Inserción correcta"
    }


    fun selectAllPersonajes(): MutableList<Personaje>{
        val namesList = mutableListOf<Personaje>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM PERSONAJES", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("NAME"))
                val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION"))
                val principal = cursor.getInt(cursor.getColumnIndexOrThrow("PRINCIPAL"))
                val secundario = cursor.getInt(cursor.getColumnIndexOrThrow("SECUNDARIO"))
                val extra = cursor.getInt(cursor.getColumnIndexOrThrow("EXTRA"))
                val imagen = cursor.getString(cursor.getColumnIndexOrThrow("IMG"))
                val personaje = Personaje( id ,nombre,descripcion,principal, secundario , extra, imagen)
                namesList.add(personaje)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return namesList
    }

}
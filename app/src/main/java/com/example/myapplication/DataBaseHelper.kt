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
            PRINCIPAL TEXT,
            SECUNDARIO TEXT,
            EXTRA TEXT,
            IMG TEXT
            ) """)
        p0?.execSQL(createTable)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
/*    fun insertName(name : String) : String{
        val db = this.writableDatabase
        *//*
        val contentValue = ContentValues().apply {
            put("NAME", name)
        }
        *//*
        val contentValues = ContentValues()
        contentValues.put("NAME", name)
        val result = db.insert("NAMES", null, contentValues  )
        return if (result == (-1).toLong() ) "Existe una falla" else "Inserción correcta"
    }*/

}
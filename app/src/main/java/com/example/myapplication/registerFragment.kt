package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import kotlin.time.Duration.Companion.milliseconds


class registerFragment : Fragment() {

    private lateinit var buttonSelect: ImageView
    private lateinit var buttonGuardar: Button
    private lateinit var imageView: ImageView
    private lateinit var name : EditText
    private lateinit var description : EditText
    private lateinit var principal: CheckBox
    private lateinit var secundario:CheckBox
    private lateinit var otros: CheckBox
    lateinit var dataBaseHelper: DataBaseHelper



    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200
    private val CAMERA_PERMISSION_CODE = 300
    private var base64Image = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        buttonSelect = view.findViewById(R.id.buttonSelectImage)
        imageView = view.findViewById(R.id.imageView)
        name = view.findViewById(R.id.editText1)
        description = view.findViewById(R.id.editText2)
        principal = view.findViewById(R.id.checkBox1)
        secundario = view.findViewById(R.id.checkBox2)
        otros = view.findViewById(R.id.checkBox3)
        buttonGuardar = view.findViewById(R.id.buttonGuardar)
        dataBaseHelper = DataBaseHelper(requireContext())



        buttonSelect.setOnClickListener {
            showImageDialog()
        }
        buttonGuardar.setOnClickListener {
            enviarData()
        }

        return view
    }

    private fun enviarData(){
        var nombre = name.text.toString()
        var descripcion = description.text.toString()
        var prin = if (principal.isChecked)1 else 0
        var secun = if (secundario.isChecked)1 else 0
        var otro = if (otros.isChecked) 1 else 0
        if(validarDatos(nombre,base64Image,prin,secun,otro)){
            val mensaje = dataBaseHelper.insertPersonaje(nombre,descripcion,prin,secun,otro,base64Image)
            mostrarMensajes(mensaje)
            name.text.clear()
            description.text.clear()
            principal.isChecked = false
            secundario.isChecked = false
            otros.isChecked = false
            base64Image = ""
            imageView.visibility= View.GONE
        }


    }
    private fun validarDatos(nombre :String, base :String , principal: Int, secundario : Int, extra :Int) : Boolean{
        if(nombre.isEmpty()){
            mostrarMensajes("El nombre no puede ser vacio")
            return false
        }

        if(base.isEmpty()){
            mostrarMensajes("Debe cargar una imagen o tomar una foto")
            return false
        }

        if(principal == 0 && secundario == 0 && extra == 0){
            mostrarMensajes("Debe seleccionar un tipo de personaje")
            return false
        }
        return true

    }

    private fun mostrarMensajes(msg : String){
        Toast.makeText(context , msg, Toast.LENGTH_SHORT).show()
    }

    private fun showImageDialog() {

        val options = arrayOf("Tomar foto", "Seleccionar de galería")

        AlertDialog.Builder(requireContext())
            .setTitle("Selecciona una opción")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkCameraPermission()
                    1 -> openGallery()
                }
            }
            .show()
    }


    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    private fun checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {

            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {

                    val photo = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(photo)
                    imageView.visibility = View.VISIBLE

                    base64Image = bitmapToBase64(photo)


                }
                GALLERY_REQUEST_CODE -> {

                    val imageUri = data?.data
                    imageView.setImageURI(imageUri)
                    imageView.visibility = View.VISIBLE

                    imageUri?.let {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                        base64Image = bitmapToBase64(bitmap)
                        Log.d("Base64", base64Image)
                    }
                }
            }
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
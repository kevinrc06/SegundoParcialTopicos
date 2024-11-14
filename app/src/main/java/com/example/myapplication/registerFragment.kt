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
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream


class registerFragment : Fragment() {

    private lateinit var buttonSelect: Button
    private lateinit var imageView: ImageView
    lateinit var dataBaseHelper: DataBaseHelper



    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 200
    private val CAMERA_PERMISSION_CODE = 300

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



        buttonSelect.setOnClickListener {
            showImageDialog()
        }

        return view
    }

    private fun showImageDialog() {

        val options = arrayOf("Tomar foto", "Seleccionar de galería")

        AlertDialog.Builder(requireContext())
            .setTitle("Selecciona una opción")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkCameraPermission()  // Opción para tomar foto
                    1 -> openGallery() // Opción para seleccionar de la galería
                }
            }
            .show()
    }

    // Abrir la cámara para capturar una imagen
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    // Abrir la galería para seleccionar una imagen
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    private fun checkCameraPermission() {
        // Verificar si el permiso de cámara ya ha sido otorgado
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            // Solicitar el permiso de cámara si no está otorgado
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
                openCamera() // Permiso otorgado, abre la cámara
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
                    // Obtiene la imagen de la cámara
                    val photo = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(photo) // Muestra la imagen en el ImageView
                    imageView.visibility = View.VISIBLE // Hace visible el ImageView

                    val base64Image = bitmapToBase64(photo)
                    Log.d("Base64", base64Image)


                }
                GALLERY_REQUEST_CODE -> {
                    // Obtiene la URI de la imagen seleccionada de la galería
                    val imageUri = data?.data
                    imageView.setImageURI(imageUri) // Muestra la imagen en el ImageView
                    imageView.visibility = View.VISIBLE // Hace visible el ImageView

                    imageUri?.let {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                        val base64Image = bitmapToBase64(bitmap)
                        Log.d("Base64", base64Image) // Imprime la cadena Base64 para pruebas
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

/*    fun setBase64ImageToImageView(base64String: String, imageView: ImageView) {
        try {
            // Decodificar la cadena Base64 en un arreglo de bytes
            val decodedString = Base64.decode(base64String, Base64.DEFAULT)

            // Convertir los bytes en un Bitmap
            val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            // Establecer el Bitmap en el ImageView
            imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            // Manejar el error en caso de que la cadena Base64 sea inválida
        }
    }*/
}
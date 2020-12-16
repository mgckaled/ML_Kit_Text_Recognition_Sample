package dev.mgck.ml_kit_text_recognition_sample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

class MainActivity : AppCompatActivity() {

    // Constants
    private val TAG = "ML_kit_text_recognition"
    private val bussines_card_id = R.drawable.business_card
    private val traffic_sign_id = R.drawable.traffic_sign

    // View Elements
    private lateinit var btn:Button
    private lateinit var tvContent:TextView
    private lateinit var pb:ProgressBar
    private lateinit var imageView:ImageView
    private lateinit var spinner:Spinner

    // BitMap
    private var selectedImage:Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Assing variables to .xml elements views
        btn = findViewById(R.id.btn_start)
        tvContent = findViewById(R.id.tv_content)
        pb = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)
        spinner = findViewById(R.id.spinner)

        // Button ClickListener
        btn.setOnClickListener {
            startTextRecognition()
            pb.visibility = View.VISIBLE
        }

        // Define image
        imageView.setImageResource(traffic_sign_id)
        selectedImage = BitmapFactory.decodeResource(resources, traffic_sign_id)

        // Spinner
        val content:Array<String> = arrayOf("Traffic Sign", "Business Card")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, content)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                tvContent.text = ""
                selectedImage = if (position == 0) {
                    imageView.setImageResource(traffic_sign_id)
                    BitmapFactory.decodeResource(resources, traffic_sign_id)
                } else {
                    imageView.setImageResource(bussines_card_id)
                    BitmapFactory.decodeResource(resources, bussines_card_id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun startTextRecognition()
    {
        val inputImage = InputImage.fromBitmap(selectedImage!!, 0)
        val recognizer = TextRecognition.getClient()
        recognizer.process(inputImage)
                .addOnSuccessListener {
                    pb.visibility = View.GONE
                    tvContent.text = it.text
                    Log.d(TAG, "Succesful Recognition!")
                }
                .addOnFailureListener {
                    Log.d(TAG, "Cannot be recognized!")
                    val msg = "Cannot be recognized!"
                    Toast.makeText(this, msg , Toast.LENGTH_SHORT).show()
                }
    }
}
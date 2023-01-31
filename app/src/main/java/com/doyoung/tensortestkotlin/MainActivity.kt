package com.doyoung.tensortestkotlin

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat
import com.doyoung.tensortestkotlin.databinding.ActivityMainBinding
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.doyoung.tensortestkotlin.ml.Skinint
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    // activity_main.xml 에서 확인할 수 있는 위젯들
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var tvOutput: TextView
    private var GALLERY_REQUEST_CODE = 123
    lateinit var mybitmap: Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 페이지 전환 시, 의도 intent를 넘겨주어야 함.
        // 각 페이지로의 intent
        val intentForAtopi = Intent(this, AtopiActivity::class.java)
        val intentForMolluscum = Intent(this, MolluscumActivity::class.java)
        val intentForEczema = Intent(this, EczemaActivity::class.java)
        val intentForCarcinoma = Intent(this, CarcinomaActivity::class.java)
        val intentForKeratoses = Intent(this, KeratosesActivity::class.java)
        val intentForNone = Intent(this, NoneActivity::class.java)

        imageView = binding.imageView
        button = binding.btnCaptureImage
        tvOutput = binding.tvOutput
        val buttonLoad = binding.btnLoadImage


        // 머신러닝 모델의 label 한 줄씩 읽기
        val fileName = "labels.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        var skinList = inputString.split("\n")


        // 카메라 버튼 눌렀을 때, 권한 확인 후 실행하기
        button.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                takePicturePreview.launch(null)
            } else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        // 갤러리 버튼 눌렀을 때, 권한 확인 후 실행하기
        buttonLoad.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)
            } else {
              requestPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }


        // 진단하기 버튼 눌렀을 때, 진행될 머신러닝 모델 불러오기 등의 순서
        binding.btnPredict.setOnClickListener {

            // 모델을 학습시켰을 때와 같은 크기로 맞추어 주기 위한 설정
            var resized: Bitmap = Bitmap.createScaledBitmap(mybitmap, 224, 224, true)
            val model = Skinint.newInstance(this)


            // 입력 모델
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

            var tbuffer = TensorImage.fromBitmap(resized)
            var bytebuffer = tbuffer.buffer
            inputFeature0.loadBuffer(bytebuffer)


            // 출력 모델
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            // getMax 함수를 이용해 확률이 가장 높은 결과값을 labels.txt로부터 읽어들임.
            var max = getMax(outputFeature0.floatArray)
            tvOutput.setText(skinList[max])

            // 진단 불가능이 나오는 경우, 새로운 페이지로의 전환이 아닌 토스트 메시지를 보냄.
            if(tvOutput.text == "진단 불가능"){
                Toast.makeText(this, "진단을 위해 다른 사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }

            model.close()
        }


        // 결과값 클릭 시, 결과에 맞는 페이지로의 이동
        tvOutput.setOnClickListener{

            val resultText = tvOutput.text
            when(resultText) {
                "아토피" -> {
                    startActivity(intentForAtopi)
                }
                "전염성 연속종" -> {
                    startActivity(intentForMolluscum)
                }
                "습진" -> {
                    startActivity(intentForEczema)
                }
                "기저 세포암" -> {
                    startActivity(intentForCarcinoma)
                }
                "지루 각화증" -> {
                    startActivity(intentForKeratoses)
                }
                "깨끗한 피부" -> {
                    startActivity(intentForNone)
                }
            }
        }


    }

    // 권한 확인하기
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if(granted) {
            takePicturePreview.launch(null)
        }
        else{
            Toast.makeText(this, "권한이 승인되지 않아 이용할 수 없습니다!", Toast.LENGTH_SHORT).show()
        }
    }

    // 카메라 버튼 클릭 시, 사진 촬영 후 화면에 보이기
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap->
       if(bitmap != null){
           imageView.setImageBitmap(bitmap)
           mybitmap = bitmap
           //outputGenerator(bitmap)
       }
    }

    // 갤러리 버튼 클릭 시, 권한 확인 후 화면에 보이기
    private val onresult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("TAG", "This is the result: ${result.data} ${result.resultCode}")
        onResultReceived(GALLERY_REQUEST_CODE, result)
    }

    private fun onResultReceived(requestCode: Int, result: ActivityResult?) {
        when(requestCode) {
            GALLERY_REQUEST_CODE -> {
                if(result?.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let{ uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap)
                        mybitmap = bitmap
                    }
                } else {
                    Log.i("TAG", "onActivityResult: error in selecting Image")
                }
            }
        }
    }

    // 결과값 도출을 위한 함수
    fun getMax(arr:FloatArray) : Int{
        var idx = 0
        var min = 0.0f

        for(i in 0.. 6){
            if(arr[i] > min) {
                idx = i
                min = arr[i]
            }
        }
        return idx
    }
}

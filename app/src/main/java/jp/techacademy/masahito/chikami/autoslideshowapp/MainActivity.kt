package jp.techacademy.masahito.chikami.autoslideshowapp

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                Log.d("ANDROID", "許可されている")
                getContentsInfo()

            } else {
                // 許可されていないので許可ダイアログを表示する
                Log.d("ANDROID", "許可されていない")
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ANDROID", "許可された")
                    getContentsInfo()

                }else{
                    Log.d("ANDROID", "許可されていない")
                    // 許可されていないので許可ダイアログを表示する
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_CODE)
                }
        }
    }



    var cursor: Cursor? = null

    fun getContentsInfo() {
        // 画像の情報を取得
        val resolver = contentResolver
        cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目（null = 全項目）
            null, // フィルタ条件（null = フィルタなし）
            null, // フィルタ用パラメータ
            null // ソート (nullソートなし）
        )

        if (cursor!!.moveToFirst()) {
            // indexからIDを取得し、そのIDから画像のURIを取得する
            var fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
            var id = cursor!!.getLong(fieldIndex)
            var imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            Log.d("ANDROID", "URI : " + imageUri.toString())

            imageView.setImageURI(imageUri)

            button1.setOnClickListener {
                if (cursor!!.moveToNext()) {
                    cursor!!.moveToNext()
                    fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    id = cursor!!.getLong(fieldIndex)
                    imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    imageView.setImageURI(imageUri)
                    Log.d("LOG", "test1")
                } else {
                    cursor!!.moveToFirst()
                    fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    id = cursor!!.getLong(fieldIndex)
                    imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    imageView.setImageURI(imageUri)
                    Log.d("LOG", "test2")
                }
            }

            button2.setOnClickListener {
                if (cursor!!.moveToPrevious()) {
                    cursor!!.moveToPrevious()
                    fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    id = cursor!!.getLong(fieldIndex)
                    imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    imageView.setImageURI(imageUri)
                    Log.d("LOG", "test3")
                } else {
                    cursor!!.moveToLast()
                    fieldIndex = cursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    id = cursor!!.getLong(fieldIndex)
                    imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    imageView.setImageURI(imageUri)
                    Log.d("LOG", "test4")
                }

           }

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("Android", "onDestroy")
        cursor!!.close()
    }
}

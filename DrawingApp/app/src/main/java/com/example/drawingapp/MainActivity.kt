@file:Suppress("DEPRECATION")

package com.example.drawingapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.Color.*
import android.media.MediaScannerConnection
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_seekbar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import kotlin.math.round

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_view.setSiteForBrush(10.toFloat())
        mImageButtonCurrentPaint = ll_paint_colors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.palette_selected)
        )

        //TEST

        ib_brush.setOnClickListener{
            showBrushSizeSeekBar()
        }

        //TEST


        ib_gallery.setOnClickListener{
            if(isReadStorageAllowed()){
                val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhotoIntent, GALLERY)
            }else{
                requestStoragePermission()
            }
        }

        ib_undo.setOnClickListener {
            drawing_view.onClickUndo()
        }
        ib_redo.setOnClickListener {
            drawing_view.onClickRedo()
        }

        ib_save.setOnClickListener {
            if(isReadStorageAllowed()){
                BitmapAsyncTask(getBitmapFromView(fl_drawing_view_container)).execute()
            }else{
                requestStoragePermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY){
                try{
                    if(data!!.data != null){
                        iv_background_image.visibility = View.VISIBLE
                        iv_background_image.setImageURI(data.data)
                    }else{
                        Toast.makeText(this, "Image type incorrect or corrupted", Toast.LENGTH_SHORT).show()
                    }
                }catch(e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showBrushSizeSeekBar(){
        val seekBrushDialog = Dialog(this)
        seekBrushDialog.setContentView(R.layout.dialog_brush_seekbar)

        //TEST WORK!
        val seekSize = seekBrushDialog.sb_brush_picker
        seekSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
                seekBrushDialog.tv_seekBar.text = "Size: ${seekSize.progress}"
            }

            override fun onStartTrackingTouch(p0: SeekBar) {
                //code
            }

            override fun onStopTrackingTouch(p0: SeekBar) {
                drawing_view.setSiteForBrush(seekSize.progress.toFloat())
                seekBrushDialog.dismiss()
            }
        })
        seekBrushDialog.show()
        //TEST WORK!!
    }


    @SuppressWarnings("MagicNumber")
    class ColorPicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {

        val colors = intArrayOf(RED, GREEN, BLUE)
        val strokeSize = 2 * context.resources.displayMetrics.density
        val rainbowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        val rainbowBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        val pickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        var pick = 0.5f
        var verticalGridSize = 0f
        var rainbowBaseline = 0f
        var showPreview = false

        override fun onDraw(canvas: Canvas) {
            drawPicker(canvas)
            drawColorAim(canvas, rainbowBaseline, verticalGridSize.toInt() / 2, verticalGridSize * 0.5f, color)
            if (showPreview) {
                drawColorAim(canvas, verticalGridSize, (verticalGridSize / 1.4f).toInt(), verticalGridSize * 0.7f, color)
            }
        }

        private fun drawPicker(canvas: Canvas) {
            val lineX = verticalGridSize / 2f
            val lineY = rainbowBaseline.toFloat()
            rainbowPaint.strokeWidth = verticalGridSize / 1.5f + strokeSize
            rainbowBackgroundPaint.strokeWidth = rainbowPaint.strokeWidth + strokeSize
            canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowBackgroundPaint)
            canvas.drawLine(lineX, lineY, width - lineX, lineY, rainbowPaint)
        }

        private fun drawColorAim(canvas: Canvas, baseLine: Float, offset: Int, size: Float, color: Int) {
            val circleCenterX = offset + pick * (canvas.width - offset * 2)
            canvas.drawCircle(circleCenterX, baseLine, size, pickPaint.apply { this.color = WHITE })
            canvas.drawCircle(circleCenterX, baseLine, size - strokeSize, pickPaint.apply { this.color = color })
        }

        @SuppressLint("DrawAllocation")
        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            val height = measuredHeight
            val width = measuredWidth
            val shader = LinearGradient(
                height / 4.0f,
                height / 2.0f,
                width - height / 4.0f,
                height / 2.0f,
                colors,
                null,
                Shader.TileMode.CLAMP
            )
            verticalGridSize = height / 3f
            rainbowPaint.shader = shader
            rainbowBaseline = verticalGridSize / 2f + verticalGridSize * 2
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val action = event.action
            if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN) {
                pick = event.x / measuredWidth.toFloat()
                if (pick < 0) {
                    pick = 0f
                } else if (pick > 1) {
                    pick = 1f
                }
                showPreview = true
            } else if (action == MotionEvent.ACTION_UP) {
                showPreview = false
            }
            postInvalidateOnAnimation()
            return true
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun interpColor(@FloatRange(from = 0.0, to = 1.0) unit: Float, colors: IntArray): Int {
            if (unit <= 0) return colors[0]
            if (unit >= 1) return colors[colors.size - 1]

            var p = unit * (colors.size - 1)
            val i = p.toInt()
            // take fractional part
            p -= i

            val c0 = colors[i]
            val c1 = colors[i + 1]
            // Calculates each channel separately
            val a = avg(alpha(c0), alpha(c1), p)
            val r = avg(red(c0), red(c1), p)
            val g = avg(green(c0), green(c1), p)
            val b = avg(blue(c0), blue(c1), p)

            return Color.argb(a, r, g, b)
        }

        fun avg(s: Int, e: Int, @FloatRange(from = 0.0, to = 1.0) p: Float) = s + round(p * (e - s))

        val color: Int
            @RequiresApi(Build.VERSION_CODES.O)
            get() = interpColor(pick, colors)
    }



    fun paintClicked(view: View){
        if(view !== mImageButtonCurrentPaint){
            val imageButton = view as ImageButton

            val colorTag = imageButton.tag.toString()
            drawing_view.setColor(colorTag)
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.palette_selected)
            )
            mImageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.palette_normal)
            )

            mImageButtonCurrentPaint = view
        }
    }

    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).toString())){
            Toast.makeText(this, "Need permission to add a background image", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted to read the storage", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isReadStorageAllowed(): Boolean{
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun getBitmapFromView(view: View): Bitmap{
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)

        return returnedBitmap
    }

    private inner class BitmapAsyncTask(val mBitmap: Bitmap): AsyncTask<Any, Void, String>(){

        private lateinit var mProgressDialog: Dialog

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }

        override fun doInBackground(vararg p0: Any?): String {
            var result = ""

            if(mBitmap != null){
                try{
                    val bytes = ByteArrayOutputStream()
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    val f = File(externalCacheDir!!.absoluteFile.toString() + File.separator
                            + "DrawingApp_" + System.currentTimeMillis() / 1000 + ".png")

                    val fos = FileOutputStream(f)
                    fos.write(bytes.toByteArray())
                    fos.close()

                    result = f.absolutePath

                }catch (e: Exception){
                    result = ""
                    e.printStackTrace()
                }
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            cancelProgressDialog()
            if(result!!.isNotEmpty()){
                Toast.makeText(this@MainActivity, "File saved successfully: $result", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@MainActivity, "Something went wrong while saving the file", Toast.LENGTH_SHORT).show()
            }

            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result), null){
                path, uri -> val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.type = "image/png"

                startActivity(Intent.createChooser(shareIntent,"share"))
            }
        }

        private fun showProgressDialog(){
            mProgressDialog = Dialog(this@MainActivity)
            mProgressDialog.setContentView(R.layout.dialog_custom_progress)
            mProgressDialog.show()
        }

        private fun cancelProgressDialog(){
            mProgressDialog.dismiss()
        }

    }

    companion object{
        private const val STORAGE_PERMISSION_CODE = 1
        private const val GALLERY = 2
    }
}
package com.example.dialogstest

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_dialog.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ib_snackbar.setOnClickListener { view ->
            Snackbar.make(view, "You have clicked on the image button", Snackbar.LENGTH_LONG).show()
        }

        btn_alert_dialog.setOnClickListener {
            alertDialogFunction()
        }

        btn_custom_dialog.setOnClickListener {
            customDialogFunction()
        }

        btn_progress_dialog.setOnClickListener { view ->
            customProgressDialogFunction()
        }
    }

    private fun alertDialogFunction(){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Alert!!")
        builder.setMessage("This is an alert dialog")
        builder.setIcon(R.drawable.ic_image_alert_2)
        builder.setPositiveButton("Yes") {dialogInterface, i ->
            Toast.makeText(applicationContext, "Click yes", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") {dialogInterface, i ->
            Toast.makeText(applicationContext, "Click no", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }
        builder.setNeutralButton("Cancel"){dialogInterface, i ->
            Toast.makeText(applicationContext, "Canceled", Toast.LENGTH_SHORT).show()
            dialogInterface.dismiss()
        }
        builder.setCancelable(false)
        val dialogBox: AlertDialog = builder.create()
        dialogBox.show()
    }

    private fun customDialogFunction(){
        val customizedDialog = Dialog(this)

        customizedDialog.setContentView(R.layout.custom_dialog)
        customizedDialog.tv_submit.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Clicked submit", Toast.LENGTH_SHORT).show()
            customizedDialog.dismiss()
        })
        customizedDialog.tv_cancel.setOnClickListener(View.OnClickListener {
            Toast.makeText(applicationContext, "Clicked cancel", Toast.LENGTH_SHORT).show()
            customizedDialog.dismiss()
        })
        customizedDialog.setCancelable(false)
        customizedDialog.show()
    }

    private fun customProgressDialogFunction(){
        val progress = Dialog(this)

        progress.setContentView(R.layout.progress_dialog)
        progress.show()
    }
}
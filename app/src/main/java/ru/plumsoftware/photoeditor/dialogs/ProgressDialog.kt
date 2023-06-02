package ru.plumsoftware.photoeditor.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import ru.plumsoftware.photoeditor.R

class ProgressDialog(private val mContext: Context) : Dialog(mContext) {
    private val dialog: Dialog = Dialog(mContext)

    @SuppressLint("InflateParams")
    fun showDialog(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialog, null, false))
        dialog.setCancelable(false)
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}
package com.evan.newtiny

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.File

class MainActivity : BaseActivity() {
    private var mInfoTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mInfoTv = findViewById<View>(R.id.tv_info) as TextView
        val metrics = Resources.getSystem().displayMetrics
        mInfoTv!!.text =
            """
            sdk version:${Build.VERSION.SDK_INT}
            density:${metrics.density},densityDpi:${metrics.densityDpi}
            width:${metrics.widthPixels},height:${metrics.heightPixels}
            """.trimIndent()
        val bitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_delete_forever_white_24dp)
        mInfoTv!!.append(",  width:" + bitmap.width + ",height:" + bitmap.height)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            AlertDialog.Builder(this)
                .setTitle("Tip")
                .setMessage("Do you want to clear \"tiny\" compressed directory?")
                .setNegativeButton("Cancel") { dialog, which -> }
                .setPositiveButton("Sure") { dialog, which -> //for test,so simple impl.
                    try {
                        val dir = File(getExternalFilesDir(null)!!.parent + File.separator + "tiny")
                        val files = dir.listFiles()
                        for (i in files.indices) {
                            files[i].delete()
                        }
                        Toast.makeText(
                            this@MainActivity.application,
                            "Clear success!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                    }
                }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btn_one -> startActivity(BitmapCompressTestActivity::class.java)
            R.id.btn_two -> startActivity(FileCompressTestActivity::class.java)
            R.id.btn_three -> startActivity(FileWithReturnBitmapCompressTestActivity::class.java)
            R.id.btn_four -> startActivity(BatchBitmapCompressTestActivity::class.java)
            R.id.btn_five -> startActivity(BatchFileCompressTestActivity::class.java)
            R.id.btn_six -> startActivity(BatchFileWithReturnBitmapCompressTestActivity::class.java)
            else -> {}
        }
    }
}

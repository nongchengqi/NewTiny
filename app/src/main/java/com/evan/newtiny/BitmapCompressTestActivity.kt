package com.evan.newtiny

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zxy.tiny.Tiny
import com.zxy.tiny.Tiny.BitmapCompressOptions
import com.zxy.tiny.callback.BitmapCallback
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream

class BitmapCompressTestActivity : BaseActivity() {
    private var mOriginImg: ImageView? = null
    private var mCompressImg: ImageView? = null
    private var mOriginTv: TextView? = null
    private var mCompressTv: TextView? = null
    private var mConfig = Bitmap.Config.ARGB_8888
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap_compress_test)
        mOriginImg = findViewById<View>(R.id.img_origin) as ImageView
        mCompressImg = findViewById<View>(R.id.img_compress) as ImageView
        mOriginTv = findViewById<View>(R.id.tv_origin) as TextView
        mCompressTv = findViewById<View>(R.id.tv_compress) as TextView
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_testcase, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        mOriginImg!!.setImageBitmap(null)
        mCompressImg!!.setImageBitmap(null)
        when (id) {
            R.id.action_config -> if (mConfig == Bitmap.Config.ARGB_8888) {
                item.setTitle("RGB_565")
                mConfig = Bitmap.Config.RGB_565
            } else if (mConfig == Bitmap.Config.RGB_565) {
                item.setTitle("ARGB_8888")
                mConfig = Bitmap.Config.ARGB_8888
            }

            R.id.action_bytes -> {
                //free memory for test
                gcAndFinalize()
                testBytes()
            }

            R.id.action_file -> {
                gcAndFinalize()
                testFile()
            }

            R.id.action_bitmap -> {
                gcAndFinalize()
                testBitmap()
            }

            R.id.action_stream -> {
                gcAndFinalize()
                testStream()
            }

            R.id.action_res -> {
                gcAndFinalize()
                testResource()
            }

            R.id.action_uri -> {
                gcAndFinalize()
                testUri()
            }

            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupOriginInfo(bitmap: Bitmap?) {
        mOriginImg!!.setImageBitmap(bitmap)
        mOriginTv!!.text = ("origin bitmap memory size:" + Formatter.formatFileSize(
            this,
            bitmap!!.byteCount.toLong()
        )
                + "\nwidth:" + bitmap.width + ",height:" + bitmap.height + ",config:" + bitmap.config)
    }

    private fun setupCompressInfo(bitmap: Bitmap) {
        mCompressImg!!.setImageBitmap(bitmap)
        mCompressTv!!.text =
            ("compress bitmap memory size:" + Formatter.formatFileSize(
                this@BitmapCompressTestActivity,
                bitmap.byteCount.toLong()
            )
                    + "\nwidth:" + bitmap.width + ",height:" + bitmap.height + ",config:" + bitmap.config)
    }

    private fun testBytes() {
        try {
            val `is` = resources.assets
                .open("test-1.jpg")
            val os = ByteArrayOutputStream()
            val buffer = ByteArray(4096)
            var len = -1
            while (`is`.read(buffer).also { len = it } != -1) {
                os.write(buffer, 0, len)
            }
            os.close()
            `is`.close()
            val bitmapBytes = os.toByteArray()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap =
                BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size, options)
            setupOriginInfo(originBitmap)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            Tiny.getInstance().source(bitmapBytes).asBitmap().withOptions(compressOptions).compress(
                BitmapCallback { isSuccess, bitmap, t ->
                    if (!isSuccess) {
                        Log.e("zxy", "error: " + t.message)
                        mCompressTv!!.text = "compress bitmap failed!"
                        return@BitmapCallback
                    }
                    setupCompressInfo(bitmap)
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testFile() {
        try {
            val `is` = resources.assets
                .open("test-2.jpg")
            val outfile = File(getExternalFilesDir(null), "test-2.jpg")
            val fos = FileOutputStream(outfile)
            val buffer = ByteArray(4096)
            var len = -1
            while (`is`.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            `is`.close()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap = BitmapFactory.decodeFile(outfile.absolutePath, options)
            setupOriginInfo(originBitmap)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            Tiny.getInstance().source(outfile).asBitmap().withOptions(compressOptions).compress(
                BitmapCallback { isSuccess, bitmap, t ->
                    if (!isSuccess) {
                        Log.e("zxy", "error: " + t.message)
                        mCompressTv!!.text = "compress bitmap failed!"
                        return@BitmapCallback
                    }
                    setupCompressInfo(bitmap)
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testBitmap() {
        try {
            val `is` = resources.assets
                .open("test-3.jpg")
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap = BitmapFactory.decodeStream(`is`, null, options)
            setupOriginInfo(originBitmap)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            Tiny.getInstance().source(originBitmap).asBitmap().withOptions(compressOptions)
                .compress(
                    BitmapCallback { isSuccess, bitmap, t ->
                        if (!isSuccess) {
                            Log.e("zxy", "error: " + t.message)
                            mCompressTv!!.text = "compress bitmap failed!"
                            return@BitmapCallback
                        }
                        setupCompressInfo(bitmap)
                    })
            `is`.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testStream() {
        try {
            val `is` = resources.assets
                .open("test-4.jpg")
            val outfile = File(getExternalFilesDir(null), "test-4.jpg")
            val fos = FileOutputStream(outfile)
            val buffer = ByteArray(4096)
            var len = -1
            while (`is`.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            val is2: InputStream = FileInputStream(outfile)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap = BitmapFactory.decodeStream(`is`, null, options)
            setupOriginInfo(originBitmap)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            Tiny.getInstance().source(is2).asBitmap().withOptions(compressOptions).compress(
                BitmapCallback { isSuccess, bitmap, t ->
                    if (!isSuccess) {
                        Log.e("zxy", "error: " + t.message)
                        mCompressTv!!.text = "compress bitmap failed!"
                        return@BitmapCallback
                    }
                    setupCompressInfo(bitmap)
                })
            `is`.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testResource() {
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap = BitmapFactory.decodeResource(resources, R.drawable.test, options)
            setupOriginInfo(originBitmap)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            Tiny.getInstance().source(R.drawable.test).asBitmap().withOptions(compressOptions)
                .compress(
                    BitmapCallback { isSuccess, bitmap, t ->
                        if (!isSuccess) {
                            Log.e("zxy", "error: " + t.message)
                            mCompressTv!!.text = "compress bitmap failed!"
                            return@BitmapCallback
                        }
                        setupCompressInfo(bitmap)
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testUri() {
        val url = "http://7xswxf.com2.z0.glb.qiniucdn.com//blog/deec2ac0373d08eb85a.jpg"
        try {
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            Tiny.getInstance().source(Uri.parse(url)).asBitmap().withOptions(compressOptions)
                .compress(
                    BitmapCallback { isSuccess, bitmap, t ->
                        if (!isSuccess) {
                            Log.e("zxy", "error: " + t.message)
                            mCompressTv!!.text = "compress bitmap failed!"
                            return@BitmapCallback
                        }
                        setupCompressInfo(bitmap)
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

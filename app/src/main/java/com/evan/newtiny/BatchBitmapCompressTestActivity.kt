package com.evan.newtiny

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zxy.tiny.Tiny
import com.zxy.tiny.Tiny.BitmapCompressOptions
import com.zxy.tiny.callback.BitmapBatchCallback
import java.io.File
import java.io.FileOutputStream

class BatchBitmapCompressTestActivity : BaseActivity() {
    private var mOriginImg1: ImageView? = null
    private var mOriginImg2: ImageView? = null
    private var mOriginImg3: ImageView? = null
    private var mOriginImg4: ImageView? = null
    private var mCompressImg1: ImageView? = null
    private var mCompressImg2: ImageView? = null
    private var mCompressImg3: ImageView? = null
    private var mCompressImg4: ImageView? = null
    private var mOriginTv: TextView? = null
    private var mCompressTv: TextView? = null
    private var mConfig = Bitmap.Config.ARGB_8888
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_bitmap_compress_test)
        mOriginImg1 = findViewById<View>(R.id.img_origin1) as ImageView
        mOriginImg2 = findViewById<View>(R.id.img_origin2) as ImageView
        mOriginImg3 = findViewById<View>(R.id.img_origin3) as ImageView
        mOriginImg4 = findViewById<View>(R.id.img_origin4) as ImageView
        mCompressImg1 = findViewById<View>(R.id.img_compress1) as ImageView
        mCompressImg2 = findViewById<View>(R.id.img_compress2) as ImageView
        mCompressImg3 = findViewById<View>(R.id.img_compress3) as ImageView
        mCompressImg4 = findViewById<View>(R.id.img_compress4) as ImageView
        mOriginTv = findViewById<View>(R.id.tv_origin) as TextView
        mCompressTv = findViewById<View>(R.id.tv_compress) as TextView
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_batch, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_config -> if (mConfig == Bitmap.Config.ARGB_8888) {
                item.setTitle("RGB_565")
                mConfig = Bitmap.Config.RGB_565
            } else if (mConfig == Bitmap.Config.RGB_565) {
                item.setTitle("ARGB_8888")
                mConfig = Bitmap.Config.ARGB_8888
            }

            R.id.action_file -> {
                free()
                gcAndFinalize()
                testFile()
            }

            R.id.action_bitmap -> {
                free()
                gcAndFinalize()
                testBitmap()
            }

            R.id.action_res -> {
                free()
                gcAndFinalize()
                testResource()
            }

            R.id.action_uri -> {
                free()
                gcAndFinalize()
                testUri()
            }

            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun free() {
        mOriginImg1!!.setImageBitmap(null)
        mOriginImg2!!.setImageBitmap(null)
        mOriginImg3!!.setImageBitmap(null)
        mOriginImg4!!.setImageBitmap(null)
        mCompressImg1!!.setImageBitmap(null)
        mCompressImg2!!.setImageBitmap(null)
        mCompressImg3!!.setImageBitmap(null)
        mCompressImg4!!.setImageBitmap(null)
    }

    private fun setupOriginInfo(
        bitmap1: Bitmap?,
        bitmap2: Bitmap?,
        bitmap3: Bitmap?,
        bitmap4: Bitmap?
    ) {
        mOriginImg1!!.setImageBitmap(bitmap1)
        mOriginImg2!!.setImageBitmap(bitmap2)
        mOriginImg3!!.setImageBitmap(bitmap3)
        mOriginImg4!!.setImageBitmap(bitmap4)
        mOriginTv!!.text =
            ("origin bitmap memory size:\nbitmap[1,2,3,4]:" + Formatter.formatFileSize(
                this,
                bitmap1!!.byteCount.toLong()
            )
                    + "," + Formatter.formatFileSize(
                this,
                bitmap2!!.byteCount.toLong()
            )
                    + "," + Formatter.formatFileSize(
                this,
                bitmap3!!.byteCount.toLong()
            )
                    + "," + Formatter.formatFileSize(
                this,
                bitmap4!!.byteCount.toLong()
            )
                    + "\nwidth[1,2,3,4]:" + bitmap1.width
                    + "," + bitmap2.width
                    + "," + bitmap3.width
                    + "," + bitmap4.width
                    + "\nheight[1,2,3,4]:" + bitmap1.height
                    + "," + bitmap2.height
                    + "," + bitmap3.height
                    + "," + bitmap4.height
                    + "\nconfig:" + mConfig)
    }

    private fun setupCompressInfo(
        bitmap1: Bitmap,
        bitmap2: Bitmap,
        bitmap3: Bitmap,
        bitmap4: Bitmap
    ) {
        mCompressImg1!!.setImageBitmap(bitmap1)
        mCompressImg2!!.setImageBitmap(bitmap2)
        mCompressImg3!!.setImageBitmap(bitmap3)
        mCompressImg4!!.setImageBitmap(bitmap4)
        mCompressTv!!.text =
            ("compress bitmap memory size:\nbitmap[1,2,3,4]:" + Formatter.formatFileSize(
                this,
                bitmap1.byteCount.toLong()
            )
                    + "," + Formatter.formatFileSize(
                this,
                bitmap2.byteCount.toLong()
            )
                    + "," + Formatter.formatFileSize(
                this,
                bitmap3.byteCount.toLong()
            )
                    + "," + Formatter.formatFileSize(
                this,
                bitmap4.byteCount.toLong()
            )
                    + "\nwidth[1,2,3,4]:" + bitmap1.width
                    + "," + bitmap2.width
                    + "," + bitmap3.width
                    + "," + bitmap4.width
                    + "\nheight[1,2,3,4]:" + bitmap1.height
                    + "," + bitmap2.height
                    + "," + bitmap3.height
                    + "," + bitmap4.height
                    + "\nconfig:" + mConfig)
    }

    private fun testFile() {
        try {
            val is1 = resources.assets
                .open("test_4.png")
            val is2 = resources.assets
                .open("test-3.jpg")
            val is3 = resources.assets
                .open("test_2.png")
            val is4 = resources.assets
                .open("test_1.png")
            val outfile1 = File(getExternalFilesDir(null), "batch-test-2.jpg")
            val outfile2 = File(getExternalFilesDir(null), "batch-test-3.jpg")
            val outfile3 = File(getExternalFilesDir(null), "batch-test-4.jpg")
            val outfile4 = File(getExternalFilesDir(null), "batch-test-5.jpg")
            var fos = FileOutputStream(outfile1)
            var buffer = ByteArray(4096)
            var len = -1
            while (is1.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos = FileOutputStream(outfile2)
            buffer = ByteArray(4096)
            len = -1
            while (is2.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos = FileOutputStream(outfile3)
            buffer = ByteArray(4096)
            len = -1
            while (is3.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos = FileOutputStream(outfile4)
            buffer = ByteArray(4096)
            len = -1
            while (is4.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            is1.close()
            is2.close()
            is3.close()
            is4.close()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap1 = BitmapFactory.decodeFile(outfile1.absolutePath, options)
            val originBitmap2 = BitmapFactory.decodeFile(outfile2.absolutePath, options)
            val originBitmap3 = BitmapFactory.decodeFile(outfile3.absolutePath, options)
            val originBitmap4 = BitmapFactory.decodeFile(outfile4.absolutePath, options)
            setupOriginInfo(originBitmap1, originBitmap2, originBitmap3, originBitmap4)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            val files = arrayOf(outfile1, outfile2, outfile3, outfile4)
            Tiny.getInstance().source(files).batchAsBitmap().withOptions(compressOptions)
                .batchCompress(
                    BitmapBatchCallback { isSuccess, bitmaps, t ->
                        if (!isSuccess) {
                            mCompressTv!!.text = "batch compress bitmap failed!"
                            return@BitmapBatchCallback
                        }
                        setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3])
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testBitmap() {
        try {
            val is1 = resources.assets
                .open("test-2.jpg")
            val is2 = resources.assets
                .open("test_1.png")
            val is3 = resources.assets
                .open("test_3.png")
            val is4 = resources.assets
                .open("test_4.png")
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap1 = BitmapFactory.decodeStream(is1, null, options)
            val originBitmap2 = BitmapFactory.decodeStream(is2, null, options)
            val originBitmap3 = BitmapFactory.decodeStream(is3, null, options)
            val originBitmap4 = BitmapFactory.decodeStream(is4, null, options)
            setupOriginInfo(originBitmap1, originBitmap2, originBitmap3, originBitmap4)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            val bitmaps = arrayOf(originBitmap1, originBitmap2, originBitmap3, originBitmap4)
            Tiny.getInstance().source(bitmaps).batchAsBitmap().withOptions(compressOptions)
                .batchCompress(
                    BitmapBatchCallback { isSuccess, bitmaps, t ->
                        if (!isSuccess) {
                            mCompressTv!!.text = "batch compress bitmap failed!"
                            return@BitmapBatchCallback
                        }
                        setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3])
                    })
            is1.close()
            is2.close()
            is3.close()
            is4.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testResource() {
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = mConfig
            val originBitmap1 = BitmapFactory.decodeResource(resources, R.drawable.test_1, options)
            val originBitmap2 = BitmapFactory.decodeResource(resources, R.drawable.test_2, options)
            val originBitmap3 = BitmapFactory.decodeResource(resources, R.drawable.test_3, options)
            val originBitmap4 = BitmapFactory.decodeResource(resources, R.drawable.test, options)
            setupOriginInfo(originBitmap1, originBitmap2, originBitmap3, originBitmap4)
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            val resIds =
                intArrayOf(R.drawable.test_1, R.drawable.test_2, R.drawable.test_3, R.drawable.test)
            Tiny.getInstance().source(resIds).batchAsBitmap().withOptions(compressOptions)
                .batchCompress(
                    BitmapBatchCallback { isSuccess, bitmaps, t ->
                        if (!isSuccess) {
                            mCompressTv!!.text = "batch compress bitmap failed!"
                            return@BitmapBatchCallback
                        }
                        setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3])
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testUri() {
        val url1 = "http://7xswxf.com2.z0.glb.qiniucdn.com//blog/deec2ac0373d08eb85a.jpg"
        val url2 = "http://7xswxf.com2.z0.glb.qiniucdn.com/IMG_1439.JPG"
        val url3 = "http://7xswxf.com2.z0.glb.qiniucdn.com/IMG_1698.JPG"
        val url4 = "http://7xswxf.com2.z0.glb.qiniucdn.com/IMG_1694.JPG"
        try {
            val `is` = resources.assets
                .open("enjoy.JPG")
            val enjoyBitmap = BitmapFactory.decodeStream(`is`)
            mOriginImg1!!.setImageBitmap(enjoyBitmap)
            mOriginImg2!!.setImageBitmap(enjoyBitmap)
            mOriginImg3!!.setImageBitmap(enjoyBitmap)
            mOriginImg4!!.setImageBitmap(enjoyBitmap)
            mOriginTv!!.text = "省略一万字~"
            val compressOptions = BitmapCompressOptions()
            compressOptions.config = mConfig
            val uris = arrayOf(Uri.parse(url1), Uri.parse(url2), Uri.parse(url3), Uri.parse(url4))
            Tiny.getInstance().source(uris).batchAsBitmap().withOptions(compressOptions)
                .batchCompress(
                    BitmapBatchCallback { isSuccess, bitmaps, t ->
                        if (!isSuccess) {
                            mCompressTv!!.text = "batch compress bitmap failed!"
                            return@BitmapBatchCallback
                        }
                        setupCompressInfo(bitmaps[0], bitmaps[1], bitmaps[2], bitmaps[3])
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

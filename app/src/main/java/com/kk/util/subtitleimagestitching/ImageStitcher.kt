import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore

object ImageStitcher {
    fun stitchImages(context: Context, imageUris: List<Uri>): Bitmap {
        if (imageUris.isEmpty()) return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        
        // 获取第一张图片的完整高度
        val firstBitmap = loadBitmapFromUri(context, imageUris[0])
        var totalHeight = firstBitmap.height
        val width = firstBitmap.width
        
        // 计算后续图片字幕部分的总高度
        val subtitleHeight = (firstBitmap.height * 0.2).toInt() // 假设字幕占图片20%
        totalHeight += (imageUris.size - 1) * subtitleHeight
        
        // 创建最终的位图
        val result = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        
        // 绘制第一张完整图片
        canvas.drawBitmap(firstBitmap, 0f, 0f, null)
        
        // 绘制后续图片的字幕部分
        var currentHeight = firstBitmap.height
        for (i in 1 until imageUris.size) {
            val bitmap = loadBitmapFromUri(context, imageUris[i])
            val srcRect = Rect(0, bitmap.height - subtitleHeight, bitmap.width, bitmap.height)
            val dstRect = Rect(0, currentHeight, width, currentHeight + subtitleHeight)
            canvas.drawBitmap(bitmap, srcRect, dstRect, null)
            currentHeight += subtitleHeight
            bitmap.recycle()
        }
        
        firstBitmap.recycle()
        return result
    }
    
    private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    }
} 
package com.anlaiye.swt.viewmodel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

import java.security.InvalidParameterException;
import java.util.Random;

/**
 * 介绍：这里写介绍
 * 作者：sweet
 * 邮箱：sunwentao@imcoming.cn
 * 时间: 2017/3/28
 */
public class MosaicUtil {
    private static final String TAG = "MosaicUtil";

    public static class Size {
        public int width;
        public int height;

        public Size(int w, int h) {
            this.width = w;
            this.height = h;
        }
    }

    private static Size defaltSize;

    /**
     * 读取图片，返回对应Bitmap对象
     *
     * @param absPath 本地图片路径
     * @return
     */
    public static Bitmap getImage(String absPath) {
        Bitmap bitmap = BitmapFactory.decodeFile(absPath);
        return bitmap;
    }

    /**
     * 获取本地图片尺寸
     *
     * @param absPath 本地图片路径
     * @return
     */
    public static Size getImageSize(String absPath) {
        // 实现方式，很优秀
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(absPath, options);
        Size size = new Size(options.outWidth, options.outHeight);
        return size;
    }

    /**
     * 图片模糊效果处理
     *
     * @param bitmap 原图片
     * @return 处理后的图片
     */
    public static Bitmap blur(Bitmap bitmap) {
        int iterations = 1;
        int radius = 8;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        Bitmap blured = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < iterations; i++) {
            blur(inPixels, outPixels, width, height, radius);
            blur(outPixels, inPixels, height, width, radius);
        }
        blured.setPixels(inPixels, 0, width, 0, 0, width, height);
        return blured;
    }

    private static void blur(int[] in, int[] out, int width, int height,
                             int radius) {
        int widthMinus1 = width - 1;
        int tableSize = 2 * radius + 1;
        int divide[] = new int[256 * tableSize];

        for (int index = 0; index < 256 * tableSize; index++) {
            divide[index] = index / tableSize;
        }

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -radius; i <= radius; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
                        | (divide[tg] << 8) | divide[tb];

                int i1 = x + radius + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - radius;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    private static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }


    public static int dp2px(Context context, int dip) {
        Resources resources = context.getResources();
        int px = Math
                .round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dip, resources.getDisplayMetrics()));
        return px;
    }



    /**
     * 用户设置的grid width是显示宽度，然而我们马赛克化图片时，操作的是图片本身。
     * 图片可能很大或很小，放到ImageView中可能缩放过。
     * 因此，grid width需要根据图片大小和ImageView大小进行适当调整。
     *
     * @param src
     * @param imageViewSize
     * @param gridWidth
     * @return
     */
    private static int getSuitableGridWidth(Context context, Bitmap src, Size imageViewSize, int gridWidth) {
        float widthRatio = src.getWidth() / ((float) imageViewSize.width);
        float heightRatio = src.getHeight() / ((float) imageViewSize.height);
        float ratio = widthRatio < heightRatio ? widthRatio : heightRatio;
        return (int) (dp2px(context, gridWidth) * ratio);
    }

    /**
     * 图片‘方格马赛克’效果处理
     *
     * @param context
     * @param src
     * @param viewSize
     * @param gridWidth
     * @return
     */
    public static Bitmap getGridMosaicBitmapOfNomalImage(Context context, Bitmap src, Size viewSize, int gridWidth) {
        if (src == null)
            return null;

        int imageWidth = src.getWidth();
        int imageHeight = src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int mGridWidth = getSuitableGridWidth(context, src, viewSize, gridWidth);
        int horCount = (int) Math.ceil(src.getWidth() / (float) mGridWidth);
        int verCount = (int) Math.ceil(src.getHeight() / (float) mGridWidth);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 将原始图片分成一个个小方块
        // 取小方块（left, top）位置原始图片像素填充小方块
        for (int horIndex = 0; horIndex < horCount; ++horIndex) {
            for (int verIndex = 0; verIndex < verCount; ++verIndex) {
                int l = mGridWidth * horIndex;
                int t = mGridWidth * verIndex;
                int r = l + mGridWidth;
                if (r > imageWidth) {
                    r = imageWidth;
                }
                int b = t + mGridWidth;
                if (b > imageHeight) {
                    b = imageHeight;
                }
                int color = src.getPixel(l, t);
                Rect rect = new Rect(l, t, r, b);
                paint.setColor(color);
                canvas.drawRect(rect, paint);
            }
        }
        canvas.save();
        return bitmap;
    }

    public static Bitmap getGridMosaicBitmapOfNomalImage(Context context, Bitmap src){
        if(defaltSize==null){
            defaltSize = new Size(150,150);
        }
        return getGridMosaicBitmapOfNomalImage(context,src,defaltSize,10);
    }

    /**
     * 获取只包含纯文本的图片的马赛克效果图
     */
    public static Bitmap getGridMosaicBitmapOfTextImage(Context context, Bitmap src, int gridWidth, int gridBaseColor) {
        if (src == null)
            return null;

        int imageWidth = src.getWidth();
        int imageHeight = src.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(imageWidth, imageHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        gridWidth = dp2px(context, gridWidth);

        int horCount = (int) Math.ceil(src.getWidth() / (float) gridWidth);
        int verCount = (int) Math.ceil(src.getHeight() / (float) gridWidth);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 将原始图片分成一个个小方块
        for (int horIndex = 0; horIndex < horCount; ++horIndex) {
            for (int verIndex = 0; verIndex < verCount; ++verIndex) {
                int l = gridWidth * horIndex;
                int t = gridWidth * verIndex;
                int r = l + gridWidth;
                if (r > imageWidth) {
                    r = imageWidth;
                }
                int b = t + gridWidth;
                if (b > imageHeight) {
                    b = imageHeight;
                }
                Rect rect = new Rect(l, t, r, b);
                int color = getGridColor(src, rect, gridBaseColor);
                paint.setColor(color);
                canvas.drawRect(rect, paint);
            }
        }
        canvas.save();
        return bitmap;
    }

    private static int getGridColor(Bitmap src, Rect rect, int gridBaseColor) {
//        Log.e(TAG, "计算小方块（" + rect + "）颜色值");
        int fontPixelNum = 0; // 小方格中，字体像素数量
        int totalPixelNum = 0;
        for (int x = rect.left; x < rect.right; ++x) {
            for (int y = rect.top; y < rect.bottom; ++y) {
                totalPixelNum++;
                int pixel = src.getPixel(x, y);
                if (pixel != 0x00000000) {
                    fontPixelNum++;
//                    Log.e(TAG, String.format("(" + x + "," + y + ")=%X", pixel));
                }
            }
        }

        // 字体像素越多，透明度越低
        int transparency = (int) (0xFF * ((float) fontPixelNum / (float) totalPixelNum));

        // 如果透明度太过极端，稍加处理
        if (transparency < 10) {
            transparency += getRandomInt(30, 100);
        } else if (transparency < 50) {
            transparency += getRandomInt(20, 50);
        } else if (transparency > 185) {
            transparency *=0.8;
        }

        // 小方块底色，加入透明度
        int color = gridBaseColor | (transparency << 24);

//        Log.e(TAG, "小方块（" + rect + "）颜色值=" + String.format("%X", color));
        return color;
    }

    private static Random random;
    public static int getRandomInt(int left, int right) {
        if (left > right) {
            throw new InvalidParameterException("left should less than right");
        }

        if (left == right)
            return left;

        if (random == null) {
            random = new Random();
        }
        return left + random.nextInt(right - left);
    }
}

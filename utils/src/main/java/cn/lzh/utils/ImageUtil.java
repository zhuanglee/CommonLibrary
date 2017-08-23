package cn.lzh.utils;

/**
 * Created by lzh on 2017/8/17.
 */

public class ImageUtil {


    /**
     * 旋转数据
     *
     * @param dst
     *            目标数据
     * @param src
     *            源数据
     * @param srcWidth
     *            源数据宽
     * @param srcHeight
     *            源数据高
     */
    private void YV12RotateNegative90(byte[] dst, byte[] src, int srcWidth,
                                      int srcHeight) {
        int t = 0;
        int i, j;

        int wh = srcWidth * srcHeight;

        for (i = srcWidth - 1; i >= 0; i--) {
            for (j = srcHeight - 1; j >= 0; j--) {
                dst[t++] = src[j * srcWidth + i];
            }
        }

        for (i = srcWidth / 2 - 1; i >= 0; i--) {
            for (j = srcHeight / 2 - 1; j >= 0; j--) {
                dst[t++] = src[wh + j * srcWidth / 2 + i];
            }
        }

        for (i = srcWidth / 2 - 1; i >= 0; i--) {
            for (j = srcHeight / 2 - 1; j >= 0; j--) {
                dst[t++] = src[wh * 5 / 4 + j * srcWidth / 2 + i];
            }
        }

    }





    private byte[] YUV42RotateDegree90(byte[] data, int imageWidth, int imageHeight)
    {
        byte [] yuv = new byte[imageWidth*imageHeight*3/2];

        // Y
        int i = 0;
        for(int x = 0;x < imageWidth;x++)
        {
            for(int y = imageHeight-1;y >= 0;y--)
            {
                yuv[i] = data[y*imageWidth+x];
                i++;
            }
        }

        // U and V
        i = imageWidth*imageHeight*3/2-1;
        int pos = imageWidth*imageHeight;
        for(int x = imageWidth-1;x > 0;x=x-2)
        {

            for(int y = 0;y < imageHeight/2;y++)
            {
                yuv[i] = data[pos+(y*imageWidth)+x];
                i--;
                yuv[i] = data[pos+(y*imageWidth)+(x-1)];
                i--;
            }
        }
        return yuv;
    }
}

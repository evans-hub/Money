package com.currency.money.env;

public class ImageUtils {
  @SuppressWarnings("unused")
  private static final Logger LOGGER = new Logger();

  static final int kMaxChannelValue = 262143;

  public static void convertYUV420ToARGB8888(
      byte[] yData,
      byte[] uData,
      byte[] vData,
      int width,
      int height,
      int yRowStride,
      int uvRowStride,
      int uvPixelStride,
      int[] out) {

    int i = 0;
    for (int y = 0; y < height; y++) {
      int pY = yRowStride * y;
      int uv_row_start = uvRowStride * (y >> 1);
      int pUV = uv_row_start;

      for (int x = 0; x < width; x++) {
        int uv_offset = pUV + (x >> 1) * uvPixelStride;
        out[i++] =
            YUV2RGB(
                convertByteToInt(yData, pY + x),
                convertByteToInt(uData, uv_offset),
                convertByteToInt(vData, uv_offset));
      }
    }
  }

  public static void convertYUV420SPToARGB8888(
          byte[] input, int[] output, int width, int height)
  {
    int pY = 0;
    for (int i=0; i<height; i++) {
      int pUV = width * height + (i / 2) * width;
      int u = 0;
      int v = 0;
      for (int j=0; j<width; j++) {
        int y = convertByteToInt(input, pY);
        if (j % 2 == 0)
        {
          v = convertByteToInt(input, pUV++);
          u = convertByteToInt(input, pUV++);
        }
        output[pY++] = YUV2RGB(y, u, v);
      }
    }
  }

  private static int convertByteToInt(byte[] arr, int pos) {
    return arr[pos] & 0xFF;
  }

  private static int YUV2RGB(int nY, int nU, int nV) {
    nY -= 16;
    nU -= 128;
    nV -= 128;
    if (nY < 0) nY = 0;


    final int foo = 1192 * nY;
    int nR = foo + 1634 * nV;
    int nG = foo - 833 * nV - 400 * nU;
    int nB = foo + 2066 * nU;

    nR = Math.min(kMaxChannelValue, Math.max(0, nR));
    nG = Math.min(kMaxChannelValue, Math.max(0, nG));
    nB = Math.min(kMaxChannelValue, Math.max(0, nB));

    return 0xff000000 | ((nR << 6) & 0x00ff0000) | ((nG >> 2) & 0x0000FF00) | ((nB >> 10) & 0xff);
  }

}

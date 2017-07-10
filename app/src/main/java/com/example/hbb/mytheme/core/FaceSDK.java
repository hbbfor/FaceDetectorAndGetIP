package com.example.hbb.mytheme.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;

/**
 * Created by hbb on 2017/7/5.
 */

public class FaceSDK {
    private static final String TAG = "FaceIdentify";
    public Bitmap DetectionBitmap(Bitmap bitmap) {
        try {
            Log.d(TAG, "开始检测");
            //检测前必须转化为RGB_565格式。文末有详述连接
            bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
            // 设定最大可查的人脸数量
            int MAX_FACES = 5;
            FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
            // 将人脸数据存储到facelist中
            FaceDetector.Face[] faceList = new FaceDetector.Face[MAX_FACES];
            faceDet.findFaces(bitmap, faceList);
            // FaceDetector API文档我们发现，它查找人脸的原理是：找眼睛。
            // 它返回的人脸数据face，
            // 通过调用public float eyesDistance ()，
            // public void getMidPoint(PointF point)，
            // 我们可以得到探测到的两眼间距，以及两眼中心点位置（MidPoint）。
            // public float confidence () 可以返回该人脸数据的可信度(0~1)，
            // 这个值越大，该人脸数据的准确度也就越高。
            RectF[] faceRects = new RectF[faceList.length];
            int num = 0;
            for (int i = 0; i < faceList.length; i++) {
                if(faceList[i] != null)
                    num++;
            }
            Log.d("MainActivityLog","faceNum="+num);
            for (int i = 0; i < faceList.length; i++) {
                FaceDetector.Face face = faceList[i];
                if (face != null) {
                    Log.d(TAG, "标志位置");
                    PointF pf = new PointF();
                    face.getMidPoint(pf);
                    // 这里的框，参数分别是：左上角的X,Y 右下角的X,Y
                    // 也就是左上角（r.left,r.top），右下角( r.right,r.bottom)。
                    // 作为定位，确定这个框的格局。
                    RectF r = new RectF();
//                    r.left = pf.x - face.eyesDistance() / 2;
//                    r.right = pf.x + face.eyesDistance() / 2;
//                    r.top = pf.y - face.eyesDistance() / 2;
//                    r.bottom = pf.y + face.eyesDistance() / 2;
                    r.left = pf.x - face.eyesDistance();
                    r.right = pf.x + face.eyesDistance();
                    r.top = pf.y - face.eyesDistance();
                    r.bottom = pf.y + (int)(face.eyesDistance() * 1.7);
                    Log.d(TAG, r.toString());
                    faceRects[i] = r;
                    // 画框:对原图进行处理，并在图上显示人脸框。
                    Canvas canvas = new Canvas(bitmap);
                    Paint p = new Paint();
                    p.setAntiAlias(true);
                    p.setStrokeWidth(4);
                    p.setStyle(Paint.Style.STROKE);
                    p.setColor(Color.RED);
                    // 画一个圈圈
//                    canvas.drawCircle(r.left, pf.y, 10, p);
//                    canvas.drawCircle(r.right, pf.y, 10, p);
                    // 画框
                    canvas.drawRect(r, p);
                }
            }
        }catch (Exception ex)
        {
        }
        return bitmap;
    }
}

package com.ace.musicplayer.lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ace.musicplayer.util.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/2 0002.
 */
public class LrcView extends View {
    private static TreeMap<Integer, LrcObject> lrcMap;

    private Paint paint = new Paint();
    private Paint paintHL = new Paint();

    private float mX;
    private float offsetY;

    private int lrcIndex = 0;




    private static boolean isLrc = false;

    public LrcView(Context context) {
        super(context);
        init();
    }

    public LrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        lrcMap = new TreeMap<Integer, LrcObject>();

        offsetY = 320;
        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setDither(true);

        paintHL.setColor(Color.GREEN);
        paintHL.setAntiAlias(true);
        paintHL.setTextAlign(Paint.Align.CENTER);
        paintHL.setDither(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isLrc) {
            paint.setTextSize(Constant.sizeWord);
            paintHL.setTextSize(Constant.sizeWord);
            LrcObject temp = lrcMap.get(lrcIndex);
            if(temp != null) {
                canvas.drawText(temp.lrc, mX, offsetY + (Constant.sizeWord + Constant.interval) * lrcIndex, paintHL);
                //画当前歌词之前的歌词
                for(int i=lrcIndex - 1; i>=0; i--) {
                    temp = lrcMap.get(i);
                    if(offsetY + (Constant.sizeWord + Constant.interval) * i < 0) {
                        break;
                    }

                    canvas.drawText(temp.lrc, mX, offsetY + (Constant.sizeWord + Constant.interval) * i, paint);

                }

                //画当前歌词之后的歌词
                for(int i=lrcIndex+1; i<lrcMap.size(); i++) {
                    temp = lrcMap.get(i);
                    if(offsetY + (Constant.sizeWord + Constant.interval) * i > 600) {
                        break;
                    }

                    canvas.drawText(temp.lrc, mX, offsetY + (Constant.sizeWord + Constant.interval) * i, paint);
                }
            }
        } else {
            paint.setTextSize(25);
            canvas.drawText("找不到歌词", mX, 310, paint);
        }

        super.onDraw(canvas);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mX = w * 0.5f;
        super.onSizeChanged(w, h, oldw, oldh);

    }

    public static void read(String file) {
        TreeMap<Integer, LrcObject> lrc_read = new TreeMap<Integer, LrcObject>();
        String data = "";
        try {
            File saveFile = new File(file);
            if(!saveFile.isFile()){
                isLrc = false;
                return;
            }
            isLrc = true;
            FileInputStream stream = new FileInputStream(saveFile);//  context.openFileInput(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream,"GB2312"));
            Pattern pattern = Pattern.compile("\\d{2}");
            while ((data = br.readLine()) != null) {
                data = data.replace("[","");//将前面的替换成后面的
                data = data.replace("]","@");
                String splitdata[] = data.split("@");//分隔
                if(data.endsWith("@")){
                    for(int k = 0; k < splitdata.length; k++){
                        String str = splitdata[k];
                        str = str.replace(":",".");
                        str = str.replace(".","@");
                        String timedata[] = str.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if(timedata.length == 3 && matcher.matches()){
                            int m = Integer.parseInt(timedata[0]);  //分
                            int s = Integer.parseInt(timedata[1]);  //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m * 60 + s) * 1000 + ms * 10;
                            LrcObject item1 = new LrcObject();
                            item1.beginTime = currTime;
                            item1.lrc = "";
                            lrc_read.put(currTime,item1);
                        }
                    }
                }else{
                    String lrcContenet = splitdata[splitdata.length-1];
                    for (int j = 0; j < splitdata.length - 1; j++){
                        String tmpstr = splitdata[j];
                        tmpstr = tmpstr.replace(":",".");
                        tmpstr = tmpstr.replace(".","@");
                        String timedata[] = tmpstr.split("@");
                        Matcher matcher = pattern.matcher(timedata[0]);
                        if(timedata.length == 3 && matcher.matches()){
                            int m = Integer.parseInt(timedata[0]);  //分
                            int s = Integer.parseInt(timedata[1]);  //秒
                            int ms = Integer.parseInt(timedata[2]); //毫秒
                            int currTime = (m * 60 + s) * 1000 + ms * 10;
                            LrcObject item1 = new LrcObject();
                            item1.beginTime = currTime;
                            item1.lrc = lrcContenet;
                            lrc_read.put(currTime,item1);// 将currTime当标签  item1当数据 插入TreeMap里
                        }
                    }
                }
            }
            stream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

		/*
		 * 遍历hashmap 计算每句歌词所需要的时间
		 */
        lrcMap.clear();
        data = "";
        Iterator<Integer> iterator = lrc_read.keySet().iterator();
        LrcObject oldval = null;
        int i = 0;
        while(iterator.hasNext()) {
            Object ob = iterator.next();
            LrcObject val = lrc_read.get(ob);
            if (oldval == null){
                oldval = val;
            }else{
                LrcObject item1 = new LrcObject();
                item1 = oldval;
                item1.timeLine = val.beginTime-oldval.beginTime;
                lrcMap.put(new Integer(i), item1);
                i++;
                oldval = val;
            }
            if (!iterator.hasNext()) {
                lrcMap.put(new Integer(i), val);
            }
        }
    }

    public void setTextSize() {
        if(!isLrc) {
            return;
        }

        int max = Constant.sizeWord;
    }

    /**
     * 按当前歌曲的播放时间，从歌词中获取到那一句
     * @param time
     * @return
     */
    public int selectIndex(int time) {
        if(!isLrc) {
            return 0;
        }

        int index = 0;
        for(int i=0; i<lrcMap.size(); i++) {
            LrcObject temp = lrcMap.get(i);
            if(temp.beginTime < time) {
                ++index;
            }
        }

        lrcIndex = index - 1;
        if(lrcIndex < 0) {
            lrcIndex = 0;
        }

        return lrcIndex;
    }

    public int getSizeWord() {
        return Constant.sizeWord;
    }

    /**
     * 歌词滚动速度
     * @return
     */
    public Float speedLrc(){
        float speed = 0;
        if(offsetY + (Constant.sizeWord + Constant.interval) * lrcIndex > 220){
            speed = ((offsetY + (Constant.sizeWord + Constant.interval) * lrcIndex - 220) / 20);

        } else if(offsetY + (Constant.sizeWord + Constant.interval) * lrcIndex < 120){
            speed = 0;
        }
        return speed;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public static boolean isLrc() {
        return isLrc;
    }

    public static void setIsLrc(boolean isLrc) {
        LrcView.isLrc = isLrc;
    }

    public float getmX() {
        return mX;
    }

    public void setmX(float mX) {
        this.mX = mX;
    }
}

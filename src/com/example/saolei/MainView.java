package com.example.saolei;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View {
    private   Mine mine;
    private  boolean isFirst=true;//����Ƿ��Ǳ��ֵ�һ�ε����Ļ
    private  Context context;
    private final int mineNum=10;//�������׵ĸ���
    private  final int ROW=13;//Ҫ���ɵľ����
    private  final int COL=8;//Ҫ���ɵľ����
    private   int TILE_WIDTH=50;//���С
    private  boolean isFalse=false;
    public  MainView(Context context)
    {
        super(context);
        this.context=context;
 
        TILE_WIDTH=MainActivity.W/10;
        mine=new Mine((MainActivity.W-COL*TILE_WIDTH)/2,(MainActivity.H-ROW*TILE_WIDTH)/2,COL,ROW,mineNum,TILE_WIDTH);
        try {
            mine.init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
    /**
     * ��Ϸ�߼�
     */
    public void logic()
    {
        int count=0;
 
        for (int i=0;i<mine.mapRow;i++)
        {
            for (int j=0;j<mine.mapCol;j++)
            {
                if(!mine.tile[i][j].open)
                {
                    count++;
                }
            }
        }
        //�߼��ж��Ƿ�ʤ��
        if(count==mineNum)
        {
            new AlertDialog.Builder(context)
                    .setMessage("��ϲ�㣬���ҳ���������")
                    .setCancelable(false)
                    .setPositiveButton("����", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
 
                            mine.init();
                            invalidate();
                            isFirst=true;
                        }
                    })
                    .setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .create()
                    .show();
        }
    }
 
 
    /**
     * ˢ��View
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        mine.draw(canvas);
    }
 
    /**
     * �����Ļ�¼�
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            int x=(int)event.getX();
            int y=(int)event.getY();
            //�ж��Ƿ���ڷ�Χ��
            if(x>=mine.x&&y>=mine.y&&x<=(mine.mapWidth+mine.x)&&y<=(mine.y+mine.mapHeight))
            {
                int idxX=(x-mine.x)/mine.tileWidth;
                int idxY=(y-mine.y)/mine.tileWidth;
                mine.open(new Mine.Point(idxX,idxY),isFirst);
                isFirst=false;
 
                if(mine.tile[idxY][idxX].value==-1)
                {
                    mine.isDrawAllMine=true;
                    new AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setMessage("���ź�����ȵ����ˣ�")
                            .setPositiveButton("����", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mine.init();
                                    isFalse=true;
                                    isFirst=true;
 
                                    invalidate();
                                }
                            })
                            .setNegativeButton("�˳�", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            })
                            .create()
                            .show();
                }
                if(isFalse)
                {
                    isFalse=false;
                    invalidate();
                    return true;
                }
                logic();
 
                invalidate();
            }
 
        }
        return true;
    }
}

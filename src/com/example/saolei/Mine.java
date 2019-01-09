package com.example.saolei;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class Mine {
    public   int x;//��ͼ������Ļ�ϵ������
    public   int y;//��ͼ������Ļ�ϵ������
    public    int mapCol;//�����
    public   int mapRow;//�����
    private  int mineNum ;
    public static short EMPTY=0;//��
    public static short MINE=-1;//��
    public Tile[][] tile;//��ͼ����
    public   int tileWidth;//���
    private  Paint textPaint;
    private Paint bmpPaint;
    private  Paint tilePaint;
    private  Paint rectPaint;
    private  Paint minePaint;
    private Random rd=new Random();
    public  int mapWidth;//��ͼ����
    public int mapHeight;//��ͼ����
    public boolean isDrawAllMine=false;//����Ƿ���
  private  int[][] dir={
            {-1,1},//���Ͻ�
            {0,1},//����
            {1,1},//���Ͻ�
            {-1,0},//����
            {1,0},//����
            {-1,-1},//���½�
            {0,-1},//����
            {1,-1}//���½�
    };//��ʾ�˸�����
 
  public   class Tile{
        short value;
        boolean flag;
        boolean open;
      public Tile()
      {
          this.value=0;
          this.flag=false;
          this.open=false;
      }
    }
 
   public static class Point{
        private int x;
        private int y;
        public Point(int x,int y)
        {
            this.x=x;
            this.y=y;
        }
 
        @Override
        public int hashCode() {
            // TODO Auto-generated method stub
            return 2*x+y;
        }
 
        @Override
        public boolean equals(Object obj) {
            // TODO Auto-generated method stub
            return this.hashCode()==((Point)(obj)).hashCode();
 
        }
    }//��ʾÿ���׿�
 
 
    public Mine(int x, int y, int mapCol, int mapRow, int mineNum, int tileWidth)
    {
        this.x=x;
        this.y=y;
        this.mapCol = mapCol;
        this.mapRow = mapRow;
        this.mineNum=mineNum;
        this.tileWidth=tileWidth;
        mapWidth=mapCol*tileWidth;
        mapHeight=mapRow*tileWidth;
 
        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(MainActivity.W/10);
        textPaint.setColor(Color.RED);
 
        bmpPaint=new Paint();
        bmpPaint.setAntiAlias(true);
        bmpPaint.setColor(Color.DKGRAY);
 
        tilePaint =new Paint();
        tilePaint.setAntiAlias(true);
        tilePaint.setColor(0xff1faeff);
 
        minePaint =new Paint();
        minePaint.setAntiAlias(true);
        minePaint.setColor(0xffff981d);
 
        rectPaint =new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(0xff000000);
        rectPaint.setStyle(Paint.Style.STROKE);
 
        tile=new Tile[mapRow][mapCol];
    }
    /**
     * ��ʼ����ͼ
     */
    public  void init()
    {
        for (int i = 0; i< mapRow; i++)
        {
            for (int j = 0; j< mapCol; j++)
            {
                tile[i][j]=new Tile();
                tile[i][j].value=EMPTY;
                tile[i][j].flag=false;
                tile[i][j].open=false;
                isDrawAllMine=false;
            }
 
        }
    }
 
    /**
     * ������
     * @param exception �ų���λ�ã���λ�ò�������
     */
    public void create(Point exception)
    {
        List<Point> allPoint=new LinkedList<Point>();
 
        //������λ�ü�������
        for (int i = 0; i< mapRow; i++)//y
        {
            for (int j = 0; j < mapCol; j++)//x
            {
                Point point=new Point(j,i);
                if(!point.equals(exception))
                {
                    allPoint.add(point);
                }
            }
        }
 
        List<Point> minePoint=new LinkedList<Point>();
        //���������
        for (int i=0; i< mineNum; i++)
        {
            int idx=rd.nextInt(allPoint.size());
            minePoint.add(allPoint.get(idx));
            allPoint.remove(idx);//ȡ��֮�󣬴����м������Ƴ�
        }
 
        //�ھ����б���׵�λ��
        for(Iterator<Point> it=minePoint.iterator();it.hasNext();)
        {
            Point p=it.next();
            tile[p.y][p.x].value=MINE;
        }
 
        //����ͼ�������
        for (int i = 0; i< mapRow; i++)//y
        {
            for (int j = 0; j< mapCol; j++)//x
            {
                short t=tile[i][j].value;
                if(t==MINE)
                {
                    for (int k=0;k<8;k++)
                    {
                        int offsetX=j+dir[k][0],offsetY=i+dir[k][1];
                        if(offsetX>=0&&offsetX< mapCol &&offsetY>=0&&offsetY< mapRow ) {
                            if (tile[offsetY][offsetX].value != -1)
                            tile[offsetY][offsetX].value += 1;
                        }
                    }
                }
            }
        }
 
    }
 
 
    /**
     * ��ĳ��λ��
     * @param op
     * @param isFirst ����Ƿ��ǵ�һ�δ�
     */
 
    public void open(Point op,boolean isFirst)
    {
            if(isFirst)
            {
                create(op);
            }
 
            tile[op.y][op.x].open=true;
            if( tile[op.y][op.x].value==-1)
                return;
            else if( tile[op.y][op.x].value>0)//�������ֿ�
            {
                return;
            }
 
             //������ȱ����ö���
            Queue<Point> qu=new LinkedList<Point>();
            //�����һ����
            qu.offer(new Point(op.x,op.y));
 
            //��8���������
            for (int i=0;i<8;i++)
            {
                int offsetX=op.x+dir[i][0],offsetY=op.y+dir[i][1];
                //�ж�Խ����Ƿ��ѷ���
                boolean isCan=offsetX>=0&&offsetX< mapCol &&offsetY>=0&&offsetY< mapRow;
                if(isCan)
                {
                    if(tile[offsetY][offsetX].value==0 &&!tile[offsetY][offsetX].open) {
                        qu.offer(new Point(offsetX, offsetY));
                    }
                    else if(tile[offsetY][offsetX].value>0)
                    {
                        tile[offsetY][offsetX].open=true;
                    }
                }
 
            }
 
            while(qu.size()!=0)
            {
                Point p=qu.poll();
                tile[p.y][p.x].open=true;
                for (int i=0;i<8;i++)
                {
                    int offsetX=p.x+dir[i][0],offsetY=p.y+dir[i][1];
                    //�ж�Խ����Ƿ��ѷ���
                    boolean isCan=offsetX>=0&&offsetX< mapCol &&offsetY>=0&&offsetY< mapRow;
                    if(isCan)
                    {
                        if( tile[offsetY][offsetX].value==0&&!tile[offsetY][offsetX].open) {
                            qu.offer(new Point(offsetX, offsetY));
                        }
                        else if(tile[offsetY][offsetX].value>0)
                        {
                            tile[offsetY][offsetX].open=true;
                        }
                    }
 
                }
            }
 
    }
 
    /**
     * ���Ƶ�ͼ
     * @param canvas
     */
    public  void draw(Canvas canvas)
    {
 
 
        for (int i = 0; i< mapRow; i++)
        {
            for (int j = 0; j< mapCol; j++)
            {
                Tile t=tile[i][j];
                if(t.open){
                    if(t.value>0)
                    {
                        canvas.drawText(t.value+"",x+j*tileWidth,y+i*tileWidth+tileWidth,textPaint);
                    }
 
                }else
                {
                    //��ǣ�����
                    if(t.flag)
                    {
 
                    }else
                    {
                        //�����η���
                        RectF reactF=new RectF(x+j*tileWidth,y+i*tileWidth,x+j*tileWidth+tileWidth,y+i*tileWidth+tileWidth);
                        canvas.drawRoundRect(reactF,0,0, tilePaint);
                    }
                }
                //�Ƿ񻭳�������
                if( isDrawAllMine&&tile[i][j].value==-1) {
                    canvas.drawCircle((x + j * tileWidth) + tileWidth / 2, (y + i * tileWidth) + tileWidth / 2, tileWidth / 2, bmpPaint);
                }
            }
        }
 
        //���߿�
        canvas.drawRect(x,y,x+mapWidth,y+mapHeight, rectPaint);
        //������
        for (int i = 0; i< mapRow; i++) {
            canvas.drawLine(x,y+i*tileWidth,x+mapWidth,y+i*tileWidth, rectPaint);
        }
        //������
        for (int i = 0;i < mapCol; i++) {
            canvas.drawLine(x+i*tileWidth,y,x+i*tileWidth,y+mapHeight, rectPaint);
        }
 
    }
 
}

	
	


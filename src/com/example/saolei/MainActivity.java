package com.example.saolei;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Menu;

public class MainActivity extends Activity {
    public  static  int W;
    public  static  int H;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        W = dm.widthPixels;//���
         H = dm.heightPixels ;//�߶�
 
        setContentView(new MainView(this));
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("��Ϸ����")
                .setMessage("������Ϊ�����׵�λ��ȫ���㿪��ֻ�������׵�λ�ã�ÿ����Ϸ��10���ס�\n\n--�Բ۹�����")
                .setPositiveButton("��֪����",null)
                .create()
                .show();
    }
}

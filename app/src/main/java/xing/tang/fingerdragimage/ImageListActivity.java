package xing.tang.fingerdragimage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * MainActivity
 *
 * @Description:
 * @Author: xing.tang
 * @CreateDate: 2020/7/18 9:05 PM
 */
public class ImageListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageListAdapter imageListAdapter;
    // 图片集合
    private ArrayList<String> urlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        setData();
        initView();
    }

    private void setData() {
        // 随便在网上找的图片（正常大小图、长图、宽图、GIF图），因为是网上找的图片资源，所以存在图片失效可能性。
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090822381&di=d4df7e10e6550b28f1e4d04f324ff1a9&imgtype=0&src=http%3A%2F%2Fa1.att.hudong.com%2F05%2F00%2F01300000194285122188000535877.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090822382&di=b616b4449ebcda8d41fc1c5bb0e6aa8e&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F14%2F75%2F01300000164186121366756803686.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595065433392&di=889fe31e5ad682f52218e37534cadbf0&imgtype=0&src=http%3A%2F%2Fwx1.sinaimg.cn%2Forj360%2F0065KBoBly1gd8f4hfrlcj30ku23qtjn.jpg");
        urlList.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3454706213,1956043842&fm=26&gp=0.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595091048894&di=60930f6da3a2a819ef17c930dff9edea&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%3D580%2Fsign%3D34a96415c3ea15ce41eee00186013a25%2Ff80e6d224f4a20a471b136579a529822730ed08f.jpg");
        urlList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1874247436,792516452&fm=26&gp=0.jpg");
        urlList.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=151472226,3497652000&fm=26&gp=0.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595074736778&di=217453e73915ce61ab06103e9ff044d5&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F84%2F95%2F01300000244525126132956029806.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090822381&di=c6c77485f6f8dc2a11d3e1ab7a5cabaf&imgtype=0&src=http%3A%2F%2Fp2.so.qhimgs1.com%2Ft01dfcbc38578dac4c2.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090822379&di=a8bae9620189fc514fd99536c392898b&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F22%2F59%2F19300001325156131228593878903.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090822378&di=a58ea98cc78b24a61f2ac95e14a14c9c&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F16%2F12%2F01300535031999137270128786964.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947679&di=7a2ff8d620327e1ba7514c48f00eee97&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1304%2F17%2Fc5%2F19955421_1366189671581.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947679&di=0978e40ef0305af10cca9e7205b27650&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1308%2F15%2Fc5%2F24496183_1376533418348.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947678&di=0b0a549dd94a4c829b2e4f85acfd8165&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201604%2F23%2F002205xqdkj84gnw4oi85v.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947678&di=c85b310f902acb630c55db713a3ba942&imgtype=0&src=http%3A%2F%2Fattach.bbs.miui.com%2Fforum%2F201401%2F23%2F095609lsejfi4thjrrwydj.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947677&di=d00e41af2cf670928916b430151d367d&imgtype=0&src=http%3A%2F%2F2f.zol-img.com.cn%2Fproduct%2F68%2F687%2FceHBXIb1L3Mpc.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947676&di=50be247671c3c433256c5b5b017bea2f&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1212%2F28%2Fc2%2F16969637_1356689230327.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947675&di=1bdb9f4a46eebe5c7f685b6b9c84f7d6&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1307%2F10%2Fc0%2F23166997_1373443405182.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947675&di=a9025144a4362b9d2630f9e4d5f9f59b&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fwallpaper%2F1307%2F22%2Fc8%2F23614483_1374477687381.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595090947675&di=615da39dde54f7a3cdd147186ddbebaa&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F2cf5e0fe9925bc31c58bcbc05cdf8db1ca137090.jpg");
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        imageListAdapter = new ImageListAdapter(urlList);
        imageListAdapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ImageListActivity.this, ImageDetailActivity.class);
                intent.putExtra("position", position);
                intent.putStringArrayListExtra("urls", urlList);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(imageListAdapter);
    }
}

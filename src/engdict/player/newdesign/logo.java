package engdict.player.newdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class logo extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 1000); //1.5�ʵڿ� �ٸ� ��Ƽ��Ƽ��...
    }

    //���÷��� 
    class splashhandler implements Runnable {
       
        public void run() {
            Intent intent = new Intent(logo.this, Update_check.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            //finish�� ���־�� �ڷΰ��� ������ �ΰ� ȭ���� �ȳ�������
            finish();
        }        
    }
}

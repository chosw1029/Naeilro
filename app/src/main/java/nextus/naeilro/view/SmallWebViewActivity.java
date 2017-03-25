package nextus.naeilro.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivitySmallWebViewBinding;

public class SmallWebViewActivity extends AppCompatActivity {

    ActivitySmallWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_small_web_view);
        setUpDisplay();
        setUpWebView(binding.webView);
    }

    public void setUpDisplay()
    {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width = (int) (display.getWidth() * 0.95); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 0.95);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    public void setUpWebView(WebView webView)
    {
        String url = getIntent().getStringExtra("url");
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WishWebViewClient());
    }

    private class WishWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.anim_default, R.anim.anim_slide_out_down);
    }
}

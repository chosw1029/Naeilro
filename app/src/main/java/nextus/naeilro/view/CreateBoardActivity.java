package nextus.naeilro.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import nextus.naeilro.R;
import nextus.naeilro.databinding.ActivityCreateBoardBinding;
import nextus.naeilro.viewmodel.CreateBoardVM;

public class CreateBoardActivity extends BaseActivity {

    ActivityCreateBoardBinding binding;
    CreateBoardVM createBoardVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_board);
        createBoardVM = new CreateBoardVM(this);
        binding.setViewModel(createBoardVM);
        setUpDisplay();
    }

    public void setUpDisplay()
    {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int width =  (int)(display.getWidth() * 0.95); //Display 사이즈의 70%
        int height = (int)(display.getHeight() * 0.95);  //Display 사이즈의 90%

        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

}

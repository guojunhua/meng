package com.wangdh.mengm.ui.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wangdh.mengm.R;
import com.wangdh.mengm.base.BaseActivity;
import com.wangdh.mengm.bean.bmob.AccountBean;
import com.wangdh.mengm.component.AppComponent;
import com.wangdh.mengm.utils.KeyboardUtils;
import com.wangdh.mengm.utils.SharedPreferencesMgr;
import com.wangdh.mengm.widget.FilterImageView;
import java.util.List;
import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.b_iv)
    ImageView imageView;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.sc)
    FilterImageView sc;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register)
    TextView mtv;
    @BindView(R.id.wjmm)
    TextView mTv;
    private String password, phone;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        imageView.setOnClickListener(v -> this.finish());
        Login();
    }

    @Override
    protected void initData() {
        initLogin();
        onClicked();
    }

    private void Login() {
        if (!SharedPreferencesMgr.getString("password", "")
                .equals("") && !SharedPreferencesMgr.getString("phone", "").equals("")) {
            password = SharedPreferencesMgr.getString("password", "");
            phone = SharedPreferencesMgr.getString("phone", "");
            etPhone.setText(phone);
            etYzm.setText(password);
        }
    }

    private void onClicked() {
        mtv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        sc.setOnClickListener(v -> etPhone.setText(""));
        mTv.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, PassWordActivity.class)));
    }

    private void initLogin() {
        login.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(this);
            password = etYzm.getText().toString().trim();
            phone = etPhone.getText().toString().trim();
            if (!password.equals("") && !phone.equals("")) {
                showDialog();
                BmobQuery<AccountBean> query = new BmobQuery<>();
                query.addWhereEqualTo("name", phone);
                query.addWhereEqualTo("password", password);
                query.findObjects(new FindListener<AccountBean>() {
                    @Override
                    public void done(List<AccountBean> list, BmobException e) {
                        if (e == null) {
                                if (list.get(0).getName().equals(phone) && list.get(0).getPassword().equals(password)) {
                                    SharedPreferencesMgr.setString("password", password);
                                    SharedPreferencesMgr.setString("phone", phone);
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                        } else {
                            toast("登陆失败");
                        }
                        hideDialog();
                    }
                });
            } else {
                toast("账户或者密码不能为空");
            }
        });
//        login.setOnClickListener(v -> {
//            KeyboardUtils.hideSoftInput(this);
//            password = etYzm.getText().toString().trim();
//            phone = etPhone.getText().toString().trim();
//            if(!password.equals("")&&!phone.equals("")) {
//                showProgress("正在登陆");
//                Map<String, String> params = new HashMap<>();
//                params.put("name", phone);
//                params.put("password", password);
//                ApiManager.getApiInstance().getMyService().LoginRxJava(params)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<RegisterBean>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.i("toast", "login  " + e.toString());
//                                hideProgress();
//                                toast("登陆失败,请确认账户密码是否正确、网络是否链接");
//                            }
//
//                            @Override
//                            public void onNext(RegisterBean registerBean) {
//                                if (registerBean.getFlag().equals("false")) {
//                                    toast(registerBean.getMessage());
//                                } else {
//                                    hideProgress();
//                                    SPUtil.put(LoginActivity.this, "vip", registerBean.getIsVip());
//                                    SPUtil.put(LoginActivity.this, "password", password);
//                                    SPUtil.put(LoginActivity.this, "phone", phone);
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    LoginActivity.this.finish();
//                                }
//                            }
//                        });
//            }else {
//                toast("账户或者密码不能为空");
//            }
//        });
    }
}

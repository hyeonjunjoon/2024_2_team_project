package com.example.dalendar;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Login_Activity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText etEmail, etPwd;
    private TextView tvIncorrectPwd;
    private Button btnLogin;
    ImageButton imgBtnBackPress;

    public void shakeTextView(TextView textView) {
        ObjectAnimator shakeAnim = ObjectAnimator.ofFloat(textView, "translationX", -10f, 10f);
        shakeAnim.setDuration(50); // 1 진동당 시간
        shakeAnim.setRepeatCount(5); // 반복 횟수
        shakeAnim.setRepeatMode(ObjectAnimator.REVERSE); // 왔다 갔다 반복

        shakeAnim.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.inLogAct_email);
        etPwd = findViewById(R.id.inLogAct_Pwd);
        btnLogin = findViewById(R.id.inLoginAct_login_btn);
        imgBtnBackPress = findViewById(R.id.inLoginActBackPress);
        tvIncorrectPwd = findViewById(R.id.inLogAct_incorrectIdPwd);

        tvIncorrectPwd.setVisibility(View.INVISIBLE);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // 로그인 버튼 클릭 리스너
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = etEmail.getText().toString().trim();
                String userPwd = etPwd.getText().toString().trim();

                if (userEmail.isEmpty()) {
                    etEmail.setError("이메일을 입력하세요");
                    return;
                }
                if (userPwd.isEmpty()) {
                    etPwd.setError("비밀번호를 입력하세요");
                    return;
                }

                // Firebase 로그인 처리 ㄱㄱ
                mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPwd)
                        .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    shakeTextView(tvIncorrectPwd);
                                    tvIncorrectPwd.setVisibility(View.VISIBLE);
                                    Toast.makeText(Login_Activity.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // 뒤로 가기 버튼 클릭 리스너
        imgBtnBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
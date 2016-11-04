package br.ufrn.imd.consultantfouls;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void logar(View v) {
        Intent i = new Intent(this, ResultActivity.class);
        OAuthTokenRequest.getInstance().getTokenCredential(this,"http://apitestes.info.ufrn.br/authz-server","sise-id", "segredo", i);
    }

    public void obterDados(View v){
        Intent intent = new Intent(this, ResultActivity.class);
        //intent.putExtra("token", credential.getAccessToken());
        startActivity(intent);
    }

    public void sair(View view) {
        OAuthTokenRequest.getInstance().logout(this, "http://apitestes.info.ufrn.br/sso-server/logout");
    }
}

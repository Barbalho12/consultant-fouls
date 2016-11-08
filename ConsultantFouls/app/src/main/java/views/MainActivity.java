package views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import auth.OAuthTokenRequest;
import br.ufrn.imd.consultantfouls.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void consultar(View v) {
        if (isOnline()){
            try{
                Intent intent = new Intent(this, ProcessingActivity.class);
                OAuthTokenRequest.getInstance().getTokenCredential(this,"http://apitestes.info.ufrn.br/authz-server","sise-id", "segredo", intent);
            }catch (Exception ERR){
                Toast.makeText(getApplicationContext(), "Erro ao tentar fazer login", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Erro ao tentar se conectar a Internet", Toast.LENGTH_LONG).show();
        }

    }

    public void sair(View view) {
        try{
            OAuthTokenRequest.getInstance().logout(this, "http://apitestes.info.ufrn.br/sso-server/logout");
            Toast.makeText(getApplicationContext(), "Usuário desconectado", Toast.LENGTH_LONG).show();
        }catch (Exception ERR){
            Toast.makeText(getApplicationContext(), "Erro ao tentar deslogar", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}

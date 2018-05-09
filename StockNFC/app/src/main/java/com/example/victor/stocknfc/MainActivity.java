package com.example.victor.stocknfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.victor.stocknfc.fragmetos.Fragmento_ControlStock;
import com.example.victor.stocknfc.fragmetos.Fragmento_ListaAlertas;
import com.example.victor.stocknfc.fragmetos.Fragmento_ListadoPedidos;
import com.example.victor.stocknfc.fragmetos.ListaArticulos;
import com.example.victor.stocknfc.logIn.Fragmento_Registro;
import com.example.victor.stocknfc.logIn.LogIn;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Datos del intent LogIn
    String nombreUsuario;
    String emailUsuario;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Modificamos los datos de la cabecera del menu lateral
        View hView = navigationView.getHeaderView(0);
        TextView nombreUsuario = hView.findViewById(R.id.nombreUsuarioMenu);
        TextView emailUsuario = (TextView) hView.findViewById(R.id.emailUsuarioMenu);
        //Obtenemos nombre e email del usuario desde el intent
        recibirDatos();
        nombreUsuario.setText(this.nombreUsuario);
        emailUsuario.setText(this.emailUsuario);

        //Cargamos por defecto el listado de productos almacenado
        cargarFragmento(new ListaArticulos());
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        uncheckAllMenuItems(navigationView);
        if (id == R.id.consultarProductoMenu) {
            cargarFragmento(new ListaArticulos());
        } else if (id == R.id.administrarStock) {
            cargarFragmento(new Fragmento_ControlStock());
        } else if (id == R.id.pedidoMenu) {
            cargarFragmento(new Fragmento_ListadoPedidos());
        } else if (id == R.id.alertaStockMenu) {
            cargarFragmento(new Fragmento_ListaAlertas());
        } else if (id == R.id.anhadirUsuarioMenu) {
            cargarFragmento(new Fragmento_Registro());
        } else if (id == R.id.cerrarSesion) {

            Intent logInIntent = new Intent(this, LogIn.class);
            startActivity(logInIntent);
            finish();
        }

        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cargarFragmento(Fragment fragmento) {
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction().replace(R.id.contenedorFragments, fragmento).commit();
    }


    public void recibirDatos() {
        Bundle extras = getIntent().getExtras();
        nombreUsuario = extras.getString("nombreUsuario");
        emailUsuario = extras.getString("emailUsuario");
    }

    private void uncheckAllMenuItems(NavigationView navigationView) {
        final Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }
}

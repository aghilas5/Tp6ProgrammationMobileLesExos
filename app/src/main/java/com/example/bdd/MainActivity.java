package com.example.bdd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SimpleDialogListener{

    final String PREFS_NAME = "preferences_file";

    private RecyclerView mRecyclerView;
    private MonRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    CoordinatorLayout mcoordinatorLayout;
    private FloatingActionButton button;
    ArrayList<Planete> planetes = new ArrayList<>();
    PlaneteDao planeteDao;

    TextView tv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  tv = findViewById(R.id.tv);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        //mLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(mLayoutManager);
       // mAdapter = new MonRecyclerViewAdapter((ArrayList<Planete>) planeteDao);
        //mRecyclerView.setAdapter(mAdapter);
        mcoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        button=(FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AjouterUnePlanete();

            }
        });


        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "planetesDB").build();

        planeteDao = db.planeteDao();

        loadData(planeteDao);
    }

    private void loadData(PlaneteDao planeteDao) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {

                if (settings.getBoolean("is_data_loaded", true)) {
                    initData(planeteDao);
                    settings.edit().putBoolean("is_data_loaded", false).commit();
                }

                List<Planete> planetes = planeteDao.getAll();

                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        /*tv.setText("Il y a [" + planetes.size() + "] Planètes dans la base de données" );
                        for (int i =0; i< planetes.size();i++) {
                            Toast.makeText(MainActivity.this, "Planete = " + planetes.get(i).getNom(), Toast.LENGTH_SHORT).show();
                    }*/
                        mAdapter = new MonRecyclerViewAdapter(planetes);
                        mRecyclerView.setAdapter(mAdapter);
                        //mAdapter.setDetecteurDeClicSurRecycler(MainActivity.this::clicSurRecyclerItem);



                    }
                });

            }
        }).start();

    }

    private void initData(PlaneteDao planeteDao) {



       /* planetes.add(new Planete(1,"Mercure",4900));
        planetes.add(new Planete(2,"Venus",12000));
        planetes.add(new Planete(3,"Terre",12800));
        planetes.add(new Planete(4,"Mars",6800));
        planetes.add(new Planete(5,"Jupiter",144000));
        planetes.add(new Planete(6,"Saturne",120000));
        planetes.add(new Planete(7,"Uranus",52000));
        planetes.add(new Planete(8,"Neptune",50000));
        planetes.add(new Planete(9,"Pluton",2300));*/

        planetes.add(new Planete(1,"Mercure",4900,R.drawable.mercure));
        planetes.add(new Planete(2,"Venus",12000,R.drawable.venus));
        planetes.add(new Planete(3,"Terre",12800,R.drawable.earth));
        planetes.add(new Planete(4,"Mars",6800,R.drawable.mars));
        planetes.add(new Planete(5,"Jupiter",144000,R.drawable.jupiter));
        planetes.add(new Planete(6,"Saturne",120000,R.drawable.saturne));
        planetes.add(new Planete(7,"Uranus",52000,R.drawable.uranus));
        planetes.add(new Planete(8,"Neptune",50000,R.drawable.neptune));
        planetes.add(new Planete(9,"Pluton",2300,R.drawable.pluton));

        for (int index = 0; index < planetes.size(); index++) {
            Planete planete = planetes.get(index);
            planeteDao.insert(planete);
        }
    }

    public void clicSurRecyclerItem(int position, View v) {
        TextView titre= v.findViewById(R.id.tv_principal);
        Snackbar.make(mcoordinatorLayout, "La planète " + titre.getText(),Snackbar.LENGTH_LONG).show();
    }
    private void AjouterUnePlanete(){

        FragmentManager fm = getSupportFragmentManager();
        FragmentInterface simpleDialogFragment = FragmentInterface.newInstance(20,"aghilas",300,R.drawable.uranus);
        simpleDialogFragment.show(fm, "fragment_simple_dialog");

    }

    @Override
    public void onOkClickDialog(String nom) {
        Toast.makeText(this, "Pour valider l'insertion dans la BDD cliquez une autre fois sur OK!! Revenez à l'application, " + nom, Toast.LENGTH_SHORT).show();
    }




    public void AjoutPlaneteDansLaBaseDonee(Planete planete) {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                planeteDao.insert(planete);
                planetes.add(planete);


                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();


                    }





                });

            }
        }).start();

    }

   /* @Override
    protected void onResume() {
        super.onResume();
        mAdapter.setDetecteurDeClicSurRecycler(this);
    }*/
}
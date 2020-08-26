package com.pedagogiksolution.CFPStJerome;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pedagogiksolution.CFPStJerome.fragment.AccueilFragment;
import com.pedagogiksolution.CFPStJerome.fragment.CalendrierFragment;
import com.pedagogiksolution.CFPStJerome.fragment.DialogueFragment;
import com.pedagogiksolution.CFPStJerome.fragment.MessagesFragment;
import com.pedagogiksolution.CFPStJerome.fragment.NotificationFragment;
import com.pedagogiksolution.CFPStJerome.fragment.NouvelleFragment;
import com.pedagogiksolution.CFPStJerome.fragment.PreferencesFragment;
import com.pedagogiksolution.CFPStJerome.fragment.RappelsFragment;
import com.pedagogiksolution.CFPStJerome.fragment.RepertoireAdminFragment;
import com.pedagogiksolution.CFPStJerome.fragment.RepertoireEnseignantFragment;
import com.pedagogiksolution.CFPStJerome.fragment.RepertoireSoutienFragment;
import com.pedagogiksolution.CFPStJerome.utils.Constants;
import com.pedagogiksolution.CFPStJerome.utils.DatabaseHelper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    FirebaseFirestore db;
    ActionBarDrawerToggle toggle;
    Boolean addToStack = false;
    Boolean addToStackAccueil = false;
    static DatabaseHelper dbHelper;
    CreateOrUpdateDatabase createOrUpdateDatabase;
    GetCalendrierFromGoogleStorage getCalendrierFromGoogleStorage;
    static SharedPreferences sharedPref;
    static int version_code;
    Toolbar toolbar;
    Fragment newFragment;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected = false;
    Long last_login, now, should_i_update;
    private static final long delay = 2000L;
    private boolean mRecentlyBackPressed = false;
    private static String TAG_NAME_TOPIC_ABONNEMENT = "Abonnement";
    boolean mTest4=false;
    boolean mTest5=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // on instantie le layout principal
        setContentView(R.layout.activity_main);

        // on récupère le contexte de l'application
        context = getBaseContext();

        // on ouvre firestore
        db = FirebaseFirestore.getInstance();

        // on crée un objet pour nos accès base de données
        dbHelper = new DatabaseHelper(context);

        // initialisation de la toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // init du Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        // init de la vue de navigation (tiroir du drawer)
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);


        // on récupère le datastore version pour gestion des updates et téléchargement des bdd --> initialise a 0 si vide
        sharedPref = getSharedPreferences("version", 0);
        int version = sharedPref.getInt("version", 0);

        // récupère la version actuellement installé et place dans variable version_code
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_code = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        // on compare la version du datastore avec celle de l'apk --> si elle sont égale on fait rien, si elles sont différente
        // il s'agit d'un update ou de la première installation, donc on initialise les bdd et les datastores
        if (version_code != version) {

            long now = System.currentTimeMillis();
            sharedPref = getSharedPreferences("update", 0);
            SharedPreferences.Editor editor2 = sharedPref.edit();
            editor2.putLong("last_login", now);
            editor2.apply();
            createOrUpdateDatabase = new CreateOrUpdateDatabase(dbHelper);
            createOrUpdateDatabase.execute();
            if (version == 0) {
                // on récupère datastore visite pour les tutos et initialise a 0

                // on create ou  update les bdd


                db = FirebaseFirestore.getInstance();
                db.collection("segmentNotifications").orderBy("rangCategorie").orderBy("rang").limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {


                                        String documentId = document.getId();
                                        FirebaseMessaging.getInstance().subscribeToTopic(documentId);
                                        SharedPreferences mSharedPref = getSharedPreferences(documentId,0);
                                        mSharedPref.getBoolean(TAG_NAME_TOPIC_ABONNEMENT,false);
                                        SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
                                        editor[0] = mSharedPref.edit();
                                        editor[0].putBoolean(TAG_NAME_TOPIC_ABONNEMENT,true);
                                        editor[0].apply();
                                    }
                                }

                            }
                        });

            }


        } else {
            // on recupere ladate du dernier update et on retelecharge les calendrier si plus de 14 jours
            sharedPref = getSharedPreferences("update", 0);
            last_login = sharedPref.getLong("last_login", 0);
            now = System.currentTimeMillis();
            should_i_update = now - last_login;
            if (should_i_update > 1209600000) {
                /* **************************************88 */

                getCalendrierFromGoogleStorage = new GetCalendrierFromGoogleStorage();
                getCalendrierFromGoogleStorage.execute();
                long now = System.currentTimeMillis();
                sharedPref = getSharedPreferences("update", 0);
                SharedPreferences.Editor editor2 = sharedPref.edit();
                editor2.putLong("last_login", now);
                editor2.apply();
                /* ***************************************************** */
                db = FirebaseFirestore.getInstance();
                db.collection("segmentNotifications").orderBy("rangCategorie").orderBy("rang").limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful())
                                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {


                                        String documentId = document.getId();
                                        FirebaseMessaging.getInstance().subscribeToTopic(documentId);
                                        SharedPreferences mSharedPref = getSharedPreferences(documentId, 0);
                                        mSharedPref.getBoolean(TAG_NAME_TOPIC_ABONNEMENT, false);
                                        SharedPreferences.Editor[] editor = new SharedPreferences.Editor[1];
                                        editor[0] = mSharedPref.edit();
                                        editor[0].putBoolean(TAG_NAME_TOPIC_ABONNEMENT, true);
                                        editor[0].apply();
                                    }

                            }
                        });
            }

        }
        // fin du if version_code == version

            mTest4 = getIntent().getBooleanExtra("RAPPEL", false);
            mTest5 = getIntent().getBooleanExtra("PUSH", false);
            boolean mTest6 = getIntent().getBooleanExtra("PUSH2", false);

            String mTestUrl = getIntent().getStringExtra("URL");
            if (mTestUrl!=null&&mTest6){
                String testUrl = mTestUrl.substring(0,7);
                if(testUrl.equalsIgnoreCase("https:/")||testUrl.equalsIgnoreCase("http://")) {
                    Intent mI = new Intent(Intent.ACTION_VIEW, Uri.parse(mTestUrl));
                    startActivity(mI);
                    getIntent().removeExtra("URL");

                } else {
                    Toast.makeText(
                            this,R.string.erreur_url,
                            Toast.LENGTH_LONG).show();
                    newFragment = new AccueilFragment();
                    toolbar.setTitle(R.string.app_name);
                    addToStackAccueil = false;
                    openFragment(newFragment, addToStack);
                }
            } else {
                if (mTest4) {
                    newFragment = new RappelsFragment();
                    toolbar.setTitle("Mes rappels");
                    addToStackAccueil = false;
                    openFragment(newFragment, addToStack);
                    getIntent().removeExtra("RAPPEL");
                    mTest4=false;
                } else if (mTest5) {
                    newFragment = new MessagesFragment();
                    toolbar.setTitle("Mes messages");
                    addToStackAccueil = false;
                    openFragment(newFragment, addToStack);
                    mTest5=false;
                } else {
                    newFragment = new AccueilFragment();
                    toolbar.setTitle(R.string.app_name);
                    addToStackAccueil = false;
                    openFragment(newFragment, addToStack);
                }

            }


        // implementation pour les push notification sur version 26 et plus du OS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            final String channelId = getString(R.string.default_notification_channel_id);
            final String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_LOW));
            }
        }




    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_accueil:
                newFragment = new AccueilFragment();
                toolbar.setTitle(R.string.app_name);
                openFragment(newFragment, addToStack);
                break;
            case R.id.nav_nouvelles:
                cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    newFragment = new NouvelleFragment();
                    toolbar.setTitle("Les nouvelles");
                    openFragment(newFragment, addToStack);
                } else {
                    DialogFragment dialogueFragment = new DialogueFragment();
                    Bundle mType = new Bundle();
                    mType.putInt("dialogType", 2);
                    dialogueFragment.setArguments(mType);
                    dialogueFragment.show(getSupportFragmentManager(), "dialog");

                }

                break;
          case R.id.nav_notification:


              cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

              activeNetwork = cm.getActiveNetworkInfo();
              isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

              if (isConnected) {

                  newFragment = new NotificationFragment();
                  toolbar.setTitle("Notifications");
                  openFragment(newFragment, addToStack);

              } else {
                  DialogFragment dialogueFragment = new DialogueFragment();
                  Bundle mType = new Bundle();
                  mType.putInt("dialogType", 2);
                  dialogueFragment.setArguments(mType);
                  dialogueFragment.show(getSupportFragmentManager(), "dialog");

              }




                break;


            case R.id.nav_repertoire_administration:
                cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    newFragment = new RepertoireAdminFragment();
                    toolbar.setTitle("Répertoire administration");
                    openFragment(newFragment, addToStack);
                } else {
                    DialogFragment dialogueFragment = new DialogueFragment();
                    Bundle mType = new Bundle();
                    mType.putInt("dialogType", 2);
                    dialogueFragment.setArguments(mType);
                    dialogueFragment.show(getSupportFragmentManager(), "dialog");

                }

                break;
            case R.id.nav_repertoire_enseignants:
                cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    newFragment = new RepertoireEnseignantFragment();
                    toolbar.setTitle("Les enseignants");
                    openFragment(newFragment, addToStack);
                } else {
                    DialogFragment dialogueFragment = new DialogueFragment();
                    Bundle mType = new Bundle();
                    mType.putInt("dialogType", 2);
                    dialogueFragment.setArguments(mType);
                    dialogueFragment.show(getSupportFragmentManager(), "dialog");

                }

                break;
            case R.id.nav_repertoire_soutien:
                cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                activeNetwork = cm.getActiveNetworkInfo();
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    newFragment = new RepertoireSoutienFragment();
                    toolbar.setTitle("Services aux élèves");
                    openFragment(newFragment, addToStack);
                } else {
                    DialogFragment dialogueFragment = new DialogueFragment();
                    Bundle mType = new Bundle();
                    mType.putInt("dialogType", 2);
                    dialogueFragment.setArguments(mType);
                    dialogueFragment.show(getSupportFragmentManager(), "dialog");

                }

                break;


            case R.id.nav_messages:
                newFragment = new MessagesFragment();
                toolbar.setTitle("Mes messages");
                openFragment(newFragment, addToStack);
                break;

            case R.id.nav_calendrier:
                newFragment = new CalendrierFragment();
                toolbar.setTitle("Calendrier");
                openFragment(newFragment, addToStack);
                break;

            case R.id.nav_rappel:
                newFragment = new RappelsFragment();
                toolbar.setTitle("Mes rappels");
                openFragment(newFragment, addToStack);
                break;
            case R.id.nav_parametre:
                newFragment = new PreferencesFragment();
                toolbar.setTitle("Paramètres");
                openFragment(newFragment, addToStack);
                break;


            default:
                newFragment = new AccueilFragment();
                toolbar.setTitle(R.string.app_name);
                addToStack = false;
                openFragment(newFragment, addToStack);

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public void onBackPressed() {
        Handler mExitHandler = new Handler();
        Runnable mExitRunnable = new Runnable() {
            @Override
            public void run() {
                mRecentlyBackPressed = false;
            }
        };
        sharedPref = getSharedPreferences("backstack", 0);
        int mBackStack = sharedPref.getInt("webview", 0);



        if (mBackStack == 1) {

            sharedPref = getSharedPreferences("backstack", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("webview", 0);
            editor.apply();

            super.onBackPressed();
        } else {
            if (mRecentlyBackPressed) {
                if(mExitHandler!=null){
                mExitHandler.removeCallbacks(mExitRunnable);
                mExitHandler = null;
                    mRecentlyBackPressed = false;
                super.onBackPressed();}
            } else if (!mRecentlyBackPressed
                    || getSupportFragmentManager().getBackStackEntryCount() != 0) {
                mRecentlyBackPressed = true;
                Toast.makeText(
                        this,
                        "1-Clique encore pour sortir\n2-Appuie sur le menu pour continuer",
                        Toast.LENGTH_SHORT).show();
                mExitHandler.postDelayed(mExitRunnable, delay);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onPause() {
        super.onPause();


    }

    private void openFragment(Fragment newFragment, Boolean addToStack) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.content_frame, newFragment);

        if (addToStack) {
            transaction.addToBackStack(null);
        }


        transaction.commit();
    }

    private class CreateOrUpdateDatabase extends AsyncTask<Void, Void, Void> {

        DatabaseHelper dbHelper;

        public CreateOrUpdateDatabase(DatabaseHelper dbHelper) {

            this.dbHelper = dbHelper;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            dbHelper.open();


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            getCalendrierFromGoogleStorage = new GetCalendrierFromGoogleStorage();
            getCalendrierFromGoogleStorage.execute();
            sharedPref = getSharedPreferences("version", 0);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("version", version_code);
            editor.apply();

        }

    }

    private static class GetCalendrierFromGoogleStorage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Request request;
            // on recupere les images et les places dans bdd
            dbHelper.open();
            dbHelper.deleteImageCalendrier();

            OkHttpClient client = new OkHttpClient();

            /* ***** aout ****** */




            request = new Request.Builder()
                        .url(Constants.CALENDRIER_AOUT)
                        .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(8, response);
            } catch (IOException e) {
                e.printStackTrace();
            }


            request = new Request.Builder()
                    .url(Constants.CALENDRIER_SEPTEMBRE)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(9, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_OCTOBRE)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(10, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_NOVEMBRE)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(11, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_DECEMBRE)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(12, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_JANVIER)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(1, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_FEVRIER)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(2, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_MARS)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(3, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_AVRIL)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(4, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_MAI)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(5, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_JUIN)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(6, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_JUILLET)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(7, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

         // deuxième année

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_AOUT2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(13, response);
            } catch (IOException e) {
                e.printStackTrace();
            }


            request = new Request.Builder()
                    .url(Constants.CALENDRIER_SEPTEMBRE2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(14, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_OCTOBRE2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(15, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_NOVEMBRE2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(16, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_DECEMBRE2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(17, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_JANVIER2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(18, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_FEVRIER2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(19, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_MARS2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(20, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_AVRIL2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(21, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_MAI2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(22, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_JUIN2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(23, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            request = new Request.Builder()
                    .url(Constants.CALENDRIER_JUILLET2)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                dbHelper.insertCalendrierImage(24, response);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("version", version_code);
            editor.apply();
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
    }



}

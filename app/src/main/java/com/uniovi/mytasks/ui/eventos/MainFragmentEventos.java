package com.uniovi.mytasks.ui.eventos;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.uniovi.mytasks.DetailsActivity;
import com.uniovi.mytasks.FormularioActivity;
import com.uniovi.mytasks.FormularioEventos;
import com.uniovi.mytasks.ListaTareasAdapter;
import com.uniovi.mytasks.R;
import com.uniovi.mytasks.datos.TareasDataSource;
import com.uniovi.mytasks.modelo.Task;
import com.uniovi.mytasks.util.Lector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainFragmentEventos extends Fragment {

    public static final String TAREA_SELECCIONADA = "tarea_seleccionada";
    public static final String TAREA_DELETE = "tarea_delete";
    public final static int GESTION_TAREA = 1;
    public final static int MODIFICAR_TAREA = 2;
    public final static String TAREA_ADD = "tarea_add";
    private static final int RESULT_OK = -1;
    private static final int RESULT_CANCELED = 0;
    public static String usuario;
    List<Task> listaTareas;

    RecyclerView listaTareaView;

    private FloatingActionButton fABAdd;
    private FloatingActionButton fABEventos;
    private FloatingActionButton fABTareas;

    private View root;

    private Paint p = new Paint();
    //TODO Arreglar Animaciones
    //private final Animation rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
    //private final Animation rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
    //private final Animation fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
    //private final Animation toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

    private boolean clicked = false;


    TareasDataSource taskDataSource;
    private GoogleSignInClient mGoogleSignInClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = inflater.inflate(R.layout.activity_main_recycler_view, container, false);

        cargarTareasDB();

        listaTareaView = root.findViewById(R.id.recyclerView);
        listaTareaView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        listaTareaView.setLayoutManager(layoutManager);

        if(listaTareas != null && !listaTareas.isEmpty())
            introListaTareas();

        introListaTareas();
        enableSwipe();

        fABAdd = root.findViewById(R.id.botonAdd);
        fABAdd.setOnClickListener(view ->{
            onAddButtonClicked();
        });
        fABEventos = root.findViewById(R.id.fABEventos);
        fABEventos.setOnClickListener(view ->{
            crearNuevoEvento();

        });
        fABTareas = root.findViewById(R.id.fABTareas);
        fABTareas.setOnClickListener(view ->{
            crearNuevaTarea();
        });

        return root;
    }

    private void crearNuevaTarea(){
        Intent intent = new Intent(root.getContext(),FormularioActivity.class);
        startActivityForResult(intent,GESTION_TAREA);
    }

    private void crearNuevoEvento(){
        Intent intent = new Intent(root.getContext(),FormularioEventos.class);
        startActivityForResult(intent,GESTION_TAREA);
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(root.getContext());
    }

    private void onAddButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        if (!clicked)
            clicked = true;
        else
            clicked = false;
    }

    private void setAnimation(boolean clicked) {
        if(!clicked){
            //TODO Arreglar Animaciones
            //fABEventos.startAnimation(fromBottom);
            //fABEventos.startAnimation(fromBottom);
            //fABAdd.startAnimation(rotateOpen);
        }else{
            //fABEventos.startAnimation(toBottom);
            //fABEventos.startAnimation(toBottom);
            //fABAdd.startAnimation(rotateClose);
        }
    }

    private void setVisibility(boolean clicked) {
        if(!clicked){
            fABEventos.setVisibility(VISIBLE);
            fABTareas.setVisibility(VISIBLE);
        }else{
            fABEventos.setVisibility(INVISIBLE);
            fABTareas.setVisibility(INVISIBLE);
        }
    }

    private void cargarTareas(){
        listaTareas = new ArrayList<Task>();
        String result = Lector.leerDeJson(root.getContext(),"tareas.json");
        try{
            JSONObject jsonTareas = new JSONObject(result);
            JSONArray tareas = jsonTareas.getJSONArray("tareas");
            for(int i=0;i<tareas.length();i++){
                JSONObject tarea = tareas.getJSONObject(i);
                String titulo = tarea.getString("titulo");
                String descripcion = tarea.getString("descripcion");
                String fecha = tarea.getString("fecha");
                String hora = tarea.getString( "hora");

                listaTareas.add(new Task(titulo,descripcion,new SimpleDateFormat("dd/MM/yyyy").parse(fecha),new SimpleDateFormat("hh:mm").parse(hora)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }catch(ParseException p){
            p.printStackTrace();
        }
    }

    private void cargarTareasDB(){
        taskDataSource = new TareasDataSource(root.getContext());
        listaTareas = taskDataSource.getEventsByUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }
    //private void listaTareasAdapter(){}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GESTION_TAREA){
            if(resultCode == RESULT_OK){
                taskDataSource = new TareasDataSource(root.getContext());
                Task task = data.getParcelableExtra(TAREA_ADD);
                taskDataSource.createtask(task);
                cargarTareasDB();
                introListaTareas();
            }
        }else if(requestCode == MODIFICAR_TAREA){
            if(resultCode == RESULT_OK){
                Task task = data.getParcelableExtra(TAREA_DELETE);
                taskDataSource = new TareasDataSource(root.getContext());
            }
        }else if(resultCode == RESULT_CANCELED)
            Log.d("MyTasks.MainActivity","FormularioActivity cancelada");
    }

    private void introListaTareas(){
        ListaTareasAdapter ltAdapter = new ListaTareasAdapter(listaTareas, new ListaTareasAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(Task item) {
                clickOnItem(item);
            }
        });
        listaTareaView.setAdapter(ltAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void clickOnItem(Task tarea) {
        Intent intent = new Intent(root.getContext(), DetailsActivity.class);
        intent.putExtra(TAREA_SELECCIONADA, tarea);
        startActivityForResult(intent,MODIFICAR_TAREA);
    }

    private void addTarea(Task task){
        taskDataSource = new TareasDataSource(root.getContext());
        taskDataSource.createtask(task);
    }

    private void deleteTask(Task task){
        Integer i = null;
        for(Task t : listaTareas)
            if(t.getTitulo().equals(task.getTitulo()) && t.getFecha().equals(task.getFecha()) && t.getDescripcion().equals(task.getDescripcion()))
                i = listaTareas.indexOf(t);
        if(i !=null)
            listaTareas.remove(i.intValue());

        introListaTareas();
    }

    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if(direction == ( ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT )){
                    deleteTask(listaTareas.get(position).getId(),position);
                }else{
                    deleteTask(listaTareas.get(position).getId(),position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if(dX > 0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.deletetask);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listaTareaView);
    }

    private void deleteTask(String id,int position){
        listaTareas.remove(position);
        introListaTareas();

        taskDataSource.deleteItem(id);
    }
}

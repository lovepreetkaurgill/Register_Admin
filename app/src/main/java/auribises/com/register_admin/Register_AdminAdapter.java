package auribises.com.register_admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class Register_AdminAdapter extends ArrayAdapter<Register_Admin> {

    Context context;
    int resource;
    ArrayList<Register_Admin> registerteacherList,tempList;

    public Register_AdminAdapter(Context context, int resource, ArrayList<Register_Admin> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        registerteacherList = objects;
        tempList = new ArrayList<>();
        tempList.addAll(registerteacherList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource,parent,false);

        TextView txtName = (TextView)view.findViewById(R.id.textViewName);
        TextView txtGender = (TextView)view.findViewById(R.id.textViewGender);

        Register_Admin register_admin = registerteacherList.get(position);
        txtName.setText(register_admin.getName());
        //txtGender.setText(student.getGender());
        txtGender.setText(String.valueOf(register_admin.getId()));

        Log.i("Test", register_admin.toString());

        return view;
    }

    public void filter(String str){

        registerteacherList.clear();

        if(str.length()==0){
            registerteacherList.addAll(tempList);
        }else{
            for(Register_Admin r : tempList){
                if(r.getName().toLowerCase().contains(str.toLowerCase())){
                    registerteacherList.add(r);
                }
            }
        }

        notifyDataSetChanged();
    }
}

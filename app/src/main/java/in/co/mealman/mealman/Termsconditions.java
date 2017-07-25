package in.co.mealman.mealman;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Termsconditions extends Fragment implements View.OnClickListener {
ImageView rbackbutton;

    public Termsconditions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.termsconditions, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        listner();
    }

    private void initialize(View view) {
        rbackbutton=(ImageView)view.findViewById(R.id.rbackbutton);
    }

    private void listner() {
        rbackbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(getActivity(),Navigationbar.class);
        startActivity(i);
        getActivity().finish();
    }
}

package android.example.newsapitest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CryptoAdapter  extends ArrayAdapter<CryptoInfo> {

    public CryptoAdapter(@NonNull Context context, ArrayList<CryptoInfo> newitems) {
        super(context, 0, newitems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if the existing view is being reused?

        //View cryptolist = convertView;
        if (convertView == null ) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout, parent, false);
        }

        CryptoInfo currentnewscrypto = getItem(position);

        assert currentnewscrypto != null;
        TextView titleView = convertView.findViewById(R.id.tv1);
        titleView.setText(currentnewscrypto.getTitle());

      // TextView urlview = convertView.findViewById(R.id.urllink);
       // urlview.setText(currentnewscrypto.getUrl());

        TextView descView = convertView.findViewById(R.id.descview);
        descView.setText(currentnewscrypto.getDesc());

        TextView authorView = convertView.findViewById(R.id.authorview);
        authorView.setText(currentnewscrypto.getAuthor());


        return convertView;
    }
}

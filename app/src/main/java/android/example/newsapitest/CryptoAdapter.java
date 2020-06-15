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

        View cryptolist = convertView;
        if (cryptolist == null ) {
            cryptolist = LayoutInflater.from(getContext()).inflate(R.layout.custom_layout, parent, false);

        }

        CryptoInfo currentnewscrypto = getItem(position);

        TextView tv1 = cryptolist.findViewById(R.id.tv1);
        tv1.setText(currentnewscrypto.getTitle());


        return cryptolist;
    }
}

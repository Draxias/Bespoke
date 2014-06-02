package com.bespoke.zaghi;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {

	String classes[] = { "Personal Mode", "Conversation Mode", "Record Mode",
			"About" };
	String menuItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Menu.this,
				android.R.layout.simple_list_item_1, classes));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		switch (position) {
		case 0:
			menuItem = "PersonalMode";
			break;
		case 1:
			menuItem = "ConversationMode";
			break;
		case 2:
			menuItem = "RecordMode";
			break;
		case 3:
			menuItem = "About";
			break;
		default:
			break;

		}

		try {
			Class<?> ourClass = Class.forName("com.bespoke.zaghi." + menuItem);
			Intent ourIntent = new Intent(Menu.this, ourClass);
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}

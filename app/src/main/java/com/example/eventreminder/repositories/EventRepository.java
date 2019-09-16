package com.example.eventreminder.repositories;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.os.AsyncTask;
import android.provider.CalendarContract;

import com.example.eventreminder.daos.EventsDao;
import com.example.eventreminder.models.event.EventList;
import com.example.eventreminder.viewModels.EventViewModel;

import java.util.List;

public class EventRepository implements EventsDao {


    // Projection array. Creating indices for this array instead of doing
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,            // 0
            CalendarContract.Events.TITLE,          // 1
            CalendarContract.Events.DESCRIPTION,    // 2
            CalendarContract.Events.EVENT_LOCATION, // 3
            CalendarContract.Events.DTSTART,        // 4
            CalendarContract.Events.DTEND,          // 5
            String.valueOf(CalendarContract.Events.AVAILABILITY_BUSY), // 6
    };

    // The indices for the projection array above.
    private static final int PROJECTION_EVENT_ID_INDEX = 0;
    private static final int PROJECTION_EVENT_TITLE_INDEX = 1;
    private static final int PROJECTION_EVENT_DESCRIPTION_INDEX = 2;
    private static final int PROJECTION_EVENT_LOCATION_INDEX = 3;
    private static final int PROJECTION_EVENT_DSTART_INDEX = 4;
    private static final int PROJECTION_EVENT_DTEND_INDEX = 5;
    private static final int PROJECTION_EVENT_AVAILABILITY_BUSY_INDEX = 6;
    private final  Application application ;
    private String userAccountName;
    List<EventList> events = null;
    EventViewModel eventViewModel;

    public EventRepository(Application application){
        this.application = application;
        getUserAccount();
    }

    private void getUserAccount() {

        // TODO check if it's in shared preferences
        // else get User's google Accoount
        AccountManager accountManager = AccountManager.get(application);
        Account[] accounts = accountManager.getAccounts();
        int count = accounts.length;
        Account account = null;
        for (int i = 0; i < count; i++) {
            account = accounts[i];
            if (account.type.equals("com.google")) {
                userAccountName = account.name;
            }
        }
    }

    @Override
    public List<EventList> getAllEvents() {
        new GetAllEventsAsyncTask(application).execute();
        return events;
    }

   // Get All User's Events AsynckTask
    private class GetAllEventsAsyncTask extends AsyncTask<Void, Void, List<EventList>> {

        private  Application application;

        public GetAllEventsAsyncTask(Application application){
            this.application = application;
        }

       @Override
       protected List<EventList> doInBackground(Void...voids) {
           return events;
       }

       @Override
       protected void onPostExecute(List<EventList> events) {
           super.onPostExecute(events);
//           Toast.makeText(application, events.get(0).getTitle(), Toast.LENGTH_SHORT).show();
       }
   }
}

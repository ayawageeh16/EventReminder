package com.example.eventreminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventreminder.R;
import com.example.eventreminder.models.event.Attendee;
import com.example.eventreminder.models.event.EventAttendessDTO;
import com.example.eventreminder.models.event.EventsModelDTO;
import com.example.eventreminder.models.event.Item;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventsViewHolder> {

    private EventsModelDTO events;
    private OnItemClickListener listener;
    private Context context;

    public EventsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        holder.bind(events.getItems().get(position), events.getUserEmail());
    }

    @Override
    public int getItemCount() {
        return events.getItems().size();
    }

    public void setEvents(EventsModelDTO events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public Item getNoteAtPosition(int position) {
        return events.getItems().get(position);
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {

        private TextView eventTitleTextView;
        private TextView eventDescriptionTextView;
        private TextView eventTimeTextView;
        private TextView eventStatusTextView;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);

            eventTitleTextView = itemView.findViewById(R.id.event_title_tv);
            eventDescriptionTextView = itemView.findViewById(R.id.event_description_tv);
            eventTimeTextView = itemView.findViewById(R.id.event_time_tv);
            eventStatusTextView = itemView.findViewById(R.id.event_status_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        EventAttendessDTO eventAttendessDTO = new EventAttendessDTO(events.getItems().get(position), events.getUserEmail());
                        listener.onItemClick(eventAttendessDTO);
                    }
                }
            });

        }

        public void bind(Item event, String userEmail) {

            eventTitleTextView.setText(event.getSummary());
            eventDescriptionTextView.setText(event.getDescription());
            String date = event.getStart().getDate();
            if(date!=null && !date.isEmpty()) {
                eventTimeTextView.setText(date);
            }else{
                eventTimeTextView.setText(getDate(event.getStart().getDateTime()));
            }

            if(userEmail.equals(event.getCreator().getEmail())){
                eventStatusTextView.setText(event.getStatus());
            }else{
                for(Attendee attendee: event.getAttendees()) {
                    if(attendee.getEmail().equals(userEmail)){
                        eventStatusTextView.setText(attendee.getResponseStatus());
                    }
                }
            }
        }

        private String getDate(String originalString){
            if(originalString !=null){
                return originalString.split("T")[0];
            }
            return "Not specified";
        }

    }

    public interface OnItemClickListener {
        void onItemClick(EventAttendessDTO event);
    }

}

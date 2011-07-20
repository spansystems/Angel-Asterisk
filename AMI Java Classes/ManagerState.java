import java.beans.PropertyChangeEvent;
import java.io.IOException;

import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.event.NewStateEvent;


public interface ManagerState {
        public void onPropertyChangeEvent(PropertyChangeEvent event, ManagerServer server) throws IllegalArgumentException, IllegalStateException, IOException, TimeoutException;
        
        public void onNewStateEvent(NewStateEvent event, ManagerServer server);

		public void doParkCall(ManagerServer managerServer);

		public void redirectToConference(ManagerServer managerServer);

		
  }

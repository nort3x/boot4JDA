package me.nort3x.b4j.core.aspects.adaptors;

import net.dv8tion.jda.api.events.Event;

public class ResponseSender {
    Event event;
    Object retVal;

    public ResponseSender(Event event, Object response) {
        this.event = event;
        this.retVal = response;
    }

    public Event getEvent() {
        return event;
    }

    public Object getRetVal() {
        return retVal;
    }



    public void sendResponse(){
        Response response = new Response(retVal);
        if(!response.isSupported()) throw new IllegalStateException("bad response type");

        RetrieveMessageFromEvent retrieveMessageFromEvent = new RetrieveMessageFromEvent(event);
        if(retrieveMessageFromEvent.canRetrieveMessage()){
            response.replayResponse(retrieveMessageFromEvent.getMessage());
            return;
        }

        MessageChannelAnalyzer messageChannelRetriever = new MessageChannelAnalyzer(event);
        if(messageChannelRetriever.isSupported()){
            response.sendResponse(messageChannelRetriever.getChannel(),event);
            return;
        }

        throw new IllegalStateException("unsupported event to response to!");
    }
}

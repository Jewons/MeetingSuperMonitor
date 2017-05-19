package cn.butel.MeetingSuperMonitor.facade.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

@Provider
public class MessageBodyWriterJSON extends JacksonJsonProvider
{
    public MessageBodyWriterJSON()
    {
    }

    @Override
    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType)
    {
        ObjectMapper mapper = super.locateMapper(type, mediaType);
//      mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
        return mapper;
    }
}

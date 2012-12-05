package org.drools.kproject.models;

import java.util.ArrayList;
import java.util.List;

import org.drools.core.util.AbstractXStreamConverter;
import org.kie.builder.KieBaseModel;
import org.kie.builder.KieSessionModel;
import org.kie.builder.ListenerModel;
import org.kie.builder.WorkItemHandlerModel;
import org.kie.runtime.conf.ClockTypeOption;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class KieSessionModelImpl
        implements
        KieSessionModel {
    private String                           name;

    private String                           type = "stateful";
    private ClockTypeOption                  clockType = ClockTypeOption.get( "realtime" );

    private KieBaseModelImpl kBase;

    private final List<ListenerModel> listeners = new ArrayList<ListenerModel>();
    private final List<WorkItemHandlerModel> wihs = new ArrayList<WorkItemHandlerModel>();

    private KieSessionModelImpl() { }

    public KieSessionModelImpl(KieBaseModelImpl kBase, String name) {
        this.kBase = kBase;
        this.name = name;
    }
    
    public KieBaseModelImpl getKieBaseModel() {
        return kBase;
    }
    
    public void setKBase(KieBaseModel kieBaseModel) {
        this.kBase = (KieBaseModelImpl) kieBaseModel;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieSessionModel#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieSessionModel#setName(java.lang.String)
     */
    public KieSessionModel setName(String name) {
        this.name = name;
        return this;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieSessionModel#getType()
     */
    public String getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieSessionModel#setType(java.lang.String)
     */
    public KieSessionModel setType(String type) {
        this.type = type;
        return this;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieSessionModel#getClockType()
     */
    public ClockTypeOption getClockType() {
        return clockType;
    }

    /* (non-Javadoc)
     * @see org.kie.kproject.KieSessionModel#setClockType(org.kie.runtime.conf.ClockTypeOption)
     */
    public KieSessionModel setClockType(ClockTypeOption clockType) {
        this.clockType = clockType;
        return this;
    }

    public ListenerModel newListenerModel(String type, ListenerModel.Kind kind) {
        ListenerModelImpl listenerModel = new ListenerModelImpl(this, type, kind);
        listeners.add(listenerModel);
        return listenerModel;
    }

    public List<ListenerModel> getListenerModels() {
        return listeners;
    }

    private void addListenerModel(ListenerModel listener) {
        listeners.add(listener);
    }

    public WorkItemHandlerModel newWorkItemHandelerModel(String type) {
        WorkItemHandlerModelImpl wihModel = new WorkItemHandlerModelImpl(this, type);
        wihs.add(wihModel);
        return wihModel;
    }

    public List<WorkItemHandlerModel> getWorkItemHandelerModels() {
        return wihs;
    }

    private void addWorkItemHandelerModel(WorkItemHandlerModel wih) {
        wihs.add(wih);
    }

    @Override
    public String toString() {
        return "KieSessionModel [name=" + name + ", clockType=" + clockType + "]";
    }

    public static class KSessionConverter extends AbstractXStreamConverter {

        public KSessionConverter() {
            super(KieSessionModelImpl.class);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            KieSessionModelImpl kSession = (KieSessionModelImpl) value;
            writer.addAttribute("name", kSession.getName());
            writer.addAttribute("type", kSession.getType());
            if (kSession.getClockType() != null) {
                writer.addAttribute("clockType", kSession.getClockType().getClockType());
            }
            for (ListenerModel listener : kSession.getListenerModels()) {
                writeObject(writer, context, listener.getKind().toString(), listener);
            }
            for (WorkItemHandlerModel wih : kSession.getWorkItemHandelerModels()) {
                writeObject(writer, context, "workItemHandler", wih);
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader, final UnmarshallingContext context) {
            final KieSessionModelImpl kSession = new KieSessionModelImpl();
            kSession.setName(reader.getAttribute("name"));
            kSession.setType(reader.getAttribute("type"));

            String clockType = reader.getAttribute("clockType");
            if (clockType != null) {
                kSession.setClockType(ClockTypeOption.get(clockType));
            }

            readNodes( reader, new AbstractXStreamConverter.NodeReader() {
                public void onNode(HierarchicalStreamReader reader,
                                   String name,
                                   String value) {
                    if ( "agendaEventListener".equals( name ) || "workingMemoryEventListener".equals( name ) || "processEventListener".equals( name ) ) {
                        ListenerModelImpl listener = readObject(reader, context, ListenerModelImpl.class);
                        listener.setKSession( kSession );
                        listener.setKind(ListenerModel.Kind.fromString(name));
                        kSession.addListenerModel(listener);
                    } else if ( "workItemHandler".equals( name ) ) {
                        WorkItemHandlerModelImpl wih = readObject(reader, context, WorkItemHandlerModelImpl.class);
                        wih.setKSession( kSession );
                        kSession.addWorkItemHandelerModel(wih);
                    }
                }
            } );
            return kSession;
        }
    }
}
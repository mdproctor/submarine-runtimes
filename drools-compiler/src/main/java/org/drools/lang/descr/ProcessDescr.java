package org.drools.lang.descr;

import org.drools.rule.Dialectable;
import org.drools.rule.Namespaceable;
import org.kie.io.Resource;

public class ProcessDescr extends BaseDescr
    implements
    Dialectable {

    private String dialect;
    private String name;
    private String className;
    private Resource resource;

    public ProcessDescr() { }

    public ProcessDescr(String name) {
        this.name = name;
    }
    
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDialect() {
        return this.dialect;
    }

}

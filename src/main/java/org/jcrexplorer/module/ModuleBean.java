package org.jcrexplorer.module;

import org.jcrexplorer.BackingBean;
import org.jcrexplorer.ContentBean;

public class ModuleBean extends BackingBean {
    
    protected ContentBean contentBean;
    
    public ContentBean getContentBean() {
	return contentBean;
    }
    
    public void setContentBean(ContentBean contentBean) {
	this.contentBean = contentBean;
    }

}

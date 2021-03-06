package com.ade.open.api.server.persistent.auto;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.exp.Property;

import com.ade.open.api.server.persistent.TOpenSecret;

/**
 * Class _TOpenSecretVm was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _TOpenSecretVm extends CayenneDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String ID_PK_COLUMN = "ID";

    public static final Property<String> INNER_CODE = Property.create("innerCode", String.class);
    public static final Property<TOpenSecret> SECRET = Property.create("secret", TOpenSecret.class);

    public void setInnerCode(String innerCode) {
        writeProperty("innerCode", innerCode);
    }
    public String getInnerCode() {
        return (String)readProperty("innerCode");
    }

    public void setSecret(TOpenSecret secret) {
        setToOneTarget("secret", secret, true);
    }

    public TOpenSecret getSecret() {
        return (TOpenSecret)readProperty("secret");
    }


}

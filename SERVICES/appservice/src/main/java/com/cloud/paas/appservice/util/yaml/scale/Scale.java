package com.cloud.paas.appservice.util.yaml.scale;
import com.cloud.paas.appservice.util.yaml.common.ObjectBase;

/**
 * @Author: srf
 * @desc: Scale对象
 * @Date: Created in 2018-03-27 09-07
 * @Modified By:
 */
public class Scale extends ObjectBase{
    /**
     * matedata
     */
    private ScaleMeta metadata;
    /**
     * deployment 描述
     */
    private ScaleSpec spec;

    public ScaleMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ScaleMeta metadata) {
        this.metadata = metadata;
    }

    public ScaleSpec getSpec() {
        return spec;
    }

    public void setSpec(ScaleSpec spec) {
        this.spec = spec;
    }
}


package com.cloud.paas.appservice.util.yaml.ingress;

import java.util.List;

/**
 * @Author: wyj
 * @desc: ingress http对象
 * @Date: Created in 2018-01-04 9:44
 * @Modified By:
 */
public class IngressHttp {

    /**
     * ingress路径配置集合
     */
    private List<IngressPaths> paths;

    public List<IngressPaths> getPaths() {
        return paths;
    }

    public void setPaths(List<IngressPaths> paths) {
        this.paths = paths;
    }
}


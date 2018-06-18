package com.cloud.paas.appservice.util.yaml.ingress;

import java.util.List;

/**
 * @Author: wyj
 * @desc: ingress详细描述
 * @Date: Created in 2018-01-03 18:53
 * @Modified By:
 */
public class IngressSpec {

    /**
     * ingress规则集
     */
    private List<IngressRules> rules;

    public List<IngressRules> getRules() {
        return rules;
    }

    public void setRules(List<IngressRules> rules) {
        this.rules = rules;
    }
}

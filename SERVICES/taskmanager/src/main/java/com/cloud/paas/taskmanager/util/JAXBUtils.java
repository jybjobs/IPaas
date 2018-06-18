package com.cloud.paas.taskmanager.util;

/**
 * @Author: srf
 * @desc: JAXBUtils对象
 * @Date: Created in 2018-04-21 17:14
 * @Modified By:
 */

import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringWriter;

public class JAXBUtils {
    private static final JAXBContext getJAXBContext(Class<?> c){
        JAXBContext jaxbContext=null;
        try {
            jaxbContext = JAXBContext.newInstance(c);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return jaxbContext;
    }
    /**
     * 将实体类转序列化为对应String类型xml节点
     * @param obj
     * @return
     */
    public static final String modelToStringXML(Object obj){
        StringWriter writer= new StringWriter();
        JAXBContext jaxbContext = getJAXBContext(obj.getClass());
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            //设置序列化的编码格式
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            //设置格式化输出
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}

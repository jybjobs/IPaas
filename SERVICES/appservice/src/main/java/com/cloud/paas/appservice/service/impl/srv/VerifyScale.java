package com.cloud.paas.appservice.service.impl.srv;

import com.cloud.paas.appservice.dao.SrvMngDAO;
import com.cloud.paas.appservice.model.SrvDetail;
import com.cloud.paas.exception.BusinessException;
import com.cloud.paas.util.codestatus.CodeStatusUtil;
import com.cloud.paas.util.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: srf
 * @desc: VerifyScale对象
 * @Date: Created in 2018-03-28 09-57
 * @Modified By:
 */
@Service
public class VerifyScale {
    private static final Logger logger = LoggerFactory.getLogger(VerifyScale.class);
    @Autowired
    SrvMngDAO srvMngDAO;

    public SrvDetail createScaleVerify(SrvDetail srvDetail) throws BusinessException {
        Result result = CodeStatusUtil.resultByCodeEn("SCALE_FAILURE");
        //1.服务存在校验，服务名只允许使用英文服务名或中文服务名中的一个
        logger.info("服务存在校验");
        String nameEn = srvDetail.getSrvNameEn();
        String nameCh = srvDetail.getSrvNameCh();
        SrvDetail srvOrigin;
        if ((nameEn != null) && (nameEn != "")) {
            srvOrigin = srvMngDAO.findNameEn(nameEn);
        } else if ((nameCh != null) && (nameCh != "")){
            srvOrigin = srvMngDAO.findNameZh(nameCh);
        } else {
            result.setMessage("请输入服务名");
            throw new BusinessException(result);
        }
        if (srvOrigin == null) {
            result.setMessage("服务不存在");
            throw new BusinessException(result);
        }

        // 2.判断是实例个数是否正确
        logger.info("判断是实例个数是否正确");
//        Byte targetNum = srvDetail.getSrvInstNum();
//        Byte originNum = srvOrigin.getSrvInstNum();
//        if ((targetNum == null) || (targetNum < 0) || (targetNum == originNum)) {
//            result.setMessage("实例个数必须为与原始实例个数不同的自然数");
//            throw new BusinessException(result);
//        }
//        srvOrigin.setSrvInstNum(srvDetail.getSrvInstNum());
        return srvOrigin;
    }
}

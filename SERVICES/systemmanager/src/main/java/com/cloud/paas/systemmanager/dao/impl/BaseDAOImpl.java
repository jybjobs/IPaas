package com.cloud.paas.systemmanager.dao.impl;


import com.cloud.paas.systemmanager.dao.BaseDAO;
import com.cloud.paas.systemmanager.model.ValueObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: wangzhifeng
 * @Description:
 * @Date: Create in 16:56 2017/11/2
 * @Modified by:
 */
public abstract class BaseDAOImpl<T extends ValueObject>  implements BaseDAO<T> {

	@Autowired
	protected SqlSessionTemplate sqlSessionTemplate;

	public abstract String getNameSpace();

	public int doInsertByBean(T bean){
		return sqlSessionTemplate.insert(this.getNameSpace()+".doInsertByBean", bean);

	}

	public int doBatchInsertByList(List<T> list){
		return sqlSessionTemplate.insert(this.getNameSpace()+".doBatchInsertByList", list);

	}

	public int doInsertByMap(Map<String,Object> param){
		return sqlSessionTemplate.insert(this.getNameSpace()+".doInsertByMap", param);

	}

	public int doDeleteById(T bean){
		return sqlSessionTemplate.delete(this.getNameSpace()+".doDeleteById",bean);
	}


	public int doDeleteById(int id){
		return sqlSessionTemplate.delete(this.getNameSpace()+".doDeleteById",id);
	}

	public int doDeleteByMap(Map<String,Object> param){

		return sqlSessionTemplate.delete(this.getNameSpace()+".doDeleteByMap",param);
	}


	public int doUpdateByBean(T bean){
		return sqlSessionTemplate.update(this.getNameSpace()+".doUpdateByBean", bean);
	}

	public int doBatchUpdateByList(List<T> list){
		return sqlSessionTemplate.update(this.getNameSpace()+".doBatchUpdateByList", list);
	}

	public int doUpdateByMap(Map<String,Object> param){
		return sqlSessionTemplate.update(this.getNameSpace()+".doUpdateByMap", param);
	}


	public T doFindById(int id){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindById", id);
	}

	public T doFindByBean(T bean){
		return sqlSessionTemplate.selectOne(this.getNameSpace()+".doFindByBean", bean);
	}

	public List<T> doSearchListByBean(T bean){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".doSearchListByBean",bean);
	}


	public List<T> doSearchListByMap(Map<String,Object> param){
		return sqlSessionTemplate.selectList(this.getNameSpace()+".doSearchListByMap",param);
	}

}

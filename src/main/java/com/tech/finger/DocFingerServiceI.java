package com.tech.finger;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.List;
@WebService(targetNamespace = "http://www.xd-tech.com")
public interface DocFingerServiceI {
	/***
	 * 计算一个文档，在flag范围内的信息指纹，并且返回是否重复
	 * @param dataid
	 * 			数据标识，必填，唯一的标识数据
	 * @param fieldList
	 * @param flag
	 * @return
	 */
	@WebResult(name = "contentFinger", targetNamespace = "http://www.xd-tech.com")
	ContentFinger fingerValue(@WebParam(name = "dataid", targetNamespace = "http://www.xd-tech.com")String dataid,@WebParam(name = "fieldList", targetNamespace = "http://www.xd-tech.com") List<DocField> fieldList,
			@WebParam(name = "flag", targetNamespace = "http://www.xd-tech.com") String flag) ;
}
